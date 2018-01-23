package com.nju.android.health.views.fragments.data;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nju.android.health.R;
import com.nju.android.health.model.data.Pressure;
import com.nju.android.health.providers.DbProvider;
import com.nju.android.health.utils.BackHandledFragment;
import com.nju.android.health.utils.DividerItemDecoration;
import com.nju.android.health.utils.RecyclerViewClickListener;
import com.nju.android.health.views.adapters.DataListAdapter;
import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataListFragment extends BackHandledFragment implements RecyclerViewClickListener{

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    private RecyclerView recyclerView;
    private List<Pressure> dataList;
    private DbProvider mProvider;
    private DataListAdapter adapter;
    private BottomBar bottomBar;



    public DataListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
//        bottomBar.setDefaultTabPosition(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_list, container, false);

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


        initData();
        initView(view);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    private void initData() {
        dataList = new ArrayList<>();
        /*for (int i=0; i<3; i++) {
            Pressure p = new Pressure();
            p.setHigh(120);
            p.setLow(70);
            p.setRate(65);
            p.setTag_send("false");
            p.setUser_id(Long.parseLong(MyApplication.getInstance().getUser_id()));
            p.setTime("2017");
            dataList.add(p);
        }*/
        mProvider = new DbProvider();
        mProvider.init(getActivity());
        dataList = mProvider.getPressure("all");

    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.data_list_recycler);

        bottomBar = (BottomBar) getActivity().findViewById(R.id.bottomBar);
        initRecycler();

    }

    private void initRecycler() {


        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DataListAdapter adapter = new DataListAdapter(dataList);
        adapter.setRecyclerViewListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v, int position) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, new OldDataFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
