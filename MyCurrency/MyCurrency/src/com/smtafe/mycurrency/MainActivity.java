package com.smtafe.mycurrency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	EditText inputValue;
	TextView outputValue, json_errors;
	String URL;
	String access_key;
	String JSON_Call;

	// http://apilayer.net/api/live?
	// access_key=dd8c8e9ec8a9ef78159969984c0c1d93
	// &currencies=EUR,GBP,AUD&source=USD&format=1

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		inputValue = (EditText) findViewById(R.id.editTextInput);	
		json_errors = (TextView)findViewById(R.id.textViewErrors);
		

	} // onCreate

private void getCurrency(){
	new GetQuotes().execute();
}

	private class GetQuotes extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected Void doInBackground(Void... arg0) {
			outputValue = (TextView) findViewById(R.id.textViewResult);

			String url = "http://apilayer.net/api/live?access_key=dd8c8e9ec8a9ef78159969984c0c1d93&currencies=EUR,GBP,AUD&source=USD&format=1";
			String jsonStr = "";
			try {
				jsonStr = getJson(url);
			} catch (ClientProtocolException e1) {
				json_errors.setText(e1.toString());

			} catch (IOException e1) {
				json_errors.setText(e1.toString());
			}
			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					JSONObject quotes = jsonObj.getJSONObject("quotes");
					double USDEUR = quotes.getDouble("USDEUR");
					double USDGBP = 9.9; // quotes.getDouble("USDGBP");
					double USDAUD = 8.8; //quotes.getDouble("USDAUD");
					outputValue.setText("quotes " + USDEUR + " " + USDGBP + " "
							+ USDAUD);

				} catch (final JSONException e) {
					json_errors.setText("Json parsing error: " + e.getMessage());
				}

			} else {
				json_errors.setText("Couldn't get json from server");
			}
			return null;
		}
		
	} // GetQuotes

	
	
	public String getJson(String url) throws ClientProtocolException,
			IOException {

		StringBuilder build = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = client.execute(httpGet);
		HttpEntity entity = response.getEntity();
		InputStream content = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				content));
		String con;
		while ((con = reader.readLine()) != null) {
			build.append(con);
		}
		return build.toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
