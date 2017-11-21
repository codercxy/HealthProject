package com.nju.android.health.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.nju.android.health.utils.RecyclerViewClickListener;

import org.w3c.dom.Text;

/**
 * Created by chy on 2017/11/21.
 */

public class ReserveDoctorDocAdapter {
}
class ReserveDoctorLeftViewHodler extends RecyclerView.ViewHolder implements View.OnTouchListener{
    public TextView docName;
    public TextView remain;
    private final RecyclerViewClickListener listener;
    public ReserveDoctorLeftViewHodler(View itemview, RecyclerViewClickListener listener) {
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