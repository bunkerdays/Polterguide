package com.gmolabs.polterguide.app;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardView;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class FragmentA extends android.support.v4.app.Fragment implements com.google.android.gms.location.LocationListener {

    GoogleMap mMap;
    Location mCurrentLocation;

    public FragmentA() {
        // Required empty public constructor
        super();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Context mContext = getActivity().getApplicationContext();

        //Create a Card
        Card card = new RecordCard(mContext);


        CardThumbnail thumb = new CardThumbnail(mContext);


//        thumb.setDrawableResource(R.drawable.ic_action_mic_black);
        //Add thumbnail to a card
        card.addCardThumbnail(thumb);

        //Set the card inner text
        card.setTitle(getString(R.string.record));

        //Set card in the cardView
        CardView cardView = (CardView) getView().findViewById(R.id.recordCard);





        Log.d("POLTERGUIDE", "cardview created: " + cardView);
        cardView.setCard(card);
        setUpMapIfNeeded();

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            SupportMapFragment mySupFrag = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map));

            Log.d("SupportFrag", mySupFrag.toString());
            mMap = mySupFrag.getMap();
            Log.d("mMap", mMap.toString());
            mMap.setMyLocationEnabled(true);
            Log.d("mMap", "myLocation enabled");

            // Check if we were successful in obtaining the map.
            if (mMap != null) {

                Log.d("mMap", "mMap != null. Calling getLocation()");
                // The Map is verified. It is now safe to manipulate the map.
//                mMap.setMyLocationEnabled(true);
                //pass map to parent
                ((MainActivity) getActivity()).setMap(mMap);
            }
        }
    }

//    private void getLocation() {
//
//        Log.d("mMap", "entered getLocation");
//        mCurrentLocation = mMap.getMyLocation();
//        Log.d("LOCO", mCurrentLocation.toString());
//        Log.d("LOCO", "Lat: "+mCurrentLocation.getLatitude()+", Lng: "+mCurrentLocation.getLongitude());
//
//    }

    @Override
    public void onLocationChanged(Location location) {
    }
}
