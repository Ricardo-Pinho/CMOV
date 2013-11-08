package com.feup.inspector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;






import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class VerifyOffline extends Activity {
	private ProgressDialog pd;
	private String UserId="-1";
	private String TicketId="-1";
	private int response=-1;
	private int diff=-1;
	private IntentIntegrator intentI = new IntentIntegrator(this);
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanResult != null) {
			if(scanResult.getContents() != null)
	          {  
				String aux = scanResult.getContents();
				String [] trunc  = aux.split(";");
				UserId = trunc[0];
				TicketId = trunc[1];
	          }
	      else
		      {
			  	this.finish();
			    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
			    return;
		      }
		}
		
		final Context context = this;
		
		if (!UserId.equals("-1"))
		{
			for(int i = 0; i<MainActivity.validatedTickets.size();i++)
			{
				if(MainActivity.validatedTickets.get(i).Id.equals(TicketId) && MainActivity.validatedTickets.get(i).UserId.equals(UserId))
				{
					if(MainActivity.validatedTickets.get(i).Verified!=-1)
						{
							response=-2;
							diff = MainActivity.validatedTickets.get(i).counter-MainActivity.validatedTickets.get(i).Verified;
						}
					else
					{
						MainActivity.validatedTickets.get(i).Verified=MainActivity.validatedTickets.get(i).counter;
						MainActivity.validatedTickets.get(i).counter++;
						Calendar currdate= Calendar.getInstance();
						DateTime dtime1 = new DateTime(currdate);
						DateTime dtime2 = new DateTime(MainActivity.validatedTickets.get(i).ValidatedTime);
						if(Hours.hoursBetween(dtime2,dtime1).getHours()<1 && MainActivity.validatedTickets.get(i).Type.equals("T3"))
						{
							response = 60 -(Minutes.minutesBetween(dtime2,dtime1).getMinutes());
						}
						else if(Minutes.minutesBetween(dtime2,dtime1).getMinutes()<31 && MainActivity.validatedTickets.get(i).Type.equals("T2"))
						{
							response = 30 - (Minutes.minutesBetween(dtime2,dtime1).getMinutes());
						}
						else if(Minutes.minutesBetween(dtime2,dtime1).getMinutes()<16 && MainActivity.validatedTickets.get(i).Type.equals("T1"))
						{
							response = 15 - (Minutes.minutesBetween(dtime2,dtime1).getMinutes());
						}
					}
				}
			}
			switch(response) {
				case -1:
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);
			 
						// set title
						alertDialogBuilder.setTitle("Error");
			 
						// set dialog message
						alertDialogBuilder
							.setMessage("User did not validate his ticket.")
							.setCancelable(false);
			 
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
						
						MediaPlayer mp = MediaPlayer.create(VerifyOffline.this, R.raw.error);
	                    mp.setOnCompletionListener(new OnCompletionListener() {

	                        @Override
	                        public void onCompletion(MediaPlayer mp) {
	                            mp.release();
	                        }

	                    });   
	                    mp.start();
			 
							// show it
							alertDialog.show();
							final AlertDialog dlg = alertDialog;
			                final Timer t = new Timer();
			                t.schedule(new TimerTask() {
			                    public void run() {
			                    	dlg.cancel();
			                        t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
			            			intentI.initiateScan();
			                    }
			                }, 2000); // after 2 second (or 2000 miliseconds), the task will be active.
				}
				break;
				case -2:
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);
			 
						// set title
						alertDialogBuilder.setTitle("Error");
			 
						// set dialog message
						alertDialogBuilder
							.setMessage("User Already Verified "+Integer.toString(diff)+" Passengers ago.")
							.setCancelable(false);
						
						MediaPlayer mp = MediaPlayer.create(VerifyOffline.this, R.raw.error);
	                    mp.setOnCompletionListener(new OnCompletionListener() {

	                        @Override
	                        public void onCompletion(MediaPlayer mp) {
	                            mp.release();
	                        }

	                    });   
	                    mp.start();
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
			 
							// show it
							alertDialog.show();
							final AlertDialog dlg = alertDialog;
			                final Timer t = new Timer();
			                t.schedule(new TimerTask() {
			                    public void run() {
			                    	dlg.cancel();
			                        t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
			            			intentI.initiateScan();
			                    }
			                }, 2000); // after 2 second (or 2000 miliseconds), the task will be active.
				}
				break;
				default:
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);
			 
						// set title
						alertDialogBuilder.setTitle("Success");
			 
						// set dialog message
						alertDialogBuilder
							.setMessage("User has validated a ticket. Valid for "+response+" more minutes.")
							.setCancelable(false);
			 
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
							
						MediaPlayer mp = MediaPlayer.create(VerifyOffline.this, R.raw.validate);
	                    mp.setOnCompletionListener(new OnCompletionListener() {

	                        @Override
	                        public void onCompletion(MediaPlayer mp) {
	                            mp.release();
	                        }

	                    });   
	                    mp.start();
		 
							// show it
							alertDialog.show();
							final AlertDialog dlg = alertDialog;
			                final Timer t = new Timer();
			                t.schedule(new TimerTask() {
			                    public void run() {
			                    	dlg.cancel();
			                        t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
			            			intentI.initiateScan();
			                    }
			                }, 2000); // after 2 second (or 2000 miliseconds), the task will be active.
				}
					break;

			}
		}
		else
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);
	 
				// set title
				alertDialogBuilder.setTitle("Error");
	 
				// set dialog message
				alertDialogBuilder
					.setMessage("Could Not Read QR Code.")
					.setCancelable(false);
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
					
				MediaPlayer mp = MediaPlayer.create(VerifyOffline.this, R.raw.error);
                mp.setOnCompletionListener(new OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }

                });   
                mp.start();
 
					// show it
					alertDialog.show();
					final AlertDialog dlg = alertDialog;
	                final Timer t = new Timer();
	                t.schedule(new TimerTask() {
	                    public void run() {
	                    	dlg.cancel();
	                        t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
	            			intentI.initiateScan();
	                    }
	                }, 2000); // after 2 second (or 2000 miliseconds), the task will be active.
		}
		
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verify);
		
		TextView t=new TextView(this); 
	    t=(TextView)findViewById(R.id.textView2);
		
		intentI.initiateScan();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() 
	{
	    this.finish();
	    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
	    return;
	}
	
	/*Date now = new Date();
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
	}*/
}
