package com.numberONe.controller.automatic.dbtable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.numberONe.controller.automatic.config.Config;

public class DBUtil {

	
	/*
	 * 连接数据库获取所有表信息
	 */
	public List<Table> getAllTables(Config config) {
		List<Table> tables = new ArrayList<Table>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName(config.getDriverName());
			con = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
			// 获取所有表名
			String showTablesSql = "";
			if(!"".equals(config.getQueryTableNamesSql().trim())) {
				showTablesSql = config.getQueryTableNamesSql();
			} else if(config.getDriverName().toLowerCase().indexOf("mysql")!=-1) {
				showTablesSql = "show tables";  // MySQL查询所有表格名称命令
			} else if(config.getDriverName().toLowerCase().indexOf("sqlserver")!=-1) {
				showTablesSql = "SELECT TABLE_NAME FROM edp.INFORMATION_SCHEMA.TABLES Where TABLE_TYPE='BASE TABLE'";  // SQLServer查询所有表格名称命令
			} else if(config.getDriverName().toLowerCase().indexOf("oracle")!=-1) {
				showTablesSql = "select table_name from user_tables"; // ORACLE查询所有表格名称命令
			}
			
			ps = con.prepareStatement(showTablesSql);
			rs = ps.executeQuery();
			
			// 循环生成所有表的表信息
			while(rs.next()) {
				if(rs.getString(1)==null) continue;
				tables.add(getTable(rs.getString(1).trim(), con,config));
			}
			
			rs.close();
			ps.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tables;
	}
	
	/*
	 * 获取指定表信息并封装成Table对象
	 * @param tableName
	 * @param con
	 */
	public Table getTableByTableName(String tableName,Config config){
		Table table = new Table();
		try {
			Connection con = null;
			Class.forName(config.getDriverName());
			con = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
			
			table.setTableName(config.isDeleteTablePrefix() ? tableName.replaceFirst(config.getTablePrefix(), "") : tableName);
			
			PreparedStatement ps = con.prepareStatement(" SELECT * FROM "+tableName);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			
			int columCount = rsmd.getColumnCount();
			for(int i=1; i<=columCount; i++) {
				table.getColumNames().add(rsmd.getColumnName(i).trim());
				table.getColumTypes().add(rsmd.getColumnTypeName(i).trim());
			}
			
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}
	
	/*
	 * 获取指定表信息并封装成Table对象
	 * @param tableName
	 * @param con
	 */
	private Table getTable(String tableName, Connection con,Config config) throws SQLException {
		Table table = new Table();
		table.setTableName(config.isDeleteTablePrefix() ? tableName.replaceFirst(config.getTablePrefix(), "") : tableName);
		
		PreparedStatement ps = con.prepareStatement(" SELECT * FROM "+tableName);
		ResultSet rs = ps.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		
		int columCount = rsmd.getColumnCount();
		for(int i=1; i<=columCount; i++) {
			table.getColumNames().add(rsmd.getColumnName(i).trim());
			table.getColumTypes().add(rsmd.getColumnTypeName(i).trim());
		}
		
		rs.close();
		ps.close();
		
		return table;
	}
}
