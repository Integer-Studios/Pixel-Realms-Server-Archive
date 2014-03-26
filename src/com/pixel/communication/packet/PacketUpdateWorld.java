package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.pixel.communication.CommunicationServer;
import com.pixel.communication.CommunicationServlet;
import com.pixel.entity.Entity;
import com.pixel.interior.Building;
import com.pixel.interior.BuildingDoor;
import com.pixel.interior.BuildingListener;
import com.pixel.interior.InteriorWorld;
import com.pixel.interior.InteriorWorldManager;
import com.pixel.piece.Piece;
import com.pixel.piece.PieceBuilding;
import com.pixel.player.PlayerManager;
import com.pixel.start.PixelRealmsServer;
import com.pixel.tile.Tile;
import com.pixel.world.WorldChunk;
import com.pixel.world.WorldServer;

public class PacketUpdateWorld extends Packet {

	private int posX, posY; 

	public PacketUpdateWorld() {
		
		this.id = 6;
		
	}
	
	@Override
	public void writeData(DataOutputStream output) throws IOException {

		ArrayList<WorldChunk> chunks = PixelRealmsServer.world.getChunksToLoad(posX, posY, userID);
		
		output.writeInt(WorldServer.c);
		output.writeInt(chunks.size());
		
		for (WorldChunk c : chunks) {
			
			output.writeInt(c.x);
			output.writeInt(c.y);
			
			output.writeInt(c.tiles.size());
			for (Tile t : c.tiles.values()) {
				
				output.writeInt(t.id);
				output.writeInt(t.posX);
				output.writeInt(t.posY);
				output.writeInt(t.metadata);

			}
			
			output.writeInt(c.pieces.size());
			for (Piece p : c.pieces.values()) {
				
				output.writeInt(p.id);
				output.writeInt(p.posX);
				output.writeInt(p.posY);
				output.writeInt(p.metadata);
				output.writeInt(p.damage);
				output.writeInt(p.lightID);
				if (p instanceof PieceBuilding) {
					
					output.writeBoolean(true);
					output.writeInt(((PieceBuilding) p).building.worldID);
					output.writeInt(((PieceBuilding) p).building.id);
					
				} else {
					
					output.writeBoolean(false);
					
				}
			}
			
			output.writeInt(c.entities.size());

			for (Entity e : c.entities.values()) {
				
				output.writeInt(e.id);
				output.writeFloat(e.getX());
				output.writeFloat(e.getY());
				output.writeInt(e.serverID);
				
			}
			
//			output.writeInt(c.size());
//			
//			for (int x = 0; x < loadPlayers.size(); x ++) {
//
//				output.writeInt(loadPlayers.get(x).userID);
//				Packet.writeString(loadPlayers.get(x).username, output);
//				output.writeFloat(loadPlayers.get(x).posX);
//				output.writeFloat(loadPlayers.get(x).posY);
//				
//			}

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
			BuildingListener.setWorld(userID, -1);
			PlayerManager.broadcastPacket(new PacketUpdatePlayer(userID));
		}
		
		CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), this);
		
	}

}
