package com.pixel.communication.packet;

import com.pixel.communication.CommunicationServer;
import com.pixel.communication.CommunicationServlet;
import com.pixel.entity.EntityAlive;
import com.pixel.entity.EntityPlayer;
import com.pixel.interior.InteriorWorld;
import com.pixel.interior.InteriorWorldManager;
import com.pixel.item.Item;
import com.pixel.item.ItemStack;
import com.pixel.piece.Piece;
import com.pixel.player.PlayerLoadThread;
import com.pixel.player.PlayerManager;
import com.pixel.start.PixelRealmsServer;
import com.pixel.world.WorldServer;

public class PacketHandler {

	public static void processLoginPacket(PacketLogin packet) {
		
		PlayerManager.playerLogin(packet);
		
	}

	public static void processMoveLivingEntity(PacketMoveLivingEntity packet) {
		PlayerManager.getPlayer(packet.userID).setVelocity(packet.velocityX, packet.velocityY);
		float xDiff = Math.abs(WorldServer.entities.get(packet.serverID).getX() - packet.posX);
		float yDiff = Math.abs(WorldServer.entities.get(packet.serverID).getY() - packet.posY);
		
		PlayerManager.broadcastPacket(packet);

		if (WorldServer.entities.get(packet.serverID).velocityX == 0 && WorldServer.entities.get(packet.serverID).velocityY == 0) {

			if (xDiff < 1 && yDiff < 1) {
				
				WorldServer.entities.get(packet.serverID).setPosition(packet.posX, packet.posY);
				
			}
			
			PlayerManager.broadcastPacket(new PacketUpdateLivingEntity(packet.serverID));

		} 

	}


	public static void processUpdateTile(PacketUpdateTile packet) {
		
		WorldServer.setTile(packet.posX, packet.posY, packet.tileID, packet.metadata);
		
	}
	
	public static void processChangePiece(PacketChangePiece packet) {
		
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

		entity = (EntityAlive)WorldServer.getEntity(packet.serverID);

		if (entity != null) {
			entity.damage(PixelRealmsServer.world, packet.damage, PlayerManager.getPlayer(packet.userID));
		}
	}
	
	public static void processInfoRequest(PacketInfoRequest packet) {
		
//		if (packet.request.equals("players")) {
//			
//			PlayerManager.sendPlayers(packet.userID);
//			
//		}
		
	}

	public static void processUpdateInteriorPiece(PacketUpdateInteriorPiece packet) {
		
		if (InteriorWorldManager.interiors.containsKey(packet.worldID)) {
			
			InteriorWorld world = InteriorWorldManager.interiors.get(packet.worldID);
			if (world.pieces.contains((packet.y*world.c) + packet.x))
				world.pieces.replace((packet.y*world.c) + packet.x, new Piece(packet.pieceID, packet.x, packet.y, packet.damage, packet.metadata, false));
			else
				world.pieces.put((packet.y*world.c) + packet.x, new Piece(packet.pieceID, packet.x, packet.y, packet.damage, packet.metadata, false));

			PlayerManager.broadcastPacketToWorld(packet.worldID, new PacketUpdateInteriorPiece(packet.worldID, world.pieces.get((packet.y * world.c) + packet.x)));
			
			InteriorWorldManager.interiors.put(packet.worldID, world);
			
		}
		
	}

	public static void processUpdatePiece(PacketUpdatePiece packet) {

		WorldServer.getPieceObject(packet.posX, packet.posY).metadata = packet.metadata;

		
	}

	public static void processEntityAnimation(PacketEntityAnimation packet) {

		PlayerManager.broadcastPacket(packet);
		
	}

	public static void processUpdateLivingEntity(PacketUpdateLivingEntity packet) {
		
		
		((EntityAlive) WorldServer.entities.get(packet.serverID)).setX(packet.posX);
		((EntityAlive) WorldServer.entities.get(packet.serverID)).setY(packet.posY);
		
		if (((EntityAlive) WorldServer.entities.get(packet.serverID)).metadata.objects.containsKey(3)) {
		
			((EntityPlayer) WorldServer.entities.get(packet.serverID)).setSelectedItem(new ItemStack(Item.getItemForId(packet.itemID), packet.itemAmount));

		}
		
		for (int x = 0; x < CommunicationServer.userConnections.size(); x ++) {
			
			CommunicationServlet servlet = CommunicationServer.userConnections.get(CommunicationServer.userConnections.keySet().toArray()[x]);
			
			if (CommunicationServlet.getUserID(servlet) != packet.userID) {
				CommunicationServlet.addPacket(servlet, new PacketUpdateLivingEntity(((EntityAlive) WorldServer.entities.get(packet.serverID)).metadata, packet.posX, packet.posY, packet.health, packet.satisfaction, packet.energy, packet.serverID));
				
			}
			
		}
		
	}

	public static void processLoginRequest(PacketLoginRequest packet) {
		System.out.println("A");

		new Thread(new PlayerLoadThread(packet)).start();
		
	}

	public static void processLoadPlayer(PacketLoadPlayer packet) {

		PlayerManager.requestLoadPlayer(packet);
		
	}

}
