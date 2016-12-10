package com.vwapps.simplescoresheet;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements
        DialogFragmentEnterScore.NoticeDialogListener,
        DialogFragmentAddPlayer.NoticeDialogListener,
        DialogFragmentNewGame.NoticeDialogListener,
        CurrentGame.AddRoundListener
{

    //    private AdView adView;
    private RoundCollectionPagerAdapter mRoundCollectionPagerAdapter;
    private ViewPager mViewPager;


    /**
     * Create the main (and only) activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_layout);


//      LinearLayout layout = (LinearLayout)findViewById(R.id.main_linear_layout);

        //
        //  Ad stuff
        //
//      AdRequest adReq = new AdRequest();
//      adReq.addTestDevice(AdRequest.TEST_EMULATOR);
//      //        adReq.addTestDevice(testDevice);
//
//      String unit_id = "ca-app-pub-4566924390439368/1341963930";
//      adView = new AdView(this, AdSize.BANNER, unit_id);
//      adView.loadAd(adReq);
//      layout.addView(adView);

        //
        // rounds pager
        //

        createRoundPagerObjects(false);
    }

    /**
     * Create the pager for round dialog fragments
     *
     * interface addRoundListener.createRoundPagerObjects
     * This used to add the round page without first creating a round.
     */
    public void createRoundPagerObjects(boolean loadingSavedGame)
    {
        // Create an adapter that when requested, will return a fragment representing an object in
        // the collection.
        //
        // ViewPager and its adapters use support library fragments, so we must use
        // getSupportFragmentManager.
        mRoundCollectionPagerAdapter = new RoundCollectionPagerAdapter(getSupportFragmentManager());

        if (CurrentGame.getTheGame().getNumRounds() == 0
                && ! loadingSavedGame )
        {
            mRoundCollectionPagerAdapter.addRoundFragment();
        }

        // Set up the ViewPager, attaching the adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mRoundCollectionPagerAdapter);
    }

    /**
     * Show add player dialog
     */
    public void showAddPlayer(View view)
    {
        final DialogFragmentAddPlayer apd = new DialogFragmentAddPlayer();
        apd.show(getSupportFragmentManager(), "enter_score_tag");
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
// overrides
///////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onResume()
    {
        super.onResume();
        populateAllRoundFragments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        if (hasFocus)
        {
            populateAllRoundFragments();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_game:
                DialogFragmentNewGame ngd = new DialogFragmentNewGame();
                ngd.show(getSupportFragmentManager(), "hi");
                return true;
            case R.id.action_reverse_sort:

                if (CurrentGame.getTheGame().reverseSort())
                {
                    Toast.makeText(this, "Sorting low to high", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, "Sorting high to low", Toast.LENGTH_LONG).show();
                }

                return true;
            case R.id.action_show_about:

                DialogFragmentAbout about = new DialogFragmentAbout();
                about.show(getSupportFragmentManager(), "hi");
                return true;

            case R.id.action_show_help:

                DialogFragmentHelp help = new DialogFragmentHelp();
                help.show(getSupportFragmentManager(), "hi");
                return true;


            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart()
    {
        super.onStart();  // Always call the superclass method first
        CurrentGame.getTheGame().restoreCurGame(this);
    }
    @Override
    protected void onStop()
    {
        super.onStop();  // Always call the superclass method first
        CurrentGame.getTheGame().saveCurGame(this);
    }

    @Override
    public void onDestroy() {
//        if (adView != null) {
//            adView.destroy();
//        }
        super.onDestroy();

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
// pop-up dialog callbacks
///////////////////////////////////////////////////////////////////////////////////////////////////
    public void onDialogOkClickAddPlayer(DialogFragment dialog)
    {
        //TODO use 'dialog' to pass data?

        if (CurrentGame.getTheGame().showNameExists())
        {
            Toast.makeText(this, "Name Exists!", Toast.LENGTH_LONG).show();
            CurrentGame.getTheGame().setDontShowNameExists();
        }
    }

    public void onEnterScoreOkClick(DialogFragment dialog)
    {
        CurrentGame curGame = CurrentGame.getTheGame();
        if (curGame.getNumRounds() == curGame.getCurRoundObject().getRoundNumber())
        {
            // This is the last round,
            // so add a new round if all scores are entered.
            if ( curGame.getCurRoundObject().allScoresEntered())
            {
                mRoundCollectionPagerAdapter.addRoundFragment();
            }
        }
    }

    public void onNewGameOkClick(DialogFragment dialog)
    {
        createRoundPagerObjects(false);
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
// round fragment pager
///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * interface addRoundListener.addRound  This used to add a round from CurrentGame so that
     * a round fragment gets added to the pager when a round is added.
     */
    public void addRound()
    {
        mRoundCollectionPagerAdapter.addRoundFragment();
    }

    /**
     * A FragmentStatePagerAdapter that returns a fragment
     * representing a round
     */
    public static class RoundCollectionPagerAdapter extends FragmentStatePagerAdapter {

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
            mFragmentMap.get(position).populatePlayerList();
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

    private void populateAllRoundFragments()
    {
        if (mRoundCollectionPagerAdapter != null)
        {
            mRoundCollectionPagerAdapter.populateAll();
        }
    }

}
