package com.nino.hrckaraoke;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	MainLayout mLayout;
	private ListView lvMenu;
	private String[] lvMenuItems;
	ImageButton btMenu;
	TextView tvTitle;
	public String session_id;
    public String session_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLayout = (MainLayout) this.getLayoutInflater().inflate(
				R.layout.activity_main, null);
		setContentView(mLayout);
		 Bundle extras = new Bundle();
		    
		    
		    if (extras != null) {
		        session_id = extras.getString("SESSION_ID");
		        session_name = extras.getString("SESSION_NAME");
		    }

		lvMenuItems = getResources().getStringArray(R.array.menu_items);
		lvMenu = (ListView) findViewById(R.id.menu_listview);
		lvMenu.setAdapter(new ArrayAdapter<String>(this,
				R.layout.menurow,R.id.menutxt, lvMenuItems));
		lvMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onMenuItemClick(parent, view, position, id);
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
		String selectedItem = lvMenuItems[position];
		String currentItem = tvTitle.getText().toString();
		if (selectedItem.compareTo(currentItem) == 0) {
			mLayout.toggleMenu();
			return;
		}

		FragmentManager fm = MainActivity.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment fragment = null;

		if (selectedItem.compareTo("Make Request") == 0) {
			fragment = new Request();
		}  else if (selectedItem.compareTo("My Profile") == 0) {
			fragment = new Profile();
		}
		else if (selectedItem.compareTo("Previous Request") == 0) {
			fragment = new Previous();
		}
		else if (selectedItem.compareTo("HRC Karaoke Guide") == 0) {
			fragment = new Guide();
		}
		else if (selectedItem.compareTo("Explore Events") == 0) {
			fragment = new Others();
		}
	

		if (fragment != null) {
			ft.replace(R.id.activity_main_content_fragment, fragment);
			ft.commit();
			tvTitle.setText(selectedItem);
		}
		mLayout.toggleMenu();
	}

	public void setCountText(String text){
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
