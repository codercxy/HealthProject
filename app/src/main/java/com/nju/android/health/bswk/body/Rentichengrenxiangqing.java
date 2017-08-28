package com.nju.android.health.bswk.body;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nju.android.health.R;


/**
 * Created by Administrator on 2016/3/14.
 */
public class Rentichengrenxiangqing extends Activity {

    TextView weight,fat,boneMass,muscle,visceral_fat,body_impedance,quality_index,moisture,heat,time,remark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rentichengfenxiangqing);


//        intent.putExtra("weight", dynamic_recordsData.get(position).getWeight());
//        intent.putExtra("fat", dynamic_recordsData.get(position).getFat());
//        intent.putExtra("boneMass", dynamic_recordsData.get(position).getBoneMass());
//        intent.putExtra("muscle", dynamic_recordsData.get(position).getMuscle());
//        intent.putExtra("visceral_fat", dynamic_recordsData.get(position).getVisceral_fat());
//        intent.putExtra("body_impedance", dynamic_recordsData.get(position).getBody_impedance());
//        intent.putExtra("quality_index", dynamic_recordsData.get(position).getQuality_index());
//        intent.putExtra("moisture", dynamic_recordsData.get(position).getMoisture());
//        intent.putExtra("heat", dynamic_recordsData.get(position).getHeat());


        weight= (TextView) findViewById(R.id.state_content_text_gy);
        fat= (TextView) findViewById(R.id.state_content_text_dy);
        boneMass= (TextView) findViewById(R.id.state_content_text_xl);
        muscle= (TextView) findViewById(R.id.result_data4);
        visceral_fat= (TextView) findViewById(R.id.result_data5);
        body_impedance= (TextView) findViewById(R.id.result_data6);
        quality_index= (TextView) findViewById(R.id.result_data7);
        moisture= (TextView) findViewById(R.id.result_data7);
        heat= (TextView) findViewById(R.id.result_data9);
        time= (TextView) findViewById(R.id.result_data10);
        remark= (TextView) findViewById(R.id.state_content_text_assessment__contentresult);


        Intent intent =getIntent();
        String time_data=intent.getStringExtra("dynamic_time");
        String date_data=intent.getStringExtra("dynamic_date");
        String remark_data=intent.getStringExtra("remark");

        String weight_data=intent.getStringExtra("weight");
        String fat_data=intent.getStringExtra("fat");
        String boneMass_data=intent.getStringExtra("boneMass");
        String muscle_data=intent.getStringExtra("muscle");
        String visceral_fat_data=intent.getStringExtra("visceral_fat");
        String body_impedance_data=intent.getStringExtra("body_impedance");
        String quality_index_data=intent.getStringExtra("quality_index");
        String moisture_data=intent.getStringExtra("moisture");
        String heat_data=intent.getStringExtra("heat");

        time.setText(date_data + " " + time_data);
        remark.setText(remark_data);

        weight.setText(weight_data);
        fat.setText(fat_data);
        boneMass.setText(boneMass_data);
        muscle.setText(muscle_data);
        visceral_fat.setText(visceral_fat_data);
        body_impedance.setText(body_impedance_data);
        quality_index.setText(quality_index_data);
        moisture.setText(moisture_data);
        heat.setText(heat_data);


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
