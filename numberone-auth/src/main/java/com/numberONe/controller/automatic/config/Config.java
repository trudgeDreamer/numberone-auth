package com.numberONe.controller.automatic.config;

import java.io.File;

public class Config {
	
	//其他工程项目配置
	public static boolean OTHER_PROJECT = false;
	//实体Bean对象所在目录
	public static String BEAN_URL = "D:\\testMaven\\numberone-auth\\src\\main\\java\\com\\numberONe\\entity\\";
	
	
	private String encode = "UTF-8";  // 字符编码
	public static String LINE_SEPARATOR = System.getProperty("line.separator"); //换行符
	
	private String baseDir = System.getProperty("user.dir") + File.separator + "DatabaseReverser" + File.separator;  // 文件生成保存的根目录
	
	private String databaseType = ""; // 数据库类型 mysql oracle等
	private String driverName = "";  // 数据库驱动名
	private String url = "";  // 数据库连接地址
	private String username = "";
	private String password = "";
	private String queryTableNamesSql = "";  // 查询所有表名的SQL语句，musql和oracle可不设置，系统自动判断
	
	private String tablePrefix = "";  // 表前缀
	private boolean deleteTablePrefix = false;  // 是否删除表前缀
	
	private boolean generateEntityAnnotation = false;  // 是否生成实体类对应的注解
	private boolean generateEntityRowMapperFile = false;  // 是否生成实体类对应的RowMapper文件
	
	private boolean generateBaseTemplateFile = false;  // 是否生成基础action、service、dao文件
	private String templateDir;  // 模板所在目录
	private String actionTemplateName = "ActionTemplate.java";  // action模板名称
	private String serviceInterfaceTemplateName = "ServiceInterfaceTemplate.java";  // serviceInterface模板名称
	private String serviceImplTemplateName = "ServiceImplTemplate.java";  // serviceImpl模板名称
	private String daoInterfaceTemplateName = "DaoInterfaceTemplate.java";  // daoInterface模板名称
	private String daoImplTemplateName = "DaoImplTemplate.java";  // daoImpl模板名称
	
	private String newActionFileName = "#REPLACE_ENTITY_NAME#Action.java";  // action模板名称
	private String newServiceInterfaceFileName = "I#REPLACE_ENTITY_NAME#Service.java";  // serviceInterface模板名称
	private String newServiceImplFileName = "#REPLACE_ENTITY_NAME#ServiceImpl.java";  // serviceImpl模板名称
	private String newDaoInterfaceFileName = "I#REPLACE_ENTITY_NAME#Dao.java";  // daoInterface模板名称
	private String newDaoImplFileName = "#REPLACE_ENTITY_NAME#DaoJdbcImpl.java";  // daoImpl模板名称
	
	// getter setter
	
	public String getEncode() {
		return encode;
	}
	
	public void setEncode(String encode) {
		this.encode = encode;
	}
	
