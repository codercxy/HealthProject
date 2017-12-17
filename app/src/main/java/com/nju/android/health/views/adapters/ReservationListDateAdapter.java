package com.nju.android.health.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.model.data.ReservationDoctor;
import com.nju.android.health.utils.RecyclerViewClickListener;

import java.util.List;

/**
 * Created by chy on 2017/12/7.
 */

public class ReservationListDateAdapter extends RecyclerView.Adapter<ReservationListDateViewHolder>{
    private List<ReservationDoctor> list;
    private RecyclerViewClickListener listener;
    public ReservationListDateAdapter(List<ReservationDoctor> list) {
        this.list = list;
    }
    public void setRecyclerViewListener(RecyclerViewClickListener listener) {
        this.listener = listener;
    }
    @Override
    public ReservationListDateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation_list_by_date, parent, false);
        return new ReservationListDateViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ReservationListDateViewHolder holder, int position) {
        holder.doctorname.setText(list.get(position).getName());
        holder.level.setText(list.get(position).getLevel());
        holder.goodAt.setText(list.get(position).getGoodAt());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class ReservationListDateViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
    private final RecyclerViewClickListener listener;
    public TextView doctorname;
    public TextView level;
    public TextView goodAt;


    public ReservationListDateViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);
        doctorname = (TextView) itemView.findViewById(R.id.item_reservation_list_by_date_name);
        level = (TextView) itemView.findViewById(R.id.item_reservation_list_by_date_level);
        goodAt = (TextView) itemView.findViewById(R.id.item_reservation_list_by_date_good_at);


        this.listener = listener;
        itemView.setOnTouchListener(this);
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