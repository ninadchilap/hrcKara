package com.nino.hrckaraoke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

//import com.nino.hrckaraoke.MostPopularTab.PopularAdapter;




import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class Logout extends Fragment {
	
	SessionManager session;
	String sesid,sesname;

	
@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    final View view = inflater.inflate(R.layout.yourrequest, null);

	    String url = "http://anujkothari.com/hrckaraoke/androidservice/user/logout";
		Context context = getActivity();
		
		final SessionManager session = new SessionManager(context);
		session.checkLogin();
        
        HashMap<String, String> user = session.getUserDetails();
        
        // name
        final String sesid = user.get(SessionManager.KEY_SESSIONID);
        final String sesname = user.get(SessionManager.KEY_SESSIONNAME);
        final String token = user.get(SessionManager.KEY_CSRF);
        Log.e("HRC",sesid +"   " +sesname+"   " +token);
        
        final ProgressDialog pDialog = new ProgressDialog(context);
		pDialog.setMessage("Logging out...");
		pDialog.show();   
		
		JsonArrayRequest req = new JsonArrayRequest(Method.POST, url, 
				null, new Response.Listener<JSONArray>() {
	                @Override
					public void onResponse(JSONArray result) {
						// TODO Auto-generated method stub
	                	Log.e("HRC logout info",result.toString());
	                	
	                	session.logoutUser();
	                	Intent intent = new Intent(getActivity(), Welcome.class);
	                	startActivity(intent);
	                	getActivity().finish();
	        	        
					}
	            }, new Response.ErrorListener() {
	                @Override
	                public void onErrorResponse(VolleyError error) {
	                    Log.d("HRC", "Error: " + error.toString());
	                    pDialog.hide();
	                }
	            }) {
	 			public Map<String, String> getHeaders() throws AuthFailureError {
	 				HashMap<String, String> headers = new HashMap<String, String>();
				    headers.put("Content-Type", "application/json");
				    headers.put("Cookie", sesname+"="+sesid);
				    headers.put("X-CSRF-Token", token);
				    return headers;
	 			}
			};
			 
			// Adding request to request queue
			
			RequestQueue queue = Volley.newRequestQueue(context);
			queue.add(req);
			
		return view;
	}
}

