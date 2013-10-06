package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.pixel.entity.EntityAlive;

public class PacketDamageEntity extends Packet {

	EntityAlive entity;
	int serverID;
	int herdID = -1;
	float damage;

	public PacketDamageEntity() {
		
		this.id = 12;
		
	}
	
	public PacketDamageEntity(EntityAlive entity) {
		
		this.id = 12;
		this.entity = entity;
		
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {

		output.writeInt(entity.serverID);
		output.writeFloat(entity.health);

	}

	@Override
	public void readData(DataInputStream input) throws IOException {

		this.serverID = input.readInt();
		this.damage = input.readFloat();

		PacketHandler.processDamageEntity(this);

	}
	
}
