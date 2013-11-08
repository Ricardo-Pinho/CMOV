package com.feup.passenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import com.google.zxing.integration.android.IntentIntegrator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class OfflineMenu extends Activity {

	private IntentIntegrator intentI = new IntentIntegrator(this);
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	//test
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.offline_menu);
			addListenerOnButton1();
			addListenerOnButton2();
			addListenerOnButton3();
			addListenerOnButton4();
			TextView name = (TextView) findViewById(R.id.namelog);
			Log.d("Name", "UsrName"+MainActivity.usr.Name);
			name.setText("Welcome, "+MainActivity.usr.Name);
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.menu, menu);
			return true;
		}
		
		@Override
		public void onBackPressed() 
		{

		    this.finish();
		    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
		    return;
		}
		
		public void addListenerOnButton1() {
			 
			final Context context = this;
	 
			button1 = (Button) findViewById(R.id.button1);
	 
			button1.setOnClickListener(new OnClickListener() {
	 
				@Override
				public void onClick(View arg0) {
	 
				    Intent intent = new Intent(context, ValidateOffline.class);
	                startActivity(intent);   
	                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
	 
				}
	 
			});
	 
		}
		
		public void addListenerOnButton2() {
			 
			final Context context = this;
	 
			button2 = (Button) findViewById(R.id.button2);
	 
			button2.setOnClickListener(new OnClickListener() {
	 
				@Override
				public void onClick(View arg0) {
					int diff=-1;
					String TicketId = "";
					for(int i=0; i<MainActivity.usr.tickets.size();i++)
					{
						if(MainActivity.usr.tickets.get(i).Status==1)
						{
							Calendar currdate= Calendar.getInstance();
							DateTime dtime1 = new DateTime(currdate);
							DateTime dtime2 = new DateTime(MainActivity.usr.tickets.get(i).ValidatedTime);
							if(diff==-1)
								{
									diff = Minutes.minutesBetween(dtime2,dtime1).getMinutes();
									TicketId = MainActivity.usr.tickets.get(i).Id;
								}
							else if(Minutes.minutesBetween(dtime2,dtime1).getMinutes()<diff)
							{
								diff = Minutes.minutesBetween(dtime2,dtime1).getMinutes();
								TicketId = MainActivity.usr.tickets.get(i).Id;
							}
						}
					}
					if(diff==-1)
					{
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);
				 
							// set title
							alertDialogBuilder.setTitle("Error");
				 
							// set dialog message
							alertDialogBuilder
								.setMessage("You have no Validated Tickets")
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
						String  aux = MainActivity.usr.Id+";"+ TicketId;
						intentI.shareText(aux);
					}
	 
				}
	 
			});
	 
		}
		
		public void addListenerOnButton3() {
			 
			final Context context = this;
	 
			button3 = (Button) findViewById(R.id.button3);
	 
			button3.setOnClickListener(new OnClickListener() {
	 
				@Override
				public void onClick(View arg0) {
	 
				    Intent intent = new Intent(context, OfflineTicketHistory.class);
	                startActivity(intent);
	                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
	 
				}
	 
			});
	 
		}
		
		public void addListenerOnButton4() {
			 
			final Context context = this;
	 
			button4 = (Button) findViewById(R.id.button4);
	 
			button4.setOnClickListener(new OnClickListener() {
	 
				@Override
				public void onClick(View arg0) {
	 
				    Intent intent = new Intent(context, OfflineAvailableTickets.class);
	                startActivity(intent);
	                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
	 
				}
	 
			});
	 
		}
		

	}
