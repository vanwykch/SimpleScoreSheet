package com.vwapps.simplescoresheet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DialogFragmentNewGame extends DialogFragment
{
	boolean mClearEverything = false;
    private DialogFragmentNewGame mDialogFragment = this;
	
	
    public interface NoticeDialogListener {
        public void onNewGameOkClick(DialogFragment dialog);
    }
	
    NoticeDialogListener mListener;
    @Override
    
    public void onAttach(Activity activity) 
    {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
        Context c = getActivity().getApplicationContext();
	    
		CharSequence choices[] = new CharSequence[2];
		choices[0] = c.getString(R.string.clear_scores);
		choices[1] = c.getString(R.string.clear_everything);
		
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder
		   .setTitle(R.string.dialog_new_game)
           .setSingleChoiceItems(choices, 0, new OnClickListener()
           {
               @Override
               public void onClick(DialogInterface dialog, int which) 
               {
                   if (which == 0) 
                   {
                	   mClearEverything = false;
                   } else {
                	   mClearEverything = true;
                   }
               }
           })		
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) 
			{
				if( mClearEverything )
				{
					CurrentGame.getTheGame().clearEverything();
				}
				else
				{
					CurrentGame.getTheGame().clearAllScores();
				}
                mListener.onNewGameOkClick(mDialogFragment);
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) 
			{
				// User cancelled the dialog
			}
		});
		
		// Create the AlertDialog object and return it
		return builder.create();
	}	
}
