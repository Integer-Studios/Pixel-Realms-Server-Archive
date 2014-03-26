package com.pixel.lighting;

public class PixelLight {

	public PixelLightType type;
	public float posX, posY;
	public int width, height, id;
	
	public PixelLight(float x, float y, int width, int height, PixelLightType type) {
		
		this.posX = x;
		this.posY = y;
		this.width = width;
		this.height = height;
		this.type = type;
		
		PixelLightingManager.propagateLight(this);
		
	}
	
	public PixelLight(float x, float y, int width, int height, PixelLightType type, int proposalID) {
		
		this.posX = x;
		this.posY = y;
		this.width = width;
		this.height = height;
		this.type = type;
		
		PixelLightingManager.propagateLight(this, proposalID);
		
	}

	
}
