package com.nino.hrckaraoke;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.System;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Previous extends Fragment {
	
	ArrayList<String> feedslist,livefeedslist;
	//ListView feedsview,livefeeds;
	 public String session_id;
	    public String session_name;
	    
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
	LayoutInflater lf = getActivity().getLayoutInflater();   
    View view = lf.inflate(R.layout.previous, null);
    Bundle extras = new Bundle();
    
    
    if (extras != null) {
        session_id = extras.getString("SESSION_ID");
        session_name = extras.getString("SESSION_NAME");
    }
    
    livefeedslist=new ArrayList<String>();

	//livefeeds=(ListView)view.findViewById(R.id.livefeeds);
	livefeedslist.add("Nothing Else Matters");
	livefeedslist.add("Stairway to Heaven ");
	livefeedslist.add("Fade to black");
	livefeedslist.add("Zombie");
	livefeedslist.add("No more lies");
	livefeedslist.add("Hotel california");
	livefeedslist.add("Smoke on Water");
    new FetchItems().execute();
	
	
	


    return view;
}
private class FetchItems extends AsyncTask<String, Void, JSONArray> {

    protected JSONArray doInBackground(String... params) {


        HttpClient httpclient = new DefaultHttpClient();

        HttpGet httpget = new HttpGet("http://anujkothari.com/hrckaraoke/androidservice/node");
        //set header to tell REST endpoint the request and response content types
        httpget.setHeader("Accept", "application/json");
        httpget.setHeader("Content-type", "application/json");

        JSONArray json = new JSONArray();

        try {

            HttpResponse response = httpclient.execute(httpget);
            

            //read the response and convert it into JSON array
            json = new JSONArray(EntityUtils.toString(response.getEntity()));
            java.lang.System.out.println("json "+json);           
            //return the JSON array for post processing to onPostExecute function
            return json;



        }catch (Exception e) {
            Log.v("Error adding article",e.getMessage());
        }



        return json;
    }


    //executed after the background nodes fetching process is complete
    protected void onPostExecute(JSONArray result) {

        //get the ListView UI element
        ListView lst = (ListView) getActivity().findViewById(R.id.livefeeds);

        //create the ArrayList to store the titles of nodes
        ArrayList<String> listItems=new ArrayList<String>();

        //iterate through JSON to read the title of nodes
        for(int i=0;i<result.length();i++){
            try {
            	if((result.getJSONObject(i).getString("type").toString()).equalsIgnoreCase("song"))
                listItems.add(result.getJSONObject(i).getString("title").toString());
            } catch (Exception e) {
                Log.v("Error adding article", e.getMessage());
            }
        }

        //create array adapter and give it our list of nodes, pass context, layout and list of items
    	ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listItems);
    	lst.setAdapter(arrayAdapter1);
    }
}


}
