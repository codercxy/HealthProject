package com.nju.android.health.views.activities.next.Reservation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nju.android.health.R;
import com.nju.android.health.model.data.ReservationDetail;
import com.nju.android.health.utils.CircleImg;
import com.nju.android.health.utils.DividerItemDecoration;
import com.nju.android.health.utils.RecyclerViewClickListener;
import com.nju.android.health.views.adapters.ReservationDetailAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ReservationDetailActivity extends AppCompatActivity implements RecyclerViewClickListener{

    private TextView nameText, levelText, roomText, hospitalText;
    private Integer headRes;
    private RecyclerView recyclerView;
    private List<ReservationDetail> dataList;
    private ReservationDetailAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_deatil);
        context = this;
        initView();

    }

    private void initView() {
        nameText = (TextView) findViewById(R.id.reservation_detail_name);
        levelText = (TextView) findViewById(R.id.reservation_detail_level);
        roomText = (TextView) findViewById(R.id.reservation_detail_room);
        hospitalText = (TextView) findViewById(R.id.reservation_detail_hospital);

        recyclerView = (RecyclerView) findViewById(R.id.reservation_detail_recycler);

        initData();
    }
    private void initData() {
        dataList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ReservationDetail detail = new ReservationDetail();
            detail.setStarttime("12：00-13：00");
            detail.setRemain(7);
            dataList.add(detail);
        }

        initRecycler();
    }

    private void initRecycler() {
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReservationDetailAdapter(dataList);
        adapter.setRecyclerViewListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v, int position) {
        showDialog();
    }
    private void showDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_reservation_detail, null);

        dialog.setView(view);
        dialog.setTitle("请确认信息")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "预约已发送", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
