package com.nju.android.health.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.model.data.Pressure;
import com.nju.android.health.utils.RecyclerViewClickListener;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by chy on 2017/2/22.
 */

public class DataListAdapter extends RecyclerView.Adapter<DataListViewHolder> {

    private List<Pressure> dataList;
    private RecyclerViewClickListener listener;

    public DataListAdapter(List<Pressure> list) {
        dataList = list;
    }
    public void setRecyclerViewListener(RecyclerViewClickListener listener) {
        this.listener = listener;
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

    @Override
    public DataListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_list, parent, false);
        return new DataListViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(DataListViewHolder holder, int position) {

        String dateTime = dataList.get(position).getTime();
        holder.date.setText(getDate(dateTime));
        holder.time.setText(getTime(dateTime));
        holder.high.setText(String.valueOf(dataList.get(position).getHigh()));
        holder.low.setText(String.valueOf(dataList.get(position).getLow()));
        holder.rate.setText(String.valueOf(dataList.get(position).getRate()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public String getDate(String dateTime){
        Date strToDate = sdf.parse(dateTime, new ParsePosition(0));
        SimpleDateFormat newSdf = new SimpleDateFormat("yyyy/MM/dd");
        return newSdf.format(strToDate);
    }

    public String getTime(String dateTime) {
        Date strToDate = sdf.parse(dateTime, new ParsePosition(0));
        SimpleDateFormat newSdf = new SimpleDateFormat("HH:mm");
        return newSdf.format(strToDate);
    }
}

class DataListViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener{

    private final RecyclerViewClickListener listener;
    public TextView date, time;
    public TextView high, low, rate;


    public DataListViewHolder(View itemview, RecyclerViewClickListener listener) {
        super(itemview);

        date = (TextView) itemview.findViewById(R.id.data_list_date);
        time = (TextView) itemview.findViewById(R.id.data_list_time);
        high = (TextView) itemview.findViewById(R.id.data_list_high);
        low = (TextView) itemview.findViewById(R.id.data_list_low);
        rate = (TextView) itemview.findViewById(R.id.data_list_rate);

        itemview.setOnTouchListener(this);
        this.listener = listener;

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

