/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thelq.scgi.xmlrpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientException;
import org.apache.xmlrpc.client.XmlRpcHttpClientConfig;
import org.apache.xmlrpc.client.XmlRpcStreamTransport;
import org.apache.xmlrpc.common.XmlRpcStreamRequestConfig;
import org.thelq.scgi4java.scgi.SCGIClient;
import org.xml.sax.SAXException;

/**
 *
 * @author Leon
 */
public class XmlRpcScgiTransport extends XmlRpcStreamTransport {
	protected Socket rtSocket;

	public XmlRpcScgiTransport(XmlRpcClient pClient) {
		super(pClient);
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
		System.out.println("About to write request");
		//Convert output to string so we can get the length of it
		ByteArrayOutputStream reqOutput = new ByteArrayOutputStream();
		pWriter.write(reqOutput);

		//Write SCGI request to socket
		OutputStreamWriter rtWrite = new OutputStreamWriter(rtSocket.getOutputStream());
		rtWrite.write(SCGIClient.makeRequest(reqOutput.toString()));
		rtWrite.flush();
		System.out.println("Finished writing request");
	}

	@Override
	protected InputStream getInputStream() throws XmlRpcException {
		try {
			System.out.println("About to read request");
			//Siphon off headers
			InputStream rtRead = rtSocket.getInputStream();
			SCGIClient.parseResponse(rtRead);
			System.out.println("Siphoned headers finished");
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
