package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.pixel.admin.PixelLogger;
import com.pixel.communication.CommunicationServlet;
import com.pixel.player.PlayerManager;

public abstract class Packet {
	
	public CommunicationServlet servletLogin;
	public int id;
	public int userID;
	@SuppressWarnings("rawtypes")
	private static HashMap<Integer, Class> packetMap = new HashMap<Integer, Class>();
	
	public static void writePacket(Packet packet, CommunicationServlet servlet) {
		try {
			
			CommunicationServlet.getOutput(servlet).writeInt(packet.id);
			
			CommunicationServlet.getOutput(servlet).writeInt(packet.userID);

			packet.writeData(CommunicationServlet.getOutput(servlet));

		} catch (IOException e) {
			PixelLogger.err("Lost connection to " + PlayerManager.getUsername(CommunicationServlet.getUserID(servlet)) + ".");

			if (CommunicationServlet.isRunning(servlet))
				CommunicationServlet.disconnectServlet(servlet);

		}

	}

	public static Packet readPacket(CommunicationServlet servlet) {

		try {

			int id = CommunicationServlet.getInput(servlet).readInt();
			int userID = CommunicationServlet.getInput(servlet).readInt();
			Packet packet = getPacket(id);
			packet.id = id;
			packet.userID = userID;
			if (id == 1) 
				packet.servletLogin = servlet;
				
			packet.readData(CommunicationServlet.getInput(servlet));

			return packet;

		} catch (IOException e) {
			try {
			if (CommunicationServlet.isRunning(servlet)) {
				PixelLogger.err("Lost connection to " + PlayerManager.getUsername(CommunicationServlet.getUserID(servlet)) + ".");
				CommunicationServlet.disconnectServlet(servlet);

			}
			} catch(NullPointerException ex) {
				PixelLogger.err("Lost connection to player.");
				CommunicationServlet.disconnectServlet(servlet);
				
			}
		} 

		return null;

	}

	public static void writeString(String string, DataOutputStream output) throws IOException {
		if (string.length() > 32767)
		{
			throw new IOException("String too big");
		}
		else
		{
			output.writeShort(string.length());
			output.writeChars(string);
		}
	}

	public static String readString(int length, DataInputStream input) throws IOException {
		short lengthRead = input.readShort();

		if (lengthRead > length)
		{
			throw new IOException("Received string length longer than maximum allowed (" + lengthRead + " > " + length + ")");
		}
		else if (lengthRead < 0)
		{
			throw new IOException("Received string length is less than zero! Weird string!");
		}
		else
		{
			StringBuilder builder = new StringBuilder();

			for (int x = 0; x < lengthRead; ++x)
			{
				builder.append(input.readChar());
			}

			return builder.toString();
		}
	}

	@SuppressWarnings("rawtypes")
	public static Packet getPacket(int id) {
        try
        {
            Class packetClass = (Class)packetMap.get(id);
            return packetClass == null ? null : (Packet)packetClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Skipping packet with id " + id);
            return null;
        }
    }
	
	public abstract void writeData(DataOutputStream output) throws IOException;

	public abstract void readData(DataInputStream input) throws IOException;
	
	static {
		
		packetMap.put(0, PacketBlank.class);
		packetMap.put(1, PacketLogin.class);
		packetMap.put(2, PacketUpdatePlayer.class);
		packetMap.put(3, PacketWorldData.class);
		packetMap.put(4, PacketUpdateTile.class);
		packetMap.put(5, PacketUpdatePiece.class);
		packetMap.put(6, PacketUpdateWorld.class);
		packetMap.put(7, PacketUpdateLivingEntity.class);
		packetMap.put(8, PacketChat.class);
		packetMap.put(9, PacketLogout.class);
		packetMap.put(10, PacketDamagePiece.class);
		packetMap.put(11, PacketUpdateInventoryContent.class);
		packetMap.put(12, PacketDamageEntity.class);
		packetMap.put(13, PacketDamagePlayer.class);

	}
	
}
