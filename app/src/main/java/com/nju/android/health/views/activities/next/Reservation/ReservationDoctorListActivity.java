package com.nju.android.health.views.activities.next.Reservation;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nju.android.health.R;

public class ReservationDoctorListActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView doctor;
    private TextView date;
    private ReservationListByDoctorFragment doctorFragment;
    private ReservationListByDateFragment dateFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_doctor_list);

        initView();
    }

    private void initView() {
        doctor = (TextView) findViewById(R.id.reservation_doctor_list_doctor);
        date = (TextView) findViewById(R.id.reservation_doctor_list_date);

        doctor.setOnClickListener(this);
        date.setOnClickListener(this);

        resetTab();
        doctor.setTextColor(getResources().getColor(R.color.aliwx_white));
        doctor.setBackground(getResources().getDrawable(R.drawable.shape_corner_left_blue));

        initFragment();
    }
    private void initFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        doctorFragment = new ReservationListByDoctorFragment();
        ft.add(R.id.container_reservation_doctor, doctorFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.reservation_doctor_list_doctor:
                resetTab();
                doctor.setTextColor(getResources().getColor(R.color.aliwx_white));
                doctor.setBackground(getResources().getDrawable(R.drawable.shape_corner_left_blue));
                hideFragments(ft);
                if (doctorFragment == null) {
                    doctorFragment = new ReservationListByDoctorFragment();
                    ft.add(R.id.container_reservation_doctor, doctorFragment);
                } else {
                    ft.show(doctorFragment);
                }
                break;
            case R.id.reservation_doctor_list_date:
                resetTab();
                date.setTextColor(getResources().getColor(R.color.aliwx_white));
                date.setBackground(getResources().getDrawable(R.drawable.shape_corner_right_blue));
                hideFragments(ft);
                if (dateFragment == null) {
                    dateFragment = new ReservationListByDateFragment();
                    ft.add(R.id.container_reservation_doctor, dateFragment);
                } else {
                    ft.show(dateFragment);
                }
                break;
        }
        ft.commit();
    }
    private void hideFragments(FragmentTransaction ft) {
        if (doctorFragment != null) {
            ft.hide(doctorFragment);
        }
        if (dateFragment != null) {
            ft.hide(dateFragment);
        }
    }

    private void resetTab() {
        doctor.setTextColor(getResources().getColor(R.color.search_blue));
        doctor.setBackground(getResources().getDrawable(R.drawable.shape_corner_left));
        date.setTextColor(getResources().getColor(R.color.search_blue));
        date.setBackground(getResources().getDrawable(R.drawable.shape_corner_right));
    }


}
