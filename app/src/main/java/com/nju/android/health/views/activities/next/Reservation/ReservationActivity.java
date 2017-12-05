package com.nju.android.health.views.activities.next.Reservation;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.model.data.FirstClassItem;
import com.nju.android.health.model.data.Hospital;
import com.nju.android.health.model.data.SecondClassItem;
import com.nju.android.health.utils.DividerItemDecoration;
import com.nju.android.health.utils.RecyclerViewClickListener;
import com.nju.android.health.utils.ScreenUtils;
import com.nju.android.health.views.adapters.FirstClassAdapter;
import com.nju.android.health.views.adapters.ReservationHospitalAdapter;
import com.nju.android.health.views.adapters.SecondClassAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReservationActivity extends AppCompatActivity implements RecyclerViewClickListener, View.OnClickListener{

    private TextView location;
    private Context context;
    private RecyclerView recyclerView;
    private ReservationHospitalAdapter adapter;
    private List<Hospital> hospitalList;

    private View darkView;
    //弹出PopupWindow时，背景变暗的动画
    private Animation animIn, animOut;

    private PopupWindow popupWindow;

    /**左侧和右侧两个ListView*/
    private ListView leftLV, rightLV;
    /**左侧一级分类的数据*/
    private List<FirstClassItem> firstLocList;
    /**右侧二级分类的数据*/
    private List<SecondClassItem> secondList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        context = this;
        init();

    }

    private void init() {
        initView();
        initData();
        initRecycler();
        showPopupWindow();
    }

    private void initView() {
        location = (TextView) findViewById(R.id.reservation_hospital_loc);
        recyclerView = (RecyclerView) findViewById(R.id.reservation_hospital_recycler);
        darkView = (View) findViewById(R.id.reservation_darkview);

        animIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_anim);
        animOut = AnimationUtils.loadAnimation(this, R.anim.fade_out_anim);
        location.setOnClickListener(this);

    }

    private void initData() {
        hospitalList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Hospital hospital = new Hospital();
            hospital.setResId(R.drawable.location);
            hospital.setName("南京市医院" + i);
            hospital.setLevel("三级甲等");
            hospitalList.add(hospital);
        }

        firstLocList = new ArrayList<FirstClassItem>();
        //1
        ArrayList<SecondClassItem> secondList1 = new ArrayList<SecondClassItem>();
        secondList1.add(new SecondClassItem(101, "北京市"));

        firstLocList.add(new FirstClassItem(1, "北京", secondList1));
        //2
        ArrayList<SecondClassItem> secondList2 = new ArrayList<SecondClassItem>();
        secondList2.add(new SecondClassItem(201, "南京"));
        secondList2.add(new SecondClassItem(202, "苏州"));
        secondList2.add(new SecondClassItem(203, "徐州"));
        secondList2.add(new SecondClassItem(204, "南通"));
        secondList2.add(new SecondClassItem(205, "宿迁"));
        secondList2.add(new SecondClassItem(206, "连云港"));
        secondList2.add(new SecondClassItem(207, "无锡"));
        secondList2.add(new SecondClassItem(208, "常州"));
        secondList2.add(new SecondClassItem(209, "镇江"));
        secondList2.add(new SecondClassItem(210, "盐城"));

        firstLocList.add(new FirstClassItem(2, "江苏", secondList2));
        //3
        ArrayList<SecondClassItem> secondList3 = new ArrayList<SecondClassItem>();
        secondList3.add(new SecondClassItem(301, "浦东区"));

        firstLocList.add(new FirstClassItem(3, "上海", secondList3));
        //4
        firstLocList.add(new FirstClassItem(4, "浙江", new ArrayList<SecondClassItem>()));
        //5
        firstLocList.add(new FirstClassItem(5, "福建", null));
        firstLocList.add(new FirstClassItem(6, "广州", null));
        firstLocList.add(new FirstClassItem(7, "安徽", null));
        firstLocList.add(new FirstClassItem(8, "湖北", null));
        firstLocList.add(new FirstClassItem(9, "湖南", null));
        firstLocList.add(new FirstClassItem(10, "四川", null));
        firstLocList.add(new FirstClassItem(11, "重庆", null));
    }

    private void initRecycler() {
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        showHospital(hospitalList);
    }
    private void showHospital(List<Hospital> list) {
        adapter = new ReservationHospitalAdapter(list);
        adapter.setRecyclerViewListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reservation_hospital_loc:
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    updateList(firstLocList);
                    popupWindow.showAsDropDown(findViewById(R.id.reservation_hospital_loc));
                    popupWindow.setAnimationStyle(-1);
                    //背景变暗
                    darkView.startAnimation(animIn);
                    darkView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void showPopupWindow() {
        popupWindow = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_phy, null);
        leftLV = (ListView) view.findViewById(R.id.pop_listview_left);
        rightLV = (ListView) view.findViewById(R.id.pop_listview_right);

        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new PaintDrawable());
        popupWindow.setFocusable(true);

        popupWindow.setHeight(ScreenUtils.getScreenH(this) * 3 / 5);
        popupWindow.setWidth(ScreenUtils.getScreenW(this));

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkView.startAnimation(animOut);
                darkView.setVisibility(View.GONE);

                leftLV.setSelection(0);
                rightLV.setSelection(0);
            }
        });

        updateList(firstLocList);



    }

    private void updateList(final List<FirstClassItem> firstList) {
        //为了方便扩展，这里的两个ListView均使用BaseAdapter.如果分类名称只显示一个字符串，建议改为ArrayAdapter.
        //加载一级分类
        final FirstClassAdapter firstAdapter = new FirstClassAdapter(this, firstList);
        leftLV.setAdapter(firstAdapter);

        //加载左侧第一行对应右侧二级分类
        secondList = new ArrayList<SecondClassItem>();
        secondList.addAll(firstList.get(0).getSecondList());
        final SecondClassAdapter secondAdapter = new SecondClassAdapter(this, secondList);
        rightLV.setAdapter(secondAdapter);

        //左侧ListView点击事件
        leftLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //二级数据
                List<SecondClassItem> list2 = firstList.get(position).getSecondList();
                //如果没有二级类，则直接跳转
                if (list2 == null || list2.size() == 0) {
                    popupWindow.dismiss();

                    int firstId = firstList.get(position).getId();
                    String selectedName = firstList.get(position).getName();
                    handleResult(firstId, -1, selectedName, "tab1");

                    return;
                }

                FirstClassAdapter adapter = (FirstClassAdapter) (parent.getAdapter());
                //如果上次点击的就是这一个item，则不进行任何操作
                if (adapter.getSelectedPosition() == position){
                    return;
                }

                //根据左侧一级分类选中情况，更新背景色
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();

                //显示右侧二级分类
                updateSecondListView(list2, secondAdapter);
            }
        });

        //右侧ListView点击事件
        rightLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //关闭popupWindow，显示用户选择的分类
                popupWindow.dismiss();

                int firstPosition = firstAdapter.getSelectedPosition();
                int firstId = firstList.get(firstPosition).getId();
                int secondId = firstList.get(firstPosition).getSecondList().get(position).getId();
                String selectedName = firstList.get(firstPosition).getSecondList().get(position)
                        .getName();

                handleResult(firstId, secondId, selectedName, "tab1");

            }
        });
    }
    //处理点击结果
    private void handleResult(int firstId, int secondId, String selectedName, String tab) {
        String text = "first id:" + firstId + ",second id:" + secondId;
        location.setText(selectedName);
        updateHospitalList(selectedName);
    }
    //刷新右侧ListView
    private void updateSecondListView(List<SecondClassItem> list2,
                                      SecondClassAdapter secondAdapter) {
        secondList.clear();
        secondList.addAll(list2);
        secondAdapter.notifyDataSetChanged();
    }
    //更新列表
    private void updateHospitalList(String name) {
        showHospital(hospitalList);
    }
    @Override
    public void onClick(View v, int position) {
        Intent intent = new Intent();
        intent.setClass(context, ReservationMainActivity.class);
        intent.putExtra("hospitalName", hospitalList.get(position).getName());
        startActivity(intent);

    }
}
