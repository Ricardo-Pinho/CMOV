package com.feup.passenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import com.feup.passenger.R;
import com.google.zxing.integration.android.IntentIntegrator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class ValidateOffline extends Activity {

	private RadioGroup rg;
	private RadioButton b1,b2,b3, b4, b5;
	private Spinner sp;
	private Button bt_validate;
	private ProgressDialog pd;
	private TextView aux;
	ArrayAdapter<String> spinnerAdapter;
	private IntentIntegrator intentI = new IntentIntegrator(this);
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.validate_offline);
		
		addListenerOnButton();
		
		aux = (TextView)findViewById(R.id.textView2);
		
		rg = (RadioGroup)findViewById(R.id.radioGroup1);
		
		b1 = (RadioButton)findViewById(R.id.radio_t1);
		b2 = (RadioButton)findViewById(R.id.radio_t2);
		b3 = (RadioButton)findViewById(R.id.radio_t3);
		b4 = (RadioButton)findViewById(R.id.radio_t4);
		b5 = (RadioButton)findViewById(R.id.radio_t5);
		b1.setId(1);
		b2.setId(2);
		b3.setId(3);
		b4.setId(1);
		b5.setId(2);
		rg.check(1);
		b4.setChecked(true);
		b5.setChecked(false);
		sp = (Spinner)findViewById(R.id.spinner1);
		spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(spinnerAdapter);
		for(int i=0; i<MainActivity.usr.busIds.size();i++)
		{
			spinnerAdapter.add(Integer.toString(MainActivity.usr.busIds.get(i)));
		}
		spinnerAdapter.notifyDataSetChanged();
		addListenerOnRadioButton1();
		addListenerOnRadioButton2();
		
	}



	private void addListenerOnButton() {
		final Context context = this;
		 
		bt_validate = (Button) findViewById(R.id.button_val);
		bt_validate.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);
				
					// set title
					alertDialogBuilder.setTitle("Are you sure?");
		 
					// set dialog message
					alertDialogBuilder
						.setMessage("Consumes a ticket.")
						.setCancelable(false)
						.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								Tickets newticket = new Tickets();
								int busid=-1;
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
								dialog.cancel();
								int ticket_type = 0;
								ticket_type = rg.getCheckedRadioButtonId();
								boolean hastickets=false, alreadyvalidated=false;
								String aux;
								if (b4.isChecked())
								{
									if(sp.getSelectedItem()!=null)
										busid=Integer.parseInt(sp.getSelectedItem().toString());
								}
								if(b5.isChecked())
								{
									EditText et1 = (EditText) findViewById(R.id.editText1);
									if(!et1.getText().toString().equals(""))
										busid=Integer.parseInt(et1.getText().toString());
								}
								
								if(busid==-1)
								{
									AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
											context);
							 
										// set title
										alertDialogBuilder.setTitle("Error");
							 
										// set dialog message
										alertDialogBuilder
											.setMessage("You Did not select a valid bus id.")
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
									switch (ticket_type)
									{
										case 1:
										{
											for(int i = 0; i<MainActivity.usr.tickets.size();i++)
											{
												if(MainActivity.usr.tickets.get(i).Status==1 && MainActivity.usr.tickets.get(i).BusId==busid)
												{
													Calendar currdate= Calendar.getInstance();
													DateTime dtime1 = new DateTime(currdate);
													DateTime dtime2 = new DateTime(MainActivity.usr.tickets.get(i).ValidatedTime);
													if(Hours.hoursBetween(dtime2,dtime1).getHours()<1 && MainActivity.usr.tickets.get(i).Type.equals("T3"))
														{
															newticket.ValidatedTime = MainActivity.usr.tickets.get(i).ValidatedTime;
															newticket.Id = MainActivity.usr.tickets.get(i).Id;
															alreadyvalidated=true;
															hastickets=true;
															break;
														}
													if(Minutes.minutesBetween(dtime2,dtime1).getMinutes()<31 && MainActivity.usr.tickets.get(i).Type.equals("T2"))
													{
														newticket.ValidatedTime = MainActivity.usr.tickets.get(i).ValidatedTime;
														newticket.Id = MainActivity.usr.tickets.get(i).Id;
														alreadyvalidated=true;
														hastickets=true;
														break;
													}
													if(Minutes.minutesBetween(dtime2,dtime1).getMinutes()<16 && MainActivity.usr.tickets.get(i).Type.equals("T1"))
													{
														newticket.ValidatedTime = MainActivity.usr.tickets.get(i).ValidatedTime;
														newticket.Id = MainActivity.usr.tickets.get(i).Id;
														alreadyvalidated=true;
														hastickets=true;
														break;
													}
												}
												if (MainActivity.usr.tickets.get(i).Status==2 && MainActivity.usr.tickets.get(i).Type.equals("T1"))
														{
															MainActivity.usr.tickets.get(i).Status=1;
															MainActivity.usr.tickets.get(i).BusId=busid;
															MainActivity.usr.tickets.get(i).ValidatedTime = Calendar.getInstance();
															newticket.ValidatedTime = MainActivity.usr.tickets.get(i).ValidatedTime;
															newticket.Id = MainActivity.usr.tickets.get(i).Id;
															hastickets=true;
															break;
														}
											}
										}
										break;
										case 2:
										{
											for(int i = 0; i<MainActivity.usr.tickets.size();i++)
											{
												if(MainActivity.usr.tickets.get(i).Status==1 && MainActivity.usr.tickets.get(i).BusId==busid)
												{
													Calendar currdate= Calendar.getInstance();
													DateTime dtime1 = new DateTime(currdate);
													DateTime dtime2 = new DateTime(MainActivity.usr.tickets.get(i).ValidatedTime);
													if(Hours.hoursBetween(dtime2,dtime1).getHours()<1 && MainActivity.usr.tickets.get(i).Type.equals("T3"))
														{
															newticket.ValidatedTime = MainActivity.usr.tickets.get(i).ValidatedTime;
															newticket.Id = MainActivity.usr.tickets.get(i).Id;
															alreadyvalidated=true;
															hastickets=true;
															break;
														}
													if(Minutes.minutesBetween(dtime2,dtime1).getMinutes()<31 && MainActivity.usr.tickets.get(i).Type.equals("T2"))
													{
														newticket.ValidatedTime = MainActivity.usr.tickets.get(i).ValidatedTime;
														newticket.Id = MainActivity.usr.tickets.get(i).Id;
														alreadyvalidated=true;
														hastickets=true;
														break;
													}
													if(Minutes.minutesBetween(dtime2,dtime1).getMinutes()<16 && MainActivity.usr.tickets.get(i).Type.equals("T1"))
													{
														newticket.ValidatedTime = MainActivity.usr.tickets.get(i).ValidatedTime;
														newticket.Id = MainActivity.usr.tickets.get(i).Id;
														alreadyvalidated=true;
														hastickets=true;
														break;
													}
												}
												if (MainActivity.usr.tickets.get(i).Status==2 && MainActivity.usr.tickets.get(i).Type.equals("T2"))
														{
															MainActivity.usr.tickets.get(i).Status=1;
															MainActivity.usr.tickets.get(i).BusId=busid;
															MainActivity.usr.tickets.get(i).ValidatedTime = Calendar.getInstance();
															newticket.ValidatedTime = MainActivity.usr.tickets.get(i).ValidatedTime;
															newticket.Id = MainActivity.usr.tickets.get(i).Id;
															hastickets=true;
															break;
														}
											}
										}
										break;
										case 3:
										{
											for(int i = 0; i<MainActivity.usr.tickets.size();i++)
											{
												if(MainActivity.usr.tickets.get(i).Status==1 && MainActivity.usr.tickets.get(i).BusId==busid)
												{
													Calendar currdate= Calendar.getInstance();
													DateTime dtime1 = new DateTime(currdate);
													DateTime dtime2 = new DateTime(MainActivity.usr.tickets.get(i).ValidatedTime);
													if(Hours.hoursBetween(dtime2,dtime1).getHours()<1 && MainActivity.usr.tickets.get(i).Type.equals("T3"))
														{
															newticket.ValidatedTime = MainActivity.usr.tickets.get(i).ValidatedTime;
															newticket.Id = MainActivity.usr.tickets.get(i).Id;
															alreadyvalidated=true;
															hastickets=true;
															break;
														}
													if(Minutes.minutesBetween(dtime2,dtime1).getMinutes()<31 && MainActivity.usr.tickets.get(i).Type.equals("T2"))
													{
														newticket.ValidatedTime = MainActivity.usr.tickets.get(i).ValidatedTime;
														newticket.Id = MainActivity.usr.tickets.get(i).Id;
														alreadyvalidated=true;
														hastickets=true;
														break;
													}
													if(Minutes.minutesBetween(dtime2,dtime1).getMinutes()<16 && MainActivity.usr.tickets.get(i).Type.equals("T1"))
													{
														newticket.ValidatedTime = MainActivity.usr.tickets.get(i).ValidatedTime;
														newticket.Id = MainActivity.usr.tickets.get(i).Id;
														alreadyvalidated=true;
														hastickets=true;
														break;
													}
												}
												if (MainActivity.usr.tickets.get(i).Status==2 && MainActivity.usr.tickets.get(i).Type.equals("T3"))
														{
															MainActivity.usr.tickets.get(i).Status=1;
															MainActivity.usr.tickets.get(i).BusId=busid;
															MainActivity.usr.tickets.get(i).ValidatedTime = Calendar.getInstance();
															newticket.ValidatedTime = MainActivity.usr.tickets.get(i).ValidatedTime;
															newticket.Id = MainActivity.usr.tickets.get(i).Id;
															hastickets=true;
															break;
														}
											}
										}
										break;
									}
									if(!hastickets)
									{
										AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
												context);
								 
											// set title
											alertDialogBuilder.setTitle("Error");
								 
											// set dialog message
											alertDialogBuilder
												.setMessage("You Don't Have Any Tickets of this type.")
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
									else if(alreadyvalidated)
									{
										aux = newticket.Id + ";" + MainActivity.usr.Id + ";" + Integer.toString(ticket_type) + ";" + sdf.format(newticket.ValidatedTime.getTime()) + ";" + MainActivity.usr.Nickname;
										
										intentI.shareText(aux);
										ValidateOffline.this.finish();
									    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
									}
									else{
										MainActivity.usr.Save();
										aux = newticket.Id + ";" + MainActivity.usr.Id + ";" + Integer.toString(ticket_type) + ";" + sdf.format(newticket.ValidatedTime.getTime()) + ";" + MainActivity.usr.Nickname;
										
										intentI.shareText(aux);
										ValidateOffline.this.finish();
									    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
										}
									}
								}
						  })
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
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
	
	private void addListenerOnRadioButton1() {
		final Context context = this;
 
		b4.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View v) {
				if(b5.isChecked())
					b5.setChecked(false);
			}
				
		});
	}
	
	private void addListenerOnRadioButton2() {
		final Context context = this;
		 
		b5.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View v) {
				if(b4.isChecked())
					b4.setChecked(false);
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

		
		/*v.setEnabled(false);
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			
			int response=-1;	
			
			@Override
			protected void onPreExecute() {
				InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
				pd = new ProgressDialog(context);
				pd.setTitle("Checking Tickets");
				pd.setMessage("Please wait.");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}
			@Override
			protected Void doInBackground(Void... arg0) {
				try {
					String ticket_type = "Error";
			        HttpURLConnection con = null;
			        try {
			          // RESTful URL for POST
			          String link = MainActivity.serverip+"ValidateTickets";
			          URL url = new URL(link);

			          con = (HttpURLConnection) url.openConnection();
			          con.setReadTimeout(10000);      
			          con.setConnectTimeout(15000);   
			          con.setRequestMethod("POST");
			          con.setDoOutput(true);
			          con.setDoInput(true);
			          con.setRequestProperty("Content-Type", "application/json");

			          ticket_type = "";
			          OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
			          writer.write(ticket_type, 0, ticket_type.length());
			          writer.close();
			          con.connect();
			          BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8" ));
			          ticket_type = reader.readLine();
			          reader.close();
			        }
			        catch (IOException e) {
			        	return null;
			        }
			        finally {
			          if (con != null)
			            con.disconnect();
			        }
			        final String p = ticket_type;
			        response=Integer.valueOf(p);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					return null;
				}
				return null;
			}
		};
	}*/