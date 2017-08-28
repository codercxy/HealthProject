package com.nju.android.health.views.activities.home;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.app.AlarmManager;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nju.android.health.R;
import com.nju.android.health.utils.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class ClockActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView timeText, dateText;
    private Button submit;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private int hour, minute;
    private int year, month, day;
    private String timeStr, dateStr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        initView();

        initDateTime();

    }

    private void initView() {
        timeText = (TextView) findViewById(R.id.clock_time);
        dateText = (TextView) findViewById(R.id.clock_date);
        submit = (Button) findViewById(R.id.btn_clock_submit);

        timeText.setOnClickListener(this);
        dateText.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    private void initDateTime() {
        //获取系统时间
        SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm");
        final String system = timeSdf.format(new java.util.Date());
        timeText.setText(system);

        //获取系统时间
        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy年MM月dd日");
        final String sys = dateSdf.format(new java.util.Date());
        dateText.setText(sys);


    }

    private void initAlarm(int mHour, int mMinute) {
        //闹铃注册
        Intent intent = new Intent(ClockActivity.this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(ClockActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long firstTime = SystemClock.elapsedRealtime();
        long systemTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.MINUTE, mMinute);
        calendar.set(Calendar.HOUR_OF_DAY, mHour);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long selectTime = calendar.getTimeInMillis();

        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
        long time = selectTime - systemTime;
        firstTime += time;

        //闹铃注册
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                firstTime, 24 * 60 * 60 * 1000, sender);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clock_submit:
                String timeStrings[]  = timeText.getText().toString().split(":");
                int mHour = Integer.parseInt(timeStrings[0]);
                int mMinute = Integer.parseInt(timeStrings[1]);

                Log.e("home_clock_save",timeText.getText().toString() );
                SharedPreferences sharedPref = getSharedPreferences("clockActivity", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.clock), timeText.getText().toString());
                editor.commit();


                initAlarm(mHour, mMinute);
                Toast.makeText(this, "设置闹钟成功", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.clock_time:

                View timeView = View.inflate(getApplicationContext(),R.layout.clock_timepicker, null);
                timePicker = (TimePicker) timeView.findViewById(R.id.clock_time_picker);

                Calendar c = Calendar.getInstance();

                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

                timePicker.setIs24HourView(true);
                timePicker.setCurrentHour(hour);
                timePicker.setCurrentMinute(minute);

                AlertDialog.Builder dialog = new AlertDialog.Builder(ClockActivity.this);

                dialog.setTitle("设置时间")
                        .setView(timeView)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                hour = timePicker.getCurrentHour();
                                minute = timePicker.getCurrentMinute();

                                if((hour < 10) && (minute < 10)){
                                    timeStr = "0" + Integer.toString(hour) + ":" + "0" + Integer.toString(minute);
                                }
                                else if(hour < 10){
                                    timeStr = "0" + Integer.toString(hour) + ":" + Integer.toString(minute);
                                }
                                else if(minute < 10){
                                    timeStr = Integer.toString(hour) + ":" + "0" + Integer.toString(minute);
                                }
                                else{
                                    timeStr = Integer.toString(hour) + ":" + Integer.toString(minute);
                                }
                                timeText.setText(timeStr);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                break;
            case R.id.clock_date:
                View dateView = View.inflate(getApplicationContext(),R.layout.clock_datepicker, null);
                datePicker = (DatePicker) dateView.findViewById(R.id.clock_date_picker);

                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
                datePicker.init(year, month, day, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(ClockActivity.this);

                builder.setTitle("设置日期")
                        .setView(dateView)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                year = datePicker.getYear();
                                month = datePicker.getMonth();
                                day = datePicker.getDayOfMonth();

                                if((month < 10) && (day < 10)){
                                    dateStr =  Integer.toString(year) + "年" + "0" + Integer.toString(month) + "月" + "0" + Integer.toString(day) + "日" + " ";
                                }else if(month<10){
                                    dateStr =  Integer.toString(year) + "年" + "0" + Integer.toString(month) + "月" + Integer.toString(day) + "日" + " ";
                                }else if(day<10){
                                    dateStr =  Integer.toString(year) + "年" + Integer.toString(month) + "月" + "0" + Integer.toString(day) + "日" + " ";
                                }else{
                                    dateStr =  Integer.toString(year) + "年" + Integer.toString(month) + "月" + Integer.toString(day) + "日" + " ";
                                }
                                dateText.setText(dateStr);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                break;
            default:
                break;
        }
    }


}
