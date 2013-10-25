package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.pixel.interior.ConstructionSite;
import com.pixel.interior.ConstructionSiteManager;
import com.pixel.util.CoordinateKey;

public class PacketUpdateConstructionSite extends Packet {

	public int siteX, siteY, buildingID;
	public boolean complete;
	public HashMap<Integer, Integer> resources = new HashMap<Integer, Integer>();
	
	public PacketUpdateConstructionSite() {
		
		this.id = 21;
		
	}
	
	public PacketUpdateConstructionSite(ConstructionSite site) {
		
		this.id = 21;
		siteX = site.x;
		siteY = site.y;
		buildingID = site.buildingID;
		resources = site.items;
		complete = site.isCompleted();
		
	}
	
	@Override
	public void writeData(DataOutputStream output) throws IOException {

		output.writeInt(siteX);
		output.writeInt(siteY);
		output.writeInt(buildingID);
		
		if (!complete) 
			output.writeInt(resources.size());
		else {
			output.writeInt(-1);
			return;
		}

		for (Integer i : resources.keySet()) {

			output.writeInt(i);
			output.writeInt(resources.get(i));
			
		}
		
	}

	@Override
	public void readData(DataInputStream input) throws IOException {

		this.siteX = input.readInt();
		this.siteY = input.readInt();
		int itemID = input.readInt();
		int amount = input.readInt();
		
		ConstructionSiteManager.sites.get(new CoordinateKey(siteX, siteY)).addItem(itemID, amount, userID);
		
	}

}
