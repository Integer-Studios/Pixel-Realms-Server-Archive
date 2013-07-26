package com.pixel.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.pixel.admin.PixelLogger;
import com.pixel.admin.PixelLogger.PixelColor;
import com.pixel.chat.ChatMessage;
import com.pixel.communication.packet.PacketChat;
import com.pixel.communication.packet.PacketUpdatePiece;
import com.pixel.communication.packet.PacketUpdateTile;
import com.pixel.entity.Entity;
import com.pixel.entity.EntityAnimal;
import com.pixel.entity.EntityPlayer;
import com.pixel.entity.ai.Herd;
import com.pixel.entity.ai.HerdBunny;
import com.pixel.piece.Piece;
import com.pixel.piece.PieceBuilding;
import com.pixel.player.PlayerManager;
import com.pixel.start.PixelRealmsServer;
import com.pixel.tile.Tile;
import com.pixel.util.FileItem;
import com.pixel.util.Toolkit;

public class WorldServer {

	public float pieceLayerOffset = 0.85F;
	public float clipConstant = 0.4F;
	public int worldSaveCount = 0;
	public static int c = 384;
	public static int tileConstant = 48;
	public static ConcurrentHashMap<Integer, Tile> tiles = new ConcurrentHashMap<Integer,Tile>();
	public static Piece[] pieces;
	public static ConcurrentHashMap<Integer, PieceBuilding> buildings = new ConcurrentHashMap<Integer,PieceBuilding>();
	public static ConcurrentHashMap<Integer, Entity> entities = new ConcurrentHashMap<Integer,Entity>();
	public static ConcurrentHashMap<Integer, Herd> herds = new ConcurrentHashMap<Integer, Herd>();
	public static boolean init = false;
	
	public WorldServer() {
		
		pieces = new Piece[c * c];
		
		if (!(new FileItem("tiles.dat").exists())) {
			PixelLogger.print("Generating Random World...", PixelColor.PURPLE);
			generateSquareMap();		
			PixelLogger.print("Generation Complete!", PixelColor.PURPLE);
			
		} else {
			PixelLogger.print("Loading World...", PixelColor.PURPLE);
			load();
			PixelLogger.print("World Loaded!", PixelColor.PURPLE);
			
		}
		
		PixelLogger.print("Server Initialized.", PixelColor.RED);
		
		init = true;
		
		PixelRealmsServer.world = this;
		
	}
	
	public void tick() {
		
		PlayerManager.tick();
		
		
		for (Herd herd : herds.values()) {
			
			herd.tick(this);
//			new HerdUpdater(herd, this).start();

		}

		for (int x = 0; x < entities.size(); x ++) {
			try {
				Thread.sleep(2);
			} catch (Exception e){}
			Entity entity = (Entity) entities.values().toArray()[x];
			if (entity instanceof EntityAnimal) {

				if (((EntityAnimal)entity).herd != null) {

				} else
					entity.tick(this);

			} else {

				entity.tick(this);

			}

		}
		
		for (int i = 0; i < pieces.length; i++) {
			pieces[i].tick(this);
		}
		
	}
	
	public ArrayList<ArrayList<WorldComponent>> getComponentsToLoad(int posX, int posY, int userID) {

		ArrayList<WorldComponent> loadTiles = new ArrayList<WorldComponent>();
		ArrayList<WorldComponent> loadPieces = new ArrayList<WorldComponent>();
		ArrayList<WorldComponent> loadEntities = new ArrayList<WorldComponent>();
		ArrayList<WorldComponent> loadPlayers = new ArrayList<WorldComponent>();

		int r = 85;
		int aX = posX - r;
		int bX = posX + r;
		int aY = posY - r;
		int bY = posY + r;
		
		for (int x = aX; x <= bX; x ++) {
			
			for (int y = aY; y <= bY; y ++) {
				
				if (Math.sqrt((posX - x)*(posX - x) + (posY - y)*(posY - y)) <= r) {

					if (getTileObject(x, y) != null) {
						WorldComponent tile = new WorldComponent(getTileObject(x, y));
						WorldComponent piece = null;
						if (getPieceObject(x, y) != null) {
							piece = new WorldComponent(getPieceObject(x, y));
							loadTiles.add(tile);
							loadPieces.add(piece);
						} else {

							loadTiles.add(tile);

						}
					}


				}

			}
			
		}
		
		for (int b = 0; b < entities.size(); b ++) {
			
			Entity e = (Entity) entities.values().toArray()[b];
			if (Math.sqrt((posX - e.getX())*(posX - e.getX()) - (posY - e.getY())*(posY - e.getY())) <= r) {
				loadEntities.add(new WorldComponent(e));
				
			}
		}
		
		for (int x = 0; x > PlayerManager.players.size(); x ++) {
			
			EntityPlayer p = (EntityPlayer) PlayerManager.players.values().toArray()[x];
			
			if (p.userID != userID) {
				
				loadPlayers.add(new WorldComponent(p));
				
			}
			
		}
		
		ArrayList<ArrayList<WorldComponent>> components = new ArrayList<ArrayList<WorldComponent>>();
		components.add(loadTiles);
		components.add(loadPieces);
		components.add(loadEntities);
		components.add(loadPlayers);

		return components;
		
	}
	
