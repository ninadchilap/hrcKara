package com.nino.hrckaraoke;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;

  
public class Welcome extends Activity {
	
	NewVideoView hrc;
	private String TAG = "MainActivity";
	
	SessionManager session_main;
    String session_name = null;
    String session_id = null;
    JSONObject session_user = null;
    String csrf = null;
    String session_event = null;
	 
	private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }
        
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		session_main = new SessionManager(Welcome.this);
	    //session_main.checkLogin();
        
        HashMap<String, String> user = session_main.getUserDetails();
        if (user.get(SessionManager.KEY_SESSIONID)=="sessionId" || user.get(SessionManager.KEY_SESSIONNAME)=="sessionname" || user.get(SessionManager.KEY_CSRF)=="csrf" || user.get(SessionManager.KEY_USER)=="session_user" || user.get(SessionManager.KEY_SESSIONID)==null || user.get(SessionManager.KEY_SESSIONNAME)==null || user.get(SessionManager.KEY_CSRF)==null || user.get(SessionManager.KEY_USER)==null)
        {
        	//continue with normal stull
        	
        }
        else
        {
        	Intent intent = new Intent(Welcome.this, MainActivity.class);
        	startActivity(intent);
            finish();
        }
        
       /* try{
        	Log.e("msg", user.get(SessionManager.KEY_SESSIONID));
        }catch(Exception e)
        {
        	Log.e("HRCwelcome", "caught exception");
        }*/
        
        final RequestQueue queue = Volley.newRequestQueue(this);
		
		PackageInfo info;
		/*try {
		    info = getPackageManager().getPackageInfo("com.nino.hrckaraoke", PackageManager.GET_SIGNATURES);
		    for (Signature signature : info.signatures) {
		        MessageDigest md;
		        md = MessageDigest.getInstance("SHA");
		        md.update(signature.toByteArray());
		        String something = new String(Base64.encode(md.digest(), 0));
		        //String something = new String(Base64.encodeBytes(md.digest()));
		        Log.e("hash key", something);
		    }
		} catch (NameNotFoundException e1) {
		    Log.e("name not found", e1.toString());
		} catch (NoSuchAlgorithmException e) {
		    Log.e("no such an algorithm", e.toString());
		} catch (Exception e) {
		    Log.e("exception", e.toString());
		}*/
		
		setContentView(R.layout.welcome);
		
		hrc=(NewVideoView)findViewById(R.id.videoView1);
		String path = "android.resource://com.nino.hrckaraoke/raw/hrckara2";
		hrc.setVideoURI(Uri.parse(path));		
		hrc.start();
		hrc.setOnPreparedListener (new OnPreparedListener() {                    
		    @Override
		    public void onPrepared(MediaPlayer mp) {
		        mp.setLooping(true);
		    }
		});
		
		// Code for facebook start
		
		LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
		authButton.setBackgroundResource(R.drawable.fbicon);
		authButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
		authButton.setOnErrorListener(new OnErrorListener() {
		   
		   @Override
		   public void onError(FacebookException error) {
		    Log.i(TAG, "Error " + error.getMessage());
		   }
		  });
		  // set permission list, Don't foeget to add email
		  authButton.setReadPermissions(Arrays.asList("public_profile","email"));
		  
		  
		  // session state call back event
		  authButton.setSessionStatusCallback(new Session.StatusCallback() {
		   
		   @SuppressWarnings("deprecation")
		@Override
		   public void call(Session session, SessionState state, Exception exception) {
		    
		    if (session.isOpened()) {
		              Log.i(TAG,"Access Token"+ session.getAccessToken());
		              Request.executeMeRequestAsync(session,
		            		  new Request.GraphUserCallback() {
		                          @Override
		                          public void onCompleted(GraphUser user,Response response) {
		                              if (user != null) { 
			                              //Log.i(TAG,"User ID "+ user.getId());
			                              //Log.i(TAG,"Email "+ user.asMap().get("email"));
			                              //lblEmail.setText(user.asMap().get("email").toString());
			                              final String fbid = user.getId();
			                              final String emailid = (String) user.asMap().get("email");
			                              final String name = user.getName();
			                              final String alldata = user.toString();
			                              
			                              //CHECK IF USER ALREADY EXISTS IN SYSTEM BY MATCHING THE FBID OBTAINED FROM FB
			                              
			                              String url = "http://anujkothari.com/hrckaraoke/androidservice/views/user_exists_check?facebook_id="+fbid;
			 	         	             
			            	    		   final ProgressDialog pDialog = new ProgressDialog(Welcome.this);
			            	    		   pDialog.setMessage("Logging into account...");
			            	    		   pDialog.show(); 
			            	    		   
			            	    		  JsonArrayRequest makeReq = new JsonArrayRequest(Method.GET, url, null,
            	    		                 new Listener<JSONArray>() {
            	    		  
            	    		                     @Override
            	    		                     public void onResponse(JSONArray response) {
            	    		                         //Log.d("HRCmq", response.toString());
            	    		                         //pDialog.hide();
            	    		                    	 if(response.length()!=0)
            	    		                    	 {
            	    		                    		 Log.d("HRC","existing user");
            	    		                    		 //USER ALREADY EXISTS IN SYSTEM. LOG IN THE USER AND SET SESSION VALUES.
            	    		                    		 
            	    		                    		 String url2 = "http://anujkothari.com/hrckaraoke/androidservice/user/login";
            				 	         	             
            				            	    		 String params2 = new String("&username=fb_"+fbid+"&password=all&");
            	    		                    		 
            	    		                    		 JsonObjectRequest makeReq = new JsonObjectRequest(Method.POST, url2, params2,
    				            	    		                 new Listener<JSONObject>() {
    				            	    		  
    				            	    		                     @Override
    				            	    		                     public void onResponse(JSONObject response) {
    				            	    		                    	 //Log.d("HRCmq", response.toString());
    				            	    		                         //pDialog.hide();
    				            	    		                         try {
    				            	    		                        	session_name = response.getString("session_name");
																			session_id=response.getString("sessid");
																			session_user= new JSONObject(response.getString("user"));
																			
																			String url4 = "http://anujkothari.com/hrckaraoke/services/session/token";
								            	            		         
										            	            		StringRequest req4 = new StringRequest(url4,
								        	            						new Listener<String>() {
								        	            		                    @Override
								        	            							public void onResponse(String response) {
								        	            								// TODO Auto-generated method stub
								        	            		                    	csrf = response.toString();
								        	            		                    	pDialog.hide();
								        	            		                    	session_main.createLoginSession( session_name,session_id,csrf, session_user, session_event);
								        	            		        	            //start the ListActivity
								        	            		                    	Intent intent = new Intent(Welcome.this, MainActivity.class);
								        	            		                    	startActivity(intent);
								        	            		        	            finish();
								        	            							}
								        	            		                }, new ErrorListener() {
								        	            		                    @Override
								        	            		                    public void onErrorResponse(VolleyError error) {
								        	            		                        Log.d("HRC", "Error: " + error.getMessage());
								        	            		                        //pDialog.hide();
								        	            		                    }
								        	            		                }) {
										            	            			 
										            	                        /**
										            	                         * Passing some request headers
										            	                         * */
										            	                        public Map<String, String> getHeaders() throws AuthFailureError {
										            	                            HashMap<String, String> headers = new HashMap<String, String>();
										            	                            headers.put("Content-Type", "application/json");
										            	                            headers.put("Cookie", session_name+"="+session_id);
										            	                            return headers;
										            	                        }
										            	            		};
										            	            		
										            	            		// Adding request to request queue
										            	            		queue.add(req4);
																			
																			
																		} catch (JSONException e) {
																			// TODO Auto-generated catch block
																			Log.e("HRC","json exception");
																			e.printStackTrace();
																		}
    				            	    		                     }
    				            	    		                 }, new ErrorListener() {
    				            	    		  
    				            	    		                     @Override
    				            	    		                     public void onErrorResponse(VolleyError error) {
    				            	    		                         Log.d("HRCmq", "Error: " + error.getMessage());
    				            	    		                         //pDialog.hide();
    				            	    		                     }
    				            	    		                 }) {
            				   	         	    			 	 public Map<String, String> getHeaders() throws AuthFailureError {
            				            	    		                 HashMap<String, String> headers = new HashMap<String, String>();
            				            	    		                 headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            				            	    		 			     return headers;
            				            	    		             }
            				            	    		  
            				            	    		  };
            				            	    		  queue.add(makeReq);
            	    		                    		 
            	    		                    	 }
            	    		                    	 else
            	    		                    	 {
            	    		                    		 Log.d("HRC","new user");
            	    		                    		 //USER DOEST NOT EXIST IN SYSTEM. REGISTER THE USER AND SET THE SESSION PARAMENTERS.
            	    		                    		 
            	    		                    		 String url3 = "http://anujkothari.com/hrckaraoke/androidservice/user";
            				 	         	             
            	    		                    		 String password = new String(Long.toHexString(Double.doubleToLongBits(Math.random())));
            	    		                    		 String params3 = new String("&name=fb_"+fbid+"&pass="+password+"&field_name[und][0][value]="+name+"&mail="+emailid+"&field_facebook_id[und][0][value]="+fbid+"&field_facebook_data[und][0][value]="+alldata+"&roles[4]=4");
            	    		                    		 
            	    		                    		 JsonObjectRequest makeReq = new JsonObjectRequest(Method.POST, url3, params3,
				            	    		                 new Listener<JSONObject>() {
				            	    		  
				            	    		                     @Override
				            	    		                     public void onResponse(JSONObject response) {
				            	    		                         //pDialog.hide();
				            	    		                         
				            	    		                       //USER HAS BEEN REGISTERED. LOG IN THE USER AND SET SESSION VALUES.
			            	    		                    		 
			            	    		                    		 String url2 = "http://anujkothari.com/hrckaraoke/androidservice/user/login";
			            				 	         	             
			            				            	    		 String params2 = new String("&username=fb_"+fbid+"&password=all&");
			            	    		                    		 
			            	    		                    		 JsonObjectRequest makeReq = new JsonObjectRequest(Method.POST, url2, params2,
	            				            	    		                 new Listener<JSONObject>() {
	            				            	    		  
	            				            	    		                     @Override
	            				            	    		                     public void onResponse(JSONObject response) {
	            				            	    		                         //Log.d("HRCmq", response.toString());
	            				            	    		                         //pDialog.hide();
	            				            	    		                         try {
	            				            	    		                        	session_name = response.getString("session_name");
	            				            	    		                        	session_id=response.getString("sessid");
	            				            	    		                        	session_user= new JSONObject(response.getString("user"));
	         																			
	         																			String url4 = "http://anujkothari.com/hrckaraoke/services/session/token";
	       								            	            		         
	        										            	            		StringRequest req4 = new StringRequest(url4,
	        								        	            						new Listener<String>() {
	        								        	            		                    @Override
	        								        	            							public void onResponse(String response) {
	        								        	            								// TODO Auto-generated method stub
	        								        	            		                    	csrf = response.toString();
	        								        	            		                    	pDialog.hide();
	        								        	            		                    	session_main.createLoginSession( session_name, session_id, csrf, session_user, session_event);
	        								        	            		        	            //start the ListActivity
	        								        	            		                    	Intent intent = new Intent(Welcome.this, MainActivity.class);
	        								        	            		                    	startActivity(intent);
	        								        	            		        	            finish();
	        								        	            							}
	        								        	            		                }, new ErrorListener() {
	        								        	            		                    @Override
	        								        	            		                    public void onErrorResponse(VolleyError error) {
	        								        	            		                        Log.d("HRC", "Error: " + error.getMessage());
	        								        	            		                        //pDialog.hide();
	        								        	            		                    }
	        								        	            		                }) {
	        										            	            			 
	        										            	                        /**
	        										            	                         * Passing some request headers
	        										            	                         * */
	        										            	                        public Map<String, String> getHeaders() throws AuthFailureError {
	        										            	                            HashMap<String, String> headers = new HashMap<String, String>();
	        										            	                            headers.put("Content-Type", "application/json");
	        										            	                            headers.put("Cookie", session_name+"="+session_id);
	        										            	                            return headers;
	        										            	                        }
	        										            	            		};
	        										            	            		
	        										            	            		// Adding request to request queue
	        										            	            		queue.add(req4);
	         																			
	         																			
																					} catch (JSONException e) {
																						// TODO Auto-generated catch block
																						e.printStackTrace();
																					}
	            				            	    		                     }
	            				            	    		                 }, new ErrorListener() {
	            				            	    		  
	            				            	    		                     @Override
	            				            	    		                     public void onErrorResponse(VolleyError error) {
	            				            	    		                         Log.d("HRCmq", "Error: " + error.getMessage());
	            				            	    		                         //pDialog.hide();
	            				            	    		                     }
	            				            	    		                 }) {
			            				   	         	    			 	 public Map<String, String> getHeaders() throws AuthFailureError {
			            				            	    		                 HashMap<String, String> headers = new HashMap<String, String>();
			            				            	    		                 headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			            				            	    		 			     return headers;
			            				            	    		             }
			            				            	    		  
			            				            	    		  };
			            				            	    		  queue.add(makeReq);
				            	    		                    	 
				            	    		                     }
				            	    		                 }, new ErrorListener() {
				            	    		  
				            	    		                     @Override
				            	    		                     public void onErrorResponse(VolleyError error) {
				            	    		                         Log.d("HRCmq", "Error: " + error.getMessage());
				            	    		                         //pDialog.hide();
				            	    		                     }
				            	    		                 }) {
        				   	         	    			 	 public Map<String, String> getHeaders() throws AuthFailureError {
    				            	    		                 HashMap<String, String> headers = new HashMap<String, String>();
    				            	    		                 headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
    				            	    		 			     return headers;
    				            	    		             }
            				            	    		  
            				            	    		  };
            				            	    		  queue.add(makeReq);
            	    		                    	 }
            	    		                     }
            	    		                 }, new ErrorListener() {
            	    		  
            	    		                     @Override
            	    		                     public void onErrorResponse(VolleyError error) {
            	    		                         Log.d("HRCmq", "Error: " + error.getMessage());
            	    		                         //pDialog.hide();
            	    		                     }
            	    		                 }) {
		   	         	    			 	 public Map<String, String> getHeaders() throws AuthFailureError {
		            	    		                 HashMap<String, String> headers = new HashMap<String, String>();
		            	    		                 headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		            	    		 			     return headers;
		            	    		             }
		            	    		  
		            	    		        };
		            	    		        queue.add(makeReq);
		                              }
		                          }
		                      });
		          }		    
		   }
		  });   
	}
	
	@Override
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
	     super.onActivityResult(requestCode, resultCode, data);
	     Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	 }
	
	//code for facebook end

}