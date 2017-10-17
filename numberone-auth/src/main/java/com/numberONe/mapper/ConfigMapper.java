package com.numberONe.mapper;

import java.util.List;

import com.numberONe.entity.ConfigFormMap;
import com.numberONe.mapper.base.BaseMapper;


public interface ConfigMapper extends BaseMapper{

	public List<ConfigFormMap> findByPage(ConfigFormMap configFormMap);
	
}
