package com.pixel.piece;

import java.util.concurrent.ConcurrentHashMap;

import com.pixel.entity.EntityPlayer;
import com.pixel.item.ItemStack;
import com.pixel.util.CoordinateKey;
import com.pixel.world.WorldServer;

public class PieceSac extends PieceInfo {
	
	public void onDestroyed(WorldServer w, Piece p, EntityPlayer player) {
		System.out.println("neg");
		ItemStack[] is = sacContents.get(new CoordinateKey(p.posX, p.posY));
		if (is != null) {
			for (int i = 0; i < is.length; i++) {
				player.giveItem(is[i]);
			}
		}
		sacContents.remove(new CoordinateKey(p.posX, p.posY));
	}
	
	public static ConcurrentHashMap<CoordinateKey, ItemStack[]> sacContents = new ConcurrentHashMap<CoordinateKey, ItemStack[]>();

}
