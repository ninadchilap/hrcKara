package com.nino.hrckaraoke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nino.hrckaraoke.MostPopularTab.PopularAdapter;
import com.nino.hrckaraoke.MostPopularTab.PopularAdapter.Holder;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PreviousTab extends Fragment {
	
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.previoustab, null);
    
 // Tag used to cancel the request
 		String url = "http://anujkothari.com/hrckaraoke/androidservice/views/previous_requests";
 		final Context context = getActivity();
 		
 		SessionManager session = new SessionManager(context);
 		session.checkLogin();
         
        HashMap<String, String> user = session.getUserDetails();
        final RequestQueue queue = Volley.newRequestQueue(context);
        // name
        final String sesid = user.get(SessionManager.KEY_SESSIONID);
        final String sesname = user.get(SessionManager.KEY_SESSIONNAME);
        final String token = user.get(SessionManager.KEY_CSRF);
        Log.e("HRC",sesid +"   " +sesname+"   " +token);
         
        final ProgressDialog pDialog = new ProgressDialog(context);
 		pDialog.setMessage("Fetching most requested songs...");
 		pDialog.show();    
 		         
 		JsonArrayRequest req = new JsonArrayRequest(Method.GET, url, 
 			null, new Response.Listener<JSONArray>() {
                 @Override
 				public void onResponse(JSONArray result) {
 					// TODO Auto-generated method stub
                 	//Log.e("HRC",response.toString());
                 	pDialog.hide();
                 	//get the ListView UI element
         	        ListView lst = (ListView) getActivity().findViewById(R.id.previouslist);

         	        //create the ArrayList to store the titles of nodes
         	        ArrayList<String> previoussonglistItems=new ArrayList<String>();
         	        ArrayList<String> previousartlistItems=new ArrayList<String>();

         	        //iterate through JSON to read the title of nodes
         	        for(int i=0;i<result.length();i++){
         	            try {
         	            	
         	            	previousartlistItems.add(result.getJSONObject(i).getString("nid").toString());
         	            	previoussonglistItems.add(result.getJSONObject(i).getString("node_title").toString());
         	            	
         	            } catch (Exception e) {
         	                Log.v("Error adding article", e.getMessage());
         	            }
         	        }
         	        
         	       lst.setOnItemClickListener(new OnItemClickListener() {
         	    	   @Override
         	    	   public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
         	    		   //String yourData = temparr.get(position);
	         	    	   Log.e("HRC","clicked") ;
         	    		   String url = "http://anujkothari.com/hrckaraoke/androidservice/node";
	         	             
         	    		   final ProgressDialog pDialog = new ProgressDialog(context);
         	    		   pDialog.setMessage("Making Request...");
         	    		   pDialog.show();    
         	    		   
         	    		   
         	    		   
         	    		   String params2 = new String("&title=dasdasda&type=request&field_customer[und][0][uid]=[uid:17]&field_song[und][0][nid]=[nid:8]&field_event[und][0][nid]=[nid:3]&field_status[und]=Requested");
         	    		   
         	    		  JsonObjectRequest makeReq = new JsonObjectRequest(Method.POST, url, params2,
         	    		                 new Response.Listener<JSONObject>() {
         	    		  
         	    		                     @Override
         	    		                     public void onResponse(JSONObject response) {
         	    		                         Log.d("HRCmq", response.toString());
         	    		                         pDialog.hide();
         	    		                     }
         	    		                 }, new Response.ErrorListener() {
         	    		  
         	    		                     @Override
         	    		                     public void onErrorResponse(VolleyError error) {
         	    		                         Log.d("HRCmq", "Error: " + error.getMessage());
         	    		                         pDialog.hide();
         	    		                     }
         	    		                 }) {
	         	    			 	 public Map<String, String> getHeaders() throws AuthFailureError {
         	    		                 HashMap<String, String> headers = new HashMap<String, String>();
         	    		                 headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
         	    		 			     headers.put("Cookie", sesname+"="+sesid);
         	    		 			     headers.put("X-CSRF-Token", token);
         	    		                 return headers;
         	    		             }
         	    		  
         	    		         };
         	    		        queue.add(makeReq);
         	    	   }
         	    	});

         	        //create array adapter and give it our list of nodes, pass context, layout and list of items
         	        PreviousAdapter arrayAdapter1 = new PreviousAdapter(getActivity(),previoussonglistItems,previousartlistItems);
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
 		
 		
 		queue.add(req);
 		
 		return view;
	}

public class PreviousAdapter extends BaseAdapter {
	 ArrayList<String> previoussong=new ArrayList<String>();
     ArrayList<String> previousartist=new ArrayList<String>();
	
	Context context;
	
	 LayoutInflater inflater = null;

	public PreviousAdapter(FragmentActivity fragmentActivity, ArrayList<String> popularsonglist,
			ArrayList<String> popularartistlist) {
		// TODO Auto-generated constructor stub
		previoussong = popularsonglist;
		context = fragmentActivity;
		previousartist = popularartistlist;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return previoussong.size();
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
		holder.popsong.setText(previoussong.get(position));
		holder.popart.setText(previousartist.get(position));
		
		return rowView;
	}

}

}
