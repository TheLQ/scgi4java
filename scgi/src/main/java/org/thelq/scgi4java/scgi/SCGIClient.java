/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.thelq.scgi4java.scgi;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Leon
 */
public class SCGIClient {
	public static String makeRequest(HashMap<String, String> header, String body) {
		String res = "CONTENT_LENGTH\0" + (body != null ? body.length() : 0)
				+ "\0SCGI\0" + "1\0";
		if (header != null)
			for (Map.Entry<String, String> entry : header.entrySet())
				res += entry.getKey() + '\0' + entry.getValue() + '\0';
		String size = new Integer(res.getBytes().length) + ":";
		res += "," + body;
		return size + res;
	}
}
