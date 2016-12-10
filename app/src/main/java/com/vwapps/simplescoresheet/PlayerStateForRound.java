package com.vwapps.simplescoresheet;

/**
 *  A class to hold data for each player that gets displayed
 *  in the round view's player list
 */
public class PlayerStateForRound implements Comparable<PlayerStateForRound>
{
	private int mRound = 0;
	private int mTotalScore = 0;
	private int mRoundScore = 0;
	private boolean mScoreEntered = false;
	private Player mPlayer = null;

	PlayerStateForRound(Player player, int round)
	{
		mPlayer = player;
		mRound = round;
	}

	///////////////////////////////////////////////////////////////////////////
	public void setPlayer(Player player)   { mPlayer = player;	}
	public void setTotalScore(int inScore) { mTotalScore = inScore;	}
	public void setRoundScore(int inScore)
	{
		mRoundScore = inScore;
		mScoreEntered = true;
	}
	public void addToRoundScore(int inScore)
	{
		mRoundScore += inScore;
		mScoreEntered = true;
	}
	///////////////////////////////////////////////////////////////////////////
	
	public int getRound()      { return mRound; }
	public Player getPlayer()  { return mPlayer;  }
	public int getTotalScore() { return mTotalScore;	}
	public int getRoundScore() { return mRoundScore;	}
	
	public String getRoundScoreString()
	{
		String retVal = " ";
		if ( mScoreEntered )
		{
			retVal = Integer.toString(mRoundScore);
		}
		return retVal;
	}
	///////////////////////////////////////////////////////////////////////////
	
	public boolean scoreEntered()
	{
		return mScoreEntered;
	}
	public int compareTo(PlayerStateForRound psfr)
	{
	    return (mTotalScore < psfr.mTotalScore ? -1 : (mTotalScore == psfr.mTotalScore ? 0 : 1) );
	}
}
