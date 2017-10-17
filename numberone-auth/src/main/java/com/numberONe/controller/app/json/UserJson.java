package com.numberONe.controller.app.json;


public class UserJson {

	private String id;
	private String userName;
	private String  sex;
	private String cityArea;
	private String headimgurl;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCityArea() {
		return cityArea;
	}
	public void setCityArea(String cityArea) {
		this.cityArea = cityArea;
	}
}
