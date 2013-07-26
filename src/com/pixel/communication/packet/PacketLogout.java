package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketLogout extends Packet {

	public PacketLogout(){}
	
	public PacketLogout(int userID) {
		
		this.userID = userID;
		this.id = 9;
		
	}
	
	@Override
	public void writeData(DataOutputStream output) throws IOException {

		
	}

	@Override
	public void readData(DataInputStream input) throws IOException {

//		PlayerManager.playerLogout(this.userID);
		
	}

}
