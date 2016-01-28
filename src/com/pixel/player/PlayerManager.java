package com.pixel.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import java.awt.Color;

import com.pixel.admin.PixelLogger;
import com.pixel.admin.PixelLogger.PixelColor;
import com.pixel.admin.StatisticsThread;
import com.pixel.chat.ChatMessage;
import com.pixel.communication.CommunicationServer;
import com.pixel.communication.CommunicationServlet;
import com.pixel.communication.packet.Packet;
import com.pixel.communication.packet.PacketChat;
import com.pixel.communication.packet.PacketLoadPlayer;
import com.pixel.communication.packet.PacketLogin;
import com.pixel.communication.packet.PacketLoginRequest;
import com.pixel.communication.packet.PacketLoginStage;
import com.pixel.communication.packet.PacketLogout;
import com.pixel.communication.packet.PacketUpdateInteriorPiece;
import com.pixel.communication.packet.PacketUpdateLivingEntity;
import com.pixel.entity.Entity;
import com.pixel.entity.EntityAlive;
import com.pixel.entity.EntityPlayer;
import com.pixel.inventory.InventoryContent;
import com.pixel.item.Item;
import com.pixel.item.ItemStack;
import com.pixel.util.CoordinateKey;
import com.pixel.util.FileItem;
import com.pixel.util.Toolkit;
import com.pixel.world.WorldServer;
import com.pixel.world.WorldChunk;

public class PlayerManager {

	public static ConcurrentHashMap<Integer, Integer> playersLoginStage = new ConcurrentHashMap<Integer, Integer>();
	public static ConcurrentHashMap<Integer, Integer> players = new ConcurrentHashMap<Integer, Integer>();
	public static HashMap<Integer, PlayerInventory> inventories = new HashMap<Integer, PlayerInventory>();
	public static ConcurrentHashMap<Integer, Float[]> playersData = new ConcurrentHashMap<Integer, Float[]>();
	public static ConcurrentHashMap<Integer, Long> playersLoginTime = new ConcurrentHashMap<Integer, Long>();
	
	public static void playerLogin(PacketLogin packet) {
		
		//USER ID
		
		//???
		//SPAWN
		
		EntityPlayer player = spawnPlayer(packet.username, packet.userID, packet.session);

		if (player == null) {
			
			players.remove(packet.userID);
			playersData.remove(packet.userID);
			inventories.remove(packet.userID);
			
		}
		
		packet.posX = player.getX();
		packet.posY = player.getY();
		
		player.loaded = true;
		playersLoginStage.put(packet.userID, 420);
		sendPacketToPlayer(packet.userID, new PacketLoginStage(1));
		broadcastPacketExcludingPlayer(new PacketLogin(player), packet.userID);
		broadcastPacketExcludingPlayer(new PacketChat(new ChatMessage("Server", player.username + " has logged in.", Color.RED, player.userID)), packet.userID);
		PixelLogger.print(player.username + " has logged in.", PixelColor.BLUE);

	}
	
	private static EntityPlayer spawnPlayer(String username, int userID, int session) {

		Float[] data = playersData.get(userID);
		
		int serverID = players.get(userID);
		Entity e = WorldServer.getEntity(serverID);
		
		if (e != null && e.id == session) {
		
			EntityPlayer player = new EntityPlayer(username, userID, data[0], data[1], data[2], data[3], data[4], Math.round(data[5]), serverID, session);
			WorldServer.entities.put(serverID, player);

			return player;

		} else {
			
			return null;
			
		}
		
//		inventories.get(userID).sendInventory();

	}
	
	public static void requestLoadPlayer(PacketLoadPlayer packet) {

		int userID = packet.userID;
		
		if (!playersData.containsKey(userID)) {
			
			if (new FileItem("players/" + userID + ".dat").exists()) {
				
				loadPlayer(userID);
				
			} else {
				
				playersData.put(userID, new Float[]{200F, 200F, 100F, 100F, 100F, -1F});
				inventories.put(userID, new PlayerInventory(userID));
				
			}
			
		} else {
			
			//HAS LOGGED IN CURRENT INSTANCE
			
		}
		
		Float[] data = playersData.get(userID);
		
		float posX = data[0];
		float posY = data[1];
		float health = data[2];
		float satisfaction = data[3];
		float energy = data[4];
		int worldID = Math.round(data[5]);
		
		packet.posX = posX;
		packet.posY = posY;
		packet.health = health;
		packet.satisfaction = satisfaction;
		packet.energy = energy;
		packet.worldID = worldID;
		packet.serverID = players.get(userID);
		packet.inventory = inventories.get(userID);
		
		packet.itemAmount = -1;
		packet.itemID = -1;
		
		sendPacketToPlayer(userID, packet);
		
	}
	
