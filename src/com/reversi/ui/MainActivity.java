package com.reversi.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.reversi.android.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void btn_StartGame2Player(View v) {
			
		Intent intent = new Intent(MainActivity.this, GameActivity.class);
		Bundle b = new Bundle();
		b.putInt("key", 2); // 1 player game 
		intent.putExtras(b); 
		startActivity(intent);
		finish();
	}

	public void btn_StartGame1Player(View v) {
		Intent intent = new Intent(MainActivity.this, GameActivity.class);
		Bundle b = new Bundle();
		b.putInt("key", 1); // 2 player game
		intent.putExtras(b); 
		startActivity(intent);
		finish();
	}

	public void btn_QuitGameClick(View v) {
		finish();
		System.exit(0);
	}
}
