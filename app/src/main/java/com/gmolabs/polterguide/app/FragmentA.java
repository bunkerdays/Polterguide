package com.gmolabs.polterguide.app;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardView;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class FragmentA extends android.support.v4.app.Fragment {


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


        thumb.setDrawableResource(R.drawable.ic_action_mic_black);
        //Add thumbnail to a card
        card.addCardThumbnail(thumb);

        //Set the card inner text
        card.setTitle(getString(R.string.record));

        //Set card in the cardView
        CardView cardView = (CardView) getView().findViewById(R.id.recordCard);


        Log.d("POLTERGUIDE", "cardview created: " + cardView);
        cardView.setCard(card);

    }

}