	public static void requestLogin(PacketLoginRequest packet) {
		System.out.println("B");

		if (!players.containsKey(packet.userID)) {
			System.out.println("C");
			playersLoginStage.put(packet.userID, 1);

			respond(packet);

			
		} else {
			
			if (WorldServer.entities.containsKey(players.get(packet.userID))) {
				
				//DENY
				
			} else {
				
				playersLoginStage.put(packet.userID, 1);
				players.remove(packet.userID);
				respond(packet);

			}
			
		}
		
	}
	
	public static void respond(PacketLoginRequest packet) {
		
		CommunicationServlet.setUserID(packet.servletLogin, packet.userID);
		int serverID = new Entity(true, packet.session).serverID;
		players.put(packet.userID, serverID);
		
		packet.serverID = serverID;
		System.out.println("D");

		sendPacketToPlayer(packet.userID, packet);
		
	}
	
//	public static void sendPlayers(int userID) {
//		
//		for (int i: players.values()) {
//			EntityPlayer player = (EntityPlayer) WorldServer.entities.get(i);
//			if (userID != player.userID)
//				sendPacketToPlayer(userID, new PacketUpdateLivingEntity(PlayerManager.getPlayer(player.userID).metadata, PlayerManager.getPlayer(player.userID).posX, PlayerManager.getPlayer(player.userID).posY, PlayerManager.getPlayer(player.userID).health, PlayerManager.getPlayer(player.userID).satisfaction, PlayerManager.getPlayer(player.userID).energy, player.serverID));
//		
//		}
//		
//	}
//	
//	public static void sendEntities(int userID) {
//		
//		for (Entity entity: WorldServer.entities.values()) {
//			sendPacketToPlayer(userID, new PacketUpdateLivingEntity((EntityAlive)entity));
//		
//		}
//
//	}
	
	public static void playerLogout(int userID) {
		
		EntityPlayer p = getPlayer(userID);

		try {
			new StatisticsThread(userID, p.username, 1).start();
		} catch (NullPointerException e){}
		
		if (p != null) {

			Float[] playerData = new Float[]{p.posX, p.posY, p.health, p.satisfaction, p.energy, p.worldID + 0.0F};
			playersData.put(userID, playerData);
			playersLoginStage.remove(userID);
			
			WorldServer.playerChunks.remove(userID);
			players.remove(userID);
			PixelLogger.print(p.username + " has disconnected.", PixelColor.BLUE);
			PlayerManager.broadcastPacket(new PacketLogout(userID));
			broadcastPacket(new PacketChat(new ChatMessage("Server", p.username + " has disconnected.", Color.RED, p.userID)));
			
		}

	}

	public static EntityPlayer getPlayer(int userID) {

		try {
			return (EntityPlayer) WorldServer.entities.get(players.get(userID));
		} catch (NullPointerException e) {
			
			return null;
		}
		
	}

