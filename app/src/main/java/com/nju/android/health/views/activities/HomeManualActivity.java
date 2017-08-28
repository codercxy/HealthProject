package com.nju.android.health.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nju.android.health.R;
import com.nju.android.health.bswk.AddUricAcidActivity;
import com.nju.android.health.bswk.XuetangAdd;
import com.nju.android.health.bswk.body.RentichengfenAdd;
import com.nju.android.health.bswk.brain.NaozuzhongActivity;
import com.nju.android.health.bswk.stone.AddCholesterolActivity;
import com.nju.android.health.views.activities.glucose.GlucoseActivity;
import com.nju.android.health.views.activities.next.AddXyrecorder;
import com.nju.android.health.views.activities.pressure.ManualActivity;

import java.text.SimpleDateFormat;

public class HomeManualActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView pressure_img;
    private ImageView sugar_img;
    private ImageView acid_img;
    private ImageView stone_img;
    private ImageView body_img;
    private ImageView brain_img;

    public static int arrive_year;
    public static int arrive_month;
    public static int arrive_day;
    public static int arrive_hour;
    public static int arrive_min;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_manual);

        initView();
    }
    private void initView() {
        pressure_img = (ImageView) findViewById(R.id.home_manual_pressure);
        sugar_img = (ImageView) findViewById(R.id.home_manual_sugar);
        acid_img = (ImageView) findViewById(R.id.home_manual_acid);
        stone_img = (ImageView) findViewById(R.id.home_manual_stone);
        body_img = (ImageView) findViewById(R.id.home_manual_body);
        brain_img = (ImageView) findViewById(R.id.home_manual_brain);

        pressure_img.setOnClickListener(this);
        sugar_img.setOnClickListener(this);
        acid_img.setOnClickListener(this);
        stone_img.setOnClickListener(this);
        body_img.setOnClickListener(this);
        brain_img.setOnClickListener(this);
        //获取系统时间
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        final String system = sDateFormat.format(new java.util.Date());
    }

    @Override
    public void onClick(View view) {
        Intent intent  = new Intent();

        switch (view.getId()) {
            case R.id.home_manual_pressure:

                intent.setClass(HomeManualActivity.this, AddXyrecorder.class);
                startActivity(intent);
                break;
            case R.id.home_manual_sugar:
//                intent2.setClass(HomeManualActivity.this, GlucoseActivity.class);
                intent.setClass(HomeManualActivity.this, XuetangAdd.class);
                startActivity(intent);
                break;

            case R.id.home_manual_acid:

                intent.setClass(HomeManualActivity.this, AddUricAcidActivity.class);
                startActivity(intent);
                break;
            case R.id.home_manual_stone:

                intent.setClass(HomeManualActivity.this, AddCholesterolActivity.class);
                startActivity(intent);
                break;
            case R.id.home_manual_body:

                intent.setClass(HomeManualActivity.this, RentichengfenAdd.class);
                startActivity(intent);
                break;
            case R.id.home_manual_brain:

                intent.setClass(HomeManualActivity.this, NaozuzhongActivity.class);
                startActivity(intent);
                break;
        }
    }
}
