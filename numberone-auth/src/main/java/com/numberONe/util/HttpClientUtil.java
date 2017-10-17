package com.numberONe.util;


import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;


public class HttpClientUtil {
	


	public final static String UTF_8_ENCODING = "UTF-8";
	public final static int CONNECTTIMEOUT = 10000;
	public final static int SOCKETTIMEOUT = 10000;
	public final static int STATUS_OK = 200;

	public final static HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER = new HostnameVerifier() {

		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	/**
	 * 发送http Post请求(使用UTF8编码字符集)
	 * @param url 请求地址
	 * @param message 请求报文
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static byte[] httpPost(String url, String message) throws ClientProtocolException, IOException {
		return httpPost(url, message, UTF_8_ENCODING, UTF_8_ENCODING, false);
	}
	
	public static byte[] httpPost(String url, String message, boolean isOpenApi) throws ClientProtocolException, IOException {
		return httpPost(url, message, UTF_8_ENCODING, UTF_8_ENCODING, isOpenApi);
	}

	/**
	 * 发送http Post请求
	 * @param url 请求地址
	 * @param message 请求报文
	 * @param messageEncoding 报文字符编码
	 * @param requestEncoding 请求头Content-type的编码
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static byte[] httpPost(String url, String message, String messageEncoding, String requestEncoding, boolean isOpenApi)
			throws ClientProtocolException, IOException {

		HttpPost httpPost = new HttpPost(url);

		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECTTIMEOUT)
				.setSocketTimeout(SOCKETTIMEOUT).build();
		httpPost.setConfig(requestConfig);

		StringEntity stringEntity = new StringEntity(message, messageEncoding);
		httpPost.setEntity(stringEntity);
		if (isOpenApi) {			
			httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
		}

		HttpRequestInterceptor httpRequestInterceptor = new DefaultHttpRequestInterceptor(requestEncoding);
		CloseableHttpClient httpClient = HttpClients.custom().addInterceptorLast(httpRequestInterceptor).build();
		
		CloseableHttpResponse response = httpClient.execute(httpPost);

		try {
			int status = response.getStatusLine().getStatusCode();
			if (STATUS_OK != status)
				throw new ClientProtocolException("Unexpected response status: " + status);

			HttpEntity httpEntity = response.getEntity();
			InputStream inputStream = httpEntity.getContent();
			try {
				return IOUtils.toByteArray(inputStream);
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 发送https Post请求
	 * @param url 请求地址
	 * @param message 请求报文
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static byte[] httpsPost(String url, String message) throws KeyManagementException, NoSuchAlgorithmException,
			ClientProtocolException, IOException {

		return httpsPost(url, message, UTF_8_ENCODING, UTF_8_ENCODING);
	}
	
	public static byte[] httpsPost(String url, String message, boolean isOpenApi) throws KeyManagementException, NoSuchAlgorithmException,
		ClientProtocolException, IOException {

		HttpRequestInterceptor httpRequestInterceptor = new DefaultHttpRequestInterceptor(UTF_8_ENCODING);

		TrustManager[] trustManager = new TrustManager[] { new DefaultX509TrustManager() };
		
		return httpsPost(url, message, UTF_8_ENCODING, null, trustManager, ALLOW_ALL_HOSTNAME_VERIFIER,
				httpRequestInterceptor, isOpenApi);
	}

	/**
	 * 发送https Post请求
	 * @param url 请求地址
	 * @param message 请求报文
	 * @param messageEncoding 报文字符编码
	 * @param requestEncoding 请求头Content-type的编码
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static byte[] httpsPost(String url, String message, String messageEncoding, String requestEncoding)
			throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException {

		TrustManager[] trustManager = new TrustManager[] { new DefaultX509TrustManager() };

		return httpsPost(url, message, messageEncoding, requestEncoding, null, trustManager);
	}

	/**
	 * 发送https Post请求
	 * @param url 请求地址
	 * @param message 请求报文
	 * @param messageEncoding 报文字符编码
	 * @param requestEncoding 请求头Content-type的编码
	 * @param keyStore
	 * @param trustStore
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws KeyStoreException 
	 * @throws UnrecoverableKeyException 
	 */
	public static byte[] httpsPost(String url, String message, String messageEncoding, String requestEncoding,
			KeyStore trustStore, KeyStore keyStore, String keyStorePassword) throws KeyManagementException,
			NoSuchAlgorithmException, ClientProtocolException, IOException, KeyStoreException,
			UnrecoverableKeyException {

		HttpRequestInterceptor httpRequestInterceptor = new DefaultHttpRequestInterceptor(requestEncoding);

		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory
				.getDefaultAlgorithm());
		trustManagerFactory.init(trustStore);

		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

		return httpsPost(url, message, messageEncoding, keyManagerFactory.getKeyManagers(),
				trustManagerFactory.getTrustManagers(), ALLOW_ALL_HOSTNAME_VERIFIER, httpRequestInterceptor, false);
	}

