package com.pixel.inventory;

import java.util.ArrayList;

import com.pixel.communication.CommunicationServer;
import com.pixel.communication.CommunicationServlet;
import com.pixel.communication.packet.PacketUpdateInventoryContent;
import com.pixel.item.Item;
import com.pixel.item.ItemStack;

public class Inventory {
	
	public Inventory(int userID, int w, int h, int id) {
		this(userID, w, h, new ArrayList<InventoryContent>(), id);
	}
	
	public Inventory(int userID, int w, int h, ArrayList<InventoryContent> ic, int id) {
		width = w;
		height = h;
		content = ic;
		this.userID = userID;
		this.id = id;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public InventoryContent getContent(int x, int y) {
		for (int i = 0; i < content.size(); i++) {
			if (content.get(i).x == x && content.get(i).y == y) {
				return content.get(i);
			}
		}
		return new InventoryContent(x, y, new ItemStack(Item.blank, 0));
	}
	
	public void setContent(int x, int y, ItemStack itemstack) {
		for (int i = 0; i < content.size(); i++) {
			if (content.get(i).x == x && content.get(i).y == y) {
				content.set(i, new InventoryContent(x, y, itemstack));
				CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), new PacketUpdateInventoryContent(x,y,itemstack.item.id, itemstack.size, id));
				return;
			}
		}
		content.add(new InventoryContent(x, y, itemstack));
		CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), new PacketUpdateInventoryContent(x,y,itemstack.item.id, itemstack.size, id));
	}
	
	public void clientSetContent(int x, int y, ItemStack itemstack) {
		for (int i = 0; i < content.size(); i++) {
			if (content.get(i).x == x && content.get(i).y == y) {
				content.set(i, new InventoryContent(x, y, itemstack));
				return;
			}
		}
		content.add(new InventoryContent(x, y, itemstack));

	}
	
	public boolean addItem(Item i, int s) {
		if (updateNextCoords(new ItemStack(i, s))) {
			ItemStack its = getContent(nextX, nextY).itemstack;
			its.size = its.size + s;
			setContent(nextX, nextY, its);
			return true;
		} else if (nextX != -1) {
			setContent(nextX, nextY, new ItemStack(i, s));
			return true;
		} else {
			return false;
		}
	}
	
	public boolean updateNextCoords(ItemStack i) {
		nextX = 0;
		nextY = 0;
		for (int y = 0; y < width; y++) {
			for (int x = 0; x < width; x++) {
				if (getContent(x,y).itemstack.item.id == i.item.id) {
					nextX = x;
					nextY = y;
					return true;
				}

			}
		}
		for (int y = 0; y < width; y++) {
			for (int x = 0; x < width; x++) {
				if (getContent(x,y).itemstack.item.id == 0) {
					nextX = x;
					nextY = y;
					return false;
				}
			}
		}
		nextX = -1;
		nextY = -1;
		return false;
	}
	
	public int nextX, nextY;
	public int width, height;
	public ArrayList<InventoryContent> content;
	public int userID;
	public int id;
}
