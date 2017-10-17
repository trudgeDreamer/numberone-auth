package com.numberONe.controller.automatic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Controller;

import com.numberONe.controller.automatic.config.Config;
import com.numberONe.controller.automatic.dbtable.DBUtil;
import com.numberONe.controller.automatic.dbtable.Table;
import com.numberONe.entity.TemplateFormMap;
import com.numberONe.util.PropertiesUtils;

@Controller
public class AutomaticMain {
	
	private  Config config;
	
	public List<Table> tableList(){
		if(config == null && !Config.OTHER_PROJECT){
			Properties props = initDBConfig();
			config = new Config();
			config.setDriverName(props.getProperty("jdbc.driverClass"));
			config.setUrl(props.getProperty("jdbc.url"));
			config.setUsername(props.getProperty("jdbc.username"));
			config.setPassword(props.getProperty("jdbc.password"));
		}else{
			Properties props = initDBConfig();
			String driverName = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://127.0.0.1:3306/numberone-auth?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true";
			String userName = "root";
			String password = "root";
			config = new Config();
			config.setDriverName(driverName);
			config.setUrl(url);
			config.setUsername(userName);
			config.setPassword(password);
		}
		return gainDbTables();
	}
	
	private Properties initDBConfig(){
		return PropertiesUtils.getjdbcProperties();
	}
	
	/**查询数据库表**/
	private List<Table> gainDbTables(){
		return new DBUtil().getAllTables(config);
	}
	
	/**查询数据库表**/
	public Table getTableByTableName(String tableName){
		return new DBUtil().getTableByTableName(tableName,config);
	}
	
	/*
	 * 表名转换为驼峰命名
	 */
	private String convertToCamelCase(String str) {
		StringBuffer result = new StringBuffer();
		String[] strArr = str.trim().split("_");
		for(String s : strArr) {
			if(s.length()>1) {
				result.append(s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase());
			} else {
				result.append(s.toUpperCase());
			}
		}
		if(!Config.OTHER_PROJECT){
			result.append("FormMap");
		}else{
			result.append("Bean");
		}
		return result.toString();
	}
	
	/*
	 * 表名转换为首字母小写的驼峰命名
	 */
	private String convertToFirstLetterLowerCaseCamelCase(String str) {
		String resultCamelCase = convertToCamelCase(str);

		String result = "";
		if(resultCamelCase.length()>1) {
			result = resultCamelCase.substring(0, 1).toLowerCase() + resultCamelCase.substring(1);
		} else {
			result = resultCamelCase.toLowerCase();
		}
		
		return result;
	}
	
	/*
	 * 将数据库的数据类型转换为java的数据类型
	 */
	private String convertType(String databaseType) {
		String javaType = "";
		
		String databaseTypeStr = databaseType.trim().toLowerCase();
		if(databaseTypeStr.equals("int")) {
			javaType = "Integer";
		} else if(databaseTypeStr.equals("char")) {
			javaType = "String";
		} else if(databaseTypeStr.equals("number")) {
			javaType = "Integer";
		} else if(databaseTypeStr.indexOf("varchar")!=-1) {
			javaType = "String";
		} else if(databaseTypeStr.equals("blob")) {
			javaType = "Byte[]";
		} else if(databaseTypeStr.equals("float")) {
			javaType = "Float";
		} else if(databaseTypeStr.equals("double")) {
			javaType = "Double";
		} else if(databaseTypeStr.equals("decimal")) {
			javaType = "BigDecimal";
		} else if(databaseTypeStr.equals("bigint")) {
			javaType = "Long";
		} else if(databaseTypeStr.equals("date")) {
			javaType = "String";
		} else if(databaseTypeStr.equals("time")) {
			javaType = "String";
		} else if(databaseTypeStr.equals("datetime")) {
			javaType = "String";
		} else if(databaseTypeStr.equals("timestamp")) {
			javaType = "String";
		} else if(databaseTypeStr.equals("year")) {
			javaType = "String";
		} else {
			javaType = "[unconverted]" + databaseType;
		}
		
		return javaType;
	}
	
	//生成无参构造方法
	private String getConstructorStr(String entityName){
		return "\t" + "public " + entityName + "() {}" + Config.LINE_SEPARATOR ;  // 无参构造方法
	}
	
