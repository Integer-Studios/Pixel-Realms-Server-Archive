package com.pixel.entity;

import com.pixel.item.ItemStack;
import com.pixel.piece.PieceSac;
import com.pixel.player.PlayerManager;
import com.pixel.util.CoordinateKey;
import com.pixel.world.WorldServer;

public class EntityAlive extends Entity {

	public float previousHealth;
	public float speed;
	public float health = 100F;
	public ItemStack[] drops;
	public boolean shouldKill = false;
	public Entity killerDamageSource;
	public float satisfaction;
	public float energy;
	
	public EntityAlive(float x, float y, float width, float height, boolean propagate) {
		super(x, y, width, height, propagate);
		// TODO Auto-generated constructor stub
	}
	
	public EntityAlive(float x, float y, float width, float height) {
		super(x, y, width, height, true);
		// TODO Auto-generated constructor stub
	}
	
	public void setDrops(ItemStack[] is) {
		drops = is;
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
			shouldKill = true;
			killerDamageSource = damageSource;
		}
		
		updated = false;
	}
	
	public void tick(WorldServer w) {
		super.tick(w);
		if (shouldKill) {
			shouldKill = false;
			kill(w, killerDamageSource);
		}
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
		WorldServer.setPiece(newX, newY, 27);
		PieceSac.sacContents.put(new CoordinateKey(newX, newY), drops);
//		
//		if (this instanceof EntityBunny) {
//			
//			WorldServer.setPiece(newX, newY, 15);
//			
//		}
		
	}
	
	
}
