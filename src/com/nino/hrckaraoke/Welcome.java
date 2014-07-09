package com.nino.hrckaraoke;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.VideoView;

public class Welcome extends Activity {
	
	VideoView hrc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		hrc=(VideoView)findViewById(R.id.videoView1);
		String path = "android.resource://com.nino.hrckaraoke/raw/famous";
		hrc.setVideoURI(Uri.parse(path));		
		hrc.start();
		
	}

	

}
