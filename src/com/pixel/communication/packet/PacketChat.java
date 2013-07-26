package com.pixel.communication.packet;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.pixel.chat.ChatManager;
import com.pixel.chat.ChatMessage;

public class PacketChat extends Packet {

	String username;
	String message;
	Color color;
	
	public PacketChat() {
		
		this.id = 8;
		
	}
	
	public PacketChat(String username, String message, Color color) {
		
		this.id = 8;
		this.username = username;
		this.message = message;
		this.color = color;
		
	}
	
	public PacketChat(ChatMessage message) {
		
		this.id = 8;
		this.userID = message.userID;
		this.username = message.username;
		this.message =  message.text;
		this.color =  message.color;
		
	}
	
	@Override
	public void writeData(DataOutputStream output) throws IOException {

		Packet.writeString(username, output);
		Packet.writeString(message, output);

		int colorInt = 1;
		if (color == Color.WHITE) {

			colorInt = 1;

		}

		if (color == Color.BLACK) {

			colorInt = 2;

		}

		if (color == Color.RED) {

			colorInt = 3;

		}

		if (color == Color.BLUE) {

			colorInt = 4;

		}
		
		if (color == Color.GRAY) {

			colorInt = 5;

		}
		
		if (color == Color.GREEN) {

			colorInt = 6;

		}
		
		if (color == Color.YELLOW) {

			colorInt = 7;

		}
		
		output.writeInt(colorInt);
		
	}

	@Override
	public void readData(DataInputStream input) throws IOException {

		username = Packet.readString(16, input);
		message = Packet.readString(64, input);
		int colorInt = input.readInt();
		
		switch (colorInt) {

		case 1:
			color = Color.WHITE;
			break;
		case 2:
			color = Color.BLACK;
			break;
		case 3:
			color = Color.RED;
			break;
		case 4:
			color = Color.BLUE;
			break;
		case 5:
			color = Color.GRAY;
			break;
		case 6:
			color = Color.GREEN;
			break;
		case 7:
			color = Color.YELLOW;
			break;

		}
		
		ChatManager.processMessage(new ChatMessage(username, message, color, userID));
		
	}

}
