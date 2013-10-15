package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.pixel.building.Building;
import com.pixel.building.BuildingDoor;
import com.pixel.communication.CommunicationServer;
import com.pixel.communication.CommunicationServlet;
import com.pixel.player.PlayerManager;
import com.pixel.start.PixelRealmsServer;
import com.pixel.world.InteriorWorld;
import com.pixel.world.InteriorWorldManager;
import com.pixel.world.WorldComponent;
import com.pixel.world.WorldServer;

public class PacketUpdateWorld extends Packet {

	private int posX, posY; 

	public PacketUpdateWorld() {
		
		this.id = 6;
		
	}
	
	@Override
	public void writeData(DataOutputStream output) throws IOException {
		ArrayList<ArrayList<WorldComponent>> components = PixelRealmsServer.world.getComponentsToLoad(posX, posY, userID);
		
		ArrayList<WorldComponent> loadTiles = components.get(0);
		ArrayList<WorldComponent> loadPieces = components.get(1);
		ArrayList<WorldComponent> loadEntities = components.get(2);
		
		output.writeInt(loadTiles.size());
		
		for (int x = 0; x < loadTiles.size(); x ++) {
			
			output.writeInt(loadTiles.get(x).id);
			output.writeInt((int) loadTiles.get(x).posX);
			output.writeInt((int) loadTiles.get(x).posY);
			output.writeInt((int) loadTiles.get(x).metadata);
			
		}

		output.writeInt(loadPieces.size());

		for (int x = 0; x < loadPieces.size(); x ++) {
			
			output.writeInt(loadPieces.get(x).id);
			output.writeInt((int) loadPieces.get(x).posX);
			output.writeInt((int) loadPieces.get(x).posY);
			output.writeInt((int) loadPieces.get(x).damage);
			output.writeInt((int) loadPieces.get(x).metadata);
			
			if (loadPieces.get(x).buildingID != -1) {
				
				output.writeBoolean(true);
				output.writeInt(loadPieces.get(x).worldID);
				output.writeInt(loadPieces.get(x).buildingID);
				
			} else {
				
				output.writeBoolean(false);
				
			}
			
		}
		
		output.writeInt(loadEntities.size());

		for (int x = 0; x < loadEntities.size(); x ++) {
			
			output.writeInt(loadEntities.get(x).id);
			output.writeFloat(loadEntities.get(x).posX);
			output.writeFloat(loadEntities.get(x).posY);
			output.writeInt(loadEntities.get(x).serverID);
			
		}
		
	}

	@Override
	public void readData(DataInputStream input) throws IOException {

		posX = input.readInt();
		posY = input.readInt();
		
		if (PlayerManager.getPlayer(userID).inside) {
			
			PlayerManager.getPlayer(userID).inside = false;
			float x, y;
			InteriorWorld w = InteriorWorldManager.interiors.get(PlayerManager.getPlayer(userID).worldID);
			BuildingDoor d = Building.info.get(InteriorWorldManager.interiors.get(PlayerManager.getPlayer(userID).worldID).buildingID).door;
					
			x = (d.x / WorldServer.tileConstant) + w.doorX + .4F;
			y = (d.y / WorldServer.tileConstant) + w.doorY + 1.5F;

			PlayerManager.getPlayer(userID).setPosition(x, y);
			PlayerManager.getPlayer(userID).worldID = -1;
			PlayerManager.broadcastPacket(new PacketUpdatePlayer(userID));
		}
		
		CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), this);
		
	}

}
