package com.HuskySoft.hellometrobike;

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
		@Override
		public void run() {
			// Change current text
			MainActivity.this.runOnUiThread(new Runnable () {
				public void run(){
					mainText.setText("Contacting Google Maps...");
				}
			});
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// Do nothing if interrupted
			}
			
			// Change current text
			MainActivity.this.runOnUiThread(new Runnable () {
				public void run(){
					mainText.setText("Got information!\n........................................");
				}
			});
		}
		
		
		
		
		
		
		
	}
}
