package com.nju.android.health.views.activities.pressure;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nju.android.health.R;
import com.nju.android.health.views.activities.HomeManualActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TwiceMoniSeActivity extends AppCompatActivity {

    protected static int arrive_year;
    protected static int arrive_month;
    protected static int arrive_day;
    protected static int arrive_hour;
    protected static int arrive_min;
    private TextView pretime;
    private EditText prehigh,prelow,heartrate;
    private Button savebtn;
    private DatePicker datePicker;
    private TimePicker timePicker;
    int year,month,day,hour,minute;
    private String dateStr,timeStr,datetime,system;
    static int i=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_pres);

        pretime = (TextView)findViewById(R.id.manual_pres_time);
        prehigh = (EditText)findViewById(R.id.et_manual_pres_high);
        prehigh.setInputType(EditorInfo.TYPE_CLASS_PHONE);


        prelow = (EditText)findViewById(R.id.et_manual_pres_low);
        prelow.setInputType(EditorInfo.TYPE_CLASS_PHONE);


        heartrate = (EditText)findViewById(R.id.et_manual_pres_rate);
        heartrate.setInputType(EditorInfo.TYPE_CLASS_PHONE);


        savebtn = (Button)findViewById(R.id.btn_manual_pres_submit);
        //获取系统时间
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        final String system = sDateFormat.format(new Date(System.currentTimeMillis()));
        Log.e("current_time", system);
        pretime.setText(system);

        //获取测量时的时间
        pretime.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                View view = View.inflate(getApplicationContext(), R.layout.dtpicker_manual, null);
                datePicker = (DatePicker)view.findViewById(R.id.manual_pres_date_picker);
                timePicker = (TimePicker)view.findViewById(R.id.manual_pres_time_picker);

                //change DatePicker layout
                LinearLayout dpContainer = (LinearLayout)datePicker.getChildAt(0);
                LinearLayout dpSpinner = (LinearLayout)dpContainer.getChildAt(0);
                for(int i=0;i<dpSpinner.getChildCount();i++){
                    NumberPicker numPicker = (NumberPicker)dpSpinner.getChildAt(i);
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(120, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params1.leftMargin = 0;
                    params1.rightMargin = 25;
                    numPicker.setLayoutParams(params1);
                }

                //change TimePicker layout
                LinearLayout tpContainer = (LinearLayout)timePicker.getChildAt(0);
                LinearLayout tpSpinner = (LinearLayout)tpContainer.getChildAt(0);
                for(int i=0;i<tpSpinner.getChildCount();i++){
                    View child = tpSpinner.getChildAt(i);
                    if(!(child instanceof NumberPicker))
                        continue;
                    NumberPicker numPicker = (NumberPicker)tpSpinner.getChildAt(i);
                    LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params3.leftMargin = 0;
                    params3.rightMargin = 25;
                    numPicker.setLayoutParams(params3);
                }
                if(pretime.getText().toString() != null){
                    final Calendar c = Calendar.getInstance();
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);
                }else{
                    year = HomeManualActivity.arrive_year;
                    month = HomeManualActivity.arrive_month;
                    day = HomeManualActivity.arrive_day;
                }
                datePicker.init(year, month, day, null);

                if(pretime.getText().toString() != null){
                    final Calendar c = Calendar.getInstance();
                    hour = c.get(Calendar.HOUR_OF_DAY);
                    minute = c.get(Calendar.MINUTE);
                }else{
                    hour = HomeManualActivity.arrive_hour;
                    minute = HomeManualActivity.arrive_min;
                }
                timePicker.setIs24HourView(true);
                timePicker.setCurrentHour(hour);
                timePicker.setCurrentMinute(minute);

                //build DateTimeDialog 时间日期圣诞框
                AlertDialog.Builder builder = new AlertDialog.Builder(TwiceMoniSeActivity.this);
                builder.setView(view);
                builder.setTitle("请选择时间");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        arrive_year = datePicker.getYear();
                        arrive_month = datePicker.getMonth();
                        arrive_month++;
                        arrive_day = datePicker.getDayOfMonth();

                        if((arrive_month<10)&&(arrive_day<10)){
                            dateStr =  Integer.toString(arrive_year)+"年"+"0"+Integer.toString(arrive_month)+"月"+"0"+Integer.toString(arrive_day)+"日"+" ";
                        }else if(arrive_month<10){
                            dateStr =  Integer.toString(arrive_year)+"年"+"0"+Integer.toString(arrive_month)+"月"+Integer.toString(arrive_day)+"日"+" ";
                        }else if(arrive_day<10){
                            dateStr =  Integer.toString(arrive_year)+"年"+Integer.toString(arrive_month)+"月"+"0"+Integer.toString(arrive_day)+"日"+" ";
                        }else{
                            dateStr =  Integer.toString(arrive_year)+"年"+Integer.toString(arrive_month)+"月"+Integer.toString(arrive_day)+"日"+" ";
                        }
                        pretime.setText(dateStr);

                        arrive_hour = timePicker.getCurrentHour();
                        arrive_min = timePicker.getCurrentMinute();
                        if((arrive_hour<10)&&(arrive_min<10)){
                            timeStr = "0"+Integer.toString(arrive_hour)+":"+"0"+Integer.toString(arrive_min);
                        }
                        else if(arrive_hour<10){
                            timeStr = "0"+Integer.toString(arrive_hour)+":"+Integer.toString(arrive_min);
                        }
                        else if(arrive_min<10){
                            timeStr = Integer.toString(arrive_hour)+":"+"0"+Integer.toString(arrive_min);
                        }
                        else{
                            timeStr = Integer.toString(arrive_hour)+":"+Integer.toString(arrive_min);
                        }
                        datetime = dateStr+timeStr;
                        pretime.setText(datetime);
                    }

                    private String formate(int arrive_hour, int arrive_min) {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    private String formatDate(int arrive_year, int arrive_month, int arrive_day) {
                        // TODO Auto-generated method stub
                        return null;
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
            }

        });

        savebtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TwiceMoniSeActivity.this,AnlsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("pretime",pretime.getText().toString());
                bundle.putString("highpre", prehigh.getText().toString());
                bundle.putString("lowpre", prelow.getText().toString());
                bundle.putString("rate", heartrate.getText().toString());
                bundle.putInt("times", i--);
                intent.putExtra("bundle",bundle);
                startActivity(intent) ;
            }

        });
    }
}
