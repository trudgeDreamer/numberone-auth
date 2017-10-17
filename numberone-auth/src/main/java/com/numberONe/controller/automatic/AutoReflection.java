package com.numberONe.controller.automatic;

import com.numberONe.controller.automatic.config.Config;


public class AutoReflection {

	private Config config;  // 配置类, 必须
	private String LINE_SEPARATOR = System.getProperty("line.separator"); //换行符
	
	public AutoReflection(Config config) {
		this.config = config;
	}
	
	
	
}
