package com.pixel.player;

import java.util.ArrayList;

import com.pixel.communication.CommunicationServer;
import com.pixel.communication.CommunicationServlet;
import com.pixel.communication.packet.PacketUpdateInventoryContent;
import com.pixel.inventory.Inventory;
import com.pixel.inventory.InventoryContent;

public class PlayerInventory {
	
	public int userID;
	public Inventory hotbar, inventoryLeft, inventoryRight;

	public PlayerInventory(int userID) {
		this.userID = userID;
		hotbar = new Inventory(userID, 10, 1, 0);
		inventoryLeft = new Inventory(userID, 3, 6, 1);
		inventoryRight = new Inventory(userID, 3, 6, 2);
	}
	
	public PlayerInventory(int userID, ArrayList<InventoryContent> hotbar,  ArrayList<InventoryContent> inventoryLeft,  ArrayList<InventoryContent> inventoryRight) {
		this.userID = userID;
		this.hotbar = new Inventory(userID, 10, 1, hotbar, 0);
		this.inventoryLeft = new Inventory(userID, 3, 6, inventoryLeft, 1);
		this.inventoryRight = new Inventory(userID, 3, 6, inventoryRight, 2);
	}
	
	public void sendInventory() {
				
		for (int x = 0; x < hotbar.content.size(); x ++) {
			
			InventoryContent c = hotbar.content.get(x);
			CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), new PacketUpdateInventoryContent(c.x,c.y,c.itemstack.item.id, c.itemstack.size, 0));

		}
		
		for (int x = 0; x < inventoryLeft.content.size(); x ++) {

			InventoryContent c = inventoryLeft.content.get(x);
			CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), new PacketUpdateInventoryContent(c.x,c.y,c.itemstack.item.id, c.itemstack.size, 1));

		}

		for (int x = 0; x < inventoryRight.content.size(); x ++) {

			InventoryContent c = inventoryRight.content.get(x);
			CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), new PacketUpdateInventoryContent(c.x,c.y,c.itemstack.item.id, c.itemstack.size, 2));

		}
		
	}
	
}
