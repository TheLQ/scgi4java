/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thelq.scgi4java.scgi;

import java.io.IOException;

/**
 *
 * @author Leon
 */
public class SCGIException extends IOException {
	public SCGIException(String message) {
		super(message);
	}

	public SCGIException(String message, Throwable cause) {
		super(message, cause);
	}
}
