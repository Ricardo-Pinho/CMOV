package com.feup.inspector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;





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

public class Verify extends Activity {
	private ProgressDialog pd;
	private String UserId="-1";
	private String TicketId="-1";
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
			  	this.finish();
			    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
			    return;
		      }
		}
		
		final Context context = this;
		
		if (!UserId.equals("-1"))
		{
			AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
				int response=-1;
				
				JSONArray arrayResponse;
				@Override
				protected void onPreExecute() {
					pd = new ProgressDialog(context);
					pd.setTitle("Getting Data");
					pd.setMessage("Please wait.");
					pd.setCancelable(false);
					pd.setIndeterminate(true);
					pd.show();
				}
					
				@Override
				protected Void doInBackground(Void... arg0) {
					try {
						String payload = "Error";
				        HttpURLConnection con = null;
				        try {
				          // RESTful URL for POST
				          String link = MainActivity.serverip+"Tickets/InspectorVerify/" + MainActivity.key;
				          URL url = new URL(link);

				          con = (HttpURLConnection) url.openConnection();
				          con.setReadTimeout(10000);      
				          con.setConnectTimeout(15000);   
				          con.setRequestMethod("POST");
				          con.setDoOutput(true);
				          con.setDoInput(true);
				          con.setRequestProperty("Content-Type", "application/json");

				          payload = "{\"UserId\":\"" + UserId + "\", \"BusId\":" + MainActivity.BusId + ", \"Id\":\"" + TicketId +"\"}";
				          OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
				          writer.write(payload, 0, payload.length());
				          writer.close();
				          con.connect();
				          BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8" ));
				          payload = reader.readLine();
				          reader.close();
				        }
				        catch (IOException e) {
				        	return null;
				        }
				        finally {
				          if (con != null)
				            con.disconnect();
				        }
				        final String p = payload;
				        response=Integer.valueOf(p);
				        for(int i=0; i<MainActivity.verifiedUsers.size();i++)
				        {
				        	if (MainActivity.verifiedUsers.get(i).equals(UserId))
				        		{
				        			response=-4;
				        			break;
				        		}
				        }
				        if(response!=-4)
				        	MainActivity.verifiedUsers.add(UserId);
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						return null;
					}
					return null;
				}
				
				@Override
				protected void onPostExecute(Void result) {
					if (pd!=null) {
						pd.dismiss();
						switch(response) {
							case -1:
							{
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
										context);
						 
									// set title
									alertDialogBuilder.setTitle("Error");
						 
									// set dialog message
									alertDialogBuilder
										.setMessage("Something went wrong. Please try again.")
										.setCancelable(false);
						 
										// create alert dialog
										AlertDialog alertDialog = alertDialogBuilder.create();
										
									MediaPlayer mp = MediaPlayer.create(Verify.this, R.raw.error);
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
										.setMessage("User did not validate his ticket.")
										.setCancelable(false);
						 
										// create alert dialog
										AlertDialog alertDialog = alertDialogBuilder.create();
									
									MediaPlayer mp = MediaPlayer.create(Verify.this, R.raw.error);
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
							case -3:
							{
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
										context);
						 
									// set title
									alertDialogBuilder.setTitle("Error");
						 
									// set dialog message
									alertDialogBuilder
										.setMessage("User did not Validate and has no tickets available.")
										.setCancelable(false);
									
									MediaPlayer mp = MediaPlayer.create(Verify.this, R.raw.error);
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
							case -4:
							{
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
										context);
						 
									// set title
									alertDialogBuilder.setTitle("Error");
						 
									// set dialog message
									alertDialogBuilder
										.setMessage("User Already Verified")
										.setCancelable(false);
									
									MediaPlayer mp = MediaPlayer.create(Verify.this, R.raw.error);
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
										
									MediaPlayer mp = MediaPlayer.create(Verify.this, R.raw.validate);
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
				}
					
			};
			task.execute((Void[])null);
			overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
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
					
				MediaPlayer mp = MediaPlayer.create(Verify.this, R.raw.error);
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
