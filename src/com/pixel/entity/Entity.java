package com.pixel.entity;

import java.util.HashMap;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.admin.PixelLogger;
import com.pixel.world.WorldServer;

public class Entity {
	
	public float posX, posY, prevPosX, prevPosY;
	public float health = 100F;
	public int id;
	public float width, height;
	public int serverID;
	public boolean updated = true;

	public Rectangle collisionBox;

	@SuppressWarnings("rawtypes")
	private static HashMap<Integer, Class> entityMap = new HashMap<Integer, Class>();

	public Entity(){}
	
	public Entity (float x, float y, float width, float height, boolean propagate) {
		posX = x;
		posY = y;
		collisionBox = new Rectangle(posX - (width/2), posY - (height/2), width, height);
		this.width = width;
		this.height = height;
		this.serverID = WorldServer.entities.size() + 1;
		
		if (!(this instanceof EntityPlayer) && propagate)
			WorldServer.propagateEntity(this);
		
	}
	
	public float getX() {
		return posX;
	}
	
	public void setX(float x) {
		posX = x;
	}
	
	public float getY() {
		return posY;
	}
	
	public void setY(float y) {
		posY = y;
	}
	
	public float getPreviousX() {
		return prevPosX;
	}
	
	public float getPreviousY() {
		return prevPosY;
	}

	public Rectangle getCollisionBox() {
		return collisionBox;
	}
	
	public void setCollisionBox(Rectangle c) {
		collisionBox = c;
	}
	
	public void setPosition(float x, float y) {
		posX = x;
		posY = y;
	}

	public boolean isInPreviousPosition() {
		return posX == prevPosX && posY == prevPosY;
	}

	public void tick(WorldServer w) {
		
	}
	
	@SuppressWarnings("rawtypes")
	public static Entity getEntity(int id) {
        try
        {
            Class entityClass = (Class)entityMap.get(id);
            return entityClass == null ? null : (Entity)entityClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            PixelLogger.err("Skipping entity with id " + id);
            return null;
        }
    } 
	
	static {
		
		entityMap.put(1, EntityBunny.class);
		
	}
	
}
