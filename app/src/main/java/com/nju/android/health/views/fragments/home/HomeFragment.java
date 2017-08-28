package com.nju.android.health.views.fragments.home;


import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Fragment;
import android.os.SystemClock;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.nju.android.health.R;
import com.nju.android.health.bluetooth.BleActivity;
import com.nju.android.health.model.data.Glucose;
import com.nju.android.health.model.data.Pressure;
import com.nju.android.health.model.data.Step;
import com.nju.android.health.providers.DbProvider;
import com.nju.android.health.utils.AlarmReceiver;
import com.nju.android.health.utils.BackHandledFragment;
import com.nju.android.health.views.activities.HomeManualActivity;
import com.nju.android.health.views.activities.home.ClockActivity;
import com.nju.android.health.views.activities.next.AddXyrecorder;
import com.nju.android.health.views.activities.next.SearchActivity;
import com.nju.android.health.views.fragments.health.HealthFragment;
import com.roughike.bottombar.BottomBar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BackHandledFragment implements View.OnClickListener{

    /*private ImageView manual;
    private ImageView auto;*/

    private TextView clock;
    private TextView clockView;
    private TextView clock_no_data;
    private LinearLayout ll_clock;
//    private CardView cardView;

    private String clock_time;
    private ToggleButton clockToggle;
    private int hour, minute;
    private Boolean isToggleOn = true;

    //健康资讯
    private LinearLayout ll_news;
    private ImageView news_img;
    private TextView news_title, news_type;

    private BottomBar bottomBar;

    private EditText et_search;

    public HomeFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        initView(view);
        initData();
        return view;
    }


    private void initView(View view) {

        /*manual = (ImageView) view.findViewById(R.id.home_manual_img);
        auto = (ImageView) view.findViewById(R.id.home_auto_img);*/



//        cardView = (CardView) view.findViewById(R.id.home_cardview);

        clock = (TextView) view.findViewById(R.id.home_clock);
        clockView = (TextView) view.findViewById(R.id.home_clock_view);
        clock_no_data = (TextView) view.findViewById(R.id.home_clock_no_data);
        ll_clock = (LinearLayout) view.findViewById(R.id.ll_home_clock);
        clockToggle = (ToggleButton) view.findViewById(R.id.home_clock_toggle);

        //news
        ll_news = (LinearLayout) view.findViewById(R.id.ll_home_news);
        news_img = (ImageView) view.findViewById(R.id.home_news_img);
        news_title = (TextView) view.findViewById(R.id.home_news_title);
        news_type = (TextView) view.findViewById(R.id.home_news_type);

        et_search = (EditText) view.findViewById(R.id.home_et_search);




        /*manual.setOnClickListener(this);
        auto.setOnClickListener(this);*/



        clock.setOnClickListener(this);
        clockToggle.setOnClickListener(this);

        ll_news.setOnClickListener(this);

        et_search.setOnClickListener(this);

        bottomBar = (BottomBar) getActivity().findViewById(R.id.bottomBar);
    }

    private void initData() {

        checkAlarm();

        //news Data
        news_img.setImageResource(R.drawable.article_pres);
        news_title.setText("如何在家测血压？");
        news_type.setText("血压");

    }

    @Override
    public void onResume() {
        super.onResume();
        checkAlarm();
    }


    private void checkAlarm() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("clockActivity", Context.MODE_PRIVATE);
        clock_time = sharedPref.getString(getString(R.string.clock), null);

        if (clock_time != null) {
            Log.e("home_clock", clock_time);
            clock_no_data.setVisibility(View.GONE);
            ll_clock.setVisibility(View.VISIBLE);

            clockView.setText(clock_time);
            if (getToggle()) {
                clockToggle.setChecked(true);
            } else {
                clockToggle.setChecked(false);
            }

        } else {
            Log.e("home_clock_null", "none");
            clock_no_data.setVisibility(View.VISIBLE);
            ll_clock.setVisibility(View.GONE);
        }
    }





    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.home_manual_img:
                Intent intent = new Intent();
                intent.setClass(getActivity(), HomeManualActivity.class);
                startActivity(intent);
                break;
            case R.id.home_auto_img:
                Intent intent_auto = new Intent(getActivity(), BleActivity.class);
                startActivity(intent_auto);
                break;*/


            case R.id.home_clock:
                Intent intent_clock = new Intent(getActivity(), ClockActivity.class);
                startActivity(intent_clock);
                break;
            case R.id.home_clock_toggle:
                if (clockToggle.isChecked()) {
                    saveToggle(true);
                    StartAlarm();
                    Toast.makeText(getActivity(), "已设置提醒", Toast.LENGTH_SHORT).show();
                } else {
                    saveToggle(false);
                    CancleAlarm();
                    Toast.makeText(getActivity(), "已取消提醒", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.ll_home_news:

                //to healthFragment
                Fragment fragment = new HealthFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.content, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.home_et_search:

                Intent intent_search = new Intent();
                intent_search.setClass(getActivity(), SearchActivity.class);
                startActivity(intent_search);
                break;
            default:
                break;
        }
    }

    private void StartAlarm() {
        String[] strings = clock_time.split(":");
        hour = Integer.parseInt(strings[0]);
        minute = Integer.parseInt(strings[1]);

        //闹铃注册
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long firstTime = SystemClock.elapsedRealtime();
        long systemTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long selectTime = calendar.getTimeInMillis();

        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
        long time = selectTime - systemTime;
        firstTime += time;

        //闹铃注册
        AlarmManager manager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                firstTime, 24 * 60 * 60 * 1000, sender);

    }

    private void CancleAlarm() {
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(
                getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        manager.cancel(sender);
    }

    private void saveToggle(Boolean isChecked) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("toggle", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.toggle), isChecked);
        editor.commit();
    }

    private Boolean getToggle() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("toggle", Context.MODE_PRIVATE);
        return sharedPref.getBoolean(getString(R.string.toggle), true);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
