package com.pixel.entity.ai;

import java.util.Random;

import com.pixel.entity.EntityAnimal;
import com.pixel.entity.EntityBunny;
import com.pixel.world.WorldServer;

public class HerdBunny extends Herd {
	
	public HerdBunny(){}

	public HerdBunny(float x, float y) {
		
		super(x, y);
		
		this.herdID = 1;
		
		int number = 5 + (int)(Math.random() * ((8 - 5) + 1));

		alpha = new EntityBunny(x, y, this);
		propagateAlpha(alpha);
		
		for (int i = 0; i < number; i ++) {
			
			float newX = x + (-3 + (int)(Math.random() * ((3 + 3) + 1)));
			float newY = y + (-3 + (int)(Math.random() * ((3 + 3) + 1)));

			newX = checkPosition(newX);
			newY = checkPosition(newY);
			
			this.propagateEntity(new EntityBunny(newX, newY, this));
			
		}
		
		
	}
	
	public void tick(WorldServer w) {
		
		super.tick(w);
		
		int alphaInt = 0 + (int)(Math.random() * ((300 - 0) + 1));
		int move = 0 + (int)(Math.random() * ((1000 - 0) + 1));
		alpha.tick(w);
		
		if ((((int)alpha.getX() == alpha.xDestination && (int)alpha.getY() == alpha.yDestination) || !moving) && alphaInt == 1)
			calculateAlphaTarget();

		if (move == 999 && timeSpent > 50000 && targetX == x && targetY == y) {
			
			if (new Random().nextBoolean()) {
				
				targetX = x + (20 + (int)(Math.random() * ((50 - 20) + 1)));
				
			} else {
				
				targetX = x - (20 + (int)(Math.random() * ((50 - 20) + 1)));
				
			}
			
			if (new Random().nextBoolean()) {
				
				targetY = y + (20 + (int)(Math.random() * ((50 - 20) + 1)));
				
			} else {
				
				targetY = y - (20 + (int)(Math.random() * ((50 - 20) + 1)));
				
			}
			
			//move herd's target

			targetX = checkPosition(targetX);
			targetY = checkPosition(targetY);
			calculateAlphaTarget();

		} else {

			for (EntityAnimal entity : entities.values()) {
				
				EntityBunny bunny = (EntityBunny) entity;

				bunny.tick(w);
				 try {
					    Thread.sleep(2);
					 } catch (Exception e){}
				 
				int change = 0 + (int)(Math.random() * ((100 - 0) + 1));

				if (((int) bunny.getX() == bunny.xDestination && (int) bunny.getY() == bunny.yDestination) && (change < 10 || moving)) {

					int r = 10;
					float newX = 0;
					float newY = 0;
					
					if (moving) {
						
						r = 10;
						newX = alpha.getX() + (-8 + (int)(Math.random() * ((8 + 8) + 1)));
						newY = alpha.getY() + (-8 + (int)(Math.random() * ((8 + 8) + 1)));

					} else {
						
						r = 20;
						newX = alpha.getX() + (-16 + (int)(Math.random() * ((16 + 16) + 1)));
						newY = alpha.getY() + (-16 + (int)(Math.random() * ((16 + 16) + 1)));

					}

					Float[] pos = checkPositionAgainstAlpha(newX, newY, r);
					
					if (pos != null) {

						newX = pos[0];
						newY = pos[1];

						bunny.xDestination = (int) newX;
						bunny.yDestination = (int) newY;

					}

				}
				

			}
			
		}
		
	}
	
}
