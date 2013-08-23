package com.pixel.world;

import com.pixel.entity.Entity;
import com.pixel.entity.EntityPlayer;
import com.pixel.piece.Piece;
import com.pixel.piece.PieceBuilding;
import com.pixel.tile.Tile;

public class WorldComponent {

	public String username;
	
	public float posX;
	public float posY;
	public float health;
	
	public int userID;
	public int id, serverID;
	public int buildingID;
	public int worldID;
	public int damage, metadata;
	
	public Tile tile;
	public Piece piece;
	public Entity entity;
	
	public Type type;
	
	public enum Type {
		
		TILE,
		PIECE,
		ENTITY;

	}

	public WorldComponent (Tile tile) {

		this.tile = tile;
		this.posX = tile.posX;
		this.posY = tile.posY;
		this.id = tile.id;
		this.type = Type.TILE;
	}

	public WorldComponent (Piece piece) {

		this.buildingID = -1;
		this.piece = piece;
		this.posX = piece.posX;
		this.posY = piece.posY;
		this.damage = piece.damage;
		this.metadata = piece.metadata;
		this.id = piece.id;
		if (piece instanceof PieceBuilding) {

			this.worldID = ((PieceBuilding) piece).building.worldID;
			this.buildingID = ((PieceBuilding) piece).building.id;

		}
		this.type = Type.PIECE;

	}

	public WorldComponent (Entity entity) {

		this.entity = entity;
		this.posX = entity.getX();
		this.posY = entity.getY();
		this.id = entity.id;
		this.serverID = entity.serverID;
		this.health = entity.health;
		this.type = Type.ENTITY;

	}
	
	public WorldComponent (EntityPlayer entity) {
		
		this.entity = entity;
		this.posX = entity.getX();
		this.posY = entity.getY();
		this.id = entity.id;
		this.health = entity.health;
		this.type = Type.ENTITY;
		this.username = entity.username;
		this.userID = entity.userID;
		
	}
	
}
