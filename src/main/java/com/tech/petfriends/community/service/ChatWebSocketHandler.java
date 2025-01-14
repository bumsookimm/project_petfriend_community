package com.tech.petfriends.community.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;


public class ChatWebSocketHandler extends TextWebSocketHandler {
    private static Map<String, WebSocketSession> sessions = new HashMap<>();
    private CChatService cChatService; // 서비스 의존성 주입
  
    public ChatWebSocketHandler(CChatService cChatService) {
        this.cChatService = cChatService;
    }
    
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String jsonData = message.getPayload();
        
        ObjectMapper mapper = new ObjectMapper();
        
        Map<String, Object> map = mapper.readValue(jsonData, Map.class);
        String sender = (String) map.get("sender");
        String receiver = (String) map.get("receiver");
        String message_content = (String) map.get("message_content");
        String room_id = (String) map.get("roomId");
        
        System.out.println(sender + " || " + receiver + " || " + message_content + " || " + room_id);
        // 메시지 저장
        cChatService.saveMessage(sender, receiver, message_content, room_id);
    	
    	
    	
    	for (WebSocketSession sess : sessions.values()) {
            if (sess.isOpen()) {
                sess.sendMessage(message); // 모든 연결된 세션에 메시지를 브로드캐스트
            }
        }
    }

   
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
    }


}
