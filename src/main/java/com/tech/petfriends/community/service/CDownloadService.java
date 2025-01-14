package com.tech.petfriends.community.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tech.petfriends.community.mapper.IDao;
import com.tech.petfriends.community.service.interfaces.CServiceMInterface;
@Service
public class CDownloadService implements CServiceMInterface{

	private IDao iDao;
	
	public CDownloadService(IDao idao) {
		this.iDao = idao;
	}

	@Override
	public void execute(Model model) {
		 Map<String, Object> m = model.asMap();

	        HttpServletRequest request = (HttpServletRequest) m.get("request");
	        HttpServletResponse response = (HttpServletResponse) m.get("response");
	        String fname = request.getParameter("f");
	        String bid = request.getParameter("bid");
	        
	        // 파일 다운로드
	        try {
	            response.setHeader("Content-Disposition", "Attachment;filename=" 
	                    + URLEncoder.encode(fname, "utf-8"));
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } // 첨부, 한글 인코딩처리
	        
	        String workPath = System.getProperty("user.dir");
	        String realPath = workPath + "\\src\\main\\resources\\static\\images\\community_img\\" + fname;
	        System.out.println(realPath);
	        FileInputStream fin = null;
	        ServletOutputStream sout = null;
	        
	        try {
	            fin = new FileInputStream(realPath);
	            sout = response.getOutputStream();
	            
	            byte[] buf = new byte[1024];
	            int size = 0;
	            
	            while ((size = fin.read(buf, 0, 1024)) != -1) {
	                sout.write(buf, 0, size);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (fin != null) fin.close();
	                if (sout != null) sout.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
