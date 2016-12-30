package com.vwapps.simplescoresheet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
public class MainActivity extends AppCompatActivity
        implements
        DialogFragmentEnterScore.NoticeDialogListener,
        DialogFragmentAddPlayer.AddPlayerNoticeDialogListener,
        DialogFragmentNewGame.NoticeDialogListener,
        CurrentGame.AddRoundListener {

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

        //
        // rounds pager
        //

        createRoundPagerObjects(false);


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
//      LinearLayout layout = (LinearLayout)findViewById(R.id.main_linear_layout);
//      layout.addView(adView);

    }

    /**
     * Creates the pager for round dialog fragments
     *
     * interface addRoundListener.createRoundPagerObjects
     * This used to add the round page without first creating a round.
     */
    public void createRoundPagerObjects(boolean loadingSavedGame) {
        // Create an adapter that when requested, will return a fragment representing an object in
        // the collection.
        //
        // ViewPager and its adapters use support library fragments, so we must use
        // getSupportFragmentManager.
        mRoundCollectionPagerAdapter = new RoundCollectionPagerAdapter(getSupportFragmentManager());

        if (CurrentGame.getTheGame().getNumRounds() == 0
                && !loadingSavedGame) {
            mRoundCollectionPagerAdapter.addRoundFragment();
        }

        // Set up the ViewPager, attaching the adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mRoundCollectionPagerAdapter);
    }

    /**
     * Shows add player dialog
     */
    public void showAddPlayer(View view) {
        final DialogFragmentAddPlayer apd = new DialogFragmentAddPlayer();
        apd.show(getSupportFragmentManager(), "enter_score_tag");
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
// overrides
///////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onResume() {
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
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
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

                if (CurrentGame.getTheGame().reverseSort()) {
                    Toast.makeText(this, R.string.sort_lo_to_hi, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, R.string.sort_hi_to_lo, Toast.LENGTH_LONG).show();
                }

                return true;
            case R.id.add_round:

                int newRound = addRound();
                Context c = this.getApplicationContext();
                String toastString = c.getString(R.string.added_round) + " " +  newRound;
                Toast.makeText(this,  toastString, Toast.LENGTH_LONG).show();

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
    public void onDestroy() {
//        if (adView != null) {
//            adView.destroy();
//        }
        super.onDestroy();

    }
//    public void gameSetup(View view) {
//        Intent intent = new Intent(this, DisplayMessageActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
//    }
///////////////////////////////////////////////////////////////////////////////////////////////////
// pop-up dialog callbacks
///////////////////////////////////////////////////////////////////////////////////////////////////
    public void addPlayerOK(DialogFragment dialog) {
        //TODO use 'dialog' to pass data?

        if (CurrentGame.getTheGame().showNameExists()) {
            Toast.makeText(this, "Name Exists!", Toast.LENGTH_LONG).show();
            CurrentGame.getTheGame().setDontShowNameExists();
        }
    }

    /**
     * DialogFragmentEnterScore
     */
    public void enterScoreOK(DialogFragment dialog) {
        CurrentGame curGame = CurrentGame.getTheGame();
        if (curGame.getNumRounds() == curGame.getCurRoundObject().getRoundNumber()) {
            // This is the last round,
            // so add a new round if all scores are entered.
            if (curGame.getCurRoundObject().allScoresEntered()) {
                addRound();
            }
        }
    }

    public void newGameOK(DialogFragment dialog) {
        createRoundPagerObjects(false);
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
// round fragment pager
///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * interface addRoundListener.addRound  This used to add a round from CurrentGame so that
     * a round fragment gets added to the pager when a round is added.
     */
    public int addRound() {
        mRoundCollectionPagerAdapter.addRoundFragment();
        int roundNumber = CurrentGame.getTheGame().getCurRoundObject().getRoundNumber();
        mViewPager.setCurrentItem(roundNumber - 1);
        return roundNumber;
    }


    private void populateAllRoundFragments() {
        if (mRoundCollectionPagerAdapter != null) {
            mRoundCollectionPagerAdapter.populateAll();
        }
    }

}
