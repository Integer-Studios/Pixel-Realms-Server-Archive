package com.pixel.entity;

public class EntityBunny extends EntityAnimal {
	
	public EntityBunny() {
		super(0, 0, .7F, .2F, false);
		this.id = 1;
		this.setMaxHealth(3.0F);
	}
	
	public EntityBunny(float x, float y) {
		super(x, y, .7F, .2F);
		this.id = 1;
		this.setMaxHealth(3.0F);
	}
	
//	public void tick(WorldServer w) {
////		for (int x = 0; x < WorldChunkManager.entitiesLoopArray.size(); x ++) {
////			CollisionBox.testEntitities(WorldChunkManager.entitiesLoopArray.get(x), this, w);
////		}
//		super.tick(w);
//	}
	
	
}