	public void generateSquareMap() {
		Random r = new Random();

		for (int y = 0; y < c; y++) {
			for (int x = 0; x < c; x++) {
				new Tile(x, y, 0);
				
				if ((x < 20 && x >= 10) && (y > 10 && y < c-11) && r.nextInt(x-9) == 0) {
					new Piece(x, y, 16);
				} else
				if ((x > c-21 && x <= c-11) && (y > 10 && y < c-11) && r.nextInt(c-10-x) == 0) {
					new Piece(x, y, 16);
				} else
				if ((y < 20 && y >= 10) && (x > 10 && x < c-11) && r.nextInt(y-9) == 0) {
					new Piece(x, y, 16);
				} else
				if ((y > c-21 && y < c-11) && (x > 10 && x < c-11) && r.nextInt(c-10-y) == 0) {
					new Piece(x, y, 16);
				} else
				if (x < 10 || x > c-11 || y < 10 || y > c-11) {
					new Piece(x, y, 16);
				} else
				if (r.nextInt(10) == 0) {
					new Piece(x, y, 1);
				} else
				if (r.nextInt(10) == 0) {
					new Piece(x, y, 2);
				}  else
				if (r.nextInt(10) == 0) {
					new Piece(x, y, 5);
				} else
				if (r.nextInt(40) == 0) {
					new Piece(x, y, 10);
				} else
				if (r.nextInt(40) == 0) {
					new Piece(x, y, 3);
				} else
				if (r.nextInt(40) == 0) {
					new Piece(x, y, 4);
				} else
				if (r.nextInt(80) == 0) {
					new Piece(x, y, 9);
				}
				else {
					new Piece(x, y, 0);
				}
			
			}
			
		}
		
		new HerdBunny(150, 150);
		
	}

	public static void setTile(int x, int y, int id) {
		tiles.put((y * c) + x, new Tile(x, y, id));
		PlayerManager.broadcastPacket(new PacketUpdateTile(id, x, y));
	}

	public static int getTile(int x, int y) {
		return tiles.get((y * c) + x).id;
	}

	public static Tile getTileObject(int x, int y) {
		return tiles.get((y * c) + x);
	}
	
	public static void setPieceObject(int x, int y, Piece p) {
		pieces[((y * c) + x)] = p;
	}

	public static void setPiece(int x, int y, int id) {
		pieces[((y * c) + x)] = new Piece(x, y, id);

		PlayerManager.broadcastPacket(new PacketUpdatePiece(pieces[((y * c) + x)]));

	}
	
	public static void setPiece(int x, int y, int id, int damage, int metadata, int buildingID) {
		
		if (buildingID == -1) {
			pieces[((y * c) + x)] = new Piece(x, y, id);
		
		} else {
			
			pieces[((y * c) + x)] = new PieceBuilding(x, y, buildingID);
			
		}
		pieces[((y * c) + x)].damage = damage;
		pieces[((y * c) + x)].metadata = metadata;

		PlayerManager.broadcastPacket(new PacketUpdatePiece(pieces[((y * c) + x)]));

	}
	
	public static int getPiece(int x, int y) {
		return pieces[(y * c) + x].id;
	}

	public static Piece getPieceObject(int x, int y) {
		return pieces[(y * c) + x];
	}
	
	public static Entity getEntity(int serverID) {
		
		return entities.get(serverID);
		
	}
	
