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
public class HealthRecordFragment extends Fragment implements View.OnClickListener{
    private LinearLayout ll_allergies;
    private LinearLayout ll_chronic;
    private LinearLayout ll_family;
    private LinearLayout ll_illnesses;
    private LinearLayout ll_hospital;
    private LinearLayout ll_medications;
    private LinearLayout ll_vaccinations;
    private LinearLayout ll_surgeries;

    private TextView allergies;
    private TextView chronic;
    private TextView family;
    private TextView illnesses;
    private TextView hospital;
    private TextView medications;
    private TextView vaccinations;
    private TextView surgeries;


    public HealthRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_health_record, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        ll_allergies = (LinearLayout) view.findViewById(R.id.ll_health_record_allergies);
        ll_chronic = (LinearLayout) view.findViewById(R.id.ll_health_record_chronic);
        ll_family = (LinearLayout) view.findViewById(R.id.ll_health_record_family);
        ll_illnesses = (LinearLayout) view.findViewById(R.id.ll_health_record_illnesses);
        ll_hospital = (LinearLayout) view.findViewById(R.id.ll_health_record_hospital);
        ll_medications = (LinearLayout) view.findViewById(R.id.ll_health_record_medications);
        ll_vaccinations = (LinearLayout) view.findViewById(R.id.ll_health_record_vaccinations);
        ll_surgeries = (LinearLayout) view.findViewById(R.id.ll_health_record_surgeries);


        allergies = (TextView) view.findViewById(R.id.health_record_allergies);
        chronic = (TextView) view.findViewById(R.id.health_record_chronic);
        family = (TextView) view.findViewById(R.id.health_record_family);
        illnesses = (TextView) view.findViewById(R.id.health_record_illnesses);
        hospital = (TextView) view.findViewById(R.id.health_record_hospital);
        medications = (TextView) view.findViewById(R.id.health_record_medications);
        vaccinations = (TextView) view.findViewById(R.id.health_record_vaccinations);
        surgeries = (TextView) view.findViewById(R.id.health_record_surgeries);


        ll_allergies.setOnClickListener(this);
        ll_chronic.setOnClickListener(this);
        ll_family.setOnClickListener(this);
        ll_illnesses.setOnClickListener(this);
        ll_hospital.setOnClickListener(this);
        ll_medications.setOnClickListener(this);
        ll_vaccinations.setOnClickListener(this);
        ll_surgeries.setOnClickListener(this);
    }

    private void onChoosePicker(final int id) {
        OptionPicker picker = new OptionPicker(getActivity(), new String[] {
                "是", "否"
        });
        picker.setCycleDisable(false);
        picker.setLineVisible(false);
        picker.setShadowVisible(false);
        picker.setTextSize(16);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                switch (id) {
                    /*case 0:
                        smoke.setText(item);
                        break;
                    case 1:
                        drink.setText(item);
                        break;*/
                }

            }
        });
        picker.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_health_record_allergies:

                break;
            case R.id.ll_health_record_chronic:

                break;
            case R.id.ll_health_record_family:

                break;
            case R.id.ll_health_record_illnesses:

                break;
            case R.id.ll_health_record_hospital:

                break;
            case R.id.ll_health_record_medications:

                break;
            case R.id.ll_health_record_vaccinations:

                break;
            case R.id.ll_health_record_surgeries:

                break;
        }
    }

}
