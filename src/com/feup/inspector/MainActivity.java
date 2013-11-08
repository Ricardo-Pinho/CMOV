package com.feup.inspector;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;


public class MainActivity extends Activity {
	
	public static final String serverip="http://192.168.1.69:8080/Server/BusServer/";
	public static String key;
	public static String BusId="-1";
	public static ArrayList<Tickets> validatedTickets = new ArrayList<Tickets>();
	public static ArrayList<String> verifiedUsers = new ArrayList<String>();
	
	private Button onlineMode;
	private Button offlineMode;
	private Button setBusId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
 
		onlineMode = (Button) findViewById(R.id.button1);
 
		onlineMode.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
					Intent intent = new Intent(context, OnlineMenu.class);
	                startActivity(intent);   
	                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
			}
		});
 
	}
	
	public void addListenerOnButton2() {
		 
		final Context context = this;
 
		offlineMode = (Button) findViewById(R.id.button2);
 
		offlineMode.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				
					Intent intent = new Intent(context, OfflineMenu.class);
	                startActivity(intent);   
	                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
			}
		});
 
	}
	
	public void addListenerOnButton3() {
		 
		final Context context = this;
 
		setBusId = (Button) findViewById(R.id.button3);
 
		setBusId.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				
				AlertDialog.Builder editalert = new AlertDialog.Builder(context);

				editalert.setTitle("Set Bus Id");


				final EditText input = new EditText(context);
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				        LinearLayout.LayoutParams.MATCH_PARENT,
				        LinearLayout.LayoutParams.MATCH_PARENT);
				input.setLayoutParams(lp);
				editalert.setView(input);

				editalert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int whichButton) {
				    	Log.d("BusId", "its"+input.getText().toString());
				    	if(!input.getText().toString().equals(""))
				    	{
				    		if(Integer.parseInt(input.getText().toString())>0)
				    			MainActivity.BusId = input.getText().toString();
				    	}
				    	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

				    }
				});
				// create alert dialog
				AlertDialog alertDialog = editalert.create();
 
				// show it
				alertDialog.show();
				input.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
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
