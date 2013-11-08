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

import org.joda.time.DateTime;
import org.joda.time.Days;
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

public class AvailableTickets extends Activity {
	
		private ProgressDialog pd;
		private TableRow tr;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.available_tickets);
			final Context context = this;
				AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
				int response=-1;
				JSONArray arrayResponse;
				String SyncDate="";
	            int T1No=0, T2No=0,T3No=0;
				@Override
				protected void onPreExecute() {
					pd = new ProgressDialog(context);
					pd.setTitle("Getting Tickets");
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
				          URL url = new URL(MainActivity.serverip+"AvailableTickets/" + MainActivity.usr.Id);

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
				            JSONObject jsonObject = new JSONObject(payload);				            
				            arrayResponse = jsonObject.getJSONArray("Tickets");
				            SyncDate = jsonObject.getString("SyncDate");
				            response=1;
							for(int i=0; i<arrayResponse.length();i++)
							{
								try {
									jsonObject = new JSONObject(arrayResponse.get(i).toString());
								} catch (JSONException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								
								Tickets newticket = new Tickets();
						        try {
									newticket.Id = jsonObject.getString("Id");
									newticket.Type = jsonObject.getString("Type");
									if(newticket.Type.equals("T1"))
										T1No++;
									else if (newticket.Type.equals("T2"))
										T2No++;
									else
										T3No++;
									newticket.UserId=jsonObject.getString("UserId");
									newticket.Status=jsonObject.getInt("State");
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						        boolean exists=false;
						        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
						        for(int j =0; j<MainActivity.usr.tickets.size();j++)
						        	{
						        		if(MainActivity.usr.tickets.get(j).Id.equals(newticket.Id))
						        			{
						        				//MainActivity.usr.tickets.get(j).visited=true;
						        				if(MainActivity.usr.tickets.get(j).Status==1)
						        				{
						        					Calendar currentDate = Calendar.getInstance();
						        					try {
						        						Log.d("Calendar", "CalendarTime:"+SyncDate);
														currentDate.setTime(sdf.parse(SyncDate));
													} catch (ParseException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
						        					DateTime dtime1 = new DateTime(currentDate);
						        					DateTime dtime2 = new DateTime(MainActivity.usr.tickets.get(j).ValidatedTime);
						        					int diff = Days.daysBetween(dtime2, dtime1).getDays();
						        					if(diff>0)
						        					{
							        					MainActivity.usr.tickets.get(j).Status=0;
							        					MainActivity.usr.tickets.get(j).BusId=-1;
							        					MainActivity.usr.tickets.get(j).ValidatedTime = Calendar.getInstance();
						        					}
						        				}
						        				exists=true;
						        				break;
						        			} 
						        	}
						        if(!exists)
						        	{
						        		Log.d("new ticket", newticket.Id);
						        		MainActivity.usr.tickets.add(newticket);
						        	}
							}
							/*for(int i = 0 ; i< MainActivity.usr.tickets.size();i++)
							{
								if(!MainActivity.usr.tickets.get(i).visited)
									{
										if(MainActivity.usr.tickets.get(i).Status==0)
										{
											MainActivity.usr.tickets.remove(i);
											i--;
										}
									}
								else
									MainActivity.usr.tickets.get(i).visited=false;
							}*/
							Log.d("saved","yes");
							MainActivity.usr.Save();
				          }
				          catch (JSONException e) {
				        	  return null;
				          }
				        final String p = payload;
						Thread.sleep(1000);
					} catch (InterruptedException e) {
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
										.setCancelable(false)
										.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,int id) {
												dialog.cancel();
												AvailableTickets.this.finish();
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
								
								tr = (TableRow)findViewById(R.id.tableRow2);
									
									LayoutParams tp = new LayoutParams(0,LayoutParams.WRAP_CONTENT);
									tp.weight=1;
									
									TextView T1 = new TextView(AvailableTickets.this);
									T1.setLayoutParams(tp);
									T1.setTextColor(Color.WHITE);
									T1.setGravity(Gravity.CENTER);
							        T1.setText(Integer.toString(T1No));
							        TextView T2 = new TextView(AvailableTickets.this);
							        T2.setLayoutParams(tp);
							        T2.setTextColor(Color.WHITE);
							        T2.setGravity(Gravity.CENTER);
							        T2.setText(Integer.toString(T2No));
							        TextView T3 = new TextView(AvailableTickets.this);
							        T3.setLayoutParams(tp);
							        T3.setTextColor(Color.WHITE);
							        T3.setGravity(Gravity.CENTER);
							        T3.setText(Integer.toString(T3No));
									tr.addView(T1);
									tr.addView(T2);
									tr.addView(T3);
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
