package com.gmolabs.polterguide.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by geoffmorris on 7/5/14.
 */
public class SetPreferenceActivity extends Activity {

    SharedPreferences p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();

//        p = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        p.getString(R.string.username, value);
    }

}