	public Piece getNearestPieceWithinRadiusWithID(int id, int x, int y, int radius) {

		int radiusSquared = radius * radius;

		ArrayList<Float[]> trees = new ArrayList<Float[]>();

		for(int b  = -radius; b <= radius; b++) {

			for(int z = -radius; z <= radius; z++) {

				if( (b*b) + (z*z) <= radiusSquared) {

		        	if (z > 0 && b > 0) {

						if (getPiece(b, z) == id) {

							float distance = (float) Math.sqrt((b - x) * (b - x) + (z - y) * (z - y));

							trees.add(new Float[]{b + 0.0F, z + 0.0F, distance});

						}

					}
		        }
		    }
		}

		Float[] top = new Float[]{1F, 1F, (float) (100000*100000)};

		for (int z = 0; z < trees.size(); z ++) {

			Float[] temp = trees.get(z);

			if (temp[2] < top[2]) {

				top = temp;

			}
 			
		}
            
		int xNew = (int) (top[0] + 0);
		int yNew = (int) (top[1] + 0);

		return getPieceObject(xNew, yNew);

	}

	public static void propagatePiece(Piece piece) {

		pieces[(piece.posY * c) + piece.posX] = piece;

	}

	public static void propagateTile(Tile tile) {

		tiles.put((tile.posY * c) + tile.posX, tile);
		
	}
	
	public static void propagateEntity(Entity entity) {

		entities.put(entity.serverID, entity);
		
	}
	
	@SuppressWarnings("unchecked")
	public void load() {
		
		Toolkit k = new Toolkit();
		ArrayList<Integer[]> tileSave = (ArrayList<Integer[]>) k.load("tiles.dat");
		ArrayList<Integer[]> piecesSave = (ArrayList<Integer[]>) k.load("pieces.dat");
		ArrayList<Float[]> entitySave = (ArrayList<Float[]>) k.load("entities.dat");
		ArrayList<ArrayList<Float[]>> herdSave = (ArrayList<ArrayList<Float[]>>) k.load("herds.dat");
		
		if (herdSave == null) {
			
			herdSave = new ArrayList<ArrayList<Float[]>>();
			
		}
		
		if (entitySave == null) {
			
			entitySave = new ArrayList<Float[]>();
			
		}
		
		tiles.clear();
		pieces = new Piece[c * c];
		entities.clear();
		herds.clear();
		
		for (int x = 0; x < tileSave.size(); x ++) {
			
			Integer[] t = tileSave.get(x);
			
			new Tile(t[1], t[2], t[0]);

		}

		for (int x = 0; x < piecesSave.size(); x ++) {

			Integer[] p = piecesSave.get(x);
			new Piece(p[0], p[1], p[2], p[3], p[4]);


		}
		
		for (int y = 0; y < c; y++) {
			for (int x = 0; x < c; x++) {
				
				if (pieces[((y * c) + x)] == null) {
					
					new Piece(x, y, 0);
					
				}
				
			}
		}
		
		for (int x = 0; x < pieces.length; x ++) {
			
			
			
		}

		for (int x = 0; x < entitySave.size(); x ++) {

			Float[] e = entitySave.get(x);
			Entity entity = Entity.getEntity((int) (e[0] + 0));

			entity.setX(e[1]);
			entity.setY(e[2]);
			entity.health = (e[3]);
			entity.serverID = entities.size() + 1;
			
			propagateEntity(entity);

		}
		
		for (int x = 0; x < herdSave.size(); x++) {
			
			ArrayList<Float[]> herdArray = herdSave.get(x);
			Float[] herdInfo = herdArray.get(0);
			
			Herd herd = Herd.getHerd(Math.round(herdInfo[8]));
			herd.load(Math.round(herdInfo[0]), Math.round(herdInfo[1]), Math.round(herdInfo[2]), herdInfo[3], herdInfo[4], herdInfo[5], herdInfo[6], Math.round(herdInfo[7]), herdArray);
			herd.id = herds.size() + 1;
			herds.put(herd.herdID, herd);
			
		}
		
		

	}

	public void save() {
		
		PlayerManager.broadcastPacket(new PacketChat(new ChatMessage("Server", "Saving World...", Color.RED, 0)));
		PixelLogger.print("Saving World...", PixelColor.RED);
		
		PlayerManager.save();
		new Thread(new WorldSaveThread(this)).start();
		
	}
	
}
