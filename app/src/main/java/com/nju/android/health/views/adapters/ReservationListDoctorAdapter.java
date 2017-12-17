package com.nju.android.health.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
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
import com.nju.android.health.utils.DividerItemDecoration;
import com.nju.android.health.utils.RecyclerViewClickListener;
import com.nju.android.health.views.activities.next.Reservation.ReservationDetailActivity;

import java.util.List;

/**
 * Created by chy on 2017/12/6.
 */

public class ReservationListDoctorAdapter extends RecyclerView.Adapter<ReservationListDoctorViewHolder> implements RecyclerViewClickListener{
    private List<ReservationDoctor> list;
    private RecyclerViewClickListener listener;
    private Context context;

    public ReservationListDoctorAdapter(List<ReservationDoctor> list) {
        this.list = list;
    }
    public void setRecyclerViewListener(Context context, RecyclerViewClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ReservationListDoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation_list_by_doctor, parent, false);
        return new ReservationListDoctorViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ReservationListDoctorViewHolder holder, int position) {
        holder.doctorname.setText(list.get(position).getName());
        holder.level.setText(list.get(position).getLevel());
        holder.goodAt.setText(list.get(position).getGoodAt());

        holder.linearLayout.measure(0, 0);
        if (list.get(position).isVisible()) {
            holder.recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            ReservationListDoctorDateAdapter adapter = new ReservationListDoctorDateAdapter(list.get(position).getRemain());
            // listener 两个listener是否一致？
            adapter.setRecyclerViewListener(this);
            holder.recyclerView.setAdapter(adapter);
            ((LinearLayout.LayoutParams) holder.recyclerView.getLayoutParams()).bottomMargin = 0;
            holder.linearLayout.setVisibility(View.VISIBLE);
        } else {
            ((LinearLayout.LayoutParams) holder.recyclerView.getLayoutParams()).bottomMargin =
                    (-1) * holder.recyclerView.getMeasuredHeight();
            holder.linearLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v, int position) {
        Intent intent = new Intent(context, ReservationDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class ReservationListDoctorViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
    private final RecyclerViewClickListener listener;
    public TextView doctorname;
    public TextView level;
    public TextView goodAt;
    public RecyclerView recyclerView;
    public LinearLayout linearLayout;

    public ReservationListDoctorViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);
        doctorname = (TextView) itemView.findViewById(R.id.item_reservation_list_by_doctor_name);
        level = (TextView) itemView.findViewById(R.id.item_reservation_list_by_doctor_level);
        goodAt = (TextView) itemView.findViewById(R.id.item_reservation_list_by_doctor_good_at);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.item_reservation_list_doctor_detail_recycler);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.item_reservation_list_doctor_detail_layout);



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