	public static void updatePlayer(int userID) {
		
		EntityPlayer p = getPlayer(userID);
		CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID),  new PacketUpdateLivingEntity(p.metadata, p.posX, p.posY, p.health, p.satisfaction, p.energy, p.serverID));

	}
	
	public static void updateLivingEntity(EntityAlive entity) {
		
		broadcastPacket(new PacketUpdateLivingEntity(entity));
		
	}
	
	public static void sendMessage(int userID, String message, Color color) {
		
		CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), new PacketChat(new ChatMessage("Server", message, color, 0)));
		
	}
	
	public static void broadcastMessage(int userID, String message, Color color) {
		
		 broadcastPacket(new PacketChat(new ChatMessage("Server", message, color, 0)));
		
	}
	
	public static EntityPlayer getPlayerForUsername(String username) {
		
		for (int x = 0; x < players.size(); x ++) {
			
			EntityPlayer p = (EntityPlayer) players.values().toArray()[x];
			
			if (p.username.equalsIgnoreCase(username)) {
				
				return p;
				
			}
			
		}
		
		return null;
		
	}
	
	public static void loadPlayer(int userID) {
		
		Toolkit k = new Toolkit();

		Float[] playerData = (Float[]) k.load("world/players/" + userID + ".dat");
		playersData.put(userID, playerData);

		if (new FileItem("world/inventories/" + userID + ".inv").exists()) {

			@SuppressWarnings("unchecked")
			ArrayList<Integer[]> inv = (ArrayList<Integer[]>) k.load("world/inventories/" + userID + ".inv");
			ConcurrentHashMap<CoordinateKey, InventoryContent> hotbar = new ConcurrentHashMap<CoordinateKey, InventoryContent>();
			ConcurrentHashMap<CoordinateKey, InventoryContent> inventoryLeft = new ConcurrentHashMap<CoordinateKey, InventoryContent>();
			ConcurrentHashMap<CoordinateKey, InventoryContent> inventoryRight = new ConcurrentHashMap<CoordinateKey, InventoryContent>();

			for (int x = 0; x < inv.size(); x ++) {

				Integer[] c = inv.get(x);
				Item item = new Item(c[2]);
				ItemStack stack = new ItemStack(item, c[4]);
				stack.metadata = c[3];
				if (c[5] == 0)
					hotbar.put(new CoordinateKey(c[0], c[1]), new InventoryContent(c[0], c[1], stack));
				else if (c[5] == 1)
					inventoryLeft.put(new CoordinateKey(c[0], c[1]), new InventoryContent(c[0], c[1], stack));
				else if (c[5] == 2)
					inventoryRight.put(new CoordinateKey(c[0], c[1]), new InventoryContent(c[0], c[1], stack));

			}

			inventories.put(userID, new PlayerInventory(userID, hotbar, inventoryLeft, inventoryRight));

		} else {

			inventories.put(userID, new PlayerInventory(userID));

		}
	}
	
	public static void save() {

		Toolkit k = new Toolkit();

		PixelLogger.print("Saving Player Data...", PixelColor.PURPLE);

		for (int userID : playersData.keySet()) {

			Float[] playerData = playersData.get(userID);
			k.save("world/players/" + userID + ".dat", playerData);

			PlayerInventory i = inventories.get(userID);
			ArrayList<Integer[]> inv = new ArrayList<Integer[]>();

			for (InventoryContent c : i.hotbar.content.values()) {

				inv.add(new Integer[]{c.x, c.y, c.itemstack.item.id, c.itemstack.metadata, c.itemstack.size, 0});

			}

			for (InventoryContent c : i.inventoryLeft.content.values()) {

				inv.add(new Integer[]{c.x, c.y, c.itemstack.item.id, c.itemstack.metadata, c.itemstack.size, 1});

			}

			for (InventoryContent c : i.inventoryRight.content.values()) {

				inv.add(new Integer[]{c.x, c.y, c.itemstack.item.id, c.itemstack.metadata, c.itemstack.size, 2});

			}

			k.save("world/inventories/" + userID + ".inv", inv);

		}

		PixelLogger.print("Player Data Saved!", PixelColor.PURPLE);

	}

	public static void tick(WorldServer w) {

		for (int i : players.values()) {

			Entity e =  WorldServer.entities.get(i);

			if (e.id == 3) {

				EntityPlayer p = (EntityPlayer) e;
				p.tick(w);

				Float[] playerData = new Float[]{p.posX, p.posY, p.health, p.satisfaction, p.energy, p.worldID + 0.0F};
				playersData.put(p.userID, playerData);

			}

		}

	}
	
	public static void sendPacketToPlayer(int userID, Packet packet) {
		
		CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), packet);
		
	}
	
	public static void broadcastPacket(Packet packet) {

		for (int i : players.keySet()) {

			if (playerLoaded(i)) {
				CommunicationServlet servlet = CommunicationServer.userConnections.get(i);
				CommunicationServlet.addPacket(servlet, packet);
			}

		}	

	}

	private static boolean playerLoaded(int i) {

		if (WorldServer.getEntity(players.get(i)).id == 3 && playersLoginStage.get(i) == 420) 
			return true;
		else
			return false;
	}

	public static void broadcastPacketExcludingPlayer(Packet packet, int userID) {

		for (int i : players.values()) {

			EntityPlayer p = (EntityPlayer) WorldServer.entities.get(i);
			
			if (p.userID != userID) {

				CommunicationServlet servlet = CommunicationServer.userConnections.get(p.userID);
				CommunicationServlet.addPacket(servlet, packet);

			}

		}	
		
	}
	
	public static void broadcastPacketUpdatePlayer(PacketUpdateLivingEntity packet) {

		for (int x = 0; x < players.size(); x ++) {

			if ((Integer)(players.keySet().toArray()[x]) != packet.userID) {
				CommunicationServlet servlet = CommunicationServer.userConnections.get(players.keySet().toArray()[x]);
				CommunicationServlet.addPacket(servlet, packet);
			}

		}	
		
	}
	
	public static void broadcastEntity(EntityAlive entity) {

		for (int i : players.values()) {

			EntityPlayer player = (EntityPlayer) WorldServer.entities.get(i);
			
			if (player.loaded) {
				CommunicationServlet servlet = CommunicationServer.userConnections.get(player.userID);
				CommunicationServlet.addPacket(servlet, new PacketUpdateLivingEntity(entity));
			}

		}	

	}

	public static String getUsername(int userID) {

		EntityPlayer p = getPlayer(userID);
		
		if (p != null) {
			
			return p.username;
			
		} else {
			
			return "servlet";
			
		}

	}

	public static void broadcastPacketToWorld(int worldID, PacketUpdateInteriorPiece packet) {

		for (int i : players.values()) {

			EntityPlayer player = (EntityPlayer) WorldServer.entities.get(i);
			
			if (player.worldID == worldID && player.loaded) {
				CommunicationServlet servlet = CommunicationServer.userConnections.get(player.userID);
				CommunicationServlet.addPacket(servlet, packet);
			}

		}	
		
	}

	public static int getDataX(int userID) {

		return Math.round(playersData.get(userID)[0]);
		
	}
	
	public static int getDataY(int userID) {

		return Math.round(playersData.get(userID)[1]);
		
	}

}
