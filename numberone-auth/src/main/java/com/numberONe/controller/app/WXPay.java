package com.numberONe.controller.app;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.numberONe.controller.app.json.WechatMetaData;
import com.numberONe.controller.app.wx.WXPayConfig;
import com.numberONe.controller.app.wx.WXPayConstants;
import com.numberONe.controller.app.wx.WXPayConstants.SignType;
import com.numberONe.controller.app.wx.WXPayRequest;
import com.numberONe.controller.app.wx.WXPayUtil;
import com.numberONe.util.HttpClientUtil;
import com.numberONe.util.OrderUtil;


public class WXPay {
	

    private WXPayConfig config;
    private SignType signType = SignType.MD5;
    private boolean autoReport;
    private boolean useSandbox;
    private String notifyUrl = "http:/www.gzshhuzhu.com/numberone-auth/weixin/pay/order_pay_return.do";
    private WXPayRequest wxPayRequest;

  
    public WXPay(WXPayConfig config) {
		super();
		this.config = config;
		wxPayRequest = new WXPayRequest(config);
	}


	/**
     * 作用：统一下单<br>
     * 场景：公共号支付、扫码支付、APP支付
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    public Map<String, String> unifiedOrder(Map<String, String> reqData) throws Exception {
        return this.unifiedOrder(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }


    /**
     * 作用：统一下单<br>
     * 场景：公共号支付、扫码支付、APP支付
     * @param reqData 向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs 读超时时间，单位是毫秒
     * @return API返回数据
     * @throws Exception
     */
    public Map<String, String> unifiedOrder(Map<String, String> reqData,  int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_UNIFIEDORDER_URL_SUFFIX;
        }
        else {
            url = WXPayConstants.UNIFIEDORDER_URL_SUFFIX;
        }
        if(this.notifyUrl != null) {
            reqData.put("notify_url", this.notifyUrl);
        }
        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }
    
    /**
     * 向 Map 中添加 appid、mch_id、nonce_str、sign_type、sign <br>
     * 该函数适用于商户适用于统一下单等接口，不适用于红包、代金券接口
     *
     * @param reqData
     * @return
     * @throws Exception
     */
    public Map<String, String> fillRequestData(Map<String, String> reqData) throws Exception {
        reqData.put("appid", config.getAppID());
        reqData.put("mch_id", config.getMchID());
        reqData.put("nonce_str", WXPayUtil.generateUUID());
        if (SignType.MD5.equals(this.signType)) {
            reqData.put("sign_type", WXPayConstants.MD5);
        }
        else if (SignType.HMACSHA256.equals(this.signType)) {
            reqData.put("sign_type", WXPayConstants.HMACSHA256);
        }
        reqData.put("sign", WXPayUtil.generateSignature(reqData, config.getKey(), this.signType));
        return reqData;
    }
    
    /**
     * 不需要证书的请求
     * @param urlSuffix String
     * @param reqData 向wxpay post的请求数据
     * @param connectTimeoutMs 超时时间，单位是毫秒
     * @param readTimeoutMs 超时时间，单位是毫秒
     * @return API返回数据
     * @throws Exception
     */
    public String requestWithoutCert(String urlSuffix, Map<String, String> reqData,
                                     int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String msgUUID = reqData.get("nonce_str");
        String reqBody = WXPayUtil.mapToXml(reqData);

        String resp = this.wxPayRequest.requestWithoutCert(urlSuffix, msgUUID, reqBody, connectTimeoutMs, readTimeoutMs, autoReport);
        return resp;
    }
    
    /**
     * 处理 HTTPS API返回数据，转换成Map对象。return_code为SUCCESS时，验证签名。
     * @param xmlStr API返回的XML格式数据
     * @return Map类型数据
     * @throws Exception
     */
    public Map<String, String> processResponseXml(String xmlStr) throws Exception {
        String RETURN_CODE = "return_code";
        String return_code;
        Map<String, String> respData = WXPayUtil.xmlToMap(xmlStr);
        if (respData.containsKey(RETURN_CODE)) {
            return_code = respData.get(RETURN_CODE);
        }
        else {
            throw new Exception(String.format("No `return_code` in XML: %s", xmlStr));
        }

        if (return_code.equals(WXPayConstants.FAIL)) {
            return respData;
        }
        else if (return_code.equals(WXPayConstants.SUCCESS)) {
           if (this.isResponseSignatureValid(respData)) {
               return respData;
           }
           else {
               throw new Exception(String.format("Invalid sign value in XML: %s", xmlStr));
           }
        }
        else {
            throw new Exception(String.format("return_code value %s is invalid in XML: %s", return_code, xmlStr));
        }
    }
    
    /**
     * 判断xml数据的sign是否有效，必须包含sign字段，否则返回false。
     *
     * @param reqData 向wxpay post的请求数据
     * @return 签名是否有效
     * @throws Exception
     */
    public boolean isResponseSignatureValid(Map<String, String> reqData) throws Exception {
        // 返回数据的签名方式和请求中给定的签名方式是一致的
        return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), this.signType);
    }
    
    /**
     * 微信下单，创建预支付订单
     * @param reqParam
     */
    public static WechatMetaData wxPay(String payFee, String goodsInfo,String openId,String orderId){
    	
		WechatMetaData wechatMetaData = new WechatMetaData();
    	wechatMetaData.setAppid(WXPayConstants.APPID);
		wechatMetaData.setMch_id(WXPayConstants.MCHID);
		wechatMetaData.setAttach("支付测试");
		wechatMetaData.setNonce_str(WechatSdk.genRondomString());
		wechatMetaData.setOut_trade_no(orderId);
		BigDecimal big = new BigDecimal(payFee);
		wechatMetaData.setTotal_fee(big.multiply(new BigDecimal(100)).toBigInteger());
		if(StringUtils.isNotEmpty(goodsInfo)){
			wechatMetaData.setBody(goodsInfo);//商品信息
		}
		wechatMetaData.setSpbill_create_ip(WXPayConstants.SERVERIP);
		wechatMetaData.setNotify_url("http://wwww.baidu.com");
		wechatMetaData.setTrade_type(WXPayConstants.TRADE_TYPE_JSAPI);
		wechatMetaData.setOpenid("ooTAswd8fFJm5O5iowrLcK32Bzms");
		wechatMetaData.setOpenid(openId);
		try {
			wechatMetaData.setSign(WechatSdk.signByMD5(wechatMetaData,WXPayConstants.KEY));
		} catch (Exception e) {
			e.getMessage();
		}
		
		String string = WechatSdk.toXml(wechatMetaData);
		System.out.println(string);
		
		byte[] responseData;
		try {
			responseData = HttpClientUtil.httpsPost(WXPayConstants.PAY_URL, string);
			System.out.println(new String(responseData));
			WechatMetaData metaData = WechatSdk.parseXml(responseData);
			return metaData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
    }
    
    /**
     * 订单查询
     * @param orderId
     * @return
     */
    public static WechatMetaData wxOrderQuery(String orderId){
    	WechatMetaData wechatMetaData = new WechatMetaData();
    	wechatMetaData.setAppid(WXPayConstants.APPID);
		wechatMetaData.setMch_id(WXPayConstants.MCHID);
		wechatMetaData.setNonce_str(WechatSdk.genRondomString());
		wechatMetaData.setOut_trade_no(orderId);
		try {
			wechatMetaData.setSign(WechatSdk.signByMD5(wechatMetaData,WXPayConstants.KEY));
		} catch (Exception e) {
			e.getMessage();
		}
		
		String string = WechatSdk.toXml(wechatMetaData);
		System.out.println(string);
		
		byte[] responseData;
		try {
			responseData = HttpClientUtil.httpsPost(WXPayConstants.QUERY_URL, string);
			System.out.println(new String(responseData));
			WechatMetaData metaData = WechatSdk.parseXml(responseData);
			return metaData;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    public static void main(String[] args) {
//    	String orderId = OrderUtil.getOrderId();
//    	System.out.println(orderId);
//    	wxPay("100", "支付测试","ooTAswd8fFJm5O5iowrLcK32Bzms",orderId);
    	wxOrderQuery("20171014160857131963557");
	}
    
} // end class
