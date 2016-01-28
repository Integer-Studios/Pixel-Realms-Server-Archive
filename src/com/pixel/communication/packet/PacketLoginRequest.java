package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketLoginRequest extends Packet {

	public int session;
	public int userID;
	public int serverID;
	public String username;
	
	public PacketLoginRequest() {this.id = 2;}
	
	public PacketLoginRequest(int userID, int session) {

		this.id = 2;
		this.userID = userID;
		this.session = session;
		
	}
	
	@Override
	public void writeData(DataOutputStream output) throws IOException {

		output.writeInt(userID);
		output.writeInt(session);
		output.writeInt(serverID);

	}

	@Override
	public void readData(DataInputStream input) throws IOException {

		userID = input.readInt();
		session = input.readInt();
		username = Packet.readString(16, input);
		PacketHandler.processLoginRequest(this);
		
	}

}
