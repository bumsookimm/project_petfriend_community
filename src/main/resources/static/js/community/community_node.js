const express = require('express');
const http = require('http');
const socketIo = require('socket.io');
const oracledb = require('oracledb');

oracledb.initOracleClient({ libDir: 'C:\\sts4\\instantclient_12_1' });

const app = express();
const server = http.createServer(app);
const io = require('socket.io')(server, {
	cors: {
		origin: "http://localhost:9002",
		methods: ["GET", "POST"]
	}
});

// 현재 접속 중인 사용자 목록 (socket.id 기준)
const activeUsers = {};

async function connectToDB() {
	let connection;
	try {
		connection = await oracledb.getConnection({
			user: 'albamon',
			password: '123456',
			connectString: '172.30.1.71:1521/XE',
		});
		console.log("DB 연결 성공");
		return connection;
	} catch (err) {
		console.error("DB 연결 실패:", err);
		return null;
	}
}


io.on('connection', (socket) => {
	console.log('새로운 사용자 연결됨:', socket.id);

	// 사용자 닉네임 설정
	socket.on('setNickname', (nickname) => {
		if (!nickname) {
			console.log(`닉네임이 설정되지 않음: ${socket.id}`);
			return;
		}

		activeUsers[socket.id] = nickname;
		console.log(`✅ 사용자 추가: ${nickname} (${socket.id})`);

		// 현재 활성 사용자 목록 출력
		console.log('📌 현재 활성 사용자 목록:', Object.values(activeUsers));

		// 모든 사용자에게 업데이트된 활성 사용자 목록 전송
		io.emit('updateUsers', Object.values(activeUsers));
	});

	socket.on('getChatHistory', async () => {
		const connection = await connectToDB();
		if (!connection) {
			console.log("❌ DB 연결 실패로 메시지 불러오기 불가");
			return;
		}

		const sql = `
	        SELECT SENDER, MESSAGE FROM (
	            SELECT * FROM COMMUNITY_ALL_CHAT ORDER BY ALL_CHAT_ID DESC
	        ) WHERE ROWNUM <= 20
	    `;

		try {
			const result = await connection.execute(sql);
			console.log('쿼리 결과:', result); // 쿼리 결과 확인

			if (result.rows) {
				// 각 행에서 sender와 message를 추출해 새로운 배열로 변환
				const chatMessages = result.rows.reverse().map(row => ({
					sender: row[0],  // SENDER는 첫 번째 컬럼
					message: row[1]   // MESSAGE는 두 번째 컬럼
				}));
				socket.emit('loadChatHistory', chatMessages); // 클라이언트로 메시지 전송
			} else {
				console.log('메시지가 없습니다.');
				socket.emit('loadChatHistory', []); // 빈 배열을 보냄
			}
		} catch (err) {
			console.error("메시지 불러오기 실패:", err);
		} finally {
			connection.close();
		}
	});

	// 메시지 전송 이벤트
	socket.on('sendMessage', async (messageData) => {
		const connection = await connectToDB();
		if (!connection) {
			console.log("❌ DB 연결 실패로 메시지 전송 불가");
			return;
		}

		const { sender, message } = messageData;
		const sql = `INSERT INTO COMMUNITY_ALL_CHAT (ALL_CHAT_ID, SENDER, MESSAGE)
	                 VALUES (all_chat_id_seq.NEXTVAL, :sender, :message)`;

		try {
			await connection.execute(sql, [sender, message], { autoCommit: true });

			// 새 메시지를 모든 사용자에게 전송
			io.emit('newMessage', { sender, message });
		} catch (err) {
			console.error("메시지 전송 실패:", err);
		} finally {
			connection.close();
		}
	});

	// 사용자 연결 해제 시 처리
	socket.on('disconnect', () => {
		console.log(`🚪 사용자 연결 끊김: ${socket.id}`);

		if (activeUsers[socket.id]) {
			console.log(`❌ 사용자 제거: ${activeUsers[socket.id]} (${socket.id})`);
			delete activeUsers[socket.id];

			// 현재 활성 사용자 목록 출력
			console.log('📌 현재 활성 사용자 목록:', Object.values(activeUsers));

			// 모든 사용자에게 업데이트된 활성 사용자 목록 전송
			io.emit('updateUsers', Object.values(activeUsers));
		}
	});
});

// 서버 시작
server.listen(3004, () => {
	console.log('서버가 3004번 포트에서 실행 중...');
});
