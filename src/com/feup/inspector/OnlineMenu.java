package com.feup.inspector;

import java.util.ArrayList;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;


public class OnlineMenu extends Activity {

	
	private Button viewData;
	private Button verify;
	private Button resetData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_menu);
		addListenerOnButton1();
		addListenerOnButton2();
		//addListenerOnButton3();
		if(!MainActivity.BusId.equals("-1"))
		{
			TextView tv = (TextView)findViewById(R.id.BusIdLog);
			tv.setText(MainActivity.BusId);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void addListenerOnButton1() {
		 
		final Context context = this;
 
		viewData = (Button) findViewById(R.id.button1);
 
		viewData.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				if(MainActivity.BusId.equals("-1"))
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);
			 
						// set title
						alertDialogBuilder.setTitle("Error");
			 
						// set dialog message
						alertDialogBuilder
							.setMessage("You need to set the Bus Id first!")
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
				else{
					Intent intent = new Intent(context, GetTicketsServer.class);
	                startActivity(intent);   
	                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
				}
			}
 
		});
 
	}
	
	public void addListenerOnButton2() {
		 
		final Context context = this;
 
		verify = (Button) findViewById(R.id.button2);
 
		verify.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				if(MainActivity.BusId.equals("-1"))
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);
			 
						// set title
						alertDialogBuilder.setTitle("Error");
			 
						// set dialog message
						alertDialogBuilder
							.setMessage("You need to set the Bus Id first!")
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
				else{
					Intent intent = new Intent(context, Verify.class);
	                startActivity(intent);   
	                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
				}
 
			}
 
		});
 
	}
	
	public void addListenerOnButton3() {
		 
		final Context context = this;
 
		resetData = (Button) findViewById(R.id.button3);
 
		resetData.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);
		 
					// set title
					alertDialogBuilder.setTitle("Success");
		 
					// set dialog message
					alertDialogBuilder
						.setMessage("Data Reseted")
						.setCancelable(false)
						.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								MainActivity.validatedTickets = new ArrayList<Tickets>();
								MainActivity.verifiedUsers = new ArrayList<String>();
								dialog.cancel();
							}
						  });
		 
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
						alertDialog.show();
				}
 
 
		});
 
	}
	
	@Override
	public void onBackPressed() 
	{

	    this.finish();
	    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
	    return;
	}

}
