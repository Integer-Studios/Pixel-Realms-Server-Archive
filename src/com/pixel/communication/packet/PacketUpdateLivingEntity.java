package com.pixel.communication.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.pixel.entity.EntityAlive;
import com.pixel.entity.EntityPlayer;
import com.pixel.item.ItemStack;
import com.pixel.player.PlayerManager;
import com.pixel.util.Metadata;
import com.pixel.util.Metadata.MetadataObject;
import com.pixel.world.WorldServer;

public class PacketUpdateLivingEntity extends Packet {

	public Metadata metadata;
	public float posX, posY, health, satisfaction, energy;
	public int itemID, itemAmount, worldID, serverID, entityID;
	
	public PacketUpdateLivingEntity(EntityAlive player) {
		
		this(player.metadata, player.getX(), player.getY(), player.health, player.satisfaction, player.energy, player.serverID);
		
	}
	
	public PacketUpdateLivingEntity(Metadata metadata, float posX, float posY, float health, float satisfaction, float energy, int serverID) {
		// TODO Auto-generated constructor stub
		this.id = 7;
		this.serverID = serverID;
		this.metadata = metadata; 
		this.posX = posX;
		this.posY = posY;
		this.health = health;
		this.satisfaction = satisfaction;
		this.energy = energy;
		this.worldID = WorldServer.entities.get(serverID).worldID;
		this.entityID = WorldServer.entities.get(serverID).id;

	}
	
	public PacketUpdateLivingEntity(int serverID) {
		this((EntityAlive) WorldServer.entities.get(serverID));
		
	}

	@Override
	public void writeData(DataOutputStream output) {

		try {
			
			output.writeFloat(this.serverID);
			output.writeFloat(this.posX);
			output.writeFloat(this.posY);
			output.writeFloat(this.health);
			output.writeFloat(this.satisfaction);
			output.writeFloat(this.energy);
			output.writeInt(this.worldID);
			output.writeInt(this.entityID);

			output.writeInt(metadata.objects.size());
			for (MetadataObject o : metadata.objects.values()) {
				
				output.writeInt(o.type);
				switch (o.type) {
				
				case 0:
					output.writeInt(o.id);
					Packet.writeString(o.s, output);
					break;
				case 1:
					output.writeInt(o.id);
					output.writeInt(o.i);
					break;
				case 2:
					output.writeInt(o.id);
					output.writeFloat(o.f);
					break;
				case 3: 
					output.writeInt(o.id);
					output.writeBoolean(o.b);
					break;
				
				}
				
			}
			
			output.writeInt(this.worldID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void readData(DataInputStream input) {

		try {
			
			this.serverID = input.readInt();
			this.posX = input.readFloat();
			this.posY = input.readFloat();

			this.health = input.readFloat();
			this.satisfaction = input.readFloat();
			this.energy = input.readFloat();
			
			int amt = input.readInt();
			Metadata m = new Metadata();
			for (int x = 0; x < amt; x ++) {
				
				int type = input.readInt();
				
				switch (type) {
				
				case 0:
					int objID = input.readInt();
					String s = Packet.readString(16, input);
					m.addString(objID, s);
					break;
				case 1:
					int objID1 = input.readInt();
					int i = input.readInt();
					m.addInteger(objID1, i);
					break;
				case 2:
					int objID2 = input.readInt();
					float f = input.readFloat();
					m.addFloat(objID2, f);
					break;
				case 3: 
					int objID3 = input.readInt();
					boolean b = input.readBoolean();
					m.addBoolean(objID3, b);
					break;
				
				}
				
			}	
			
			if (m.objects.containsKey(3)) {
				
				this.itemID = m.objects.get(3).i;
				this.itemAmount = m.objects.get(4).i;
				
			}
			
			PacketHandler.processUpdateLivingEntity(this);
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
