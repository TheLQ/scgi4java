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

		//Read body
		Assert.assertEquals(TestUtils.inputToString(responseStream), TestUtils.RESPONSE_BODY, "Body does not match given");
	}
}
