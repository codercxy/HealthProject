package com.nju.android.health.views.activities.doctor;

import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.nju.android.health.R;
import com.nju.android.health.model.data.Doctor;
import com.nju.android.health.model.data.FirstClassItem;
import com.nju.android.health.model.data.Physician;
import com.nju.android.health.model.data.SecondClassItem;
import com.nju.android.health.utils.DividerItemDecoration;
import com.nju.android.health.utils.RecyclerInsetsDecoration;
import com.nju.android.health.utils.RecyclerViewClickListener;
import com.nju.android.health.utils.ScreenUtils;
import com.nju.android.health.utils.VolleyRequestImp;
import com.nju.android.health.views.adapters.FirstClassAdapter;
import com.nju.android.health.views.adapters.MyDoctorAdapter;
import com.nju.android.health.views.adapters.PhysicianAdapter;
import com.nju.android.health.views.adapters.SecondClassAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhysicianActivity extends AppCompatActivity implements RecyclerViewClickListener{

    private RecyclerView mRecycler;
    /*private MyDoctorAdapter mAdapter;
    private List<Doctor> mDoctors;*/
    private PhysicianAdapter myAdapter;
    private List<Physician> phyList;
    private String getRoomType;
    private TextView locTab, roomTab, sortTab;

    /**左侧一级分类的数据*/
    private List<FirstClassItem> firstLocList, firstRoomList, firstOrderList;
    /**右侧二级分类的数据*/
    private List<SecondClassItem> secondList;

    /**使用PopupWindow显示一级分类和二级分类*/
    private PopupWindow popupWindow;

    /**使用PopupWindow显示一级分类和二级分类*/
    private PopupWindow popupWindow_order;

    /**左侧和右侧两个ListView*/
    private ListView leftLV, rightLV;

    /**OrderListView*/
    private ListView orderLV;

    /**弹出PopupWindow时背景变暗*/
    private View darkView;

    //弹出PopupWindow时，背景变暗的动画
    private Animation animIn, animOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physician);

        initView();

        initData();

        initRecycler();

        showDoctors(phyList);

        showPopupWindow();

        showPopupOrderWindow();
    }

    private void initView() {
        mRecycler = (RecyclerView) findViewById(R.id.phy_recycler);

        locTab = (TextView) findViewById(R.id.phy_tab_loc);
        roomTab = (TextView) findViewById(R.id.phy_tab_room);
        sortTab = (TextView) findViewById(R.id.phy_tab_order);

        darkView = (View) findViewById(R.id.main_darkview);

        animIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_anim);
        animOut = AnimationUtils.loadAnimation(this, R.anim.fade_out_anim);

        OnClickListenerImpl l = new OnClickListenerImpl();
        locTab.setOnClickListener(l);
        roomTab.setOnClickListener(l);
        sortTab.setOnClickListener(l);
    }

    private void initData() {
//        mDoctors = new ArrayList<Doctor>();
        getRoomType = getIntent().getStringExtra("roomType");
        initPhyList(getRoomType);


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

        //copy
        //firstList.addAll(firstList);

        //科室

        firstRoomList = new ArrayList<FirstClassItem>();
        //1
        ArrayList<SecondClassItem> secondRoomList1 = new ArrayList<SecondClassItem>();
        secondRoomList1.add(new SecondClassItem(101, "全部儿科"));
        secondRoomList1.add(new SecondClassItem(102, "小儿科"));
        secondRoomList1.add(new SecondClassItem(103, "新生儿科"));
        firstRoomList.add(new FirstClassItem(1, "儿科", secondRoomList1));
        //2
        ArrayList<SecondClassItem> secondRoomList2 = new ArrayList<SecondClassItem>();
        secondRoomList2.add(new SecondClassItem(201, "全部内科"));
        secondRoomList2.add(new SecondClassItem(202, "呼吸内科"));
        secondRoomList2.add(new SecondClassItem(203, "心血管内科"));
        secondRoomList2.add(new SecondClassItem(204, "神经内科"));
        secondRoomList2.add(new SecondClassItem(205, "消化内科"));
        secondRoomList2.add(new SecondClassItem(206, "肾内科"));
        secondRoomList2.add(new SecondClassItem(207, "内分泌与代谢科"));
        secondRoomList2.add(new SecondClassItem(208, "风湿免疫科"));
        secondRoomList2.add(new SecondClassItem(209, "血液病科"));
        secondRoomList2.add(new SecondClassItem(210, "感染科"));

        firstRoomList.add(new FirstClassItem(2, "内科", secondRoomList2));
        //3
        ArrayList<SecondClassItem> secondRoomList3 = new ArrayList<SecondClassItem>();
        secondRoomList3.add(new SecondClassItem(301, "全部男科"));

        firstRoomList.add(new FirstClassItem(3, "男科", secondRoomList3));
        //4
        firstRoomList.add(new FirstClassItem(4, "产科", new ArrayList<SecondClassItem>()));
        //5
        firstRoomList.add(new FirstClassItem(5, "外科", null));
        firstRoomList.add(new FirstClassItem(6, "眼科", null));
        firstRoomList.add(new FirstClassItem(7, "中医科", null));
        firstRoomList.add(new FirstClassItem(8, "骨伤科", null));
        firstRoomList.add(new FirstClassItem(9, "精神心理科", null));
        firstRoomList.add(new FirstClassItem(10, "肿瘤及防治科", null));

        firstRoomList.add(new FirstClassItem(11, "妇科", null));
        firstRoomList.add(new FirstClassItem(12, "耳鼻喉科", null));
        firstRoomList.add(new FirstClassItem(13, "口腔科", null));
        firstRoomList.add(new FirstClassItem(14, "美容", null));
        firstRoomList.add(new FirstClassItem(15, "肝病", null));
        firstRoomList.add(new FirstClassItem(16, "皮肤科", null));

        //copy
        //firstRoomList.addAll(firstOrderList);


        //排序

        firstOrderList = new ArrayList<FirstClassItem>();
        //1
        ArrayList<SecondClassItem> secondOrderList1 = new ArrayList<SecondClassItem>();

        firstOrderList.add(new FirstClassItem(1, "智能排序", null));
        //2


        firstOrderList.add(new FirstClassItem(2, "评价最高", null));



        //copy
        //firstOrderList.addAll(firstOrderList);

    }

    private void initPhyList(String room) {

        //volley
        Map<String, String> param = new HashMap<>();
        param.put("url", "doctor");
        param.put("action", "search");

        VolleyRequestImp volley = new VolleyRequestImp(param);
        String response = volley.myVolleyRequestSearch_POST(this);

        System.out.println("doctor response:" + response);

        phyList = new ArrayList<>();
        Physician phy = new Physician();
        switch (room) {
            case "内科":
                phy.setImageRes(R.drawable.phy);
                phy.setName("舒慧君");
                phy.setRoom("内科");
                phy.setLevel("副主任医师");
                phy.setLoc("北京协和医院");
                phy.setGoodAt("擅长：慢性胃炎、自身免疫性肝炎");
                phyList.add(phy);
                roomTab.setText(room);
                break;
            case "儿科":
                phy.setImageRes(R.drawable.phy);
                phy.setName("张晓蕊");
                phy.setRoom("儿科");
                phy.setLevel("副主任医师");
                phy.setLoc("北京大学人民医院");
                phy.setGoodAt("擅长：新生儿疾病、发育、幼儿急诊");
                phyList.add(phy);
                roomTab.setText(room);
                break;
            case "男科":
                phy.setImageRes(R.drawable.phy);
                phy.setName("胡建军");
                phy.setRoom("外科");
                phy.setLevel("主治医师");
                phy.setLoc("中国人民解放军总医院");
                phy.setGoodAt("擅长：乙肝、肝癌、胆结石");
                phyList.add(phy);
                roomTab.setText(room);
                break;
            default:
                break;
        }


    }


    private void initRecycler() {
        mRecycler.addItemDecoration(new DividerItemDecoration(PhysicianActivity.this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void showDoctors(List<Physician> doctors) {
        myAdapter = new PhysicianAdapter(doctors);
        myAdapter.setOnRecyclerListListener(this);
        mRecycler.setAdapter(myAdapter);
    }

    //点击事件
    class OnClickListenerImpl implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.phy_tab_loc:

                    tab1OnClick();
                    break;
                case R.id.phy_tab_room:

                    tab2OnClick();
                    break;
                case R.id.phy_tab_order:

                    tab3OnClick();
                    break;
                default:
                    break;
            }
        }
    }
    //顶部第一个标签的点击事件
    private void tab1OnClick() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            updateList(firstLocList, "tab1");
            popupWindow.showAsDropDown(findViewById(R.id.phy_tab_loc));
            popupWindow.setAnimationStyle(-1);
            //背景变暗
            darkView.startAnimation(animIn);
            darkView.setVisibility(View.VISIBLE);
        }

    }
    //顶部第2个标签的点击事件
    private void tab2OnClick() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            updateList(firstRoomList, "tab2");
            popupWindow.showAsDropDown(findViewById(R.id.phy_tab_room));
            popupWindow.setAnimationStyle(-1);
            //背景变暗
            darkView.startAnimation(animIn);
            darkView.setVisibility(View.VISIBLE);
        }

    }
    //顶部第3个标签的点击事件
    private void tab3OnClick() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else if (popupWindow_order.isShowing()) {
            popupWindow_order.dismiss();
        } else {
            popupWindow_order.showAsDropDown(findViewById(R.id.phy_tab_order));
            popupWindow_order.setAnimationStyle(-1);
            //背景变暗
            darkView.startAnimation(animIn);
            darkView.setVisibility(View.VISIBLE);
        }
    }

    //刷新右侧ListView
    private void updateSecondListView(List<SecondClassItem> list2,
                                      SecondClassAdapter secondAdapter) {
        secondList.clear();
        secondList.addAll(list2);
        secondAdapter.notifyDataSetChanged();
    }

    //处理点击结果
    private void handleResult(int firstId, int secondId, String selectedName, String tab){
        String text = "first id:" + firstId + ",second id:" + secondId;
        Toast.makeText(PhysicianActivity.this, text, Toast.LENGTH_SHORT).show();

        switch (tab) {
            case "tab1":
                locTab.setText(selectedName);
                updatePhyList("tab1", selectedName);
                break;
            case "tab2":
                roomTab.setText(selectedName);

                break;
            case "tab3":
                sortTab.setText(selectedName);
                break;
        }

    }
    private void updatePhyList(String tab, String name) {
        switch (tab) {
            case "tab1":
                System.out.println("NAME:" + name);
                if (!name.equals("北京市")) {
                    System.out.println("HERE" + name);
                    phyList = new ArrayList<>();
                    showDoctors(phyList);
                } else if (phyList.isEmpty()){
                    Physician phy = new Physician();
                    phy.setImageRes(R.drawable.phy);
                    phy.setName("舒慧君");
                    phy.setRoom("内科");
                    phy.setLevel("副主任医师");
                    phy.setLoc("北京协和医院");
                    phy.setGoodAt("擅长：慢性胃炎、自身免疫性肝炎");
                    phyList.add(phy);
                    showDoctors(phyList);
                }
                break;
            case "tab2":

                break;
            case "tab3":

                break;
        }
    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.phy_tab_loc:


                break;
            case R.id.phy_tab_room:
                break;
            case R.id.phy_tab_order:
                break;
        }
    }*/

    @Override
    public void onClick(View v, int position) {

        /*Intent intent = new Intent(PhysicianActivity.this, PhyDetailActivity.class);
        intent.putExtra("position", position);

        startActivity(intent);*/
        Intent intent = new Intent(PhysicianActivity.this, DoctorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("headImg", phyList.get(position).getImageRes());
        bundle.putString("name", phyList.get(position).getName());
        bundle.putString("room", phyList.get(position).getRoom());
        bundle.putString("level", phyList.get(position).getLevel());
        bundle.putString("loc", phyList.get(position).getLoc());
        bundle.putString("goodAt", phyList.get(position).getGoodAt());

        intent.putExtra("doctor", bundle);
        startActivity(intent);


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

        updateList(firstLocList, "tab1");



    }

    private void updateList(final List<FirstClassItem> firstList, final String tab) {
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
                    switch (tab) {
                        case "tab1":
                            handleResult(firstId, -1, selectedName, "tab1");
                            break;
                        case "tab2":
                            handleResult(firstId, -1, selectedName, "tab2");
                            break;
                    }

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

                switch (tab) {
                    case "tab1":
                        handleResult(firstId, secondId, selectedName, "tab1");
                        break;
                    case "tab2":
                        handleResult(firstId, secondId, selectedName, "tab2");
                        break;
                }

            }
        });
    }

    private void showPopupOrderWindow() {
        popupWindow_order = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_phy_order, null);
        orderLV = (ListView) view.findViewById(R.id.pop_listview_order);

        popupWindow_order.setContentView(view);
        popupWindow_order.setBackgroundDrawable(new PaintDrawable());
        popupWindow_order.setFocusable(true);

        popupWindow_order.setHeight(ScreenUtils.getScreenH(this) * 1 / 7);
        popupWindow_order.setWidth(ScreenUtils.getScreenW(this));

        popupWindow_order.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkView.startAnimation(animOut);
                darkView.setVisibility(View.GONE);

                orderLV.setSelection(0);
            }
        });


        //为了方便扩展，这里的两个ListView均使用BaseAdapter.如果分类名称只显示一个字符串，建议改为ArrayAdapter.
        //加载一级分类
        final FirstClassAdapter firstAdapter = new FirstClassAdapter(this, firstOrderList);
        orderLV.setAdapter(firstAdapter);


        //左侧ListView点击事件
        orderLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //二级数据
                List<SecondClassItem> list2 = firstOrderList.get(position).getSecondList();
                //如果没有二级类，则直接跳转
                if (list2 == null || list2.size() == 0) {
                    popupWindow_order.dismiss();

                    int firstId = firstOrderList.get(position).getId();
                    String selectedName = firstOrderList.get(position).getName();
                    handleResult(firstId, -1, selectedName, "tab3");
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

            }
        });


    }
}
