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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

public class DialogFragmentEditPlayer extends DialogFragment
{
    private LinearLayout mLayout;
    private EditText mNameEditText;
    private LinearLayout.LayoutParams mParams;
//    private DialogFragmentEditPlayer mDialogFragmnet = this;
    private CheckBox mDeletePlayerCheckBox;
    
//    public interface AddPlayerNoticeDialogListener {
//        public void onDialogOkClick(DialogFragment dialog);
//    }
//    
//    AddPlayerNoticeDialogListener mListener;
    
    @Override
    public void onAttach(Activity activity) 
    {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
//        try {
//            // Instantiate the AddPlayerNoticeDialogListener so we can send events to the host
//            mListener = (AddPlayerNoticeDialogListener) activity;
//        } catch (ClassCastException e) {
//            // The activity doesn't implement the interface, throw exception
//            throw new ClassCastException(activity.toString()
//                    + " must implement AddPlayerNoticeDialogListener");
//        }
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
     	CurrentGame curGame = CurrentGame.getTheGame();

		Dialog diag;
        mLayout   = new LinearLayout(this.getActivity());
        mNameEditText = new EditText(this.getActivity());
        mDeletePlayerCheckBox = new CheckBox(this.getActivity());
	    mParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                                   LayoutParams.WRAP_CONTENT);
		mLayout.setOrientation(LinearLayout.VERTICAL);
		mNameEditText.setFocusable(true);
		mNameEditText.setText(curGame.getCurName());
		mDeletePlayerCheckBox.setClickable(true);
		mDeletePlayerCheckBox.setChecked(false);
		mDeletePlayerCheckBox.setText(R.string.delete_player);
		mParams.gravity = Gravity.LEFT;
		mLayout.addView(mNameEditText, mParams);
		mLayout.addView(mDeletePlayerCheckBox, mParams);
		
		final InputMethodManager imm = (InputMethodManager) 
				this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder
		   .setTitle(R.string.edit_player)
		   .setView(mLayout)
           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) 
			{
				try
				{
	             	CurrentGame curGame = CurrentGame.getTheGame();
	             	curGame.setCurName(mNameEditText.getText().toString());
	             	if(mDeletePlayerCheckBox.isChecked())
	             	{
	             		curGame.deletePlayer(curGame.getCurName());
	             	}
				}
				catch(NumberFormatException ex)
				{
					// just don't don't do anything
				}
//				mListener.onDialogOkClick(mDialogFragmnet);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
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
