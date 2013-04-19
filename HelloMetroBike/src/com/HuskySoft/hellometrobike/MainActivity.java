package com.HuskySoft.hellometrobike;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	public static final String BICYCLE_URL = 
			"http://maps.googleapis.com/maps/api/directions/json?" +
			"origin=6504%20Latona%20Ave%20NE%2CSeattle%2CWA&" +
			"destination=3801%20Brooklyn%20Ave%20NE%2CSeattle%2CWA&" +
			"sensor=false&" +
			"mode=bicycling";
	
	public static final String TRANSIT_URL = 
			"http://maps.googleapis.com/maps/api/directions/json?" +
			"origin=6504%20Latona%20Ave%20NE%2CSeattle%2CWA&" +
			"destination=3801%20Brooklyn%20Ave%20NE%2CSeattle%2CWA&" +
			"sensor=false&" +
			"arrival_time=1368644400&" +
			"mode=transit";
	
	public static final String TAG = "HelloMetroBikeMain";
	
	TextView bicycleText; // The place to display bicycle directions
	TextView transitText; // The place to display transit directions
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		// Find our display TextView objects and save references to them
		bicycleText = (TextView) findViewById(R.id.bicycleText);
		transitText = (TextView) findViewById(R.id.transitText);
		
		if(bicycleText == null | transitText == null){
			Log.e(TAG,"Couldn't find main text views!");
		} else {
			// Start the communication thread to get directions!
			bicycleText.setText("App is starting!");
			transitText.setText("App is starting!");
			Thread testThread = new Thread(new GMapsCommTest());
			testThread.start();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private class GMapsCommTest implements Runnable {
		String myResponse = "empty";
		
		@Override
		public void run() {
			
			// Get bicycle directions
			
			// Change current text
			MainActivity.this.runOnUiThread(new Runnable () {
				public void run(){
					bicycleText.setText("Contacting Google Maps...");
				}
			});
			
			myResponse = doQuery(BICYCLE_URL);
			
			Log.w(TAG, "Got response: " + myResponse);
			
			// Change current text
			MainActivity.this.runOnUiThread(new Runnable () {
				public void run(){
					bicycleText.setText("Response from Google Maps:\n\n" + GMapsCommTest.this.myResponse);
				}
			});
			
			// Get transit directions
			
			// Change current text
			MainActivity.this.runOnUiThread(new Runnable () {
				public void run(){
					transitText.setText("Contacting Google Maps...");
				}
			});
			
			myResponse = doQuery(TRANSIT_URL);
			
			Log.w(TAG, "Got response: " + myResponse);
			
			// Change current text
			MainActivity.this.runOnUiThread(new Runnable () {
				public void run(){
					transitText.setText("Response from Google Maps:\n\n" + GMapsCommTest.this.myResponse);
				}
			});
		}

		private String doQuery(String theURL){
			String myResponse = "";
			
			try {
				URL url = new URL(theURL);
				//URL url = new URL("http://google.com");
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				try {
					InputStream in = new BufferedInputStream(urlConnection.getInputStream());
					byte[] thisResponse = new byte[2048];
					int bytesThisTime = 0;
					do{
						bytesThisTime = in.read(thisResponse);
						if(bytesThisTime < 0) break;
						String stringResponse = new String(thisResponse,0,bytesThisTime);
						myResponse += stringResponse;
					} while(bytesThisTime > 0);
				} finally {
					urlConnection.disconnect();
				}
				
			} catch (MalformedURLException e) {
				Log.e(TAG,"Bad url..." + e.getMessage());
			} catch (IOException e) {
				Log.e(TAG,"IO Exception..." + e.getMessage());
			}
			return myResponse;
		}
	}
}
