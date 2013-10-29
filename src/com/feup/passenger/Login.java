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
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {
		private Button login;
		private ProgressDialog pd;
		private EditText et1, et2;
	
	//test
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.login);
			addListenerOnButton();
			et1 = (EditText) findViewById(R.id.username);
		    et2 = (EditText) findViewById(R.id.password);
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.login, menu);
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
	 
			login = (Button) findViewById(R.id.login_button);
	 
			login.setOnClickListener(new OnClickListener() {
	 
				@Override
				public void onClick(View v) {
					v.setEnabled(false);
					AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
						int response=-1;	
						@Override
						protected void onPreExecute() {
							pd = new ProgressDialog(context);
							pd.setTitle("Authenticating...");
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
						          String link = MainActivity.serverip+"Users/Login";
						          URL url = new URL(link);

						          con = (HttpURLConnection) url.openConnection();
						          con.setReadTimeout(10000);      
						          con.setConnectTimeout(15000);   
						          con.setRequestMethod("POST");
						          con.setDoOutput(true);
						          con.setDoInput(true);
						          con.setRequestProperty("Content-Type", "application/json");

						          payload = "{\"Username\":\"" + et1.getText().toString() + "\",\"Password\":\"" + et2.getText().toString() + "\"}";
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
								login.setEnabled(true);
								switch(response) {
									case -2:
									{
										AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
												context);
								 
											// set title
											alertDialogBuilder.setTitle("Error");
								 
											// set dialog message
											alertDialogBuilder
												.setMessage("Wrong Username or Password. Please try again.")
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
										MainActivity.Id=response;
										Login.this.finish();
										Intent intent = new Intent(context, MainMenu.class);
						                startActivity(intent);
									    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
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
