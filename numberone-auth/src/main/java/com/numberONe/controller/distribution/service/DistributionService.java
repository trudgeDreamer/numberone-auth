package com.numberONe.controller.distribution.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.numberONe.entity.ConfigFormMap;
import com.numberONe.mapper.ConfigMapper;


@RestController  
public class DistributionService {  
	
	private static String loginKey = "";
	
	@Inject
	private ConfigMapper configMapper;
	
    @RequestMapping("/service/verifyKey.do")  
    public void hello1(HttpServletRequest req, HttpServletResponse res,String key) throws IOException {  
    	PrintWriter printWrter = setHttpRequest(req, res);// 设置编码规范
		StringBuffer returnXml = new StringBuffer();
		Integer status = CommonUtil.STATUS_400;//错误400
		if(!CommonUtil.isEmpty(key)){
			if(!CommonUtil.isEmpty(loginKey) && loginKey.equals(key)){
				
			}
			ConfigFormMap config = configMapper.findbyFrist("loginkey", key, ConfigFormMap.class);
			if(config != null){
				status =  CommonUtil.STATUS_200;
			}
		}
		returnXml.append(status);
		printWrter.println(returnXml.toString());
		return;
    	
    }  
    
	private PrintWriter setHttpRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String method = req.getMethod();
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html; charset=utf-8");
		return res.getWriter();
	}
} 


