package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.pixel.entity.EntityAlive;
import com.pixel.entity.EntityAnimal;

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

			if (entity instanceof EntityAnimal) {
				
				EntityAnimal animal = (EntityAnimal) entity;
				
				output.writeFloat(animal.getX());
				output.writeFloat(animal.getY());
				output.writeFloat(animal.health);
				output.writeInt(animal.id);
				output.writeBoolean(true);
				output.writeInt(animal.herdID);
				output.writeInt(animal.serverID);

			} else {

				output.writeFloat(entity.getX());
				output.writeFloat(entity.getY());
				output.writeFloat(entity.health);
				output.writeInt(entity.id);
				output.writeBoolean(false);
				output.writeInt(entity.serverID);

			}
		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	@Override
	public void readData(DataInputStream input) throws IOException {

		
		
	}
	
}
