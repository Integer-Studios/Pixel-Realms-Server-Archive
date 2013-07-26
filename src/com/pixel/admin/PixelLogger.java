package com.pixel.admin;

public class PixelLogger {

	public static final String RESET = "\u001B[0m";
	public static String red_color = "\u001B[31m";
	public static String green_color = "\u001B[32m";
	public static String yellow_color = "\u001B[33m";
	public static String blue_color = "\u001B[34m";
	public static String purple_color = "\u001B[35m";
	
	public static String prefix = "[Pixel Realms] ";
	
	public enum PixelColor {
		
		RED(red_color),
		GREEN(green_color),
		YELLOW(yellow_color),
		BLUE(blue_color),
		PURPLE(purple_color);
		
		private final String escape;
		
		private PixelColor(final String escape) {
			
			this.escape = escape;
			
		}

		@Override
		public String toString() {
			return escape;
		}

	}
	
	public static void err(String error) {
		
		System.out.println(PixelColor.RED + prefix + error);
		System.out.print(RESET);
		
	}
	
	public static void log(String log) {
		
		System.out.println(prefix + log);
		System.out.print(RESET);

	}
	
	public static void print(String msg, PixelColor color) {
		
		System.out.println(color + prefix + msg);
		System.out.print(RESET);

	}
	
}
