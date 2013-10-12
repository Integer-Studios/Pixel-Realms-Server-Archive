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
import com.pixel.communication.packet.PacketLogin;
import com.pixel.communication.packet.PacketLogout;
import com.pixel.communication.packet.PacketUpdateLivingEntity;
import com.pixel.communication.packet.PacketUpdatePlayer;
import com.pixel.entity.EntityAlive;
import com.pixel.entity.EntityPlayer;
import com.pixel.inventory.InventoryContent;
import com.pixel.item.Item;
import com.pixel.item.ItemStack;
import com.pixel.start.PixelRealmsServer;
import com.pixel.util.CoordinateKey;
import com.pixel.util.FileItem;
import com.pixel.util.Toolkit;

public class PlayerManager {

	public static ConcurrentHashMap<Integer, EntityPlayer> players = new ConcurrentHashMap<Integer, EntityPlayer>();
	public static HashMap<Integer, PlayerInventory> inventories = new HashMap<Integer, PlayerInventory>();
	public static ConcurrentHashMap<Integer, Float[]> playersData = new ConcurrentHashMap<Integer, Float[]>();
	public static ConcurrentHashMap<Integer, Long> playersLoginTime = new ConcurrentHashMap<Integer, Long>();

	public static void playerLogin(PacketLogin packet) {
		
		CommunicationServlet.setUserID(packet.servletLogin, packet.userID);
		EntityPlayer player = new EntityPlayer(packet.username, packet.userID);
		player.setSession(packet.session);
		new StatisticsThread(packet.userID, packet.username, 0).start();

		if (!playersData.containsKey(player.userID)) {
			
			if (new FileItem("players/" + player.userID + ".dat").exists()) {
				
				loadPlayer(player.userID, player);
				
			} else {
				
				playersData.put(packet.userID, new Float[]{150F, 150F, 100F, 100F, 100F});
				inventories.put(packet.userID, new PlayerInventory(player.userID));
				
			}
			
		}
		else {
			
			inventories.get(packet.userID).sendInventory();
			
		}

		player.setPosX(playersData.get(packet.userID)[0]);
		player.setPosY(playersData.get(packet.userID)[1]);
		
		try {

			player.setHealth(playersData.get(packet.userID)[2]);
			player.setSatisfaction(playersData.get(packet.userID)[3]);
			player.setEnergy(playersData.get(packet.userID)[4]);

		} catch (ArrayIndexOutOfBoundsException e) {

			player.setHealth(100F);
			player.setSatisfaction(100F);
			player.setEnergy(100F);
			
		}
		PlayerManager.inventories.get(player.userID).sendInventory();

		player.velocityX = 0;
		player.velocityY = 0;
		players.put(player.userID, player);
		CommunicationServlet.addPacket(CommunicationServer.userConnections.get(packet.userID), new PacketUpdatePlayer(PlayerManager.getPlayer(packet.userID).username, PlayerManager.getPlayer(packet.userID).posX, PlayerManager.getPlayer(packet.userID).posY, PlayerManager.getPlayer(packet.userID).health, PlayerManager.getPlayer(packet.userID).satisfaction, PlayerManager.getPlayer(packet.userID).energy, packet.userID, PlayerManager.getPlayer(packet.userID).selectedItem));

		PixelLogger.print(player.username + " has logged in.", PixelColor.BLUE);
		sendPlayers(player.userID);
		broadcastPacket(packet);
		broadcastPacket(new PacketChat(new ChatMessage("Server", player.username + " has logged in.", Color.RED, player.userID)));
		
	}
	
	public static void sendPlayers(int userID) {
		
		for (EntityPlayer player: players.values()) {
			sendPacketToPlayer(userID, new PacketUpdatePlayer(PlayerManager.getPlayer(player.userID).username, PlayerManager.getPlayer(player.userID).posX, PlayerManager.getPlayer(player.userID).posY, PlayerManager.getPlayer(player.userID).health, PlayerManager.getPlayer(player.userID).satisfaction, PlayerManager.getPlayer(player.userID).energy, player.userID, PlayerManager.getPlayer(player.userID).selectedItem));
		
		}
		
	}
	
	public static void playerLogout(int userID) {
		
		EntityPlayer p = players.get(userID);

		new StatisticsThread(userID, p.username, 1).start();
		
		if (p != null) {

			Float[] playerData = new Float[]{p.posX, p.posY, p.health, p.satisfaction, p.energy};
			playersData.put(userID, playerData);

			players.remove(userID);
			PixelLogger.print(p.username + " has disconnected.", PixelColor.BLUE);
			PlayerManager.broadcastPacket(new PacketLogout(userID));
			broadcastPacket(new PacketChat(new ChatMessage("Server", p.username + " has disconnected.", Color.RED, p.userID)));

		}

	}

	public static EntityPlayer getPlayer(int userID) {

		return players.get(userID);
		
	}

	public static void updatePlayer(int userID) {
		
		EntityPlayer p = players.get(userID);
		CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID),  new PacketUpdatePlayer(p.username, p.posX, p.posY, p.health, p.satisfaction, p.energy, p.userID, p.selectedItem));

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
	
	public static void loadPlayer(int userID, EntityPlayer player) {
		
		Toolkit k = new Toolkit();

		Float[] playerData = (Float[]) k.load("players/" + userID + ".dat");
		playersData.put(userID, playerData);

		if (new FileItem("inventories/" + userID + ".inv").exists()) {

			@SuppressWarnings("unchecked")
			ArrayList<Integer[]> inv = (ArrayList<Integer[]>) k.load("inventories/" + userID + ".inv");
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

			inventories.put(userID, new PlayerInventory(player.userID));

		}
	}
	
	public static void save() {

		Toolkit k = new Toolkit();

		PixelLogger.print("Saving Player Data...", PixelColor.PURPLE);

		for (int userID : playersData.keySet()) {

			Float[] playerData = playersData.get(userID);
			k.save("players/" + userID + ".dat", playerData);

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

			k.save("inventories/" + userID + ".inv", inv);

		}

		PixelLogger.print("Player Data Saved!", PixelColor.PURPLE);

	}

	public static void tick() {
		
		for (EntityPlayer p : players.values()) {
			
			p.tick(PixelRealmsServer.world);
			Float[] playerData = new Float[]{p.posX, p.posY, p.health, p.satisfaction, p.energy};
			playersData.put(p.userID, playerData);
			
		}
		
	}
	
	public static void sendPacketToPlayer(int userID, Packet packet) {
		
		CommunicationServlet.addPacket(CommunicationServer.userConnections.get(userID), packet);
		
	}
	
	public static void broadcastPacket(Packet packet) {

		for (int x = 0; x < players.size(); x ++) {

			CommunicationServlet servlet = CommunicationServer.userConnections.get(players.keySet().toArray()[x]);
			CommunicationServlet.addPacket(servlet, packet);

		}	
		
	}
	
	public static void broadcastPacketUpdatePlayer(PacketUpdatePlayer packet) {

		for (int x = 0; x < players.size(); x ++) {

			if ((Integer)(players.keySet().toArray()[x]) != packet.userID) {
				CommunicationServlet servlet = CommunicationServer.userConnections.get(players.keySet().toArray()[x]);
				CommunicationServlet.addPacket(servlet, packet);
			}

		}	
		
	}
	
	public static void broadcastEntity(EntityAlive entity) {

		for (int x = 0; x < players.size(); x ++) {

			CommunicationServlet servlet = CommunicationServer.userConnections.get(players.keySet().toArray()[x]);
			CommunicationServlet.addPacket(servlet, new PacketUpdateLivingEntity(entity));

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
}