	/**
	 * 发送https Post请求
	 * @param url 请求地址
	 * @param message 请求报文
	 * @param messageEncoding 报文字符编码
	 * @param requestEncoding 请求头Content-type的编码
	 * @param keyManager
	 * @param trustManager
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static byte[] httpsPost(String url, String message, String messageEncoding, String requestEncoding,
			KeyManager[] keyManager, TrustManager[] trustManager) throws KeyManagementException,
			NoSuchAlgorithmException, ClientProtocolException, IOException {

		HttpRequestInterceptor httpRequestInterceptor = new DefaultHttpRequestInterceptor(requestEncoding);

		return httpsPost(url, message, messageEncoding, keyManager, trustManager, ALLOW_ALL_HOSTNAME_VERIFIER,
				httpRequestInterceptor, false);
	}

	/**
	 * 发送https Post请求
	 * @param url 请求地址
	 * @param message 请求报文
	 * @param messageEncoding 报文字符编码
	 * @param keyManager
	 * @param trustManager
	 * @param hostnameVerifier
	 * @param httpRequestInterceptor
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static byte[] httpsPost(String url, String message, String messageEncoding, KeyManager[] keyManager,
			TrustManager[] trustManager, HostnameVerifier hostnameVerifier,
			HttpRequestInterceptor httpRequestInterceptor, boolean isOpenApi) throws KeyManagementException, NoSuchAlgorithmException,
			ClientProtocolException, IOException {

		HttpPost httpPost = new HttpPost(url);

		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECTTIMEOUT)
				.setSocketTimeout(SOCKETTIMEOUT).build();
		httpPost.setConfig(requestConfig);
		
		if (isOpenApi) {			
			httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
		}

		StringEntity stringEntity = new StringEntity(message, messageEncoding);
		httpPost.setEntity(stringEntity);

		SSLContext sslContext = SSLContexts.custom().build();
		sslContext.init(keyManager, trustManager, null);

		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);

		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
				.addInterceptorLast(httpRequestInterceptor).build();

		CloseableHttpResponse response = httpClient.execute(httpPost);

		try {
			int status = response.getStatusLine().getStatusCode();
			if (STATUS_OK != status)
				throw new ClientProtocolException("Unexpected response status: " + status);

			HttpEntity httpEntity = response.getEntity();
			InputStream inputStream = httpEntity.getContent();
			try {
				return IOUtils.toByteArray(inputStream);
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static class DefaultX509TrustManager implements X509TrustManager {
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

	private static class DefaultHttpRequestInterceptor implements HttpRequestInterceptor {
		private String encoding;

		public DefaultHttpRequestInterceptor(String encoding) {
			this.encoding = encoding;
		}

		public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
			request.setHeader("Content-type", "application/x-www-form-urlencoded;charset=" + encoding);
		}
	}

	/*
	public static void main(String[] args) throws Exception {
		System.err.println(
		IOUtils.toString(httpsPost("https://estore-test.infinitus.com.cn/api/public/pay/callback/doAfterPaid?bizTypeCode=ESTORE_STORE_PO&orderId=PAPP0000000012875631&payAmt=0.01&arPmAmt=0&otherPmCode=BOCCREDIT&otherPmName=%E4%B8%AD%E5%9B%BD%E9%93%B6%E8%A1%8C%E4%BF%A1%E7%94%A8%E5%8D%A1%E5%88%86%E6%9C%9F%E6%94%AF%E4%BB%98&otherPmAmt=0.01&signKey=686995e6774375d7b4fec61ba9a5de5468b903b9", "", true))
				);
		
	}*/


}
