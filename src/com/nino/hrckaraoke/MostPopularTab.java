package com.nino.hrckaraoke;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import com.nino.hrckaraoke.CustomAdapter.Holder;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Video.VideoColumns;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MostPopularTab extends Fragment {

	ArrayList<String> popularsongs;
	ListView popularlist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mostpopulartab, null);
		popularsongs = new ArrayList<String>();

		popularlist = (ListView) view.findViewById(R.id.popularlist);
		popularsongs.add("Nothing Else Matters");
		popularsongs.add("Stairway to Heaven ");
		popularsongs.add("Fade to black");
		popularsongs.add("Zombie");
		popularsongs.add("No more lies");
		popularsongs.add("Hotel california");
		popularsongs.add("Smoke on Water");
		popularsongs.add("Comfortably Numb");

		popularsongs.add("Wish You Were Here");

		popularsongs.add("Paradise");


		ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(
				getActivity(), R.layout.mostpopularrow, R.id.popularsong,
				popularsongs);
		popularlist.setAdapter(arrayAdapter1);
		
	    new FetchItems().execute();


		return view;
	}
	
	private class FetchItems extends AsyncTask<String, Void, JSONArray> {

	    protected JSONArray doInBackground(String... params) {

	      

	        HttpClient httpclient = new DefaultHttpClient();

	        HttpGet httpget = new HttpGet("http://anujkothari.com/hrckaraoke/androidservice/views/popular_requests");
	        //set header to tell REST endpoint the request and response content types
	        httpget.setHeader("Accept", "application/json");
	        httpget.setHeader("Content-type", "application/json");
	        System.out.println("ninad");

	        JSONArray json = new JSONArray();

	        try {

	            HttpResponse response = httpclient.execute(httpget);
	            System.out.println("ninad1");

	            //read the response and convert it into JSON array
	            json = new JSONArray(EntityUtils.toString(response.getEntity()));
	            
	            System.out.println("ninad12");
	            System.out.println("json "+json);           
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
	        ListView lst = (ListView) getActivity().findViewById(R.id.popularlist);

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
	    	ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(),R.layout.feedsrow,R.id.feedtxt,listItems);
	    	lst.setAdapter(arrayAdapter1);
	    }
	}
	
	
	
	
	
	// adapter for list view
	public class PopularAdapter extends BaseAdapter {
		String[] popularsong;
		Context context;
		String[] popularartist;
		 LayoutInflater inflater = null;

		public PopularAdapter(MainActivity mainActivity, String[] popularsonglist,
				String[] popularartistlist) {
			// TODO Auto-generated constructor stub
			popularsong = popularsonglist;
			context = mainActivity;
			popularartist = popularartistlist;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return popularsong.length;
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
			rowView = inflater.inflate(R.layout.menurow, null);
			holder.popsong = (TextView) rowView.findViewById(R.id.popularsong);
			holder.popart = (TextView) rowView.findViewById(R.id.popularartist);
			holder.popsong.setText(popularsong[position]);
			holder.popart.setText(popularartist[position]);
			
			return rowView;
		}

	}
}
