package com.nju.android.health.views.activities.next.Reserve;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nju.android.health.R;
import com.nju.android.health.model.data.Room;
import com.nju.android.health.utils.DividerItemDecoration;
import com.nju.android.health.utils.RecyclerViewClickListener;
import com.nju.android.health.views.adapters.ReserveRoomAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReserveRoomActivity extends AppCompatActivity implements RecyclerViewClickListener{

    private RecyclerView recyclerview;
    private List<Room> roomList;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_room);

        context = ReserveRoomActivity.this;
        initView();

        initData();
    }

    private void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.reserve_pick_room);
    }

    private void initData() {
        roomList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Room room = new Room();
            room.setName("room " + i);
            roomList.add(room);
        }

        initRecycler();

    }

    private void initRecycler() {
        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        ReserveRoomAdapter adapter = new ReserveRoomAdapter(roomList);
        adapter.setRecyclerViewListener(this);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void onClick(View v, int position) {

    }
}
