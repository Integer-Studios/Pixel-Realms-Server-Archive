package com.pixel.piece;

import com.pixel.building.Building;

public class PieceBuilding extends Piece {

	public Building building;
	
	public PieceBuilding(int x, int y, int i) {
		super(x, y, 17, true);
		this.building = new Building(x, y, i);
		
	}
	
	public PieceBuilding(int x, int y, int i, int damage, int metadata, int worldID) {
		super(x, y, 17, true);
		
		this.building = new Building(x, y, i, worldID);
		
	}

}
