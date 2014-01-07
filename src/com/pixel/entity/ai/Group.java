package com.pixel.entity.ai;

import java.util.ArrayList;

import com.pixel.world.WorldServer;

public class Group {

	public int entityID;
	public int spawnSize;
	public int spawnX, spawnY;
	
	public ArrayList<Integer> entitiesLiving = new ArrayList<Integer>();
	
	public Group(int entityID, int spawnSize) {
		
		this.entityID = entityID;
		this.spawnSize = spawnSize;
		
	}

	public void tick(WorldServer w) {
		// TODO Auto-generated method stub
		
	}
	
	public int calculateDistance(int x, int y) {
		
		int count = 0, avg = 0, total = 0;
		
		for (int a = 0; a < entitiesLiving.size(); a++){
			
			int tempX = Math.round(WorldServer.getEntity(entitiesLiving.get(a)).getX());
			int tempY = Math.round(WorldServer.getEntity(entitiesLiving.get(a)).getY());
			
			int xTemp = (tempX - x) * (tempX - x);
			int yTemp = (tempY - y) * (tempY - y);
			int dist = (int) Math.sqrt(xTemp + yTemp);
			
			total += dist;
			count ++;

		}
		
		avg = total / count;
		
		return avg;
		
	}
	
}
