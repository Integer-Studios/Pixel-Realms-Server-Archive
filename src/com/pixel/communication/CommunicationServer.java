package com.pixel.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import com.pixel.admin.PixelLogger;

public class CommunicationServer implements Runnable {

//	public static ArrayList<CommunicationServletHandler> connections = new ArrayList<CommunicationServletHandler>();
	public static HashMap<Integer, CommunicationServlet> userConnections = new HashMap<Integer, CommunicationServlet>();
	public int port;

	public CommunicationServer(int port) {
		
		this.port = port;
		
	}
	
	public void run() {

		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(port);
		} 
		catch (IOException e) {
			PixelLogger.err("Error: Could not listen on port " + port);
			System.exit(-1);
		}

		while (true) {
			
			Socket clientSocket;

			try {

				clientSocket = serverSocket.accept();
				clientSocket.setTcpNoDelay(true);
				new CommunicationServletHandler(clientSocket);
			} catch (IOException e) {
				PixelLogger.err("Error: Accept failed on " + port);
				System.exit(-1);
			}

		}

	}
	
}
