package com.nino.hrckaraoke;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;

import com.android.volley.Request.Method;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MostPopularTab extends Fragment {


	ListView popularlist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mostpopulartab, null);
		
		// Tag used to cancel the request
		String url = "http://anujkothari.com/hrckaraoke/androidservice/views/current_user";
		Context context = getActivity();
		
		SessionManager session = new SessionManager(context);
		session.checkLogin();
        
        HashMap<String, String> user = session.getUserDetails();
        
        // name
        final String sesid = user.get(SessionManager.KEY_SESSIONID);
        final String sesname = user.get(SessionManager.KEY_SESSIONNAME);
        final String token = user.get(SessionManager.KEY_CSRF);
        Log.e("HRC",sesid +"   " +sesname+"   " +token);
        
        ProgressDialog pDialog = new ProgressDialog(context);
		pDialog.setMessage("Loading...");
		//pDialog.show();    
		         
		JsonArrayRequest req = new JsonArrayRequest(Method.GET, url, 
			new Response.Listener<JSONArray>() {
                @Override
				public void onResponse(JSONArray response) {
					// TODO Auto-generated method stub
                	Log.e("HRC",response.toString());
				}
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("HRC", "Error: " + error.toString());
                    //pDialog.hide();
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