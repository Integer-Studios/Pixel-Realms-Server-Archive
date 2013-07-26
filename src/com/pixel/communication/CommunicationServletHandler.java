package com.pixel.communication;

import java.net.Socket;

public class CommunicationServletHandler {

	public CommunicationServlet servlet;
	
	public CommunicationServletHandler (Socket socket) {
		
		this.servlet = new CommunicationServlet(socket);
		
	}
	
}
