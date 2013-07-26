package com.pixel.piece;

import com.pixel.entity.Entity;
import com.pixel.entity.EntityPlayer;
import com.pixel.item.ItemStack;
import com.pixel.world.WorldServer;

public class PieceInfo {
	
	public PieceInfo() {
	}
	
	public void tick(WorldServer w, Piece p) {

	}

	public void onCreated(Piece p) {
		
	}
	
	public void onEntityCollided(WorldServer w, Piece p, Entity entity) {

	}
	
	public void onPlayerCollided(WorldServer w, Piece p, EntityPlayer player) {

	}
	
	public void onPlayerHitting(WorldServer w, Piece p, EntityPlayer player) {

	}
	
	public void onDestroyed(WorldServer w, Piece p, EntityPlayer player) {
		if (dropItemStack != null)
		player.giveItem(dropItemStack);
	}
	
	public void setPlayerInInteractionZone(boolean b) {
		playerInInteractionZone = b;
	}
	
	public boolean getPlayerInInteractionZone() {
		return playerInInteractionZone;
	}

	public PieceInfo setShouldCollide(boolean b) {
		shouldCollide = b;
		return this;
	}

	
	public PieceInfo setSize(float x, float y, float w, float h) {
		xOffset = x;
		yOffset = y;
		width = w;
		height = h;
		return this;
	}
	
	public PieceInfo setDropPiece(int p) {
		dropPiece = p;
		return this;
	}
	
	public PieceInfo setDropItemStack(ItemStack i) {
		dropItemStack = i;
		return this;
	}
	
	public ItemStack getDropItemStack() {
		return dropItemStack;
	}
	
	public PieceInfo setMaxDamage(int i) {
		maxDamage = i;	
		return this;
	}
	
	public PieceInfo setIsCollectable(boolean b) {
		isCollectable = b;
		return this;
	}
	
	public boolean shouldCollide, isCollectable = true;;
	public boolean playerInInteractionZone;
	public int dropPiece;
	public float width = 0.8F, height = 0.15F, xOffset = 0.1F, yOffset = 0.85F;
	public int maxDamage = 1;
	public ItemStack dropItemStack = null;
}
