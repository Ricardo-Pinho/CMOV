package com.feup.passenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.feup.passenger.R;
import com.google.zxing.integration.android.IntentIntegrator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Validate extends Activity {

	private final int button_base_id = 2131361847;
	private RadioGroup rg;
	private Button bt_validate;
	private ProgressDialog pd;
	private TextView aux;
	private IntentIntegrator intentI = new IntentIntegrator(this);
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.validate);
		
		addListenerOnButton();
		
		aux = (TextView)findViewById(R.id.textView2);
		
		rg = (RadioGroup)findViewById(R.id.radioGroup1);
		
		rg.clearCheck();
		
	}



	private void addListenerOnButton() {
		final Context context = this;
		 
		bt_validate = (Button) findViewById(R.id.button_val);
 
		bt_validate.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View v) {
				int ticket_type = 0;
				ticket_type = rg.getCheckedRadioButtonId();
				
				String aux;
				
				aux = Integer.toString(MainActivity.Id) + " " + Integer.toString(ticket_type-button_base_id);
				
				intentI.shareText(aux);
			}
				
		});
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