package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.pixel.entity.EntityPlayer;
import com.pixel.item.ItemStack;
import com.pixel.player.PlayerManager;

public class PacketUpdatePlayer extends Packet {

	public String username;
	public float posX, posY, health, satisfaction, energy;
	public int itemID, itemAmount;
	
	public PacketUpdatePlayer() {
		this.id = 2;
	}
	
	public PacketUpdatePlayer(EntityPlayer player) {
		
		this(player.username, player.getX(), player.getY(), player.health, player.satisfaction, player.energy, player.userID, player.selectedItem);
		
	}
	
	public PacketUpdatePlayer(String username, float posX, float posY, float health, float satisfaction, float energy, int userID, ItemStack stack) {
		// TODO Auto-generated constructor stub
		this.id = 2;
		this.username = username; 
		this.posX = posX;
		this.posY = posY;
		this.health = health;
		this.satisfaction = satisfaction;
		this.energy = energy;
		this.userID = userID;
		if (stack != null) {
			this.itemID = stack.item.id;
			this.itemAmount = stack.size;
		}
	}
	
	public PacketUpdatePlayer(int userID) {
		// TODO Auto-generated constructor stub
		EntityPlayer p = PlayerManager.getPlayer(userID);
		
		this.id = 2;
		this.username = p.username; 
		this.posX = p.getX();
		this.posY = p.getY();
		this.health = p.health;
		this.satisfaction = p.satisfaction;
		this.energy = p.energy;
		this.userID = userID;
		if (p.selectedItem != null) {
			this.itemID = p.selectedItem.item.id;
			this.itemAmount = p.selectedItem.size;
		}
	}

	@Override
	public void writeData(DataOutputStream output) {

		try {
			
			Packet.writeString(username, output);
			output.writeFloat(this.posX);
			output.writeFloat(this.posY);
			output.writeFloat(this.health);
			output.writeFloat(this.satisfaction);
			output.writeFloat(this.energy);
			output.writeInt(this.itemID);
			output.writeInt(this.itemAmount);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void readData(DataInputStream input) {

		try {
			this.username = Packet.readString(16, input);
			
			this.posX = input.readFloat();
			this.posY = input.readFloat();

			this.health = input.readFloat();
			this.satisfaction = input.readFloat();
			this.energy = input.readFloat();
			this.itemID = input.readInt();
			this.itemAmount = input.readInt();
			
			PacketHandler.processUpdatePlayer(this);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
