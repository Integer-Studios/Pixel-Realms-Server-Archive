package com.pixel.piece;

import com.pixel.building.Building;

public class PieceBuilding extends Piece {

	public Building building;
	
	public PieceBuilding(int x, int y, int i) {
		super(x, y, 17);
		
		this.building = new Building(x, y, i);
		
	}

}
