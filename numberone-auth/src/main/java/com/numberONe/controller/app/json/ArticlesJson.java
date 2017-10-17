package com.numberONe.controller.app.json;


public class ArticlesJson {

	private String id;
	private String title;
	private String content;
	private String dateTime;
	private String source;//来源
	private String num;//查看 次数
	private String userName;
	private String iocUrl;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIocUrl() {
		return iocUrl;
	}
	public void setIocUrl(String iocUrl) {
		this.iocUrl = iocUrl;
	}
}
