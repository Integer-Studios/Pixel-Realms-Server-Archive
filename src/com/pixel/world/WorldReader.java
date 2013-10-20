package com.pixel.world;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;
import java.util.HashMap;

import com.pixel.admin.PixelLogger;
import com.pixel.tile.Tile;

public class WorldReader {
	
	public WorldReader(WorldServer w) {
		world = w;
	}
	
	public WorldServer world;
	public static HashMap<Integer, Integer> tiles = new HashMap<Integer, Integer>();
	public static HashMap<Integer, Integer> pieces = new HashMap<Integer, Integer>();
	
	public void readWorld() {
		 Image image = Toolkit.getDefaultToolkit().getImage("map.png");

		    PixelGrabber grabber = new PixelGrabber(image, 0, 0, -1, -1, false);

		    try {
		    	if (grabber.grabPixels()) {
		    		
		    		int width = grabber.getWidth();
		    		int height = grabber.getHeight();
		    		if (width != height) {
		    			PixelLogger.err("Pixel map does not conform to c squared requirements (width != height). Aborting read.");
		    			System.exit(-1);
		    		}
		    		
		    		WorldServer.c = width;
		    		
		    		int[] data = (int[]) grabber.getPixels();
		    		int index = 0;
		    		for (int y = 0; y < WorldServer.c; y++) {
		    			for (int x = 0; x < WorldServer.c; x++) {
		    				new Tile(x, y, getTileIDForColor(data[index]), -1);
		    				System.out.println("(" + x + ", " + y + ") id:" + getTileIDForColor(data[index]));
		    				index++;
		    			}
		    		}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
	private int getTileIDForColor(int i) {
		return tiles.get(i);
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
		
		
		pieces.put(69, 0);
		
	}

}
