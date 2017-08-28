package com.nju.android.health.views.fragments.doctor;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.views.activities.doctor.MyDoctorActivity;
import com.nju.android.health.views.activities.doctor.PhysicianActivity;
import com.nju.android.health.views.activities.doctor.SpecialistActivity;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorFragment extends Fragment implements View.OnClickListener{

    private TextView mDoctor;
    private TextView physician;
    private TextView specialist;

    public DoctorFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_doctor, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        mDoctor = (TextView) view.findViewById(R.id.doctor_my);
        physician = (TextView) view.findViewById(R.id.doctor_physician);
        specialist = (TextView) view.findViewById(R.id.doctor_specialist);

        mDoctor.setOnClickListener(this);
        physician.setOnClickListener(this);
        specialist.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.doctor_my:

                Intent intent_my = new Intent(getActivity(), MyDoctorActivity.class);
                startActivity(intent_my);
                break;
            case R.id.doctor_physician:
                Intent intent_phy = new Intent(getActivity(), PhysicianActivity.class);
                startActivity(intent_phy);
                break;
            case R.id.doctor_specialist:
                Intent intent_spe = new Intent(getActivity(), SpecialistActivity.class);
                startActivity(intent_spe);
                break;
        }
    }
}
