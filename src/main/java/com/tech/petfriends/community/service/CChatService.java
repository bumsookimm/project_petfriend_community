package com.tech.petfriends.community.service;

import org.springframework.stereotype.Service;

import com.tech.petfriends.community.dto.CChatDto;
import com.tech.petfriends.community.mapper.IDao;

	@Service
	public class CChatService {
	
		private IDao iDao;
	
		public CChatService(IDao iDao) {
			this.iDao = iDao;
		}
	
		public void saveMessage(String sender, String receiver, String messageContent, String roomId) {
			CChatDto chatMessage = new CChatDto();
			chatMessage.setSender(sender);
			chatMessage.setReceiver(receiver);
			chatMessage.setMessage_content(messageContent);
			chatMessage.setMessage_content(roomId);
	
			System.out.println("sender:" + sender);
			System.out.println("receiver:" + receiver);
			System.out.println("messageContent:" + messageContent);
			System.out.println("roomId:" + roomId);
	
			iDao.insertMessage(sender, receiver, messageContent, roomId);
		  
			// 채팅방이 이미 존재하는지 확인하고 존재하지 않으면 삽입
		    boolean isRoomExist = iDao.isRoomExist(roomId);
		    if (!isRoomExist) {
		        iDao.insertChatRoom(sender, receiver, roomId);
		    }
			
		}
	
		public static void saveMessag(String sender, String receiver, String roomId) {
			// TODO Auto-generated method stub
	
		}
}
