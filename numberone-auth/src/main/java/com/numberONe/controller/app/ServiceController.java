package com.numberONe.controller.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.numberONe.controller.app.json.ArticlesJson;
import com.numberONe.controller.app.json.CommunalJson;
import com.numberONe.controller.app.json.DonationResult;
import com.numberONe.controller.app.json.JsonBean;
import com.numberONe.controller.app.json.UserJson;
import com.numberONe.controller.app.json.WechatMetaData;
import com.numberONe.controller.app.wx.WXPayConstants;
import com.numberONe.controller.index.BaseController;
import com.numberONe.entity.AutoCodeFormMap;
import com.numberONe.entity.DonationFormMap;
import com.numberONe.entity.HttpsX509TrustManager;
import com.numberONe.entity.OrderPayFromMap;
import com.numberONe.entity.UserFormMap;
import com.numberONe.entity.WeChatUser;
import com.numberONe.entity.WeixinOauth2Token;
import com.numberONe.mapper.AutoCodeMapper;
import com.numberONe.mapper.DonationMapper;
import com.numberONe.mapper.OrderMapper;
import com.numberONe.mapper.UserMapper;
import com.numberONe.util.Common;
import com.numberONe.util.DateUtils;
import com.numberONe.util.JsonUtils;
import com.numberONe.util.OrderUtil;

import net.sf.json.JSONObject;


@RestController  
public class ServiceController extends BaseController {
	
	@Inject
	private AutoCodeMapper autoCodeMapper;
	
	@Inject
	private DonationMapper donationMapper;
	
	@Inject
	private OrderMapper orderMapper;
	
	@Inject
	private UserMapper userMapper;
	
	private String appID = "wxf06843f92dd264e0";
	private String appSecret = "1b3c4e2bfae491ff2681b6f0a9c421dd";
	
	Map<String,String> numMap = new HashMap<String,String>();
	
	
	/**
	 * http://www.gzshhuzhu.com/numberone-auth/weixin/wx_Login.do
	 * @throws IOException 
	 **/
	@RequestMapping("/weixin/wx_Login.do")  
	public void wx_Login(HttpServletRequest req, HttpServletResponse res,String randomNum) throws IOException{
		numMap.put(randomNum, randomNum);
//		String redirectUrl = urlEnodeUTF8("http://www.gzshhuzhu.com/numberone-auth/weixin/wx_returnUrl.do?randomNum="+randomNum);
		String redirectUrl = urlEnodeUTF8("http://zengyq.free.ngrok.cc/numberone-auth/weixin/wx_returnUrl.do?randomNum="+randomNum);
		res.sendRedirect(getRequestCodeUrl(redirectUrl));// 跳转到要访问的地址
		return;
	}
	
	
	/**个人信息*
	 * http:/127.0.0.1:8080/numberone-auth/weixin/person_Info.do?randomNum=11112222
	 * */
	@RequestMapping("/weixin/person_Info.do")  
	public void person_Info(HttpServletRequest req, HttpServletResponse res,String randomNum) throws IOException{
		StringBuffer returnXml = new StringBuffer();
		UserFormMap formMap = userMapper.findbyFrist("randomNum",randomNum, UserFormMap.class);
		System.out.println(formMap);
		if(formMap.size() >= 1){
			UserJson userJson = new UserJson();
			userJson.setUserName(formMap.get("userName")+"");
			userJson.setCityArea(formMap.get("country")+""+formMap.get("province")+formMap.get("city"));
			userJson.setSex(formMap.get("sex")+"");
			userJson.setId(formMap.get("id")+"");
			userJson.setHeadimgurl(formMap.get("headimgurl")+"");
    		CommunalJson communalJson = new CommunalJson("200","成功",userJson);
    		returnXml.append(JsonUtils.beanToJson(communalJson));
    	}else{
    		JsonBean jsonBean = new JsonBean("201","没有数据");
    		returnXml.append(JsonUtils.beanToJson(jsonBean));
    	}
		 PrintWriter printWrter = setHttpRequest(req, res);// 设置编码规范
		 printWrter.println(returnXml.toString());
		 return;
	}
	
