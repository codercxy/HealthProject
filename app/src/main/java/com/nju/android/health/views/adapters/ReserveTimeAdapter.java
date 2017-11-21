package com.nju.android.health.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.nju.android.health.utils.RecyclerViewClickListener;

/**
 * Created by chy on 2017/11/21.
 */

public class ReserveTimeAdapter {
}
class ReserveTimeViewHodler extends RecyclerView.ViewHolder implements View.OnTouchListener{
    public TextView docName;
    public TextView time;
    public TextView reamin;
    private final RecyclerViewClickListener listener;
    public ReserveTimeViewHodler(View itemview, RecyclerViewClickListener listener) {
        super((itemview));
        this.listener = listener;

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