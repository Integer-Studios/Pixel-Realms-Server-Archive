package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.pixel.entity.EntityAlive;
import com.pixel.player.PlayerManager;

public class PacketMoveLivingEntity extends Packet {

	EntityAlive entity;
	int serverID, entityID;
	public float velocityX, velocityY, posX, posY;
	
	public PacketMoveLivingEntity() {
		this.id = 15;
	}
	
	public PacketMoveLivingEntity(EntityAlive entity) {
		
		this.id = 15;
		this.entity = entity;
		this.entityID = entity.id;
		this.serverID = entity.serverID;
		this.velocityX = entity.velocityX;
		this.velocityY = entity.velocityY;
		
	}
	
	public void writeData(DataOutputStream output) throws IOException {

		output.writeInt(entityID);
		output.writeInt(serverID);
		output.writeFloat(velocityX);
		output.writeFloat(velocityY);
		
	}

	public void readData(DataInputStream input) throws IOException {

		serverID = input.readInt();
		velocityX = input.readFloat();
		velocityY = input.readFloat();
		posX = input.readFloat();
		posY = input.readFloat();
		
		if (PlayerManager.players.containsKey(userID)) {

			PacketHandler.processMoveLivingEntity(this);
			
		}
		
		
	}

}
