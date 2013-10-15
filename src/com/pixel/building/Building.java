package com.pixel.building;

import java.util.ArrayList;

import com.pixel.world.InteriorWorld;
import com.pixel.world.InteriorWorldManager;
import com.pixel.world.WorldServer;

public class Building {

	public static ArrayList<BuildingInfo> info = new ArrayList<BuildingInfo>();

	public int worldID;
	public int id, x, y, width, height;
	public InteriorWorld interior;
	public BuildingDoor door;
	
	public Building(int x, int y, int id) {
		
		this.id = id;
		this.x = x;
		this.y = y;
		
		this.width = info.get(id).width;
		this.height = info.get(id).height;
		this.door = info.get(id).door;
		this.interior = info.get(id).interior;
		this.worldID = InteriorWorldManager.addWorld(this.interior, x, y);

	}
	

	public Building(int x, int y, int id, int worldID) {
		
		this.id = id;
		this.x = x;
		this.y = y;
		
		this.width = info.get(id).width;
		this.height = info.get(id).height;
		this.door = info.get(id).door;
		this.worldID = worldID;
		this.interior = InteriorWorldManager.loadWorld(worldID);
		this.interior.doorX = x;
		this.interior.doorY = y;
	

	}
	
public static boolean canBuildingFit(int buildingID, int x, int y) {
		
		int width = info.get(buildingID).width;
		int height = info.get(buildingID).height;
		
		ArrayList<Integer[]> deleteables = new ArrayList<Integer[]>();
		
		for (int b = x; b < (x + width); b ++) {
			
			for (int i = (y - height); i < y; i ++) {

				if (WorldServer.pieces[((i * WorldServer.c) + b)] != null) {
					
					int tempID = WorldServer.pieces[((i * WorldServer.c) + b)].id;
					
					if (tempID != 0 && tempID != 1 && tempID != 2 && tempID != 3) {
						
						//Obstruction
						System.out.println("Obstruction");
						return false;
						
					} else {
						
						deleteables.add(new Integer[]{b, i});
						
					}
					
				}

			}

		}
		
		for (Integer[] coord : deleteables) {
			
			WorldServer.setPiece(coord[0], coord[1], 0);
			
		}
		
		return true;
	}
	
	static {
		
		info.add(new BuildingInfo(0, 4, 3, 1000).setInterior(6, 6).setDoor(new BuildingDoor(46F, -10F, 32, 56, 0)));
		
	}
	
}
