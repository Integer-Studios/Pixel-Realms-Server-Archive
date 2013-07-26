package com.pixel.tile;

import com.pixel.entity.EntityPlayer;
import com.pixel.player.PlayerManager;
import com.pixel.world.WorldServer;

public class Tile {
	
	public Tile(int x, int y, int i) {
		id = i;
		posX = x;
		posY = y;
		WorldServer.propagateTile(this);
			
	}
	
	public Tile(int x, int y, int i, boolean propagate) {
		id = i;
		posX = x;
		posY = y;
		
		if (propagate)
			WorldServer.propagateTile(this);
			
	}
	
	public void tick(WorldServer w) {
		info[id].tick(w, this);
		
		for (int x = 0; x < PlayerManager.players.size(); x ++) {
			
			EntityPlayer player = PlayerManager.players.get(PlayerManager.players.keySet().toArray()[x]);
			if ((int)player.getX() == posX && (int)player.getY() == posY) {
				info[id].onPlayerWalkOver(w, this, player);
			}
			
		}
		
		
	}
	
	static {
		info = new TileInfo[]{
				new TileInfo(), //grass flat 0
				new TileInfo(), //sand flat 1
				new TileRounded(0), //sand rounded 2
				new TileInfo(), //water flat 3
				new TileRounded(0), //water rounded 4
				new TileInfo(), //cobble flat 3
				new TileRounded(0) //cobble rounded 4
		};
		
	}
	
	public int id;
	public int posX;
	public int posY;
	public static TileInfo[] info;
}
