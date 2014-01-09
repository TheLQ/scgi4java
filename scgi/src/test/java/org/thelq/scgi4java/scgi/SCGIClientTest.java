/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thelq.scgi4java.scgi;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author Leon
 */
public class SCGIClientTest {
	@Test
	public void makeRequestTest() {
		String requestCreated = SCGIClient.makeRequest(TestUtils.REQUEST_BODY, TestUtils.REQUEST_HEADERS);

		Assert.assertEquals(TestUtils.REQUEST_RAW, requestCreated, "Generated request doesn't match given");
	}
	
	@Test
	public void parseResponseTest() throws IOException {
		InputStream responseStream = new ByteArrayInputStream(TestUtils.RESPONSE_RAW.getBytes());
		Map<String, String> parsedResponse = SCGIClient.parseResponse(responseStream);
		
		//Check response againast actual values
		ImmutableMap<String, String> fullHeaders = ImmutableMap.<String, String>builder()
				.putAll(TestUtils.RESPONSE_HEADERS)
				.put("Status", "200 OK")
				.put("Content-Type", "text/plain")
				.put("Content-Length", "5")
				.build();
		MapDifference<String, String> headerDiff = Maps.difference(parsedResponse, fullHeaders);
		Assert.assertEquals(headerDiff.entriesDiffering().size(), 0, "Headers do not match given" + headerDiff);
	}
}
