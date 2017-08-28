package com.nju.android.health.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.model.data.Doctor;
import com.nju.android.health.utils.RecyclerViewClickListener;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class MyDoctorAdapter extends RecyclerView.Adapter<MyDoctorViewHolder>{

    private List<Doctor> doctorList;

    private RecyclerViewClickListener listener;

    public MyDoctorAdapter(List<Doctor> doctors) {
        this.doctorList = doctors;
    }

    public void setRecyclerListListener(RecyclerViewClickListener listListener) {
        this.listener = listListener;
    }



    @Override
    public MyDoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor_my, parent, false);
        //view.setOnClickListener(this);
        return new MyDoctorViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(MyDoctorViewHolder holder, int position) {
        holder.imageView.setImageResource(doctorList.get(position).getPicRes());
        holder.name.setText(doctorList.get(position).getName());
        holder.office.setText(doctorList.get(position).getOffice());
        holder.loc.setText(doctorList.get(position).getLoc());
        holder.num.setText(String.valueOf(doctorList.get(position).getNum()));
        holder.good.setText(String.valueOf(doctorList.get(position).getGood()));

    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

}

class MyDoctorViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

    private final RecyclerViewClickListener mClickListener;
    LinearLayout my_ll;
    ImageView imageView;
    TextView name;
    TextView office;
    TextView loc;
    TextView num;
    TextView good;

    public MyDoctorViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);
        my_ll = (LinearLayout) itemView.findViewById(R.id.ll_doctor_my_item);
        imageView = (ImageView) itemView.findViewById(R.id.doctor_my_pic);
        name = (TextView) itemView.findViewById(R.id.doctor_my_name);
        office = (TextView) itemView.findViewById(R.id.doctor_my_office);
        loc = (TextView) itemView.findViewById(R.id.doctor_my_loc);
        num = (TextView) itemView.findViewById(R.id.doctor_my_num);
        good = (TextView) itemView.findViewById(R.id.doctor_my_good);

        my_ll.setOnTouchListener(this);
        this.mClickListener = listener;

    }
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_MOVE) {

            mClickListener.onClick(view, getPosition());
        }
        return true;
    }
}