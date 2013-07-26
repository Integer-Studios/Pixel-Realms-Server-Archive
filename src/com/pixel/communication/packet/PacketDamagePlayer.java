package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.pixel.player.PlayerManager;

public class PacketDamagePlayer extends Packet {

	float damage;
	int targetID;

	public PacketDamagePlayer() {
		
		this.id = 13;
		
	}
	
	@Override
	public void writeData(DataOutputStream output) throws IOException {

	}

	@Override
	public void readData(DataInputStream input) throws IOException {

		targetID = input.readInt();
		damage = input.readFloat();
		PlayerManager.getPlayer(targetID).damage(damage);
		
	}

}
