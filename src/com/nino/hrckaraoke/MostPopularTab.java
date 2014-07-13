package com.nino.hrckaraoke;

import java.util.ArrayList;
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
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MostPopularTab extends Fragment {


	ListView popularlist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mostpopulartab, null);
		
		// Tag used to cancel the request
		String url = "http://anujkothari.com/hrckaraoke/androidservice/views/popular_requests";
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
		pDialog.setMessage("Fetching most requested songs...");
		pDialog.show();    
		         
		JsonArrayRequest req = new JsonArrayRequest(Method.GET, url, 
			new Response.Listener<JSONArray>() {
                @Override
				public void onResponse(JSONArray result) {
					// TODO Auto-generated method stub
                	//Log.e("HRC",response.toString());
                	pDialog.hide();
                	//get the ListView UI element
        	        ListView lst = (ListView) getActivity().findViewById(R.id.popularlist);

        	        //create the ArrayList to store the titles of nodes
        	        ArrayList<String> popularsonglistItems=new ArrayList<String>();
        	        ArrayList<String> popularartlistItems=new ArrayList<String>();

        	        //iterate through JSON to read the title of nodes
        	        for(int i=0;i<result.length();i++){
        	            try {
        	            	
        	                popularartlistItems.add(result.getJSONObject(i).getString("nid").toString());
        	            	popularsonglistItems.add(result.getJSONObject(i).getString("node_title").toString());
        	            } catch (Exception e) {
        	                Log.v("Error adding article", e.getMessage());
        	            }
        	        }

        	        //create array adapter and give it our list of nodes, pass context, layout and list of items
        	    	PopularAdapter arrayAdapter1 = new PopularAdapter(getActivity(),popularsonglistItems,popularartlistItems);
        	    	lst.setAdapter(arrayAdapter1);
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
	
	// adapter for list view
	public class PopularAdapter extends BaseAdapter {
		 ArrayList<String> popularsong=new ArrayList<String>();
	        ArrayList<String> popularartist=new ArrayList<String>();
		
		Context context;
		
		 LayoutInflater inflater = null;

		public PopularAdapter(FragmentActivity fragmentActivity, ArrayList<String> popularsonglist,
				ArrayList<String> popularartistlist) {
			// TODO Auto-generated constructor stub
			popularsong = popularsonglist;
			context = fragmentActivity;
			popularartist = popularartistlist;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return popularsong.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public class Holder {
			TextView popart;
			TextView popsong;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Holder holder = new Holder();
			View rowView;
			rowView = inflater.inflate(R.layout.mostpopularrow,parent, false);
			holder.popsong = (TextView) rowView.findViewById(R.id.popularsong);
			holder.popart = (TextView) rowView.findViewById(R.id.popularartist);
			holder.popsong.setText(popularsong.get(position));
			holder.popart.setText(popularartist.get(position));
			
			return rowView;
		}

	}
		
}