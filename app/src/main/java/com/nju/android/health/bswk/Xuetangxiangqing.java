package com.nju.android.health.bswk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nju.android.health.R;


/**
 * Created by Administrator on 2016/3/11.
 */
public class Xuetangxiangqing extends Activity {
    TextView numerical_value,time_interval,time,remark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xuetangxiangqing);

        numerical_value= (TextView) findViewById(R.id.state_content_text_dy);
        time= (TextView) findViewById(R.id.state_content_text_xl);
        remark= (TextView) findViewById(R.id.state_content_text_assessment__contentresult);
        time_interval= (TextView) findViewById(R.id.state_content_text_gy);

        Intent intent =getIntent();
        String time_data=intent.getStringExtra("dynamic_time");
        String date_data=intent.getStringExtra("dynamic_date");
        String remark_data=intent.getStringExtra("remark");
        String time_interval_data=intent.getStringExtra("time_interval");
        String numerical_value_data=intent.getStringExtra("numerical_value");

        time.setText(date_data + " " + time_data);
        remark.setText(remark_data);
        numerical_value.setText(numerical_value_data);
        time_interval.setText(time_interval_data);

        back();
    }

    public void back() {
        RelativeLayout back = (RelativeLayout) findViewById(R.id.back_relative);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
