package com.nino.hrckaraoke;
import java.util.HashMap;
 
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
 
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
     
    // Email address (make variable public to access from outside)
    public static final String KEY_CSRF = "csrf";
    
    public static final String KEY_UID = "uid";

     
    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
     
    /**
     * Create login session
     * */
    public void createLoginSession(String sessionname,String sessionId,String csrf,String uid ){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        
        editor.putString(KEY_SESSIONNAME, sessionname);
         
        editor.putString(KEY_SESSIONID, sessionId);
        
        editor.putString(KEY_CSRF, csrf);
        
        editor.putString(KEY_UID, uid);

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
            Intent i = new Intent(_context, Login.class);
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
         
        // user email id
        // user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        
        user.put(KEY_SESSIONID, pref.getString(KEY_SESSIONID, null));
        
        user.put(KEY_CSRF, pref.getString(KEY_CSRF, null));
        
        user.put(KEY_UID, pref.getString(KEY_UID, null));
        
        
         
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