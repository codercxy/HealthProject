package com.nju.android.health.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.model.data.Room;
import com.nju.android.health.utils.RecyclerViewClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chy on 2017/11/21.
 */

public class ReserveRoomAdapter extends RecyclerView.Adapter<ReserveRoomViewHodler> {
    private RecyclerViewClickListener listener;
    List<Room> list = new ArrayList<>();

    public ReserveRoomAdapter(List<Room> roomList) {
        this.list = roomList;
    }

    public void setRecyclerViewListener(RecyclerViewClickListener listener) {
        this.listener = listener;
    }


    @Override
    public ReserveRoomViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserve_room, parent, false);
        return new ReserveRoomViewHodler(view, listener);
    }

    @Override
    public void onBindViewHolder(ReserveRoomViewHodler holder, int position) {
        holder.roomName.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class ReserveRoomViewHodler extends RecyclerView.ViewHolder implements View.OnTouchListener{
    public TextView roomName;
    private final RecyclerViewClickListener listener;
    public ReserveRoomViewHodler(View itemview, RecyclerViewClickListener listener) {
        super(itemview);
        this.listener = listener;
        roomName = (TextView) itemview.findViewById(R.id.item_reserve_name_room);
        itemview.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_MOVE) {

            listener.onClick(v, getPosition());
            Log.e("diagnoseadapter", String.valueOf(getPosition()));
        }
        return true;

    }
}