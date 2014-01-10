/**
 * Copyright (C) 2010-2013 Leon Blakey <lord.quackstar at gmail.com>
 *
 * This file is part of scgi4java.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, softwar
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
