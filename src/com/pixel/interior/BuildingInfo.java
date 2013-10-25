package com.pixel.interior;

import java.util.HashMap;

public class BuildingInfo {

	public int width, height;
	public int id;
	public BuildingDoor door;
	public InteriorWorld interior;
	public int maxDamage;
	public HashMap<Integer, Integer> requirements = new HashMap<Integer, Integer>();
	
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
	
	
	public BuildingInfo addRequirement(int id, int amount) {
		
		requirements.put(id, amount);
		return this;
		
	}
	
}
