package com.pixel.world;

import java.util.concurrent.ConcurrentHashMap;

import com.pixel.entity.EntityAlive;
import com.pixel.piece.Piece;
import com.pixel.tile.Tile;

public class InteriorWorld {
	
	public int c;
	public ConcurrentHashMap<Integer, Tile> tiles = new ConcurrentHashMap<Integer, Tile>();
	public ConcurrentHashMap<Integer, Piece> pieces = new ConcurrentHashMap<Integer, Piece>();
	public ConcurrentHashMap<Integer, EntityAlive> entities = new ConcurrentHashMap<Integer, EntityAlive>();
	public boolean building;
	public int wallID;
	
	public InteriorWorld(int width, int height, int id) {
		
		c = width * height;
		
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				
				pieces.put((y * (width * height)) + x, new Piece(x, y, 0, false));
				
			}			
		}
		wallID = 1;
		for (int x = 0; x < width; x ++) {
			
			for (int y = 0; y < height; y ++) {
				
				tiles.put((y * (width * height)) + x, new Tile(x, y, id, false));
				
			}
			
		}
		
	}
	
}
