package com.nju.android.health.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.model.data.ReservationDetail;
import com.nju.android.health.utils.RecyclerViewClickListener;

import java.util.List;

/**
 * Created by chy on 2017/12/12.
 */

public class ReservationDetailAdapter extends RecyclerView.Adapter<ReservationDetailViewHolder>{
    private RecyclerViewClickListener listener;
    private List<ReservationDetail> dataList;

    public ReservationDetailAdapter(List<ReservationDetail> list) {
        dataList = list;
    }
    public void setRecyclerViewListener(RecyclerViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ReservationDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation_detail, parent, false);
        return new ReservationDetailViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ReservationDetailViewHolder holder, int position) {
        //!!!!设置时间范围
        holder.timeText.setText(dataList.get(position).getStarttime());
        holder.remainText.setText(String.valueOf(dataList.get(position).getRemain()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
class ReservationDetailViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener{
    private final RecyclerViewClickListener listener;


    public TextView timeText;
    public TextView remainText;

    public ReservationDetailViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);
        this.listener = listener;
        timeText = (TextView) itemView.findViewById(R.id.item_reservation_detail_time);
        remainText = (TextView) itemView.findViewById(R.id.item_reservation_detail_remain);

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