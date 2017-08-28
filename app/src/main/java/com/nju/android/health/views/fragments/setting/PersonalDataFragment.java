package com.nju.android.health.views.fragments.setting;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.nju.android.health.R;
import com.nju.android.health.views.dialog.NumberPickerDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.LinkagePicker;
import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalDataFragment extends Fragment implements View.OnClickListener{

    private LinearLayout ll_sex;
    private LinearLayout ll_birth;
    private LinearLayout ll_height;
    private LinearLayout ll_weight;
    private LinearLayout ll_marriage;

    private TextView sex;
    private TextView birth;
    private TextView height;
    private TextView weight;
    private TextView marriage;


    public PersonalDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_data, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        ll_sex = (LinearLayout) view.findViewById(R.id.ll_personal_data_sex);
        ll_birth = (LinearLayout) view.findViewById(R.id.ll_personal_data_birth);
        ll_height = (LinearLayout) view.findViewById(R.id.ll_personal_data_height);
        ll_weight = (LinearLayout) view.findViewById(R.id.ll_personal_data_weight);
        ll_marriage = (LinearLayout) view.findViewById(R.id.ll_personal_data_marriage);

        sex = (TextView) view.findViewById(R.id.personal_data_sex);
        birth = (TextView) view.findViewById(R.id.personal_data_birth);
        height = (TextView) view.findViewById(R.id.personal_data_height);
        weight = (TextView) view.findViewById(R.id.personal_data_weight);
        marriage = (TextView) view.findViewById(R.id.personal_data_marriage);


        ll_sex.setOnClickListener(this);
        ll_birth.setOnClickListener(this);
        ll_height.setOnClickListener(this);
        ll_weight.setOnClickListener(this);
        ll_marriage.setOnClickListener(this);
    }

    public void onHeightPicker() {
        NumberPicker picker = new NumberPicker(getActivity());
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
                height.setText(String.valueOf(item));
            }
        });
        picker.show();

    }

    public void onWeightPicker() {
        LinkagePicker.DataProvider provider = new LinkagePicker.DataProvider() {
            @Override
            public boolean isOnlyTwo() {
                return true;
            }

            @Override
            public List<String> provideFirstData() {
                ArrayList<String> firstList = new ArrayList<>();
                for (int i=40; i<100; i++ ) {
                    firstList.add(String.valueOf(i));
                }
                return firstList;
            }

            @Override
            public List<String> provideSecondData(int firstIndex) {
                ArrayList<String> secondList = new ArrayList<>();
                for (int i=0; i<10; i++) {
                    secondList.add(String.valueOf(i));
                }
                return secondList;
            }

            @Override
            public List<String> provideThirdData(int firstIndex, int secondIndex) {
                return null;
            }
        };
        LinkagePicker picker = new LinkagePicker(getActivity(), provider);
        picker.setCycleDisable(true);
        picker.setLabel(".", "kg");
        picker.setSelectedIndex(24, 0);
        picker.setOnLinkageListener(new LinkagePicker.OnLinkageListener() {
            @Override
            public void onPicked(String first, String second, String third) {
                weight.setText(first + "." + second);
            }
        });
        picker.show();


    }

    private void onMarriagePicker() {
        OptionPicker picker = new OptionPicker(getActivity(), new String[] {
                "已婚", "未婚"
        });
        picker.setCycleDisable(true);
        picker.setLineVisible(false);
        picker.setShadowVisible(false);
        picker.setTextSize(16);
        picker.setHeight(picker.getScreenHeightPixels() / 3);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                marriage.setText(item);
            }
        });
        picker.show();
    }

    private void onSexPicker() {
        OptionPicker picker = new OptionPicker(getActivity(), new String[] {
                "男", "女"
        });
        picker.setCycleDisable(true);
        picker.setLineVisible(false);
        picker.setShadowVisible(false);
        picker.setTextSize(16);
        picker.setHeight(picker.getScreenHeightPixels() / 3);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                sex.setText(item);
            }
        });
        picker.show();
    }

    public void onYearMonthDayPicker() {
        final DatePicker picker = new DatePicker(getActivity());
        picker.setTopPadding(2);
        picker.setRangeStart(1960, 1, 1);
        picker.setRangeEnd(new Date().getYear() + 1900, 1, 1);
        picker.setSelectedItem(1990, 1, 1);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                birth.setText(year + "-" + month + "-" + day);
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_personal_data_sex:
                onSexPicker();
                break;
            case R.id.ll_personal_data_birth:
                onYearMonthDayPicker();
                break;
            case R.id.ll_personal_data_height:
                onHeightPicker();
                break;
            case R.id.ll_personal_data_weight:
                onWeightPicker();
                break;
            case R.id.ll_personal_data_marriage:
                onMarriagePicker();
                break;
        }
    }
}
