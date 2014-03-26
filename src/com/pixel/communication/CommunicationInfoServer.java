package com.pixel.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.pixel.admin.PixelLogger;
import com.pixel.start.PixelRealmsServer;

public class CommunicationInfoServer implements Runnable {

	public int port;
	
	public CommunicationInfoServer() {
		
		port = PixelRealmsServer.port + 1;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
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
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
				String cmd = in.readLine();
				
				if (cmd.equals("status")) {
					
					out.println("1");
					
				}
			
			} catch (IOException e) {
				PixelLogger.err("Error: Accept failed on " + port);
				System.exit(-1);
			}

		}
	}

}
