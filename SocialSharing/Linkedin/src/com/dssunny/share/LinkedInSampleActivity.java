package com.dssunny.share;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dssunny.share.LinkedinDialog.OnVerifyListener;

import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Person;

/**
 * @author Deepak Sharma
 */
public class LinkedInSampleActivity extends Activity {
	Button login;
	Button share;
	EditText et;
	TextView name;
	ImageView photo;
	public static final String OAUTH_CALLBACK_HOST = "litestcalback";

	final LinkedInOAuthService oAuthService = LinkedInOAuthServiceFactory
            .getInstance().createLinkedInOAuthService(
                    Config.LINKEDIN_CONSUMER_KEY,Config.LINKEDIN_CONSUMER_SECRET, Config.scopeParams);
	final LinkedInApiClientFactory factory = LinkedInApiClientFactory
			.newInstance(Config.LINKEDIN_CONSUMER_KEY,
					Config.LINKEDIN_CONSUMER_SECRET);
	LinkedInRequestToken liToken;
	LinkedInApiClient client;
	LinkedInAccessToken accessToken = null;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		if( Build.VERSION.SDK_INT >= 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy); 
		}
		share = (Button) findViewById(R.id.share);
		name = (TextView) findViewById(R.id.name);
		et = (EditText) findViewById(R.id.et_share);
		login = (Button) findViewById(R.id.login);
		photo = (ImageView) findViewById(R.id.photo);

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// login with linked in 
				linkedInLogin();
			}
		});

		// share on linkedin
		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// creating an custom dialog 
				final Dialog dialog = new Dialog(LinkedInSampleActivity.this);
				dialog.setContentView(R.layout.custom_dailog_box);
				dialog.setTitle("LinkedIn sharing");
	 
				
				final EditText text = (EditText) dialog.findViewById(R.id.text);
				text.setText("Sample text");
					 
				Button dialogCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
				dialogCancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Toast.makeText(LinkedInSampleActivity.this,
								"cancel dialog",
								Toast.LENGTH_LONG).show();
					}
				});
				
				Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
				// if button is clicked, close the custom dialog
				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						/*Toast.makeText(FcebookActivity.this,
								"ok dialog",
								Toast.LENGTH_LONG).show();
						
						message=text.getText().toString();
						postStatusMessage();
						*/
						// get the text which is to be shared
						String share = text.getText().toString();
						if (null != share && !share.equalsIgnoreCase("")) {
							// creating an OAuthConsumer object
							OAuthConsumer consumer = new CommonsHttpOAuthConsumer(Config.LINKEDIN_CONSUMER_KEY, Config.LINKEDIN_CONSUMER_SECRET);
						    consumer.setTokenWithSecret(accessToken.getToken(), accessToken.getTokenSecret());
							DefaultHttpClient httpclient = new DefaultHttpClient();
							HttpPost post = new HttpPost("https://api.linkedin.com/v1/people/~/shares");
							try {
								consumer.sign(post);
							} catch (OAuthMessageSignerException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (OAuthExpectationFailedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (OAuthCommunicationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} // here need the consumer for sign in for post the share
							post.setHeader("content-type", "text/XML");
							String myEntity = "<share><comment>"+ share +"</comment><visibility><code>anyone</code></visibility></share>";
							try {
								post.setEntity(new StringEntity(myEntity));
								org.apache.http.HttpResponse response = httpclient.execute(post);
								Toast.makeText(LinkedInSampleActivity.this,
										"Shared sucessfully", Toast.LENGTH_SHORT).show();
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ClientProtocolException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else {
							Toast.makeText(LinkedInSampleActivity.this,
									"Please enter the text to share",
									Toast.LENGTH_SHORT).show();
						}
						
					}
				});
				
				
	 
				dialog.show();
				
				
				/*String share = et.getText().toString();
				if (null != share && !share.equalsIgnoreCase("")) {
					client = factory.createLinkedInApiClient(accessToken);
					client.postNetworkUpdate(share);
					et.setText("");
					Toast.makeText(LinkedInSampleActivity.this,
							"Shared sucessfully", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(LinkedInSampleActivity.this,
							"Please enter the text to share",
							Toast.LENGTH_SHORT).show();
				}*/
			}
		});
	}

	private void linkedInLogin() {
		ProgressDialog progressDialog = new ProgressDialog(
				LinkedInSampleActivity.this);
// creating a linked n dialog object for authenticating and allow the user to share co=ntent
		LinkedinDialog d = new LinkedinDialog(LinkedInSampleActivity.this,
				progressDialog);
		d.show();

		// set call back listener to get oauth_verifier value
		d.setVerifierListener(new OnVerifyListener() {
			@Override
			public void onVerify(String verifier) {
				try {
					Log.i("LinkedinSample", "verifier: " + verifier);
					// get the token 
					accessToken = LinkedinDialog.oAuthService
							.getOAuthAccessToken(LinkedinDialog.liToken,
									verifier);
					LinkedinDialog.factory.createLinkedInApiClient(accessToken);
					client = factory.createLinkedInApiClient(accessToken);
					// client.postNetworkUpdate("Testing by Mukesh!!! LinkedIn wall post from Android app");
					Log.i("LinkedinSample",
							"ln_access_token: " + accessToken.getToken());
					Log.i("LinkedinSample",
							"ln_access_token: " + accessToken.getTokenSecret());
					// get the profile information of login user 
					Person p = client.getProfileForCurrentUser();
					name.setText("Welcome " + p.getFirstName() + " "
							+ p.getLastName());
					name.setVisibility(0);
					login.setVisibility(4);
					share.setVisibility(0);
//					et.setVisibility(0);

				} catch (Exception e) {
					Log.i("LinkedinSample", "error to get verifier");
					e.printStackTrace();
				}
			}
		});

		// set progress dialog
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(true);
		progressDialog.show();
	}
}

