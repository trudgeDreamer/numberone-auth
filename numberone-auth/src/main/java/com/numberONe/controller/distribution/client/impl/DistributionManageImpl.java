package com.numberONe.controller.distribution.client.impl;

import java.io.IOException;

import org.springframework.stereotype.Controller;

import com.numberONe.controller.distribution.client.DistributionManage;
import com.numberONe.controller.distribution.client.http.HttpDistributionService;
import com.numberONe.controller.distribution.client.page.PagingUtil;
import com.numberONe.util.PropertiesUtils;

@Controller
public class DistributionManageImpl implements DistributionManage {

	/**
	 * @param key KEY
	 * 验证签名Key
	 **/
	public String verifyKey(String key) {
		StringBuffer url = new StringBuffer();
		
		url.append(PropertiesUtils.getProperties().getProperty("distribution_url")).append("service/verifyKey.do").append("?key=").append(key);
		
		try {
			return  HttpDistributionService.doHttpGet(url.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public Integer addUser(String referralCode, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String addUser(String referralCode, String userId,
			String lastReferralCode, String lastUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String queryLastUser(String referralCode, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String queryNextUsers(String referralCode, String userId,
			PagingUtil pagingUtil) {
		// TODO Auto-generated method stub
		return null;
	}

	public String queryUser(String referralCode, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String queryDistributionProportion() {
		// TODO Auto-generated method stub
		return null;
	}

	public String setDistributionProportion(String onelevel,
			String secondLevel, String threeLevel) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
