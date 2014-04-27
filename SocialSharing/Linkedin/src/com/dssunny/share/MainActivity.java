package com.dssunny.share;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try  {  
        	  
          PackageInfo info = getPackageManager().  
        	           getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);

        	      for (Signature signature : info.signatures) {

        	          MessageDigest md = MessageDigest.getInstance("SHA");
        	          md.update(signature.toByteArray());
        	          Log.d("====Hash Key===",Base64.encodeToString(md.digest(), 
        	                   Base64.DEFAULT));

        	      }

        	  } catch (NameNotFoundException e) {

        	      e.printStackTrace();

        	  } catch (NoSuchAlgorithmException ex) {

        	      ex.printStackTrace();

        	  }
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
