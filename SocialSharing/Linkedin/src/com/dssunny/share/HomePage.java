package com.dssunny.share;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.google.android.gms.plus.PlusShare;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomePage extends Activity {

	Button btnfb;
	Button btntwitter;
	Button btnlinked;
	Button btngooglepls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);

		/*
		 * getting the reference of all the button
		 */
		btnfb = (Button) findViewById(R.id.btnfb);
		btntwitter = (Button) findViewById(R.id.btntwtr);
		btnlinked = (Button) findViewById(R.id.btnlinked);
		btngooglepls = (Button) findViewById(R.id.btngoogleplus);

		/*
		 * Setting click listener on facebook button
		 */
		btnfb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent i = new Intent(HomePage.this, FcebookActivity.class);
				startActivity(i);
			}
		});

		/*
		 * Setting click listener on Linked-In button
		 */
		btnlinked.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent i = new Intent(HomePage.this,
						LinkedInSampleActivity.class);
				startActivity(i);
			}
		});

		/*
		 * Setting click listener on Google Plus button
		 */
		btngooglepls.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent shareIntent = new PlusShare.Builder(HomePage.this)
						.setText("Hello Android!")
						.setType("image/png")
						.setContentDeepLinkId(
								"testID",
								"Test Title",
								"Test Description",
								Uri.parse("https://developers.google.com/+/images/interactive-post-android.png"))
						.getIntent();
				startActivityForResult(shareIntent, 0);

			}
		});
		/*
		 * Setting click listener on twitter button
		 */
		btntwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent shareIntent = new Intent();
				shareIntent.setType("text/plain");
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						"Content to share");
				PackageManager pm = v.getContext().getPackageManager();
				List<ResolveInfo> activityList = pm.queryIntentActivities(
						shareIntent, 0);
				for (final ResolveInfo app : activityList) {
					if ((app.activityInfo.name).contains("twitter")) {
						final ActivityInfo activity = app.activityInfo;
						final ComponentName name = new ComponentName(
								activity.applicationInfo.packageName,
								activity.name);
						shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
						shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
						shareIntent.setComponent(name);
						v.getContext().startActivity(shareIntent);

					}
				}
			}
		});

		/*
		 * try {
		 * 
		 * PackageInfo info = getPackageManager().
		 * getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
		 * 
		 * for (Signature signature : info.signatures) {
		 * 
		 * MessageDigest md = MessageDigest.getInstance("SHA");
		 * md.update(signature.toByteArray());
		 * Log.d("====Hash Key===",Base64.encodeToString(md.digest(),
		 * Base64.DEFAULT));
		 * 
		 * }
		 * 
		 * } catch (NameNotFoundException e) {
		 * 
		 * e.printStackTrace();
		 * 
		 * } catch (NoSuchAlgorithmException ex) {
		 * 
		 * ex.printStackTrace();
		 * 
		 * }
		 */

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
