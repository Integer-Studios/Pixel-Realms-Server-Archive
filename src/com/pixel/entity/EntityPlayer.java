package com.pixel.entity;

import com.pixel.communication.packet.PacketUpdatePlayer;
import com.pixel.item.ItemStack;
import com.pixel.player.PlayerInventory;
import com.pixel.player.PlayerManager;
import com.pixel.world.WorldServer;

public class EntityPlayer extends Entity {

	public float damage = 1.0F;
	public float satisfaction, energy;
	float direction;
	int action;
	public String username;
	public int userID;
	public ItemStack selectedItem;
	public int session;
	public int speed;
	
	public EntityPlayer(String username, int userID) {
		super(20, 20, .9F, .2F, false);
		this.setPosition(152, 152);
		this.username = username;
		this.userID = userID;
	}
	public void tick(WorldServer world) {
		
			
		PlayerManager.broadcastPacketUpdatePlayer(new PacketUpdatePlayer(this));
			
		
		super.tick(world);
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

	public void setDirection(float direction) {

		this.direction = direction;

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
		
	}

}
