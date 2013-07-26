package com.pixel.piece;

import com.pixel.entity.EntityPlayer;
import com.pixel.world.WorldServer;

public class PieceTree extends PieceDoubleHeight {

	public PieceTree(int d) {
		super();
		treeDown = d;
	}
	
	public void onDestroyed(WorldServer w, Piece p, EntityPlayer pl) {
		WorldServer.setPiece(p.posX+1, p.posY, treeDown);
		super.onDestroyed(w, p, pl);
	}
	
	public int treeDown;

}
