package com.tech.petfriends.community.service.interfaces;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

public interface CServiceMRInterface {
	  Map<String, Object> execute(HttpServletRequest request, Model model); 
	
}
