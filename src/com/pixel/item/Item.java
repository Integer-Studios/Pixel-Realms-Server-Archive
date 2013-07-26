package com.pixel.item;

import com.pixel.util.Toolkit;

public class Item {
	
	public Item(int i) {
		id = i;
		items[id] = this;
	}
	
	public String getTexture() {
		return texture;
	}
	
	public static Item getItemForId(int id) {
		return items[id];
	}
	
	public static Item[] items = new Item[420];

	public static Item blank = new Item(0);
	public static Item sac = new Item(1);
	public static Item flowerPurple = new Item(2);
	public static Item logPine = new Item(3);
	public static Item logApple = new Item(4);
	public static Item stumpPine = new Item(5);
	public static Item stumpApple = new Item(6);
	public static Item branchPine = new Item(7);
	public static Item branchApple = new Item(8);
	public static Item rock = new Item(9);
	public static Item bunnyFoot = new Item(10);
	public static Item bunnyFur = new Item(11);
	public static Item bunnyLeg = new Item(12);
	public static Item grass = new Item(13);
	public static Item testSword = new Item(14);
	public static Item testAxe = new Item(15);
	public static Item testPick = new Item(16);
	public static Item ragaStaff = new Item(17);
	public static Item littleChapsStoreManagementDevice = new Item(18);
	public static Item goldSword = new Item(19);
	public static Item baneOfEmera = new Item(20);
	public static Item sharpenedRock = new Item(21);
	public static Item stick = new Item(22);

	public int id;
	public String texture;
	public Toolkit t = new Toolkit();

}
