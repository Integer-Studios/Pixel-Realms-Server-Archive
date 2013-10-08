package com.pixel.entity;

import com.pixel.player.PlayerManager;
import com.pixel.world.WorldServer;

public class EntityAlive extends Entity {

	public float previousX, previousY, previousHealth;
	public int speed;
	
	public EntityAlive(float x, float y, float width, float height, boolean propagate) {
		super(x, y, width, height, propagate);
		// TODO Auto-generated constructor stub
	}
	
	public EntityAlive(float x, float y, float width, float height) {
		super(x, y, width, height, true);
		// TODO Auto-generated constructor stub
	}
	
	public void setMaxHealth(float health) {
		
		this.health = health;
		
	}
	
	public void setHealth(float health) {
		
		this.health = health;
		
	}
	
	public void damage(WorldServer w, float damage, Entity damageSource) {
		this.health -= damage;
		if (this.health <= 0.0F) {
			this.kill(w, damageSource);
			
		}
		
		updated = false;
	}
	
	public void tick(WorldServer w) {

		super.tick(w);
		
		if (posX < 10) {
			posX = 10;
		}
		if (posY < 10) {
			posY = 10;
		}
		if (posX > WorldServer.c-11) {
			posX = WorldServer.c-11;
		}
		if (posY > WorldServer.c-11) {
			posY = WorldServer.c-11;
		} 
		
		PlayerManager.broadcastEntity(this);

		previousX = posX;
		previousY = posY;

	}

	public void kill(WorldServer w, Entity damageSource) {
		spawnBody(w);
		health = 0;
		PlayerManager.updateLivingEntity(this);
		WorldServer.entities.remove(this.serverID);
	}
	
	public void spawnBody(WorldServer w) {

		int newX = Math.round(posX);
		int newY = Math.round(posY);
		
		if (this instanceof EntityBunny) {
			
			WorldServer.setPiece(newX, newY, 15);
			
		}
		
	}
	
	
}
