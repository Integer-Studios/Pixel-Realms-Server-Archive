package com.pixel.world;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;
import java.util.HashMap;
import java.util.Random;

import com.pixel.admin.PixelLogger;
import com.pixel.piece.Piece;
import com.pixel.tile.Tile;

public class WorldReader {
	
	public WorldReader(WorldServer w) {
		world = w;
	}
	
	public static boolean debug = true;
	public static boolean genBiome = true;
	
	public WorldServer world;
	public static HashMap<Integer, Integer> tiles = new HashMap<Integer, Integer>();
	public static HashMap<Integer, Integer> pieces = new HashMap<Integer, Integer>();
	
	public void readWorld() {
		 Image tileMap = Toolkit.getDefaultToolkit().getImage("map/tiles.png");

		    PixelGrabber grabber = new PixelGrabber(tileMap, 0, 0, -1, -1, false);
		    
		    log("Pixel map loading...");
		    
		    try {
		    	if (grabber.grabPixels()) {
		    		
				    log("Tile map loaded.");
				    
		    		int width = grabber.getWidth();
		    		int height = grabber.getHeight();
		    		if (width != height) {
		    			PixelLogger.err("Tile map does not conform to c squared requirements (width != height). Aborting read.");
		    			System.exit(-1);
		    		}
		    		
		    		WorldServer.c = width;
				    log("Reading tile map...");

		    		int[] data = (int[]) grabber.getPixels();
		    		int index = 0;
		    		int id = -1;
		    		for (int y = 0; y < WorldServer.c; y++) {
		    			for (int x = 0; x < WorldServer.c; x++) {
		    				id = getTileIDForColor(data[index]);
		    				if (id != -1) {
//		    					if (id != WorldServer.defautTile) {
		    						new Tile(x, y, id, -1);
//		    					}
		    					log("Tile loaded at position: (" + x + ", " + y + ") with id:" + id);
		    				} else {
		    					err("Tile not loaded at (" + x + ", " + y + ")");
	    						new Tile(x, y, WorldServer.defautTile, -1);
		    				}
		    				index++;
		    			}
		    		}
				}
			    log("Tile map read.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		    

		    
			 Image pieceMap = Toolkit.getDefaultToolkit().getImage("map/pieces.png");

			    grabber = new PixelGrabber(pieceMap, 0, 0, -1, -1, false);

			    try {
			    	if (grabber.grabPixels()) {
			    		
					    log("Piece map loaded.");
			    		
			    		int width = grabber.getWidth();
			    		int height = grabber.getHeight();
			    		if (width != height) {
			    			PixelLogger.err("Piece map does not conform to c squared requirements (width != height). Aborting read.");
			    			System.exit(-1);
			    		}
			    		
			    		WorldServer.c = width;
			    		
			    		WorldServer.pieces = new Piece[WorldServer.c * WorldServer.c];
			    		
					    log("Reading piece map...");
					    
			    		int[] data = (int[]) grabber.getPixels();
			    		int index = 0;
			    		int id = -1;
			    		for (int y = 0; y < WorldServer.c; y++) {
			    			for (int x = 0; x < WorldServer.c; x++) {
			    				
			    				id = getPieceIDForColor(data[index]);
			    				if (id != -1) {
				    				new Piece(x, y, id, true);
			    					log("Piece loaded at position: (" + x + ", " + y + ") with id:" + id);
			    				} else {
			    					err("Piece not loaded at (" + x + ", " + y + ")");
		    						new Piece(x, y, 0, true);
			    				}
			    				
			    				index++;
			    			}
			    		}
					}
				    log("Piece map read.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			    
			    log("Pixel map read.");
			    
			    if (genBiome) {
			    	
			    	generateBiome();
			    	
			    }

	}
	
	public void generateBiome() {
		log("Generating biome...");
		Random r = new Random();

		for (int y = 0; y < WorldServer.c; y++) {
			for (int x = 0; x < WorldServer.c; x++) {
				
				if (WorldServer.getTile(x, y) == 0) {
				
				if ((x < 20 && x >= 10) && (y > 10 && y < WorldServer.c-11) && r.nextInt(x-9) == 0) {
					new Piece(x, y, 16, true);
				} else
				if ((x > WorldServer.c-21 && x <= WorldServer.c-11) && (y > 10 && y < WorldServer.c-11) && r.nextInt(WorldServer.c-10-x) == 0) {
					new Piece(x, y, 16, true);
				} else
				if ((y < 20 && y >= 10) && (x > 10 && x < WorldServer.c-11) && r.nextInt(y-9) == 0) {
					new Piece(x, y, 16, true);
				} else
				if ((y > WorldServer.c-21 && y < WorldServer.c-11) && (x > 10 && x < WorldServer.c-11) && r.nextInt(WorldServer.c-10-y) == 0) {
					new Piece(x, y, 16, true);
				} else
				if (x < 10 || x > WorldServer.c-11 || y < 10 || y > WorldServer.c-11) {
					new Piece(x, y, 16, true);
				} else
				if (r.nextInt(10) == 0) {
					new Piece(x, y, 1, true);
				} else
				if (r.nextInt(10) == 0) {
					new Piece(x, y, 2, true);
				}  else
				if (r.nextInt(10) == 0) {
					new Piece(x, y, 5, true);
				} else
				if (r.nextInt(40) == 0) {
					new Piece(x, y, 10, true);
				} else
				if (r.nextInt(40) == 0) {
					new Piece(x, y, 3, true);
				} else
				if (r.nextInt(40) == 0) {
					new Piece(x, y, 4, true);
				} else
				if (r.nextInt(80) == 0) {
					new Piece(x, y, 9, true);
				}
				else {
					new Piece(x, y, 0, true);
				}
				
				}
			
			}
			
		}
		
		log("Biome generated!");
		
	}
	
	private int getTileIDForColor(int i) {
		if (!tiles.containsKey(i)) {
			err("Tile id could not be found for color code: " + i);
			return -1;
		}
		return tiles.get(i);
	}

	private int getPieceIDForColor(int i) {
		if (!pieces.containsKey(i)) {
			err("Piece id could not be found for color code: " + i);
			return -1;
		}
		return pieces.get(i);
	}
	
	private void log(String s) {
		if (debug)
		PixelLogger.print(s, PixelLogger.PixelColor.PURPLE);
	}
	
	private void err(String s) {
		if (debug)
		PixelLogger.err(s);
	}
	
	static {
		tiles.put(-10516671, 0);//grass flat #5f8741
		tiles.put(-6581689, 1);//sand flat #9b9247
		tiles.put(-8291524, 2);//sand rounded #817b3c
		tiles.put(-14514510, 3);//water flat #2798c0
		tiles.put(-15965550, 4);//water rounded #0c6292
		tiles.put(-15262173, 5);//cobble flat #171e23
		tiles.put(-11837583, 6);//cobble rounded #4b5f71
		tiles.put(-10075873, 7);//hill #66411f
		tiles.put(-16777216, 8);//bg #000000
		tiles.put(-11521513, 9);//building floor #503217
		
		
		
		pieces.put(16777215, 0);//blank (transparent)
		pieces.put(-6827661, 1);//grass #97d173
		pieces.put(-12227797, 2);//grass #456b2b
		pieces.put(-10725986, 3);//flower #5c559e
		pieces.put(-7301508, 4);//rock #90967c
		pieces.put(-13074873, 5);//pine #387e47
		pieces.put(-10404837, 6);//pine stump #1b3922
		pieces.put(-14993118, 7);//pine down #1b3922
		pieces.put(-14540782, 8);//pine logs #1b3922
		pieces.put(-12170947, 9);//big rock #46493d
		pieces.put(-13472213, 10);//apple tree #326e2b
		pieces.put(-5468610, 11);//apple stump #4e3d21
		pieces.put(-15191787, 12);//apple down #326e2b
		pieces.put(-11649759, 13);//apple logs #4e3d21
		
		pieces.put(-14776012, 16);//abyssal fur #1e8934


		
	}

}
