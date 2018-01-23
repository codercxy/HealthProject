package com.nju.android.health.views.fragments.data;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;

import android.support.v13.app.FragmentPagerAdapter;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.service.StepDetector;
import com.nju.android.health.utils.CircleBarTiny;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
    * Activities that contain this fragment must implement the
    * {@link OldDataFragment.OnFragmentInteractionListener} interface
    * to handle interaction events.
            * Use the {@link OldDataFragment#newInstance} factory method to
    * create an instance of this fragment.
    */
    public class OldDataFragment extends Fragment{
        private int Type = 1;
        private int total_step = 0;
        /*private ImageView presImg;
        private ImageView sugarImg;*/
        private RecyclerView mRecyclerView;
        private LinearLayoutManager linearLayoutManager;
        private String[] mDataset;
        private MyAdapter myAdapter;

        private CircleBarTiny circleBar;

//    private TextView title;

        private TabLayout tabLayout;
        private ViewPager viewPager;
        private FragmentManager fm;

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                total_step = StepDetector.CURRENT_STEP;
                if (Type == 1) {
                    circleBar.setProgress(total_step, Type);
                } else if (Type == 2) {
//                calories = (int) (weight * total_step * step_length * 0.01 * 0.01);
//                circleBar.setProgress(calories, Type);
                }
            }
        };
        public OldDataFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_old, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        /*mRecyclerView = (RecyclerView) view.findViewById(R.id.data_recycler);
        mRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), onItemClickListener));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mDataset = new String[] {
                "血压", "血糖"
        };
        myAdapter = new MyAdapter(getActivity(), mDataset);
        mRecyclerView.setAdapter(myAdapter);
        *//*presImg = (ImageView) view.findViewById(R.id.data_pressure) ;
        sugarImg = (ImageView) view.findViewById(R.id.data_sugar);
        presImg.setOnClickListener(this);
        sugarImg.setOnClickListener(this);*//*
        circleBar = (CircleBarTiny) view.findViewById(R.id.data_progress_pedometer_run);
        circleBar.setMax(5000);
        circleBar.setProgress(StepDetector.CURRENT_STEP, 1);
        circleBar.startCustomAnimation();*/
        //circleBar.setOnClickListener(this);

//        title = (TextView) view.findViewById(R.id.data_head);

        fm = getChildFragmentManager();
        viewPager = (ViewPager) view.findViewById(R.id.data_viewPager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.data_tabLayout);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.chart);
//        tabLayout.getTabAt(1).setIcon(R.drawable.list);
        tabLayout.getTabAt(1).setIcon(R.drawable.heart);
        tabLayout.getTabAt(2).setIcon(R.drawable.step);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
//                        title.setText("图表");
                        break;
                    /*case 1:
                        title.setText("列表");
                        break;*/
                    case 1:
//                        title.setText("心率");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
    private void setupViewPager(ViewPager viewPager) {

        List<Fragment> fragments = new ArrayList<>();
//        fragments.add(new DataStepFragment());
        fragments.add(new DataLineFragment());
//        fragments.add(new DataListFragment());
        fragments.add(new DataRateFragment());
        fragments.add(new DataStepFragment());
        ViewPagerAdapter adapter = new ViewPagerAdapter(fm, fragments, getActivity());
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList;
        private Context context;


        public ViewPagerAdapter(FragmentManager manager, List<Fragment> fragmentList, Context context) {
            super(manager);
            this.mFragmentList = fragmentList;
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
    /*private RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            switch (position) {
                case 0:
                    Fragment presFragment = new PressureFragment();
                    ft.replace(R.id.content, presFragment);
                    break;
                case 1:
                    Fragment gluFragment = new GluFragment();
                    ft.replace(R.id.content, gluFragment);
                    break;

            }
            ft.addToBackStack(null);
            ft.commit();
        }
    };
*/
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private final int mBackground;
        private String[] mDataset;
        private final TypedValue mTypedValue = new TypedValue();

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;

            public int position;

            public ViewHolder(View v) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.data_title);
            }
        }
        public MyAdapter(Context context, String[] myDataset) {
            mDataset = myDataset;
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_data, parent, false);
            v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(mDataset[position]);
        }

        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }
    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.data_pressure:
                FragmentManager manager = getFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                Fragment presFragment = new PressureFragment();
                ft.replace(R.id.content, presFragment);
                ft.addToBackStack(null);
                ft.commit();

                break;
            case R.id.data_sugar:
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ftr = fm.beginTransaction();
                Fragment gluFragment = new GluFragment();
                ftr.replace(R.id.content, gluFragment);
                ftr.addToBackStack(null);
                ftr.commit();
                break;
            default:
                break;
        }
    }*/

}
