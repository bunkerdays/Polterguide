package com.gmolabs.polterguide.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener, GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private static final int ZOOM_DEFAULT = 20;
    ActionBar actionBar;
    ViewPager viewPager;
    Chronometer mChrono;
    LocationClient mLocationClient;
    // Global variable to hold the current location
    // Define an object that holds accuracy and frequency parameters
    LocationRequest mLocationRequest;
    Location mCurrentLocation;
    boolean mUpdatesRequested;
    boolean mLocSet = false;

    TextView mAddress;
    Firebase mFirebase;

    GoogleMap mMap;

    FragmentManager fm;


    String mCurrentUser = "";

    String mCurrentAddress = "";

    SharedPreferences mySharedPreferences;

    Firebase usersRef;
    Firebase userRef;
    Firebase soundscapeRef;
    Firebase recRef;
    Firebase urlRef;
    private int nRecs = 0;



    // Global constants
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    private static final int MAX_ZOOM_OFFSET = 2;

    /*
         * Called when the Activity becomes visible.
         */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
        //launch the login screen
        mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        boolean my_checkbox_preference = mySharedPreferences.getBoolean("checkbox_preference", false);
//        prefCheckBox.setChecked(my_checkbox_preference);
//
        mCurrentUser = mySharedPreferences.getString("username", "NO_USER");
        if(mCurrentUser=="NO_USER"||mCurrentUser=="") {
            LoginDialogFragment mLoginDialog = new LoginDialogFragment();
            mLoginDialog.setCancelable(false);
            mLoginDialog.show(fm, "dialog_signin");
        } else {

        }

        //setUpMapIfNeeded();
    }

    public void setCurrentUser(String u) {
        mCurrentUser = u;
    }

    public void setMap(GoogleMap newMap) {
        mMap = newMap;
    }

    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mySharedPreferences.getBoolean("autoflip", false)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
    @Override
    protected void onRestart() {
        super.onResume();
        if (mySharedPreferences.getBoolean("autoflip", false)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }




    // Define the callback method that receives location updates
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        // Report to the UI that the location was updated
//        String msg = "Updated Location: " +
//                Double.toString(location.getLatitude()) + "," +
//                Double.toString(location.getLongitude());
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        try {
            LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            if(!mLocSet) {
                mLocSet = true;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, ZOOM_DEFAULT));
            }
        } catch(Throwable e) {
//            Toast.makeText(this, "initializing map first...", Toast.LENGTH_SHORT).show();
        } finally {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fm = getSupportFragmentManager();

        actionBar = getActionBar();
        try {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        } catch (Exception exception) {
            Log.d("polterguided", "couldn't config action bar");
        }

        mChrono = (Chronometer) findViewById(R.id.chronometer1);


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
                if (state == ViewPager.SCROLL_STATE_IDLE) {
//                    Log.d("polterguide", "onPageScrollStateChanged IDLE");
                } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
//                    Log.d("polterguide", "onPageScrollStateChanged DRAGGING");
                } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
//                    Log.d("polterguide", "onPageScrollStateChanged SETTLING");
                }
            }
        });

        ActionBar.Tab tab1 = actionBar.newTab();
//        tab1.setText("Record");
        tab1.setIcon(R.drawable.ic_action_mic);
        tab1.setTabListener(this);

        ActionBar.Tab tab2 = actionBar.newTab();
//        tab2.setText("Browse");
        tab2.setIcon(R.drawable.ic_action_headphones);
        tab2.setTabListener(this);

//        ActionBar.Tab tab3 = actionBar.newTab();
//        tab3.setText("Tab 3");
//        tab3.setTabListener(this);

        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
