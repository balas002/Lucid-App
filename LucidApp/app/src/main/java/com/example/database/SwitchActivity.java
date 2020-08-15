package com.example.database;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

// For the Welcome Page.
public class SwitchActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_switch);
	}

	public void gotoAdvance(View view) {
		startActivity(new Intent(this, SignInActivity.class));
		finish();
	}
}