package com.pixel.entity;


import com.pixel.player.PlayerManager;
import com.pixel.world.WorldServer;

public class EntityAnimal extends EntityAlive {

	public boolean alpha = false;
	public int xDestination, yDestination;
	private double speed = .03;
	
	public EntityAnimal(float x, float y, float width, float height, boolean propagate) {
		super(x, y, width, height, propagate);
		
		this.xDestination = (int) x;
		this.yDestination = (int) y;
	}
	
	public EntityAnimal(float x, float y, float width, float height) {
		super(x, y, width, height, true);
		
		this.xDestination = (int) x;
		this.yDestination = (int) y;
	}

	public void tick(WorldServer w) {
		super.tick(w);
		
//		if (Math.abs(xDestination - posX) > 20 || Math.abs(yDestination - posY) > 20) {
//			
//			this.xDestination = (int) posX;
//			this.yDestination = (int) posY;
//		}
//		
//		if (yDestination < 0) {
//			
//			yDestination = 0;
//			
//		} 
//		
//		if (xDestination < 0) {
//			
//			xDestination = 0;
//			
//		}
//
//		Random r = new Random();
//		
//		if ((int) yDestination != (int) posY && (int) xDestination != (int) posX) {
//			
//			if (r.nextInt(100) >= 95) {
//								
//				int xChange = r.nextInt(5);
//				int yChange = r.nextInt(5);
//				
//				if (r.nextBoolean()) 
//					yDestination += yChange;
//				else
//					yDestination -= yChange;
//
//				
//				if (r.nextBoolean()) 
//					xDestination += xChange;
//				else
//					xDestination -= xChange;
//				
//			} else {
//				
				if (yDestination > (int) posY) {
					
					posY += speed;
					
				} else if (yDestination < (int) posY) {
					
					posY -= speed;
					
				}
				
				if (xDestination > (int)posX) {
					
					posX += speed;
					
				} else if (xDestination < (int)posX) {
					
					posX -= speed;
					
				}
				updated = false;
				
//else {
//			
//			
//			if (r.nextInt(1000) >= 950) {
//				
//				int xChange = r.nextInt(5);
//				int yChange = r.nextInt(5);
//				
//				if (r.nextBoolean()) {
//				
//					yDestination += yChange;
//					
//				} else {
//				
//					yDestination -= yChange;
//				
//				}
//					
//				
//				if (r.nextBoolean()) {
//					xDestination += xChange;
//					
//				} else {
//				
//					xDestination -= xChange;
//				
//				}
//				
//			} 
//			
//		}

	}
	
	public void kill(WorldServer w, Entity damageSource) {
		spawnBody(w);
		health = 0;
		PlayerManager.updateLivingEntity(this);
	}
	
}
