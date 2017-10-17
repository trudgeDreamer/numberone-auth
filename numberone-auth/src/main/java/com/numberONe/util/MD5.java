package com.numberONe.util;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MD5 {

	private static Digest digest;

	protected static Log LOG = LogFactory.getLog(MD5.class);

	static {
		digest = new Digest("MD5");
	}

	/**
	 * 生成消息摘要
	 * @param bytes
	 */
	public static String gen(byte[] bytes) {
		try {
			return digest.gen(bytes);
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	public static String gen(String str) {
		try {
			return digest.gen(str);
		} catch (Exception e) {
			LOG.debug(e);
		}
		return null;
	}

	public static void main(String[] args) {
		String str = "test";
		System.out.println("消息:" + str);
		String digest = gen(str);
		System.out.println("摘要:" + digest);
	}

	
    /**
     * 一下是支付宝提供的md5Demo方式，可另行修改
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
    	text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }
    
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
    	text = text + key;
    	String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
    	if(mysign.equals(sign)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException 
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
}
