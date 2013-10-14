package com.pixel.piece;

public class PieceBuildingInfo extends PieceInfo {

	int buildingID;
	
	public PieceBuildingInfo(int buildingID) {
		
		setSize(0F, -1.2F, 4F, 2.2F);
		this.buildingID = buildingID;
		this.maxDamage = 1000;
		
	}
	
	public void onCreated(Piece p) {

	}

}
