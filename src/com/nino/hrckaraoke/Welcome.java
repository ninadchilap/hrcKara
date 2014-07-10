package com.nino.hrckaraoke;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;

public class Welcome extends Activity {
	
	NewVideoView hrc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	}

	

}