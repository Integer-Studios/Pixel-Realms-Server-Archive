package com.pixel.chat;

import java.awt.Color;

import com.pixel.player.PlayerManager;

public class ChatMessage {

	public String username;
	public int userID;
	public Color color;
	public String text;
	public int timeShown;
	
	public ChatMessage(String username, String text, Color color, int userID) { 
		
		this.username = username;
		this.text = text;
		this.color = color;
		this.userID = userID;
		
	}
	
	public ChatMessage(String text, int userID) { 
		
		this.text = text;
		this.userID = userID;
		this.username = PlayerManager.getPlayer(userID).username;
		this.color = Color.WHITE;
		
	}
	
}
