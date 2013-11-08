package com.feup.validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.feup.validator.MainActivity;
import com.feup.validator.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class ValidateOffline extends Activity {
	
		private ProgressDialog pd;
		private TableLayout tb;
		
		private String UserId="0", Id="0";
		private String Type="T3", Nickname="";
		private Calendar ValidatedTime = Calendar.getInstance();
		private IntentIntegrator intentI = new IntentIntegrator(this);
		private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
		
		public void onActivityResult(int requestCode, int resultCode, Intent intent) {
			IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
			final Context context = this;
			if (scanResult != null) {
			      if(scanResult.getContents() != null)
			          {
			      	  	  String aux = scanResult.getContents();
			      	  	  String[] trunc = aux.split(";");
			      	  	  Id = trunc[0];
					      UserId = trunc[1];
					      Type = "T" + trunc[2];
					      ValidatedTime=Calendar.getInstance();
					      Nickname=trunc[4];
						    try {
								ValidatedTime.setTime(sdf.parse(trunc[3]));
							} catch (ParseException e) {
								Log.d("Error", "Error parsing Validation Date.");
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
										
									MediaPlayer mp = MediaPlayer.create(ValidateOffline.this, R.raw.error);
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
						                }, 1000); // after 2 second (or 2000 miliseconds), the task will be active.
							}
			          }
			      else
			          {
			    	  	this.finish();
			    	  	this.finish();
					    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
					    return;
			          }
			}
			
			AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
				int response=-1;
				int time=-1;
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
						
						for(int i =0 ; i<MainActivity.validatedTickets.size();i++)
						{
							if(MainActivity.validatedTickets.get(i).UserId.equals(UserId))
								{
									response=-2;
									Calendar currdate= Calendar.getInstance();
									DateTime dtime1 = new DateTime(currdate);
									DateTime dtime2 = new DateTime(MainActivity.validatedTickets.get(i).ValidatedTime);
									if(MainActivity.validatedTickets.get(i).Type.equals("T1"))
										time = 15 - Minutes.minutesBetween(dtime2,dtime1).getMinutes();
									else if(MainActivity.validatedTickets.get(i).Type.equals("T2"))
										time = 30 - Minutes.minutesBetween(dtime2,dtime1).getMinutes();
									else
										time = 60 - Minutes.minutesBetween(dtime2,dtime1).getMinutes();
									break;
								}
						}
				        Calendar currentDate = Calendar.getInstance();
				        DateTime dtime1 = new DateTime(currentDate);
    					DateTime dtime2 = new DateTime(ValidatedTime);
    					int diff = Hours.hoursBetween(dtime2, dtime1).getHours();
    					if(diff!=0)
    					{
    						response=-3;
    					}
    					if(response==-1)
    					{
    						Tickets newticket = new Tickets(Id, MainActivity.BusId, ValidatedTime, Type, UserId, Nickname);
    						MainActivity.validatedTickets.add(newticket);
    						response=1;
    					}
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
										
									MediaPlayer mp = MediaPlayer.create(ValidateOffline.this, R.raw.error);
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
						                }, 1000); // after 2 second (or 2000 miliseconds), the task will be active.
							}
								break;
							case -2:
							{
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
										context);
						 
									// set title
									alertDialogBuilder.setTitle("Success");
						 
									// set dialog message
									alertDialogBuilder
										.setMessage("You Have Already Validated a Ticket. You still have "+time+" more minutes.")
										.setCancelable(false);
									
									MediaPlayer mp = MediaPlayer.create(ValidateOffline.this, R.raw.validate);
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
						                }, 1000); // after 2 second (or 2000 miliseconds), the task will be active.
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
										.setMessage("Validation Date mismatch.")
										.setCancelable(false);
									
									MediaPlayer mp = MediaPlayer.create(ValidateOffline.this, R.raw.error);
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
						                }, 1000); // after 2 second (or 2000 miliseconds), the task will be active.
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
										.setMessage("Ticket Validated!")
										.setCancelable(false);
						 
										// create alert dialog
										AlertDialog alertDialog = alertDialogBuilder.create();
										
									MediaPlayer mp = MediaPlayer.create(ValidateOffline.this, R.raw.validate);
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
						                }, 1000); // after 2 second (or 2000 miliseconds), the task will be active.
							}
								break;

						}
					}
				}
					
			};
			task.execute((Void[])null);
			overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
			
		}
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.validate);
			
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
		

	}
