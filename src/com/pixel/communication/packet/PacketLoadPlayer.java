package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.pixel.entity.EntityPlayer;
import com.pixel.player.PlayerInventory;

public class PacketLoadPlayer extends Packet{

	public String username;
	public float posX, posY, health, satisfaction, energy;
	public int itemID, itemAmount, worldID;
	public PlayerInventory inventory;
	public int serverID;
	
	public PacketLoadPlayer() {this.id = 18;}
	
	public PacketLoadPlayer(EntityPlayer player) {
		
		this.username = player.username;
		this.posX = player.getX();
		this.posY = player.getY();
		this.health = player.health;
		this.satisfaction = player.satisfaction;
		this.energy = player.energy;
		this.userID = player.userID;
		this.worldID = player.worldID;
		this.id = 18;
		
	}
	
	@Override
	public void writeData(DataOutputStream output) throws IOException {
		Packet.writeString(username, output);
		output.writeFloat(this.posX);
		output.writeFloat(this.posY);
		output.writeFloat(this.health);
		output.writeFloat(this.satisfaction);
		output.writeFloat(this.energy);
		output.writeInt(this.itemID);
		output.writeInt(this.itemAmount);
		output.writeInt(this.worldID);
		output.writeInt(this.serverID);
		
		inventory.sendInventory(output);

	}

	@Override
	public void readData(DataInputStream input) throws IOException {

		this.username = Packet.readString(16, input);
		
		PacketHandler.processLoadPlayer(this);
		
	}
	
}
