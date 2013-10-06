package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.pixel.entity.EntityAlive;

public class PacketUpdateLivingEntity extends Packet {

	EntityAlive entity;
	
	public PacketUpdateLivingEntity() {
		
		this.id = 7;
		
	}
	
	public PacketUpdateLivingEntity(EntityAlive entity) {
		
		this.id = 7;
		this.entity = entity;
		
	}

	@Override
	public void writeData(DataOutputStream output) {

		try {

			output.writeFloat(entity.getX());
			output.writeFloat(entity.getY());
			output.writeFloat(entity.health);
			output.writeInt(entity.id);
			output.writeInt(entity.serverID);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	@Override
	public void readData(DataInputStream input) throws IOException {

		
		
	}
	
}
