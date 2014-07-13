package com.nino.hrckaraoke;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class RequestTask extends AsyncTask<String, String, String>{

	private String requestType = null;
	private Boolean authRequired = null;
	private String csrftoken = null;
	
	RequestTask(String requestType, Boolean authRequired)
    {
		this.requestType = requestType;
		this.authRequired = authRequired;
    }
	
	@Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        String responseString = null;
        
        try {
            if(requestType == "GET")
            {
            	HttpGet get = new HttpGet(uri[0]);
            	if(authRequired == true)
            	{
            		//Log.e("HRCauth","true");
            		//get.setHeader("Cookie", "SESS88de8e8d72248d1ea482c0924564d6a3=L2FnHBxfpDn8p4MGECyC052o02-PlctORozhRTitVn4");
            	}
            	//get.setHeader("token", "N5Nm6Dl64fp-jE1e4GEIdMd1Jf6y1dM-96Ek-H1KeRc");
            	//get.setHeader("X-CSRF-Token", csrftoken);
            	response = httpclient.execute(get);
            }
            if(requestType == "POST")
            {
            	response = httpclient.execute(new HttpPost(uri[0]));
            }
            if(requestType == "PUT")
            {
            	response = httpclient.execute(new HttpPut(uri[0]));
            }
        	//response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            Log.e("statusline", statusLine.getStatusCode()+"");
            if(statusLine.getStatusCode() != HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
        	 System.out.println("exception 1");
        } catch (IOException e) {
        	 System.out.println("exception 2 at popular");
        }
        
        //Log.e("HRCRT", responseString);
        
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Do anything with response..
    }
}