package com.pixel.admin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.pixel.player.PlayerManager;

public class StatisticsThread extends Thread {

	public int userID;
	public long time;
	public String username;
	public int id;
	
	public StatisticsThread(int userID, String username, int id) {

		this.userID = userID;
		this.username = username;
		this.id = id;
	}

	public void run() {

		switch (id) {
		case 0:
			onLogin();
			break;
		case 1:
			onLogout();
			break;
			
		
		}
		
	}
	
	public void onLogout() {
		
		String urlParameters = "";

		try {
			urlParameters = "user_id=" + userID + "&username=" + username + "&time=" + time + "&login_time=" + 		PlayerManager.playersLoginTime.get(userID);
			PlayerManager.playersLoginTime.remove(userID);
			String request = "http://pixel-realms.com/backend/on_user_logout.php";
			URL url = new URL(request);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false); 
			connection.setRequestMethod("POST"); 
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			connection.setUseCaches (false);

			DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			InputStream input = connection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(input));

			StringBuilder sb = new StringBuilder();

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			} 

			String responseString = sb.toString();
			
			if (responseString.equals("SUCCESS")) {
				
				PixelLogger.log("Success!");
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
public void onLogin() {
		
		String urlParameters = "";

		try {
			urlParameters = "user_id=" + userID + "&username=" + username + "&time=" + time;

			String request = "http://pixel-realms.com/backend/on_user_login.php";
			URL url = new URL(request);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false); 
			connection.setRequestMethod("POST"); 
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			connection.setUseCaches (false);

			DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			InputStream input = connection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(input));

			StringBuilder sb = new StringBuilder();

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			} 

			String responseString = sb.toString();
			
			try {
				
				time = Long.parseLong(responseString);
				PlayerManager.playersLoginTime.put(userID, time);

			} catch (Exception e) {
				
				PixelLogger.err("Error Parsing Time From Statistics Login");
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
