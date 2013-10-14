package com.pixel.building;

import com.pixel.world.InteriorWorld;

public class BuildingInfo {

	public int width, height;
	public int id;
	public BuildingDoor door;
	public InteriorWorld interior;
	public int maxDamage;
	
	public BuildingInfo(int id, int width, int height, int maxDamage) {
		
		this.id = id;
		this.width = width;
		this.height = height;
		this.maxDamage = maxDamage;
		
	}
	
	public BuildingInfo setDoor(BuildingDoor door) {
		
		this.door = door;
		return this;
		
	}
	
	public BuildingInfo setInterior(int width, int height) {
		
		this.interior = new InteriorWorld(width, height, id);
		
		return this;
		
	}
	
}
