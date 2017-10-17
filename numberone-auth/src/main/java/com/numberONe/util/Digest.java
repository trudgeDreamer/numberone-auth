package com.numberONe.util;

import java.security.MessageDigest;

public class Digest {
	


	private String algorithm;

	public Digest(String algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * 产生散列值
	 * 
	 * @param bytes 需要散列的字节数组
	 * @throws Exception
	 */
	public String gen(byte[] bytes) throws Exception {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(bytes);
			byte[] digests = md.digest();
			return EncryptionHelper.byte2hex(digests);
		} catch (Exception e) {
			throw new Exception("非法摘要算法");
		}
	}

	/**
	 * 产生散列值
	 * 
	 * @param str 需要散列的字符串，用UTF-8转为字节数组
	 * @throws Exception
	 */
	public String gen(String str) throws Exception {
		return gen(str.getBytes("UTF-8"));
	}


}
