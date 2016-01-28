package com.pixel.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.pixel.entity.Entity;
import com.pixel.entity.EntityPlayer;
import com.pixel.piece.Piece;
import com.pixel.piece.PieceBuilding;
import com.pixel.tile.Tile;

public class WorldChunk {
	
	public Map<Integer, Tile> tiles;
	public Map<Integer, Piece> pieces;
	public Map<Integer, PieceBuilding> buildings;
	public ArrayList<Integer> entities;
	public WorldServer world; 
	public int x, y;
	
	public WorldChunk(WorldServer world) {
		
		this.world = world;
//		pieces = new ConcurrentLinkedHashMap.Builder<Integer, Piece>().maximumWeightedCapacity(1000000).build();
		pieces = Collections.synchronizedMap(new LinkedHashMap<Integer, Piece>());
		tiles = Collections.synchronizedMap(new LinkedHashMap<Integer, Tile>());
		buildings = Collections.synchronizedMap(new LinkedHashMap<Integer, PieceBuilding>());
		entities = new ArrayList<Integer>();

	}
	
	public WorldChunk(WorldServer world, int x, int y) {

		this.world = world;
		this.x = x; 
		this.y = y;
		pieces = Collections.synchronizedMap(new LinkedHashMap<Integer, Piece>());
		tiles = Collections.synchronizedMap(new LinkedHashMap<Integer, Tile>());
		buildings = Collections.synchronizedMap(new LinkedHashMap<Integer, PieceBuilding>());
		entities = new ArrayList<Integer>();

		WorldServer.propagateChunk(this);
		
	}

	public void tick() {
		
		for (Piece piece : pieces.values()) {
			try {
				Thread.sleep(2);
			} catch (Exception e){}

			piece.tick(world);

		}

		
	}

	public void propagateTile(Tile tile) {

		tiles.put((tile.posY * (WorldServer.c)) + tile.posX, tile);
		
	}
	
	public void propagatePiece(Piece piece) {

		pieces.put((piece.posY * (WorldServer.c)) + piece.posX, piece);
		
	}
	
	public void propagateEntity(Entity entity) {
		
		entities.add(entity.serverID);
		
	}

	public void removeEntity(Entity entity) {
		
		entities.remove(entity.serverID);
		
	}
	
	public Tile getTile(int x, int y) {

		return tiles.get((y * (WorldServer.c)) + x);
	
	}

	public Piece getPiece(int x, int y) {

		return pieces.get((y * (WorldServer.c)) + x);
	
	}
	
	public void setTile(Tile t) {
		
		tiles.put((t.posY * (WorldServer.c)) + t.posX, t);
		
	}

	public void setPiece(Piece p) {
		
		pieces.put((p.posY * (WorldServer.c)) + p.posX, p);
		
	}

	public void tick(WorldServer w) {
		
		for (Tile t : tiles.values()) {
			
			t.tick(w);
			
		}
		
		for (Piece p : pieces.values()) {
			
			p.tick(w);
			
		}
		
	}

	
}
