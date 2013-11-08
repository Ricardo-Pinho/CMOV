package com.feup.passenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;


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

public class OfflineAvailableTickets extends Activity {
	
		private ProgressDialog pd;
		private TableRow tr;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.available_tickets);
			int T1No=0, T2No=0,T3No=0;
			for(int i=0; i<MainActivity.usr.tickets.size();i++)
			{
				if(MainActivity.usr.tickets.get(i).Status==2)
				{
					if(MainActivity.usr.tickets.get(i).Type.equals("T1"))
						T1No++;
					else if(MainActivity.usr.tickets.get(i).Type.equals("T2"))
						T2No++;
					else
						T3No++;
				}
			}
			
			tr = (TableRow)findViewById(R.id.tableRow2);
				
				LayoutParams tp = new LayoutParams(0,LayoutParams.WRAP_CONTENT);
				tp.weight=1;
				
				TextView T1 = new TextView(OfflineAvailableTickets.this);
				T1.setLayoutParams(tp);
				T1.setTextColor(Color.WHITE);
				T1.setGravity(Gravity.CENTER);
		        T1.setText(Integer.toString(T1No));
		        TextView T2 = new TextView(OfflineAvailableTickets.this);
		        T2.setLayoutParams(tp);
		        T2.setTextColor(Color.WHITE);
		        T2.setGravity(Gravity.CENTER);
		        T2.setText(Integer.toString(T2No));
		        TextView T3 = new TextView(OfflineAvailableTickets.this);
		        T3.setLayoutParams(tp);
		        T3.setTextColor(Color.WHITE);
		        T3.setGravity(Gravity.CENTER);
		        T3.setText(Integer.toString(T3No));
				tr.addView(T1);
				tr.addView(T2);
				tr.addView(T3);
			
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
