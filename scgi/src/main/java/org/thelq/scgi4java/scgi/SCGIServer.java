/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thelq.scgi4java.scgi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Leon
 */
public class SCGIServer {
	public static Map<String, String> parseRequest(InputStream input) throws IOException {
		return parseRequest(Charset.defaultCharset(), input);
	}
	
	/**
	 * Read the <a href="http://python.ca/scgi/protocol.txt">SCGI</a> request
	 * headers.<br>
	 * After the headers had been loaded, you can read the body of the request
	 * manually from the same {@code input} stream:
	 * <p>
	 * <pre>
	 * // Load the SCGI headers.
	 * Socket clientSocket = socket.accept();
	 * BufferedInputStream bis = new BufferedInputStream(
	 * clientSocket.getInputStream(), 4096);
	 * HashMap&lt;String, String&gt; env = SCGI.parse(bis);
	 * // Read the body of the request.
	 * bis.read(new byte[Integer.parseInt(env.get(&quot;CONTENT_LENGTH&quot;))]);
	 * </pre>
	 * <p>
	 * @param input
	 * an efficient (buffered) input stream.
	 * @return strings passed via the SCGI request.
	 */
	public static Map<String, String> parseRequest(Charset charset, InputStream input) throws IOException {
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
}
