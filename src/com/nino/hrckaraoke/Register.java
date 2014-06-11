package com.nino.hrckaraoke;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {
	TextView registered;
	Button btn_login;
	Person person;

	static EditText name;
	static EditText uname;
	static EditText pass;
	static EditText email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		name = (EditText) findViewById(R.id.fullname);
		uname = (EditText) findViewById(R.id.username);
		pass = (EditText) findViewById(R.id.password);
		email = (EditText) findViewById(R.id.email);

		registered = (TextView) findViewById(R.id.registered);

		btn_login = (Button) findViewById(R.id.login_button);

		btn_login.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// new Registration().execute();

				if (!validate())
					Toast.makeText(getBaseContext(), "Enter some data!",
							Toast.LENGTH_LONG).show();
				// call AsynTask to perform network operation on separate thread
				new HttpAsyncTask()
						.execute("http://anujkothari.com/hrckaraoke/androidservice/user/register");

				/*
				 * Intent loginactivity = new Intent(Register.this,
				 * MainActivity.class); startActivity(loginactivity); finish();
				 */
			}
		});

		registered.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent loginactivity = new Intent(Register.this, Login.class);
				startActivity(loginactivity);
				// finish();
			}
		});

	}

	/**
	 * @param url
	 * @return
	 */
	public static String POST(String url) {
		InputStream inputStream = null;
		String result = "";
		JSONObject jsonObject = null;
		try {

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			StringEntity se = new StringEntity(" { \"mail\":\""
					+ email.getText().toString().trim() + "\",\"name\":\""
					+ uname.getText().toString().trim()
					+ "\",\"field_name\":{\"und\":[{ \"value\":\""
					+ name.getText().toString().trim() + "\",\"safe_value\":\""
					+ name.getText().toString().trim()
					+ "\"}]},\"field_phone_number\":{\"und\":[{ \"value\":\""
					+ pass.getText().toString().trim() + "\"}]}}");

			/*
			 * ","\"mail\":\"" +
			 * email.getText().toString().trim()+"\",\"name\":\"" +
			 * uname.getText().toString().trim()+"\"
			 */

			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			httpPost.setEntity(se);

			HttpResponse httpResponse = httpclient.execute(httpPost);
			String jsonResponse = EntityUtils
					.toString(httpResponse.getEntity());

			jsonObject = new JSONObject(jsonResponse);
			System.out.println("Hrcc " + jsonObject.toString());

			// 9. receive response as inputStream
			/*
			 * inputStream = httpResponse.getEntity().getContent();
			 * System.out.println("Register " +
			 * convertInputStreamToString(inputStream));
			 * 
			 * // 10. convert inputstream to string if (inputStream != null)
			 * result = convertInputStreamToString(inputStream); else result =
			 * "Did not work!";
			 */

			/*
			 * String json = "";
			 * 
			 * // 3. build jsonObject JSONObject jsonObject = new JSONObject();
			 * jsonObject.put("value", name.getText().toString().trim());
			 * jsonObject.put("name",uname.getText().toString().trim());
			 * jsonObject.put("mail", email.getText().toString().trim());
			 * 
			 * 
			 * // 4. convert JSONObject to JSON to String json =
			 * jsonObject.toString();
			 * 
			 * // ** Alternative way to convert Person object to JSON string
			 * usin Jackson Lib // ObjectMapper mapper = new ObjectMapper(); //
			 * json = mapper.writeValueAsString(person);
			 * 
			 * // 5. set json to StringEntity StringEntity se = new
			 * StringEntity(json);
			 * 
			 * // 6. set httpPost Entity httpPost.setEntity(se);
			 * 
			 * // 7. Set some headers to inform server about the type of the
			 * content httpPost.setHeader("Accept", "application/json");
			 * httpPost.setHeader("Content-type", "application/json");
			 * 
			 * // 8. Execute POST request to the given URL HttpResponse
			 * httpResponse = httpclient.execute(httpPost);
			 * 
			 * // 9. receive response as inputStream inputStream =
			 * httpResponse.getEntity().getContent();
			 * System.out.println("Register "+
			 * convertInputStreamToString(inputStream));
			 * 
			 * // 10. convert inputstream to string if(inputStream != null)
			 * result = convertInputStreamToString(inputStream); else result =
			 * "Did not work!";
			 */
		} catch (Exception e) {
			Log.d("InputStreamsss", e.getMessage());
		}

		// 11. return result
		return result;
	}

	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			return POST(urls[0]);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG)
					.show();
		}
	}

	private boolean validate() {
		if (name.getText().toString().trim().equals(""))
			return false;
		else if (uname.getText().toString().trim().equals(""))
			return false;
		else if (email.getText().toString().trim().equals(""))
			return false;
		else
			return true;
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}
}

