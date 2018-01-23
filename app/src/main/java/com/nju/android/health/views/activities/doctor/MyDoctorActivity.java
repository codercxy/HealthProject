package com.nju.android.health.views.activities.doctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.nju.android.health.MyApplication;
import com.nju.android.health.R;
import com.nju.android.health.model.data.Doctor;
import com.nju.android.health.utils.RecyclerInsetsDecoration;
import com.nju.android.health.utils.RecyclerViewClickListener;
import com.nju.android.health.utils.VolleyRequestImp;
import com.nju.android.health.views.adapters.MyDoctorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDoctorActivity extends AppCompatActivity implements RecyclerViewClickListener{

    private RecyclerView mRecycler;
    private MyDoctorAdapter mAdapter;
    private List<Doctor> mDoctors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctor);

        initView();

        initData();

        initRecycler();

        showDoctors(mDoctors);
    }

    private void initView() {
        mRecycler = (RecyclerView) findViewById(R.id.doctor_my_recycler);
    }

    private void initData() {


        mDoctors = new ArrayList<Doctor>();
        Doctor doctor = new Doctor();
        doctor.setPicRes(R.drawable.doctor);
        doctor.setName("王彬入");
        doctor.setOffice("神经内科");
        doctor.setLoc("南京市人民医院");
        doctor.setNum(100);
        doctor.setGood(90);

        Doctor doctor2 = new Doctor();
        doctor2.setPicRes(R.drawable.doctor_1);
        doctor2.setName("赵翔");
        doctor2.setOffice("骨科");
        doctor2.setLoc("南京市人民医院");
        doctor2.setNum(200);
        doctor2.setGood(94);

        Doctor doctor3 = new Doctor();
        doctor3.setPicRes(R.drawable.doctor_2);
        doctor3.setName("李强");
        doctor3.setOffice("内科");
        doctor3.setLoc("南京市人民医院");
        doctor3.setNum(100);
        doctor3.setGood(90);

        mDoctors.add(doctor);
        mDoctors.add(doctor2);
        mDoctors.add(doctor3);

    }

    private void initRecycler() {
        mRecycler.addItemDecoration(new RecyclerInsetsDecoration(this));
    }

    private void showDoctors(List<Doctor> doctors) {
        mAdapter = new MyDoctorAdapter(doctors);
        mAdapter.setRecyclerListListener(this);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v, int position) {
        Intent intent  = new Intent(MyDoctorActivity.this, DoctorActivity.class);
        intent.putExtra("doctor_id", position);
        startActivity(intent);

    }
}
