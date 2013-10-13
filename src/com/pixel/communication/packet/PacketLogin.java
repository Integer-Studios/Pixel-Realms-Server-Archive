package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.pixel.entity.EntityPlayer;

public class PacketLogin extends Packet {

	public String username;
	public float posX, posY, health, satisfaction, energy;
	public int itemID, itemAmount, worldID, session;
	
	public PacketLogin () {
		this.id = 1;
	}
	
	public PacketLogin(EntityPlayer player) {

		this.id = 1;
		this.username = player.username; 
		this.userID = player.userID;
		this.posX = player.posX;
		this.posY = player.posY;
		this.health = player.health;
		this.session = player.session;
		this.energy = player.energy;
		this.satisfaction = player.satisfaction;
		this.worldID = player.worldID;
		
	}
	
	public void writeData(DataOutputStream output) throws IOException {

		Packet.writeString(username, output);
		output.writeFloat(this.posX);
		output.writeFloat(this.posY);
		output.writeFloat(this.health);
		output.writeFloat(this.energy);
		output.writeFloat(this.satisfaction);
		output.writeInt(0);
		output.writeInt(worldID);

	}

	@Override
	public void readData(DataInputStream input) throws IOException {

		this.username = Packet.readString(16, input);
		this.session = input.readInt();

		PacketHandler.processLoginPacket(this);
		
	}

}
