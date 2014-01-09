/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.thelq.scgi4java.scgi;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import static org.thelq.scgi4java.scgi.SCGIClient.makeRequest;

/**
 *
 * @author Leon
 */
public class SCGIClientTest {
	@Test
	public void makeRequestTest() {
		//Generated from nginx scgi_pass
		String exampleEncoded = "730:CONTENT_LENGTH|81|"
				+ "SCGI|1|"
				+ "REQUEST_METHOD|POST|"
				+ "REQUEST_URI|/SCGI|"
				+ "QUERY_STRING||"
				+ "CONTENT_TYPE|text/xml; charset=UTF-8|"
				+ "DOCUMENT_URI|/SCGI|"
				+ "DOCUMENT_ROOT|/usr/share/nginx/html|"
				+ "SERVER_PROTOCOL|HTTP/1.1|"
				+ "REMOTE_ADDR|55.555.5.55|"
				+ "REMOTE_PORT|55555|"
				+ "SERVER_PORT|80|"
				+ "SERVER_NAME|localhost|"
				+ "HTTP_HOST|55.555.555.55|"
				+ "HTTP_USER_AGENT|Mozilla/5.0 (Windows NT 6.3; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0|"
				+ "HTTP_ACCEPT|application/xml, text/xml, */*; q=0.01|"
				+ "HTTP_ACCEPT_LANGUAGE|en-US,en;q=0.5|"
				+ "HTTP_ACCEPT_ENCODING|gzip, deflate|"
				+ "HTTP_CONTENT_TYPE|text/xml; charset=UTF-8|"
				+ "HTTP_X_REQUESTED_WITH|XMLHttpRequest|"
				+ "HTTP_REFERER|http://55.555.555.55/test.html|"
				+ "HTTP_CONTENT_LENGTH|81|"
				+ "HTTP_CONNECTION|keep-alive|"
				+ "HTTP_PRAGMA|no-cache|"
				+ "HTTP_CACHE_CONTROL|no-cache|"
				+ ",<methodCall><methodName>system.library_version</methodName><params/></methodCall>";
		String exampleRaw = StringUtils.replace(exampleEncoded, "|", "\0");
		
		Map<String, String> requestMap = new LinkedHashMap<String, String>();
		//requestMap.put("CONTENT_LENGTH", "81");
		requestMap.put("REQUEST_METHOD", "POST");
		requestMap.put("REQUEST_URI", "/SCGI");
		requestMap.put("QUERY_STRING", "");
		requestMap.put("CONTENT_TYPE", "text/xml; charset=UTF-8");
		requestMap.put("DOCUMENT_URI", "/SCGI");
		requestMap.put("DOCUMENT_ROOT", "/usr/share/nginx/html");
		requestMap.put("SERVER_PROTOCOL", "HTTP/1.1");
		requestMap.put("REMOTE_ADDR", "55.555.5.55");
		requestMap.put("REMOTE_PORT", "55555");
		requestMap.put("SERVER_PORT", "80");
		requestMap.put("SERVER_NAME", "localhost");
		requestMap.put("HTTP_HOST", "55.555.555.55");
		requestMap.put("HTTP_USER_AGENT", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
		requestMap.put("HTTP_ACCEPT", "application/xml, text/xml, */*; q=0.01");
		requestMap.put("HTTP_ACCEPT_LANGUAGE", "en-US,en;q=0.5");
		requestMap.put("HTTP_ACCEPT_ENCODING", "gzip, deflate");
		requestMap.put("HTTP_CONTENT_TYPE", "text/xml; charset=UTF-8");
		requestMap.put("HTTP_X_REQUESTED_WITH", "XMLHttpRequest");
		requestMap.put("HTTP_REFERER", "http://55.555.555.55/test.html");
		requestMap.put("HTTP_CONTENT_LENGTH", "81");
		requestMap.put("HTTP_CONNECTION", "keep-alive");
		requestMap.put("HTTP_PRAGMA", "no-cache");
		requestMap.put("HTTP_CACHE_CONTROL", "no-cache");
		String requestBody = "<methodCall><methodName>system.library_version</methodName><params/></methodCall>";
		String requestCreated = makeRequest(requestMap, requestBody);
		
		Assert.assertEquals(exampleRaw, requestCreated, "Generated request doesn't match given");
	}
}
