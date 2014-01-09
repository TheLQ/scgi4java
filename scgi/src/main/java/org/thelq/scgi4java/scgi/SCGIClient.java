/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thelq.scgi4java.scgi;

import java.util.Map;
import lombok.NonNull;

/**
 *
 * @author Leon
 */
public class SCGIClient {
	public static String makeRequest(@NonNull Map<String, String> header, @NonNull String body) {
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
}