	/*
	 * 生成指定表对象对应的类文件
	 * @param table
	 */
	public void generateEntityFile(Table table) {
		
		String tableName = table.getTableName();
		List<String> columNames = table.getColumNames();//列名称
		List<String> columTypes = table.getColumTypes();//列类型
		
		if(!Config.OTHER_PROJECT){
			String entityName = convertToCamelCase(tableName);// 表名对应的实体类名
			String entitySaveDir = config.BEAN_URL;
			System.out.println(entitySaveDir);
			File entityFile = new File(entitySaveDir+entityName+".java");
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(entityFile)));
				bw.flush();
				bw.close();
			} catch (Exception e) {
				System.out.println("生成类文件("+entityName+")出错！");
				e.printStackTrace();
			} 
		}else{
			String entityName = convertToCamelCase(tableName);// 表名对应的实体类名
			String constructorStr = getConstructorStr(entityName); // 无参构造方法
			
			// 生成重写的toString方法
			String propertyName = "";  // 属性名
			StringBuffer toStringStr = new StringBuffer(); 
			
			toStringStr.append("\tpublic String toString() { " + Config.LINE_SEPARATOR + 
					"\t\treturn ");  // toString方法
			for(int i=0; i<columNames.size(); i++) {
				propertyName = convertToFirstLetterLowerCaseCamelCase(columNames.get(i));
				if(i==0) {
					toStringStr.append("\"" + propertyName + ":\" + " + propertyName);
				} else {
					toStringStr.append("\", " + propertyName + ":\" + " + propertyName);
				}
				if((i+1)!=columNames.size()) {
					toStringStr.append(" + ");
				}
			}
			toStringStr.append(";" + Config.LINE_SEPARATOR + "\t}" + Config.LINE_SEPARATOR);
			
			// 生成私有属性和get、set方法
			String propertiesStr = "";  // 私有属性字符串
			String getterSetterStr = "";  // get、set方法字符串
			String getterAnnotation = "";  // 申明在getter方法上的属性与数据表列对应的注解
			String getterSetterPropertyNameStr = "";  
			String javaType = "";  // 数据库对应的java类型
			for(int i=0; i<columNames.size(); i++) {
				String columName = columNames.get(i);
				String columType = columTypes.get(i);
				
				propertyName = convertToFirstLetterLowerCaseCamelCase(columName);
				getterSetterPropertyNameStr = convertToCamelCase(columName);
				javaType = convertType(columType);
				
				propertiesStr += "\t" + "private " + javaType + " " + propertyName + ";" + Config.LINE_SEPARATOR;
				
				if(config.isGenerateEntityAnnotation())
					getterAnnotation = Config.LINE_SEPARATOR + "\t" + "@Column(name = \""+ columName +"\")" + Config.LINE_SEPARATOR;
				
				getterSetterStr += getterAnnotation +
						"\t" + "public " + javaType + " get" + getterSetterPropertyNameStr + "() {" + Config.LINE_SEPARATOR +
						"\t\t" + "return this." + propertyName + ";" + Config.LINE_SEPARATOR +
						"\t}" + Config.LINE_SEPARATOR + Config.LINE_SEPARATOR +
						"\t" + "public void set" + getterSetterPropertyNameStr + "(" + javaType + " " + propertyName + ") {" + Config.LINE_SEPARATOR +
						"\t\t" + "this." + propertyName + " = " + propertyName + ";" +  Config.LINE_SEPARATOR + Config.LINE_SEPARATOR +
						"\t}" + Config.LINE_SEPARATOR + Config.LINE_SEPARATOR;
			}
			
			// 生成实体类文件
			String entitySaveDir = config.getBaseDir() + File.separator + "src" + File.separator + "entity" + File.separator;
			System.out.println(entitySaveDir);
			File folder = new File(entitySaveDir);
			if(!folder.exists()) {
				folder.mkdirs();
			}
			
			String realTableName = config.isDeleteTablePrefix()? config.getTablePrefix()+tableName : tableName;
			File entityFile = new File(entitySaveDir+entityName+".java");
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(entityFile)));
				if(config.isGenerateEntityAnnotation())
					bw.write("@Entity"+Config.LINE_SEPARATOR+"@Table(name = \""+realTableName+"\")"+Config.LINE_SEPARATOR);
				bw.write("public class "+entityName + " {" +Config.LINE_SEPARATOR);
				bw.write(propertiesStr);
				bw.write(Config.LINE_SEPARATOR);
				bw.write(constructorStr);
				bw.write(Config.LINE_SEPARATOR);
				bw.write(toStringStr.toString());
				bw.write(Config.LINE_SEPARATOR);
				bw.write(getterSetterStr);
				bw.write(Config.LINE_SEPARATOR);
				bw.write("}");
				bw.flush();
				bw.close();
			} catch (Exception e) {
				System.out.println("生成类文件("+entityName+")出错！");
				e.printStackTrace();
			} 
		}
	}

}
