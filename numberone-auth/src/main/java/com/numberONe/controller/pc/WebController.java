package com.numberONe.controller.pc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.numberONe.entity.UserFormMap;
import com.numberONe.mapper.UserMapper;

@RestController  
public class WebController {  
	
	@Inject
	private UserMapper userMapper;
	
    @RequestMapping("/index.htm")  
    public void hello1(HttpServletRequest req, HttpServletResponse res,String param1, String param2) throws IOException {  
    	UserFormMap userFormMap = userMapper.findbyFrist("id", "1", UserFormMap.class);
    	System.out.println(userFormMap.get("id"));
        
    }  
    
	private PrintWriter setHttpRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String method = req.getMethod();
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html; charset=utf-8");
		return res.getWriter();
	}
} 


