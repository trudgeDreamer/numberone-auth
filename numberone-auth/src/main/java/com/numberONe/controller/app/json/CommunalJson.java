package com.numberONe.controller.app.json;

public class CommunalJson extends JsonBean{


	private Object data;
	
	public CommunalJson(String result, String msg, Object data) {
		super(result,msg);
		this.data = data;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
