package com.numberONe.controller.app;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.numberONe.controller.app.json.WechatMetaData;
import com.numberONe.util.MD5;

import net.sf.json.JSONObject;

public class WechatSdk {
	


	private static Logger logger = LoggerFactory.getLogger(WechatSdk.class);

	private static JAXBContext jaxbContext;
	private static Marshaller marshaller;
	private static Unmarshaller unmarshaller;
	
	private static Lock lock = new ReentrantLock();  

	public static JAXBContext getJAXBContext() throws JAXBException {
		if (jaxbContext == null)
			jaxbContext = JAXBContext.newInstance(WechatMetaData.class);
		return jaxbContext;
	}

	public static Marshaller getMarshaller() throws JAXBException {
		if (marshaller == null) {
			marshaller = getJAXBContext().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		}
		return marshaller;
	}

	public static Unmarshaller getUnmarshaller() throws JAXBException {
		if (unmarshaller == null) {
			unmarshaller = getJAXBContext().createUnmarshaller();
		}
		return unmarshaller;
	}
	
	/**
	 * 生成32位随机字符串
	 * @return
	 */
	public static String genRondomString() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 *  生成时间戳
	 * @return
	 */
	public static String genTimeStamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	/**
	 * 将WechatMetaData对象序列化为XML字符串
	 * @param metaData
	 * @return
	 */
	public static String toXml(WechatMetaData metaData){
		StringWriter stringWriter = new StringWriter();
		lock.lock();
		try {
			getMarshaller().marshal(metaData, stringWriter);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		lock.unlock();
		return stringWriter.toString();
	}
	
//	/**
//	 * 将ThirdPartyPaymentResult对象序列化为XML字符串
//	 * @param metaData
//	 * @return
//	 * @throws Exception
//	 */
//	public static String toXml(ThirdPartyPaymentResult thirdPartyPaymentResult) throws Exception {
//		StringWriter stringWriter = new StringWriter();
//		lock.lock();
//		getMarshaller().marshal(thirdPartyPaymentResult, stringWriter);
//		lock.unlock();
//		return stringWriter.toString();
//	}
	
/*	*//**
	 * 将AliPaytMetaData对象序列化为XML字符串
	 * @param metaData
	 * @return
	 * @throws Exception
	 *//*
	public static String toXmlAliPay(AliPayMetaData aliPayMetaData) throws Exception {
		StringWriter stringWriter = new StringWriter();
		lock.lock();
		getMarshaller().marshal(aliPayMetaData, stringWriter);
		lock.unlock();
		return stringWriter.toString();
	}*/

	/**
	 * 将报文元数据Bean转换为url查询字符串格式(默认采用UTF-8字符集做urlEncode)
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws UnsupportedEncodingException
	 */
	public static String toUrlQryStr(WechatMetaData WechatMetaData) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, UnsupportedEncodingException {
		return toUrlQryStr(WechatMetaData, true);
	}

	/**
	 * 将报文元数据Bean转换为url查询字符串格式
	 * @param WechatMetaData 待转换的报文元数据Bean
	 * @param isUrlEncode 是否做urlEncode转换(urlEncode默认采用UTF-8字符集)
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws UnsupportedEncodingException
	 */
	public static String toUrlQryStr(Object objectBean, boolean isUrlEncode) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, UnsupportedEncodingException {
		return toUrlQryStr(objectBean, isUrlEncode, "UTF-8");
	}

	/**
	 * 将报文元数据Bean转换为url查询字符串格式
	 * @param WechatMetaData 待转换的报文元数据Bean
	 * @param isUrlEncode 是否做urlEncode转换
	 * @param encoding urlEncode使用的字符集
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws UnsupportedEncodingException
	 */
	public static String toUrlQryStr(Object objectBean, boolean isUrlEncode, String encoding)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException,
			UnsupportedEncodingException {

		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		String key, value;

		if (objectBean instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) objectBean;
			Iterator<?> it = jsonObject.keys();
			while (it.hasNext()) {
				key = (String) it.next();
				value = jsonObject.getString(key);
				if (StringUtils.isNotEmpty(value)) {
					treeMap.put(key, value);
				}
			}
		} else {
			Map<String, String> metaMap = BeanUtils.describe(objectBean);
			metaMap.remove("class");
			for (Map.Entry<String, String> entry : metaMap.entrySet()) {
				key = entry.getKey();
				value = entry.getValue();
				if (StringUtils.isNotEmpty(value)) {
					treeMap.put(key, value);
				}
			}
		}

		StringBuilder result = new StringBuilder();
		for (Entry<String, String> entry : treeMap.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();

			if (StringUtils.isEmpty(value))
				continue;

			if (isUrlEncode) {
				result.append(key).append("=").append(URLEncoder.encode(value, encoding)).append("&");
			} else {
				result.append(key).append("=").append(value).append("&");
			}
		}

		if (result.length() > 0) {
			result.deleteCharAt(result.length() - 1);
		}

		return result.toString();
	}

