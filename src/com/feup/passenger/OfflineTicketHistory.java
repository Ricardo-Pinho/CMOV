package com.feup.passenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.feup.passenger.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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

public class OfflineTicketHistory extends Activity {
	
		private ProgressDialog pd;
		private TableLayout tb;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.offline_ticket_history);
			tb = (TableLayout)findViewById(R.id.TableLayout);
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
		    int rowno = 1;
			for(int i=0; i<MainActivity.usr.tickets.size();i++)
			{
				if(MainActivity.usr.tickets.get(i).Status==1){
					LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
					
					LayoutParams tp = new LayoutParams(0,LayoutParams.WRAP_CONTENT);
					tp.weight=1;
					TableRow tr = new TableRow(OfflineTicketHistory.this);
					tr.setLayoutParams(lp);
					TextView date = new TextView(OfflineTicketHistory.this);
					date.setLayoutParams(tp);
					date.setTextColor(Color.WHITE);
					date.setGravity(Gravity.CENTER);
					String dateString = sdf.format(MainActivity.usr.tickets.get(i).ValidatedTime.getTime());
			        date.setText(dateString);
			        TextView type = new TextView(OfflineTicketHistory.this);
			        type.setLayoutParams(tp);
			        type.setTextColor(Color.WHITE);
			        type.setGravity(Gravity.CENTER);
			        type.setText(MainActivity.usr.tickets.get(i).Type);
			        TextView bus = new TextView(OfflineTicketHistory.this);
			        bus.setLayoutParams(tp);
			        bus.setTextColor(Color.WHITE);
			        bus.setGravity(Gravity.CENTER);
			        bus.setText(Integer.toString(MainActivity.usr.tickets.get(i).BusId));
					tr.addView(date);
					tr.addView(type);
					tr.addView(bus);
					tb.addView(tr, rowno);
					rowno++;
				}
			}
				
			
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
