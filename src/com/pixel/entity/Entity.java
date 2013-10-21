package com.pixel.entity;

import java.util.HashMap;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.admin.PixelLogger;
import com.pixel.communication.packet.PacketMoveLivingEntity;
import com.pixel.communication.packet.PacketUpdateLivingEntity;
import com.pixel.piece.Piece;
import com.pixel.player.PlayerManager;
import com.pixel.util.CollisionBox;
import com.pixel.world.WorldServer;

public class Entity {
	
	public float posX, posY, prevPosX, prevPosY;
	public float velocityX, velocityY, prevVelocityX, prevVelocityY;
	public int id;
	public float width, height;
	public int serverID;
	public boolean updated = true;
	public boolean shouldCollide = true;

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
		if (this instanceof EntityAlive) {
			System.out.println(velocityX + " " + velocityY);
		}
		checkPieceCollisions(w);
		if (this instanceof EntityAlive) {
			System.out.println(velocityX + " " + velocityY);
			System.out.println();
		}


		if ((velocityX - prevVelocityX != 0) || (velocityY - prevVelocityY != 0)) {
			//call same collisions on client, don't send this packet after collisions, call it before
			if (this instanceof EntityAlive) {
				PlayerManager.broadcastPacket(new PacketMoveLivingEntity((EntityAlive)this));
			}
		}
		
		if (velocityX == 0 && velocityY == 0) {
			//only send this packet
			if (this instanceof EntityAlive) {
				PlayerManager.broadcastPacket(new PacketUpdateLivingEntity((EntityAlive)this));
			}

		}
		
//		System.out.println(posX + ", " + posY);

		prevVelocityX = velocityX;
		prevVelocityY = velocityY;
			
		prevPosX = posX;
		prevPosY = posY;
	}
	
	public void checkPieceCollisions(WorldServer w) {
		int r = 10;
		int aX = (int)posX - r;
		int bX = (int)posX + r;
		int aY = (int)posY - r;
		int bY = (int)posY + r;
		
		for (int y = aY; y <= bY; y ++) {

			for (int x = aX; x <= bX; x ++) {
			
				if (Math.sqrt(((int)posX - x)*((int)posX - x) + ((int)posY - y)*((int)posY - y)) <= r) {
					CollisionBox.testPieceAgainstEntity(WorldServer.getPieceObject(x, y), this, w, Piece.info[WorldServer.getPiece(x, y)].shouldCollide);
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
