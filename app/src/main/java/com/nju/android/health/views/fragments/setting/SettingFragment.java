package com.nju.android.health.views.fragments.setting;


import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.utils.BackHandledFragment;
import com.nju.android.health.utils.CircleImg;
import com.nju.android.health.utils.PictureUtil;
import com.nju.android.health.views.activities.MainActivity;
import com.nju.android.health.views.activities.me.HealthRecordActivity;
import com.nju.android.health.views.activities.me.MeInfoActivity;
import com.nju.android.health.views.activities.next.Chart_1_Activity;
import com.nju.android.health.views.activities.next.Chart_2_Activity;
import com.nju.android.health.views.activities.next.Chart_3_Activity;
import com.nju.android.health.views.activities.next.Chart_4_Activity;
import com.nju.android.health.views.activities.next.Chart_5_Activity;
import com.nju.android.health.views.activities.next.SearchActivity;
import com.roughike.bottombar.BottomBar;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends BackHandledFragment implements View.OnClickListener{
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    public final static int REQUEST_IMAGE = 0;
    public final static int REQUEST_IMAGE_OLD = 1;


    private Toolbar toolbar;
//    private ActionBar actionBar;



    private LinearLayout setting_info;
    private TextView info;
    private TextView message;
    private TextView username;
//    private TextView setting;
    private TextView feedback;
    private TextView search;

    private TextView chart_1;
    private TextView chart_2;
    private TextView chart_3;
    private TextView chart_4;
    private TextView chart_5;

    private BottomBar bottomBar;



    public SettingFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
//        bottomBar.setDefaultTabPosition(3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

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

        initView(view);
        initData();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    private void initView(View view) {

        toolbar = (Toolbar) view.findViewById(R.id.toolbar_me);


        info = (TextView) view.findViewById(R.id.me_info);
        message = (TextView) view.findViewById(R.id.me_message);
//        setting = (TextView) view.findViewById(R.id.me_setting);
        feedback = (TextView) view.findViewById(R.id.me_feedback);
        search = (TextView) view.findViewById(R.id.me_search);

        chart_1 = (TextView) view.findViewById(R.id.me_chart_1);
        chart_2 = (TextView) view.findViewById(R.id.me_chart_2);
        chart_3 = (TextView) view.findViewById(R.id.me_chart_3);
        chart_4 = (TextView) view.findViewById(R.id.me_chart_4);
        chart_5 = (TextView) view.findViewById(R.id.me_chart_5);

        setting_info = (LinearLayout) view.findViewById(R.id.ll_setting_info);
        username = (TextView) view.findViewById(R.id.me_username);


        info.setOnClickListener(this);
        message.setOnClickListener(this);
//        setting.setOnClickListener(this);
        feedback.setOnClickListener(this);
        setting_info.setOnClickListener(this);
        search.setOnClickListener(this);
        chart_1.setOnClickListener(this);
        chart_2.setOnClickListener(this);
        chart_3.setOnClickListener(this);
        chart_4.setOnClickListener(this);
        chart_5.setOnClickListener(this);




    }


    private void initData() {
        //init Toolbar
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_more_vert_white_24dp);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        /*SharedPreferences sharedPref = MeInfoActivity.instance.getPreferences(Context.MODE_PRIVATE);
        username.setText(sharedPref.getString(getString(R.string.username), "username"));*/

//        //init ActionBar
//
        setHasOptionsMenu(true);
//        actionBar = getActivity().getActionBar();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_me, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.me_action_setting:
                //设置界面
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.me_info:
                /*intent = new Intent(getActivity(), MeInfoActivity.class);
                startActivity(intent);*/
                intent = new Intent(getActivity(), HealthRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_setting_info:
                intent = new Intent(getActivity(), MeInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.me_search:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;

            case R.id.me_chart_1:
                intent = new Intent(getActivity(), Chart_1_Activity.class);
                startActivity(intent);
                break;
            case R.id.me_chart_2:
                intent = new Intent(getActivity(), Chart_2_Activity.class);
                startActivity(intent);
                break;
            case R.id.me_chart_3:
                intent = new Intent(getActivity(), Chart_3_Activity.class);
                startActivity(intent);
                break;
            case R.id.me_chart_4:
                intent = new Intent(getActivity(), Chart_4_Activity.class);
                startActivity(intent);
                break;
            case R.id.me_chart_5:
                intent = new Intent(getActivity(), Chart_5_Activity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
