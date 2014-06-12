package com.gmolabs.polterguide.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Geoffrey Clark Morris on 6/9/14.
 */
public class MyAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 2;

    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment myFrag = null;
        if (position==0) {
            myFrag = new FragmentA();
        } else if(position==1) {
            myFrag = new FragmentB();
        } else if(position==2) {
            myFrag = new FragmentC();
        }
        return myFrag;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}