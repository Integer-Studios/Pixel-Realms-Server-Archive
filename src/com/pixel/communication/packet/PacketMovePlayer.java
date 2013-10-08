package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.pixel.entity.EntityAlive;
import com.pixel.player.PlayerManager;

public class PacketMovePlayer extends Packet {

	EntityAlive entity;
	int userID;
	int speed;
	
	public PacketMovePlayer() {
		this.id = 16;
	}
	
	public PacketMovePlayer(int userID, boolean n, boolean w, boolean e, boolean s, int speed) {
		
		this.id = 16;
		this.userID = userID;
		this.speed = speed;
		
	}
	
	public void writeData(DataOutputStream output) throws IOException {

		output.writeInt(userID);
		output.writeFloat(.07F);
		
	}

	public void readData(DataInputStream input) throws IOException {

		userID = input.readInt();

		if (PlayerManager.players.containsKey(userID)) {

			PlayerManager.players.get(userID).speed = speed;
			
			PlayerManager.broadcastPacket(this);

		}

	}

}
