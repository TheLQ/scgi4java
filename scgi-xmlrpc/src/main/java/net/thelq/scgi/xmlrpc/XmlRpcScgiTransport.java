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
package net.thelq.scgi.xmlrpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientException;
import org.apache.xmlrpc.client.XmlRpcHttpClientConfig;
import org.apache.xmlrpc.client.XmlRpcStreamTransport;
import org.apache.xmlrpc.common.XmlRpcStreamRequestConfig;
import net.thelq.scgi4java.scgi.SCGIClient;
import org.xml.sax.SAXException;

/**
 *
 * @author Leon
 */
@Slf4j
public class XmlRpcScgiTransport extends XmlRpcStreamTransport {
	protected final Charset charset;
	protected Socket rtSocket;

	public XmlRpcScgiTransport(XmlRpcClient pClient, Charset charset) {
		super(pClient);
		this.charset = charset;
	}

	@Override
	public Object sendRequest(XmlRpcRequest pRequest) throws XmlRpcException {
		try {
			XmlRpcHttpClientConfig config = (XmlRpcHttpClientConfig) pRequest.getConfig();
			rtSocket = new Socket(config.getServerURL().getHost(), config.getServerURL().getPort());
		} catch (IOException ex) {
			throw new XmlRpcClientException("Cannot connect to server", ex);
		}
		return super.sendRequest(pRequest);
	}

	@Override
	protected void writeRequest(ReqWriter pWriter) throws XmlRpcException, IOException, SAXException {
		//Convert output to string so we can get the length of it
		ByteArrayOutputStream reqOutput = new ByteArrayOutputStream();
		pWriter.write(reqOutput);

		//Write SCGI request to socket
		log.debug("Writing SCGI request");
		OutputStreamWriter rtWrite = new OutputStreamWriter(rtSocket.getOutputStream());
		rtWrite.write(SCGIClient.makeRequest(reqOutput.toString()));
		rtWrite.flush();
		log.debug("Finished writing SCGI request");
	}

	@Override
	protected InputStream getInputStream() throws XmlRpcException {
		try {
			log.debug("Reading SCGI headers");
			//Siphon off headers
			InputStream rtRead = rtSocket.getInputStream();
			SCGIClient.parseResponse(rtRead);
			log.debug("Finished reading SCGI headers");
			return rtRead;
		} catch (IOException e) {
			throw new XmlRpcClientException("Could not get input stream", e);
		}
	}

	@Override
	protected void close() throws XmlRpcClientException {
		try {
			rtSocket.close();
		} catch (IOException ex) {
			throw new XmlRpcClientException("Could not close socket", ex);
		}
	}

	@Override
	protected boolean isResponseGzipCompressed(XmlRpcStreamRequestConfig pConfig) {
		return false;
	}
}
