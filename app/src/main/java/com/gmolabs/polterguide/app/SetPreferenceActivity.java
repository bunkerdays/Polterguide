package com.gmolabs.polterguide.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.firebase.client.Firebase;

/**
 * Created by geoffmorris on 7/5/14.
 */
public class SetPreferenceActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences p;

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
        Firebase usersRef = new Firebase("https://polterguide.firebaseio.com/users/");
        Firebase userRef = usersRef.child(myUser);
        userRef.child("pwd").setValue(myPass);
        // do stuff
    }


}