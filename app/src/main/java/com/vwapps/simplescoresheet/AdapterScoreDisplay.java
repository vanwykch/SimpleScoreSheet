package com.vwapps.simplescoresheet;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 *  Fills in the name and score date for each player
 */
public class AdapterScoreDisplay extends ArrayAdapter<PlayerStateForRound> 
{
	private ArrayList<PlayerStateForRound> mPlayerData;

	public AdapterScoreDisplay(Context context, int textViewResourceId, ArrayList<PlayerStateForRound> playerData) 
	{
		super(context, textViewResourceId, playerData);
		this.mPlayerData = playerData;
	}

	/**
	 *  Create a view for each player
	 */
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// assign the view we are converting to a local variable
		LayoutInflater inflater = (LayoutInflater) 
				getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) 
		{
			convertView = inflater.inflate(R.layout.list_row, null);
		}

		PlayerStateForRound i = mPlayerData.get(position);
		if (i != null) {

			// Get TextView for each field in for the player	
			TextView ttd = (TextView) convertView.findViewById(R.id.nametext);
			TextView mtd = (TextView) convertView.findViewById(R.id.roundscoretext);
			TextView btd = (TextView) convertView.findViewById(R.id.totalscoretext);

			// check to see if each individual textview is null.
			// if not, assign some text!
			if (ttd != null){ ttd.setText( i.getPlayer().getName() ); }
			if (mtd != null){ mtd.setText( i.getRoundScoreString() ); }
			if (btd != null){ btd.setText(Integer.toString( i.getTotalScore() ) ); }
			
			if (i.scoreEntered())
			{
				convertView.setBackgroundColor(Color.LTGRAY);
			}
		}
		
		convertView.setClickable(false);

		// the view must be returned to our activity
		return convertView;
	}

}
