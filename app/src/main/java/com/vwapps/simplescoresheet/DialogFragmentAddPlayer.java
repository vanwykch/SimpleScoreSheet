package com.vwapps.simplescoresheet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

public class DialogFragmentAddPlayer extends DialogFragment
{
    private LinearLayout mLayout;
    private EditText mNameEditText;
    private LinearLayout.LayoutParams mParams;
    private DialogFragmentAddPlayer mDialogFragment = this;
    
    public interface NoticeDialogListener {
        public void onDialogOkClickAddPlayer(DialogFragment dialog);
    }
    
    NoticeDialogListener mListener;
    
    @Override
    public void onAttach(Activity activity) 
    {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
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
		Dialog diag;
        mLayout   = new LinearLayout(this.getActivity());
        mNameEditText = new EditText(this.getActivity());
	    mParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                                   LayoutParams.WRAP_CONTENT);
		mLayout.setOrientation(LinearLayout.HORIZONTAL);
		mNameEditText.setFocusable(true);
		mNameEditText.setText("");
		mNameEditText.setHint(R.string.player_name);
		mParams.gravity = Gravity.LEFT;
		mLayout.addView(mNameEditText, mParams);
		
		final InputMethodManager imm = (InputMethodManager) 
				this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder
		   .setTitle(R.string.add_player)
		   .setView(mLayout)
           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) 
			{
             	CurrentGame curGame = CurrentGame.getTheGame();
				String newName = mNameEditText.getText().toString();
				if (curGame.doesNameExist(newName))
				{
					// set flag so activity can show toast 
					// to alert user that name exists
					curGame.setShowNameExists();
					mListener.onDialogOkClickAddPlayer(mDialogFragment);
				}
				else
				{
					try
					{
		             	curGame.addPlayer(newName);
					}
					catch(NumberFormatException ex)
					{
						// just don't don't do anything
					}
					mListener.onDialogOkClickAddPlayer(mDialogFragment);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
				}
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) 
			{
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
			}
		});
		
		// Create the AlertDialog object and return it
		diag = builder.create();
		mNameEditText.requestFocus();
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
		return diag;
	}	
}
