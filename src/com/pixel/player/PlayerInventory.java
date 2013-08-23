package com.pixel.player;

import java.util.concurrent.ConcurrentHashMap;

import com.pixel.communication.CommunicationServer;
import com.pixel.communication.CommunicationServlet;
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
	
	public void sendInventory() {
				
		for (InventoryContent c : hotbar.content.values()) {
			
			CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), new PacketUpdateInventoryContent(c.x,c.y,c.itemstack.item.id, c.itemstack.size, 0));

		}
		
		for (InventoryContent c : inventoryLeft.content.values()) {

			CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), new PacketUpdateInventoryContent(c.x,c.y,c.itemstack.item.id, c.itemstack.size, 1));

		}

		for (InventoryContent c : inventoryRight.content.values()) {

			CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), new PacketUpdateInventoryContent(c.x,c.y,c.itemstack.item.id, c.itemstack.size, 2));

		}
		
	}
	
}
