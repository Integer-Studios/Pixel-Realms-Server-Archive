package com.pixel.building;

import java.util.ArrayList;

import com.pixel.tile.Tile;

public class Building {

	public static ArrayList<BuildingInfo> info = new ArrayList<BuildingInfo>();
	public Tile[] tiles;

	public int id, x, y, width, height;
	public BuildingDoor door;
	
	public Building(int id, int x, int y) {
		
		this.id = id;
		this.x = x;
		this.y = y;
		
		this.width = info.get(id).width;
		this.height = info.get(id).height;
		this.door = info.get(id).door;
		this.tiles = info.get(id).tiles;

	}
	
	static {
		
		info.add(new BuildingInfo(0, 5, 5).setInterior(6, 6).setDoor(new BuildingDoor(0F, 0F, 0, 0)));
		
	}
	
}
