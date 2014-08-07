package com.nino.hrckaraoke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
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
import com.nino.hrckaraoke.PreviousTab.PreviousAdapter;

public class MainActivity extends FragmentActivity {

	MainLayout mLayout;
	private ListView lvMenu;
	// private String[] lvMenuItems;
	ImageButton btMenu;
	TextView tvTitle;
	AutoCompleteTextView search;
	public String session_id;
	public String session_name;
	ArrayList<String> song_id;

	ArrayList<String> songname;

	String sesid, sesname;
	ArrayList prgmName;
	// ImageButton search;
	public static int[] prgmImages = { R.drawable.menuprofile,
			R.drawable.menurequest, R.drawable.menuprevious,
			R.drawable.menukaraokeguide, R.drawable.menuexplore,
			R.drawable.ic_launcher };
	public static String[] prgmNameList = { "My Profile", "Make Request",
			"Previous Request", "HRC Karaoke Guide", "Explore Events", "Logout" };

	SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mLayout = (MainLayout) this.getLayoutInflater().inflate(
				R.layout.activity_main, null);
		setContentView(mLayout);
		Bundle extras = new Bundle();
		session = new SessionManager(getApplicationContext());

		if (extras != null) {
			session_id = extras.getString("SESSION_ID");
			session_name = extras.getString("SESSION_NAME");
		}

		session.checkLogin();
 		song_id=new ArrayList<String>();

		HashMap<String, String> user = session.getUserDetails();

		// name
		sesid = user.get(SessionManager.KEY_SESSIONID);
		sesname = user.get(SessionManager.KEY_SESSIONNAME);

		Log.d("Session name ", sesid + "   " + sesname);

		// search=(ImageButton)findViewById(R.id.search);

