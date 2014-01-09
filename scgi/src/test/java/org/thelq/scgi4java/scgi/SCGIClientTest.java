/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thelq.scgi4java.scgi;

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
}
