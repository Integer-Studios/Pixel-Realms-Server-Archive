package com.pixel.world;

import java.util.concurrent.ConcurrentHashMap;

import com.pixel.building.Building;

public class InteriorWorldManager {

	public static ConcurrentHashMap<Integer, InteriorWorld> interiors = new ConcurrentHashMap<Integer, InteriorWorld>();

	public static int addWorld(InteriorWorld world) {
		
		int id = interiors.size() + 1;
		interiors.put(id, world);
		return id;
		
	}
	
	public static void saveInteriors() {
		
		
		
	}
	
	public static void loadInteriors() {
		
		
		
	}

	public static InteriorWorld loadWorld(int worldID, int buildingID) {
		System.out.println(worldID + " " + buildingID);
		interiors.put(worldID, Building.info.get(buildingID).interior);
		return Building.info.get(buildingID).interior;
	}
	
}