//        actionBar.addTab(tab3);

        forceTabs();

        //do geo stuff
        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(errorCode, this, 0).show();
        }


        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        mLocationClient = new LocationClient(this, this, this);
        mUpdatesRequested = true;


        //firebase stuff
        // Create a reference to a Firebase location
        mFirebase = new Firebase("https://polterguide.firebaseio.com/");
        usersRef = mFirebase.child("users");
        // Write data to Firebase

        // Read data and react to changes
        mFirebase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snap) {
                System.out.println(snap.getName() + " -> " + snap.getValue());
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });


        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

    }



    public void startRecording() {
        userRef = usersRef.child(mCurrentUser);
        if(soundscapeRef == null) {

            //set count of recordings to 0
            nRecs = 0;
            soundscapeRef = userRef.child("TempSoundscape");
        }
        nRecs++;

        String recName = "TempRecording"+nRecs;
        recRef = soundscapeRef.child(recName);
        Firebase el = recRef.child("el");
        Firebase lat = recRef.child("lat");
        Firebase lng = recRef.child("lng");
        Firebase url = recRef.child("url");
        el.setValue(mCurrentLocation.getAltitude());
        lat.setValue(mCurrentLocation.getLatitude());
        lng.setValue(mCurrentLocation.getLongitude());
        url.setValue(recName);


    }

    public void finishRecording() {
        //show undo and done buttons if they're not already there


        View db = findViewById(R.id.doneButton);
        db.setVisibility(View.VISIBLE);


        View ub = findViewById(R.id.undoButton);
        ub.setVisibility(View.VISIBLE);
        //wrap stuff up...
        //
        //remove fb refs...
        //soundscapeRef = null;
        //recRef = null;
        //nRecs = 0;
    }



    public void onDoneClicked(View view) {
        //save this as a soundscape
        //copy TempSoundscape to new ref based on address and timestamp, delete TempSoundscape
        //TODO edit details cardflip to set title
        // TODO and ultimately trim soundfile...



        SimpleDateFormat s = new SimpleDateFormat("MMMddyyyyhhmmss");
        String format = s.format(new Date());

        String soundscapeName = mCurrentAddress+", "+format;
        Toast.makeText(this, soundscapeName, Toast.LENGTH_LONG).show();


        Firebase newRef = userRef.child(soundscapeName);
        Firebase oldRef = userRef.child("TempSoundscape");

        //TODO: copy soundscape
        //

        //remove old soundscape
        oldRef.removeValue();


        Log.d("DONE", "duplicated "+soundscapeName+" ref from TempSoundscape");
//        soundscapeRef.removeValue();
//        Log.d("polterguide", "removed temp ref");


    }


    public void onUndoClicked(View view) {
        //save this as a soundscape
        //copy TempSoundscape to new ref based on address and timestamp, delete TempSoundscape
        //TODO edit details cardflip to set title
        // TODO and ultimately trim soundfile...

        Toast.makeText(this, "undo clicked", Toast.LENGTH_LONG).show();


//        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
//        String format = s.format(new Date());
//
//        String soundscapeName = mCurrentAddress+", "+format;
//        userRef.child(soundscapeName).setValue(soundscapeRef);
//        soundscapeRef.removeValue();


    }


    public void onToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;



        mChrono = (Chronometer) findViewById(R.id.chronometer1);

        if (on) {
            // Enable vibrate
//            CharSequence text = "Recording";
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
            startRecording();
            mChrono.setBase(SystemClock.elapsedRealtime());
            mChrono.start();

           // getLocation();

        } else {
//            // Disable vibrate
//            CharSequence text = "Finished";
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();

              mChrono.stop();
              finishRecording();
        }
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

            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SetPreferenceActivity.class);
            startActivityForResult(intent, 0);



            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
//            ...
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */
//                        ...
                        break;
                    default:
                        loadPref();
                }
//                ...
        }
