package com.nju.android.health.views.activities.next.Reservation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.nju.android.health.R;

public class ReservationMainActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout reserve;
    private LinearLayout history;
    private LinearLayout follow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_main);
        initView();
    }

    private void initView() {
        reserve = (LinearLayout) findViewById(R.id.reservation_main_reserve);
        history = (LinearLayout) findViewById(R.id.reservation_main_history);
        follow = (LinearLayout) findViewById(R.id.reservation_main_follow);

        reserve.setOnClickListener(this);
        history.setOnClickListener(this);
        follow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.reservation_main_reserve:
                intent.setClass(ReservationMainActivity.this, ReservationChooseRoomActivity.class);
                break;
            case R.id.reservation_main_history:
                break;
            case R.id.reservation_main_follow:
                break;
        }

        startActivity(intent);
    }
}
