package com.pixel.inventory;

import java.util.concurrent.ConcurrentHashMap;

import com.pixel.util.CoordinateKey;
import com.pixel.admin.PixelLogger;
import com.pixel.communication.CommunicationServer;
import com.pixel.communication.CommunicationServlet;
import com.pixel.communication.packet.PacketUpdateInventoryContent;
import com.pixel.item.Item;
import com.pixel.item.ItemStack;

public class Inventory {
	
	public Inventory(int userID, int w, int h, int id) {
		this(userID, w, h, new ConcurrentHashMap<CoordinateKey, InventoryContent>(), id);
	}
	
	public Inventory(int userID, int w, int h, ConcurrentHashMap<CoordinateKey, InventoryContent> ic, int id) {
		width = w;
		height = h;
		content = ic;
		if (content.size() > 10 && id == 0) {
			
			PixelLogger.err("hotbar huge mofo");
			
		}
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
		
		if (content.containsKey(new CoordinateKey(x, y))) 
			return content.get(new CoordinateKey(x, y));
		else
			return new InventoryContent(x, y, new ItemStack(Item.blank, 0));

	}
	
	public void setContent(int x, int y, ItemStack itemstack) {
		content.put(new CoordinateKey(x, y), new InventoryContent(x, y, itemstack));
		CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), new PacketUpdateInventoryContent(x,y,itemstack.item.id, itemstack.size, id));

	}
	
	public void clientSetContent(int x, int y, ItemStack itemstack) {
		content.put(new CoordinateKey(x, y), new InventoryContent(x, y, itemstack));

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
	public ConcurrentHashMap<CoordinateKey, InventoryContent> content;
	public int userID;
	public int id;
}
