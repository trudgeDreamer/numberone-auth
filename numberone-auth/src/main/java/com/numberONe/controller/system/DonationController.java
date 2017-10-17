package com.numberONe.controller.system;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.io.Resources;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.numberONe.mapper.AutoCodeMapper;
import com.numberONe.mapper.ConfigMapper;
import com.numberONe.mapper.LogMapper;
import com.numberONe.mapper.UserMapper;
import com.numberONe.annotation.SystemLog;
import com.numberONe.controller.automatic.AutomaticMain;
import com.numberONe.controller.automatic.config.Config;
import com.numberONe.controller.automatic.dbtable.Table;
import com.numberONe.controller.index.BaseController;
import com.numberONe.entity.AutoCodeFormMap;
import com.numberONe.entity.ConfigFormMap;
import com.numberONe.entity.ResFormMap;
import com.numberONe.entity.ResUserFormMap;
import com.numberONe.entity.UserFormMap;
import com.numberONe.entity.UserGroupsFormMap;
import com.numberONe.exception.SystemException;
import com.numberONe.plugin.PageView;
import com.numberONe.util.Common;
import com.numberONe.util.DateUtils;
import com.numberONe.util.JsonUtils;
import com.numberONe.util.POIUtils;
import com.numberONe.util.PasswordHelper;
import com.numberONe.util.PropertiesUtils;

/**
 * 
 * @author numberONe 2014-11-19
 * @version 3.0v
 */
@Controller
@RequestMapping("/donation/")
public class DonationController extends BaseController {
	@Inject
	private AutoCodeMapper autoCodeMapper;
	@Inject
	private AutomaticMain automaticMain;
	
	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/donation/list";
	}
	@ResponseBody
	@RequestMapping("autoBean")
	public String autoBean() throws Exception {
		String[] ids = getParaValues("ids");
		for(String id: ids){
			AutoCodeFormMap formMap = autoCodeMapper.findbyFrist("id", id, AutoCodeFormMap.class);
			String tableName = formMap.get("table_name").toString();
			Table table = automaticMain.getTableByTableName(tableName);
			automaticMain.generateEntityFile(table);
		}
		return "success";
	}

	
	@ResponseBody
	@RequestMapping("findByPage")
	public PageView findByPage( String pageNow,
			String pageSize,String column,String sort) throws Exception {
		AutoCodeFormMap autoCodeFormMap = getFormMap(AutoCodeFormMap.class);
		autoCodeFormMap=toFormMap(autoCodeFormMap, pageNow, pageSize,autoCodeFormMap.getStr("orderby"));
		autoCodeFormMap.put("column", column);
		autoCodeFormMap.put("sort", sort);
        pageView.setRecords(autoCodeMapper.findByPage(autoCodeFormMap));//不调用默认分页,调用自已的mapper中findUserPage
        return pageView;
	}
	
	
	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			AutoCodeFormMap autoCode = autoCodeMapper.findbyFrist("id", id, AutoCodeFormMap.class);
			byte[] blob = autoCode.getBytes("CONTENT");
			String t = new String(blob,"utf-8");//bytep[]转换为String
			model.addAttribute("CONTENT", t);
			model.addAttribute("autoCode", autoCode);
		}
		return Common.BACKGROUND_PATH + "/system/automatic/edit";
	}
	
}