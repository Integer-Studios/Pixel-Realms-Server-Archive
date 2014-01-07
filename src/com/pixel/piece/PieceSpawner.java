package com.pixel.piece;

import com.pixel.world.WorldServer;
import com.pixel.world.WorldSpawnerManager;

public class PieceSpawner extends PieceInfo {
	
	public void onCreated(Piece p) {
		
		if (!WorldSpawnerManager.spawnerRegistered(p.posX, p.posY))
			WorldSpawnerManager.registerSpawner(p.posX, p.posY, p.id);
		
		WorldSpawnerManager.getSpawner(p.posX, p.posY).spawnGroup();
		
	}

	public void tick (WorldServer w, Piece p) {
		
		WorldSpawnerManager.getSpawner(p.posX, p.posY).tick(w);
		
	}
	

	
}
