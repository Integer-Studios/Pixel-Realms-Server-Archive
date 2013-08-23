package com.pixel.world;

import java.util.concurrent.ConcurrentHashMap;

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
	
}
