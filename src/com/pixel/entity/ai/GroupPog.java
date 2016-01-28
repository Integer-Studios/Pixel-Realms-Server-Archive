package com.pixel.entity.ai;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.admin.PixelLogger;
import com.pixel.entity.EntityPlayer;
import com.pixel.entity.EntityPog;
import com.pixel.interior.BuildingListener;
import com.pixel.interior.InteriorWorldManager;
import com.pixel.piece.Piece;
import com.pixel.player.PlayerManager;
import com.pixel.util.CollisionBox;
import com.pixel.world.WorldServer;

public class GroupPog extends Group {

	public int targetUser = -1;
	public int count = 5;
	
	public GroupPog(int x, int y) {
		super(2, 3);
		
		this.spawnX = x;
		this.spawnY = y;

	}

	public GroupPog spawn() {

//		for (int x = 0; x < spawnSize; x ++) {
//
//			EntityPog p = new EntityPog(spawnX + 0F, spawnY + 0F);
//			entitiesLiving.add(p.serverID);
//
//		}

		EntityPog p = new EntityPog(spawnX + 0F, spawnY + 0F);
		entitiesLiving.add(p.serverID);

		p = new EntityPog(spawnX + 2F, spawnY + 0F);
		entitiesLiving.add(p.serverID);

		p = new EntityPog(spawnX - 2F, spawnY + 0F);
		entitiesLiving.add(p.serverID);
		
		return this;

	}

	public void tick(WorldServer w) {
		super.tick(w);
		if (targetUser == -1) {
			int tempUser = -1, tempDistance = 30000;
			for (int i : PlayerManager.players.keySet()) {

				EntityPlayer p = PlayerManager.getPlayer(i);
				
				int x, y;

				if (BuildingListener.playerInside(p.userID)) {

					x = InteriorWorldManager.interiors.get(p.worldID).doorX;
					y = InteriorWorldManager.interiors.get(p.worldID).doorY;

				} else {

					x = Math.round(p.getX());
					y = Math.round(p.getY());

				}

				int dist = calculateDistance(x, y);
				PixelLogger.debug("Distance: ", dist, tempDistance);
				if (dist < tempDistance) {

					tempUser = p.userID;
					tempDistance = dist;

				}

			}

			if (tempUser != -1) {

				if (tempDistance <= 4) {
					
					PixelLogger.debug("New Target", tempDistance, tempUser);
					targetUser = tempUser;

				}

			}

		} else {
			
			count ++;

			if (count > 5) {

				count = 0;
				for (int a = 0; a < entitiesLiving.size(); a++){

					EntityPog p = (EntityPog) WorldServer.getEntity(entitiesLiving.get(a));

					try { 
						float x = PlayerManager.getPlayer(targetUser).getX();
						float y = PlayerManager.getPlayer(targetUser).getY();

						float diffX = x - p.posX;
						float diffY = y - p.posY;

						if (diffX <= 1 && diffY <= 1) {
							
							//Combat
							p.velocityX = 0;
							p.velocityY = 0;
							
							//Attack!

						} else {

							//Calculate the angle
							double angle = Math.atan2(diffY, diffX);

							//Update the positions
							p.velocityX = (float) (Math.cos(angle) * p.speed);
							p.velocityY = (float) (Math.sin(angle) * p.speed);
							
							Rectangle box = new Rectangle((p.getX() + p.velocityX) - (p.width/2), (p.getY() + p.velocityY) - (p.height/2), p.width, p.health);

							Piece b = CollisionBox.testPiecesAgainstCollisionBox(box, w);

							if (b != null) {
								
//								if (Piece.info[WorldServer.pieces[b].id].shouldCollide) {
									
//									float diffXOb = WorldServer.pieces[b].posX - p.posX;
//									float diffYOb = WorldServer.pieces[b].posY - p.posY;
//									Random r = new Random();
//									if (diffXOb < diffYOb) {
//										
//										if (r.nextBoolean())
//											angle = Math.atan2(diffY, diffX + 10);
//										else
//											angle = Math.atan2(diffY, diffX - 10);
//										
//									} else {
//										
//										if (r.nextBoolean())
//											angle = Math.atan2(diffY + 10, diffX);
//										else
//											angle = Math.atan2(diffY - 10, diffX);
//										
//									}
									

									//Update the positions
//									p.velocityX = (float) (Math.cos(angle) * p.speed);
//									p.velocityY = (float) (Math.sin(angle) * p.speed);
									
//								}
								
							}

						}

					} catch (NullPointerException e) {
						targetUser = -1; 
						return;
					}


				}

			}
			
		}

	}


}
