package com.vwapps.simplescoresheet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogFragmentEnterScore extends DialogFragment
{
    private View mLayoutView;

    private LinearLayout mLayout;
    private LinearLayout.LayoutParams mParams;
    private DialogFragmentEnterScore mDialogFragmnet = this;
    
    public interface NoticeDialogListener {
        public void enterScoreOK(DialogFragment dialog);
    }
    
    NoticeDialogListener mListener;
    
    @Override
    public void onAttach(Activity activity) 
    {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the AddPlayerNoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement AddPlayerNoticeDialogListener");
        }
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        final EditText scoreEditText;
        final TextView nameTestView;
        final CurrentGame cg = CurrentGame.getTheGame();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        mLayoutView = inflater.inflate(R.layout.enter_score_layout, null);

        nameTestView = (TextView) mLayoutView.findViewById(R.id.nameForEnterScore);
        nameTestView.setText(cg.getCurName());

        scoreEditText = (EditText) mLayoutView.findViewById(R.id.editTextEnterScore);
        scoreEditText.setFocusable(true);
        int title = R.string.enter_score;
        scoreEditText.setText("");
        if (cg.getCurPlayerStateForRound().scoreEntered()){
            scoreEditText.setText(cg.getCurPlayerCurRoundScore().toString());
            title = R.string.change_score;
        }
		final InputMethodManager imm = (InputMethodManager) 
				this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder
		   .setTitle(title)
		   .setView(mLayoutView)
           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) 
			{
				int score = 0;
				try
				{
					score = Integer.parseInt(scoreEditText.getText().toString() );
	             	cg.setScoreForCurPlayerAndCurPosition(score);
				}
				catch(NumberFormatException ex)
				{
					// just don't don't do anything
				}
				mListener.enterScoreOK(mDialogFragmnet);
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
		scoreEditText.requestFocus();
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
		return diag;
	}	
}