	/**
	 * 解析Url查询字符串
	 * @param urlQryStr
	 * @return
	 */
	public static WechatMetaData parseUrlQryStr(String urlQryStr) throws Exception {

		WechatMetaData WechatMetaData = new WechatMetaData();
		HashMap<String, String> dataMap = new HashMap<String, String>();
		try {
			String[] ArrayStr = urlQryStr.split("&");
			String[] keyValue;
			for (String keyValuePair : ArrayStr) {
				keyValue = keyValuePair.split("=");
				if (keyValue.length > 1)
					dataMap.put(keyValue[0], keyValue[1]);
			}
			BeanUtils.populate(WechatMetaData, dataMap);
		} catch (Exception e) {
			throw new Exception("解析Url查询字符串异常", e);
		}

		return WechatMetaData;
	}

	/**
	 * 解析XML数据(采用UTF-8字符集编码)
	 * @param xmlBytes 待解析的XML数据
	 * @return
	 * @throws ParseResultException
	 */
	public static WechatMetaData parseXml(byte[] xmlBytes) throws Exception {
		return parseXml(xmlBytes, "UTF-8");
	}

	/**
	 * 解析XML数据
	 * @param xmlBytes 待解析的XML数据
	 * @param encoding 字符集编码
	 * @return
	 * @throws ParseResultException
	 */
	public static WechatMetaData parseXml(byte[] xmlBytes, String encoding) throws Exception {

		WechatMetaData WechatMetaData = new WechatMetaData();
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(xmlBytes);
			InputStreamReader isr = new InputStreamReader(bis, encoding);
			JAXBContext jaxbContext = JAXBContext.newInstance(WechatMetaData.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			WechatMetaData = (WechatMetaData) unmarshaller.unmarshal(isr);
		} catch (Exception e) {
			try {
				logger.info("Original XML:" + new String(xmlBytes, encoding));
			} catch (Exception e1) {
				logger.error(e.getMessage(), e);
			}
			throw new Exception("解析XML数据异常", e);
		}

		return WechatMetaData;
	}

	/**
	 * MD5算法签名(默认UTF-8字符集编码)
	 * @param WechatMetaData 待签名元数据
	 * @param key 秘钥
	 * @throws UnsupportedEncodingException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static String signByMD5(Object objectBean, String key) throws UnsupportedEncodingException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return signByMD5(objectBean, key, "UTF-8");
	}

	/**
	 * MD5算法签名
	 * @param WechatMetaData 待签名元数据
	 * @param key 秘钥
	 * @param encoding 字符编码
	 * @throws UnsupportedEncodingException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws ExcelException 
	 */
	public static String signByMD5(Object objectBean, String key, String encoding)
			throws UnsupportedEncodingException, IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		String sign = null;
		String qryStr = toUrlQryStr(objectBean, false);
		qryStr = qryStr + "&key=" + key;
		logger.info("原签名字符串:{}", qryStr);
		sign = MD5.gen(qryStr.getBytes(encoding)).toUpperCase();
		logger.info("签名结果串:{}", sign);
		return sign;
	}
	
	/**
	 * 把字节数组转换为十六进制字符串
	 * 
	 * @param b 字节数组
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0xFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toLowerCase();
	}

	/**
	 * MD5算法验证签名
	 * @param WechatMetaData 待验证签名元数据
	 * @param key 秘钥
	 * @throws Exception 
	 */
	public static void validateByMD5(WechatMetaData WechatMetaData, String key) throws Exception {
		validateByMD5(WechatMetaData, key, "UTF-8");
	}

	/**
	 * MD5算法验证签名
	 * @param WechatMetaData 待验证签名元数据
	 * @param key 秘钥
	 * @param encoding 字符编码
	 * @throws Exception 
	 */
	public static void validateByMD5(WechatMetaData WechatMetaData, String key, String encoding) throws Exception {

		String orginSign = WechatMetaData.getSign();

		if (StringUtils.isEmpty(orginSign))
			throw new Exception("验签结果失败");

		try {
			signByMD5(WechatMetaData, key);
		} catch (Exception e) {
			WechatMetaData.setSign(orginSign);
			throw new Exception("验签时生成MD5摘要异常");
		}

		if (!orginSign.equals(WechatMetaData.getSign())) {
			WechatMetaData.setSign(orginSign);
			throw new Exception("验签结果失败");
		}
	}

//	public static String genQrCodeImageData(String content) {
//		try {
//			BufferedImage bufferedImage = QRCodeUtils.createQRCode(content, 200, 200);
//			ByteArrayOutputStream is = new ByteArrayOutputStream();
//			ImageIO.write(bufferedImage, "jpg", is);
//			return "data:image/jpg;base64," + Base64Utils.encodeToString(is.toByteArray());
//		} catch (Exception e) {
//			logger.info("生成二维码异常:{}", e.getMessage());
//			throw new RuntimeException("生成二维码异常", e);
//		}
//	}


}
