package com.pixel.piece;

import com.pixel.entity.EntityPlayer;
import com.pixel.item.ItemStack;
import com.pixel.world.WorldServer;

public class PieceMultiItem extends PieceInfo {

	public PieceMultiItem(ItemStack[] i) {
		super();
		dropItems = i;
	}
	
	public PieceInfo setDropItemStacks(ItemStack[] i) {
		dropItems = i;
		return this;
	}
	
	public ItemStack[] getDropItemStacks() {
		return dropItems;
	}
	
	public void onDestroyed(WorldServer w, Piece p, EntityPlayer player) {
		for (int i = 0; i < dropItems.length; i++) {
			dropItemStack = dropItems[i];
			if (dropItemStack != null) {
				player.giveItem(dropItemStack);
			}
		}
	}
	
	public ItemStack[] dropItems;

}
