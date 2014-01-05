package com.pixel.entity;

import com.pixel.item.Item;
import com.pixel.item.ItemStack;

public class EntityPog extends EntityMonster {
	
	public EntityPog(float x, float y) {
		super(x, y, .9F, .2F);
		this.id = 2;
		setDrops(new ItemStack[]{new ItemStack(Item.goldSword, 1), new ItemStack(Item.ragaStaff, 1)});
	}
	
	public EntityPog() {
		super(0, 0, .9F, .2F, false);
		this.id = 2;
		setDrops(new ItemStack[]{new ItemStack(Item.goldSword, 1), new ItemStack(Item.ragaStaff, 1)});
	}

}
