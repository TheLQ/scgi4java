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

import java.nio.charset.Charset;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcTransport;
import org.apache.xmlrpc.client.XmlRpcTransportFactoryImpl;

/**
 *
 * @author Leon
 */
public class XmlRPCScgiTransportFactory extends XmlRpcTransportFactoryImpl {
	protected final Charset charset;

	public XmlRPCScgiTransportFactory(XmlRpcClient pClient) {
		super(pClient);
		charset = Charset.defaultCharset();
	}

	public XmlRPCScgiTransportFactory(XmlRpcClient pClient, Charset charset) {
		super(pClient);
		this.charset = charset;
	}

	public XmlRpcTransport getTransport() {
		return new XmlRpcScgiTransport(getClient(), charset);
	}
}
