package com.feup.passenger;

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

import com.feup.passenger.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

public class TicketHistory extends Activity {
	
		private ProgressDialog pd;
		private TableLayout tb;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.ticket_history);
			final Context context = this;
				AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
				int response=-1;
				JSONArray arrayResponse;
				JSONObject jsonObject;
	            ArrayList<Tickets> newtickets = new ArrayList<Tickets>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
				@Override
				protected void onPreExecute() {
					pd = new ProgressDialog(context);
					pd.setTitle("Getting History");
					pd.setMessage("Please wait.");
					pd.setCancelable(false);
					pd.setIndeterminate(true);
					pd.show();
				}
					
				@Override
				protected Void doInBackground(Void... arg0) {
					try {
						HttpURLConnection con = null;
				        String payload = "Error";
				        try {

				          // Build RESTful query (GET)
				          URL url = new URL(MainActivity.serverip+"TicketHistory/" + MainActivity.usr.Id);

				          con = (HttpURLConnection) url.openConnection();
				          con.setReadTimeout(10000);
				          con.setConnectTimeout(15000);
				          con.setRequestMethod("GET");
				          con.setDoInput(true);

				          // Start the query
				          con.connect();

				          // Read results from the query
				          BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8" ));
				          payload = reader.readLine();
				          reader.close();
				          } catch (IOException e) {
				        	  return null;
				        } finally {
				          if (con != null)
				            con.disconnect();
				        }
				        if (payload != "Error")
				          try {
				            jsonObject = new JSONObject(payload);			            
				            arrayResponse = jsonObject.getJSONArray("Tickets");
				            response=1;
				            for(int i=0; i<arrayResponse.length();i++)
							{
								JSONObject jsonObject = null;
								try {
									jsonObject = new JSONObject(arrayResponse.get(i).toString());
								} catch (JSONException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
						        boolean exists=false;
								Tickets newticket = new Tickets();
						        try {
									newticket.Id = jsonObject.getString("Id");
									newticket.Type = jsonObject.getString("Type");
									newticket.UserId=jsonObject.getString("UserId");
									newticket.Status=jsonObject.getInt("State");
									newticket.ValidatedTime=Calendar.getInstance();
								    try {
										newticket.ValidatedTime.setTime(sdf.parse(jsonObject.getString("ValidatedTime")));
									} catch (ParseException e) {
										Log.d("Error", "Error parsing Validation Date.");
										e.printStackTrace();
									}
									newticket.BusId=jsonObject.getInt("BusId");
									newtickets.add(newticket);
									for(int j=0; j<MainActivity.usr.busIds.size(); j++)
									{
										if(MainActivity.usr.busIds.get(j)==newticket.BusId)
											{
												
												exists=true;
											}
									}
									if(!exists)
										 {
											MainActivity.usr.busIds.add(newticket.BusId);
										 }
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						        exists=false;
						        for(int j=0; j<MainActivity.usr.tickets.size();j++)
						        	{
						        		if(MainActivity.usr.tickets.get(j).Id.equals(newticket.Id))
						        			{
						        				MainActivity.usr.tickets.get(j).ValidatedTime=newticket.ValidatedTime;
						        				MainActivity.usr.tickets.get(j).BusId=newticket.BusId;
						        				MainActivity.usr.tickets.get(j).Status=1;
						        				exists=true;
						        			}
						        	}
						        if(!exists)
						        	{
						        		Log.d("new ticket", newticket.Id);
						        		MainActivity.usr.tickets.add(newticket);
						        	}
							}
					        MainActivity.usr.Save();
				            
				          }
				          catch (JSONException e) {
				        	  return null;
				          }
				        final String p = payload;
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
										.setCancelable(false)
										.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,int id) {
												dialog.cancel();
												TicketHistory.this.finish();
											    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
											}
										  });
						 
										// create alert dialog
										AlertDialog alertDialog = alertDialogBuilder.create();
						 
										// show it
										alertDialog.show();
							}
								break;
							default:
							{
								//TextView tv = (TextView)findViewById(R.id.textView4);
								//tv.setText(jsonObject.toString());
								tb = (TableLayout)findViewById(R.id.TableLayout);
								int rowno=1;
								for(int i=0; i<newtickets.size();i++)
								{
									LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
									
									LayoutParams tp = new LayoutParams(0,LayoutParams.WRAP_CONTENT);
									tp.weight=1;
									
									TableRow tr = new TableRow(TicketHistory.this);
									tr.setLayoutParams(lp);
									TextView date = new TextView(TicketHistory.this);
									date.setLayoutParams(tp);
									date.setTextColor(Color.WHITE);
									date.setGravity(Gravity.CENTER);
									String dateString = sdf.format(newtickets.get(i).ValidatedTime.getTime());
							        date.setText(dateString);
							        TextView type = new TextView(TicketHistory.this);
							        type.setLayoutParams(tp);
							        type.setTextColor(Color.WHITE);
							        type.setGravity(Gravity.CENTER);
							        type.setText(newtickets.get(i).Type);
							        TextView bus = new TextView(TicketHistory.this);
							        bus.setLayoutParams(tp);
							        bus.setTextColor(Color.WHITE);
							        bus.setGravity(Gravity.CENTER);
							        bus.setText(Integer.toString(newtickets.get(i).BusId));
									tr.addView(date);
									tr.addView(type);
									tr.addView(bus);
									tb.addView(tr, rowno);
									rowno++;
								}
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
