package com.nju.android.health.views.activities.next.Reservation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.model.data.ReservationDoctor;
import com.nju.android.health.utils.DividerItemDecoration;
import com.nju.android.health.utils.RecyclerViewClickListener;
import com.nju.android.health.views.adapters.ReservationListDateAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ReservationListByDateFragment extends Fragment implements RecyclerViewClickListener, View.OnClickListener{

    private TextView dateText;
    private RecyclerView recyclerView;
    private List<ReservationDoctor> doctorList;
    private ReservationListDateAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reservation_list_by_date, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        dateText = (TextView) view.findViewById(R.id.reservation_list_by_date_date);
        recyclerView = (RecyclerView) view.findViewById(R.id.reservation_list_by_date_recycler);
        dateText.setOnClickListener(this);

        initData();

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ReservationListDateAdapter(doctorList);
        adapter.setRecyclerViewListener(this);
        recyclerView.setAdapter(adapter);

    }

    private void initData() {
        List<String> stringList = new ArrayList<>();
        stringList.add("2017/12/06");
        stringList.add("2017/12/07");
        stringList.add("2017/12/08");

        doctorList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ReservationDoctor doctor = new ReservationDoctor();
            doctor.setName("Doctor" + i);
            doctor.setLevel("专家");
            doctor.setGoodAt("暂无");
            doctor.setRemain(stringList);
            doctor.setVisible(false);
            doctorList.add(doctor);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        dateText.setText(format.format(date));
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        final View dialogView = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_reservation_date, null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.dialog_reserve_dp);
        datePicker.setCalendarViewShown(false);

        dialog.setTitle("选择日期");
        dialog.setView(dialogView);
        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth() + 1;
                        int day = datePicker.getDayOfMonth();
                        dateText.setText(year + "年" + month + "月" + day + "日");
                    }
                });
        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reservation_list_by_date_date:

                showDialog();
                break;
        }
    }

    @Override
    public void onClick(View v, int position) {
        Intent intent = new Intent(getActivity(), ReservationDetailActivity.class);
        startActivity(intent);
    }
}
