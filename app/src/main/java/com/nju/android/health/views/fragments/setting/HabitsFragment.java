package com.nju.android.health.views.fragments.setting;


import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nju.android.health.R;

import cn.qqtheme.framework.picker.OptionPicker;

/**
 * A simple {@link Fragment} subclass.
 */
public class HabitsFragment extends Fragment implements View.OnClickListener{

    private LinearLayout ll_smoke;
    private LinearLayout ll_drink;

    private TextView smoke;
    private TextView drink;


    public HabitsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_habits, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        ll_smoke = (LinearLayout) view.findViewById(R.id.ll_habits_smoke);
        ll_drink = (LinearLayout) view.findViewById(R.id.ll_habits_drink);

        smoke = (TextView) view.findViewById(R.id.habits_smoke);
        drink = (TextView) view.findViewById(R.id.habits_drink);


        ll_smoke.setOnClickListener(this);
        ll_drink.setOnClickListener(this);
    }

    private void onChoosePicker(final int id) {
        OptionPicker picker = new OptionPicker(getActivity(), new String[] {
                "是", "否"
        });
        picker.setCycleDisable(true);
        picker.setLineVisible(false);
        picker.setShadowVisible(false);
        picker.setTextSize(16);
        picker.setHeight(picker.getScreenHeightPixels() / 3);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                switch (id) {
                    case 0:
                        smoke.setText(item);
                        break;
                    case 1:
                        drink.setText(item);
                        break;
                }

            }
        });
        picker.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_habits_smoke:
                onChoosePicker(0);
                break;
            case R.id.ll_habits_drink:
                onChoosePicker(1);
                break;
        }
    }
}
