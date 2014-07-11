package com.nino.hrckaraoke;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class YourRequest extends Fragment {
	
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.yourrequest, null);
    
    Bundle bundle = this.getArguments();
    if(bundle != null){
       String recmysong = bundle.getString("sendmysong");
       String recmyartist = bundle.getString("sendmyartist");
       
       TextView mysong=(TextView)view.findViewById(R.id.mysong);
       TextView myartist=(TextView)view.findViewById(R.id.myartist);
       
       mysong.setText(recmysong);
       myartist.setText(recmyartist);
    }
    return view;
}

}
