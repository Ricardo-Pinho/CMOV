package com.feup.inspector;

import java.util.Date;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Verify extends Activity {
	
	private String UserId="-1";
	private IntentIntegrator intentI = new IntentIntegrator(this);
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanResult != null) {
		      String aux = scanResult.getContents();
		      UserId = aux;
		}
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verify);
		
		TextView t=new TextView(this); 
	    t=(TextView)findViewById(R.id.textView2);
		
		intentI.initiateScan();
		
		Date now = new Date();
		
		if (UserId != "-1")
		{
			int i = 0;
			boolean continueFlag = true;
			
			do
			{
				if (MainActivity.validatedTickets.get(i).UserId == Integer.parseInt(UserId)) 
				{
					continueFlag = false;
					
					long aux = MainActivity.validatedTickets.get(i).ValidatedTime.getTime();
					switch (MainActivity.validatedTickets.get(i).Type.charAt(1)){
						case 1: aux+=15*60*1000;
								break;
								
						case 2: aux+=30*60*1000;
								break;
								
						case 3: aux+=60*60*1000;
								break;
					}
					
					if (aux >= now.getTime()) {
						t.setText("The passanger has a valid ticket.");
					}
					else t.setText("The passanger hasn't a valid ticket!!!");
				}
				else i++;
				
				if (i == MainActivity.validatedTickets.size())
				{
					continueFlag = false;
					t.setText("The passanger hasn't a valid ticket!!!");
				}
			}while(continueFlag);
		}
		else
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	 
				// set title
				alertDialogBuilder.setTitle("Error");
	 
				// set dialog message
				alertDialogBuilder
					.setMessage("Something went wrong. Please try again.")
					.setCancelable(false)
					.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					  });
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
		}
		
	}
}
