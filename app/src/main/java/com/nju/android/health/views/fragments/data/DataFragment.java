package com.nju.android.health.views.fragments.data;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.bswk.AddUricAcidActivity;
import com.nju.android.health.bswk.XuetangAdd;
import com.nju.android.health.bswk.body.RentichengfenAdd;
import com.nju.android.health.bswk.brain.NaozuzhongActivity;
import com.nju.android.health.bswk.stone.AddCholesterolActivity;
import com.nju.android.health.model.data.Disease;
import com.nju.android.health.model.data.Glucose;
import com.nju.android.health.model.data.Pressure;
import com.nju.android.health.model.data.Step;
import com.nju.android.health.providers.DbProvider;
import com.nju.android.health.utils.DividerItemDecoration;
import com.nju.android.health.utils.RecyclerViewClickListener;
import com.nju.android.health.views.activities.HomeManualActivity;
import com.nju.android.health.views.activities.next.AddXyrecorder;
import com.nju.android.health.views.adapters.DataAdapter;
import com.nju.android.health.views.fragments.home.BurnActivity;
import com.nju.android.health.views.fragments.home.RunActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataFragment extends Fragment implements RecyclerViewClickListener, View.OnClickListener, SensorEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    public static final String EMPTY = "空腹";
    public static final String FULL = "餐后2小时";

    private TextView timeText, highText, lowText, rateText;
    private TextView gluTime, gluValue;
    private TextView gluFullTime, gluFullValue;


    private LinearLayout ll_pressure, ll_glu_empty, ll_glu_full;
    private LinearLayout ll_no_data;
    private TextView stepView;
    private TextView runText, burnText;
    private LinearLayout ll_run, ll_burn;

    //step
    private int mDetector;
    private DbProvider provider;
    private String today;
    private SimpleDateFormat sdf;
    private Step step;
    private int step_length;
    private int weight;

    // TODO: Rename and change types of parameters
    private RecyclerView recyclerView;
    private List<Disease> diseaseList;

    public DataFragment() {
        // Required empty public constructor
    }

    public static DataFragment newInstance(String param1, String param2) {
        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data, container, false);

        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
        stepService(view);
        initView(view);

        initData();

        initRecycler();


        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.data_rec);

        timeText = (TextView) view.findViewById(R.id.home_time);
        highText = (TextView) view.findViewById(R.id.home_high);
        lowText = (TextView) view.findViewById(R.id.home_low);
        rateText = (TextView) view.findViewById(R.id.home_rate);

        gluTime = (TextView) view.findViewById(R.id.home_time_glu);
        gluValue = (TextView) view.findViewById(R.id.home_glu);

        gluFullTime = (TextView) view.findViewById(R.id.home_time_glu_full);
        gluFullValue = (TextView) view.findViewById(R.id.home_glu_full);

        ll_pressure = (LinearLayout) view.findViewById(R.id.ll_home_pressure);
        ll_glu_empty = (LinearLayout) view.findViewById(R.id.ll_home_glu_empty);
        ll_glu_full = (LinearLayout) view.findViewById(R.id.ll_home_glu_full);
        ll_no_data = (LinearLayout) view.findViewById(R.id.ll_home_no_data);

        runText = (TextView) view.findViewById(R.id.home_run);
        burnText = (TextView) view.findViewById(R.id.home_burn);

        ll_run = (LinearLayout) view.findViewById(R.id.ll_home_run);
        ll_burn = (LinearLayout) view.findViewById(R.id.ll_home_burn);

        provider = new DbProvider();
        provider.init(getActivity());

        ll_run.setOnClickListener(this);
        ll_burn.setOnClickListener(this);

        ll_pressure.setOnClickListener(this);
    }

    private void initData() {
        //init diseaseList
        diseaseList = new ArrayList<Disease>();

        String[] strings = new String[] {"血压", "血糖", "尿酸", "胆固醇", "身体成分", "脑卒中"};
        Integer[] integers = new Integer[] {R.drawable.blood_pressure, R.drawable.suger, R.drawable.acid,
                R.drawable.stone, R.drawable.body, R.drawable.brain};

        for (int i = 0; i < strings.length; i++) {
            Disease disease = new Disease();
            disease.setImgRes(integers[i]);
            disease.setText(strings[i]);
            diseaseList.add(disease);
        }
        //import from home
        ll_no_data.setVisibility(View.GONE);
//        cardView.setVisibility(View.VISIBLE);
        Pressure pressure = new Pressure();
        pressure = provider.getLastPressure();
        if (pressure != null) {
            ll_pressure.setVisibility(View.VISIBLE);
            timeText.setText(pressure.getTime());
            highText.setText(String.valueOf(pressure.getHigh()));
            lowText.setText(String.valueOf(pressure.getLow()));
            rateText.setText(String.valueOf(pressure.getRate()));
        } else {
            ll_pressure.setVisibility(View.GONE);
        }
        Glucose glucose_empty = new Glucose();
        Glucose glucose_full = new Glucose();
        glucose_empty = provider.getLastGlucose(EMPTY);
        glucose_full = provider.getLastGlucose(FULL);

        if (glucose_empty != null) {
            ll_glu_empty.setVisibility(View.VISIBLE);
            gluTime.setText(glucose_empty.getTime());
            gluValue.setText(String.valueOf(glucose_empty.getValue()));
        } else {
            ll_glu_empty.setVisibility(View.GONE);
        }

        if (glucose_full != null) {
            ll_glu_full.setVisibility(View.VISIBLE);
            gluFullTime.setText(glucose_full.getTime());
            gluFullValue.setText(String.valueOf(glucose_full.getValue()));
        } else {
            ll_glu_full.setVisibility(View.GONE);
        }

        if (pressure == null && glucose_empty == null && glucose_full == null) {
            ll_no_data.setVisibility(View.VISIBLE);
//            cardView.setVisibility(View.GONE);
        }

        sdf = new SimpleDateFormat("yyyyMMdd");
        today = sdf.format(new Date());
        weight = BurnActivity.weight;
        step_length = BurnActivity.step_length;

        step = provider.loadStep(today);
        if (step != null) {
            runText.setText(String.valueOf(step.getNumber()));
            burnText.setText(String.valueOf((int) (step.getNumber() * weight * step_length * 0.01 * 0.01)));
        } else {
            runText.setText("0");
            burnText.setText("0");
        }

    }

    private void initRecycler() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));

        DataAdapter adapter = new DataAdapter(diseaseList);
        adapter.setRecyclerViewListener(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
    }

    @Override
    public void onClick(View v, int position) {
        Intent intent = new Intent();
        switch (position) {
            case 0:

                intent.setClass(getActivity(), AddXyrecorder.class);
                startActivity(intent);
                break;
            case 1:
//                intent2.setClass(HomeManualActivity.this, GlucoseActivity.class);
                intent.setClass(getActivity(), XuetangAdd.class);
                startActivity(intent);
                break;

            case 2:

                intent.setClass(getActivity(), AddUricAcidActivity.class);
                startActivity(intent);
                break;
            case 3:

                intent.setClass(getActivity(), AddCholesterolActivity.class);
                startActivity(intent);
                break;
            case 4:

                intent.setClass(getActivity(), RentichengfenAdd.class);
                startActivity(intent);
                break;
            case 5:

                intent.setClass(getActivity(), NaozuzhongActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home_run:
                Intent intent_run = new Intent(getActivity(), RunActivity.class);
                startActivity(intent_run);
                break;
            case R.id.ll_home_burn:
                Intent intent_burn = new Intent(getActivity(), BurnActivity.class);
                startActivity(intent_burn);
                break;
            case R.id.ll_home_pressure:
                System.out.println("fr transaction");
                FragmentTransaction manager = getFragmentManager().beginTransaction();
                manager.replace(R.id.content, new DataListFragment());
                manager.addToBackStack(null);
                /*manager.hide(new DataFragment());
                manager.show(new DataListFragment());*/
                manager.commit();

                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Override
    public void onResume() {
        super.onResume();
//        bottomBar.setDefaultTabPosition(1);

        initData();
        step = provider.loadStep(today);
        if (step != null) {
            runText.setText(String.valueOf(step.getNumber()));
            burnText.setText(String.valueOf((int) (step.getNumber() * weight * step_length * 0.01 * 0.01)));
        } else {
            runText.setText("0");
            runText.setText("0");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        provider.database.close();
    }

    private void stepService(View view) {
        stepView = (TextView) view.findViewById(R.id.home_step);

        mDetector = 0;
        SensorManager mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        //单次计步
        Sensor mStepCount = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        //累积计步
        Sensor mStepDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        mSensorManager.registerListener(this, mStepDetector, SensorManager.SENSOR_DELAY_FASTEST);

        mSensorManager.registerListener(this, mStepCount, SensorManager.SENSOR_DELAY_FASTEST);

    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {

            //event.values[0]为计步历史累加值

            stepView.setText(event.values[0] + "步");

        }

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            if (event.values[0] == 1.0) {

                mDetector++;

                //event.values[0]一次有效计步数据

                stepView.setText(mDetector + "步");

            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
