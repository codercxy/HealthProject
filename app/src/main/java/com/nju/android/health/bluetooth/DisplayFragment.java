package com.nju.android.health.bluetooth;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nju.android.health.R;


public class DisplayFragment extends Fragment {
	public static DisplayFragment newInstance() {
		return new DisplayFragment();
	}

	private TextView mHighpressure = null;
	private TextView mLowpressure = null;
	private TextView mPulse = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_display, container, false);
		if (v != null) {
			mHighpressure = (TextView) v.findViewById(R.id.highpress);
			mLowpressure = (TextView) v.findViewById(R.id.lowpress);
			mPulse = (TextView) v.findViewById(R.id.pulse);
		}
		return v;
	}

	public void setData(int highpress, int lowpress, int pulse) {
		if (mHighpressure != null) {
			mHighpressure.setText(highpress+"");
		}
		if (mLowpressure != null) {
			mLowpressure.setText(lowpress+"");
		}
		if (mPulse != null) {
			mPulse.setText(pulse+"");
		}
	}
}
