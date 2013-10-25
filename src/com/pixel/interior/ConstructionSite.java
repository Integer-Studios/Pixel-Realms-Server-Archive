package com.pixel.interior;

import java.util.HashMap;

import com.pixel.communication.packet.PacketChangePiece;
import com.pixel.communication.packet.PacketUpdateConstructionSite;
import com.pixel.piece.PieceBuilding;
import com.pixel.player.PlayerManager;
import com.pixel.util.CoordinateKey;

public class ConstructionSite {

	public int x, y;
	public int buildingID;
	public HashMap<Integer, Integer> items = new HashMap<Integer, Integer>();	
	
	public ConstructionSite(int x, int y, int buildingID) {
		
		this.x = x;
		this.y = y;
		this.buildingID = buildingID;
		
		for (Integer id : Building.info.get(buildingID).requirements.keySet()) {

			items.put(id, Building.info.get(buildingID).requirements.get(id));
			
		}
		
	}
	
	public int addItem(int id, int amount, int userID) {
		
		if (items.containsKey(id)) {
			
			if (amount <= items.get(id)) {
				amount = items.get(id) - amount;
				items.put(id, amount);
			} else {
				
				amount -= items.get(id);
				items.put(id, 0);
				
			}

		} else {
			
			return -1;
			
		}
		System.out.println(isCompleted());
		PlayerManager.broadcastPacketExcludingPlayer(new PacketUpdateConstructionSite(this), userID);
		if (isCompleted()) {
			
			PlayerManager.broadcastPacket(new PacketChangePiece(new PieceBuilding(x, y, buildingID)));
			ConstructionSiteManager.sites.remove(new CoordinateKey(x, y));
			
		}
		return 0;
		
	}
	
	
	public boolean isCompleted() {
		
		for (Integer id : Building.info.get(buildingID).requirements.keySet()) {
			
			if (!items.containsKey(id)) {
				
				return false;
				
			} else {
				
				if (items.get(id) != 0) {
					
					return false;
					
				}
				
			}
			
		}
		
		return true;
		
	}
	
}
