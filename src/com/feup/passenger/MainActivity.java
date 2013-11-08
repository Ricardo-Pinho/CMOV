package com.feup.passenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.view.View.OnClickListener;


public class MainActivity extends Activity {
	
	public static final String serverip="http://192.168.1.69:8080/Server/BusServer/";
	public static User usr = new User();
	
	private Button login;
	private Button register;
	private Button offline_login;
	private ProgressDialog pd;
	private EditText et1, et2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TabHost tabs=(TabHost)findViewById(R.id.tabhost);
		tabs.setup();
		TabHost.TabSpec spec=tabs.newTabSpec("Online");
		spec.setContent(R.id.tab1);
		spec.setIndicator("Online");
		tabs.addTab(spec);
		spec=tabs.newTabSpec("Offline");
		spec.setContent(R.id.tab2);
		spec.setIndicator("Offline");
		tabs.addTab(spec);
		tabs.setCurrentTab(0); 
		addListenerOnButton1();
		addListenerOnButton2();
		addListenerOnButton3();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void addListenerOnButton1() {
		 
		final Context context = this;
 
		login = (Button) findViewById(R.id.button1);
 
		login.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
			    Intent intent = new Intent(context, Login.class);
                startActivity(intent);   
                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
 
			}
 
		});
 
	}
	
	public void addListenerOnButton2() {
		 
		final Context context = this;
 
		register = (Button) findViewById(R.id.button2);
 
		register.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
			    Intent intent = new Intent(context, Register.class);
                startActivity(intent);
                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
 
			}
 
		});
 
	}
	
	public void addListenerOnButton3() {
		 
		final Context context = this;
 
		offline_login = (Button) findViewById(R.id.offline_login_button);
 
		offline_login.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View v) {
				v.setEnabled(false);
				AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
					boolean response=true;
					boolean loadresponse=false;
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
								et1 = (EditText) findViewById(R.id.offline_username);
								et2 = (EditText) findViewById(R.id.offline_password);
								
								usr.Nickname = et1.getText().toString();
								loadresponse = usr.Load();
								usr.Password = et2.getText().toString();
								if(loadresponse)
								{
									response=false;
								}
								if(response)
								{
									try {
										response = usr.authenticate(usr.Password, usr.getEncryptedPassword(), usr.getSalt());
									} catch (NoSuchAlgorithmException e) {
										Log.d("Error", "error deSerializing User: NoSuchAlgorithmxception");
										e.printStackTrace();
									} catch (InvalidKeySpecException e) {
										Log.d("Error", "error deSerializing User: InvalidKeySpecException");
										e.printStackTrace();
									}
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
							offline_login.setEnabled(true);
							if(response)
								{
									Intent intent = new Intent(context, OfflineMenu.class);
									startActivity(intent);
									overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
								}
								else if(loadresponse)
								{
									new User();
									AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
											context);
							 
										// set title
										alertDialogBuilder.setTitle("User Not Found");
							 
										// set dialog message
										alertDialogBuilder
											.setMessage("You need to login online at least once before logging offline.")
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
								else
								{
									new User();
									AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
											context);
							 
										// set title
										alertDialogBuilder.setTitle("Login Failed");
							 
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
						}
					}
						
				};
				task.execute((Void[])null);
		/*
			    Intent intent = new Intent(context, Register.class);
                startActivity(intent);
                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);*/
 
			}
 
		});
 
	}
	
	/*@Override
	public void onBackPressed() 
	{

	    this.finish();
	    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
	    return;
	}*/

}
