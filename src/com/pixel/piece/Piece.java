package com.pixel.piece;

import com.badlogic.gdx.math.Rectangle;
import com.pixel.communication.packet.PacketUpdatePiece;
import com.pixel.entity.EntityPlayer;
import com.pixel.item.Item;
import com.pixel.item.ItemStack;
import com.pixel.player.PlayerManager;
import com.pixel.world.WorldServer;

public class Piece {
	
	public Piece(int x, int y, int i, boolean propagate) {
		id = i;
		posX = x;
		posY = y;
		damage = info[id].maxDamage;
		info[id].onCreated(this);
		collisionBox = new Rectangle(posX + info[id].xOffset, posY + info[id].yOffset, info[id].width, info[id].height);
	
		if (propagate)
			WorldServer.propagatePiece(this);
	
	}
	
	public Piece(int i, int x, int y, int damage, int metadata, boolean propagate) {
		id = i;
		posX = x;
		posY = y;
		this.damage = info[i].maxDamage;
		this.metadata = metadata;
		info[id].onCreated(this);
		collisionBox = new Rectangle(posX + info[id].xOffset, posY + info[id].yOffset, info[id].width, info[id].height);
	
		if (propagate)
			WorldServer.propagatePiece(this);
	
	}
	
	public void tick(WorldServer w) {
		info[id].tick(w, this);
	}
	
	public void damage(int i, WorldServer w, EntityPlayer player) {
		info[id].onPlayerHitting(w, this, player);
		damage -= i;
		if (damage < 0)
			damage = 0;
		if (damage == 0) {
			info[id].onDestroyed(w, this, player);
			Piece p = new Piece(posX, posY, info[id].dropPiece, true);
			p.playerInCollidedPosition = true;
			WorldServer.setPieceObject(posX, posY, p);

			PlayerManager.broadcastPacket(new PacketUpdatePiece(p));

		}
	}
	
	public Rectangle getCollisionBox() {
		return collisionBox;
	}
	
	static {
		info = new PieceInfo[]{
				new PieceBlank().setIsCollectable(false),//0
				new PieceInfo().setMaxDamage(10).setIsCollectable(false),//1
				new PieceInfo().setMaxDamage(10).setIsCollectable(false),//2
				new PieceInfo().setDropItemStack(new ItemStack(Item.flowerPurple, 1)),//3
				new PieceInfo().setShouldCollide(true).setDropItemStack(new ItemStack(Item.rock, 1)),//4
				new PieceTree(7).setShouldCollide(true).setMaxDamage(100).setDropPiece(6),//5
				new PieceDoubleHeight().setShouldCollide(true).setMaxDamage(120).setDropItemStack(new ItemStack(Item.stumpPine, 1)),//6
				new PieceDoubleWidth().setShouldCollide(true).setDropPiece(8).setDropItemStack(new ItemStack(Item.branchPine, 3)),//7
				new PieceDoubleWidth().setShouldCollide(true).setMaxDamage(20).setDropItemStack(new ItemStack(Item.logPine, 3)),//8
				new PieceInfo().setShouldCollide(true).setSize(0.0F, 0.7F, 0.9F, 0.2F).setDropItemStack(new ItemStack(Item.rock, 2)).setMaxDamage(50),//9
				new PieceTree(12).setShouldCollide(true).setMaxDamage(100).setDropPiece(11),//10
				new PieceDoubleHeight().setShouldCollide(true).setMaxDamage(120).setDropItemStack(new ItemStack(Item.stumpApple, 1)),//11
				new PieceDoubleWidth().setShouldCollide(true).setDropPiece(13).setDropItemStack(new ItemStack(Item.branchApple, 3)),//12
				new PieceDoubleWidth().setShouldCollide(true).setMaxDamage(20).setDropItemStack(new ItemStack(Item.logApple, 3)),//13
				new PieceCabinBasic().setShouldCollide(true),//14
				new PieceMultiItem(new ItemStack[] {new ItemStack(Item.bunnyFoot, 1), new ItemStack(Item.bunnyLeg, 1), new ItemStack(Item.bunnyFur, 1)}),//15
				new PieceTripleHeight().setShouldCollide(true).setMaxDamage(-1).setIsCollectable(false),//16
				new PieceBuildingInfo(),//17
				new PieceInfo().setIsCollectable(false).setShouldCollide(true).setSize(0F, 0F, 1F, 1F),//18
				new PieceInfo().setIsCollectable(false).setShouldCollide(true).setSize(0F, 0F, 1F, 1F),//19
				new PieceInfo().setIsCollectable(false).setShouldCollide(true).setSize(0F, 0F, 1F, 1F),//20
				new PieceInfo().setIsCollectable(false).setShouldCollide(true).setSize(0F, 0F, 1F, 1F),//21
				new PieceInfo().setIsCollectable(false).setShouldCollide(true).setSize(0F, 0F, 1F, 1F),//22
				new PieceInfo().setIsCollectable(false).setShouldCollide(true).setSize(0F, 0F, 1F, 1F),//23
				new PieceInfo().setIsCollectable(false).setShouldCollide(true).setSize(0F, 0F, 1F, 1F),//24
				new PieceInfo().setIsCollectable(false).setShouldCollide(true).setSize(0F, 0F, 1F, 1F),//25


		};
		
	}
	
//	public void paintComponent(Graphics g) {
//    BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//    Graphics2D gbi = result.createGraphics();
//    BufferedImage x = null;
//    try {
//        x = ImageIO.read(getClass().getResource("/resources/someimage.png"));
//    } catch (IOException ex) {
//        Logger.getLogger(CanvasPanel.class.getName()).log(Level.SEVERE, null, ex);
//    }
//    gbi.drawImage(x, 0, 0, this);
//    gbi.setColor(selectedColor);
//    gbi.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.75f));
//    gbi.fillRect(0, 0, width, height);
//    g.drawImage(result, 0, 0, this);
//}
	//color overlay for selecting
	
	public int id;
	public int metadata;
	public int posX;
	public int posY;
	public int damage = 10;
	public boolean playerInCollidedPosition;
	public Rectangle collisionBox;
	public static PieceInfo[] info;
	
}
