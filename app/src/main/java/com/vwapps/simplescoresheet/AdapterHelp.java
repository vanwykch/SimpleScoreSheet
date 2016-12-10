package com.vwapps.simplescoresheet;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Help dialog array adapter
 */
public class AdapterHelp extends ArrayAdapter<HelpElement> 
{
	private ArrayList<HelpElement> mHelpElements;

	public AdapterHelp(Context context, int textViewResourceId, ArrayList<HelpElement> helpElements) 
	{
		super(context, textViewResourceId, helpElements);
		mHelpElements = helpElements;
	}

	/**
	 * get a view for each row on the help dialog
	 */
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// assign the view we are converting to a local variable
		LayoutInflater inflater = (LayoutInflater) 
				getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) 
		{
			convertView = inflater.inflate(R.layout.help_element_row, null);
		}
		HelpElement he = mHelpElements.get(position);

		if (he != null) {

			// Get TextView for each field in for the player	
			TextView taskView = (TextView) convertView.findViewById(R.id.tasktext);
			TextView howView  = (TextView) convertView.findViewById(R.id.howtext);

			// check to see if each individual textview is null.
			// if not, assign some text!
			if (taskView!= null) taskView.setText( he.getTask() );
			if (howView != null) howView.setText( he.getHow() );
		}
		
		// the view must be returned to our activity
		return convertView;

	}

}