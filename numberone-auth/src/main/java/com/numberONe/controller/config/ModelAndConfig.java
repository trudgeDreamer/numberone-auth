package com.numberONe.controller.config;

import org.springframework.ui.Model;

import com.numberONe.util.PropertiesUtils;

public class ModelAndConfig {
	
	public static void setModelConfig(Model model){
		model.addAttribute("HTML_TITLE", PropertiesUtils.getProperties().get("HTML_TITLE"));
	}
}
