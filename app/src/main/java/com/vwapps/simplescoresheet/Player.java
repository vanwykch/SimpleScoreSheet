package com.vwapps.simplescoresheet;

import java.util.HashMap;
import java.util.Map;


/**
 *  A player in the game
 */
public class Player {
    
    private String mName;
    private Map<Integer,PlayerStateForRound> mRoundScores = new HashMap<Integer,PlayerStateForRound>();
    
    Player(String name)
    {
        mName = name;
        
        // When a player is added in the middle of the game, 
        // we must create a score object for each round
        int numRounds = CurrentGame.getTheGame().getNumRounds();
        for(int round = 1; round <= numRounds; round++) 
        {
            mRoundScores.put(round, new PlayerStateForRound(this, round));
        }
    }
    
    public void setName(String name) { mName = name; }
    public String getName()          { return mName; }
    public void clearScores()
    {
        mRoundScores.clear();
    }

    /**
     * Return object for the score and total score for the round.
     * Calculate the total up to and including the round.
     *
     * @param round  Round number to get data for
     * @return PlayerStateForRound for the passed in round
     */
    public PlayerStateForRound getPlayerStateForRound(int round)
    {
        PlayerStateForRound retVal = mRoundScores.get(round);
        if (retVal != null) retVal.setTotalScore(getTotalScoreUpToRound(round));
        return retVal;
    }
    
    public void addPlayerStateForRound(int roundNumber)
    {
        if (getPlayerStateForRound(roundNumber) == null) 
        {
            mRoundScores.put(roundNumber, new PlayerStateForRound(this, roundNumber));
        }
    }
    public void setRoundScore(int round, int score)
    {
        PlayerStateForRound psfrToUse = getPlayerStateForRound(round);
        if (psfrToUse == null) 
        {
            mRoundScores.put(round, new PlayerStateForRound(this, round));
        }
        psfrToUse.setRoundScore(score);
    }
    public int getRoundScore(int round)
    {
        PlayerStateForRound psfr = getPlayerStateForRound(round);
        if (psfr != null)
        {
            return psfr.getRoundScore();
        }
        return 0;
    }

    /**
     * @return total score for all rounds
     */
    private int getTotalScore()
    {
        int total = 0;
        for (Integer r : mRoundScores.keySet())
        {
            total += mRoundScores.get(r).getRoundScore();
        }
        return total;
    }
    /**
     * @return total score for all previous rounds and the round passed in
     */
    private int getTotalScoreUpToRound(int round)
    {
        int total = 0;
        for (Integer r : mRoundScores.keySet())
        {
            if (r <= round)
                total += mRoundScores.get(r).getRoundScore();
        }
        return total;
    }
}
