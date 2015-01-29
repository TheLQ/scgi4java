/**
 * Copyright (C) 2010-2013 Leon Blakey <lord.quackstar at gmail.com>
 *
 * This file is part of scgi4java.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package net.thelq.scgi4java;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.InputStream;
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

	public static String inputToString(InputStream input) throws IOException {
		StringBuilder body = new StringBuilder();
		int curChar;
		while ((curChar = input.read()) != -1)
			body.append((char) curChar);
		return body.toString();
	}
}
