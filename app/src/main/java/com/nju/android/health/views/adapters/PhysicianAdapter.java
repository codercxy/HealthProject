package com.nju.android.health.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.model.data.Physician;
import com.nju.android.health.utils.RecyclerItemClickListener;
import com.nju.android.health.utils.RecyclerViewClickListener;

import java.util.List;

/**
 * Created by chy on 2017/2/23.
 */

public class PhysicianAdapter extends RecyclerView.Adapter<PhysicianViewHolder> {

    private List<Physician> phyList;
    private RecyclerViewClickListener listener;

    public PhysicianAdapter(List<Physician> list) {
        phyList = list;
    }

    public void setOnRecyclerListListener(RecyclerViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public PhysicianViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phy, parent, false);
        return new PhysicianViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(PhysicianViewHolder holder, int position) {
        holder.headImage.setImageResource(phyList.get(position).getImageRes());
        holder.name.setText(phyList.get(position).getName());
        holder.room.setText(phyList.get(position).getRoom());
        holder.level.setText(phyList.get(position).getLevel());
        holder.location.setText(phyList.get(position).getLoc());
        holder.gootAt.setText(phyList.get(position).getGoodAt());
    }

    @Override
    public int getItemCount() {
        return phyList.size();
    }
}


class PhysicianViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener{

    private final RecyclerViewClickListener mListener;
    public ImageView headImage;
    public TextView name;
    public TextView room;
    public TextView level;
    public TextView location;
    public TextView gootAt;

    public PhysicianViewHolder(View v, RecyclerViewClickListener listener) {
        super(v);

        headImage = (ImageView) v.findViewById(R.id.item_phy_image);
        name = (TextView) v.findViewById(R.id.item_phy_name);
        room = (TextView) v.findViewById(R.id.item_phy_room);
        level = (TextView) v.findViewById(R.id.item_phy_level);
        location = (TextView) v.findViewById(R.id.item_phy_loc);
        gootAt = (TextView) v.findViewById(R.id.item_phy_good_at);

        mListener = listener;

        v.setOnTouchListener(this);
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_MOVE) {

            mListener.onClick(v, getPosition());
        }
        return true;
    }
}