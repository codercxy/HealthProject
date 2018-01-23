package com.nju.android.health.views.fragments.doctor;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.nju.android.health.MyApplication;
import com.nju.android.health.R;
import com.nju.android.health.model.data.DiagnoseItem;
import com.nju.android.health.utils.BackHandledFragment;
import com.nju.android.health.utils.RecyclerInsetsDecoration;
import com.nju.android.health.utils.RecyclerViewClickListener;
import com.nju.android.health.views.activities.doctor.PhysicianActivity;
import com.nju.android.health.views.activities.next.Reservation.ReservationActivity;
import com.nju.android.health.views.activities.next.SearchActivity;
import com.nju.android.health.views.activities.next.SearchResultActivity;
import com.nju.android.health.views.adapters.DiagnoseAdapter;
import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiagnoseFragment extends BackHandledFragment implements RecyclerViewClickListener{
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;

    private Integer[] pictureList;
    private String[] discList;
    private List<DiagnoseItem> diagnoseItems;

    private TextView predict;
    private TextView reservation;
    private BottomBar bottomBar;

    public DiagnoseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
//        bottomBar.setDefaultTabPosition(2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diagnose, container, false);

        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }

        init(view);

        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    private void init(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.diagnose_recycler);
        predict = (TextView) view.findViewById(R.id.diagnose_predict);
        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                startActivity(intent);
            }
        });
        reservation = (TextView) view.findViewById(R.id.diagnose_reservation);
        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReservationActivity.class);
                startActivity(intent);
            }
        });
        initData();
        initRecycler();

        bottomBar = (BottomBar) getActivity().findViewById(R.id.bottomBar);
    }

    private void initData() {
        pictureList = new Integer[]{R.drawable.room_all, R.drawable.room_surgery, R.drawable.room_internal, R.drawable.room_bone,
                R.drawable.room_mouth, R.drawable.room_ear, R.drawable.room_mental, R.drawable.room_skin};

        discList = new String[]{"全科", "外科", "内科", "骨科",
                "口腔科", "耳鼻喉科", "精神科", "皮肤科"};
        diagnoseItems = new ArrayList<>();
        for (int i = 0; i < discList.length; i++) {
            DiagnoseItem newItem = new DiagnoseItem();
            newItem.setImgRes(pictureList[i]);
            newItem.setText(discList[i]);
            diagnoseItems.add(newItem);
        }
    }
    private void initRecycler() {
        mLayoutManager = new GridLayoutManager(getActivity(), 4, OrientationHelper.VERTICAL, false);
        mRecyclerView.addItemDecoration(new RecyclerInsetsDecoration(getActivity()));
        mRecyclerView.setLayoutManager(mLayoutManager);
        DiagnoseAdapter adapter = new DiagnoseAdapter(diagnoseItems);
        adapter.setRecyclerListener(this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v, int position) {

        Intent intent = new Intent(getActivity(), PhysicianActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("roomType", discList[position]);
        startActivity(intent);

        /*YWIMKit mIMKit = YWAPI.getIMKitInstance(MyApplication.getInstance().userid,
                MyApplication.getInstance().APP_KEY);
        String target = "testpro2";
        Intent intent = mIMKit.getChattingActivityIntent(target);
        startActivity(intent);*/
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
