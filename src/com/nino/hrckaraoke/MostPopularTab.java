package com.nino.hrckaraoke;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MostPopularTab extends Fragment {


	ListView popularlist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mostpopulartab, null);

		popularlist = (ListView) view.findViewById(R.id.popularlist);
		
	    new FetchItems().execute();
	    
		popularlist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
			TextView mysongtxt = (TextView) view.findViewById(R.id.popularsong);
			TextView myartisttxt = (TextView) view.findViewById(R.id.popularartist);


			String myartist = myartisttxt.getText().toString();
			String mysong = mysongtxt.getText().toString();
			
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			YourRequest fragment = new YourRequest();
			Bundle args = new Bundle();
			args.putString("sendmysong", mysong);
			args.putString("sendmyartist", myartist);

			fragment.setArguments(args);
			ft.replace(R.id.activity_main_content_fragment, fragment);
			//ft.addToBackStack(null);
			ft.commit(); 
			((MainActivity) getActivity()).setCountText("Your Request");

			((MainActivity) getActivity()).prgmNameList[1]="Your Request";


		

			}
			});

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
