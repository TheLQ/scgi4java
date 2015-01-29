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
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * Utility class for SCGI servers
 *
 * @author Leon
 */
public class SCGIServer {
	protected static final String RESPONSE_LINE_END = "\r\n";

	/**
	 * Convenience method for {@link #parseRequestHeaders(java.io.InputStream, java.nio.charset.Charset)
	 * }
	 * with default charset
	 *
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
	 *
	 * @param input An input stream, preferably buffered
	 * @param charset Charset to use during decoding
	 * @return A map of header names and values
	 */
	public static Map<String, String> parseRequestHeaders(@Nonnull InputStream input, @Nonnull Charset charset) throws IOException {
		if (input == null)
			throw new NullPointerException("input cannot be null");
		if (charset == null)
			throw new NullPointerException("charset cannot be null");

		//Parse header length
		StringBuilder headerLengthRaw = new StringBuilder(4);
		int headerLengthInt = -1;
		char curChar;
		while ((curChar = (char) input.read()) != (char) -1)
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
	 * Convenience method for {@link #createResponse(java.lang.String, java.util.Map)
	 * } with no extra headers
	 *
	 * @param body Text to send. Cannot be null
	 * @return
	 */
	public static String createResponse(@Nonnull String body) {
		return createResponse(body, Collections.EMPTY_MAP);
	}

	/**
	 * Make an SCGI response. The following headers will be added if they do not
	 * exist in extraHeaders:
	 * <ul>
	 * <li>Status: 200 OK</li>
	 * <li>Content-Type: text/plain</li>
	 * <li>Content-Length: [length of body]</li>
	 * </ul>
	 *
	 * @param body Text to send. Cannot be null
	 * @param extraHeaders Headers to send. Cannot be null
	 * @return A full SCGI response
	 */
	public static String createResponse(@Nonnull String body, @Nonnull Map<String, String> extraHeaders) {
		if (body == null)
			throw new NullPointerException("body cannot be null");
		if (extraHeaders == null)
			throw new NullPointerException("extraHeaders cannot be null");

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

		resp.append(RESPONSE_LINE_END);
		resp.append(body);

		return resp.toString();
	}

	/**
	 * Utility method to create a SCGI response header in the correct format
	 *
	 * @param key Header name
	 * @param value Header value
	 * @return Formatted header
	 */
	protected static String makeResponseHeader(@Nonnull String key, @Nonnull String value) {
		if (key == null)
			throw new NullPointerException("key cannot be null");
		if (value == null)
			throw new NullPointerException("value cannot be null");

		return key + ": " + value + RESPONSE_LINE_END;
	}

	/**
	 * Convenience method for {@link #sendResponse(java.lang.String, java.util.Map, java.io.OutputStream, java.nio.charset.Charset)
	 * }
	 * with default charset
	 *
	 * @param body Text to send. Cannot be null
	 * @param extraHeaders Additional headers to send. Cannot be null
	 * @param output Stream to send response on. Cannot be null
	 * @throws IOException
	 */
	public static void sendResponse(@Nonnull String body, @Nonnull Map<String, String> extraHeaders, @Nonnull OutputStream output) throws IOException {
		sendResponse(body, extraHeaders, output, Charset.defaultCharset());
	}

	/**
	 * Send an SCGI response on the specified OutputStream
	 *
	 * @param body Text to send. Cannot be null
	 * @param extraHeaders Additional headers to send. Cannot be null
	 * @param output Stream to send response on. Cannot be null
	 * @param charset Charset of stream. Cannot be null
	 * @throws IOException
	 */
	public static void sendResponse(@Nonnull String body, @Nonnull Map<String, String> extraHeaders, @Nonnull OutputStream output, @Nonnull Charset charset) throws IOException {
		if (body == null)
			throw new NullPointerException("body cannot be null");
		if (extraHeaders == null)
			throw new NullPointerException("extraHeaders cannot be null");
		if (output == null)
			throw new NullPointerException("output cannot be null");
		if (charset == null)
			throw new NullPointerException("charset cannot be null");

		output.write(createResponse(body, extraHeaders).getBytes(charset));
	}
}
