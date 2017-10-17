package com.numberONe.controller.app;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.numberONe.controller.app.wx.IWXPayDomain;
import com.numberONe.controller.app.wx.WXPayConfig;
import com.numberONe.controller.app.wx.WXPayDomain;

public class WxPayConfig extends WXPayConfig{

	private byte[] certData;

    public WxPayConfig() throws Exception {
        String certPath = "C:/cert/apiclient_cert.p12";
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public String getAppID() {
        return "wxf06843f92dd264e0";
    }

    public String getMchID() {
        return "1487775882";
    }

    public String getKey() {
        return "nihaoaworinilaomu123uijsdfsfuiuijkfsdfsdfas";
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

	@Override
	public IWXPayDomain getWXPayDomain() {
		
		return new WXPayDomain();
	}
}
