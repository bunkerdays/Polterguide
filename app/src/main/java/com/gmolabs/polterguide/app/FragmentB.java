package com.gmolabs.polterguide.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.Firebase;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class FragmentB extends android.support.v4.app.ListFragment {

    Firebase mRef;
    Firebase userRef;
    Firebase listRef;
    String mCurrentUser;
    SharedPreferences p;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Context mContext = getActivity().getApplicationContext();
        p = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        mCurrentUser = p.getString("username", "NO_USER");


        mRef = new Firebase("https://polterguide.firebaseio.com/");
        userRef = mRef.child("users/"+mCurrentUser);
        listRef = userRef.child("soundscapes");
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2",  "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" ,  "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
    }
}
