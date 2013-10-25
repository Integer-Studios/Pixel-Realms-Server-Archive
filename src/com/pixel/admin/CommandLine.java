package com.pixel.admin;

import java.util.Scanner;

import java.awt.Color;
import com.pixel.chat.ChatMessage;
import com.pixel.communication.packet.PacketChat;
import com.pixel.communication.packet.PacketChangePiece;
import com.pixel.player.PlayerManager;
import com.pixel.start.PixelRealmsServer;
import com.pixel.world.WorldServer;

public class CommandLine extends Thread {
	
	public CommandLine() {
		
		
		
	}
	
	public void run() {
		
		while (true) {
			
			Scanner scanner = new Scanner(System.in);
			String command = scanner.nextLine();
			
			if (command.equalsIgnoreCase("/stop")) {
				
				PixelLogger.err("Server Stopping.");
				System.exit(0);
				
			} else if (command.equalsIgnoreCase("/list")) {
				
				for (int x = 0; x < PlayerManager.players.size(); x ++) {
					
//					System.out.println(PlayerManager.players.get(PlayerManager.players.keySet().toArray()[x]).username);
					
				}
				
			} else if (command.equalsIgnoreCase("/save")) {
				
				PixelRealmsServer.world.save();
				
			} else if (command.startsWith("/test")) {
				
				WorldServer.getPieceObject(Integer.parseInt(command.split(" ")[1]), Integer.parseInt(command.split(" ")[2])).metadata = Integer.parseInt(command.split(" ")[3]);
				PlayerManager.broadcastPacket(new PacketChangePiece(WorldServer.getPieceObject(Integer.parseInt(command.split(" ")[1]), Integer.parseInt(command.split(" ")[2]))));
				
			} else {
				
				PlayerManager.broadcastPacket(new PacketChat(new ChatMessage("Admin", command, Color.BLUE, 0)));
				
			} 
		}
		
	}
	
}
