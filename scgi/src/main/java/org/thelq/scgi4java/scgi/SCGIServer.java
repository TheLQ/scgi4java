/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thelq.scgi4java.scgi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.NonNull;

/**
 * Utility class for SCGI servers
 * @author Leon
 */
public class SCGIServer {
	protected static final String RESPONSE_LINEEND = "\r\n";

	/**
	 * Convenience method for {@link #parseRequestHeaders(java.io.InputStream, java.nio.charset.Charset) }
	 * with default charset
	 * @param input An input stream, preferably buffered
	 * @return A map of header names and values
	 * @throws IOException 
	 */
	public static Map<String, String> parseRequestHeaders(InputStream input) throws IOException {
		return parseRequestHeaders(input, Charset.defaultCharset());
	}

	/**
	 * Read SCGI request headers from InputStream. After calling this method you
	 * can read the body of the request manually from the same stream. 
	 * @param input An input stream, preferably buffered
	 * @param charset Charset to use during decoding
	 * @return A map of header names and values
	 */
	public static Map<String, String> parseRequestHeaders(@NonNull InputStream input, @NonNull Charset charset) throws IOException {
		//Parse header length
		StringBuilder headerLengthRaw = new StringBuilder(4);
		int headerLengthInt = -1;
		char curChar;
		while ((curChar = (char) input.read()) != -1)
			if (curChar >= '0' && curChar <= '9')
				headerLengthRaw.append(curChar);
			else if (curChar == ':') {
				headerLengthInt = Integer.parseInt(headerLengthRaw.toString());
				break;
			} else {
				headerLengthRaw.append(curChar);
				throw new SCGIException("Unexpected character in SCGI header length: " + headerLengthRaw);
			}

		//Read headers
		if (headerLengthInt == -1)
			throw new SCGIException("Could not get SCGI header length: " + headerLengthRaw);
		byte[] headerBuffer = new byte[headerLengthInt];
		int readLength = input.read(headerBuffer);
		if (readLength != headerLengthInt)
			throw new SCGIException("Couldn't read all the headers (" + headerLengthInt + ").");
		String headerString = new String(headerBuffer, charset);
		if (input.read() != ',')
			throw new SCGIException("Wrong SCGI header length: " + headerLengthRaw);

		//Parse headers
		Map<String, String> env = new LinkedHashMap<String, String>();
		int headerPos = 0;
		while (headerPos < headerString.length()) {
			int valueStartSep = headerString.indexOf(0, headerPos);
			String key = headerString.substring(headerPos, valueStartSep);
			int valueEndSep = headerString.indexOf(0, valueStartSep + 1);
			String value = headerString.substring(valueStartSep + 1, valueEndSep);
			env.put(key, value);
			headerPos = valueEndSep + 1;
		}
		return env;
	}

	/**
	 * Convenience method for {@link #makeResponse(java.lang.String, java.util.Map) } with no extra headers
	 * @param body Text to send. Cannot be null
	 * @return 
	 */
	public static String makeResponse(@NonNull String body) {
		return makeResponse(body, Collections.EMPTY_MAP);
	}

	/**
	 * Make an SCGI response. The following headers will be added if they do not exist
	 * in extraHeaders:
	 * <ul>
	 * <li>Status: 200 OK</li>
	 * <li>Content-Type: text/plain</li>
	 * <li>Content-Length: [length of body]</li>
	 * </ul>
	 * @param body Text to send. Cannot be null
	 * @param extraHeaders Headers to send. Cannot be null
	 * @return A full SCGI response
	 */
	public static String makeResponse(@NonNull String body, @NonNull Map<String, String> extraHeaders) {
		//Start with our default values if they don't exist already
		StringBuilder resp = new StringBuilder();
		if (!extraHeaders.containsKey("Status"))
			resp.append(makeResponseHeader("Status", "200 OK"));
		if (!extraHeaders.containsKey("Content-Type"))
			resp.append(makeResponseHeader("Content-Type", "text/plain"));
		if (!extraHeaders.containsKey("Content-Length"))
			resp.append(makeResponseHeader("Content-Length", Integer.toString(body.length())));

		//Add users values
		for (Map.Entry<String, String> curHeader : extraHeaders.entrySet())
			resp.append(makeResponseHeader(curHeader.getKey(), curHeader.getValue()));

		resp.append(RESPONSE_LINEEND);
		resp.append(body);

		return resp.toString();
	}

	/**
	 * Utility method to create a SCGI response header in the correct format
	 * @param key Header name
	 * @param value Header value
	 * @return Formatted header
	 */
	protected static String makeResponseHeader(@NonNull String key, @NonNull String value) {
		return key + ": " + value + RESPONSE_LINEEND;
	}

	/**
	 * Convenience method for {@link #sendResponse(java.lang.String, java.util.Map, java.io.OutputStream, java.nio.charset.Charset) }
	 * with default charset
	 * @param body Text to send. Cannot be null
	 * @param extraHeaders Additional headers to send. Cannot be null
	 * @param output Stream to send response on. Cannot be null
	 * @throws IOException 
	 */
	public static void sendResponse(@NonNull String body, @NonNull Map<String, String> extraHeaders, @NonNull OutputStream output) throws IOException {
		sendResponse(body, extraHeaders, output, Charset.defaultCharset());
	}

	/**
	 * Send an SCGI response on the specified OutputStream
	 * @param body Text to send. Cannot be null
	 * @param extraHeaders Additional headers to send. Cannot be null
	 * @param output Stream to send response on. Cannot be null
	 * @param charset Charset of stream. Cannot be null
	 * @throws IOException 
	 */
	public static void sendResponse(@NonNull String body, @NonNull Map<String, String> extraHeaders, @NonNull OutputStream output, @NonNull Charset charset) throws IOException {
		output.write(makeResponse(body, extraHeaders).getBytes(charset));
	}
}
