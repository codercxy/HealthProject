package com.nju.android.health.views.fragments.home;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nju.android.health.R;
import com.nju.android.health.model.data.Step;
import com.nju.android.health.providers.DbProvider;
import com.nju.android.health.service.StepDetector;
import com.nju.android.health.service.StepService;
import com.nju.android.health.utils.CircleBar;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class RunActivity extends AppCompatActivity implements View.OnClickListener{
    private int Type = 1;
    private CircleBar circleBar;
    private int total_step = 0;
    private Thread thread;
    private SimpleDateFormat sdf;
    private Step step = null;
    private DbProvider provider;
    private String today;

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



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        init();
        mThread();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveDate();
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        saveDate();
    }

    private void saveDate() {
        step = provider.loadStep(today);
        Log.e("runActivity_2", String.valueOf(StepDetector.CURRENT_STEP));
        if (step != null) {
            step.setNumber(StepDetector.CURRENT_STEP);
            provider.updateStep(step);
        } else {
            step = new Step();
            step.setNumber(StepDetector.CURRENT_STEP);
            step.setDate(today);
            provider.saveStep(step);
        }

    }

    @SuppressLint("SimpleDateFormat")
    private void init() {

        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {

        }else {
            Toast.makeText(this, "没有网络", Toast.LENGTH_LONG).show();
        }
        sdf = new SimpleDateFormat("yyyyMMdd");
        today = sdf.format(new Date());
        provider = new DbProvider();
        provider.init(this);

        step = provider.loadStep(today);
//        pedometerDB = PedometerDB.getInstance(getActivity());
//        user = pedometerDB.loadUser(MainActivity.myObjectId);
//		Toast.makeText(getActivity(), MainActivity.myObjectId+"--" ,
//				Toast.LENGTH_LONG).show();
//        if (MainActivity.myObjectId != null) {
//            step_length = user.getStep_length();
//            weight = user.getWeight();
        if (step != null) {
            StepDetector.SENSITIVITY = 10;
            StepDetector.CURRENT_STEP = step.getNumber();
        } else {
            StepDetector.SENSITIVITY = 10;
            StepDetector.CURRENT_STEP = 0;
        }

//        } else {
//            Toast.makeText(getActivity(), "this is my", Toast.LENGTH_SHORT)
//                    .show();
//        }

//        sharekey = (ImageView) view.findViewById(R.id.title_pedometer);
//        weather = new Weather();
        circleBar = (CircleBar) findViewById(R.id.progress_pedometer_run);
        circleBar.setMax(10000);
        Log.e("runActivity_1", String.valueOf(StepDetector.CURRENT_STEP));
        circleBar.setProgress(StepDetector.CURRENT_STEP, 1);
        circleBar.startCustomAnimation();
        circleBar.setOnClickListener(this);

//		ShareSDK.initSDK(getActivity());
//		sharekey.setOnClickListener(this);

    }
    private void mThread() {
        if (thread == null) {

            thread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (StepService.flag) {
                            Message msg = new Message();
                            handler.sendMessage(msg);
                        }
                    }
                }
            });
            thread.start();
        }
    }

    @Override
    public void onClick(View view) {

    }
}
