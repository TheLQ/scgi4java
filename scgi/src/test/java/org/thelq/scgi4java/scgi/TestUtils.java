/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thelq.scgi4java.scgi;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Leon
 */
public class TestUtils {
	public static final String REQUEST_RAW = StringUtils.replace("730:CONTENT_LENGTH|81|"
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
			+ ",<methodCall><methodName>system.library_version</methodName><params/></methodCall>", "|", "\0");
	public static final ImmutableMap<String, String> REQUEST_HEADERS = ImmutableMap.<String, String>builder()
			.put("REQUEST_METHOD", "POST")
			.put("REQUEST_URI", "/SCGI")
			.put("QUERY_STRING", "")
			.put("CONTENT_TYPE", "text/xml; charset=UTF-8")
			.put("DOCUMENT_URI", "/SCGI")
			.put("DOCUMENT_ROOT", "/usr/share/nginx/html")
			.put("SERVER_PROTOCOL", "HTTP/1.1")
			.put("REMOTE_ADDR", "55.555.5.55")
			.put("REMOTE_PORT", "55555")
			.put("SERVER_PORT", "80")
			.put("SERVER_NAME", "localhost")
			.put("HTTP_HOST", "55.555.555.55")
			.put("HTTP_USER_AGENT", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
			.put("HTTP_ACCEPT", "application/xml, text/xml, */*; q=0.01")
			.put("HTTP_ACCEPT_LANGUAGE", "en-US,en;q=0.5")
			.put("HTTP_ACCEPT_ENCODING", "gzip, deflate")
			.put("HTTP_CONTENT_TYPE", "text/xml; charset=UTF-8")
			.put("HTTP_X_REQUESTED_WITH", "XMLHttpRequest")
			.put("HTTP_REFERER", "http://55.555.555.55/test.html")
			.put("HTTP_CONTENT_LENGTH", "81")
			.put("HTTP_CONNECTION", "keep-alive")
			.put("HTTP_PRAGMA", "no-cache")
			.put("HTTP_CACHE_CONTROL", "no-cache")
			.build();
	public static final String REQUEST_BODY = "<methodCall><methodName>system.library_version</methodName><params/></methodCall>";
	public static final String RESPONSE_RAW = "Status: 200 OK\r\n"
			+ "Content-Type: text/plain\r\n"
			+ "Content-Length: 5\r\n"
			+ "Test-Header: flib\r\n"
			+ "OtherHeader: flab\r\n"
			+ "\r\n"
			+ "ab cd";
	public static final ImmutableMap<String, String> RESPONSE_HEADERS = ImmutableMap.<String, String>builder()
			.put("Test-Header", "flib")
			.put("OtherHeader", "flab")
			.build();
	public static final String RESPONSE_BODY = "ab cd";
}
