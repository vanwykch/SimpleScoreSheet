package com.vwapps.simplescoresheet;


/**
 *  Data for a row in the help dialog
 */
public class HelpElement 
{
	private String mTask;
	private String mHow;

	HelpElement(String task, String how)
	{
		mTask = task;
		mHow = how;
	}

	///////////////////////////////////////////////////////////////////////////
	
	public String getTask() { return mTask; }
	public String getHow() { return mHow; }
}
