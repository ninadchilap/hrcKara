package com.nino.hrckaraoke;
import java.util.HashMap;
 



import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
 
public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;
     
    // Editor for Shared preferences
    Editor editor;
     
    // Context
    Context _context;
     
    // Shared pref mode
    int PRIVATE_MODE = 0;
     
    // Sharedpref file name
    private static final String PREF_NAME = "HrcKaraoke";
     
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
     
    // User name (make variable public to access from outside)
    public static final String KEY_SESSIONNAME = "sessionname";
    
    public final static String KEY_SESSIONID = "sessionId";
    
    public final static String KEY_USER = "session_user";
    
    public final static String KEY_EVENT = "session_event";
     
    // Email address (make variable public to access from outside)
    public static final String KEY_CSRF = "csrf";
     
    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
     
    /**
     * Create login session
     * @param session_event 
     * @param session_user 
     * */
    public void createLoginSession(String sessionname,String sessionId,String csrf, JSONObject session_user, String session_event){
        // Storing login value as TRUE
    	//Log.e("HRC Session name",sessionname);
    	//Log.e("HRC Session id",sessionId);
    	//Log.e("HRC Session csrf",csrf);
    	
        editor.putBoolean(IS_LOGIN, true);
        
        editor.putString(KEY_SESSIONNAME, sessionname);
         
        editor.putString(KEY_SESSIONID, sessionId);
        
        editor.putString(KEY_CSRF, csrf);
        
        editor.putString(KEY_USER, session_user.toString());

        editor.putString(KEY_EVENT, session_event);

        editor.commit();
    }  
     
    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Welcome.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             
            // Staring Login Activity
            _context.startActivity(i);
        }
         
    }
     
     
     
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_SESSIONNAME, pref.getString(KEY_SESSIONNAME, null));
        
        user.put(KEY_SESSIONID, pref.getString(KEY_SESSIONID, null));
        
        user.put(KEY_CSRF, pref.getString(KEY_CSRF, null));
        
        user.put(KEY_EVENT, pref.getString(KEY_EVENT, null));
        
        user.put(KEY_USER, pref.getString(KEY_USER, null));
         
        // return user
        return user;
    }
     
    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
         
        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         
        // Staring Login Activity
        _context.startActivity(i);
    }
     
    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}