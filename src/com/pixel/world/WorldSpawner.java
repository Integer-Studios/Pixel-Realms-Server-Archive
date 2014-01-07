package com.pixel.world;

import java.util.ArrayList;

import com.pixel.entity.ai.Group;
import com.pixel.entity.ai.GroupPog;

public class WorldSpawner {

	public int entityID;
	public int size;
	public int x, y;
	
	public ArrayList<Group> groups = new ArrayList<Group>();
	
	public WorldSpawner(int x, int y, int entityID, int size) {
		
		this.x = x;
		this.y = y;
		this.entityID = entityID;
		this.size = size;
		
	}
	
	public void spawnGroup() {
		
		groups.add(new GroupPog(x, y).spawn());
		
	}
	
	public void tick(WorldServer w) {
		
		for (Group g : groups) {
			
			g.tick(w);
			
		}
		
	}
	
}
