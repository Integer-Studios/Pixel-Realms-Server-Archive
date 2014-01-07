package com.pixel.world;

import java.util.HashMap;

import com.pixel.util.CoordinateKey;

public class WorldSpawnerManager {
	
	public static HashMap<CoordinateKey, WorldSpawner> spawners = new HashMap<CoordinateKey, WorldSpawner>();

	public static boolean spawnerRegistered(int x, int y) {
		
		if (spawners.containsKey(new CoordinateKey(x, y))) {
			
			return true;
			
		}
		
		return false;
		
	}
	
	public static void registerSpawner(int x, int y, int id) {
		
		CoordinateKey key = new CoordinateKey(x, y);

		switch (id) {

		case 28:
			//pog
			spawners.put(key, new WorldSpawner(x, y, 2, 10));
			break;

		}
		
	}

	public static WorldSpawner getSpawner(int posX, int posY) {
		
		return spawners.get(new CoordinateKey(posX, posY));
	
	}
	
}