		// lvMenuItems = getResources().getStringArray(R.array.menu_items);
		lvMenu = (ListView) findViewById(R.id.menu_listview);
		lvMenu.setAdapter(new CustomAdapter(this, prgmNameList, prgmImages));
		lvMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onMenuItemClick(parent, view, position, id);
			}

		});

		/*
		 * search.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * Intent intent = new Intent(MainActivity.this, Search.class);
		 * startActivity(intent);
		 * 
		 * } });
		 */

		btMenu = (ImageButton) findViewById(R.id.button_menu);
		btMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Show/hide the menu
				toggleMenu(v);
			}
		});
		
		// Tag used to cancel the request
		final Context context = this;

		
		session.checkLogin();

		

	
		final RequestQueue queue = Volley.newRequestQueue(context);
		// name
		final String sesid = user.get(SessionManager.KEY_SESSIONID);
		final String sesname = user.get(SessionManager.KEY_SESSIONNAME);
		final String token = user.get(SessionManager.KEY_CSRF);
		final String event = "59";
		final String userdata = user.get(SessionManager.KEY_USER);

		Log.e("HRCPrevTab", sesid + "   " + sesname + "   " + token
				+ "   " + event + "   " + userdata);

		tvTitle = (TextView) findViewById(R.id.activity_main_content_title);
		search = (AutoCompleteTextView) findViewById(R.id.editText1);
		
		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				String textsearch= search.getText().toString();
				 String url = "http://www.anujkothari.com/hrckaraoke/androidservice/views/search_song?term="+textsearch+"";

			

				JsonArrayRequest req = new JsonArrayRequest(Method.GET, url,
						null, new Response.Listener<JSONArray>() {
							@Override
							public void onResponse(JSONArray result) {
								// TODO Auto-generated method stub
								// Log.e("HRC",response.toString());
								
								// get the ListView UI element
								//ListView lst = (ListView)findViewById(R.id.previouslist);

								// create the ArrayList to store the titles of
								// nodes
								songname = new ArrayList<String>();
								
								System.out.println("response from search"+result);


								// iterate through JSON to read the title of
								// nodes
								for (int i = 0; i < result.length(); i++) {
									try {

										songname
												.add(result
														.getJSONObject(i)
														.getString(
																"node_title")
														.toString());
										
										
			        	            	song_id.add(result.getJSONObject(i).getString("nid").toString());
										

									} catch (Exception e) {
										Log.v("Error adding article",
												e.getMessage());
									}
								}

								search.setOnItemClickListener(new OnItemClickListener() {
									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										 String url = "http://anujkothari.com/hrckaraoke/androidservice/node";
				         	             
				         	    		   final ProgressDialog pDialog = new ProgressDialog(context);
				         	    		   pDialog.setMessage("Making Request...");
				         	    		   pDialog.show();    
				         	    		   
				         	    		   String userid = null;
											try {
												JSONObject userdatajson = new JSONObject(userdata);
												userid = userdatajson.getString("uid");
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
				         	    		   
				         	    		   String params2 = new String("&title=asassas&type=request&field_customer[und][0][uid]=[uid:"+userid+"]&field_song[und][0][nid]=[nid:"+ song_id.get(position)+"]&field_event[und][0][nid]=[nid:"+event+"]&field_status[und]=Requested");
				         	    		   
				         	    		   JsonObjectRequest makeReq = new JsonObjectRequest(Method.POST, url, params2,
					    		                 new Response.Listener<JSONObject>() {
					    		  
					    		                     @Override
					    		                     public void onResponse(JSONObject response) {
					    		                         Log.d("HRCmq", response.toString());
					    		                         pDialog.hide();
					    		                         
					    		                         FragmentManager fm = MainActivity.this.getSupportFragmentManager();
					    		                 		 FragmentTransaction ft = fm.beginTransaction();
												    		YourRequest fragment = new YourRequest();
												    		Bundle args = new Bundle();
												    		/*args.putString("sendmysong", mysong);
												    		args.putString("sendmyartist", myartist);

												    		fragment.setArguments(args);*/
												    		ft.replace(R.id.activity_main_content_fragment, fragment);
												    		//ft.addToBackStack(null);
												    		ft.commit();
												    		(MainActivity.this).setCountText("Your Request");

												    		(MainActivity.this).prgmNameList[1]="Your Request";
												    		(MainActivity.this).search.setVisibility(View.GONE);
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
				         	    	   }});

								// create array adapter and give it our list of
								// nodes, pass context, layout and list of items
								ArrayAdapter arrayAdapter1 = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,songname);  
										search.setAdapter(arrayAdapter1);
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Log.d("HRC", "Error: " + error.toString());
								
							}
						}) {
					public Map<String, String> getHeaders()
							throws AuthFailureError {
						HashMap<String, String> headers = new HashMap<String, String>();
						headers.put("Content-Type", "application/json");
						headers.put("Cookie", sesname + "=" + sesid);
						headers.put("X-CSRF-Token", token);
						return headers;
					}
				};

				// Adding request to request queue

				queue.add(req);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		FragmentManager fm = MainActivity.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Request fragment = new Request();
		ft.add(R.id.activity_main_content_fragment, fragment);
		ft.commit();

	}

	public void toggleMenu(View v) {
		mLayout.toggleMenu();
	}

	private void onMenuItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		String selectedItem = prgmNameList[position];
		String currentItem = tvTitle.getText().toString();
		if (selectedItem.compareTo(currentItem) == 0) {
			mLayout.toggleMenu();
			return;
		}

		FragmentManager fm = MainActivity.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment fragment = null;

		if (selectedItem.compareTo("Make Request") == 0) {

			search.setVisibility(View.VISIBLE);
			tvTitle.setVisibility(View.GONE);

			fragment = new Request();
		} else if (selectedItem.compareTo("Your Request") == 0) {

			search.setVisibility(View.GONE);
			tvTitle.setVisibility(View.VISIBLE);
			fragment = new YourRequest();

		}

		else if (selectedItem.compareTo("My Profile") == 0) {

			search.setVisibility(View.GONE);
			tvTitle.setVisibility(View.VISIBLE);

			fragment = new Profile();
		} else if (selectedItem.compareTo("Previous Request") == 0) {

			search.setVisibility(View.GONE);
			tvTitle.setVisibility(View.VISIBLE);

			fragment = new Previous();
		} else if (selectedItem.compareTo("HRC Karaoke Guide") == 0) {
			search.setVisibility(View.GONE);
			tvTitle.setVisibility(View.VISIBLE);

			fragment = new Guide();
		} else if (selectedItem.compareTo("Explore Events") == 0) {
			search.setVisibility(View.GONE);
			tvTitle.setVisibility(View.VISIBLE);

			fragment = new Others();
		} else if (selectedItem.compareTo("Logout") == 0) {
			fragment = new Logout();
		}
		if (fragment != null) {
			ft.replace(R.id.activity_main_content_fragment, fragment);
			ft.commit();
			tvTitle.setText(selectedItem);
		}
		mLayout.toggleMenu();
	}

	public void setCountText(String text) {
		tvTitle = (TextView) findViewById(R.id.activity_main_content_title);
		tvTitle.setText(text);
	}

	@Override
	public void onBackPressed() {
		if (mLayout.isMenuShown()) {
			mLayout.toggleMenu();
		} else {
			super.onBackPressed();
		}
	}
}
