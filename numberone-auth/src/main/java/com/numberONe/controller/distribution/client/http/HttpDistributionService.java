package com.numberONe.controller.distribution.client.http;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public class HttpDistributionService {

	 public static String doHttpGet(final String url) throws IOException {
	        final GetMethod get = new GetMethod(url);
	        final HttpClient httpClient = new HttpClient();
	        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
	        httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
	        int status = 0;
	        status = httpClient.executeMethod(get);
	        if ((status >= 200) && (status < 300)) {
	            return new String(get.getResponseBody(), "UTF-8");
	        }
	        //除了status成功状态码，其它情况都以Exception抛出
	        throw new IOException("HttpGet-Status:" + status);
	    }
}
