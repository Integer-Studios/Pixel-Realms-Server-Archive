package com.pixel.building;

import com.pixel.tile.Tile;

public class BuildingInfo {

	public int width, height;
	public int id;
	public BuildingDoor door;
	public Tile[] tiles;
	
	public BuildingInfo(int id, int width, int height) {
		
		this.id = id;
		this.width = width;
		this.height = height;
		
	}
	
	public BuildingInfo setDoor(BuildingDoor door) {
		
		this.door = door;
		return this;
		
	}
	
	public BuildingInfo setInterior(int width, int height) {
		
		tiles = new Tile[width * height];
		for (int x = 0; x < width; x ++) {
			
			for (int y = 0; y < height; y ++) {
				
				tiles[((y * (width * height)) + x)] = new Tile(x, y, 0, false);
				
			}
			
		}
		
		return this;
		
	}
	
}
