package com.tech.petfriends.community.service.interfaces;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface CServiceRInterface {
	  Map<String, Object> execute(HttpServletRequest request); 
	
}
