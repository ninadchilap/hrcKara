package com.nino.hrckaraoke;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	MainLayout mLayout;
	private ListView lvMenu;
	//private String[] lvMenuItems;
	ImageButton btMenu;
	TextView tvTitle;
	public String session_id;
	public String session_name;
	
	 String sesid,sesname;
	ArrayList prgmName;
	ImageButton search;
	public static int[] prgmImages = { R.drawable.menuprofile, R.drawable.menurequest,
			R.drawable.menuprevious, R.drawable.menukaraokeguide, R.drawable.menuexplore,R.drawable.ic_launcher
			};
	public static String[] prgmNameList = { "My Profile", "Make Request", "Previous Request",
			"HRC Karaoke Guide", "Explore Events","Logout" };
	
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
        
        HashMap<String, String> user = session.getUserDetails();
        
        // name
         sesid = user.get(SessionManager.KEY_SESSIONID);
        sesname = user.get(SessionManager.KEY_SESSIONNAME);
        
        Log.d("Session name ", sesid +"   " +sesname);
        
        
		
		search=(ImageButton)findViewById(R.id.search);
		

		//lvMenuItems = getResources().getStringArray(R.array.menu_items);
		lvMenu = (ListView) findViewById(R.id.menu_listview);
		lvMenu.setAdapter(new CustomAdapter(this, prgmNameList,prgmImages));
		lvMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onMenuItemClick(parent, view, position, id);
			}

		});
		
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				 Intent intent = new Intent(MainActivity.this, Search.class);
		            startActivity(intent);
				
			}
		});

		btMenu = (ImageButton) findViewById(R.id.button_menu);
		btMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Show/hide the menu
				toggleMenu(v);
			}
		});

		tvTitle = (TextView) findViewById(R.id.activity_main_content_title);

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

			fragment = new Request();
		}
		else if (selectedItem.compareTo("Your Request") == 0) {
			
			search.setVisibility(View.GONE);

			fragment = new YourRequest();
		} 
		
		else if (selectedItem.compareTo("My Profile") == 0) {
			
			search.setVisibility(View.GONE);
			fragment = new Profile();
		} else if (selectedItem.compareTo("Previous Request") == 0) {
			
			search.setVisibility(View.GONE);
			fragment = new Previous();
		} else if (selectedItem.compareTo("HRC Karaoke Guide") == 0) {
			search.setVisibility(View.GONE);
			fragment = new Guide();
		} else if (selectedItem.compareTo("Explore Events") == 0) {
			search.setVisibility(View.GONE);
			fragment = new Others();
		}
		else if (selectedItem.compareTo("Logout") == 0) {
            session.logoutUser();		}

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
