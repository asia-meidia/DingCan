package com.healthybear.dingcan.consumer.ui.fragment;

import com.healthybear.dingcan.consumer.R;
import com.healthybear.dingcan.consumer.R.layout;
import com.healthybear.dingcan.consumer.ui.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link DFragment.OnFragmentInteractionListener} interface
 * to handle interaction events. Use the {@link DFragment#newInstance} factory
 * method to create an instance of this fragment.
 *
 */
public class DFragment extends Fragment {
	
	public DFragment() {
		// Required empty public constructor
	}

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_d, container, false);
		Button btn = (Button) v.findViewById(R.id.btn_fragmentd);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getActivity(),MainActivity.class));
				getActivity().finish();
			}
		});
		return v;
	}


}
