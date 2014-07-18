package com.gmolabs.polterguide.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.RatingBar;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by geoffmorris on 6/25/14.
 */
public class RecordCard extends Card {


//    protected TextView mTitle;
    protected TextView mLocationTitle;
    protected RatingBar mRatingBar;
    protected String mLocationString;
    public Chronometer mChrono;

    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public RecordCard(Context context) {
        this(context, R.layout.card_record_inner_content);
    }

    /**
     *
     * @param context
     * @param innerLayout
     */
    public RecordCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    /**
     * Init
     */
    private void init(){

        //No Header

        //Set a OnClickListener listener
//            setOnClickListener(new Card.OnCardClickListener() {
//                @Override
//                public void onClick(Card card, View view) {
//                    Toast.makeText(getContext(), "Click Listener card=", Toast.LENGTH_LONG).show();
//                }
//            });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
//        mTitle = (TextView) parent.findViewById(R.id.card_main_inner_primary_title);
//        mRecTimeTitle = (TextView) parent.findViewById(R.id.card_main_inner_secondary_title);
            mLocationTitle = (TextView) parent.findViewById(R.id.locationTitle);


        // Getting reference to the SupportMapFragment of activity_main.xml
          //  SupportMapFragment fm = (SupportMapFragment) parent.findViewById(R.id.map);
         //   Log.d("LOCO", "mySupFrag: " + fm.toString());
//            mRatingBar = (RatingBar) parent.findViewById(R.id.carddemo_myapps_main_inner_ratingBar);

//        if (mTitle!=null)
//            mTitle.setText(R.string.record_title);

//        if (mRecTimeTitle!=null)
//            mRecTimeTitle.setText(mRecTime);


        if (mLocationTitle!=null)
            mLocationTitle.setText(R.string.record_location);

//            if (mRatingBar!=null)
//                mRatingBar.setNumStars(5);
//            mRatingBar.setMax(5);
//            mRatingBar.setRating(4.7f);

    }
}
