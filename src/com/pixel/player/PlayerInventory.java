package com.pixel.player;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.pixel.communication.CommunicationServer;
import com.pixel.communication.CommunicationServlet;
import com.pixel.communication.packet.PacketLoginStage;
import com.pixel.communication.packet.PacketUpdateInventoryContent;
import com.pixel.inventory.Inventory;
import com.pixel.inventory.InventoryContent;
import com.pixel.util.CoordinateKey;

public class PlayerInventory {
	
	public int userID;
	public Inventory hotbar, inventoryLeft, inventoryRight;

	public PlayerInventory(int userID) {
		this.userID = userID;
		hotbar = new Inventory(userID, 10, 1, 0);
		inventoryLeft = new Inventory(userID, 3, 6, 1);
		inventoryRight = new Inventory(userID, 3, 6, 2);
	}
	
	public PlayerInventory(int userID,  ConcurrentHashMap<CoordinateKey, InventoryContent> hotbar,   ConcurrentHashMap<CoordinateKey, InventoryContent> inventoryLeft,   ConcurrentHashMap<CoordinateKey, InventoryContent> inventoryRight) {
		this.userID = userID;
		this.hotbar = new Inventory(userID, 10, 1, hotbar, 0);
		this.inventoryLeft = new Inventory(userID, 3, 6, inventoryLeft, 1);
		this.inventoryRight = new Inventory(userID, 3, 6, inventoryRight, 2);
	}
	
	public void sendInventory(DataOutputStream output) throws IOException {
		
		output.writeInt(hotbar.content.size());
		for (InventoryContent c : hotbar.content.values()) {
			
			CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), new PacketUpdateInventoryContent(c.x,c.y,c.itemstack.item.id, c.itemstack.size, 0));
			output.writeInt(c.x);
			output.writeInt(c.y);
			output.writeInt(c.itemstack.item.id);
			output.writeInt(c.itemstack.size);
			output.writeInt(0);

		}
		
		output.writeInt(inventoryLeft.content.size());
		for (InventoryContent c : inventoryLeft.content.values()) {

			CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), new PacketUpdateInventoryContent(c.x,c.y,c.itemstack.item.id, c.itemstack.size, 1));
			output.writeInt(c.x);
			output.writeInt(c.y);
			output.writeInt(c.itemstack.item.id);
			output.writeInt(c.itemstack.size);
			output.writeInt(0);
			
		}

		output.writeInt(inventoryRight.content.size());
		for (InventoryContent c : inventoryRight.content.values()) {

			CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), new PacketUpdateInventoryContent(c.x,c.y,c.itemstack.item.id, c.itemstack.size, 2));
			output.writeInt(c.x);
			output.writeInt(c.y);
			output.writeInt(c.itemstack.item.id);
			output.writeInt(c.itemstack.size);
			output.writeInt(0);
			
		}
		
//		CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), new PacketLoginStage(2, 1F));
		
	}
	
}
