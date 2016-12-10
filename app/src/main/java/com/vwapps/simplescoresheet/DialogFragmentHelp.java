package com.vwapps.simplescoresheet;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

public class DialogFragmentHelp extends DialogFragment
{
    private ArrayList<HelpElement> mHelpElements;
    private AdapterHelp mAdapter;
    private View mLayoutView;
    
    public DialogFragmentHelp()
    {
        mHelpElements = new ArrayList<HelpElement>();
    }
    public void add(int task, int how)
    {
        Context c = getActivity().getApplicationContext();
        mHelpElements.add(new HelpElement(c.getString(task), c.getString(how)) );
    }
	private void setHelpElements()
	{
        add(R.string.help_task_add_player,  R.string.help_how_add_player);
        add(R.string.help_task_enter_score, R.string.help_how_enter_score);
        add(R.string.help_task_edit_player, R.string.help_how_edit_player);
        add(R.string.help_task_add_round,   R.string.help_how_add_round);
        add(R.string.help_task_next_prev_round,  R.string.help_how_swipe);
        add(R.string.help_task_clear, R.string.help_how_clear);
        add(R.string.help_task_sort, R.string.help_how_sort);
        add(R.string.help_task_save, R.string.help_how_save);
	}
	
	private void populateList()
	{
		if (mHelpElements.isEmpty()) setHelpElements();
    	
    	mAdapter = new AdapterHelp
    			(getActivity().getApplicationContext(), 
    		     android.R.layout.simple_list_item_1,
    			 mHelpElements);
    	
	 	ListView listView = (ListView) mLayoutView.findViewById(R.id.helpListView);
	 	if (listView != null)
	 	{
	 		listView.setAdapter(mAdapter);
	 	}
	}
    
    //	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mLayoutView = inflater.inflate(R.layout.help_layout, null);
		
		
		builder
		   .setTitle(R.string.help)
		   .setView(mLayoutView)
           .setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id)
			{
				
			}
 		});
		
		// Create the AlertDialog object and return it
		return builder.create();
	}
	
	@Override
	public void onResume() 
	{
		super.onResume();
		populateList();
	}
}
