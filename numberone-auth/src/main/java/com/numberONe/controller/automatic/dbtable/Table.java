package com.numberONe.controller.automatic.dbtable;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private String tableName;  // 表名
	private List<String> columNames = new ArrayList<String>(); // 列名集合
	private List<String> columTypes = new ArrayList<String>();  // 列类型集合，列类型严格对应java类型，如String不能写成string，与列名一一对应
	
	public String toString() {
		String tableStr = "";
		tableStr = tableStr + tableName + "\r\n";
		
		// 遍历列集合
		for(int i=0; i<columNames.size(); i++) {
			String columName = columNames.get(i);
			String columType = columTypes.get(i);
			tableStr +=  "  " + columName + ":  " + columType + "\r\n";
		}
		
		return tableStr;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getColumNames() {
		return columNames;
	}

	public void setColumNames(List<String> columNames) {
		this.columNames = columNames;
	}

	public List<String> getColumTypes() {
		return columTypes;
	}

	public void setColumTypes(List<String> columTypes) {
		this.columTypes = columTypes;
	}
}
