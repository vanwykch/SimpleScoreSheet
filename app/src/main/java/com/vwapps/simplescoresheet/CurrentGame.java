package com.vwapps.simplescoresheet;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 *  Singleton class for the current game being view/modified/etc.
 */
public class CurrentGame {

	private ArrayList<Player> mPlayers = new ArrayList<Player>();
	private ArrayList<Round> mRounds = new ArrayList<Round>();
	private int mCurRoundNumber = 0;
	private Player mCurPlayer = null;
	private boolean mReverseSort = false;
	private boolean mShowNameExists = false;
	
	private static CurrentGame theGame;
	
	private CurrentGame()
	{
	}
	
	public static CurrentGame getTheGame()
	{
		if (theGame == null)
		{
			theGame = new CurrentGame();
		}
		return theGame;
	}
	public static void addRoundToGame()	{ getTheGame().addRound();	}
	
	/**
	 *  toggle sorting score high-to-low or low-to-high
	 */
	public boolean reverseSort()
	{
		mReverseSort = ! mReverseSort;
		return mReverseSort;
	}
    /**
     *  Pass in the postion in the round list and round number to set the current player
     */
	public void setCurPlayer(int pos, int roundNumber)
	{
		mCurPlayer = this.mRounds.get(roundNumber - 1).getPlayerByPos(pos);
		mCurRoundNumber = roundNumber;
	}
	
    /**
     *  Delete a player completely from the game
     */
	public void deletePlayer(String name)
	{
	    Player goner = null;
		for (Player player : mPlayers)
		{
			if (name == player.getName()) 
			{
				goner = player;
			}
		}
		if (goner != null) mPlayers.remove(goner);
	}
	
	private Round getRoundByNumber(int number)
	{
		for(Round r : mRounds)
		{
			if(r.getRoundNumber() == number)
			{
				return r;
			}
		}
		return null;
	}
	
    /**
     *  set score for player by position in array
     */
	public void setScoreForCurPlayerAndCurPosition(int score)
	{
		mCurPlayer.setRoundScore(mCurRoundNumber, score);
	}
	
	public ArrayList<PlayerStateForRound> getPlayerDataForCurRound()
	{
	    return getPlayerDataForRound(mCurRoundNumber);
    }
    public ArrayList<PlayerStateForRound> getPlayerDataForRound(int roundNumber)
    {
        int round = roundNumber;
        if (round == 0)
        {
            this.addRound();
        }
        Round r = getRoundByNumber(round);
        if(r != null)
        {
            return r.getPlayerList(this.mReverseSort);
        }
        
        // return an empty array
        return new ArrayList<PlayerStateForRound>();
    }
	
	public void addRound()
	{
		mRounds.add(new Round(mPlayers, mRounds.size() + 1) );
		mCurRoundNumber = mRounds.size();
	}
    public void addPlayer(String name)  { mPlayers.add(new Player(name));   }
	
	public String getCurName()       { return this.mCurPlayer.getName();	}
    public PlayerStateForRound getCurPlayerStateForRound(){
        return mCurPlayer.getPlayerStateForRound(mCurRoundNumber);
    }
	public Integer getCurPlayerCurRoundScore() {
        Integer score = getCurRoundObject().getPlayersScore(mCurPlayer);
        return score;
    }
	public Round getCurRoundObject() { return getRoundByNumber(mCurRoundNumber);	}
	public int getNumRounds()	     { return mRounds.size(); }
    public ArrayList<Player> getPlayerList()  { return mPlayers;  }
	
	public void clearAllScores()
	{
		mRounds.clear();
		for (Player player : mPlayers)
		{
		    player.clearScores();
		}
		mCurRoundNumber = 0;
		mCurPlayer = null;
        mReverseSort = false;
        mShowNameExists = false;
	}
	
    /**
     *  Clear all the scores and players
     */
	public void clearEverything ()
	{
	    clearAllScores();
		mPlayers.clear();
	}
	
	public boolean doesNameExist(String newName)
	{
		for (Player player : mPlayers)
		{
			if (player.getName().equals(newName))
			{
				return true;
			}
		}
		return false;
	}
	
    public void setCurName(String newName){ mCurPlayer.setName(newName); }
	public void setShowNameExists() 	{ this.mShowNameExists = true;	}
	public void setDontShowNameExists()	{ this.mShowNameExists = false;	}
	public boolean showNameExists()  	{ return this.mShowNameExists;	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// save and restore the current game
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     *  Save the current game to a SharedPreferences file
     */
	public void saveCurGame(Activity act)
	{
	    SharedPreferences sharedPref = act.getPreferences(Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = sharedPref.edit();
	    
	    int namesCount = 0;
	    
	    // store data for each player
	    for (Player player : this.mPlayers)
	    {
		    editor.putString("name" + String.valueOf(namesCount), player.getName());
		    namesCount++;
		    
		    // store the score for every round for this player
		    for (Round round : this.mRounds)
		    {
		    	editor.putInt("score" + player.getName() + String.valueOf(round.getRoundNumber()), 
		    			      round.getPlayersScore(player) );
		    }
	    }
	    editor.putInt("numRounds", this.getNumRounds() );
	    editor.putInt("numNames", namesCount);
	    editor.commit();
	}
	
    public interface AddRoundListener {
        public int addRound();
        public void createRoundPagerObjects(boolean loadingSavedGame);
    }

    /**
     *  Restore the last saved game
     */
	public void restoreCurGame(Activity act)
	{
        AddRoundListener mainActivity;
        
        // Verify that the activity implements the callback interface
        try {
            mainActivity = (AddRoundListener) act;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(act.toString()
                    + " must implement AddPlayerNoticeDialogListener");
        }

	    if (mPlayers.size() == 0)
	    {
		    SharedPreferences sharedPref = act.getPreferences(Context.MODE_PRIVATE);
		    
		    int numNames = sharedPref.getInt("numNames", 
	                         0); // default value of 0, in case key is not present
		    if(numNames > 0)
            {
                // start with a fresh pager with no rounds
                clearEverything();
                mainActivity.createRoundPagerObjects(true);
            }

		    // Add all the players and rounds
		    for (int ndx = 0; ndx < numNames; ++ndx)
		    {
		    	String name = sharedPref.getString("name" + String.valueOf(ndx), 
		    			                           "No Name Found");
		    	addPlayer(name);
		    }		    
	    	int numRounds = sharedPref.getInt("numRounds", 
                    0); // default value of 0, in case key is not present
		    for (int round = 1; round <= numRounds; ++round)
		    {
		        mainActivity.addRound();
		    }

		    // add scores for each round
		    for (Player player : mPlayers)
		    {
		    	// set score for each round
			    for (int round = 1; round <= numRounds; ++round)
			    {
			    	int score = sharedPref.getInt("score" + player.getName() + String.valueOf(round), 
	                           0); // return 0 if no score found for round
			    	
					Round r = getRoundByNumber(round);
					
					if (r != null)
					{
					    // Leave blank if last round and score is 0
					    if (! (round == numRounds
					        && score == 0) )
					        player.setRoundScore(r.getRoundNumber(), score);   
					}
					
			    }
		    }
	    }
	}
}