package com.pixel.util;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.entity.Entity;
import com.pixel.piece.Piece;
import com.pixel.world.WorldServer;

public class CollisionBox {
	
	public CollisionBox() {
	}

	public static boolean testEntititiesInvisible(Entity entity1, Entity entity, WorldServer w) {
		
		if (entity.equals(entity1))
			return false;
		
		if (entity.getCollisionBox().overlaps(entity1.getCollisionBox())) {
			
			return true;

		}
		return false;
	}
	
public static boolean testEntitiesAgainstPoint(Entity sender, float x, float y, WorldServer w) {
		
		for (int b = 0; b < WorldServer.entities.size(); b ++) {

			Entity entity = (Entity) WorldServer.entities.values().toArray()[b];
			
			if (entity.getCollisionBox().contains(x, y) && !entity.equals(sender))
				return true;

		}
		
		return false;

	}
	
	public static Entity testEntitiesAgainstCollisionBox(Rectangle r, WorldServer w) {
		
		for (int b = 0; b < WorldServer.entities.size(); b ++) {

			Entity entity = (Entity) WorldServer.entities.values().toArray()[b];
			
			if (entity.getCollisionBox().overlaps(r)) {
				return entity;
			}

		}
		
		return null;

	}
	
	public static int testPiecesAgainstCollisionBox(Rectangle r, WorldServer w) {
		
		for (int b = 0; b < WorldServer.pieces.length; b ++) {

			Piece p = WorldServer.pieces[b];
			
			if (r.overlaps(p.getCollisionBox()) && Piece.info[p.id].isCollectable) {
				return b;
			}

		}
		
		return -1;

	}
	
	public static boolean testPieceAgainstEntity(Piece p, Entity e, WorldServer w, boolean b) {
		if (e.getCollisionBox().overlaps(p.getCollisionBox())) {
			if (b) {
				if (e.getPreviousX() > p.getCollisionBox().getX() && e.getPreviousX() < p.getCollisionBox().getX() + p.getCollisionBox().getWidth()) {
					int diff;
					if (e.getPreviousY() < e.getY()) {
						diff = -1;
					} else  if (e.getPreviousY() > e.getY()) {
						diff = 1;
					} else {
						diff = 0;
					}
					float borderTop = p.getCollisionBox().getY();
					float borderBottom = p.getCollisionBox().getY() + p.getCollisionBox().getHeight();
					if (Math.abs(e.getPreviousY()-borderTop) < Math.abs(e.getPreviousY()-borderBottom)) {
						if (diff == -1)
							e.setY(e.getPreviousY());
					} else {
						if (diff == 1)
							e.setY(e.getPreviousY());
					}
				} else
				if (e.getPreviousY() > p.getCollisionBox().getY() && e.getPreviousY() < p.getCollisionBox().getY() + p.getCollisionBox().getHeight()) {
					int diff;
					if (e.getPreviousX() < e.getX()) {
						diff = -1;
					} else  if (e.getPreviousX() > e.getX()) {
						diff = 1;
					} else {
						diff = 0;
					}
					float borderLeft = p.getCollisionBox().getX();
					float borderRight = p.getCollisionBox().getX() + p.getCollisionBox().getWidth();
					if (Math.abs(e.getPreviousX()-borderLeft) < Math.abs(e.getPreviousX()-borderRight)) {
						if (diff == -1)
							e.setX(e.getPreviousX());
					} else {
						if (diff == 1)
							e.setX(e.getPreviousX());
					}
				} 
			}
			return true;
		} 
		return false;
	}
	
	public static Entity getEntityAtPoint(Entity sender, float x, float y, WorldServer w) {
		
		for (int b = 0; b < WorldServer.entities.size(); b ++) {

			Entity entity = (Entity) WorldServer.entities.values().toArray()[b];
			
			if (entity.getCollisionBox().contains(x, y) && !entity.equals(sender))
				return entity;

		}
		return null;

	}
	
	public static boolean testEntitities(Entity p, Entity e, WorldServer w) {		
		if (e.getCollisionBox().overlaps(p.getCollisionBox())) {
			if (e.getPreviousX() > p.getCollisionBox().getX() && e.getPreviousX() < p.getCollisionBox().getX() + p.getCollisionBox().getWidth()) {
				int diff;
				if (e.getPreviousY() < e.getY()) {
					diff = -1;
				} else  if (e.getPreviousY() > e.getY()) {
					diff = 1;
				} else {
					diff = 0;
				}
				float borderTop = p.getCollisionBox().getY();
				float borderBottom = p.getCollisionBox().getY() + p.getCollisionBox().getHeight();
				if (Math.abs(e.getPreviousY()-borderTop) < Math.abs(e.getPreviousY()-borderBottom)) {
					if (diff == -1)
						e.setY(e.getPreviousY());
				} else {
					if (diff == 1)
						e.setY(e.getPreviousY());
				}
			} else
			if (e.getPreviousY() > p.getCollisionBox().getY() && e.getPreviousY() < p.getCollisionBox().getY() + p.getCollisionBox().getHeight()) {
				int diff;
				if (e.getPreviousX() < e.getX()) {
					diff = -1;
				} else  if (e.getPreviousX() > e.getX()) {
					diff = 1;
				} else {
					diff = 0;
				}
				float borderLeft = p.getCollisionBox().getX();
				float borderRight = p.getCollisionBox().getX() + p.getCollisionBox().getWidth();
				if (Math.abs(e.getPreviousX()-borderLeft) < Math.abs(e.getPreviousX()-borderRight)) {
					if (diff == -1)
						e.setX(e.getPreviousX());
				} else {
					if (diff == 1)
						e.setX(e.getPreviousX());
				}
			} 
		}
		return false;
	}
	
}
