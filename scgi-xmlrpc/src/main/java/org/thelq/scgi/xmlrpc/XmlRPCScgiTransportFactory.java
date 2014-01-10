/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thelq.scgi.xmlrpc;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcTransport;
import org.apache.xmlrpc.client.XmlRpcTransportFactoryImpl;

/**
 *
 * @author Leon
 */
public class XmlRPCScgiTransportFactory extends XmlRpcTransportFactoryImpl {
	public XmlRPCScgiTransportFactory(XmlRpcClient pClient) {
		super(pClient);
	}

	public XmlRpcTransport getTransport() {
		return new XmlRpcScgiTransport(getClient());
	}
}
