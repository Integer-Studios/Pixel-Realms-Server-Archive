package com.pixel.start;

import java.io.IOException;

import com.pixel.admin.CommandLine;
import com.pixel.communication.CommunicationInfoServer;
import com.pixel.communication.CommunicationServer;
import com.pixel.util.FileItem;
import com.pixel.util.TextFile;
import com.pixel.world.WorldServer;

public class PixelRealmsServer extends Thread {

	public static WorldServer world;
	public GameServer game;
	public static int port;
	public CommunicationInfoServer is;
	
	public static void main(String[] args) {

		new PixelRealmsServer().start();
		
	}
	
	public void run () {
		
		this.loadConfiguration();
		
		CommunicationServer server = new CommunicationServer(port);
		new Thread(server).start();
		is = new CommunicationInfoServer();
		new Thread(is).start();

		CommandLine commandLine = new CommandLine();
		commandLine.start();
		
		world = new WorldServer();
		
		game = new GameServer(world);
		game.start();
		
	}
	
	public void loadConfiguration() {

		if (new FileItem("configuration.pxl").exists()) {

			TextFile configuration = new TextFile("configuration.pxl");

			for (int x = 1; x <= configuration.lineCount(); x ++) {

				String line = configuration.readLine(x);

				if (line.startsWith("port: ")) {

					line = line.replaceFirst("port: ", "");
					port = Integer.parseInt(line);

				}

			}

		} else {
			
			new FileItem("configuration.pxl").create();
			
			new TextFile("configuration.pxl").write("port: 25565");
		
			port = 25565;
			
		}
		
		if (!new FileItem("world/").exists()) {
			
			try {
				new FileItem("world/").createNewDirectory();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			
	}

}
