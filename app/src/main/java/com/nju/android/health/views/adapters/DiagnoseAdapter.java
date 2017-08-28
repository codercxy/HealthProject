package com.nju.android.health.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.model.data.DiagnoseItem;
import com.nju.android.health.utils.CircleImg;
import com.nju.android.health.utils.RecyclerViewClickListener;

import java.util.List;

/**
 * Created by 57248 on 2016/12/28.
 */

public class DiagnoseAdapter extends RecyclerView.Adapter<DiagnoseViewHolder>{
    private List<DiagnoseItem> itemList;

    private RecyclerViewClickListener listener;

    public DiagnoseAdapter(List<DiagnoseItem> list) {
        this.itemList = list;
    }

    public void setRecyclerListener(RecyclerViewClickListener mlistener) {
        this.listener = mlistener;
    }

    @Override
    public void onBindViewHolder(DiagnoseViewHolder holder, int position) {
        holder.imageView.setImageResource(itemList.get(position).getImgRes());
        holder.textView.setText(itemList.get(position).getText());
    }

    @Override
    public DiagnoseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diagnose, parent, false);
        return new DiagnoseViewHolder(view, listener);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

class DiagnoseViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
    private final RecyclerViewClickListener mListener;
    public CircleImg imageView;
    public TextView textView;

    public DiagnoseViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);

        imageView = (CircleImg) itemView.findViewById(R.id.diagnose_item_img);
        textView = (TextView) itemView.findViewById(R.id.diagnose_item_text);
        this.mListener = listener;
        itemView.setOnTouchListener(this);
    }
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_MOVE) {

            mListener.onClick(view, getPosition());
            Log.e("diagnoseadapter", String.valueOf(getPosition()));
        }
        return true;
    }
}