package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.pixel.piece.Piece;
import com.pixel.piece.PieceBuilding;
import com.pixel.player.PlayerManager;

public class PacketUpdatePiece extends Packet {

	public int pieceID, posX, posY, damage, metadata, buildingID;
	public Piece piece;
	
	public PacketUpdatePiece() {
		this.id = 5;
	}
	
	public PacketUpdatePiece(Piece piece) {
		this.id = 5;
		this.piece = piece;
		this.pieceID = piece.id;
		this.posX = piece.posX;
		this.posY = piece.posY;
		this.damage = piece.damage;
		this.metadata = piece.metadata;
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(pieceID);
		output.writeInt(posX);
		output.writeInt(posY);
		output.writeInt(damage);
		output.writeInt(metadata);

		if (piece instanceof PieceBuilding) {

			output.writeBoolean(true);
			output.writeInt(((PieceBuilding) piece).building.worldID);
			output.writeInt(((PieceBuilding) piece).building.id);

		} else {

			output.writeBoolean(false);

		}

	}

	@Override
	public void readData(DataInputStream input) throws IOException {

		pieceID = input.readInt();
		posX = input.readInt();
		posY = input.readInt();
		damage = input.readInt();
		metadata = input.readInt();
		
		if (input.readBoolean()) {
			
			buildingID = input.readInt();
			PlayerManager.broadcastPacket(new PacketUpdatePiece(new PieceBuilding(posX, posY, buildingID)));
			
		} else {
			
			buildingID = -1;
			
		}
		
		PacketHandler.processUpdatePiece(this);
		
	}
	
}
