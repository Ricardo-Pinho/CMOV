package com.feup.validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.feup.validator.R;
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

public class GetDataOffline extends Activity {
	
		private ProgressDialog pd;
		private TableLayout tb;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.get_data);
			final Context context = this;
								tb = (TableLayout)findViewById(R.id.TableLayout);
								for(int i=0; i<MainActivity.validatedTickets.size();i++)
								{
									LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
									
									LayoutParams tp = new LayoutParams(0,LayoutParams.WRAP_CONTENT);
									tp.weight=1;
									TableRow tr = new TableRow(GetDataOffline.this);
									tr.setLayoutParams(lp);
									TextView Id = new TextView(GetDataOffline.this);
									Id.setLayoutParams(tp);
									Id.setTextColor(Color.WHITE);
									Id.setGravity(Gravity.CENTER);
									Id.setText(MainActivity.validatedTickets.get(i).Id);
							        TextView type = new TextView(GetDataOffline.this);
							        type.setLayoutParams(tp);
							        type.setTextColor(Color.WHITE);
							        type.setGravity(Gravity.CENTER);
									type.setText(MainActivity.validatedTickets.get(i).Type);
							        TextView user = new TextView(GetDataOffline.this);
							        user.setLayoutParams(tp);
							        user.setTextColor(Color.WHITE);
							        user.setGravity(Gravity.CENTER);
									user.setText(MainActivity.validatedTickets.get(i).UserId+"("+MainActivity.validatedTickets.get(i).Nickname+")");
							        TextView usedtime = new TextView(GetDataOffline.this);
							        usedtime.setLayoutParams(tp);
							        usedtime.setTextColor(Color.WHITE);
							        usedtime.setGravity(Gravity.CENTER);
							        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
							        usedtime.setText(sdf.format(MainActivity.validatedTickets.get(i).ValidatedTime.getTime()));
									tr.addView(Id);
									tr.addView(type);
									tr.addView(user);
									tr.addView(usedtime);
									tb.addView(tr, i+1);
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
