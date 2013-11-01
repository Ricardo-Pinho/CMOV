package com.feup.passenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import net.simonvt.numberpicker.NumberPicker;
import android.widget.TextView;

public class BuyTickets extends Activity implements NumberPicker.OnValueChangeListener {
		private Button buy;
		private ProgressDialog pd;
		private TextView total;
		private double totalValue=0.0;
		private final double pricet1=0.50;
		private final double pricet2=1.00;
		private final double pricet3=1.50;
		private NumberPicker np1,np2,np3;
	
	//test
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.buytickets);
			addListenerOnButton();
			total = (TextView)findViewById(R.id.total);
			np1 = (NumberPicker)findViewById(R.id.numberPicker1);
	        np1.setMaxValue(10);
	        np1.setMinValue(0);
	        np1.setValue(0);
	        np1.setId(1);
	        np1.setOnValueChangedListener(this);
	        
	        np2 = (NumberPicker)findViewById(R.id.numberPicker2);
	        np2.setMaxValue(10);
	        np2.setMinValue(0);
	        np2.setValue(0);
	        np2.setId(2);
	        np2.setOnValueChangedListener(this);
	        
	        np3 = (NumberPicker)findViewById(R.id.numberPicker3);
	        np3.setMaxValue(10);
	        np3.setMinValue(0);
	        np3.setValue(0);
	        np3.setId(3);
	        np3.setOnValueChangedListener(this);
			
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
	        switch(picker.getId()) {
		        case 1:
		        {
		        	if(oldVal==0 && newVal==10)
		        		totalValue=totalValue+pricet1*10;
		        	else if(oldVal==10 && newVal==0)
		        		totalValue=totalValue-pricet1*10;
		        	else if(oldVal<newVal)
		        		totalValue=totalValue+pricet1;
		        	else
		        		totalValue=totalValue-pricet1;
		        }
		        	break;
		        case 2:
		        {
		        	if(oldVal==0 && newVal==10)
		        		totalValue=totalValue+pricet2*10;
		        	else if(oldVal==10 && newVal==0)
		        		totalValue=totalValue-pricet2*10;
		        	else if(oldVal<newVal)
		        		totalValue=totalValue+pricet2;
		        	else
		        		totalValue=totalValue-pricet2;
		        }
		        	break;
		        case 3:
		        {
		        	if(oldVal==0 && newVal==10)
		        		totalValue=totalValue+pricet3*10;
		        	else if(oldVal==10 && newVal==0)
		        		totalValue=totalValue-pricet3*10;
		        	else if(oldVal<newVal)
		        		totalValue=totalValue+pricet3;
		        	else
		        		totalValue=totalValue-pricet3;
		        }
		        	break;
	        }
	        total.setText("Total: "+Double.toString(totalValue)+"€");
	    }
		
		public void addListenerOnButton() {
			 
			final Context context = this;
	 
			buy = (Button) findViewById(R.id.buy_button);
	 
			buy.setOnClickListener(new OnClickListener() {
	 
				@Override
				public void onClick(View v) {
					v.setEnabled(false);
					AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
						int response=-1;	
						@Override
						protected void onPreExecute() {
							InputMethodManager imm = (InputMethodManager)getSystemService(
								      Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(np1.getWindowToken(), 0);
							imm.hideSoftInputFromWindow(np2.getWindowToken(), 0);
							imm.hideSoftInputFromWindow(np3.getWindowToken(), 0);
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
								String payload = "Error";
						        HttpURLConnection con = null;
						        try {
						          // RESTful URL for POST
						          String link = MainActivity.serverip+"BuyTickets";
						          URL url = new URL(link);

						          con = (HttpURLConnection) url.openConnection();
						          con.setReadTimeout(10000);      
						          con.setConnectTimeout(15000);   
						          con.setRequestMethod("POST");
						          con.setDoOutput(true);
						          con.setDoInput(true);
						          con.setRequestProperty("Content-Type", "application/json");

						          payload = "{\"ID\":" + Integer.toString(MainActivity.Id) + ",\"T1No\":" + np1.getValue() + ",\"T2No\":" + np2.getValue() + ",\"T3No\":" + np3.getValue() + "}";
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
								return null;
							}
							return null;
						}
						
						@Override
						protected void onPostExecute(Void result) {
							if (pd!=null) {
								pd.dismiss();
								buy.setEnabled(true);
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
												.setMessage("Tickets are now available for use.")
												.setCancelable(false)
												.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog,int id) {
														dialog.cancel();
														BuyTickets.this.finish();
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
