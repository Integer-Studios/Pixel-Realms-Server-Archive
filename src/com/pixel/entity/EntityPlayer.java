package com.pixel.entity;

import java.util.ArrayList;

import com.pixel.item.ItemStack;
import com.pixel.player.PlayerInventory;
import com.pixel.player.PlayerManager;

public class EntityPlayer extends EntityAlive {

	public int serverID;
	public float damage = 1.0F;
	public float satisfaction, energy;
	int action;
	public String username;
	public int userID;
	public ItemStack selectedItem;
	public int session;
	public float speed;
	public boolean inside;
	public boolean loaded;
	public float oldX, oldY;
	public float health = 100F;
	public ArrayList<Integer> loadedChunks = new ArrayList<Integer>();

	public EntityPlayer(String username, int userID) {
		super(200, 200, .9F, .2F, true);
		this.metadata.addBoolean(0, true);
		this.metadata.addInteger(1, userID);
		this.metadata.addString(2, username);
		if (selectedItem != null) {
			this.metadata.addInteger(3, selectedItem.item.id);
			this.metadata.addInteger(4, selectedItem.size);
			this.metadata.addInteger(5, selectedItem.metadata);
		}

		this.setPosition(200, 200);
		this.username = username;
		this.userID = userID;
		this.shouldCollide = false;
	}

	public EntityPlayer(String username, int userID, float posX, float posY, float health, float satisfaction, float energy, int worldID, int serverID, int session) {
		super(posX, posY, .9F, .2F, false);
		this.metadata.addBoolean(0, true);
		this.metadata.addInteger(1, userID);
		this.metadata.addString(2, username);
		if (selectedItem != null) {
			this.metadata.addInteger(3, selectedItem.item.id);
			this.metadata.addInteger(4, selectedItem.size);
			this.metadata.addInteger(5, selectedItem.metadata);
		}
		
		this.userID = userID;
		this.username = username;
		this.shouldCollide = false;
		this.health = health;
		this.satisfaction = satisfaction;
		this.energy = energy;
		this.worldID = worldID;
		this.velocityX = 0F;
		this.velocityY = 0F;
		this.serverID = serverID;
		this.session = session;
		this.id = 3;
	
	}

	public PlayerInventory getPlayerInventory() {
		return PlayerManager.inventories.get(userID);
	}

	public void setPosX(float x) {

		this.posX = x;

	}

	public void setPosY(float y) {

		this.posY = y;

	}
	
	public void setSession(int session) {
		
		this.session = session;
		
	}

	public void setHealth(float health) {

		this.health = health;

	}
	
	public void setSatisfaction(float satisfaction) {
		
		this.satisfaction = satisfaction;
		
	}
	
	public void setEnergy(float energy) {
		
		this.energy = energy;
		
	}

	public void setAction(int action) {

		this.action = action;

	}
	
	public void damage(float damage) {
		
		setHealth(health - damage);
		PlayerManager.updatePlayer(userID);
		
	}
	
	public boolean giveItem(ItemStack is) {
		if (!getPlayerInventory().hotbar.addItem(is.item, is.size)) {
			if (!getPlayerInventory().inventoryLeft.addItem(is.item, is.size)) {
				if (!getPlayerInventory().inventoryRight.addItem(is.item, is.size)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void setSelectedItem(ItemStack itemStack) {

		this.selectedItem = itemStack;
		this.metadata.addInteger(3, selectedItem.item.id);
		this.metadata.addInteger(4, selectedItem.size);
		this.metadata.addInteger(5, selectedItem.metadata);
		
	}

}
