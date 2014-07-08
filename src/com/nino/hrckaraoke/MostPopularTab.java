package com.nino.hrckaraoke;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MostPopularTab extends Fragment {

	ArrayList<String> popularsongs;
	ListView popularlist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mostpopulartab, null);
		popularsongs = new ArrayList<String>();

		popularlist = (ListView) view.findViewById(R.id.popularlist);
		popularsongs.add("Nothing Else Matters");
		popularsongs.add("Stairway to Heaven ");
		popularsongs.add("Fade to black");
		popularsongs.add("Zombie");
		popularsongs.add("No more lies");
		popularsongs.add("Hotel california");
		popularsongs.add("Smoke on Water");
		popularsongs.add("Comfortably Numb");

		popularsongs.add("Wish You Were Here");

		popularsongs.add("Paradise");


		ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(
				getActivity(), R.layout.mostpopularrow, R.id.popularrow,
				popularsongs);
		popularlist.setAdapter(arrayAdapter1);

		return view;
	}

}
