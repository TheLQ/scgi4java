/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thelq.scgi4java.scgi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;

/**
 *
 * @author Leon
 */
public class SCGIClient {
	public static String makeRequest(@NonNull String body) {
		return makeRequest(body, Collections.EMPTY_MAP);
	}

	public static String makeRequest(@NonNull String body, @NonNull Map<String, String> header) {
		//Start building request with required SCGI headers
		StringBuilder req = new StringBuilder()
				.append(makeRequestHeader("CONTENT_LENGTH", Integer.toString(body.length())))
				.append(makeRequestHeader("SCGI", "1"));

		//Add user's headers
		for (Map.Entry<String, String> entry : header.entrySet())
			req.append(makeRequestHeader(entry.getKey(), entry.getValue()));

		//Prepend header length
		req.insert(0, ':');
		req.insert(0, req.length() - 1);

		//Add body and send
		req.append(',').append(body);
		return req.toString();
	}

	protected static String makeRequestHeader(String key, String value) {
		return key + "\0" + value + "\0";
	}
	
	protected static Map<String, String> parseResponse(InputStream input) throws IOException {
		return parseResponse(input, Charset.defaultCharset());
	}
	
	protected static Map<String, String> parseResponse(InputStream input, Charset charset) throws IOException {
		Map<String, String> responseHeaders = new HashMap<String, String>();
		
		StringBuilder valBuilder = new StringBuilder();
		String lastKey = null;
		char curChar;
		while ((curChar = (char) input.read()) != (char)-1) {
			if(curChar == ':' && lastKey == null) {
				//Finished building the key name
				lastKey = valBuilder.toString();
				valBuilder.delete(0, valBuilder.length());
			} else if(curChar == ' ' && valBuilder.length() == 0) {
				//This is the spacing after the key but before the value
			} else if(curChar == '\r') {
				//Finished building value
				if(valBuilder.length() != 0) {
					//JUST finished
					responseHeaders.put(lastKey, valBuilder.toString());
					lastKey = null;
					valBuilder.delete(0, valBuilder.length());
				} else if(lastKey == null) {
					//No key and no value, this is the blank line before the body
					break;
				}
			} else if(curChar == '\n') {
				//Real end of line, ignore
			} else {
				//Currently building something
				valBuilder.append(curChar);
			}
		}
		
		return responseHeaders;
	}
}
