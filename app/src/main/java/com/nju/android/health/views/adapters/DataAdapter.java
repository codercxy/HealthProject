package com.nju.android.health.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.model.data.Disease;
import com.nju.android.health.utils.CircleImg;
import com.nju.android.health.utils.RecyclerViewClickListener;

import java.util.List;

/**
 * Created by chy on 2017/6/25.
 */

public class DataAdapter extends RecyclerView.Adapter<DataViewHolder>{

    private List<Disease> dataList;
    private RecyclerViewClickListener listener;

    public DataAdapter(List<Disease> list) {
        dataList = list;
    }
    public void setRecyclerViewListener(RecyclerViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disease, parent, false);

        return new DataViewHolder(view, listener);
    }


    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        holder.imageView.setImageResource(dataList.get(position).getImgRes());
        holder.textView.setText(dataList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

class DataViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener{
    private final RecyclerViewClickListener listener;

    public ImageView imageView;
    public TextView textView;

    public DataViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);
        this.listener = listener;
        imageView = (ImageView) itemView.findViewById(R.id.data_item_img);
        textView = (TextView) itemView.findViewById(R.id.data_item_text);

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
