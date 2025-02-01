package com.tech.petfriends.community.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tech.petfriends.community.mapper.IDao;

	@Component // 이 클래스가 Spring 컨테이너에 의해 관리되는 빈이 되도록 함
	public class CCommunityScheduled {
	
		private final IDao iDao;
		private final RedisTemplate<String, String> redisTemplate;
	
		
		public CCommunityScheduled(IDao iDao, RedisTemplate<String, String> redisTemplate) {
	
			this.iDao = iDao;
			this.redisTemplate = redisTemplate;
		}
	
		// 매일 자정에 todayVisitKey 초기화
		@Scheduled(cron = "0 0 0 * * ?") // 1분마다 실행
		public void deleteVisitKeyEveryMinute() {
			 ScanOptions scanOptions = ScanOptions.scanOptions().match("*today*").count(100).build(); // 패턴 및 검색 수량 설정
			    Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(scanOptions);
			    
			    Set<String> keys = new HashSet<>();
			    while (cursor.hasNext()) {
			        keys.add(new String(cursor.next()));
			    }
	
			    System.out.println("keys: " + keys);
	
			    if (!keys.isEmpty()) {
			        redisTemplate.delete(keys); // 키 삭제
			        System.out.println("오늘 방문 기록 초기화 완료: " + keys.size() + "개의 키 삭제됨.");
			    } else {
			        System.out.println("삭제할 키가 없습니다.");
			    }
			}

	// 매일 자정에 방문자 수 초기화
	@Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
	public void resetDailyVisits() {
		iDao.resetDailyVisits(); // 방문자 수 초기화 로직
		System.out.println("1분마다 일일 방문자 수 초기화.");
	}
}
