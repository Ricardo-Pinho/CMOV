package com.feup.validator;

import java.util.ArrayList;

import com.feup.validator.Tickets;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;


public class MainActivity extends Activity {
	
	public static final String serverip="http://192.168.1.69:8080/Server/BusServer/";
	public static String key="1";
	public static ArrayList<Tickets> validatedTickets = new ArrayList<Tickets>();
	
	private Button getData;
	private Button validate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addListenerOnButton1();
		addListenerOnButton2();
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
 
			    Intent intent = new Intent(context, GetData.class);
                startActivity(intent);   
                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
 
			}
 
		});
 
	}
	
	public void addListenerOnButton2() {
		 
		final Context context = this;
 
		validate = (Button) findViewById(R.id.button2);
 
		validate.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
			    Intent intent = new Intent(context, Validate.class);
                startActivity(intent);
                overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
 
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
