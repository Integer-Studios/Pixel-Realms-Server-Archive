package com.pixel.chat;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.pixel.communication.packet.PacketChat;
import com.pixel.communication.packet.PacketUpdatePiece;
import com.pixel.communication.packet.PacketUpdatePlayer;
import com.pixel.entity.EntityBunny;
import com.pixel.entity.EntityPlayer;
import com.pixel.piece.Piece;
import com.pixel.piece.PieceBuilding;
import com.pixel.player.PlayerManager;
import com.pixel.start.PixelRealmsServer;

public class ChatManager {

	static String log;
	ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();
	
	public static void processMessage(ChatMessage msg) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		System.out.println("[Pixel Realms] " + dateFormat.format(date) + " " + msg.username + "(" + PlayerManager.getPlayer(msg.userID).posX + ", " + PlayerManager.getPlayer(msg.userID).posY + "): " + msg.text);
 		
		if (msg.text.startsWith("/")) {
			
			msg.text = msg.text.substring(1);
			if (msg.text.equalsIgnoreCase("save")) {
				
				PixelRealmsServer.world.save();
				
			} else if (msg.text.equalsIgnoreCase("spawn")) {
				
				new EntityBunny(PlayerManager.getPlayer(msg.userID).getX(), PlayerManager.getPlayer(msg.userID).getY());
				PlayerManager.sendMessage(msg.userID, "Spawning Bunny at your position!", Color.RED);
				
			} else if (msg.text.equalsIgnoreCase("locate") || msg.text.equalsIgnoreCase("loc")) {
				
				EntityPlayer p = PlayerManager.getPlayer(msg.userID);
				PlayerManager.sendMessage(msg.userID, "You are at: " + p.getX() + ", " + p.getY() + ".", Color.BLUE);

				
			} else if (msg.text.startsWith("tp ")) {
				
				String[] args = msg.text.split(" ");
				
				if (args.length == 3) {
					
					float x = Float.parseFloat(args[1]);
					float y = Float.parseFloat(args[2]);

					PlayerManager.getPlayer(msg.userID).setPosition(x, y);
					PlayerManager.sendMessage(msg.userID, "Teleported to " + x + ", " + y + "!" , Color.BLUE);
					PlayerManager.broadcastPacket(new PacketUpdatePlayer(msg.userID));

				} else if (args.length == 2) {
					
					EntityPlayer p = PlayerManager.getPlayerForUsername(args[1]);
					
					if (p != null) {
						
						PlayerManager.getPlayer(msg.userID).setPosition(p.getX(), p.getY());
						PlayerManager.sendMessage(msg.userID, "Teleported to " + args[1] + "!" , Color.BLUE);
						PlayerManager.sendMessage(p.userID, msg.username + " teleported to you!" , Color.BLUE);
						PlayerManager.broadcastPacket(new PacketUpdatePlayer(msg.userID));
						
					} else {
						
						PlayerManager.sendMessage(msg.userID, "There's no player by that name!", Color.RED);
						
					}
					
				}
				
			} else if (msg.text.equals("cabin")) {
				int x = ((int)PlayerManager.getPlayer(msg.userID).getX()) + 2;
				int y = ((int)PlayerManager.getPlayer(msg.userID).getY()) + 2;
				PlayerManager.broadcastPacket(new PacketUpdatePiece(new PieceBuilding(x, y, 0)));

				for (int b = x; b < x + 4; b ++) {
					
					for (int i = y; i < y + 4; i ++) {
						
						if (!(b == x && i == y))
							PlayerManager.broadcastPacket(new PacketUpdatePiece(new Piece(b, i, 0, true)));

					}
					
				}
			}
		
//			} else if (msg.text.startsWith("")) {
//				
//				String[] args = msg.text.split(" ");
//				
//				String target = "";
//				int id = 0;
//				int amount = 0;
//				
//				if (args.length >= 3) {
//					
//					//give me one item
//					target = args[1];
//					id = Integer.parseInt(args[2]);
//
//				}
//
//				if (args.length >= 4) {
//
//					//give me this many items
//					amount = Integer.parseInt(args[3]);
//
//				}
//
//				EntityPlayer p = PlayerManager.getPlayerForUsername(target);
//
//				if (p != null) {
//
//					try {
//						
//						p.getInventory().addItem(new Item(id), amount);
//						CommunicationServlet.addPacket(CommunicationServer.userConnections.get(msg.userID), new PacketChat(new ChatMessage("Server", "Giving " + target + " some " + id + ".", Color.RED, 0)));
//						CommunicationServlet.addPacket(CommunicationServer.userConnections.get(p.userID), new PacketChat(new ChatMessage("Server", "You just got some " + id + " from " + msg.username + "!", Color.RED, 0)));
//
//						
//					} catch (Exception e) {
//
//						CommunicationServlet.addPacket(CommunicationServer.userConnections.get(msg.userID), new PacketChat(new ChatMessage("Server", "The id or amount is invalid!", Color.RED, 0)));
//						
//					}
//					
//				} else {
//					
//					CommunicationServlet.addPacket(CommunicationServer.userConnections.get(msg.userID), new PacketChat(new ChatMessage("Server", "No user by that name.", Color.RED, 0)));
//					
//				}
//
//			}

		} else {
		
			PlayerManager.broadcastPacket(new PacketChat(msg));
		
		}
		
	}
	
}
