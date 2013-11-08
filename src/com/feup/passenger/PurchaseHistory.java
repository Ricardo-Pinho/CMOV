package com.feup.passenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.feup.passenger.R;
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

public class PurchaseHistory extends Activity {
	
		private ProgressDialog pd;
		private TableLayout tb;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.purchase_history);
			final Context context = this;
				AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
				int response=-1;
				JSONArray arrayResponse;
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
				          URL url = new URL(MainActivity.serverip+"PurchaseHistory/" + MainActivity.usr.Id);

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
				            TextView t2 = (TextView)findViewById(R.id.textView2);
				            TextView t3 = (TextView)findViewById(R.id.textView3);				            
				            arrayResponse = jsonObject.getJSONArray("Purchase");
				            //t2.setText(jsonObject.toString());
				            //t3.setText(result.get(1).toString());
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
												PurchaseHistory.this.finish();
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
									LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
									
									LayoutParams tp = new LayoutParams(0,LayoutParams.WRAP_CONTENT);
									tp.weight=1;
									
									TableRow tr = new TableRow(PurchaseHistory.this);
									tr.setLayoutParams(lp);
									String [] trunc = null;
									try {
										trunc = arrayResponse.get(i).toString().split(";");
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									TextView date = new TextView(PurchaseHistory.this);
									date.setLayoutParams(tp);
									date.setTextColor(Color.WHITE);
									date.setGravity(Gravity.CENTER);
							        date.setText(trunc[1]);
							        TextView value = new TextView(PurchaseHistory.this);
							        value.setLayoutParams(tp);
							        value.setTextColor(Color.WHITE);
							        value.setGravity(Gravity.CENTER);
							        value.setText(trunc[0]+"0 €");
									tr.addView(date);
									tr.addView(value);
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
