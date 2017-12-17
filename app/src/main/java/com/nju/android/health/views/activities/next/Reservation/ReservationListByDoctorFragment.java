package com.nju.android.health.views.activities.next.Reservation;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.model.data.ReservationDoctor;
import com.nju.android.health.utils.DividerItemDecoration;
import com.nju.android.health.utils.ExpandAnimation;
import com.nju.android.health.utils.RecyclerViewClickListener;
import com.nju.android.health.views.adapters.ReservationListDoctorAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.alibaba.mobileim.YWChannel.getResources;

public class ReservationListByDoctorFragment extends Fragment implements RecyclerViewClickListener{
    private RecyclerView recyclerView;
    private ReservationListDoctorAdapter adapter;
    private List<ReservationDoctor> doctorList;
    private View animationView;
    /** 控制重复点击 */
    private boolean animationFinished = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reservation_list_by_doctor, container, false);

        initData();

        recyclerView = (RecyclerView) view.findViewById(R.id.reservation_list_by_doctor_recycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ReservationListDoctorAdapter(doctorList);
        adapter.setRecyclerViewListener(getActivity(), this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void initData() {
        List<String> stringList = new ArrayList<>();
        stringList.add("2017/12/06");
        stringList.add("2017/12/07");
        stringList.add("2017/12/08");

        doctorList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ReservationDoctor doctor = new ReservationDoctor();
            doctor.setName("Doctor" + i);
            doctor.setLevel("专家");
            doctor.setGoodAt("暂无");
            doctor.setRemain(stringList);
            doctor.setVisible(false);
            doctorList.add(doctor);
        }
    }

    @Override
    public void onClick(View v, int position) {
        if (!animationFinished)
            return;;
        animationView = v.findViewById(R.id.item_reservation_list_doctor_detail_layout);
        ExpandAnimation animation = new ExpandAnimation(animationView, 300);
        animation.setAnimationListener(new MyAnimationListener(position, v));
        animationView.startAnimation(animation);

    }
    class MyAnimationListener implements Animation.AnimationListener {

        private int position;
        private LinearLayout linearLayout;
        private RecyclerView recyclerView;
        private View view;

        public MyAnimationListener(int position, View view) {
            this.position = position;
            this.view = view;
            init();
        }

        private void init() {
//            view = recyclerView.getChildCount()
//            System.out.println("recyclerChildCount:" + recyclerView.getChildCount());
            linearLayout = (LinearLayout) view.findViewById(R.id.item_reservation_list_doctor_detail_layout);
//            recycler = (RecyclerView) view.findViewById(R.id.item_reservation_list_doctor_detail_recycler);
        }

        @Override
        public void onAnimationStart(Animation arg0) {
            animationFinished = false;
            ReservationDoctor item = doctorList.get(position);
            if (!item.isVisible()) {
//                linearLayout.setBackgroundResource(R.drawable.head);
                /*tVprojectTitle.setTextColor(getResources().getColor(R.color.white));
                tVprojectContent.setTextColor(getResources().getColor(R.color.white));*/
            }
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {

        }

        @Override
        public void onAnimationEnd(Animation arg0) {
            ReservationDoctor item = doctorList.get(position);
            item.setVisible(!item.isVisible());
            adapter.notifyDataSetChanged();
            animationFinished = true;
        }
    }
}

