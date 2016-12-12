package com.vwapps.simplescoresheet;

/**
 * Created by chris on 12/10/2016.
 */

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * A FragmentStatePagerAdapter that returns a fragment
 * representing a round
 */
public class RoundCollectionPagerAdapter extends FragmentStatePagerAdapter {

    private int mNumRounds = 0;

    @SuppressLint("UseSparseArrays")
    private Map<Integer,FragmentRound> mFragmentMap = new HashMap<Integer,FragmentRound>();
    public RoundCollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addRoundFragment()
    {
        mNumRounds++;
        CurrentGame.addRoundToGame();
        this.notifyDataSetChanged();
    }

    public void populateAll()
    {
        for (Integer key : mFragmentMap.keySet())
        {
            mFragmentMap.get(key).populatePlayerList();
        }
    }


    @Override
    public Fragment instantiateItem(ViewGroup container, int position)
    {
        Fragment fragment = new FragmentRound();
        Bundle args = new Bundle();
        args.putInt(FragmentRound.ARG_OBJECT, position + 1);
        fragment.setArguments(args);

        // If this round has a fragment in the list,
        // remove the old one first
        if (mFragmentMap.get(position) != null)
        {
            mFragmentMap.remove(position);
        }
        mFragmentMap.put(position,(FragmentRound)fragment);

        super.instantiateItem(container, position);

        return fragment;

    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        mFragmentMap.remove(position);
        super.destroyItem(container, position,object);
    }


    @Override
    public Fragment getItem(int position)
    {
        FragmentRound fr= mFragmentMap.get(position);
        if (fr != null)
            fr.populatePlayerList();
        return mFragmentMap.get(position);
    }

    @Override
    public int getCount()
    {
        return mNumRounds;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Round " + (position + 1);
    }
}

