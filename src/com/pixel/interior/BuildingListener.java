package com.pixel.interior;

import java.util.HashMap;

public class BuildingListener {

	public static HashMap<Integer, Integer> players = new HashMap<Integer, Integer>();
	
	public static void setWorld(int userID, int worldID) {
		
		players.put(userID, worldID);
		
	}

	public static boolean playerInside(int userID) {

		try {
			
			if (players.get(userID) != -1) {

				return true;

			}

		} catch (NullPointerException e){return false;}
		
		return false;
		
	}
	
}
