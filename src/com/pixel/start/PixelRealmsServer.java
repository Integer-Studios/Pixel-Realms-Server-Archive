package com.pixel.start;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.pixel.admin.CommandLine;
import com.pixel.communication.CommunicationServer;
import com.pixel.world.WorldServer;

public class PixelRealmsServer extends Thread implements ActionListener{

	public static WorldServer world;
	public GameServer game;
	
	public static void main(String[] args) {

		new PixelRealmsServer().start();
		
	}
	
	public void run () {
		
		CommunicationServer server = new CommunicationServer();
		new Thread(server).start();
		
		CommandLine commandLine = new CommandLine();
		commandLine.start();
		
		world = new WorldServer();
		
		game = new GameServer(world);
		game.start();
		
	
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		world.tick();

	}

}
