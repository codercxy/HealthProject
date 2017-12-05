package com.nju.android.health.views.activities.next.Reserve;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;

import com.nju.android.health.R;
import com.nju.android.health.utils.RecyclerViewClickListener;

public class ReserveTimeActivity extends AppCompatActivity implements RecyclerViewClickListener{

    private DatePicker datePicker;
    private RecyclerView recyclerView;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_time);

        context = ReserveTimeActivity.this;

        initView();

        updateData();
    }

    private void initView() {
        datePicker = (DatePicker) findViewById(R.id.reserve_pick_time);
        recyclerView = (RecyclerView) findViewById(R.id.reserve_time_recycler);

    }

    private void updateData() {

    }



    @Override
    public void onClick(View v, int position) {

    }
}
