package com.pixel.player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.pixel.admin.PixelLogger;
import com.pixel.communication.CommunicationServlet;
import com.pixel.communication.packet.PacketLogin;
import com.pixel.communication.packet.PacketLogout;

public class PlayerLoadThread implements Runnable {

	private int userID;
	private int session;
	private String username;
	private PacketLogin packet;
	
	public PlayerLoadThread(PacketLogin packet) {

		this.userID = packet.userID;
		this.username = packet.username;
		this.session = packet.session;
		this.packet = packet;
		

	}

	public void run() {

		if (PlayerManager.getPlayer(userID) != null) {

			CommunicationServlet.addPacket(packet.servletLogin, new PacketLogout(packet.userID));
			return;
		}

		Connection connection = null;
		Statement playerStatement = null;
		Statement playerSession = null;
		ResultSet playerResult = null;
		ResultSet sessionResult = null;
		String player;

		//load jdbc driver for mysql database
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch(Exception e) {
			PixelLogger.err("Unable to load Driver");
		}

		try {
			connection = DriverManager.getConnection("jdbc:mysql://pixel-realms.com/pixel_users", "pixel_admin", "wareVoid123P");
			if(connection != null) {
				
						playerStatement = connection.createStatement();

						try {

							playerResult = playerStatement.executeQuery("SELECT * FROM `" + this.username + "` WHERE type='id'");

							playerSession = connection.createStatement();
							sessionResult = playerSession.executeQuery("SELECT * FROM `" + this.username + "` WHERE type='current_session'");

						} catch (SQLException e) {

							e.printStackTrace();
							PixelLogger.err("Unable to get result set 1");

						}

						player = username;
						username = "";
						
						if(playerResult != null) {
							
							while(playerResult.next()) {

								if (Integer.parseInt(playerResult.getString(2)) == userID) {
									while(sessionResult.next()) {

										if (Integer.parseInt(sessionResult.getString(2)) == session) {
											username = player;
											break;
											
										}
									}
									break;
									
								}
							}
							
							
						}	
						if (!username.equals(player) || username.equals("")) {
							
							CommunicationServlet.addPacket(packet.servletLogin, new PacketLogout(packet.userID));
							
							return;
							
						}
						
					}
				try {
					connection.close();
				} catch (SQLException e) {
				     e.printStackTrace();
				}
		} catch (SQLException e) {
			e.printStackTrace();
			PixelLogger.err("Unable to connect to database");
		}

		if (username != null) {

			packet.username = username;
			PlayerManager.playerLogin(packet);
			
		}

	}
	
}
