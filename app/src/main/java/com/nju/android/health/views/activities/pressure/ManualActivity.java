package com.nju.android.health.views.activities.pressure;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.nju.android.health.providers.DbPressure;
import com.nju.android.health.providers.DbProvider;
import com.nju.android.health.views.activities.HomeManualActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ManualActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_high, et_low, et_rate;
    private TextView time;
    private Button submit;
    private DatePicker datePicker;
    private TimePicker timePicker;
    protected static int arrive_year;
    protected static int arrive_month;
    protected static int arrive_day;
    protected static int arrive_hour;
    protected static int arrive_min;
    private String dateStr,timeStr,datetime;
    private Float getHigh, getLow, getRate;
    private Button voiceBtn;
    private Toast mToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_pres);

        initView();

        initTime();
    }
    private void initView() {
        et_high = (EditText) findViewById(R.id.et_manual_pres_high);
        //锁定数字键盘
        et_high.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        et_low = (EditText) findViewById(R.id.et_manual_pres_low);
        et_low.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        et_rate = (EditText) findViewById(R.id.et_manual_pres_rate);
        et_rate.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        time = (TextView) findViewById(R.id.manual_pres_time);
        submit = (Button) findViewById(R.id.btn_manual_pres_submit);

        voiceBtn = (Button) findViewById(R.id.pres_voice);
        mToast = Toast.makeText(ManualActivity.this, "", Toast.LENGTH_SHORT);

        time.setOnClickListener(this);
        submit.setOnClickListener(this);
        voiceBtn.setOnClickListener(this);
    }
    private void initTime() {
        //获取系统时间
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        final String system = sDateFormat.format(new java.util.Date());
        time.setText(system);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pres_voice:
                /*RecognizerListener mRecognizerListener = new RecognizerListener() {
                    @Override
                    public void onVolumeChanged(int i, byte[] bytes) {
                        showTip("当前正在说话，音量大小："  + i);
                    }

                    @Override
                    public void onBeginOfSpeech() {
                        showTip("getStart");
                    }

                    @Override
                    public void onEndOfSpeech() {
                        showTip("finished!");
                    }

                    @Override
                    public void onResult(com.iflytek.cloud.RecognizerResult recognizerResult, boolean b) {

                        showTip(recognizerResult.getResultString());
                    }

                    @Override
                    public void onError(SpeechError speechError) {
                        showTip("error" + speechError.toString());
                        speechError.getPlainDescription(true);

                    }

                    @Override
                    public void onEvent(int i, int i1, int i2, Bundle bundle) {

                    }
                };
                SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(ManualActivity.this, mInitListener);
                mIat.setParameter(SpeechConstant.PARAMS, null);
                // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
                mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

                // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
                mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

                // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
                mIat.setParameter(SpeechConstant.ASR_PTT, "1");
                mIat.setParameter(SpeechConstant.DOMAIN, "iat");
                mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
                mIat.setParameter(SpeechConstant.ACCENT, "mandarin");


                mIat.startListening(mRecognizerListener);*/

                break;
            case R.id.btn_manual_pres_submit:



                if (!et_high.getText().toString().equals("") && !et_low.getText().toString().equals("") && !et_rate.getText().toString().equals("")) {
                    //判断输入为数值
                    if (isNumeric(et_high.getText().toString()) &&
                            isNumeric(et_low.getText().toString()) &&
                            isNumeric(et_rate.getText().toString())) {
                        getHigh = Float.parseFloat(et_high.getText().toString());
                        getLow = Float.parseFloat(et_low.getText().toString());
                        getRate = Float.parseFloat(et_rate.getText().toString());
                    } else {
                        Toast.makeText(ManualActivity.this, "请输入数字", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    //判断输入为数值是否正确
                    if (getHigh < 0 || getLow < 0 || getRate < 0) {
                        Toast.makeText(ManualActivity.this, "请输入正确的数值", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (getHigh > 220 || getLow > 130 || getRate > 220) {
                        Toast.makeText(ManualActivity.this, "请输入正确的数值", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (getHigh < getLow) {
                        Toast.makeText(ManualActivity.this, "请检测是否输入正确", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    savePressureValue();

                    Intent intent_mb = new Intent(ManualActivity.this, AnlsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("pretime",time.getText().toString());
                    bundle.putString("highpre", et_high.getText().toString());
                    bundle.putString("lowpre", et_low.getText().toString());
                    bundle.putString("rate", et_rate.getText().toString());
                    intent_mb.putExtra("bundle",bundle);
                    startActivity(intent_mb);
                }

                break;
            case R.id.manual_pres_time:
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ManualActivity.this);
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
            default:
                break;
        }
    }
    /**
     * 初始化监听器。
     */
    /*private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
//            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };*/
    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }
    private void savePressureValue() {
        DbProvider provider = new DbProvider();
        provider.init(this);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbPressure.Pressure.USER_ID, Integer.parseInt(MyApplication.getInstance().getUser_id()));
        contentValues.put(DbPressure.Pressure.TIME, time.getText().toString());
        contentValues.put(DbPressure.Pressure.HIGH, Integer.parseInt(et_high.getText().toString()));
        contentValues.put(DbPressure.Pressure.LOW, Integer.parseInt(et_low.getText().toString()));
        contentValues.put(DbPressure.Pressure.RATE, Integer.parseInt(et_rate.getText().toString()));

        provider.insert(DbPressure.CONTENT_URI, contentValues);
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
