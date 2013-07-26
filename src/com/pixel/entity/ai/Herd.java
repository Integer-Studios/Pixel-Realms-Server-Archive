package com.pixel.entity.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.pixel.admin.PixelLogger;
import com.pixel.entity.Entity;
import com.pixel.entity.EntityAnimal;
import com.pixel.player.PlayerManager;
import com.pixel.world.WorldServer;

public class Herd {

	@SuppressWarnings("rawtypes")
	private static HashMap<Integer, Class> herdMap = new HashMap<Integer, Class>();
	public ConcurrentHashMap<Integer, EntityAnimal> entities = new ConcurrentHashMap<Integer, EntityAnimal>();
	public int currentID;
	public float x, y;	
	public float targetX, targetY;
	public int timeSpent;
	public EntityAnimal alpha;
	public boolean moving;
	public int id;
	public int herdID;
	
	public Herd(){}
	
	public Herd(float x, float y) {

		this.x = x;
		this.y = y;
		this.targetX = x;
		this.targetY = y;
		
		this.id = WorldServer.herds.size() + 1;
		
		WorldServer.herds.put(id, this);
	
	}
	
	public void load(int id, int alphaID, int timeSpent, float x, float y, float targetX, float targetY, int currentID, ArrayList<Float[]> entities) {
		
		this.timeSpent = timeSpent;
		this.x = x;
		this.y = y;
		this.targetX = targetX;
		this.targetY = targetY;
		this.currentID = currentID;
		
		for (int i = 1; i < entities.size(); i ++) {
			
			Float[] e = entities.get(i);
			int entityID = Math.round(e[0]);
			EntityAnimal entity = (EntityAnimal) Entity.getEntity(entityID);
			entity.herd = this;
			entity.herdID = id;
			entity.setPosition(e[1], e[2]);
			entity.xDestination = Math.round(e[1]);
			entity.yDestination = Math.round(e[2]);
			entity.setHealth(e[3]);
			entity.serverID = Math.round(e[4]);
			if (entity.serverID != alphaID)
				this.entities.put(entity.serverID, entity);
			else {
				
				entity.alpha = true;
				alpha = entity;
				
			}
			PlayerManager.updateLivingEntity(entity);
			
		}
		
	}
	
	public void propagateAlpha(EntityAnimal entity) {
		
		entity.herdID = this.id;
		entity.serverID = 0;
		entity.alpha = true;
		PlayerManager.updateLivingEntity(entity);
		
	}
	
	public void propagateEntity(EntityAnimal entity) {
		
		entity.herdID = this.id;
		entity.serverID = entities.size() + 1;
		entities.put(entity.serverID, entity);
		PlayerManager.updateLivingEntity(entity);
		
	}

	public void tick(WorldServer w) {
		
		if (entities.size() == 0) {
			
			die();
			
		}
		
		if(Math.abs(alpha.getX() - targetX) <= 1) {
			
			targetX = alpha.getX();
			x = targetX;
		}

		if(Math.abs(alpha.getY() - targetY) <= 1) {
			
			targetY = alpha.getY();
			y = targetY;
		}
		
		if (x == targetX && y == targetY) {
			
			timeSpent ++;
			moving = false;
			
		} else {
			
			moving = true;
			timeSpent = 0;
			
		}
		
		
	}

	public void calculateAlphaTarget() {

		if (moving) {

			float difX = Math.abs(alpha.getY() - targetY);
			float difY = Math.abs(alpha.getY() - targetY);

			if (difY < 10 && difX < 10) {

				alpha.xDestination = (int) targetX;
				alpha.yDestination = (int) targetY;
				return;
				
			}

			if (targetX < alpha.getX()) {

				alpha.xDestination = (int) (alpha.getX() - (5 + (int)(Math.random() * ((10 - 5) + 1))));

			} else if (targetX > alpha.getX()) {

				alpha.xDestination = (int) (alpha.getX() + (5 + (int)(Math.random() * ((10 - 5) + 1))));

			} 

			if (targetY < alpha.getY()) {

				alpha.yDestination = (int) (alpha.getY() - (5 + (int)(Math.random() * ((10 - 5) + 1))));

			} else if (targetX > alpha.getX()) {

				alpha.yDestination = (int) (alpha.getY() + (5 + (int)(Math.random() * ((10 - 5) + 1))));

			}

		} else {
			
			float newX = x + (-8 + (int)(Math.random() * ((8 + 8) + 1)));
			float newY = y + (-8 + (int)(Math.random() * ((8 + 8) + 1)));
			
			Float[] pos = checkPositionAgainstHerd(newX, newY, 10);
			
			if (pos != null) {
				
				alpha.xDestination = Math.round(pos[0]);
				alpha.yDestination = Math.round(pos[1]);
				
			}
			
		}

	}
	
	public float checkPosition(float x) {
		
		if (x <= 0) {
			
			x = (-1 * x);
			
		} else if (x >= WorldServer.c) {
		
			x = x - (x - WorldServer.c);
			
		}
		
		return x;
		
	}
	
	public Float[] checkPositionAgainstAlpha(float x, float y, int r) {

		if ((Math.sqrt((x - alpha.getX())*(x - alpha.getX()) + (y - alpha.getY())*(y - alpha.getY())) >= r)) {
			
			return null;

		}
		
		x = checkPosition(x);
		y = checkPosition(y);
		
		return new Float[]{x, y};
		
	}
	
	public Float[] checkPositionAgainstHerd(float x, float y, int r) {

		if ((Math.sqrt((x - this.x)*(x - this.x) + (y - this.y)*(y - this.y)) >= r)) {
			
			return null;

		}
		
		x = checkPosition(x);
		y = checkPosition(y);
		
		return new Float[]{x, y};
		
	}
	
	public void onEntityDeath(WorldServer w, EntityAnimal animal) {
		
		if (animal.alpha) {
			
			//alpha death
			int tempID = currentID;
			for (EntityAnimal entity : entities.values()) {
				
				if (entity.serverID < tempID) {
					
					tempID = entity.serverID;
					
				}
				
			}
			
			alpha = entities.get(tempID);
			alpha.alpha = true;
			entities.remove(tempID);
			
			
		} else {
			
			entities.remove(animal.serverID);
			
		}
		
	}
	
	public void die() {
		
		WorldServer.herds.remove(id);
		
	}
	
	@SuppressWarnings("rawtypes")
	public static Herd getHerd(int id) {
        try
        {
            Class herdClass = (Class)herdMap.get(id);
            Herd herd = herdClass == null ? null : (Herd)herdClass.newInstance();
            
            if (herd != null) 
            	herd.herdID = id;
            
            return herd;
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            PixelLogger.err("Skipping herd with id " + id);
            return null;
        }
    }
	
	static {
		
		herdMap.put(1, HerdBunny.class);
		
	}
	
	
}
