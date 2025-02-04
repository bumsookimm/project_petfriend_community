const express = require('express');
const http = require('http');
const socketIo = require('socket.io');
const oracledb = require('oracledb');

oracledb.initOracleClient({ libDir: 'C:\\sts4\\instantclient_12_1' });

const app = express();
const server = http.createServer(app);
const io = require('socket.io')(server, {
  cors: {
    origin: "http://localhost:9002", // 허용할 클라이언트의 주소
    methods: ["GET", "POST"]
  }
});


io.on('connection', (socket) => {
  console.log('a user connected');
  socket.on('disconnect', () => {
    console.log('user disconnected');
  });
});
// Oracle DB 연결 함수
async function connectToDB() {
  let connection;
  try {
    connection = await oracledb.getConnection({
      user: 'albamon',
      password: '123456',
      connectString: '192.168.0.15:1521/XE',
    });
    console.log("DB 연결 성공");
    return connection;
  } catch (err) {
    console.error("DB 연결 실패:", err);
    return null; // 연결 실패 시 null 반환
  }
}

io.on('connection', (socket) => {
  console.log('새로운 사용자 연결됨:', socket.id);

  socket.on('joinChat', async () => {
    const connection = await connectToDB();
    
    if (!connection) {
      console.log("DB 연결 실패로 채팅 참여 불가");
      return;
    }

    // 모든 유저 목록을 가져옴
    const result = await connection.execute('SELECT SENDER FROM COMMUNITY_All_CHAT');
    activeUsers = result.rows.map(row => row[0]);

    // 모든 사용자에게 새로운 유저 목록을 브로드캐스트
    io.emit('updateUsers', activeUsers);
    
    connection.close();
  });

  socket.on('sendMessage', async (messageData) => {
    const connection = await connectToDB();
    
    if (!connection) {
      console.log("DB 연결 실패로 메시지 전송 불가");
      return;
    }

    // 메시지를 DB에 저장
    const { sender, message } = messageData;
	const sql = `INSERT INTO COMMUNITY_ALL_CHAT (ALL_CHAT_ID, SENDER, MESSAGE)
	             VALUES (all_chat_id_seq.NEXTVAL, :sender, :message)`;
    await connection.execute(sql, [sender, message], { autoCommit: true });
    
    // 모든 사용자에게 메시지 전송
    io.emit('newMessage', { sender, message });

    connection.close();
  });

  socket.on('disconnect', () => {
    console.log('사용자 연결 끊김:', socket.id);
  });
});

// 서버 시작
server.listen(3004, () => {
  console.log('서버가 3004번 포트에서 실행 중...');
});