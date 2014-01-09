/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thelq.scgi4java.scgi;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author Leon
 */
public class SCGIServerTest {
	@Test
	public void parseRequestHeadersTest() throws IOException {
		Map<String, String> parsedHeaders = SCGIServer.parseRequestHeaders(new ByteArrayInputStream(TestUtils.REQUEST_RAW.getBytes()), Charset.defaultCharset());
		
		//Make sure there are no differences
		MapDifference<String, String> headerDiff = Maps.difference(parsedHeaders, TestUtils.REQUEST_HEADERS);
		Assert.assertEquals(headerDiff.entriesDiffering().size(), 0, "Headers do not match given " + headerDiff);
	}
	
	@Test
	public void makeResponseTest() {
		String createdResponse = SCGIServer.makeResponse(TestUtils.RESPONSE_BODY, TestUtils.RESPONSE_HEADERS);
		
		Assert.assertEquals(createdResponse, TestUtils.RESPONSE_RAW, "Response doesn't match given");
	}
}
