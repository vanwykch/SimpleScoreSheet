package com.vwapps.simplescoresheet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DialogFragmentAbout extends DialogFragment
{
	
	private View mLayoutView;
	
    //	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mLayoutView = inflater.inflate(R.layout.about_layout, null);
		
		TextView version_name = (TextView) mLayoutView.findViewById(R.id.app_version_text);
		
		try {
			String versionName = mLayoutView.getContext().getPackageManager()
				    .getPackageInfo(mLayoutView.getContext().getPackageName(), 0).versionName;
			
			version_name.setText(versionName);
			
		} catch (PackageManager.NameNotFoundException e) {
		    e.printStackTrace();
		}		
		
		
		builder
		   .setTitle(R.string.about)
		   .setView(mLayoutView)
           .setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id)
			{
				
			}
 		});
		
		
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
