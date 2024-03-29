package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.pixel.entity.EntityAlive;
import com.pixel.interior.BuildingListener;
import com.pixel.interior.InteriorWorld;
import com.pixel.interior.InteriorWorldManager;
import com.pixel.piece.Piece;
import com.pixel.player.PlayerManager;
import com.pixel.tile.Tile;

public class PacketLoadInterior extends Packet {

	int worldID;
	
 	public PacketLoadInterior() {
		
		this.id = 14;
		
	}
	
	@Override
	public void writeData(DataOutputStream output) throws IOException {

		InteriorWorld w = InteriorWorldManager.interiors.get(worldID);
		
		output.writeInt(worldID);
		output.writeInt(w.c);
		output.writeInt(w.tiles.size());
		
		for (Tile t : w.tiles.values()) {
			
			output.writeInt(t.id);
			output.writeInt(t.posX);
			output.writeInt(t.posY);
			output.writeInt(t.metadata);

		}
		
		output.writeInt(w.pieces.size());
		
		for (Piece p : w.pieces.values()) {
			
			output.writeInt(p.id);
			output.writeInt(p.posX);
			output.writeInt(p.posY);
			output.writeInt(p.metadata);
			output.writeInt(p.damage);


		}
		
		output.writeInt(w.entities.size());
		
		for (EntityAlive e : w.entities.values()) {
			
			output.writeInt(e.id);
			output.writeFloat(e.getX());
			output.writeFloat(e.getY());
			output.writeFloat(e.health);
			output.writeInt(e.serverID);


		}
		
	}

	@Override
	public void readData(DataInputStream input) throws IOException {

		worldID = input.readInt();

		PlayerManager.getPlayer(userID).worldID = worldID;
		PlayerManager.getPlayer(userID).inside = true;
		PlayerManager.getPlayer(userID).setPosition(1.5F, 4F);
		
		BuildingListener.setWorld(userID, worldID);
		
		PlayerManager.broadcastPacket(new PacketUpdateLivingEntity(PlayerManager.players.get(userID)));
		PlayerManager.sendPacketToPlayer(userID, this);
		
	}

}
