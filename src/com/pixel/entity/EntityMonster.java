package com.pixel.entity;

import java.util.Random;

import com.pixel.world.WorldServer;

public class EntityMonster extends EntityAlive{

	public EntityMonster(float x, float y, float width, float height, boolean propagate) {
		super(x, y, width, height, propagate);
		speed = .03F;
	}
	
	public EntityMonster(float x, float y, float width, float height) {
		super(x, y, width, height, true);
		speed = .03F;
	}

	public void startRandomMovement(Random r) {
		System.out.println("starting random entity movement");
		switch (r.nextInt(7)) {
		case 0:
			velocityX = speed;
			velocityY = 0;
			break;
		case 1:
			velocityX = -1*speed;
			velocityY = 0;
			break;
		case 2:
			velocityX = 0;
			velocityY = speed;
			break;
		case 3:
			velocityX = 0;
			velocityY = -1*speed;
			break;
		case 4:
			velocityX = (float)Math.sqrt(2D)*speed;
			velocityY = (float)Math.sqrt(2D)*speed;
			break;
		case 5:
			velocityX = -1*(float)Math.sqrt(2D)*speed;
			velocityY = (float)Math.sqrt(2D)*speed;
			break;
		case 6:
			velocityX = (float)Math.sqrt(2D)*speed;
			velocityY = -1*(float)Math.sqrt(2D)*speed;
			break;
		case 7:
			velocityX = -1*(float)Math.sqrt(2D)*speed;
			velocityY = -1*(float)Math.sqrt(2D)*speed;
			break;
		default:
			System.out.println("random movement randomizer out of bounds");
			break;
		}
	}

	public void tick(WorldServer w) {
		
		Random r = new Random();
		
		if (velocityX == 0 && velocityY == 0 && r.nextInt(100) == 0) {
			startRandomMovement(r);
		} else
		if ((velocityX != 0 || velocityY != 0) && r.nextInt(100) == 0) {
			System.out.println("stopping entity movement");
			velocityX = 0;
			velocityY = 0;
		}

//		if (yDestination > (int) posY) {
//
//			posY += speed;
//
//		} else if (yDestination < (int) posY) {
//
//			posY -= speed;
//
//		}
//
//		if (xDestination > (int)posX) {
//
//			posX += speed;
//
//		} else if (xDestination < (int)posX) {
//
//			posX -= speed;
//
//		}
//		updated = false;
		
		super.tick(w);


	}
	
}
