package com.nino.hrckaraoke;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

public class YourRequest extends Fragment {
	
	SessionManager session;
	String sesid,sesname;

	
@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    final View view = inflater.inflate(R.layout.yourrequest, null);

	    String url = "http://anujkothari.com/hrckaraoke/androidservice/views/current_request";
		Context context = getActivity();
		
		SessionManager session = new SessionManager(context);
		session.checkLogin();
        
        HashMap<String, String> user = session.getUserDetails();
        
        // name
        final String sesid = user.get(SessionManager.KEY_SESSIONID);
        final String sesname = user.get(SessionManager.KEY_SESSIONNAME);
        final String token = user.get(SessionManager.KEY_CSRF);
        Log.e("HRC",sesid +"   " +sesname+"   " +token);
        
        final ProgressDialog pDialog = new ProgressDialog(context);
		pDialog.setMessage("Fetchingyour request details...");
		pDialog.show();   
		
		JsonArrayRequest req = new JsonArrayRequest(Method.GET, url, 
				null, new Response.Listener<JSONArray>() {
	                @Override
					public void onResponse(JSONArray result) {
						// TODO Auto-generated method stub
	                	System.out.println("HRC "+ result.toString());
	                	pDialog.hide();
	                	
	                	
	                			
	                	String recmysong = "123";
	        			String recmyartist = "123";
	        			   
	        			TextView mysong=(TextView)view.findViewById(R.id.mysong);
	        			TextView myartist=(TextView)view.findViewById(R.id.myartist);
	        			   
	        			try {
							mysong.setText(result.getJSONObject(0).getString("nid").toString());
							myartist.setText(result.getJSONObject(0).getString("node_field_data_field_song_nid").toString());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	        		
	        	        
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

