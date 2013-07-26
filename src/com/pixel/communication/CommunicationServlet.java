package com.pixel.communication;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import com.pixel.admin.PixelLogger;
import com.pixel.communication.packet.Packet;
import com.pixel.player.PlayerManager;

public class CommunicationServlet {

	private Socket socket;
	private int userID;
	private boolean running = false;
	private ArrayList<Packet> packetQue = new ArrayList<Packet>();

	private volatile Thread reader;
	private volatile Thread writer;
	private volatile DataInputStream input;
	private volatile DataOutputStream output;
	
	
	public CommunicationServlet(Socket socket) {

		this.socket = socket;

		try {
			this.input = new DataInputStream(socket.getInputStream());
			this.output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream(), 5120));
			this.reader = new CommunicationServletReaderThread(this);
			this.writer = new CommunicationServletWriterThread(this);
			this.running = true;
			this.reader.start();
			this.writer.start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private boolean writePacket() {
		
		if (packetQue.size() > 0) {
			
			Packet packet = packetQue.get(0);
			if (packet != null) {
				try {
					Packet.writePacket(packet, this);
					output.flush();
				} catch (IOException e) {
					if (isRunning(this)) {
						PixelLogger.err("Lost connection to " + PlayerManager.getUsername(userID) + ".");
						disconnectServlet(this);

					}
				}
				packetQue.remove(0);
				return true;
				
			}

		}
		
		return false;
		
	}
	
	private boolean readPacket() {
		
		Packet packet = Packet.readPacket(this);
		
		if (packet != null)
			return true;
		else		
			return false;
		
	}
	
	public static void writeNetworkPacket(CommunicationServlet servlet) {
		
		servlet.writePacket();
		
	}
	
	public static void readNetworkPacket(CommunicationServlet servlet) {
		
		servlet.readPacket();
		
	}
	
	public static DataOutputStream getOutput(CommunicationServlet servlet) {
		
		return servlet.output;
		
	}
	
	public static DataInputStream getInput(CommunicationServlet servlet) {
		
		return servlet.input;
		
	}
	
	public static boolean isRunning(CommunicationServlet servlet) {
		
		return servlet.running;
		
	}
	
	public static void addPacket(CommunicationServlet servlet, Packet packet) {
		
		servlet.packetQue.add(packet);
		
	}
	
	public static int getUserID(CommunicationServlet servlet) {
		
		return servlet.userID;
		
	}
	
	public static void setUserID(CommunicationServlet servlet, int userID) {
		
		servlet.userID = userID;
		CommunicationServer.userConnections.put(userID, servlet);
		
	}
	
	public static void disconnectServlet(CommunicationServlet servlet) {
		
		try {

			servlet.output.close();
			servlet.input.close();
			servlet.socket.close();
			servlet.running = false;
			servlet.reader = null;
			servlet.writer = null;

			CommunicationServer.userConnections.remove(servlet.userID);
			PlayerManager.playerLogout(servlet.userID);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
