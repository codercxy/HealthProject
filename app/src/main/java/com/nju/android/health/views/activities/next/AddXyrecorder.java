package com.nju.android.health.views.activities.next;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nju.android.health.MyApplication;
import com.nju.android.health.R;
import com.nju.android.health.bluetooth.BleActivity;
import com.nju.android.health.bswk.BluetoothLEService;
import com.nju.android.health.bswk.FileService;
import com.nju.android.health.bswk.HttpInfo;
import com.nju.android.health.providers.DbPressure;
import com.nju.android.health.providers.DbProvider;
import com.nju.android.health.utils.SaveValueToDB;
import com.nju.android.health.utils.VolleyRequestImp;
import com.nju.android.health.views.activities.MainActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.alibaba.sdk.android.rpc.RpcServiceClient.JSON;


/**
 * Created by Administrator on 2016/1/14 0014.
 *
 */
public class AddXyrecorder extends Activity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 123;
    private TextView showDate = null;
    private ImageView pickDate = null;
    private TextView showTime = null;
    private ImageView pickTime = null;
    private EditText xyrecorder_gy;
    private EditText xyrecorder_dy;
    private EditText xyrecorder_xl;
    private EditText remark;
    private Button xyrecorder_tijiao;
    private TextView liebiao;           //列表按钮

    private String uri = null;
    private static final int SHOW_DATAPICK = 0;
    private static final int DATE_DIALOG_ID = 1;
    private static final int SHOW_TIMEPICK = 2;
    private static final int TIME_DIALOG_ID = 3;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private FileService fileService;
    private Map<String, String> map = new HashMap<>();
    private String mem_account, mem_token;

    private Button startMeasuring;
    private int num = 0;        //用于计量处于哪个流程
    private boolean ConnectBlue = false;
    private static BluetoothLEService mBluetoothLEService = null;
    public static String bluetoothCommand;          //根据连接的蓝牙不同，保存不容的蓝牙测量命令
    private ServiceConnection mBluetoothLEConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder service) {
            mBluetoothLEService = ((BluetoothLEService.LocalBinder) service).getService();
            if (mBluetoothLEService == null) {
                finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBluetoothLEService = null;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addxyrecorder);
        initializeViews();

        fileService = new FileService(this);
        try {
            Map<String, String> map = fileService.getUserInfo("bswk.txt");
            mem_account = map.get("mem_account");
            mem_token = map.get("mem_token");
        } catch (Exception e) {
            e.printStackTrace();
        }


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());
        fileService = new FileService(this);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        Intent bindintent = new Intent(AddXyrecorder.this, BluetoothLEService.class);
        bindService(bindintent, mBluetoothLEConnection, BIND_AUTO_CREATE);

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLEService.ACTION_BLUETOOTH_CONNECTED);
        intentFilter.addAction(BluetoothLEService.ACTION_BLUETOOTH_DISCONNECTED);
        intentFilter.addAction(BluetoothLEService.ACTION_BLUETOOTH_DEVICE_FOUND);
        intentFilter.addAction(BluetoothLEService.ACTION_BLUETOOTH_DATAARRIVED);

        registerReceiver(onReceiver, intentFilter);

        setDateTime();
        setTimeOfDay();
        setview();
        back();
        measurement();          //测量按钮监听
    }

    //测量血压
    private void measurement() {
        startMeasuring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有权限
                /*if (ContextCompat.checkSelfPermission(AddXyrecorder.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //请求权限
                    ActivityCompat.requestPermissions(AddXyrecorder.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                    //判断是否需要 向用户解释，为什么要申请该权限
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddXyrecorder.this,
                            Manifest.permission.READ_CONTACTS)) {
                        Toast.makeText(AddXyrecorder.this, "should Show Request Permission Rationale", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!ConnectBlue) {
                        if (mBluetoothLEService == null) {
                            return;
                        }
                        //连接蓝牙
                        if (mBluetoothLEService.IsOpened(AddXyrecorder.this)) {
                            mBluetoothLEService.Scan(true);
                        }
                    } else {
                        //发送测量指令开始测量
                        mBluetoothLEService.writePackage(hexStringToBytes(bluetoothCommand));
                    }
                    xyrecorder_gy.setText("");
                    xyrecorder_dy.setText("");
                    xyrecorder_xl.setText("");
                }*/
                Intent intent = new Intent(AddXyrecorder.this, BleActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothLEService != null) {
            unbindService(mBluetoothLEConnection);
            mBluetoothLEService.Disconnect();
            mBluetoothLEService = null;
        }
        if (onReceiver != null) {
            unregisterReceiver(onReceiver);
        }
    }


    public void setview() {
        xyrecorder_tijiao = (Button) findViewById(R.id.addxyrecorder_tijiao);
        xyrecorder_gy = (EditText) findViewById(R.id.addxycorder_edt_xy_g);
        xyrecorder_dy = (EditText) findViewById(R.id.addxycorder_edt_xy_d);
        xyrecorder_xl = (EditText) findViewById(R.id.addxycorder_edt_xl);
        remark = (EditText) findViewById(R.id.addxycorder_edt_remark);
        xyrecorder_tijiao.setOnClickListener(this);
    }


    /**
     * 初始化控件和UI视图
     */
    private void initializeViews() {
        showDate = (TextView) findViewById(R.id.addxycorder_text_date);
        pickDate = (ImageView) findViewById(R.id.addxycorder_img_date);
        showTime = (TextView) findViewById(R.id.addxycorder_text_time);
        pickTime = (ImageView) findViewById(R.id.addxycorder_img_time);
        startMeasuring = (Button) findViewById(R.id.startMeasuring);
        liebiao = (TextView) findViewById(R.id.liebiao);

        //设置选择日期、时间
//        pickDate.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Message msg = new Message();
//                if (pickDate.equals((ImageView) v)) {
//                    msg.what = AddXyrecorder.SHOW_DATAPICK;
//                }
//                AddXyrecorder.this.dateandtimeHandler.sendMessage(msg);
//            }
//        });
//
//        pickTime.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Message msg = new Message();
//                if (pickTime.equals((ImageView) v)) {
//                    msg.what = AddXyrecorder.SHOW_TIMEPICK;
//                }
//                AddXyrecorder.this.dateandtimeHandler.sendMessage(msg);
//            }
//        });

        //历次记录列表按钮点击
        liebiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddXyrecorder.this, MainActivity.class);
                intent.putExtra("tabIndex", 0);
                startActivity(intent);
            }
        });
    }

    /**
     * 设置日期
     */
    private void setDateTime() {
        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        updateDateDisplay();
    }

    /**
     * 更新日期显示
     */
    private void updateDateDisplay() {
        showDate.setText(new StringBuilder().append(mYear).append("-")
                .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
                .append((mDay < 10) ? "0" + mDay : mDay));
    }

    /**
     * 日期控件的事件
     */
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            updateDateDisplay();
        }
    };

    /**
     * 设置时间
     */
    private void setTimeOfDay() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        updateTimeDisplay();
    }

    /**
     * 更新时间显示
     */
    private void updateTimeDisplay() {
        showTime.setText(new StringBuilder().append(mHour).append(":")
                .append((mMinute < 10) ? "0" + mMinute : mMinute));

    }

    /**
     * 时间控件事件
     */
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;

            updateTimeDisplay();
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
                        mDay);
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, true);
        }

        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
            case TIME_DIALOG_ID:
                ((TimePickerDialog) dialog).updateTime(mHour, mMinute);
                break;
        }
    }

    /**
     * 处理日期和时间控件的Handler
     */
    Handler dateandtimeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AddXyrecorder.SHOW_DATAPICK:
                    showDialog(DATE_DIALOG_ID);
                    break;
                case AddXyrecorder.SHOW_TIMEPICK:
                    showDialog(TIME_DIALOG_ID);
                    break;
            }
        }

    };


    String url;
    String dynamic_date;
    String dynamic_time;
    String max_blood_pressure;
    String min_blood_pressure;
    String heart_rate;
    String Remark;

    @Override
    public void onClick(View v) {
        if (xyrecorder_gy.getText().toString().equals("") || xyrecorder_dy.getText().toString().equals("") || xyrecorder_xl.getText().toString().equals("")) {
            Toast.makeText(AddXyrecorder.this, "请不要留空值", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Double.parseDouble(xyrecorder_gy.getText().toString()) <= 280.0 && Double.parseDouble(xyrecorder_dy.getText().toString()) >= 20.0 &&
                Double.parseDouble(xyrecorder_gy.getText().toString()) > Double.parseDouble(xyrecorder_dy.getText().toString())){
            if(Double.parseDouble(xyrecorder_xl.getText().toString()) <= 180.0 && 20.0 <= Double.parseDouble(xyrecorder_xl.getText().toString())){
                double gaoya = Double.parseDouble(xyrecorder_gy.getText().toString());
                double diya = Double.parseDouble(xyrecorder_dy.getText().toString());
                String evaluate = "";
                if (180.0 <= gaoya || 110.0 <= diya) {
                    evaluate = "重度高血压，建议您尽快到医院就医，调整血压，同时检查有无并发症的发生。";
                } else if ((160.0 <= gaoya && gaoya <= 179.0) || (100.0 <= diya && diya <= 109.0)) {
                    evaluate = "中度高血压，连续监测7天以上如无变化建议您增加锻炼，低盐饮食.多吃鲜果蔬菜.减少脂肪的摄入.（油.动物脂肪）戒烟酒.保持好心态。同时要就医选择适当的药物治疗。";
                } else if ((140.0 <= gaoya && gaoya <= 159.0) || (90.0 <= diya && diya <= 99.0)) {
                    evaluate = "轻度高血压，请每半小时检测一次频率监测，3次以上如无变化，建议您控制体重，适当增加锻炼。低盐饮食，戒烟限酒，保持良好的心态。(如果连续多次出现此种情况，建议治疗)";
                } else if (90.0 > gaoya || 60.0 > diya) {
                    evaluate = "血压偏低，建议观察上下肢血压比较，如有疲乏无力、头晕头痛，视物黑朦，甚至晕厥，或冷汗、心悸等症状，建议您到医院做全面检查，如无上速症状建议您高营养、富含 饮食，适当参加体育锻炼，增加心肺功能。";
                } else if ((131.0 <= gaoya && gaoya <= 139.0) || (85.0 <= diya && diya <= 89.0)) {
                    evaluate = "正常的高值血压，请注意调整饮食和作息习惯。";
                } else if (90.0 <= gaoya && gaoya <= 130.0 && 60.0 <= diya && diya <= 84.0) {
                    evaluate = "血压正常，你的血压正常,请继续保持！";
                } else {
                    evaluate = "血压不准确，建议您重新测量。";
                }
                if(gaoya - diya >= 60.0){
                    evaluate = evaluate + "\n压差过大，存在动脉硬化的风险，请操作多次测量，测量时保持心态平和，测量间隔5分钟，再咨询医生。";
                }else if(gaoya - diya <= 20.0){
                    evaluate = evaluate + "\n压差过小，存在心力衰竭的风险，请操作多次测量，测量时保持心态平和，测量间隔5分钟再次测量，如持续变小请咨询医师。";
                }
                //执行提交操作
                url = HttpInfo.PATH + HttpInfo.TIANJIADONGTAI;
                dynamic_date = showDate.getText().toString();
                dynamic_time = showTime.getText().toString();
                max_blood_pressure = xyrecorder_gy.getText().toString();
                min_blood_pressure = xyrecorder_dy.getText().toString();
                heart_rate = xyrecorder_xl.getText().toString();
                Remark = remark.getText().toString();
                if (dynamic_date.length() <= 0 || dynamic_time.length() <= 0 || max_blood_pressure.length() <= 0 || min_blood_pressure.length() <= 0 || heart_rate.length() <= 0) {
                    Toast.makeText(AddXyrecorder.this, "请不要留空值", Toast.LENGTH_SHORT).show();
                } else {
                    //不能点击第二次
                    xyrecorder_tijiao.setClickable(false);

                    /*OkHttpUtils.post()
                            .url(url)
                            .addParams("mem_account", mem_account)
                            .addParams("mem_token", mem_token)
                            .addParams("type", "10")
                            .addParams("dynamic_date", dynamic_date)
                            .addParams("dynamic_time", dynamic_time)
                            .addParams("max_blood_pressure", max_blood_pressure)
                            .addParams("min_blood_pressure", min_blood_pressure)
                            .addParams("heart_rate", heart_rate)
                            .addParams("remark", evaluate+"\n"+Remark)
                            .build()
                            .execute(new MyStringCallback());*/

                    System.out.println("dynamic_date:" + dynamic_date);
                    //save to db
                    SaveValueToDB saveValueToDB = new SaveValueToDB();

                    SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");

                    SimpleDateFormat newSdf = new SimpleDateFormat("yyyy-MM-dd");

                    Date date = sdf_date.parse(dynamic_date, new ParsePosition(0));

                    String newDateTime = newSdf.format(date) + " " + dynamic_time + ":00";

                    System.out.println("newDateTime:" + newDateTime);


                    savePressureValue(newDateTime, max_blood_pressure, min_blood_pressure, heart_rate);
                    Toast.makeText(AddXyrecorder.this,"提交成功！", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }

            }else{
                Toast.makeText(AddXyrecorder.this,"心率数据异常", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(AddXyrecorder.this,"血压数据异常", Toast.LENGTH_SHORT).show();
        }

    }
    public void savePressureValue(String time, String high, String low, String rate) {

        //volley
        Map<String, String> param = new HashMap<>();
        param.put("url", "bloodpressure");
        param.put("action", "upload_data");
        param.put("clientid", "10001");
        param.put("SBP", high);
        param.put("DBP", low);
        param.put("HR", rate);
        param.put("measureTime", time);
        /*param.put("url", "user");
        param.put("action", "login");
        param.put("username", "paotail");
        param.put("password", "123456");*/
                    /*param.put("pretime",time.getText().toString());
                    param.put("highpre", et_high.getText().toString());
                    param.put("lowpre", et_low.getText().toString());
                    param.put("rate", et_rate.getText().toString());*/
        System.out.println("pressure volley start");
        VolleyRequestImp volleyRequest = new VolleyRequestImp(param);
        volleyRequest.myVolleyRequestPressure_POST(this);
//        volleyRequest.volleyJsonObjectRequestDome_POST();
//        volleyRequest.volleyStringRequestDome_POST();
        System.out.println("pressure volley fin");


        /*DbProvider provider = new DbProvider();
        provider.init(this);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbPressure.Pressure.USER_ID, Integer.parseInt(MyApplication.getInstance().getUser_id()));
        contentValues.put(DbPressure.Pressure.TIME, time);
        contentValues.put(DbPressure.Pressure.HIGH, Integer.parseInt(high));
        contentValues.put(DbPressure.Pressure.LOW, Integer.parseInt(low));
        contentValues.put(DbPressure.Pressure.RATE, Integer.parseInt(rate));

        provider.insert(DbPressure.CONTENT_URI, contentValues);
        provider.database.close();*/

        /*System.out.println("okhttp start");
        upload();
        System.out.println("okhttp fin");*/
    }

    private void upload() {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        String json = "{'url':'user'," +
                "'action':'login'," +
                "'username':'paotail'," +
                "'password':'123456'}";
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("http://gist.nju.edu.cn:12080/action.php")
                .post(body)
                .build();
        try  {
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class MyStringCallback extends StringCallback {

        @Override
        public void onError(Call call, Exception e) {
            Toast.makeText(AddXyrecorder.this, "添加失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String response) {
            /*xyrecorder_tijiao.setClickable(true);
            Intent intent = new Intent(AddXyrecorder.this, Xiangqing.class);
            intent.putExtra("max_blood_pressure", max_blood_pressure);
            intent.putExtra("min_blood_pressure", min_blood_pressure);
            intent.putExtra("heart_rate", heart_rate);
            startActivity(intent);
            AddXyrecorder.this.finish();*/
        }
    }

    public void back() {
        RelativeLayout back = (RelativeLayout) findViewById(R.id.back_relative);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num == 0){
                    finish();
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddXyrecorder.this,"血压测量中，请稍后", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    //16进制String字符串转换byte数组
    private byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    //广播接收血压等信息
    private final BroadcastReceiver onReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BluetoothLEService.ACTION_BLUETOOTH_DEVICE_FOUND.equals(action)) {
                if (mBluetoothLEService != null) {
                    Toast.makeText(AddXyrecorder.this, "正在连接", Toast.LENGTH_SHORT).show();
                    mBluetoothLEService.Connect(mBluetoothLEService.getDeviceAddress());
                }
            } else if (BluetoothLEService.ACTION_BLUETOOTH_CONNECTED.equals(action)) {
                Toast.makeText(AddXyrecorder.this, "连接成功", Toast.LENGTH_SHORT).show();
                ConnectBlue = true;
                //发送测量指令开始测量
                mBluetoothLEService.writePackage(hexStringToBytes(bluetoothCommand));

            } else if (BluetoothLEService.ACTION_BLUETOOTH_DISCONNECTED.equals(action)) {
                Toast.makeText(AddXyrecorder.this, "连接已断开", Toast.LENGTH_SHORT).show();
                ConnectBlue = false;
                num = 0;
            } else if (BluetoothLEService.ACTION_BLUETOOTH_DATAARRIVED.equals(action)) {
                byte bleData[] = intent.getByteArrayExtra(BluetoothLEService.ACTION_BLUETOOTH_DATAARRIVED);
                if(String.format("%02X", bleData[0]).equalsIgnoreCase("D0")){
                    //进入第一顺序选择的血压仪器
                    if(bleData.length == 8){
                        startMeasuring.setText("测量中···");
                        startMeasuring.setClickable(false);
                        num = 2;
                    }else if(bleData.length == 9){
                        startMeasuring.setText("开始测量");
                        startMeasuring.setClickable(true);
                        if(String.format("%02X", bleData[5]).equalsIgnoreCase("00") && String.format("%02X", bleData[6]).equalsIgnoreCase("00")){
                            String errorInfo = "";
                            Log.i("1526", String.format("%02X", bleData[4]));
                            if(String.format("%02X", bleData[4]).equalsIgnoreCase("01")){
                                errorInfo = "传感器震荡异常";
                            }else if(String.format("%02X", bleData[4]).equalsIgnoreCase("02")){
                                errorInfo = "检测不到足够的心跳或算不出血压";
                            }else if(String.format("%02X", bleData[4]).equalsIgnoreCase("03")){
                                errorInfo = "测量结果异常";
                            }else if(String.format("%02X", bleData[4]).equalsIgnoreCase("04")){
                                errorInfo = "袖带过松或漏气(10秒内加压不到30mmHg)";
                            }else if(String.format("%02X", bleData[4]).equalsIgnoreCase("05")){
                                errorInfo = "气管被堵住";
                            }else if(String.format("%02X", bleData[4]).equalsIgnoreCase("06")){
                                errorInfo = "测量时压力波动大";
                            }else if(String.format("%02X", bleData[4]).equalsIgnoreCase("07")){
                                errorInfo = "压力超过上限";
                            }else if(String.format("%02X", bleData[4]).equalsIgnoreCase("08")){
                                errorInfo = "血压计存储区没有测量数据";
                            }
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AddXyrecorder.this);
                            builder.setTitle("测量错误");
                            builder.setMessage(errorInfo);
                            builder.setPositiveButton("确定", null);
                            builder.show();
                        }else{
                            startMeasuring.setText("开始测量");
                            startMeasuring.setClickable(true);
                            int highPressure = Integer.parseInt(String.format("%02X", bleData[4]), 16);
                            xyrecorder_gy.setText(String.valueOf(highPressure));
                            int lowPressure = Integer.parseInt(String.format("%02X", bleData[5]), 16);
                            xyrecorder_dy.setText(String.valueOf(lowPressure));
                            int heartRate = Integer.parseInt(String.format("%02X", bleData[6]), 16);
                            xyrecorder_xl.setText(String.valueOf(heartRate));
                        }
                        num = 0;
                    }
                } else {
                    //进入第二顺序选择的血压仪器
                    String result = String.valueOf(String.format("%02X", bleData[5]));        //注意每个数字后面都有空格
                    if (result.equals("50") && num == 0) {
                        startMeasuring.setText("准备测量");
                        startMeasuring.setClickable(false);
                        num = 1;
                    } else if (result.equals("54") && num == 1) {
                        startMeasuring.setText("测量中···");
                        startMeasuring.setClickable(false);
                        num = 2;
                    } else if (result.equals("55") && num == 2) {
                        startMeasuring.setText("开始测量");
                        startMeasuring.setClickable(true);
                        int highPressure = Integer.parseInt(String.format("%02X", bleData[7]), 16);
                        xyrecorder_gy.setText(String.valueOf(highPressure));
                        int lowPressure = Integer.parseInt(String.format("%02X", bleData[9]), 16);
                        xyrecorder_dy.setText(String.valueOf(lowPressure));
                        int heartRate = Integer.parseInt(String.format("%02X", bleData[10]), 16);
                        xyrecorder_xl.setText(String.valueOf(heartRate));
//                    Log.i("1526",String.format("%02X", bleData[11]));
//                    if(String.format("%02X", bleData[11]).equals("11")){
//                        //无心率不齐
//                    }else{
//                        //心率不齐
//                    }
                        num = 0;
                    } else if (result.equals("56")) {
                        startMeasuring.setText("开始测量");
                        startMeasuring.setClickable(true);
                        String errorInfo = null;
                        if (String.format("%02X", bleData[6]).equals("01")) {
                            errorInfo = "打气11s袖带气压不足50mmHg";
                        } else if (String.format("%02X", bleData[6]).equals("02")) {
                            errorInfo = "气袋压力超过295mmHg";
                        } else if (String.format("%02X", bleData[6]).equals("03")) {
                            errorInfo = "测量不到有效脉搏";
                        } else if (String.format("%02X", bleData[6]).equals("04")) {
                            errorInfo = "干扰过多";
                        } else if (String.format("%02X", bleData[6]).equals("05")) {
                            errorInfo = "测量结果数值有误";
                        }
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AddXyrecorder.this);
                        builder.setTitle("测量错误");
                        builder.setMessage(errorInfo);
                        builder.setPositiveButton("确定", null);
                        builder.show();
                        num = 0;
                    }
                }
            }
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!ConnectBlue) {
                        if (mBluetoothLEService == null) {
                            return;
                        }
                        //连接蓝牙
                        if (mBluetoothLEService.IsOpened(AddXyrecorder.this)) {
                            mBluetoothLEService.Scan(true);
                        }
                    } else {
                        //发送测量指令开始测量
                        mBluetoothLEService.writePackage(hexStringToBytes(bluetoothCommand));
                    }
                    xyrecorder_gy.setText("");
                    xyrecorder_dy.setText("");
                    xyrecorder_xl.setText("");
                } else {
                    Toast.makeText(AddXyrecorder.this, "必须通过位置授权", Toast.LENGTH_SHORT).show();
                }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(num == 0){
            finish();
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddXyrecorder.this,"血压测量中，请稍后", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
