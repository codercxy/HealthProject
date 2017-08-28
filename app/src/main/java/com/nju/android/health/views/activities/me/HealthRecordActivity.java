package com.nju.android.health.views.activities.me;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nju.android.health.R;
import com.nju.android.health.views.fragments.setting.HabitsFragment;
import com.nju.android.health.views.fragments.setting.HealthRecordFragment;
import com.nju.android.health.views.fragments.setting.PersonalDataFragment;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.picker.NumberPicker;

public class HealthRecordActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_record);

        initView();

        initData();
    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.health_record_tab);
        viewPager = (ViewPager) findViewById(R.id.health_record_viewpager);

    }

    private void initData() {
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("个人资料");
        tabLayout.getTabAt(1).setText("生活习惯");
        tabLayout.getTabAt(2).setText("健康记录");
        tabLayout.setBackgroundColor(Color.WHITE);



    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new PersonalDataFragment(), "个人资料");
        adapter.addFragment(new HabitsFragment(), "生活习惯");
        adapter.addFragment(new HealthRecordFragment(), "健康记录");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitles.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    public void onNumberPicker(View view) {
        NumberPicker picker = new NumberPicker(this);
        picker.setWidth(picker.getScreenWidthPixels());
        picker.setCycleDisable(false);
        picker.setLineVisible(false);
        picker.setOffset(2);
        picker.setRange(120, 200, 1);
        picker.setSelectedItem(172);
        picker.setLabel("cm");
        picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                Toast.makeText(HealthRecordActivity.this, String.valueOf(item), Toast.LENGTH_SHORT).show();
            }
        });
        picker.show();

    }
}
