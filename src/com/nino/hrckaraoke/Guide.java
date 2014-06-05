package com.nino.hrckaraoke;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Guide extends Fragment {
	
	Button others;
	MainActivity main;
	
	
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
	
	LayoutInflater lf = getActivity().getLayoutInflater(); 
    View view = lf.inflate(R.layout.guide, null);
    main=new MainActivity();
    
    others=(Button)view.findViewById(R.id.button1);
    others.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			
			
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			Others fragment = new Others();
			ft.replace(R.id.activity_main_content_fragment, fragment);
			ft.addToBackStack(null);
			ft.commit(); 
			((MainActivity) getActivity()).setCountText("Other Events");

			
		}
	});
    return view;
}

}
