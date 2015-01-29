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
package net.thelq.scgi4java.scgi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 *
 * @author Leon
 */
public class SCGIClient {
	public static String createRequest(@Nonnull String body) {
		return createRequest(body, Collections.EMPTY_MAP);
	}

	public static String createRequest(@Nonnull String body, @Nonnull Map<String, String> header) {
		if (body == null)
			throw new NullPointerException("body cannot be null");
		if (header == null)
			throw new NullPointerException("header cannot be null");

		//Start building request with required SCGI headers
		StringBuilder req = new StringBuilder()
				.append(createRequestHeader("CONTENT_LENGTH", Integer.toString(body.length())))
				.append(createRequestHeader("SCGI", "1"));

		//Add user's headers
		for (Map.Entry<String, String> entry : header.entrySet())
			req.append(createRequestHeader(entry.getKey(), entry.getValue()));

		//Prepend header length
		req.insert(0, ':');
		req.insert(0, req.length() - 1);

		//Add body and send
		req.append(',').append(body);
		return req.toString();
	}

	protected static String createRequestHeader(String key, String value) {
		if (key == null)
			throw new NullPointerException("key cannot be null");
		if (value == null)
			throw new NullPointerException("value cannot be null");
		
		return key + "\0" + value + "\0";
	}

	public static Map<String, String> parseResponse(InputStream input) throws IOException {
		return parseResponse(input, Charset.defaultCharset());
	}

	public static Map<String, String> parseResponse(InputStream input, Charset charset) throws IOException {
		if (input == null)
			throw new NullPointerException("input cannot be null");
		if (charset == null)
			throw new NullPointerException("charset cannot be null");
		
		Map<String, String> responseHeaders = new HashMap<String, String>();

		StringBuilder valBuilder = new StringBuilder();
		String lastKey = null;
		char curChar;
		while ((curChar = (char) input.read()) != (char) -1) {
			if (curChar == ':' && lastKey == null) {
				//Finished building the key name
				lastKey = valBuilder.toString();
				valBuilder.delete(0, valBuilder.length());
			} else if (curChar == ' ' && valBuilder.length() == 0) {
				//This is the spacing after the key but before the value
			} else if (curChar == '\r') {
				//Finished building value
				if (valBuilder.length() != 0) {
					//JUST finished
					responseHeaders.put(lastKey, valBuilder.toString());
					lastKey = null;
					valBuilder.delete(0, valBuilder.length());
				} else if (lastKey == null) {
					//No key and no value, this is the blank line before the body
					//Read next newline char to finish line
					input.read();
					break;
				}
			} else if (curChar == '\n') {
				//Real end of line, ignore
			} else {
				//Currently building something
				valBuilder.append(curChar);
			}
		}

		return responseHeaders;
	}
}
