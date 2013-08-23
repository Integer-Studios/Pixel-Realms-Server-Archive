package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.pixel.communication.CommunicationServer;
import com.pixel.communication.CommunicationServlet;
import com.pixel.player.PlayerManager;
import com.pixel.start.PixelRealmsServer;
import com.pixel.world.WorldComponent;
import com.pixel.world.WorldServer;

public class PacketWorldData extends Packet {

	public PacketWorldData() {}
	
	public PacketWorldData(int userID) {
		
		this.id = 3;
		this.userID = userID;
		
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		
		ArrayList<ArrayList<WorldComponent>> components = PixelRealmsServer.world.getComponentsToLoad((int)PlayerManager.getPlayer(userID).getX(), (int)PlayerManager.getPlayer(userID).getY(), userID);
		
		ArrayList<WorldComponent> loadTiles = components.get(0);
		ArrayList<WorldComponent> loadPieces = components.get(1);
		ArrayList<WorldComponent> loadEntities = components.get(2);
		ArrayList<WorldComponent> loadPlayers = components.get(3);

		output.writeInt(WorldServer.c);
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
		
		output.writeInt(loadPlayers.size());
		
		for (int x = 0; x < loadPlayers.size(); x ++) {

			output.writeInt(loadPlayers.get(x).userID);
			Packet.writeString(loadPlayers.get(x).username, output);
			output.writeFloat(loadPlayers.get(x).posX);
			output.writeFloat(loadPlayers.get(x).posY);
			
		}
		
	}

	@Override
	public void readData(DataInputStream input) throws IOException {

		CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), this);

	}

}
