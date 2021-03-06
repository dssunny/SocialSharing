package com.dssunny.share;
/*
 * created by deepak sharma
 */
import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;

public class FcebookActivity extends FragmentActivity {

	private LoginButton loginBtn;
	private Button postImageBtn;
	private Button updateStatusBtn;

	private TextView userName;

	private UiLifecycleHelper uiHelper;

	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");

	private static String message = "Sample status posted from android app";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		uiHelper = new UiLifecycleHelper(this, statusCallback);
		uiHelper.onCreate(savedInstanceState);

		setContentView(R.layout.facebook);

		userName = (TextView) findViewById(R.id.user_name);
		loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
		loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
			@Override
			public void onUserInfoFetched(GraphUser user) {
				if (user != null) {
					userName.setText("Hello, " + user.getName());
				} else {
					userName.setText("You are not logged");
				}
			}
		});

		postImageBtn = (Button) findViewById(R.id.post_image);
	
		updateStatusBtn = (Button) findViewById(R.id.update_status);
		updateStatusBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//postStatusMessage();
				
				// Showing a dialog 
				final Dialog dialog = new Dialog(FcebookActivity.this);
				// setting a layout of dailog
				dialog.setContentView(R.layout.custom_dailog_box);
				// setting a tittle
				dialog.setTitle("Facebook sharing");
	 
				// get the reference of EditText
				final EditText text = (EditText) dialog.findViewById(R.id.text);
				text.setText("sample text");
				
				// get the reference of Cancel button
				
				Button dialogCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
				dialogCancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// dialog the dismiss
						dialog.dismiss();
						// showing a toast
						Toast.makeText(FcebookActivity.this,
								"cancel dialog",
								Toast.LENGTH_LONG).show();
					}
				});
				
				// get the reference of sharing button
				Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
				// if button is clicked, close the custom dialog
				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// dismissing the dialog
						dialog.dismiss();
						Toast.makeText(FcebookActivity.this,
								"ok dialog",
								Toast.LENGTH_LONG).show();
						// get the text of the edit text
						message=text.getText().toString();
						// posting the status on facebook of loginned user 
						postStatusMessage();
						
						
					}
				});
				
				
	 
				dialog.show();
				
			}
		});

		buttonsEnabled(false);
	}

	private Session.StatusCallback statusCallback = new Session.StatusCallback() {

		public void call(Session session, SessionState state,
				Exception exception) {
			if (state.isOpened()) {
				buttonsEnabled(true);
				Log.d("FacebookSampleActivity", "Facebook session opened");
			} else if (state.isClosed()) {
				buttonsEnabled(false);
				Log.d("FacebookSampleActivity", "Facebook session closed");
			}
		}
	};

	public void buttonsEnabled(boolean isEnabled) {
		postImageBtn.setEnabled(isEnabled);
		updateStatusBtn.setEnabled(isEnabled);
	}
/*
	public void postImage() {
		if (checkPermissions()) {
			Bitmap img = BitmapFactory.decodeResource(getResources(),
					R.drawable.ic_launcher);
			Request uploadRequest = Request.newUploadPhotoRequest(
					Session.getActiveSession(), img, new Request.Callback() {
						@Override
						public void onCompleted(Response response) {
							Toast.makeText(FcebookActivity.this,
									"Photo uploaded successfully",
									Toast.LENGTH_LONG).show();
						}
					});
			uploadRequest.executeAsync();
		} else {
			requestPermissions();
		}
	}
*/
	public void postStatusMessage() {
		// checking for permission
		if (checkPermissions()) {
			// creating a request object , apssing a session object and message
			Request request = Request.newStatusUpdateRequest(
					Session.getActiveSession(), message,
					new Request.Callback() {
						@Override
						public void onCompleted(Response response) {
							// call back method , called when status s sucessfully updated
							if (response.getError() == null)
								Toast.makeText(FcebookActivity.this,
										"Status updated successfully",
										Toast.LENGTH_LONG).show();
						}
					});
			request.executeAsync();
		} else {
			requestPermissions();
		}
	}

	public boolean checkPermissions() {
		// checking for pession of publish 
		Session s = Session.getActiveSession();
		if (s != null) {
			return s.getPermissions().contains("publish_actions");
		} else
			return false;
	}

	public void requestPermissions() {
		Session s = Session.getActiveSession();
		if (s != null)
			s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
					this, PERMISSIONS));
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		buttonsEnabled(Session.getActiveSession().isOpened());
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle savedState) {
		super.onSaveInstanceState(savedState);
		uiHelper.onSaveInstanceState(savedState);
	}

}