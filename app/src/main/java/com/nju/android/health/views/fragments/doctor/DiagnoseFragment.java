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
        initData();
        initRecycler();

        bottomBar = (BottomBar) getActivity().findViewById(R.id.bottomBar);
    }

    private void initData() {
        pictureList = new Integer[]{R.drawable.pills, R.drawable.heartbeat, R.drawable.drops, R.drawable.microscope,
                R.drawable.xray, R.drawable.eye, R.drawable.thermometer, R.drawable.pills2,
                R.drawable.stethoscope, R.drawable.snake, R.drawable.pulsometer, R.drawable.cardio_machine,
                R.drawable.enema, R.drawable.marker, R.drawable.test_tube, R.drawable.test_tube};

        discList = new String[]{"儿科", "内科", "男科", "产科",
                "外科", "眼科", "中医科", "骨伤科",
                "精神心理科", "肿瘤科", "妇科", "耳鼻喉科",
                "口腔科", "美容", "肝病", "皮肤科"};
        diagnoseItems = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
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
