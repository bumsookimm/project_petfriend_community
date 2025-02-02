const express = require('express');
const http = require('http');
const socketIo = require('socket.io');
const oracledb = require('oracledb');


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
  try {
    const connection = await oracledb.getConnection({
      user: 'albamon',
      password: '123456',
      connectString: '172.30.1.100:1521/XE',
    });
    return connection;
  } catch (err) {
    console.error("DB 연결 실패:", err);
  }
}

// 실시간 전체 채팅 기능
let activeUsers = []; // 로그인된 사용자 목록

io.on('connection', (socket) => {
  console.log('새로운 사용자 연결됨:', socket.id);

  // 전체채팅 방에 참여한 사용자를 activeUsers에 추가
  socket.on('joinChat', async () => {
    const connection = await connectToDB();
    
    // 모든 유저 목록을 가져옴
    const result = await connection.execute('SELECT mem_nick FROM USERS');
    activeUsers = result.rows.map(row => row[0]);

    // 모든 사용자에게 새로운 유저 목록을 브로드캐스트
    io.emit('updateUsers', activeUsers);
    
    connection.close();
  });

  // 메시지 전송
  socket.on('sendMessage', async (messageData) => {
    const connection = await connectToDB();
    
    // 메시지를 DB에 저장
    const { sender, message } = messageData;
    const sql = `INSERT INTO COMMUNITY_All_CHAT (SENDER, MESSAGE) VALUES (:sender, :message)`;
    await connection.execute(sql, [sender, message], { autoCommit: true });
    
    // 모든 사용자에게 메시지 전송
    io.emit('newMessage', { sender, message });

    connection.close();
  });

  // 연결 끊기
  socket.on('disconnect', () => {
    console.log('사용자 연결 끊김:', socket.id);
  });
});

// 서버 시작
server.listen(3000, () => {
  console.log('서버가 3000번 포트에서 실행 중...');
});