/*
 * private class HttpAsyncTask extends AsyncTask<String, Void, String> {
 * 
 * @Override protected String doInBackground(String... urls) { InputStream
 * inputStream = null; String result = "";
 * 
 * try{ HttpClient httpclient = new DefaultHttpClient();
 * 
 * // 2. make POST request to the given URL HttpPost httpPost = new
 * HttpPost("http://anujkothari.com/hrckaraoke/androidservice/user/register");
 * 
 * String json = "";
 * 
 * // 3. build jsonObject JSONObject jsonObject = new JSONObject();
 * jsonObject.put("username", uname.getText().toString().trim());
 * 
 * 
 * jsonObject.put("email", email.getText().toString().trim());
 * 
 * 
 * jsonObject.put("name", name.getText().toString().trim());
 * jsonObject.put("password", pass.getText().toString().trim());
 * 
 * // 4. convert JSONObject to JSON to String json = jsonObject.toString();
 * 
 * // ** Alternative way to convert Person object to JSON string usin Jackson
 * Lib // ObjectMapper mapper = new ObjectMapper(); // json =
 * mapper.writeValueAsString(person);
 * 
 * // 5. set json to StringEntity StringEntity se = new StringEntity(json);
 * 
 * // 6. set httpPost Entity httpPost.setEntity(se);
 * 
 * // 7. Set some headers to inform server about the type of the content
 * httpPost.setHeader("Accept", "application/json");
 * httpPost.setHeader("Content-type", "application/json");
 * 
 * // 8. Execute POST request to the given URL HttpResponse httpResponse =
 * httpclient.execute(httpPost);
 * 
 * // 9. receive response as inputStream inputStream =
 * httpResponse.getEntity().getContent();
 * 
 * // 10. convert inputstream to string if(inputStream != null) result =
 * convertInputStreamToString(inputStream); else result = "Did not work!";
 * 
 * } catch (Exception e) { Log.d("InputStream", e.getLocalizedMessage()); }
 * 
 * // 11. return result return result; }
 * 
 * // onPostExecute displays the results of the AsyncTask. protected void
 * onPostExecute(String result) { Toast.makeText(getBaseContext(), "Data Sent!",
 * Toast.LENGTH_LONG).show(); }
 * 
 * 
 * private String convertInputStreamToString(InputStream inputStream) throws
 * IOException{ BufferedReader bufferedReader = new BufferedReader( new
 * InputStreamReader(inputStream)); String line = ""; String result = "";
 * while((line = bufferedReader.readLine()) != null) result += line;
 * 
 * inputStream.close(); return result;
 * 
 * } }}
 */

/*
 * private class Registration extends AsyncTask<String, Integer, Integer> {
 * protected Integer doInBackground(String... params) {
 * 
 * HttpClient httpclient= new DefaultHttpClient(); HttpPost httppost= new
 * HttpPost("http://anujkothari.com/hrckaraoke/androidservice/user/register");
 * try { EditText name =(EditText)findViewById(R.id.fullname); EditText uname
 * =(EditText)findViewById(R.id.username); EditText pass
 * =(EditText)findViewById(R.id.password); EditText email
 * =(EditText)findViewById(R.id.email);
 * 
 * JSONObject json = new JSONObject(); System.out.println("Spartaaaaa123");
 * 
 * json.put("username", uname.getText().toString().trim());
 * 
 * 
 * json.put("email", email.getText().toString().trim());
 * 
 * 
 * json.put("name", name.getText().toString().trim()); json.put("password",
 * pass.getText().toString().trim());
 * 
 * 
 * 
 * StringEntity se = new StringEntity(json.toString()); Log.v("HRCAPP",
 * se.toString()); //set request content type se.setContentType(new
 * BasicHeader(HTTP.CONTENT_TYPE, "application/json")); httppost.setEntity(se);
 * 
 * //send the POST request httpclient.execute(httppost);
 * 
 * 
 * 
 * } catch (JSONException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } catch (UnsupportedEncodingException e) { // TODO
 * Auto-generated catch block e.printStackTrace(); } catch
 * (ClientProtocolException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated catch
 * block e.printStackTrace(); } return 0; }
 * 
 * protected void onProgressUpdate(Integer... progress) {
 * 
 * }
 * 
 * protected void onPostExecute(Integer result) {
 * 
 * Intent loginactivity = new Intent(Register.this, MainActivity.class);
 * startActivity(loginactivity); finish();
 * 
 * } }
 */

