package com.numberONe.controller.app.json;

public class DonationResult {
	private String id;
	private String createTime;
	private String donationTitle;//主题
	private String donationContent;//捐赠物
	private String donationTelephone;//联系方式
	private String donationInstruction;//描述
	private String donationType;//类型 1-金钱 2 物品
	private String openId;//捐赠人openid
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDonationTitle() {
		return donationTitle;
	}
	public void setDonationTitle(String donationTitle) {
		this.donationTitle = donationTitle;
	}
	public String getDonationContent() {
		return donationContent;
	}
	public void setDonationContent(String donationContent) {
		this.donationContent = donationContent;
	}
	public String getDonationTelephone() {
		return donationTelephone;
	}
	public void setDonationTelephone(String donationTelephone) {
		this.donationTelephone = donationTelephone;
	}
	public String getDonationInstruction() {
		return donationInstruction;
	}
	public void setDonationInstruction(String donationInstruction) {
		this.donationInstruction = donationInstruction;
	}
	public String getDonationType() {
		return donationType;
	}
	public void setDonationType(String donationType) {
		this.donationType = donationType;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
}
