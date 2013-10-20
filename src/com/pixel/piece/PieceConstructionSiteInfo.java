package com.pixel.piece;

import com.pixel.interior.BuildingInfo;

public class PieceConstructionSiteInfo extends PieceInfo {

	public int buildingID;
	public BuildingInfo building;

	public PieceConstructionSiteInfo(int buildingID) {
		super();
		this.buildingID = buildingID;
		this.maxDamage = 1000;

		// TODO Auto-generated constructor stub
	}
	
	public void onCreated(Piece p) {
		
		super.onCreated(p);

	}

}