package com.vwapps.simplescoresheet;

import java.util.ArrayList;
import java.util.Collections;


/**
 *  A round in the game
 */
public class Round {

    private ArrayList<PlayerStateForRound> mPlayerStateForRoundList = new ArrayList<PlayerStateForRound>();
    private int mRoundNumber;
    
///////////////////////////////////////////////////////////////////////////////////////////////////     

    Round(ArrayList<Player> players, int number)
    {
        CurrentGame game = CurrentGame.getTheGame();
        mRoundNumber = number;
        for (Player player : game.getPlayerList())
        {
            player.addPlayerStateForRound(number);
        }
    }
    
    public void setRoundNumber(int roundNum) { mRoundNumber = roundNum;     }
    public int getPlayersScore(Player player){ return player.getRoundScore(mRoundNumber);   }
    public int getRoundNumber()              { return mRoundNumber;    }
    public Player getPlayerByPos(int pos)    { return mPlayerStateForRoundList.get(pos).getPlayer(); }
    
///////////////////////////////////////////////////////////////////////////////////////////////////    
    
    /**
     *  return true if all scores are entered for this round
     */
    public boolean allScoresEntered()
    {
        boolean retVal = true;
        for (Player player : CurrentGame.getTheGame().getPlayerList())
        {
            if ( ! player.getPlayerStateForRound(mRoundNumber).scoreEntered() )
            {
                retVal = false;
                break;
            }
        }
        return retVal;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////     

    /**
     *  Create an array list of objects to display round
     */
    public ArrayList<PlayerStateForRound> getPlayerList(boolean reverseSort)
    {
        mPlayerStateForRoundList.clear();
        
        if (CurrentGame.getTheGame().getPlayerList().size() > 0 )
        {
            for (Player player : CurrentGame.getTheGame().getPlayerList())
            {
                mPlayerStateForRoundList.add(player.getPlayerStateForRound(mRoundNumber));
            }
            
            if (reverseSort) //  Sort low to high
            {
                Collections.sort(mPlayerStateForRoundList);
            }
            else //  Sort High to low
            {
                Collections.sort(mPlayerStateForRoundList, Collections.reverseOrder());
            }
        }
        return mPlayerStateForRoundList;
    }
}