//        ...
    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Get the error code
            int errorCode = resultCode;
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    errorCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(
                        getSupportFragmentManager(),
                        "Location Updates");
            }
        }
        return false;
    }

    private boolean isGooglePlay(){
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if(status == ConnectionResult.SUCCESS){
            return true;

        }else{
            ((Dialog)GooglePlayServicesUtil.getErrorDialog(status, this, 10)).show();
            //Toast.makeText(this, "nai", Toast.LENGTH_LONG).show();
        }
        return false;

    }

    private void loadPref(){
//        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//
////        boolean my_checkbox_preference = mySharedPreferences.getBoolean("checkbox_preference", false);
////        prefCheckBox.setChecked(my_checkbox_preference);
////
//        String defValue = "Ghost# ";
//        int r = (int) (Math.random()*100);
//        defValue += r;
//        SharedPreferences.Editor editor1 = mySharedPreferences.edit();
//        editor1.putString("username",defValue);
//        editor1.commit();
//        String my_edittext_preference = mySharedPreferences.getString("username", defValue);
//        Toast.makeText(this, my_edittext_preference, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConfigurationChanged(final Configuration config) {
        super.onConfigurationChanged(config);
        forceTabs(); // Ensure tabs are still forced after orientation changes.
    }

    // This is where the magic happens!
    public void forceTabs() {
        try {
            final ActionBar actionBar = getActionBar();
            final Method setHasEmbeddedTabsMethod = actionBar.getClass()
                    .getDeclaredMethod("setHasEmbeddedTabs", boolean.class);
            setHasEmbeddedTabsMethod.setAccessible(true);
            setHasEmbeddedTabsMethod.invoke(actionBar, true);
        }
        catch(final Exception e) {
            // Handle issues as needed: log, warn user, fallback etc
            // Alternatively, ignore this and default tab behaviour will apply.
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Display the connection status
//        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

        mCurrentLocation = mLocationClient.getLastLocation();

        // Display the connection status
//        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        // If already requested, start periodic updates
        if (mUpdatesRequested) {
            mLocationClient.requestLocationUpdates(mLocationRequest, this);
        }

// Ensure that a Geocoder services is available
        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.GINGERBREAD
                &&
                Geocoder.isPresent()) {
            // Show the activity indicator
//            mActivityIndicator.setVisibility(View.VISIBLE);
            /*
             * Reverse geocoding is long-running and synchronous.
             * Run it on a background thread.
             * Pass the current location to the background task.
             * When the task finishes,
             * onPostExecute() displays the address.
             */
            (new GetAddressTask(this)).execute(mCurrentLocation);
        }
//        Toast.makeText(this, mCurrentLocation.toString(), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onDisconnected() {
        // Display the connection status
//        Toast.makeText(this, "Disconnected. Please re-connect.",
//                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
//            showErrorDialog(connectionResult.getErrorCode());

            int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
            if (errorCode != ConnectionResult.SUCCESS) {
                GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
            }
        }
    }

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    /**
     * A subclass of AsyncTask that calls getFromLocation() in the
     * background. The class definition has these generic types:
     * Location - A Location object containing
     * the current location.
     * Void     - indicates that progress units are not used
     * String   - An address passed to onPostExecute()
     */
    private class GetAddressTask extends
            AsyncTask<Location, Void, String> {
        Context mContext;

        public GetAddressTask(Context context) {
            super();
            mContext = context;
        }

        /**
         * Get a Geocoder instance, get the latitude and longitude
         * look up the address, and return it
         *
         * @locations locations One or more Location objects
         * @return A string containing the address of the current
         * location, or an empty string if no address can be found,
         * or an error message
         */
        @Override
        protected String doInBackground(Location... locations) {Geocoder geocoder =
                new Geocoder(mContext, Locale.getDefault());
            // Get the current location from the input parameter list
            Location loc = locations[0];
            // Create a list to contain the result address
            List<Address> addresses = null;
            try {
                /*
                 * Return 1 address.
                 */
                addresses = geocoder.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
            } catch (IOException e1) {
                Log.e("LocationSampleActivity",
                        "IO Exception in getFromLocation()");
                e1.printStackTrace();
                return ("IO Exception trying to get address");
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments " +
                        Double.toString(loc.getLatitude()) +
                        " , " +
                        Double.toString(loc.getLongitude()) +
                        " passed to address service";
                Log.e("LocationSampleActivity", errorString);
                e2.printStackTrace();
                return errorString;
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                Address address = addresses.get(0);
                /*
                 * Format the first line of address (if available),
                 * city, and country name.
                 */
                String addressText = String.format(
                        "%s, %s",
                        // If there's a street address, add it
//                        address.getMaxAddressLineIndex() > 0 ?
//                                address.getAddressLine(0) : "",
                        // Locality is usually a city
                        address.getLocality(),
                        // The country of the address
                        address.getCountryName());
                // Return the text
                return addressText;
            } else {
                return "No address found";
            }
        }

        /**
         * A method that's called once doInBackground() completes. Turn
         * off the indeterminate activity indicator and set
         * the text of the UI element that shows the address. If the
         * lookup failed, display the error message.
         */
        @Override
        protected void onPostExecute(String address) {
            // Set activity indicator visibility to "gone"
//            mActivityIndicator.setVisibility(View.GONE);
            // Display the results of the lookup.
//            Toast.makeText(mContext, address, Toast.LENGTH_SHORT).show();
//
            mAddress = (TextView) findViewById(R.id.locationTitle);
            mAddress.setText(address);
            mCurrentAddress = address;
        }
    }
}