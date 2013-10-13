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
	int worldID;
	
	public PacketMovePlayer() {
		this.id = 16;
	}
	
	public PacketMovePlayer(int userID) {
		
		this.id = 16;
		this.userID = userID;
		this.worldID = PlayerManager.getPlayer(userID).worldID;
		this.velocityX = PlayerManager.getPlayer(userID).velocityX;
		this.velocityY = PlayerManager.getPlayer(userID).velocityY;
		this.posX = PlayerManager.getPlayer(userID).posX;
		this.posY = PlayerManager.getPlayer(userID).posY;
		this.speed = PlayerManager.getPlayer(userID).speed;
		
	}
	
	public void writeData(DataOutputStream output) throws IOException {

		output.writeInt(userID);
		output.writeFloat(velocityX);
		output.writeFloat(velocityY);
		output.writeFloat(posX);
		output.writeFloat(posY);
		output.writeInt(PlayerManager.getPlayer(userID).worldID);
		
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
