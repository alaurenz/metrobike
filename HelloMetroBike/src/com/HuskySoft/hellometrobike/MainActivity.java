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

	public static final String TAG = "HelloMetroBikeMain";
	
	TextView mainText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		mainText = (TextView) findViewById(R.id.mainText);
		if(mainText == null){
			Log.e(TAG,"Couldn't find mainText!");
		} else {
			mainText.setText("App is starting!");
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
			// Change current text
			MainActivity.this.runOnUiThread(new Runnable () {
				public void run(){
					mainText.setText("Contacting Google Maps...");
				}
			});
			
			try {
				URL url = new URL("http://maps.googleapis.com/maps/api/directions/xml?origin=Seattle%2CWA&destination=New%20York%2CNY&sensor=false");
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
			
			Log.w(TAG, "Got response: " + myResponse);
			
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// Do nothing if interrupted
//			}
			
			// Change current text
			MainActivity.this.runOnUiThread(new Runnable () {
				public void run(){
					mainText.setText("Got information!\n" + GMapsCommTest.this.myResponse);
				}
			});
		}
		
		
		
		
		
		
		
	}
}