	/**创建订单，生成预支付订单
	 * http://localhost:8080/numberone-auth/weixin/create_order.do?randomNum=20171013113213479162&order_amount=10&descript=test&phone=15302377956&theme=test
	 * http:/www.gzshhuzhu.com/numberone-auth/weixin/create_order.do?randomNum=2017925232488942203&order_amount=10
	 * @throws IOException **/
	@RequestMapping("/weixin/create_order.do")  
	public JSONObject createOrder(HttpServletRequest req, HttpServletResponse res,String randomNum,String orderAmount) throws IOException{
		String phone = req.getParameter("phone");
		String theme = req.getParameter("theme");
		String descript = req.getParameter("descript");
		JSONObject jsonObject = new JSONObject();
		//参数校验
		if(StringUtils.isEmpty(phone)){
			jsonObject.put("returnCode", "ERROR");
			jsonObject.put("returnMsg", "请输入联系方式");
			return jsonObject;
		}
		if(randomNum != null && !"".equals(randomNum)){
			UserFormMap userFormMap = userMapper.findbyFrist("randomNum",randomNum, UserFormMap.class);
			if(userFormMap != null && userFormMap.size() >= 1){
				String orderId = OrderUtil.getOrderId();
				OrderPayFromMap formMap = getFormMap(OrderPayFromMap.class);
				formMap.put("donate_user", userFormMap.get("userName"));
				formMap.put("donate_randomNum", userFormMap.get("openId"));//捐赠人唯一标识
				formMap.put("order_id",orderId );
				formMap.put("order_amount", orderAmount);
				formMap.put("phone", phone);
				formMap.put("donate_theme",theme);
				formMap.put("descript", descript);
				formMap.put("order_time", DateUtils.getNow());
				try {
					this.orderMapper.addEntity(formMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//预下单
				WechatMetaData data = WXPay.wxPay(orderAmount, descript, userFormMap.getStr("openId"),orderId);
				
				if(null == data){
					jsonObject.put("returnCode","ERROR");
					jsonObject.put("returnMsg", "创建订单失败，请稍后重试！");
					return jsonObject;
				}
				formMap.put("order_status", data.getReturn_code());
				formMap.put("return_msg", data.getReturn_msg());
				formMap.put("prepay_id", data.getPrepay_id());
				
				try {
					this.orderMapper.editEntity(formMap);
					jsonObject.put("prepayId", data.getPrepay_id());
				} catch (Exception e) {
					System.out.println("数据更新失败！");
					e.printStackTrace();
					
				}
				jsonObject.put("returnCode", data.getReturn_code());
				jsonObject.put("returnMsg", data.getReturn_msg());
			}
			
		}else{
			jsonObject.put("returnCode", "ERROR");
			jsonObject.put("returnMsg", "参数错误");
			return jsonObject;
		}
		return jsonObject;
		
	}
	
	
	/**订单查询
	 * http:/www.gzshhuzhu.com/numberone-auth/weixin/order_pay.do?orderId=1506356282564
	 * @throws Exception 
	 * **/
	@RequestMapping("/weixin/order_pay.do")  
	public JSONObject orderPay(HttpServletRequest req, HttpServletResponse res,String orderId) {
		
		JSONObject jsonObject = new JSONObject();
		OrderPayFromMap orderFormMap = orderMapper.findbyFrist("order_id",orderId, OrderPayFromMap.class);
		System.out.println(orderFormMap.size());
		if(null != orderFormMap && orderFormMap.size() > 1){
			WechatMetaData metaData = WXPay.wxOrderQuery(orderId);
			if(null != metaData){
				jsonObject.put("returnCode", metaData.getReturn_code());
				jsonObject.put("returnMsg", metaData.getReturn_msg());
				jsonObject.put("orderId", metaData.getOut_trade_no());
			}else{
				jsonObject.put("returnCode", "ERROR");
				jsonObject.put("returnMsg", "订单查询失败!");
			}
			
		}else{
			jsonObject.put("returnCode", "ERROR");
			jsonObject.put("returnMsg", "订单不存在");
		}
		return jsonObject;
	}
	
	/**回调地址
	 *
	 * **/
	@RequestMapping("weixin/pay/order_pay_return.do")  
	public String order_pay_return(HttpServletRequest req, HttpServletResponse res) throws IOException{
		WechatMetaData metaData = new WechatMetaData();
    	BufferedReader br = null;
		String str = "";
		InputStreamReader inputStreamReader = null;
		req.setCharacterEncoding("UTF-8");
		inputStreamReader = new InputStreamReader(req.getInputStream(), "UTF-8");
		br = new BufferedReader(inputStreamReader);
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		// 接收到的流
		str = sb.toString();
		if (StringUtils.isEmpty(str)) {
			metaData.setReturn_code("FAIL");
			metaData.setReturn_msg("回调失败");
			return WechatSdk.toXml(metaData);
		}
		
		//处理接收到的流
		try {
			metaData = WechatSdk.parseXml(str.getBytes());
			OrderPayFromMap orderFormMap = this.orderMapper.findbyFrist("order_id",metaData.getOut_trade_no(), OrderPayFromMap.class);
			if(null != orderFormMap && orderFormMap.size() > 0){
				//处理重复回调
				if("FINISH_OK".equals(orderFormMap.get("order_status"))){
					return null;
				}
				//商户参数校验
				if(!WXPayConstants.APPID.equals(metaData.getAppid()) || !WXPayConstants.MCHID.equals(metaData.getMch_id())){
					return null;
				}
				orderFormMap.put("update_time", DateUtils.getNow());
				 if("SUCCESS".equals(metaData.getResult_code())){
					 orderFormMap.put("order_status", "FINISH_OK");
					 WechatMetaData wechatMetaData = new WechatMetaData();
					 wechatMetaData.setReturn_code("SUCCESS");
					 wechatMetaData.setReturn_msg("OK");
					 this.orderMapper.editEntity(orderFormMap);
					 //回调成功之后，将捐赠记录记录到捐赠表中
					 DonationFormMap donationFormMap = getFormMap(DonationFormMap.class);
			   		 donationFormMap.put("donationTitle", orderFormMap.get("theme"));
			   		 donationFormMap.put("donationContent", Integer.parseInt(orderFormMap.get("order_amount").toString())/100 + "元");
			   		 donationFormMap.put("donationTelephone", orderFormMap.get("phone"));
			   		 donationFormMap.put("donationInstruction", orderFormMap.get("descript"));
			   		 donationFormMap.put("donationType", 1);
				   	 this.donationMapper.addEntity(donationFormMap);
					 return WechatSdk.toXml(wechatMetaData);
				 }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**获取code
	 * @author Ada
	 * http://www.gzshhuzhu.com/numberone-auth/weixin/wx_returnUrl.do
	 * @throws Exception 
	 * **/
	@RequestMapping( {"/weixin/wx_returnUrl.do"} )
	public ModelAndView wx_returnUrl( HttpServletRequest request, HttpServletResponse response,String randomNum) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8"); // 用户同意授权后，能获取到code
		String code = request.getParameter("code");// 用户同意授权
		WeixinOauth2Token wxToken = getOauth2AccessToken(appID,appSecret,code);
		if(wxToken != null){
			UserFormMap formMap = userMapper.findbyFrist("openId",wxToken.getOpenId(), UserFormMap.class);
			if(formMap == null){
				formMap = getFormMap(UserFormMap.class);
				formMap.put("openId", wxToken.getOpenId());
				formMap.put("accessToken", wxToken.getAccessToken());
				formMap.put("expiresIn", wxToken.getExpiresIn());
				formMap.put("refreshToken", wxToken.getRefreshToken());
				formMap.put("scope", wxToken.getScope());
				this.userMapper.addEntity(formMap);
			}
			WeChatUser weChatUser = getSNSUserInfo(wxToken.getAccessToken(),wxToken.getOpenId());
			formMap.put("randomNum", numMap.get(randomNum));
			System.out.println("randomNum=="+numMap.get(randomNum));
			formMap.put("userName", weChatUser.getNickname());
			formMap.put("sex", weChatUser.getSex());
			formMap.put("country", weChatUser.getCountry());
			formMap.put("province", weChatUser.getProvince());
			formMap.put("city", weChatUser.getCity());
			formMap.put("headimgurl", weChatUser.getHeadImgUrl());
			this.userMapper.editEntity(formMap);
		}
		
//		response.sendRedirect("http://www.gzshhuzhu.com/gongyi/html/my.html?donate");// 跳转到要访问的地址
		return new ModelAndView("http://www.gzshhuzhu.com/gongyi/html/my.html?donate");  
	}
	
	
	
	private WeixinOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {
		WeixinOauth2Token wat = null;
		// 拼接请求地址
		String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		requestUrl = requestUrl.replace("APPID", appId);
		requestUrl = requestUrl.replace("SECRET", appSecret);
		requestUrl = requestUrl.replace("CODE", code);
		// 获取网页授权凭证
		JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
		if (null != jsonObject) {
			try {
				wat = new WeixinOauth2Token();
				wat.setAccessToken(jsonObject.getString("access_token"));
				wat.setExpiresIn(jsonObject.getInt("expires_in"));
				wat.setRefreshToken(jsonObject.getString("refresh_token"));
				wat.setOpenId(jsonObject.getString("openid"));
				wat.setScope(jsonObject.getString("scope"));
			} catch (Exception e) {
				wat = null;
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
			}
		}
		return wat;
	}
	
	/**
	 * 通过网页授权获取用户信息
	 * @param accessToken 网页授权接口调用凭证
	 * @param openId 用户标识
	 * @return WeChatUser
	 */
	@SuppressWarnings( { "deprecation", "unchecked" })
	public WeChatUser getSNSUserInfo(String accessToken, String openId) {
		WeChatUser wcu = null;
		// 拼接请求地址
		String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
		// 通过网页授权获取用户信息
		JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
		if (null != jsonObject) {
			try {
				wcu = new WeChatUser();
				
				// 用户的标识
				wcu.setOpenId(jsonObject.getString("openid"));
				
				// 昵称
				wcu.setNickname(jsonObject.getString("nickname"));
				
				// 性别（1是男性，2是女性，0是未知）
				wcu.setSex(jsonObject.getInt("sex"));
				
				// 用户所在国家
				wcu.setCountry(jsonObject.getString("country"));
				
				// 用户所在省份
				wcu.setProvince(jsonObject.getString("province"));
				
				// 用户所在城市
				wcu.setCity(jsonObject.getString("city"));
				
				// 用户头像
				wcu.setHeadImgUrl(jsonObject.getString("headimgurl"));
				
				// 用户特权信息
			} catch (Exception e) {
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
			}
		}
		return wcu;
	}
	
	

	
	/**
	 * 发送https请求
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new HttpsX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);
			
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);

			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
		} catch (Exception e) {
		}
		return jsonObject;
	}

	

	public static String urlEnodeUTF8(String str){ 
        String result = str; 
        try { 
            result = URLEncoder.encode(str,"UTF-8"); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return result; 
	 } 

	public String getRequestCodeUrl(String redirectUrl) {
        return String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect",appID, redirectUrl, "snsapi_userinfo", "STATE");
    }
	
	
	
	/**
	 * 公益信息列表显示
	 * 分页1-10
	 * **/
    @RequestMapping("/weixin/benefitList.do")  
    public void benefitList(HttpServletRequest req, HttpServletResponse res,String startPage) throws IOException {
    	StringBuffer returnXml = new StringBuffer();
    	if(Common.isEmpty(startPage)){
    		startPage = "1";
    	}
    	AutoCodeFormMap formMap = getFormMap(AutoCodeFormMap.class);
    	
    	String order = " order by id asc";
    	formMap.put("$orderby", order);
    	formMap=toFormMap(formMap, startPage, "5",formMap.getStr("orderby"));
        pageView.setRecords(autoCodeMapper.findByPage(formMap));
        if(pageView.getPageCount() != 0){
        	List<AutoCodeFormMap> AutoCodeFormList = (List<AutoCodeFormMap>) pageView.getRecords();
        	List<ArticlesJson> articlesJsonList = new ArrayList();
        	for(AutoCodeFormMap autoCodeForm :AutoCodeFormList){
        		ArticlesJson articlesJson = new ArticlesJson();
        		articlesJson.setId(autoCodeForm.get("id").toString());
        		articlesJson.setTitle(autoCodeForm.get("title").toString());
        		byte[] blob = autoCodeForm.getBytes("CONTENT");
    			String t = new String(blob,"utf-8");//bytep[]转换为String
        		articlesJson.setContent(t);
        		articlesJson.setSource(autoCodeForm.get("source").toString());
        		articlesJson.setNum(autoCodeForm.get("num").toString());
        		articlesJson.setUserName("userName");
        		articlesJson.setIocUrl("----");
        		articlesJsonList.add(articlesJson);
        	}
        	if(articlesJsonList.size() >= 1){
        		CommunalJson communalJson = new CommunalJson("200","成功",articlesJsonList);
        		returnXml.append(JsonUtils.beanToJson(communalJson));
        	}else{
        		JsonBean jsonBean = new JsonBean("201","没有数据");
        		returnXml.append(JsonUtils.beanToJson(jsonBean));
        	}
        }
    	PrintWriter printWrter = setHttpRequest(req, res);// 设置编码规范
		
		printWrter.println(returnXml.toString());
		return;
    }
   
    /**
	 * 增加查看文章次数
	 * 分页1-10
     * @throws Exception 
	 * **/
    @RequestMapping("/weixin/addArticlesNum.do")  
    public void addArticlesNum(HttpServletRequest req, HttpServletResponse res,String id) throws Exception {
    	StringBuffer returnXml = new StringBuffer();
    	if(!Common.isEmpty(id)){
    		AutoCodeFormMap autoCodeMap = autoCodeMapper.findbyFrist("id", id, AutoCodeFormMap.class);
    		autoCodeMap.put("num", Integer.parseInt(autoCodeMap.get("num").toString())+1);
    		byte[] blob = autoCodeMap.getBytes("CONTENT");
			String t = new String(blob,"utf-8");//bytep[]转换为String
			autoCodeMap.put("CONTENT", t);
    		autoCodeMapper.editEntity(autoCodeMap);
    		JsonBean jsonBean = new JsonBean("200","成功");
    		returnXml.append(JsonUtils.beanToJson(jsonBean));
    	}else{
    		JsonBean jsonBean = new JsonBean("400","失败");
    		returnXml.append(JsonUtils.beanToJson(jsonBean));
    	}
    	PrintWriter printWrter = setHttpRequest(req, res);// 设置编码规范
		printWrter.println(returnXml.toString());
		return;
    }
    
    /**
   	 * 文章详细
   	 * 分页1-10
        * @throws Exception 
   	 * **/
   @RequestMapping("/weixin/articlesDetail.do")  
   public void articlesDetail(HttpServletRequest req, HttpServletResponse res,String id) throws Exception {
	   	StringBuffer returnXml = new StringBuffer();
	   	if(!Common.isEmpty(id)){
	   		AutoCodeFormMap autoCodeMap = autoCodeMapper.findbyFrist("id", id, AutoCodeFormMap.class);
	   		ArticlesJson  articlesJson = new ArticlesJson();
	   		articlesJson.setId(autoCodeMap.get("id").toString());
    		articlesJson.setTitle(autoCodeMap.get("title").toString());
    		byte[] blob = autoCodeMap.getBytes("CONTENT");
			String t = new String(blob,"utf-8");//bytep[]转换为String
    		articlesJson.setContent(t);
    		articlesJson.setSource(autoCodeMap.get("source").toString());
    		articlesJson.setNum(autoCodeMap.get("num").toString());
    		articlesJson.setUserName("userName");
    		articlesJson.setIocUrl("----");
	   		
	   		CommunalJson communalJson = new CommunalJson("200","成功",articlesJson);
	   		returnXml.append(JsonUtils.beanToJson(communalJson));
	   	}else{
	   		JsonBean jsonBean = new JsonBean("400","失败");
	   		returnXml.append(JsonUtils.beanToJson(jsonBean));
	   	}
	   	PrintWriter printWrter = setHttpRequest(req, res);// 设置编码规范
		printWrter.println(returnXml.toString());
		return;
    }
    
   
   /**
  	 * 捐款物资
       * @throws Exception 
  	 * **/
   @RequestMapping("/weixin/addDonation.do")  
   public void addDonation(HttpServletRequest req, HttpServletResponse res,String donationTitle,
		   String donationContent,String donationTelephone,String donationInstruction,String donationType,String openId) throws Exception {
	   	StringBuffer returnXml = new StringBuffer();
   		DonationFormMap donationFormMap = getFormMap(DonationFormMap.class);
   		donationFormMap.put("donationTitle", donationTitle);
   		donationFormMap.put("donationContent", donationContent);
   		donationFormMap.put("donationTelephone", donationTelephone);
   		donationFormMap.put("donationInstruction", donationInstruction);
   		donationFormMap.put("openId", openId);
   		if(!Common.isEmpty(donationType)){
   			donationFormMap.put("donationType", donationType);
   		}
   		this.donationMapper.addEntity(donationFormMap);
   		JsonBean jsonBean = new JsonBean("200","成功");
   		returnXml.append(JsonUtils.beanToJson(jsonBean));
	   	PrintWriter printWrter = setHttpRequest(req, res);// 设置编码规范
		printWrter.println(returnXml.toString());
		return;
    }
   
   /**
    * 我的捐赠
    * @param req
    * @param res
    * @return
    */
//   http://localhost:8080/numberone-auth/weixin/myDonation.do?openId=111
   @RequestMapping("/weixin/myDonation.do")
   public List<DonationResult> myDonation(HttpServletRequest req, HttpServletResponse res){
	 
	   String openId = req.getParameter("openId");
	   List<DonationFormMap> donationList = this.donationMapper.findByAttribute("openId", openId, DonationFormMap.class);
	   List<DonationResult> resultList = new ArrayList<DonationResult>();
	   for(DonationFormMap d : donationList){
		   DonationResult donationResult = new DonationResult();
		   donationResult.setId(d.get("id").toString());
		   donationResult.setDonationTitle(d.getStr("donationTitle"));
		   donationResult.setDonationTelephone(d.getStr("donationTelephone"));
		   donationResult.setDonationInstruction(d.getStr("donationInstruction"));
		   donationResult.setDonationType(d.get("donationType").toString());
		   donationResult.setOpenId(openId);
		   resultList.add(donationResult);
	   }
	return resultList;
	   
   }
   
    
	private PrintWriter setHttpRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String method = req.getMethod();
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html; charset=utf-8");
		res.setHeader("Access-Control-Allow-Origin", "*");
		return res.getWriter();
	}
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
	}
} 


