package com.pixel.entity;

public class EntityMonster extends EntityAlive{

	public EntityMonster(float x, float y, float width, float height, boolean propagate) {
		super(x, y, width, height, propagate);
		speed = .03F;
	}
	
	public EntityMonster(float x, float y, float width, float height) {
		super(x, y, width, height, true);
		speed = .03F;
	}

}
