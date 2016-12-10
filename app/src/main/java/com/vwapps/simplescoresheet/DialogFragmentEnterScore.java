package com.vwapps.simplescoresheet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogFragmentEnterScore extends DialogFragment
{
    private View mLayoutView;

    private LinearLayout mLayout;
    private EditText mScoreEditText;
    private TextView mNameTextView;
    private LinearLayout.LayoutParams mParams;
    private DialogFragmentEnterScore mDialogFragmnet = this;
    
    public interface NoticeDialogListener {
        public void onEnterScoreOkClick(DialogFragment dialog);
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
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mLayoutView = inflater.inflate(R.layout.enter_score_layout, null);

        mNameTextView = (TextView) mLayoutView.findViewById(R.id.nameForEnterScore);
        mNameTextView.setText(CurrentGame.getTheGame().getCurName());

        mScoreEditText= (EditText) mLayoutView.findViewById(R.id.editTextEnterScore);
		mScoreEditText.setFocusable(true);
		mScoreEditText.setText("");

		final InputMethodManager imm = (InputMethodManager) 
				this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder
		   .setTitle(R.string.enter_score)
		   .setView(mLayoutView)
           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) 
			{
				int score = 0;
				try
				{
					score = Integer.parseInt(mScoreEditText.getText().toString() );
	             	CurrentGame curGame = CurrentGame.getTheGame();
	             	curGame.setScoreForCurPlayerAndCurPosition(score);
				}
				catch(NumberFormatException ex)
				{
					// just don't don't do anything
				}
				mListener.onEnterScoreOkClick(mDialogFragmnet);
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
        Dialog diag = builder.create();
		mScoreEditText.requestFocus();
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
		return diag;
	}	
}
