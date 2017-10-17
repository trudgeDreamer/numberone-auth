package com.numberONe.controller.app.wx;

public class WXPayDomain implements IWXPayDomain{
	public final String domain ="";
	public long elapsedTimeMillis;
	public final Exception ex = null;
	public void report(String domain, long elapsedTimeMillis, Exception ex) {
		domain = "http://www.gzshhuzhu.com/numberone-auth/weixin/pay/";
		elapsedTimeMillis= 6*1000;
		ex = new Exception();
	}

	public DomainInfo getDomain(WXPayConfig config) {
		DomainInfo domainInfo = new DomainInfo("http://www.gzshhuzhu.com/numberone-auth/",false);
		return domainInfo;
	}

}
