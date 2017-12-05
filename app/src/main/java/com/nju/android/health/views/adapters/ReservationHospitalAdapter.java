package com.nju.android.health.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.model.data.Hospital;
import com.nju.android.health.utils.RecyclerViewClickListener;

import java.util.List;

/**
 * Created by chy on 2017/12/5.
 */

public class ReservationHospitalAdapter extends RecyclerView.Adapter<ReservationHospitalViewHolder>{
    private List<Hospital> list;
    private RecyclerViewClickListener listener;

    public ReservationHospitalAdapter(List<Hospital> list) {
        this.list = list;
    }

    public void setRecyclerViewListener(RecyclerViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ReservationHospitalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation_hospital, parent, false);
        return new ReservationHospitalViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ReservationHospitalViewHolder holder, int position) {
        holder.imageView.setImageResource(list.get(position).getResId());
        holder.hospital.setText(list.get(position).getName());
        holder.level.setText(list.get(position).getLevel());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class ReservationHospitalViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

    private final RecyclerViewClickListener listener;
    public ImageView imageView;
    public TextView hospital;
    public TextView level;


    public ReservationHospitalViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);
        this.listener = listener;

        imageView = (ImageView) itemView.findViewById(R.id.item_reservation_img);
        hospital = (TextView) itemView.findViewById(R.id.item_reservation_hospital);
        level = (TextView) itemView.findViewById(R.id.item_reservation_level);

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