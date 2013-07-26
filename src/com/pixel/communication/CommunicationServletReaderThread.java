package com.pixel.communication;

import com.pixel.admin.PixelLogger;
import com.pixel.player.PlayerManager;

public class CommunicationServletReaderThread extends Thread {

	CommunicationServlet servlet;
	
	public CommunicationServletReaderThread (CommunicationServlet servlet) {
		
		this.servlet = servlet;
		
	}
	
	public void run() {

		while (CommunicationServlet.isRunning(servlet)) {

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (CommunicationServlet.isRunning(servlet)) 
				CommunicationServlet.readNetworkPacket(servlet);
			else
				break;
			
		}
		
		if (CommunicationServlet.isRunning(servlet)) {
			PixelLogger.err("Lost connection to " + PlayerManager.getUsername(CommunicationServlet.getUserID(servlet)) + ".");
			CommunicationServlet.disconnectServlet(servlet);
		}
		
	}
	
}
