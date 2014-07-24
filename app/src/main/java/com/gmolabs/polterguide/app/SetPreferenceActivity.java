package com.gmolabs.polterguide.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by geoffmorris on 7/5/14.
 */
public class SetPreferenceActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences p;
    boolean newUser = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();

        p = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        p.getString(R.string.username, value);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        p.registerOnSharedPreferenceChangeListener(this);
        if (p.getBoolean("autoflip", false)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        p.unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key) {
        String myUser = sharedPreferences.getString("username", "DEFAULT");
        String myPass = sharedPreferences.getString("password", "DEFAULT");
        if (sharedPreferences.getBoolean("autoflip", false)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if(newUser) {

            newUser = false;
            Firebase usersRef = new Firebase("https://polterguide.firebaseio.com/users/");
            Firebase userRef = usersRef.push();
            String userId = userRef.getName();

            Map<String, Object> toSet = new HashMap<String, Object>();
            toSet.put("username", myUser);
            toSet.put("password", myPass);

            userRef.setValue(toSet);

            SharedPreferences.Editor editor1 = sharedPreferences.edit();
            editor1.putString("userId", userId);
            editor1.commit(); //add unique firebase id to shared preferences for easy reference
        }// do stuff

    }


}