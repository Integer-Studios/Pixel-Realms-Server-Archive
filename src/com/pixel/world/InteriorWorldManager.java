package com.pixel.world;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.pixel.piece.Piece;
import com.pixel.tile.Tile;
import com.pixel.util.Toolkit;

public class InteriorWorldManager {

	public static ConcurrentHashMap<Integer, InteriorWorld> interiors = new ConcurrentHashMap<Integer, InteriorWorld>();

	public static int addWorld(InteriorWorld world, int doorX, int doorY) {
		
		int id = interiors.size() + 1;
		world.id = id;
		world.doorX = doorX;
		world.doorY = doorY;
		interiors.put(id, world);
		return id;
		
	}
	
	public static void saveInteriors() {
		
		Toolkit t = new Toolkit();
		
		ArrayList<ArrayList<ArrayList<Object[]>>> worlds = new ArrayList<ArrayList<ArrayList<Object[]>>>();
		
		for (InteriorWorld w : interiors.values()) {
			
			ArrayList<ArrayList<Object[]>> world = w.save();
			System.out.println(w.id);

			worlds.add(world);
			
		}
		
		t.save("interiors.dat", worlds);
		
	}
	
	public static void loadInteriors() {

		Toolkit k = new Toolkit();

		@SuppressWarnings("unchecked")
		ArrayList<ArrayList<ArrayList<Object[]>>> worlds = (ArrayList<ArrayList<ArrayList<Object[]>>>) k.load("interiors.dat");
		
		for (ArrayList<ArrayList<Object[]>> world : worlds) {
			
			InteriorWorld w = new InteriorWorld();
			w.id = (Integer) world.get(3).get(0)[0];
			w.c = (Integer) world.get(3).get(0)[1];
			w.wallID = (Integer) world.get(3).get(0)[2];
			w.building = (Boolean) world.get(3).get(0)[3];
			
			ArrayList<Object[]> tiles = world.get(0);
			ArrayList<Object[]> pieces = world.get(1);

			for(Object[] t : tiles) {
				
				w.tiles.put((((Integer)t[1] * w.c) + (Integer)t[2]), new Tile((Integer)t[1], (Integer)t[2], (Integer)t[0], (Integer)t[3], false));
				
			}
			
			for(Object[] p : pieces) {
				
				w.pieces.put((((Integer)p[1] * w.c) + (Integer)p[2]), new Piece((Integer)p[0], (Integer)p[1], (Integer)p[2], (Integer)p[3], (Integer)p[4], false));
				
			}
			
			interiors.put(w.id, w);
			
		}
		
	}

	public static InteriorWorld loadWorld(int worldID) {
		return interiors.get(worldID);
	}
	
}
