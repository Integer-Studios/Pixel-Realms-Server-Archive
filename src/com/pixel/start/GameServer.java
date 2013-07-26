package com.pixel.start;

import com.pixel.world.WorldServer;

public class GameServer extends Thread {

    final double GAME_HERTZ = 30.0;
    final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
    double lastUpdateTime;
    public WorldServer world;
    public boolean running = true;
    
	public GameServer(WorldServer world) {
		
		this.world = world;

	}
	
	public void run() {

		lastUpdateTime = System.nanoTime();

		while (running) {

			double now = System.nanoTime();
//			int updateCount = 0;

			while (now - lastUpdateTime > TIME_BETWEEN_UPDATES)	{
				world.tick();
				lastUpdateTime += TIME_BETWEEN_UPDATES;
//				updateCount++;
			}
			
			while (now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
               Thread.yield();
            
               try {Thread.sleep(1);} catch(Exception e) {} 
            
               now = System.nanoTime();
            }

		}

	}
		
}
