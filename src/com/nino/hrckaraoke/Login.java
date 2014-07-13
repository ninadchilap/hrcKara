package com.nino.hrckaraoke;



import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity {

	TextView forgotpass,notregistered;
	Button btn_login;
    SessionManager session;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		forgotpass=(TextView)findViewById(R.id.forgotpassword);
		notregistered=(TextView)findViewById(R.id.notregister);
		btn_login=(Button)findViewById(R.id.login_button);
		
        session = new SessionManager(getApplicationContext());                
		
		btn_login.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			public void onClick(View v) {
				
				new LoginProcess().execute();
				
			}
		});
		
		notregistered.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			public void onClick(View v) {
				
				  Intent intent = new Intent(Login.this, Register.class);
		            
		          
		            startActivity(intent);
		            finish();
				
				
			}
		});
	 }
	
	public String session_name;
    public String session_id;


    //background task to login into Drupal
    private class LoginProcess extends AsyncTask<String, Integer, JSONObject> {
    	
    	JSONObject json;
    	JSONObject jsonObject=null;

        protected JSONObject doInBackground(String... params) {
        	

            HttpClient httpclient = new DefaultHttpClient();

            //set the remote endpoint URL
            HttpPost httppost = new HttpPost("http://anujkothari.com/hrckaraoke/androidservice/user/login");


            try {

                //get the UI elements for username and password
                EditText username= (EditText) findViewById(R.id.username);
                EditText password= (EditText) findViewById(R.id.password);

                 json = new JSONObject();
                //extract the username and password from UI elements and create a JSON object
                json.put("username", username.getText().toString().trim());
                json.put("password", password.getText().toString().trim());
                
                

                //add serialised JSON object into POST request
                StringEntity se = new StringEntity(json.toString());
                Log.v("HRCAPP", se.toString());
                //set request content type
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                
                httppost.setEntity(se);

                //send the POST request
                HttpResponse response = httpclient.execute(httppost);

                //read the response from Services endpoint
                String jsonResponse = EntityUtils.toString(response.getEntity());

                jsonObject = new JSONObject(jsonResponse);
                //read the session information
                Log.v("HRCAPP", jsonObject.toString());
                System.out.println("Hrcc "+jsonObject.toString());
                session_name=jsonObject.getString("session_name");
                session_id=jsonObject.getString("sessid");
                
                session.createLoginSession( session_name,session_id);
             
            

                return jsonObject;

            }catch (Exception e) {
                Log.v("HRCAPP", e.getMessage());
            }

            return jsonObject;
        }


        protected void onPostExecute(JSONObject result) {
        	
        	
        	if(result!=null)
        	{
            //create an intent to start the ListActivity
            Intent intent = new Intent(Login.this, MainActivity.class);
            //pass the session_id and session_name to ListActivity
            intent.putExtra("SESSION_ID", session_id);
            intent.putExtra("SESSION_NAME", session_name);
           
            //start the ListActivity
            startActivity(intent);
            finish();
            }
        	
        	else
        	{
        		AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        	      //  builder.setIcon(R.drawable.ic_launcher);
        			
        	        builder.setTitle("Authetication failed");
        	        builder.setMessage(	"Please Enter correct User Name and Password"
        	        		)
        	       
        	        .setCancelable(false)
        	        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	        public void onClick(DialogInterface dialog, int id) {
        	        	 Intent intent = new Intent(Login.this, Login.class);
                         //pass the session_id and session_name to ListActivity
                        
                         //start the ListActivity
                         startActivity(intent);
                         finish();
        	         }
        	        });
        	        AlertDialog alert = builder.create();
        	        alert.show();
        	       

        	}
        }
    }

   


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

}
