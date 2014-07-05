package com.gmolabs.polterguide.app;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by geoffmorris on 7/5/14.
 */

public class PrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}
