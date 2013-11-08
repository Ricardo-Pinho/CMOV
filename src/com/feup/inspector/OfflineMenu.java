package com.feup.inspector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;


public class OfflineMenu extends Activity {
	
	private Button getData;
	private Button viewData;
	private Button resetData;
	private Button verify;
	private ProgressDialog pd;
	private IntentIntegrator intentI = new IntentIntegrator(this);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		final Context context = this;
		if (scanResult != null) {
			if(scanResult.getContents() != null)
	          {  
				String aux = scanResult.getContents();
				Log.d("test", "coiso"+aux);
				String [] tickets = aux.split("/");
				String [] ts;
				Tickets newticket = new Tickets();
				if(tickets.length==0)
				{
					this.finish();
				    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
				    return;
				}
				for(int i = 0; i< (tickets.length-1);i++)
				{
					
					ts  = tickets[i].split(";");
					if(ts.length<6)
					{
						break;
						/*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);
				 
							// set title
							alertDialogBuilder.setTitle("Error");
				 
							// set dialog message
							alertDialogBuilder
								.setMessage("Incomplete Information.")
								.setCancelable(false)
								.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										dialog.cancel();
										OfflineMenu.this.finish();
									    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
									    return;
									}
								  });
				 
								// create alert dialog
								AlertDialog alertDialog = alertDialogBuilder.create();
				 
								// show it
								alertDialog.show();
								return;*/
					}
					Log.d("test2",ts[0]);
					newticket.Id=ts[0];
					newticket.UserId = ts[1];
					newticket.Type = ts[2];
					newticket.Nickname=ts[4];
					newticket.BusId=Integer.parseInt(ts[5]);
					try {
						newticket.ValidatedTime.setTime(sdf.parse(ts[3]));
					} catch (ParseException e) {
						Log.d("Error", "Error parsing Validation Date.");
						e.printStackTrace();
					}
					MainActivity.validatedTickets.add(newticket);
				}
	          }
	      else
		      {
			  	this.finish();
			    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
			    return;
		      }
		}
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
 
			// set title
			alertDialogBuilder.setTitle("Success");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("Data Imported Currectly.")
				.setCancelable(false)
				.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					    return;
					}
				  });
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
		
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offline_menu);
		addListenerOnButton1();
		addListenerOnButton2();
		addListenerOnButton3();
		addListenerOnButton4();
		if(!MainActivity.BusId.equals("-1"))
		{
			TextView tv = (TextView)findViewById(R.id.BusIdLog);
			tv.setText(MainActivity.BusId);
		}
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
 
				intentI.initiateScan();
 
			}
 
		});
 
	}
	
	public void addListenerOnButton2() {
		 
		final Context context = this;
 
		viewData = (Button) findViewById(R.id.button2);
 
		viewData.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
			    Intent intent = new Intent(context, ViewData.class);
                startActivity(intent);
                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
 
			}
 
		});
 
	}
	
	public void addListenerOnButton3() {
		 
		final Context context = this;
 
		verify = (Button) findViewById(R.id.button3);
 
		verify.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
			    Intent intent = new Intent(context, VerifyOffline.class);
                startActivity(intent);
                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
 
			}
 
		});
 
	}
	
	public void addListenerOnButton4() {
		 
		final Context context = this;
 
		resetData = (Button) findViewById(R.id.button4);
 
		resetData.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);
		 
					// set title
					alertDialogBuilder.setTitle("Success");
		 
					// set dialog message
					alertDialogBuilder
						.setMessage("Data Reseted")
						.setCancelable(false)
						.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								MainActivity.validatedTickets = new ArrayList<Tickets>();
								MainActivity.verifiedUsers = new ArrayList<String>();
								dialog.cancel();
							}
						  });
		 
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
						alertDialog.show();
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
