package com.nju.android.health.views.activities.next;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.views.activities.next.Reserve.ReserveDoctorActivity;
import com.nju.android.health.views.activities.next.Reserve.ReserveRoomActivity;
import com.nju.android.health.views.activities.next.Reserve.ReserveTimeActivity;
import com.nju.android.health.views.activities.next.Reserve.ReserveUserActivity;

public class ReserveActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_room;
    private TextView tv_time;
    private TextView tv_doctor;
    private TextView tv_level;
    private TextView tv_poi;
    private TextView tv_cost;
    private TextView tv_usr;
    private TextView tv_usrid;

    private ImageView more_room;
    private ImageView more_time;
    private ImageView more_doctor;
    private ImageView more_usr;

    private Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        initView();

    }


    private void initView() {
        tv_room = (TextView) findViewById(R.id.reserve_room);
        tv_time = (TextView) findViewById(R.id.reserve_time);
        tv_doctor = (TextView) findViewById(R.id.reserve_doctor);
        tv_level = (TextView) findViewById(R.id.reserve_doctor_level);
        tv_poi = (TextView) findViewById(R.id.reserve_doctor_poi);
        tv_usr = (TextView) findViewById(R.id.reserve_usr);
        tv_usrid = (TextView) findViewById(R.id.reserve_usrid);
        tv_cost = (TextView) findViewById(R.id.reserve_cost);

        more_room = (ImageView) findViewById(R.id.reserve_room_more);
        more_time = (ImageView) findViewById(R.id.reserve_time_more);
        more_doctor = (ImageView) findViewById(R.id.reserve_doctor_more);
        more_usr = (ImageView) findViewById(R.id.reserve_usr_more);

        confirm = (Button) findViewById(R.id.reserve_confirm);

        more_room.setOnClickListener(this);
        more_time.setOnClickListener(this);
        more_doctor.setOnClickListener(this);
        more_usr.setOnClickListener(this);
        confirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();


        switch (v.getId()) {

            case R.id.reserve_room_more:
                intent.setClass(ReserveActivity.this, ReserveRoomActivity.class);
                break;
            case R.id.reserve_time_more:
                intent.setClass(ReserveActivity.this, ReserveTimeActivity.class);
                break;
            case R.id.reserve_doctor_more:
                intent.setClass(ReserveActivity.this, ReserveDoctorActivity.class);
                break;
            case R.id.reserve_usr_more:
                intent.setClass(ReserveActivity.this, ReserveUserActivity.class);
                break;
            case R.id.reserve_confirm:
                break;

        }
        startActivity(intent);

    }
}
