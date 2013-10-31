package com.feup.validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.feup.validator.MainActivity;
import com.feup.validator.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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

public class Validate extends Activity {
	
		private ProgressDialog pd;
		private TableLayout tb;
		
		private String UserId="1";
		private String Type="T3";
		private IntentIntegrator intentI = new IntentIntegrator(this);
		
		public void onActivityResult(int requestCode, int resultCode, Intent intent) {
			IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
			if (scanResult != null) {
			      String aux = scanResult.getContents();
			      UserId = aux.substring(0, aux.indexOf(' '));
			      Type = aux.substring(aux.indexOf(' ')+1);
			}
		}
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.validate);
			final Context context = this;
			
			intentI.initiateScan();
			
				AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
				int response=-1;
				
				int BusId= 303;
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
				          String link = MainActivity.serverip+"Tickets/TerminalValidate/" + MainActivity.key;
				          URL url = new URL(link);

				          con = (HttpURLConnection) url.openConnection();
				          con.setReadTimeout(10000);      
				          con.setConnectTimeout(15000);   
				          con.setRequestMethod("POST");
				          con.setDoOutput(true);
				          con.setDoInput(true);
				          con.setRequestProperty("Content-Type", "application/json");

				          payload = "{\"UserId\":" + UserId + ",\"Type\":\"" + Type + "\", \"BusId\":" + BusId +"}";
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
										
									MediaPlayer mp = MediaPlayer.create(Validate.this, R.raw.error);
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
						                    }
						                }, 1000); // after 2 second (or 2000 miliseconds), the task will be active.
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
										.setMessage("You do not have tickets of this type.")
										.setCancelable(false);
						 
										// create alert dialog
										AlertDialog alertDialog = alertDialogBuilder.create();
									
									MediaPlayer mp = MediaPlayer.create(Validate.this, R.raw.error);
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
						                    }
						                }, 1000); // after 2 second (or 2000 miliseconds), the task will be active.
							}
							break;
							case -3:
							{
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
										context);
						 
									// set title
									alertDialogBuilder.setTitle("Success");
						 
									// set dialog message
									alertDialogBuilder
										.setMessage("You Have Already Validated a Ticket")
										.setCancelable(false);
									
									MediaPlayer mp = MediaPlayer.create(Validate.this, R.raw.validate);
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
										.setMessage("Ticket Validated! You Have "+response+" more ticket(s) of this Type.")
										.setCancelable(false);
						 
										// create alert dialog
										AlertDialog alertDialog = alertDialogBuilder.create();
										
									MediaPlayer mp = MediaPlayer.create(Validate.this, R.raw.validate);
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
						                    }
						                }, 1000); // after 2 second (or 2000 miliseconds), the task will be active.
							}
								break;

						}
					}
				}
					
			};
			task.execute((Void[])null);
			
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
