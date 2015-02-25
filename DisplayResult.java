/*
This class implements the activity to display the result 
of classification - whether flight will be delayed or not.
*/

package com.jenxsol.parallaxscrollviewdemo1;

import couk.jenxsol.parallaxscrollviewdemo1.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayResult extends Activity {
	
	TextView tv_Result;
	String result = "Error";
	Bundle extras;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		extras = getIntent().getExtras();
		
		if (extras != null) {
			result = extras.getString("result");
		}
		
		tv_Result = (TextView) findViewById(R.id.textViewResult);
		
		if(result.equalsIgnoreCase("0"))
		{
			tv_Result.setText("Your flight will NOT be delayed...!!");
		}
		else
		{
			tv_Result.setText("Your flight will be delayed...!!");
		}
	}
}
