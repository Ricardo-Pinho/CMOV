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

public class MainMenu extends Activity {

	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private Button button5;
	private Button button6;
	//test
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.main_menu);
			addListenerOnButton1();
			addListenerOnButton2();
			addListenerOnButton3();
			addListenerOnButton4();
			addListenerOnButton5();
			addListenerOnButton6();
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.menu, menu);
			return true;
		}
		
		@Override
		public void onBackPressed() 
		{

		    this.finish();
		    overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
		    return;
		}
		
		public void addListenerOnButton1() {
			 
			final Context context = this;
	 
			button1 = (Button) findViewById(R.id.button1);
	 
			button1.setOnClickListener(new OnClickListener() {
	 
				@Override
				public void onClick(View arg0) {
	 
				    Intent intent = new Intent(context, BuyTickets.class);
	                startActivity(intent);   
	                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
	 
				}
	 
			});
	 
		}
		
		public void addListenerOnButton2() {
			 
			final Context context = this;
	 
			button2 = (Button) findViewById(R.id.button2);
	 
			button2.setOnClickListener(new OnClickListener() {
	 
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
	 
			button3 = (Button) findViewById(R.id.button3);
	 
			button3.setOnClickListener(new OnClickListener() {
	 
				@Override
				public void onClick(View arg0) {
	 
				    Intent intent = new Intent(context, PurchaseHistory.class);
	                startActivity(intent);
	                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
	 
				}
	 
			});
	 
		}
		
		public void addListenerOnButton4() {
			 
			final Context context = this;
	 
			button4 = (Button) findViewById(R.id.button4);
	 
			button4.setOnClickListener(new OnClickListener() {
	 
				@Override
				public void onClick(View arg0) {
	 
				    Intent intent = new Intent(context, TicketHistory.class);
	                startActivity(intent);
	                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
	 
				}
	 
			});
	 
		}
		
		public void addListenerOnButton5() {
			 
			final Context context = this;
	 
			button5 = (Button) findViewById(R.id.button5);
	 
			button5.setOnClickListener(new OnClickListener() {
	 
				@Override
				public void onClick(View arg0) {
	 
				    Intent intent = new Intent(context, AvailableTickets.class);
	                startActivity(intent);
	                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
	 
				}
	 
			});
	 
		}
		
		public void addListenerOnButton6() {
			 
			final Context context = this;
	 
			button6 = (Button) findViewById(R.id.button6);
	 
			button6.setOnClickListener(new OnClickListener() {
	 
				@Override
				public void onClick(View arg0) {
	 
				    Intent intent = new Intent(context, AvailableTickets.class);
	                startActivity(intent);
	                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
	 
				}
	 
			});
	 
		}
		

	}
