package com.gmolabs.polterguide.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by geoffmorris on 6/25/14.
 */
public class RecordCard extends Card {


//    protected TextView mTitle;
    protected TextView mRecTimeTitle;
    protected TextView mLocationTitle;
    protected RatingBar mRatingBar;
    protected String mRecTime;
    protected String mLocationString;

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

    public String getRecTime() {
        return mRecTime;
    }
    public void setRecTime(String t) {
        mRecTime = t;
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

        setRecTime("00:00:00");
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
//        mTitle = (TextView) parent.findViewById(R.id.card_main_inner_primary_title);
        mRecTimeTitle = (TextView) parent.findViewById(R.id.card_main_inner_secondary_title);
        mLocationTitle = (TextView) parent.findViewById(R.id.card_main_inner_tertiary_title);
//            mRatingBar = (RatingBar) parent.findViewById(R.id.carddemo_myapps_main_inner_ratingBar);

//        if (mTitle!=null)
//            mTitle.setText(R.string.record_title);

        if (mRecTimeTitle!=null)
            mRecTimeTitle.setText(mRecTime);


        if (mLocationTitle!=null)
            mLocationTitle.setText(R.string.record_location);

//            if (mRatingBar!=null)
//                mRatingBar.setNumStars(5);
//            mRatingBar.setMax(5);
//            mRatingBar.setRating(4.7f);

    }
}
