package com.pixel.entity;

import com.pixel.world.WorldServer;

public class EntityPog extends EntityMonster {
	
	public EntityPog(float x, float y) {
		super(x, y, .9F, .2F);
		this.id = 2;
	}
	
	public EntityPog() {
		super(0, 0, .9F, .2F, false);
		this.id = 2;
	}

}
