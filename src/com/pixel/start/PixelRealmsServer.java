package com.pixel.start;

import com.pixel.admin.CommandLine;
import com.pixel.communication.CommunicationServer;
import com.pixel.util.TextFile;
import com.pixel.world.WorldServer;

public class PixelRealmsServer extends Thread {

	public static WorldServer world;
	public GameServer game;
	public static int port;
	
	public static void main(String[] args) {

		new PixelRealmsServer().start();
		
	}
	
	public void run () {
		
		this.loadConfiguration();
		
		CommunicationServer server = new CommunicationServer(port);
		new Thread(server).start();
		
		CommandLine commandLine = new CommandLine();
		commandLine.start();
		
		world = new WorldServer();
		
		game = new GameServer(world);
		game.start();
		
	}
	
	public void loadConfiguration() {
		
		TextFile configuration = new TextFile("configuration.pxl");
		
		for (int x = 1; x <= configuration.lineCount(); x ++) {
			
			String line = configuration.readLine(x);
			
			if (line.startsWith("port: ")) {
				
				line = line.replaceFirst("port: ", "");
				port = Integer.parseInt(line);
				
			}
			
		}
			
	}

}
