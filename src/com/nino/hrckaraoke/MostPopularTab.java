package com.nino.hrckaraoke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MostPopularTab extends Fragment {


	ListView popularlist;
	String uid=null;
	ArrayList<String> song_id;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mostpopulartab, null);
		
		song_id=new ArrayList<String>();
		// Tag used to cancel the request
		String url = "http://anujkothari.com/hrckaraoke/androidservice/views/popular_requests";
		final Context context = getActivity();
		
		SessionManager session = new SessionManager(context);
		session.checkLogin();
        
        HashMap<String, String> user = session.getUserDetails();
        
        // name
        final String sesid = user.get(SessionManager.KEY_SESSIONID);
        final String sesname = user.get(SessionManager.KEY_SESSIONNAME);
        final String token = user.get(SessionManager.KEY_CSRF);
        uid= user.get(SessionManager.KEY_UID);

        Log.e("HRC",sesid +"   " +sesname+"   " +token+"And uid is" +uid);
        
       
        popularlist = (ListView) view.findViewById(R.id.popularlist);

        
        
    	popularlist.setOnItemClickListener(new OnItemClickListener() {
    		public void onItemClick(AdapterView<?> parent, View view,
    		int position, long id) {
    		/*TextView mysongtxt = (TextView) view.findViewById(R.id.popularsong);
    		TextView myartisttxt = (TextView) view.findViewById(R.id.popularartist);


    		String myartist = myartisttxt.getText().toString();
    		String mysong = mysongtxt.getText().toString();*/

/***************************************************
  * Code for make request
  */
    			 final RequestQueue queue = Volley.newRequestQueue(context);
    			 Log.e("HRC","clicked") ;
	    		   String url = "http://anujkothari.com/hrckaraoke/androidservice/node";
   	             
	    		   final ProgressDialog pDialog = new ProgressDialog(context);
	    		   pDialog.setMessage("Making Request...");
	    		   pDialog.show();    
	    		   
	    		   
	    		   
	    		   String params2 = new String("&title=dasdasda&type=request&field_customer[und][0][uid]=[uid:\""+ uid.toString()+"\"]&field_song[und][0][nid]=[nid:\""+ song_id.get(position)+"\"]&field_event[und][0][nid]=[nid:3]&field_status[und]=Requested");
	    		   
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
/******************************************************************/
	    		        
    		android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
    		YourRequest fragment = new YourRequest();
    		Bundle args = new Bundle();
    		/*args.putString("sendmysong", mysong);
    		args.putString("sendmyartist", myartist);

    		fragment.setArguments(args);*/
    		ft.replace(R.id.activity_main_content_fragment, fragment);
    		//ft.addToBackStack(null);
    		ft.commit();
    		((MainActivity) getActivity()).setCountText("Your Request");

    		((MainActivity) getActivity()).prgmNameList[1]="Your Request";




    		}
    		});


        
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
        	        ListView lst = (ListView) getActivity().findViewById(R.id.popularlist);

        	        //create the ArrayList to store the titles of nodes
        	        ArrayList<String> popularsonglistItems=new ArrayList<String>();
        	        ArrayList<String> popularartlistItems=new ArrayList<String>();
        	        
        	        System.out.println("popular list" +result.toString());

        	        //iterate through JSON to read the title of nodes
        	        for(int i=0;i<result.length();i++){
        	            try {
        	            	song_id.add(result.getJSONObject(i).getString("song").toString());
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