package com.nju.android.health.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.utils.RecyclerViewClickListener;

import java.util.List;

/**
 * Created by chy on 2017/12/6.
 */

public class ReservationListDoctorDateAdapter extends RecyclerView.Adapter<ReservationListDoctorDateViewHolder>{
    private RecyclerViewClickListener listener;
    private List<String> list;

    public ReservationListDoctorDateAdapter(List<String> list) {
        this.list = list;
    }
    public void setRecyclerViewListener(RecyclerViewClickListener listener) {
        this.listener = listener;
    }
    @Override
    public ReservationListDoctorDateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserve_list_by_doctor_detail, parent, false);
        return new ReservationListDoctorDateViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ReservationListDoctorDateViewHolder holder, int position) {
        holder.dateText.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}



class ReservationListDoctorDateViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
    private final RecyclerViewClickListener listener;
    public TextView dateText;
    public ReservationListDoctorDateViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);

        dateText = (TextView) itemView.findViewById(R.id.item_reserve_list_by_doctor_detail_date);
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