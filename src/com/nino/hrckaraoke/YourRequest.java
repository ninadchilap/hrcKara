package com.nino.hrckaraoke;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class YourRequest extends Fragment {
SessionManager session;
String sesid,sesname;
LinearLayout cancel;
String nid = null;
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
   final View view = inflater.inflate(R.layout.yourrequest, null);
   
   cancel = (LinearLayout) view.findViewById(R.id.cancellinear);
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
pDialog.setMessage("Fetching your request details...");
pDialog.show();   
JsonArrayRequest req = new JsonArrayRequest(Method.GET, url, 
null, new Response.Listener<JSONArray>() {
               @Override
public void onResponse(JSONArray result) {
// TODO Auto-generated method stub
               	System.out.println("HRC "+ result.toString());
               	pDialog.hide();
               	
               	TextView mysong=(TextView)view.findViewById(R.id.mysong);
       	TextView myartist=(TextView)view.findViewById(R.id.myartist);
       	
       	  
       	try {
mysong.setText(result.getJSONObject(0).getString("title").toString());
myartist.setText(result.getJSONObject(0).getString("node_field_data_field_song_nid").toString());
nid = result.getJSONObject(0).getString("nid").toString();
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
cancel.setOnTouchListener(new OnTouchListener() {
@Override
public boolean onTouch(View arg0, MotionEvent arg1) {
if(arg1.getAction() == MotionEvent.ACTION_DOWN){

String url = "http://anujkothari.com/hrckaraoke/androidservice/node/"+nid;
System.out.println("helllooooooo");
final ProgressDialog pDialog = new ProgressDialog( getActivity() );
pDialog.setMessage("Cancelling Request...");
pDialog.show();

String userid = null;
String params2 = new String("&field_status[und]=User Cancelled");

JsonObjectRequest makeReq = new JsonObjectRequest(
Method.PUT, url, params2,
new Response.Listener<JSONObject>() {

@Override
public void onResponse(JSONObject response) {
Log.d("HRCmq", response.toString());
pDialog.hide();
android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
   	Request fragment = new Request();
   	Bundle args = new Bundle();
   	
   	ft.replace(R.id.activity_main_content_fragment, fragment);
   	//ft.addToBackStack(null);
   	ft.commit();
   	
   	((MainActivity) getActivity()).prgmNameList[1]="Make Request";
	((MainActivity) getActivity()).search.setVisibility(View.VISIBLE);
	((MainActivity) getActivity()).tvTitle.setVisibility(View.GONE);




}
}, new Response.ErrorListener() {

@Override
public void onErrorResponse(VolleyError error) {
Log.d("HRCmq", "Error: "+ error.getMessage());
pDialog.hide();
}
}) {
public Map<String, String> getHeaders()
throws AuthFailureError {
HashMap<String, String> headers = new HashMap<String, String>();
headers.put("Content-Type",
"application/x-www-form-urlencoded; charset=utf-8");
headers.put("Cookie", sesname + "="
+ sesid);
headers.put("X-CSRF-Token", token);
return headers;
}

};
RequestQueue queue = Volley.newRequestQueue(getActivity());
queue.add(makeReq);
           return true;
       }
       return false;
}
});
RequestQueue queue = Volley.newRequestQueue(context);
queue.add(req);
return view;
}
}