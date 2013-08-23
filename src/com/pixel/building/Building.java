package com.pixel.building;

import java.util.ArrayList;

import com.pixel.world.InteriorWorld;
import com.pixel.world.InteriorWorldManager;

public class Building {

	public static ArrayList<BuildingInfo> info = new ArrayList<BuildingInfo>();

	public int worldID;
	public int id, x, y, width, height;
	public InteriorWorld interior;
	public BuildingDoor door;
	
	public Building(int x, int y, int id) {
		
		this.id = id;
		this.x = x;
		this.y = y;
		
		this.width = info.get(id).width;
		this.height = info.get(id).height;
		this.door = info.get(id).door;
		this.interior = info.get(id).interior;
		this.worldID = InteriorWorldManager.addWorld(this.interior);

	}
	
	static {
		
		info.add(new BuildingInfo(0, 5, 5).setInterior(6, 6).setDoor(new BuildingDoor(0F, 0F, 0, 0)));
		
	}
	
}
