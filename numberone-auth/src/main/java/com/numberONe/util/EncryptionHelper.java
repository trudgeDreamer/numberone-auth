package com.numberONe.util;
import java.security.interfaces.RSAPublicKey;



public class EncryptionHelper {

	/**
	 * Utility method to delete the leading zeros from the modulus.
	 * 
	 * @param a modulus
	 */
	public static byte[] stripLeadingZeros(byte[] a) {
		int lastZero = -1;
		for (int i = 0; i < a.length; i++) {
			if (a[i] == 0) {
				lastZero = i;
			} else {
				break;
			}
		}
		lastZero++;
		byte[] result = new byte[a.length - lastZero];
		System.arraycopy(a, lastZero, result, 0, result.length);
		return result;
	}

	/**
	 * Gets the RSA Public Key as XML. The key idea is to make the key readable for .Net platform.
	 * 
	 * @param key RSAPublicKey
	 * @return XML document.
	 */
	public static String getRSAPublicKeyAsXML(RSAPublicKey key) {
		String result = "<RSAKeyValue>";
		result += "<Modulus>";
		byte[] modulusBytes = key.getModulus().toByteArray();
		modulusBytes = stripLeadingZeros(modulusBytes);
		result += new String(Base64.encode(modulusBytes));
		result += "</Modulus>";
		result += "<Exponent>";
		byte[] exponentBytes = key.getPublicExponent().toByteArray();
		result += new String(Base64.encode(exponentBytes));
		result += "</Exponent>";
		result += "</RSAKeyValue>";
		return result;
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
	 * 把十六进制字符串转换为字节数组
	 * 
	 * @param strhex 字符串
	 */
	public static byte[] hex2byte(String strhex) {
		if (strhex == null)
			return null;
		int len = strhex.length();
		if (len % 2 == 1)
			return null;
		byte[] b = new byte[len / 2];
		for (int i = 0; i < len / 2; i++) {
			b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
		}
		return b;
	}


}
