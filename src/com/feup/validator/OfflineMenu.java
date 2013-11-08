package com.feup.validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.json.JSONArray;

import com.feup.validator.Tickets;
import com.google.zxing.integration.android.IntentIntegrator;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;


public class OfflineMenu extends Activity {	
	
	private Button getData;
	private Button uploadData;
	private Button Validate;
	private Button Showdata;
	private ProgressDialog pd;
	private IntentIntegrator intentI = new IntentIntegrator(this);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offlinemenu);
		addListenerOnButton1();
		addListenerOnButton2();
		addListenerOnButton3();
		addListenerOnButton4();
		TextView tv = (TextView)findViewById(R.id.BusIdLog);
		tv.setText(Integer.toString(MainActivity.BusId));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void addListenerOnButton1() {
		 
		final Context context = this;
 
		getData = (Button) findViewById(R.id.button1);
 
		getData.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
				Intent intent = new Intent(context, GetDataOffline.class);
                startActivity(intent);
                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
 
			}
 
		});
 
	}
	
	public void addListenerOnButton2() {
		 
		final Context context = this;
 
		uploadData = (Button) findViewById(R.id.button2);
 
		uploadData.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
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
							String payload = "Error";
					        HttpURLConnection con = null;
					        try {
					          // RESTful URL for POST
					          String link = MainActivity.serverip+"Tickets/TerminalOfflineValidate/" + MainActivity.key;
					          URL url = new URL(link);

					          con = (HttpURLConnection) url.openConnection();
					          con.setReadTimeout(10000);      
					          con.setConnectTimeout(15000);   
					          con.setRequestMethod("POST");
					          con.setDoOutput(true);
					          con.setDoInput(true);
					          con.setRequestProperty("Content-Type", "application/json");
					          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
					          payload = "{\"SyncDate\":\"\",\"Tickets\":[";
					          for(int i = 0; i<MainActivity.validatedTickets.size();i++)
					          {
					        	  payload = payload.concat("{\"Id\":\"" + MainActivity.validatedTickets.get(i).Id +"\",\"UserId\":\""+ MainActivity.validatedTickets.get(i).UserId +"\",\"State\":1,\"Type\":\"" + MainActivity.validatedTickets.get(i).Type + "\",\"ValidatedTime\":\""+ sdf.format(MainActivity.validatedTickets.get(i).ValidatedTime.getTime()) + "\",\"BusId\":"+ Integer.toString(MainActivity.BusId) +"}");
						          if(i<MainActivity.validatedTickets.size()-1)
						        	  payload = payload.concat(",");
					          }
					          payload = payload.concat("]}");
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
											.setMessage("Something Went Wrong.")
											.setCancelable(false)
											.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,int id) {
													dialog.cancel();
													OfflineMenu.this.finish();
												    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
												}
											  });
							 
											// create alert dialog
											AlertDialog alertDialog = alertDialogBuilder.create();
							 
											// show it
											alertDialog.show();

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
											.setMessage("Some tickets were not found.")
											.setCancelable(false)
											.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,int id) {
													dialog.cancel();
													OfflineMenu.this.finish();
												    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
												}
											  });
							 
											// create alert dialog
											AlertDialog alertDialog = alertDialogBuilder.create();
							 
											// show it
											alertDialog.show();

								}
								break;
								case -3:
								{
									AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
											context);
							 
										// set title
										alertDialogBuilder.setTitle("Error");
							 
										// set dialog message
										alertDialogBuilder
											.setMessage("Some tickets were already validated.")
											.setCancelable(false)
											.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,int id) {
													dialog.cancel();
													OfflineMenu.this.finish();
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
									MainActivity.validatedTickets = new ArrayList<Tickets>();
									AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
											context);
							 
										// set title
										alertDialogBuilder.setTitle("Success");
							 
										// set dialog message
										alertDialogBuilder
											.setMessage("Tickets uploaded to the server correctly.")
											.setCancelable(false)
											.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,int id) {
													dialog.cancel();
													OfflineMenu.this.finish();
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
	
	public void addListenerOnButton3() {
		 
		final Context context = this;
 
		Validate = (Button) findViewById(R.id.button3);
 
		Validate.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
			    Intent intent = new Intent(context, ValidateOffline.class);
                startActivity(intent);
                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
 
			}
 
		});
 
	}
	
	public void addListenerOnButton4() {
		 
		final Context context = this;
 
		Showdata = (Button) findViewById(R.id.button4);
 
		Showdata.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
			  String aux = "";
			  for(int i = 0; i<MainActivity.validatedTickets.size();i++)
			  {
				  aux = aux.concat(MainActivity.validatedTickets.get(i).Id+";"+MainActivity.validatedTickets.get(i).UserId+";"+MainActivity.validatedTickets.get(i).Type+";"+sdf.format(MainActivity.validatedTickets.get(i).ValidatedTime.getTime())+";"+MainActivity.validatedTickets.get(i).Nickname+";"+Integer.toString(MainActivity.validatedTickets.get(i).BusId));
				  aux = aux.concat("/");
			  }
			  aux = aux.concat("end");
			  intentI.shareText(aux);
			}
 
		});
 
	}
	
	@Override
	public void onBackPressed() 
	{

	    this.finish();
	    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
	    return;
	}

}
