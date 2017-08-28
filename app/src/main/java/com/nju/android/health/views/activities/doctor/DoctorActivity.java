package com.nju.android.health.views.activities.doctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.nju.android.health.MyApplication;
import com.nju.android.health.R;
import com.nju.android.health.utils.CircleImg;

public class DoctorActivity extends AppCompatActivity implements View.OnClickListener{

    private CircleImg headImg;
    private TextView nameText, roomText, poiText, locText;
//    private TextView follow;
    private LinearLayout ll_phone, ll_hospital, ll_private, ll_video;
    private TextView detailText;

    private TextView tab;
    private Integer headImgRes;
    private String name, room, poi, loc;
    private String detail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        initView();

        initData();

        setUpView();

    }

    private void initView() {
        headImg = (CircleImg) findViewById(R.id.doctor_circleimg);
        nameText = (TextView) findViewById(R.id.doctor_name);
        roomText = (TextView) findViewById(R.id.doctor_room);
        poiText = (TextView) findViewById(R.id.doctor_position);
        locText = (TextView) findViewById(R.id.doctor_location);
        tab = (TextView) findViewById(R.id.doctor_tab);

//        follow = (TextView) findViewById(R.id.doctor_follow);

//        ll_chat = (LinearLayout) findViewById(R.id.ll_doctor_chat);
        ll_phone = (LinearLayout) findViewById(R.id.ll_doctor_phone);
        ll_hospital = (LinearLayout) findViewById(R.id.ll_doctor_hospital);
        ll_private = (LinearLayout) findViewById(R.id.ll_doctor_private);
        ll_video = (LinearLayout) findViewById(R.id.ll_doctor_video);

        detailText = (TextView) findViewById(R.id.doctor_detail);

//        follow.setOnClickListener(this);
//        ll_chat.setOnClickListener(this);
        ll_phone.setOnClickListener(this);
        ll_hospital.setOnClickListener(this);
        ll_private.setOnClickListener(this);
        ll_video.setOnClickListener(this);
        tab.setOnClickListener(this);

    }

    private void initData() {
        Bundle bundle = getIntent().getBundleExtra("doctor");
        headImgRes = bundle.getInt("headImg");
        name = bundle.getString("name");
        room = bundle.getString("room");
        poi = bundle.getString("level");
        loc = bundle.getString("loc");
        detail = bundle.getString("goodAt");

        /*int id = getIntent().getIntExtra("doctor_id", 1);
        switch (id) {
            case 0:
                headImgRes = R.drawable.doctor;
                name = "王彬入";
                room = "神经内科";
                poi = "主任";
                loc = "南京市人民医院";
                detail = "神经内科";
                break;
            case 1:
                headImgRes = R.drawable.doctor_1;
                name = "赵翔";
                room = "骨科";
                poi = "主任";
                loc = "南京市人民医院";
                detail = "骨科";
                break;
            case 2:
                headImgRes = R.drawable.doctor_2;
                name = "李强";
                room = "内科";
                poi = "专家";
                loc = "南京市人民医院";
                detail = "内科";
                break;
        }*/
        /*headImgRes = R.drawable.doctor;
        name = "王彬入";
        room = "神经内科";
        poi = "主任";
        loc = "南京市人民医院";
        detail = "神经内科";*/
    }

    private void setUpView() {
        headImg.setImageResource(headImgRes);
        nameText.setText(name);
        roomText.setText(room);
        poiText.setText(poi);
        locText.setText(loc);
        detailText.setText(detail);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.doctor_follow:
//                break;
//            case R.id.ll_doctor_chat:
//                YWIMKit mIMKit = YWAPI.getIMKitInstance(MyApplication.getInstance().userid,
//                        MyApplication.getInstance().APP_KEY);
//
//                String target = "testpro2";
//                Intent intent = mIMKit.getChattingActivityIntent(target);
//                startActivity(intent);
//                break;
            case R.id.ll_doctor_phone:
                break;
            case R.id.ll_doctor_hospital:
                break;
            case R.id.ll_doctor_private:
                break;
            case R.id.ll_doctor_video:
                break;
            case R.id.doctor_tab:
                YWIMKit mIMKit = YWAPI.getIMKitInstance(MyApplication.getInstance().userid,
                        MyApplication.getInstance().APP_KEY);

                String target = "testpro2";
                Intent intent = mIMKit.getChattingActivityIntent(target);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
