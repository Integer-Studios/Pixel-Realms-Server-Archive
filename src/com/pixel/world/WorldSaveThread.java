package com.pixel.world;

import java.awt.Color;
import java.util.ArrayList;

import com.pixel.admin.PixelLogger;
import com.pixel.admin.PixelLogger.PixelColor;
import com.pixel.chat.ChatMessage;
import com.pixel.communication.packet.PacketChat;
import com.pixel.entity.Entity;
import com.pixel.piece.Piece;
import com.pixel.player.PlayerManager;
import com.pixel.tile.Tile;
import com.pixel.util.Toolkit;

public class WorldSaveThread implements Runnable {

	WorldServer world;

	public WorldSaveThread(WorldServer world) {

		this.world = world;

	}

	public void run() {

		Toolkit k = new Toolkit();
		
		ArrayList<Integer[]> tileSave = new ArrayList<Integer[]>();

		for (int x = 0; x < WorldServer.tiles.size(); x ++) {

			Tile t = WorldServer.tiles.get(x);
			tileSave.add(new Integer[]{t.id, t.posX, t.posY, t.metadata, 1});
			
		}
		
		k.save("tiles.dat", tileSave);

		PixelLogger.print("Tiles Saved!", PixelColor.PURPLE);
		
		ArrayList<Integer[]> piecesSave = new ArrayList<Integer[]>();

		for (int x = 0; x < WorldServer.pieces.length; x ++) {

			Piece p = WorldServer.pieces[x];
			piecesSave.add(new Integer[]{p.id, p.posX, p.posY, p.damage, p.metadata, 1});

		}
		k.save("pieces.dat", piecesSave);

		PixelLogger.print("Pieces Saved!", PixelColor.PURPLE);

		ArrayList<Float[]> entitySave = new ArrayList<Float[]>();

		for (int x = 0; x < WorldServer.entities.size(); x ++) {

			Entity e = (Entity) WorldServer.entities.values().toArray()[x];
			entitySave.add(new Float[]{e.id + 0F, e.getX(), e.getY(), e.health, e.width, e.height, 1F});


		}
		k.save("entities.dat", entitySave);
		
		ArrayList<ArrayList<Float[]>> herdSave = new ArrayList<ArrayList<Float[]>>();

		k.save("herds.dat", herdSave);

		PixelLogger.print("Entities Saved!", PixelColor.PURPLE);

		PixelLogger.print("Save Complete!", PixelColor.RED);
		
		PlayerManager.broadcastPacket(new PacketChat(new ChatMessage("Server", "Save Complete!", Color.BLUE, 0)));

	}

}