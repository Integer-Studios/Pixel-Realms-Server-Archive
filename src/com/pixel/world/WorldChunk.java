package com.pixel.world;

import java.util.concurrent.ConcurrentHashMap;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.pixel.entity.Entity;
import com.pixel.piece.Piece;
import com.pixel.piece.PieceBuilding;
import com.pixel.tile.Tile;

public class WorldChunk {
	
	public ConcurrentHashMap<Integer, Tile> tiles = new ConcurrentHashMap<Integer,Tile>();
	public ConcurrentLinkedHashMap<Integer, Piece> pieces;
	public ConcurrentHashMap<Integer, PieceBuilding> buildings = new ConcurrentHashMap<Integer,PieceBuilding>();
	public ConcurrentHashMap<Integer, Entity> entities = new ConcurrentHashMap<Integer,Entity>();
	public WorldServer world; 
	public int x, y;
	
	public WorldChunk(WorldServer world) {
		
		this.world = world;
		pieces = new ConcurrentLinkedHashMap.Builder<Integer, Piece>().maximumWeightedCapacity(1000000).build();

	}
	
	public WorldChunk(WorldServer world, int x, int y) {

		this.world = world;
		this.x = x; 
		this.y = y;
		pieces = new ConcurrentLinkedHashMap.Builder<Integer, Piece>().maximumWeightedCapacity(1000000).build();

		WorldServer.propagateChunk(this);
		
	}

	public void tick() {
		
		for (Entity entity : entities.values()) {
			try {
				Thread.sleep(2);
			} catch (Exception e){}

			entity.tick(world);

		}
		
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
		
		for (Entity e : entities.values()) {
			
			e.tick(w);
			
		}
		
	}
	
}