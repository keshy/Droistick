package com.facebook.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

public class Choose  extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.choose);
		

		View homeB = findViewById(R.id.Button01);
		homeB.setOnClickListener(this);

		View awayB = findViewById(R.id.Button02);
		awayB.setOnClickListener(this);

	}

	public void onClick(View v) {
		Intent i;

		switch (v.getId()) {
		case R.id.Button01:
			Cloud.teamid = "0";
			break;
		case R.id.Button02:
			Cloud.teamid = "1";
			break;
		}
		
		i = new Intent(this, orientation.class);
		startActivity(i);

	}
}