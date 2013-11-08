package com.feup.passenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import net.simonvt.numberpicker.NumberPicker;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class AddTicketsOffline extends Activity implements NumberPicker.OnValueChangeListener {
		private Button addTickets;
		private ProgressDialog pd;
		private NumberPicker np1,np2,np3;
		private int T1no=0,T2no=0,T3no=0;
		private int t1tOn=0, t2tOn=0, t3tOn=0;
		private int t1tOff=0, t2tOff=0, t3tOff=0;
		private TableRow tr, tr2;
	
	//test
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.add_tickets_offline);
			addListenerOnButton();
			
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
										t1tOn++;
									else if (newticket.Type.equals("T2"))
										t2tOn++;
									else
										t3tOn++;
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
						        	MainActivity.usr.tickets.add(newticket);
							}
							MainActivity.usr.Save();
							for(int i=0;i<MainActivity.usr.tickets.size();i++)
							{
								if(MainActivity.usr.tickets.get(i).Status==2)
								{
									if(MainActivity.usr.tickets.get(i).Type.equals("T1"))
										t1tOff++;
									else if(MainActivity.usr.tickets.get(i).Type.equals("T2"))
										t2tOff++;
									else
										t3tOff++;
								}
							}
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
												AddTicketsOffline.this.finish();
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
								
								tr = (TableRow)findViewById(R.id.tableRowOn);
									
									LayoutParams tp = new LayoutParams(0,LayoutParams.WRAP_CONTENT);
									tp.weight=1;
									
									TextView T1 = new TextView(AddTicketsOffline.this);
									T1.setLayoutParams(tp);
									T1.setTextColor(Color.WHITE);
									T1.setGravity(Gravity.CENTER);
							        T1.setText(Integer.toString(t1tOn));
							        TextView T2 = new TextView(AddTicketsOffline.this);
							        T2.setLayoutParams(tp);
							        T2.setTextColor(Color.WHITE);
							        T2.setGravity(Gravity.CENTER);
							        T2.setText(Integer.toString(t2tOn));
							        TextView T3 = new TextView(AddTicketsOffline.this);
							        T3.setLayoutParams(tp);
							        T3.setTextColor(Color.WHITE);
							        T3.setGravity(Gravity.CENTER);
							        T3.setText(Integer.toString(t3tOn));
									tr.addView(T1);
									tr.addView(T2);
									tr.addView(T3);
								
								tr2 = (TableRow)findViewById(R.id.tableRowOff);
																	
								LayoutParams tp2 = new LayoutParams(0,LayoutParams.WRAP_CONTENT);
								tp2.weight=1;
								
								TextView T4 = new TextView(AddTicketsOffline.this);
								T4.setLayoutParams(tp);
								T4.setTextColor(Color.WHITE);
								T4.setGravity(Gravity.CENTER);
						        T4.setText(Integer.toString(t1tOff));
						        TextView T5 = new TextView(AddTicketsOffline.this);
						        T5.setLayoutParams(tp);
						        T5.setTextColor(Color.WHITE);
						        T5.setGravity(Gravity.CENTER);
						        T5.setText(Integer.toString(t2tOff));
						        TextView T6 = new TextView(AddTicketsOffline.this);
						        T6.setLayoutParams(tp);
						        T6.setTextColor(Color.WHITE);
						        T6.setGravity(Gravity.CENTER);
						        T6.setText(Integer.toString(t3tOff));
								tr2.addView(T4);
								tr2.addView(T5);
								tr2.addView(T6);
								
								for(int i = 0; i<MainActivity.usr.tickets.size();i++)
								{
									if(MainActivity.usr.tickets.get(i).Type.equals("T1"))
									{
										if(MainActivity.usr.tickets.get(i).Status==0)
											T1no++;
									}
									else if(MainActivity.usr.tickets.get(i).Type.equals("T2"))
									{
										if(MainActivity.usr.tickets.get(i).Status==0)
											T2no++;
									}
									else
									{
										if(MainActivity.usr.tickets.get(i).Status==0)
											T3no++;
									}
								}
								Log.d("t1no", Integer.toString(T1no));
								Log.d("t2no", Integer.toString(T2no));
								Log.d("t3no", Integer.toString(T3no));
								np1 = (NumberPicker)findViewById(R.id.numberPicker1);
						        np1.setMaxValue(T1no);
						        np1.setMinValue(0);
						        np1.setValue(0);
						        np1.setId(1);
						        np1.setOnValueChangedListener(AddTicketsOffline.this);
						        
						        np2 = (NumberPicker)findViewById(R.id.numberPicker2);
						        np2.setMaxValue(T2no);
						        np2.setMinValue(0);
						        np2.setValue(0);
						        np2.setId(2);
						        np2.setOnValueChangedListener(AddTicketsOffline.this);
						        
						        np3 = (NumberPicker)findViewById(R.id.numberPicker3);
						        np3.setMaxValue(T3no);
						        np3.setMinValue(0);
						        np3.setValue(0);
						        np3.setId(3);
						        np3.setOnValueChangedListener(AddTicketsOffline.this);
						        
								InputMethodManager imm = (InputMethodManager)getSystemService(
									      Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(np1.getWindowToken(), 0);
								imm.hideSoftInputFromWindow(np2.getWindowToken(), 0);
								imm.hideSoftInputFromWindow(np3.getWindowToken(), 0);
								
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
		
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
	        
	    }
		
		public void addListenerOnButton() {
			 
			final Context context = this;
	 
			addTickets = (Button) findViewById(R.id.add_offline_button);
	 
			addTickets.setOnClickListener(new OnClickListener() {
	 
				@Override
				public void onClick(View v) {
					v.setEnabled(false);
					AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
						int response=-1;
						JSONArray arrayResponse;
						@Override
						protected void onPreExecute() {
							pd = new ProgressDialog(context);
							pd.setTitle("Adding Tickets to Offline Mode");
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
						          String link = MainActivity.serverip+"OfflineTickets";
						          URL url = new URL(link);

						          con = (HttpURLConnection) url.openConnection();
						          con.setReadTimeout(10000);      
						          con.setConnectTimeout(15000);   
						          con.setRequestMethod("POST");
						          con.setDoOutput(true);
						          con.setDoInput(true);
						          con.setRequestProperty("Content-Type", "application/json");

						          payload = "{\"ID\":\"" + MainActivity.usr.Id + "\",\"T1No\":" + np1.getValue() + ",\"T2No\":" + np2.getValue() + ",\"T3No\":" + np3.getValue() + "}";
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
						        if (payload != "Error")
						          try {
							            JSONObject jsonObject = new JSONObject(payload);				            
							            arrayResponse = jsonObject.getJSONArray("Tickets");
							            response=1;
							            for(int i=0; i<arrayResponse.length();i++)
										{
											try {
												jsonObject = new JSONObject(arrayResponse.get(i).toString());
												Log.d("Type2", jsonObject.getString("Id"));
											} catch (JSONException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
											
									        try {
									        	for(int j=0;j<MainActivity.usr.tickets.size();j++)
									        	{
									        		if(MainActivity.usr.tickets.get(j).Id.equals(jsonObject.getString("Id")))
									        		{
									        			MainActivity.usr.tickets.get(j).Status=2;
									        		}
									        	}
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
							            Log.d("save", "it saved");
										MainActivity.usr.Save();
							          }
							          catch (JSONException e) {
							        	  return null;
							          }
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
								addTickets.setEnabled(true);
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
										
										AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
												context);
								 
											// set title
											alertDialogBuilder.setTitle("Success");
								 
											// set dialog message
											alertDialogBuilder
												.setMessage("Tickets are now available for use in Offline Mode.")
												.setCancelable(false)
												.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog,int id) {
														dialog.cancel();
														AddTicketsOffline.this.finish();
													    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
													}
												  });
								 
												// create alert dialog
												AlertDialog alertDialog = alertDialogBuilder.create();
								 
												// show it
												alertDialog.show();
									}
										break;

								}
							}
						}
							
					};
					task.execute((Void[])null);
				}

	 
			});
	 
		}
		

	}
