package com.pixel.entity;

import java.util.HashMap;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.admin.PixelLogger;
import com.pixel.communication.packet.PacketMoveLivingEntity;
import com.pixel.communication.packet.PacketUpdateLivingEntity;
import com.pixel.piece.Piece;
import com.pixel.player.PlayerManager;
import com.pixel.util.CollisionBox;
import com.pixel.util.CoordinateKey;
import com.pixel.world.WorldServer;

public class Entity {
	
	public float posX, posY, prevPosX, prevPosY;
	public float velocityX, velocityY, prevVelocityX, prevVelocityY;
	public int id;
	public float width, height;
	public int serverID;
	public boolean updated = true;
	public boolean shouldCollide = true;
	public HashMap<CoordinateKey, Integer[]> entityKeys = new HashMap<CoordinateKey, Integer[]>();

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
	

	public void setVelocity(float x, float y) {
		velocityX = x;
		velocityY = y;
	}
	
	public float getVelocityY() {
		return velocityY;
	}
	
	public float getVelocityX() {
		return velocityX;
	}
	
	public float getPreviousVelocityY() {
		return prevVelocityY;
	}
	
	public float getPreviousVelocityX() {
		return prevVelocityX;
	}
	
	public void setVelocityX(float f) {
		velocityX = f;
	}
	
	public void setVelocityY(float f) {
		velocityY = f;
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
		posX += velocityX;
		posY += velocityY;
		
		if (posX < 0) {
			posX = 0;
		}
		if (posY < 0) {
			posY = 0;
		}
		if (posX > WorldServer.c-1) {
			posX = WorldServer.c-1;
		}
		if (posY > WorldServer.c-1) {
			posY = WorldServer.c-1;
		} 
		
		addToEntityKeys();


		if (shouldCollide) {

			checkCollisions(w);

			if ((velocityX - prevVelocityX != 0) || (velocityY - prevVelocityY != 0)) {
				//call same collisions on client, don't send this packet after collisions, call it before
				PlayerManager.broadcastPacket(new PacketMoveLivingEntity((EntityAlive)this));
			}

			if (velocityX == 0 && velocityY == 0) {
				//only send this packet
				PlayerManager.broadcastPacket(new PacketUpdateLivingEntity((EntityAlive)this));

			}
		}
		
//		System.out.println(posX + ", " + posY);

		prevVelocityX = velocityX;
		prevVelocityY = velocityY;
			
		prevPosX = posX;
		prevPosY = posY;
	}
	
	public void addToEntityKeys() {
		CoordinateKey location = new CoordinateKey((int)posX, (int)posY);

		if (entityKeys.containsKey(location)) {
			Integer[] entitiesAtLocation = entityKeys.get(location);
			Integer[] newEntitiesAtLocation = new Integer[entitiesAtLocation.length+1];
			for (int i = 0; i < entitiesAtLocation.length; i++) {
				newEntitiesAtLocation[i] = entitiesAtLocation[i];
			}
			newEntitiesAtLocation[entitiesAtLocation.length] = this.serverID;
			entityKeys.put(location, newEntitiesAtLocation);
		} else {
			Integer[] newEntitiesAtLocation = new Integer[]{this.serverID};
			entityKeys.put(location, newEntitiesAtLocation);
		}
	}
	
	public void checkCollisions(WorldServer w) {
		int r = 10;
		int aX = (int)posX - r;
		int bX = (int)posX + r;
		int aY = (int)posY - r;
		int bY = (int)posY + r;
		
		for (int y = aY; y <= bY; y ++) {

			for (int x = aX; x <= bX; x ++) {
			
				if (Math.sqrt(((int)posX - x)*((int)posX - x) + ((int)posY - y)*((int)posY - y)) <= r) {
					CollisionBox.testPieceAgainstEntity(WorldServer.getPieceObject(x, y), this, w, Piece.info[WorldServer.getPiece(x, y)].shouldCollide);
					CoordinateKey location = new CoordinateKey(x, y);
					if (entityKeys.containsKey(location)) {
						Integer[] e = entityKeys.get(location);
						for (int i = 0; i < e.length; i++) {
							CollisionBox.testEntitities(this, WorldServer.entities.get(e[i]), w);
						}
					}
				}

			}
			
		}
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
		entityMap.put(2, EntityPog.class);
		
	}
	
}
