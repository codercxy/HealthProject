package com.nju.android.health.views.activities.glucose;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nju.android.health.MyApplication;
import com.nju.android.health.R;
import com.nju.android.health.providers.DbGlucose;
import com.nju.android.health.providers.DbPressure;
import com.nju.android.health.providers.DbProvider;
import com.nju.android.health.views.activities.HomeManualActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GlucoseActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_glu;
    private TextView time, type;
    private Button submit;
    private DatePicker datePicker;
    private TimePicker timePicker;

    private TextView empty, full;

    protected static int arrive_year;
    protected static int arrive_month;
    protected static int arrive_day;
    protected static int arrive_hour;
    protected static int arrive_min;
    private String dateStr,timeStr,datetime,system;

    String[] strings = new String[]{"空腹", "餐后2小时"};

    private Float getValue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glucose);

        initView();

        initData();
    }

    private void initView() {
        et_glu = (EditText) findViewById(R.id.et_glu_value);
        et_glu.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        time = (TextView) findViewById(R.id.glu_time);
        type = (TextView) findViewById(R.id.glu_type);
        submit = (Button) findViewById(R.id.btn_glu_submit);

        time.setOnClickListener(this);
        type.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    private void initData() {
        //获取系统时间
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        final String system = sDateFormat.format(new java.util.Date());
        time.setText(system);

        type.setText("空腹");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.glu_time:
                int year,month,day,hour,minute;

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
                if(time.getText().toString() != null){
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

                if(time.getText().toString() != null){
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
                AlertDialog.Builder builder = new AlertDialog.Builder(GlucoseActivity.this);
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
                        time.setText(dateStr);

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
                        time.setText(datetime);
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
                break;
            case R.id.glu_type:
                View typeView = View.inflate(getApplicationContext(), R.layout.glupicker, null);


                AlertDialog.Builder builder_glu = new AlertDialog.Builder(GlucoseActivity.this);


                builder_glu.setView(typeView)
                        .setTitle("是否空腹")
                        .setItems(strings, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                type.setText(strings[which]);
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.btn_glu_submit:





                if (!et_glu.getText().toString().equals("")) {
                    //判断输入为数值
                    if (isNumeric(et_glu.getText().toString())) {
                        getValue = Float.parseFloat(et_glu.getText().toString());
                    } else {
                        Toast.makeText(GlucoseActivity.this, "请输入数字", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    //判断输入为数值是否正确
                    if (getValue < 0) {
                        Toast.makeText(GlucoseActivity.this, "请输入正确的血糖值", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (getValue > 30) {
                        Toast.makeText(GlucoseActivity.this, "请输入正确的血糖值", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    saveGluValue();

                    Intent intent = new Intent(GlucoseActivity.this, AnlsGluActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("gluTime",time.getText().toString());
                    bundle.putString("gluType", type.getText().toString());
                    bundle.putString("value", et_glu.getText().toString());
                    intent.putExtra("bundle",bundle);
                    startActivity(intent);
                }
                break;
        }
    }

    private void saveGluValue() {
        DbProvider provider = new DbProvider();
        provider.init(this);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbGlucose.Glucose.USER_ID, Integer.parseInt(MyApplication.getInstance().getUser_id()));
        contentValues.put(DbGlucose.Glucose.TIME, time.getText().toString());
        contentValues.put(DbGlucose.Glucose.TYPE, type.getText().toString());
        contentValues.put(DbGlucose.Glucose.VALUE, Integer.parseInt(et_glu.getText().toString()));

        provider.insert(DbGlucose.CONTENT_URI, contentValues);
    }
    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

}
