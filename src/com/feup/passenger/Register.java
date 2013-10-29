package com.feup.passenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.feup.passenger.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;



public class Register extends Activity {
		private static final int RB1_ID = 1000;//first radio button id
		private static final int RB2_ID = 1001;//second radio button id
		private static final int RB3_ID = 1002;//third radio button id
	
		private Button register;
		private ProgressDialog pd;
		private EditText et1, et2, et3, et5;
		private RadioGroup et4;
		private DatePicker et6;
		private RadioButton rb1, rb2, rb3;
	//test
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.register);
			addListenerOnButton();
			et1 = (EditText) findViewById(R.id.name);
		    et2 = (EditText) findViewById(R.id.username);
		    et3 = (EditText) findViewById(R.id.password);
		    et4 = (RadioGroup) findViewById(R.id.CcType);
		    et5 = (EditText) findViewById(R.id.CcNumber);
		    et6 = (DatePicker) findViewById(R.id.CcValidation);
		    rb1 = (RadioButton) findViewById(R.id.CcType1);
		    rb2 = (RadioButton) findViewById(R.id.CcType2);
		    rb3 = (RadioButton) findViewById(R.id.CcType3);
		    rb1.setId(RB1_ID);
		    rb2.setId(RB2_ID);
		    rb3.setId(RB3_ID);
		    et4.check(RB1_ID);
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.register, menu);
			return true;
		}
		
		@Override
		public void onBackPressed() 
		{

		    this.finish();
		    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
		    return;
		}
		
		public void addListenerOnButton() {
			 
			final Context context = this;
	 
			register = (Button) findViewById(R.id.register_button);

			register.setOnClickListener(new OnClickListener() {
	 
				@Override
				public void onClick(View v) {
					v.setEnabled(false);
				    int cId = et4.getCheckedRadioButtonId();
				    String type="";
				    switch (cId) {
				    case RB1_ID:
					    type=rb1.getText().toString();
				    break;
				    
				    case RB2_ID:
				    	type=rb2.getText().toString();
				    break;
				    
				    case RB3_ID:
				    	type=rb3.getText().toString();
				    break;
				        //other checks for the other RadioButtons ids from the RadioGroup
				    case -1:
				    	type="Error";
				    break;
				    }
				    final String Cctype=type;
				    final String Ccdate = et6.getYear()+"-"+et6.getMonth()+"-"+et6.getDayOfMonth();

					AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
					int response=-1;	
						@Override
						protected void onPreExecute() {
							pd = new ProgressDialog(context);
							pd.setTitle("Registering...");
							pd.setMessage("Please wait");
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
						          String link = MainActivity.serverip+"Users";
						          URL url = new URL(MainActivity.serverip+"Users");

						          con = (HttpURLConnection) url.openConnection();
						          con.setReadTimeout(10000);      
						          con.setConnectTimeout(15000);   
						          con.setRequestMethod("POST");
						          con.setDoOutput(true);
						          con.setDoInput(true);
						          con.setRequestProperty("Content-Type", "application/json");

						          payload = "{\"Name\":\"" + et1.getText().toString() + "\",\"Username\":\"" + et2.getText().toString() + "\",\"Password\":\"" + et3.getText().toString() + "\",\"CcardType\":\"" + Cctype + "\",\"CcardNumber\":" + et5.getText().toString() + ",\"CcardValidation\":\"" + Ccdate +  "\"}";
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
						        if(p.equals("ok"))
						        	response=1;
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
								register.setEnabled(true);
								switch(response) {
									case 1:
									{
										AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
												context);
								 
											// set title
											alertDialogBuilder.setTitle("Success");
								 
											// set dialog message
											alertDialogBuilder
												.setMessage("You've been successfully Registered")
												.setCancelable(false)
												.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog,int id) {
														Register.this.finish();
													    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
													}
												  });
								 
												// create alert dialog
												AlertDialog alertDialog = alertDialogBuilder.create();
								 
												// show it
												alertDialog.show();
									}
										break;
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

								}
							}
						}
							
					};
					task.execute((Void[])null);
				}

	 
			});
	 
		}

	}