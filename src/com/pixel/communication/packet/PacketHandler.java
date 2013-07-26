package com.pixel.communication.packet;


import com.pixel.communication.CommunicationServer;
import com.pixel.communication.CommunicationServlet;
import com.pixel.entity.EntityAlive;
import com.pixel.item.Item;
import com.pixel.item.ItemStack;
import com.pixel.player.PlayerLoadThread;
import com.pixel.player.PlayerManager;
import com.pixel.start.PixelRealmsServer;
import com.pixel.world.WorldServer;

public class PacketHandler {

	public static void processLoginPacket(PacketLogin packet) {
		
		new Thread(new PlayerLoadThread(packet)).start();

	}
	
	public static void processUpdatePlayer(PacketUpdatePlayer packet) {
		
		PlayerManager.players.get(packet.userID).setPosX(packet.posX);
		PlayerManager.players.get(packet.userID).setPosY(packet.posY);
		PlayerManager.players.get(packet.userID).setSelectedItem(new ItemStack(Item.getItemForId(packet.itemID), packet.itemAmount));

		for (int x = 0; x < CommunicationServer.userConnections.size(); x ++) {
			
			CommunicationServlet servlet = CommunicationServer.userConnections.get(CommunicationServer.userConnections.keySet().toArray()[x]);
			
			if (CommunicationServlet.getUserID(servlet) != packet.userID) {
				CommunicationServlet.addPacket(servlet, new PacketUpdatePlayer(packet.username, packet.posX, packet.posY, packet.satisfaction, packet.health, packet.energy, packet.userID, new ItemStack(new Item(packet.itemID), packet.itemAmount)));
				
			}
			
		}
		
	}
	
	public static void processUpdateTile(PacketUpdateTile packet) {
		
		WorldServer.setTile(packet.posX, packet.posY, packet.tileID);
		
	}
	
	public static void processUpdatePiece(PacketUpdatePiece packet) {
		
		WorldServer.setPiece(packet.posX, packet.posY, packet.pieceID, packet.damage, packet.metadata, packet.buildingID);

	}
	
	public static void processDamagePiece(PacketDamagePiece packet) {
		
		WorldServer.getPieceObject(packet.posX, packet.posY).damage(packet.deltaDamage, PixelRealmsServer.world, PlayerManager.getPlayer(packet.userID));

	}
	
	public static void processUpdateWorld(PacketUpdateWorld packet) {
		

		CommunicationServlet servlet = CommunicationServer.userConnections.get(packet.userID);
		CommunicationServlet.addPacket(servlet, packet);

	}
	
	public static void processUpdateInventoryContent(PacketUpdateInventoryContent packet) {
		if (packet.inventory == 0) {
			PlayerManager.getPlayer(packet.userID).getPlayerInventory().hotbar.clientSetContent(packet.x, packet.y, new ItemStack(Item.getItemForId(packet.itemID), packet.size));
		} else if (packet.inventory == 1) {
			PlayerManager.getPlayer(packet.userID).getPlayerInventory().inventoryLeft.clientSetContent(packet.x, packet.y, new ItemStack(Item.getItemForId(packet.itemID), packet.size));
		} else if (packet.inventory == 2) {
			PlayerManager.getPlayer(packet.userID).getPlayerInventory().inventoryRight.clientSetContent(packet.x, packet.y, new ItemStack(Item.getItemForId(packet.itemID), packet.size));
		}
		
	}

	public static void processDamageEntity(PacketDamageEntity packet) {

		EntityAlive entity = null;
		
		if (packet.herdID != -1) {
		
			entity = (EntityAlive)WorldServer.herds.get(packet.herdID).entities.get(packet.serverID);
		
		} else {

			entity = (EntityAlive)WorldServer.getEntity(packet.serverID);
			
		}
		
		if (entity != null) {
			entity.damage(PixelRealmsServer.world, packet.damage, PlayerManager.getPlayer(packet.userID));
		}
	}
	
}