package com.gmolabs.polterguide.app;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    ActionBar actionBar;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar=getActionBar();
        try {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        } catch (Exception exception) {
            Log.d("polterguided", "couldn't set mode actionbartabs");
        }



        viewPager = (ViewPager) findViewById(R.id.pager);

        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.d("polterguide", "onPageScrolled at position" + position+" from position offset "+positionOffset+" with positionOffsetPixels "+positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
//                Log.d("polterguide", "onPageSelected at position"+position);
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state==ViewPager.SCROLL_STATE_IDLE) {
//                    Log.d("polterguide", "onPageScrollStateChanged IDLE");
                } else if(state==ViewPager.SCROLL_STATE_DRAGGING) {
//                    Log.d("polterguide", "onPageScrollStateChanged DRAGGING");
                } else if(state==ViewPager.SCROLL_STATE_SETTLING) {
//                    Log.d("polterguide", "onPageScrollStateChanged SETTLING");
                }
            }
        });

        ActionBar.Tab tab1 = actionBar.newTab();
        tab1.setText("Record");
        tab1.setTabListener(this);

        ActionBar.Tab tab2 = actionBar.newTab();
        tab2.setText("Browse");
        tab2.setTabListener(this);

//        ActionBar.Tab tab3 = actionBar.newTab();
//        tab3.setText("Tab 3");
//        tab3.setTabListener(this);

        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
//        actionBar.addTab(tab3);
    }


    //tablistener implementation
    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
//        Log.d("polterguide", "onTabSelected at position" + tab.getPosition()+" name "+tab.getText());
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
//        Log.d("polterguide", "onTabUnselected at position" + tab.getPosition()+" name "+tab.getText());

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
//        Log.d("polterguide", "onTabReselected at position" + tab.getPosition()+" name "+tab.getText());

    }

    //options menu implementation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}