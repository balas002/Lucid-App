package com.example.database;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.database.fragment.MyPostsFragment;
import com.example.database.fragment.MyTopPostsFragment;
import com.example.database.fragment.RecentPostsFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
	// Request number to store the videos
	private static final int RC_STORAGE_PERMS1 = 101;
	private static final int RC_STORAGE_PERMS2 = 102;
	private int hasWriteExtStorePMS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// The fragments in the main page which shows the recent, new post and my posts.
		FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			private final Fragment[] mFragments = new Fragment[]{
					new RecentPostsFragment(),
					new MyPostsFragment(),
					new MyTopPostsFragment(),
			};

		//Setting the position for the fragment.
			@Override
			public Fragment getItem(int position) {
				return mFragments[position];
			}

			@Override
			public int getCount() {
				return mFragments.length;
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return getResources().getStringArray(R.array.headings)[position];
			}
		};

		ViewPager mViewPager = findViewById(R.id.container);
		mViewPager.setAdapter(mPagerAdapter);


		TabLayout tabLayout = findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(mViewPager);

		// Chnage from main activity to NewPostActivity class.
		findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, NewPostActivity.class));
			}
		});
	}

//To create menu.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	// Permission to access the camera for the video.
	@Override
	public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case RC_STORAGE_PERMS1:
			case RC_STORAGE_PERMS2:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

				} else {
					AlertDialog.Builder alert = new AlertDialog.Builder(this);
					alert.setMessage("You need to allow permission");
					alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							dialogInterface.dismiss();
							Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
							intent.setData(Uri.parse("package:" + getPackageName()));
							startActivityForResult(intent, requestCode);
						}
					});
					alert.setCancelable(false);
					alert.show();
				}
				break;
		}
	}

	//If the storage access is granted and we need to store then this function writes it to the storage.
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case RC_STORAGE_PERMS1:
			case RC_STORAGE_PERMS2:
				hasWriteExtStorePMS = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

				ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_STORAGE_PERMS2);

				break;
		}
	}

	//	Options for the menu.
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

			//Goes to ChatActivity.
			case R.id.action_chat:
				startActivity(new Intent(this, ChatActivity.class));
				return true;

			//Goes to SignIn Acitivity.
			case R.id.action_logout:
				FirebaseAuth.getInstance().signOut();
				startActivity(new Intent(this, SignInActivity.class));
				finish();
				return true;



			default:
				return super.onOptionsItemSelected(item);
		}
	}

}