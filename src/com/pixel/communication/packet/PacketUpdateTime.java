package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

import com.pixel.world.WorldServer;

public class PacketUpdateTime extends Packet {

	public long time;
	public long timeBetween;
	public long timestamp;
	public WorldServer w;
	
	public PacketUpdateTime() {}
	
	public PacketUpdateTime(WorldServer w, long timeBetween) {
		
		this.id = 23;
		this.w = w;
		this.timeBetween = timeBetween;
		
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		// TODO Auto-generated method stub
		this.time = w.getTime();
		java.util.Date date= new java.util.Date();
		this.timestamp = new Timestamp(date.getTime()).getTime();
		
		output.writeLong(time);
		output.writeLong(timeBetween);
		output.writeLong(timestamp);

	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		// TODO Auto-generated method stub

	}

}
