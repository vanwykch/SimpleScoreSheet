package com.vwapps.simplescoresheet;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

/**
 * A fragment representing a round
 */
public class FragmentRound extends Fragment
{
    private int mRoundNumber = 0;
    public static final String ARG_OBJECT = "object";

    public FragmentRound()
    {
    }

    /**
     * Override onCreateView to inflate round_layout.xml for
     * this round.  Populate it with data by calling populatePlayerList.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        mRoundNumber = args.getInt(ARG_OBJECT);

        View view = inflater.inflate(R.layout.round_layout, container, false);

        populatePlayerList(view);

        return view;
    }

    /**
     * populatePlayerList public entry point that uses getView()
     */
    public void populatePlayerList()
    {
        View view = this.getView();
        if (view != null)
        {
            populatePlayerList(view);
        }
    }
    /**
     * Refresh the list of players and their scores
     */
    private void populatePlayerList( View view)
    {

        CurrentGame curGame = CurrentGame.getTheGame();
        AdapterScoreDisplay adapter;
        final FragmentManager fm = getActivity().getSupportFragmentManager();

        ArrayList<PlayerStateForRound> psfr = curGame.getPlayerDataForRound(mRoundNumber);
        adapter = new AdapterScoreDisplay
                (view.getContext(), android.R.layout.simple_list_item_1, psfr );


        ListView listView = (ListView) view.findViewById(R.id.roundDataListView);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setLongClickable(true);

        //
        // Long Click
        //
        listView.setOnItemLongClickListener(new OnItemLongClickListener()
        {
            public boolean onItemLongClick(AdapterView<?> parent, View v,
                                           int position, long id)
            {
                CurrentGame curGame = CurrentGame.getTheGame();
                curGame.setCurPlayer(position, mRoundNumber);

                final DialogFragmentEditPlayer cnd = new DialogFragmentEditPlayer();
                cnd.show(fm, "change_name_tag");

                return true;
            }
        });

        //
        // Short Click
        //
        listView.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id)
            {
                CurrentGame curGame = CurrentGame.getTheGame();
                curGame.setCurPlayer(position, mRoundNumber);

                final DialogFragmentEnterScore esd = new DialogFragmentEnterScore();
                esd.show(fm, "enter_score_tag");
            }
        });

    }

}
