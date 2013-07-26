package com.pixel.entity.ai;

import com.pixel.world.WorldServer;

public class HerdUpdater extends Thread {

	public Herd herd;
	public WorldServer world;
	
	public HerdUpdater(Herd herd, WorldServer world) {
		
		this.herd = herd;
		this.world = world;
		
	}
	
	public void run(){
		
		herd.tick(world);
		
	}
	
}
