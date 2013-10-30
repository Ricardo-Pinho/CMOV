package com.feup.validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.feup.validator.R;
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

public class GetData extends Activity {
	
		private ProgressDialog pd;
		private TableLayout tb;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.get_data);
			final Context context = this;
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
						HttpURLConnection con = null;
				        String payload = "Error";
				        try {

				          // Build RESTful query (GET)
				          URL url = new URL(MainActivity.serverip+"Tickets/GetTickets/" + MainActivity.key);

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
				            response=1;
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
												GetData.this.finish();
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
								tb = (TableLayout)findViewById(R.id.TableLayout);
								for(int i=0; i<arrayResponse.length();i++)
								{
									JSONObject jsonObject = null;
									try {
										jsonObject = new JSONObject(arrayResponse.get(i).toString());
									} catch (JSONException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
									
									LayoutParams tp = new LayoutParams(0,LayoutParams.WRAP_CONTENT);
									tp.weight=1;
									int id = 0;
									String Type="";
									int userid=0;
							        try {
										id = jsonObject.getInt("Id");
										Type = jsonObject.getString("Type");
										userid=jsonObject.getInt("UserId");
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									TableRow tr = new TableRow(GetData.this);
									tr.setLayoutParams(lp);
									TextView Id = new TextView(GetData.this);
									Id.setLayoutParams(tp);
									Id.setTextColor(Color.WHITE);
									Id.setGravity(Gravity.CENTER);
									Id.setText(Integer.toString(id));
							        TextView type = new TextView(GetData.this);
							        type.setLayoutParams(tp);
							        type.setTextColor(Color.WHITE);
							        type.setGravity(Gravity.CENTER);
									type.setText(Type);
							        TextView user = new TextView(GetData.this);
							        user.setLayoutParams(tp);
							        user.setTextColor(Color.WHITE);
							        user.setGravity(Gravity.CENTER);
									user.setText(Integer.toString(userid));
									//Tickets newticket = new Tickets(id, Type, userid);
									//MainActivity.validatedTickets.add(newticket);
									tr.addView(Id);
									tr.addView(type);
									tr.addView(user);
									tb.addView(tr, i+1);
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