	/**
	 * 文件生成保存的根目录
	 * @return
	 */
	public String getBaseDir() {
		return baseDir;
	}
	
	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
		this.templateDir = baseDir + File.separator + "template";  // 设置默认模板目录
	}
	
	public String getDriverName() {
		return driverName;
	}
	
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 查询所有表名的SQL语句，musql和oracle可不设置，系统自动判断
	 * @return
	 */
	public String getQueryTableNamesSql() {
		return queryTableNamesSql;
	}

	/**
	 * 查询所有表名的SQL语句，musql和oracle可不设置，系统自动判断
	 * @return
	 */
	public void setQueryTableNamesSql(String queryTableNamesSql) {
		this.queryTableNamesSql = queryTableNamesSql;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	public String getTemplateDir() {
		return templateDir;
	}

	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}

	public String getActionTemplateName() {
		return actionTemplateName;
	}

	public void setActionTemplateName(String actionTemplateName) {
		this.actionTemplateName = actionTemplateName;
	}

	public String getServiceInterfaceTemplateName() {
		return serviceInterfaceTemplateName;
	}

	public void setServiceInterfaceTemplateName(String serviceInterfaceTemplateName) {
		this.serviceInterfaceTemplateName = serviceInterfaceTemplateName;
	}

	public String getServiceImplTemplateName() {
		return serviceImplTemplateName;
	}

	public void setServiceImplTemplateName(String serviceImplTemplateName) {
		this.serviceImplTemplateName = serviceImplTemplateName;
	}

	public String getDaoInterfaceTemplateName() {
		return daoInterfaceTemplateName;
	}

	public void setDaoInterfaceTemplateName(String daoInterfaceTemplateName) {
		this.daoInterfaceTemplateName = daoInterfaceTemplateName;
	}

	public String getDaoImplTemplateName() {
		return daoImplTemplateName;
	}

	public void setDaoImplTemplateName(String daoImplTemplateName) {
		this.daoImplTemplateName = daoImplTemplateName;
	}

	public boolean isGenerateBaseTemplateFile() {
		return generateBaseTemplateFile;
	}

	public void setGenerateBaseTemplateFile(boolean generateBaseTemplateFile) {
		this.generateBaseTemplateFile = generateBaseTemplateFile;
	}

	/**
	 * 是否生成实体类注解
	 * @return
	 */
	public boolean isGenerateEntityAnnotation() {
		return generateEntityAnnotation;
	}

	/**
	 * 设置是否生成实体类注解
	 * @return
	 */
	public void setGenerateEntityAnnotation(boolean generateEntityAnnotation) {
		this.generateEntityAnnotation = generateEntityAnnotation;
	}

	public String getNewActionFileName() {
		return newActionFileName;
	}

	/**
	 * 设置要保存的Action名称
	 * @param newActionFileName 新名称，可用变量：#REPLACE_ENTITY_NAME#，表示实体类名
	 */
	public void setNewActionFileName(String newActionFileName) {
		this.newActionFileName = newActionFileName;
	}

	public String getNewServiceInterfaceFileName() {
		return newServiceInterfaceFileName;
	}

	/**
	 * 设置要保存的Service接口名称
	 * @param newServiceInterfaceFileName 新名称，可用变量：#REPLACE_ENTITY_NAME#，表示实体类名
	 */
	public void setNewServiceInterfaceFileName(String newServiceInterfaceFileName) {
		this.newServiceInterfaceFileName = newServiceInterfaceFileName;
	}

	public String getNewServiceImplFileName() {
		return newServiceImplFileName;
	}

	/**
	 * 设置要保存的Service实现类名称
	 * @param newServiceImplFileName 新名称，可用变量：#REPLACE_ENTITY_NAME#，表示实体类名
	 */
	public void setNewServiceImplFileName(String newServiceImplFileName) {
		this.newServiceImplFileName = newServiceImplFileName;
	}

	public String getNewDaoInterfaceFileName() {
		return newDaoInterfaceFileName;
	}

	/**
	 * 设置要保存的Dao接口名称
	 * @param newDaoInterfaceFileName 新名称，可用变量：#REPLACE_ENTITY_NAME#，表示实体类名
	 */
	public void setNewDaoInterfaceFileName(String newDaoInterfaceFileName) {
		this.newDaoInterfaceFileName = newDaoInterfaceFileName;
	}

	public String getNewDaoImplFileName() {
		return newDaoImplFileName;
	}

	/**
	 * 设置要保存的Dao实现类名称
	 * @param newDaoImplFileName 新名称，可用变量：#REPLACE_ENTITY_NAME#，表示实体类名
	 */
	public void setNewDaoImplFileName(String newDaoImplFileName) {
		this.newDaoImplFileName = newDaoImplFileName;
	}

	public String getTablePrefix() {
		return tablePrefix;
	}

	/**
	 * 设置表前缀
	 * @param tablePrefix
	 */
	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}

	public boolean isDeleteTablePrefix() {
		return deleteTablePrefix;
	}

	/**
	 * 设置是否删除表前缀
	 * @param deleteTablePrefix
	 */
	public void setDeleteTablePrefix(boolean deleteTablePrefix) {
		this.deleteTablePrefix = deleteTablePrefix;
	}

	public boolean isGenerateEntityRowMapperFile() {
		return generateEntityRowMapperFile;
	}

	public void setGenerateEntityRowMapperFile(boolean generateEntityRowMapperFile) {
		this.generateEntityRowMapperFile = generateEntityRowMapperFile;
	}
	
}
