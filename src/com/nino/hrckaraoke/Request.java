package com.nino.hrckaraoke;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


public class Request extends Fragment {
	
	Button most_popular,previous_req;
	
		
	@SuppressLint("NewApi")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		 LayoutInflater lf = getActivity().getLayoutInflater();   
        View view = lf.inflate(R.layout.request, null);
        most_popular=(Button)view.findViewById(R.id.populartab);
        previous_req=(Button)view.findViewById(R.id.prerequesttab);
        
        most_popular.setBackground(getResources().getDrawable(R.drawable.btn_request_left_active));
        previous_req.setBackground(getResources().getDrawable(R.drawable.btn_request_right_inactive));
    	FragmentTransaction ft = getFragmentManager().beginTransaction();
		MostPopularTab fragment = new MostPopularTab();
		ft.replace(R.id.requestfragment, fragment);
		ft.addToBackStack(null);
		ft.commit(); 
        
       most_popular.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			 most_popular.setBackground(getResources().getDrawable(R.drawable.btn_request_left_active));
			 previous_req.setBackground(getResources().getDrawable(R.drawable.btn_request_right_inactive));
			
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			MostPopularTab fragment = new MostPopularTab();
			ft.replace(R.id.requestfragment, fragment);
			ft.addToBackStack(null);
			ft.commit(); 
			
			
		}
	});
       
       previous_req.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 previous_req.setBackground(getResources().getDrawable(R.drawable.btn_request_right_active));
			 most_popular.setBackground(getResources().getDrawable(R.drawable.btn_request_left_inactive));
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			PreviousTab fragment = new PreviousTab();
			ft.replace(R.id.requestfragment, fragment);
			ft.addToBackStack(null);
			ft.commit(); 
			
		}
	});

        
        
        return view;
        
       
    }
	



}
