package com.pixel.interior;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.pixel.entity.EntityAlive;
import com.pixel.piece.Piece;
import com.pixel.piece.PieceBuilding;
import com.pixel.tile.Tile;

public class InteriorWorld {
	
	public int c;
	public ConcurrentHashMap<Integer, Tile> tiles = new ConcurrentHashMap<Integer, Tile>();
	public ConcurrentHashMap<Integer, Piece> pieces = new ConcurrentHashMap<Integer, Piece>();
	public ConcurrentHashMap<Integer, EntityAlive> entities = new ConcurrentHashMap<Integer, EntityAlive>();
	public boolean building;
	public int wallID;
	public int id, buildingID;
	public int doorX, doorY;
	
	public InteriorWorld() {
		
		
		
	}
	
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
				
				tiles.put((y * (width * height)) + x, new Tile(x, y, 8, -1, false));
				
			}
			
		}
		this.buildingID = id;
		
		if (buildingID == 0) {
			//cabin 1
			for (int x = 1; x < 5; x ++) {
				
				for (int y = 1; y < 4; y ++) {
					
					tiles.put((y * (width * height)) + x, new Tile(x, y, 9, -1, false));
					
				}
				
			}
			
			for (int x = 1; x < 5; x ++) {
				pieces.put((0 * (width * height)) + x, new Piece(x, 0, 18, false));
				pieces.put((4 * (width * height)) + x, new Piece(x, 4, 18, false));
			}
			for (int y = 1; y < 4; y ++) {
				pieces.put((y * (width * height)) + 0, new Piece(0, y, 20, false));
				pieces.put((y * (width * height)) + 5, new Piece(5, y, 19, false));
			}
			pieces.put((0 * (width * height)) + 0, new Piece(0, 0, 21, false));
			pieces.put((0 * (width * height)) + 5, new Piece(5, 0, 22, false));
			pieces.put((4 * (width * height)) + 0, new Piece(0, 4, 23, false));
			pieces.put((4 * (width * height)) + 5, new Piece(5, 4, 24, false));
			pieces.put((4 * (width * height)) + 1, new Piece(1, 4, 25, false));


		}
		
	}

	public ArrayList<ArrayList<Object[]>> save() {

		ArrayList<Object[]> tileSave = new ArrayList<Object[]>();

		for (Tile t : tiles.values()) {

			if (t != null) {
				tileSave.add(new Integer[]{t.id, t.posX, t.posY, t.metadata, 1});
			}

		}

		ArrayList<Object[]> piecesSave = new ArrayList<Object[]>();

		for (Piece p : pieces.values()) {

			if (p.id == 17)
				piecesSave.add(new Integer[]{p.id, p.posX, p.posY, p.damage, p.metadata, 1, ((PieceBuilding) p).building.id,  ((PieceBuilding) p).building.worldID});
			else
				piecesSave.add(new Integer[]{p.id, p.posX, p.posY, p.damage, p.metadata, 1});

		}

		ArrayList<Object[]> entitySave = new ArrayList<Object[]>();
//
//		for (int x = 0; x < entities.size(); x ++) {
//
//			Entity e = (Entity) entities.values().toArray()[x];
//			entitySave.add(new Float[]{e.id + 0F, e.getX(), e.getY(), e.health, e.width, e.height, 1F});
//
//
//		}
//		
		ArrayList<Object[]> properties = new ArrayList<Object[]>();
		properties.add(new Object[] {id, c, wallID, building});
		
		ArrayList<ArrayList<Object[]>> world = new ArrayList<ArrayList<Object[]>>();
		world.add(tileSave);
		world.add(piecesSave);
		world.add(entitySave);
		world.add(properties);
		
		return world;

	}

}
