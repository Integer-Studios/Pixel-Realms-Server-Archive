package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.pixel.entity.EntityAlive;
import com.pixel.player.PlayerManager;

public class PacketMovePlayer extends Packet {

	EntityAlive entity;
	float velocityX, velocityY, posX, posY;
	float speed;
	
	public PacketMovePlayer() {
		this.id = 16;
	}
	
	public PacketMovePlayer(int userID, float velocityX, float velocityY, float posX, float posY, int speed) {
		
		this.id = 16;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.posX = posX;
		this.posY = posY;
		this.userID = userID;
		this.speed = speed;
		
	}
	
	public void writeData(DataOutputStream output) throws IOException {

		output.writeInt(userID);
		output.writeFloat(velocityX);
		output.writeFloat(velocityY);
		output.writeFloat(posX);
		output.writeFloat(posY);
		
	}

	public void readData(DataInputStream input) throws IOException {

		userID = input.readInt();
		velocityX = input.readFloat();
		velocityY = input.readFloat();
		posX = input.readFloat();
		posY = input.readFloat();
		
		if (PlayerManager.players.containsKey(userID)) {

			PacketHandler.processMovePlayer(this);
			
		}

	}

}
