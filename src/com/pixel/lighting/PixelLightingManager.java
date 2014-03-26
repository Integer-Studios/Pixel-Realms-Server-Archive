package com.pixel.lighting;

import java.util.HashMap;

import com.pixel.communication.packet.PacketLight;
import com.pixel.player.PlayerManager;

public class PixelLightingManager {
	
	public boolean rising, setting;
	float lightValue = 0.15f;
	float defaultNight = 0.15F;
	float defaultDay = 1F;
	float defaultLightChange = 0.001F;
	public static HashMap<Integer, PixelLight> lights = new HashMap<Integer, PixelLight>();
	
	public PixelLightingManager() {}
	
	public static void propagateLight(PixelLight light, int proposalID) {
		
		int id = lights.size() + 1;
		while (lights.containsKey(id)) {
			
			id ++;
			
		}
		
		light.id = id;
		lights.put(id, light);
		
		PlayerManager.broadcastPacket(new PacketLight(light, proposalID));
		
	}
	
	
	public static void propagateLight(PixelLight light) {
		
		int id = lights.size() + 1;
		while (lights.containsKey(id)) {
			
			id ++;
			
		}
		
		light.id = id;
		lights.put(id, light);
		
		PlayerManager.broadcastPacket(new PacketLight(light));
		
	}
	
	public static PixelLight getLight(int id) {
		
		return lights.get(id);
		
	}
	
	public void calcuateLightValue() {
		
		float t = 0;
		int min = 0;
		
		if (t < 12000) {
			
			t = t / 1000;
			
			float m = t - ((int) t);
			m = 60 * m;
			min = (int) m;
			
			
		} else {
			
			t = t / 1000;
			
			float m = t - ((int) t);
			m = 60 * m;
			min = (int) m;
			
		}
		
		if ((t >= 6 && t <= 6.9999) || rising) {
			
			handleSunrise(min);
			
		} else if ((t >= 18 && t <= 18.9999) || setting) {
			
			handleSunset(min);
			
		} else {
			
			handleNormal(t, min);
			
		}
		
	}
	
	public void handleNormal(float t, int m) {
		
		if (t > 7 && t < 18) {
			
			lightValue = defaultDay;
			
		} 
		
		if (t < 6 || t > 19) {
			
			lightValue = defaultNight;
			
		}
		
	}
	
	public void handleSunrise(int m) {

		if (m > 15 && !rising && lightValue != defaultDay) {
			
			rising = true;
			
		} else if (rising && lightValue < defaultDay) {
			
			lightValue += defaultLightChange;

		} else if (lightValue >= defaultDay) {
			
			lightValue = defaultDay; 
			rising = false;
			
		}
		
	}
	
	public void handleSunset(int m) {

		if (m > 15 && !setting && lightValue != defaultNight) {
			
			setting = true;
			
		} else if (setting && lightValue > defaultNight) {
			
			lightValue -= defaultLightChange;

		} else if (lightValue <= defaultNight) {
			
			lightValue = defaultNight; 
			setting = false;
			
		}
		
	}

	public static boolean hasLight(int lightID) {
	
		return lights.containsKey(lightID);

	}
	
}
