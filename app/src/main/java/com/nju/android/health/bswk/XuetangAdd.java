package com.nju.android.health.bswk;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nju.android.health.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Calendar;
import java.util.Map;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/3/9.
 */
public class XuetangAdd extends Activity {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 123;
    @Bind(R.id.addxyrecorder_tijiao)
    TextView addxyrecorderTijiao;
    @Bind(R.id.shuzhi_xuetang)
    EditText shuzhiXuetang;
    private TextView showDate = null;
    private ImageView pickDate = null;
    private TextView showTime = null;
    private ImageView pickTime = null;
    private TextView liebiao_xuetang;
    private Button bt_ConnectBluetooth;

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

    //自定义实体弹出类
    private Xuetangshiduan xuetangshiduan;
    private String mem_account, mem_token;

    private static BluetoothLEService_2 mBluetoothLEService = null;
    private ServiceConnection mBluetoothLEConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder service) {
            mBluetoothLEService = ((BluetoothLEService_2.LocalBinder) service).getService();
            if (mBluetoothLEService == null) {
                finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBluetoothLEService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xuetangadd);
        ButterKnife.bind(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());
        fileService = new FileService(this);


        try {
            Map<String, String> map = fileService.getUserInfo("bswk.txt");
            mem_account = map.get("mem_account");
            mem_token = map.get("mem_token");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent bindintent = new Intent(XuetangAdd.this, BluetoothLEService_2.class);
        bindService(bindintent, mBluetoothLEConnection, BIND_AUTO_CREATE);
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLEService_2.ACTION_BLUETOOTH_CONNECTED);
        intentFilter.addAction(BluetoothLEService_2.ACTION_BLUETOOTH_DISCONNECTED);
        intentFilter.addAction(BluetoothLEService_2.ACTION_BLUETOOTH_DEVICE_FOUND);
        intentFilter.addAction(BluetoothLEService_2.ACTION_BLUETOOTH_DATAARRIVED);
        registerReceiver(onReceiver, intentFilter);


        initializeViews();
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        setDateTime();
        setTimeOfDay();
        back();
        dialog();
    }

    private TextView shiduan;

    public void dialog() {

        RelativeLayout shiduan_choose = (RelativeLayout) findViewById(R.id.shiduan_choose);
        shiduan = (TextView) findViewById(R.id.shiduanxuanze);
        shiduan_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(XuetangAdd.this);
                builder1.setTitle("请选择时段：");
                //    指定下拉列表的显示数据
                final String[] choose1 = {"空腹", "餐后"};
                //    设置一个下拉的列表选择项
                builder1.setItems(choose1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shiduan.setText(choose1[which]);
                    }
                });
                builder1.show();

            }
        });

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


    /**
     * 初始化控件和UI视图
     */
    private void initializeViews() {
        showDate = (TextView) findViewById(R.id.xuetang_dateshow);
        pickDate = (ImageView) findViewById(R.id.xuetang_datepick);
        showTime = (TextView) findViewById(R.id.xuetang_timeshow);
        pickTime = (ImageView) findViewById(R.id.xuetang_timepick);
        liebiao_xuetang = (TextView) findViewById(R.id.liebiao_xuetang);
        bt_ConnectBluetooth = (Button) findViewById(R.id.bt_ConnectBluetooth);

        pickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Message msg = new Message();
                if (pickDate.equals((ImageView) v)) {
                    msg.what = XuetangAdd.SHOW_DATAPICK;
                }
                XuetangAdd.this.dateandtimeHandler.sendMessage(msg);
            }
        });

        pickTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Message msg = new Message();
                if (pickTime.equals((ImageView) v)) {
                    msg.what = XuetangAdd.SHOW_TIMEPICK;
                }
                XuetangAdd.this.dateandtimeHandler.sendMessage(msg);
            }
        });

        //记录列表按钮
        liebiao_xuetang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XuetangAdd.this, Xuetang.class);
                startActivity(intent);
            }
        });

        //连接蓝牙设备测量血糖
        bt_ConnectBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有权限
                if (ContextCompat.checkSelfPermission(XuetangAdd.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //请求权限
                    ActivityCompat.requestPermissions(XuetangAdd.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                    //判断是否需要 向用户解释，为什么要申请该权限
                    if (ActivityCompat.shouldShowRequestPermissionRationale(XuetangAdd.this,
                            Manifest.permission.READ_CONTACTS)) {
                        Toast.makeText(XuetangAdd.this, "should Show Request Permission Rationale", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (mBluetoothLEService == null) {
                        return;
                    }
                    if (mBluetoothLEService.IsOpened(XuetangAdd.this)) {
                        mBluetoothLEService.setDeviceName("BeneCheck TC-B DONGLE");
                        mBluetoothLEService.Scan(true);
                    }
                }
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
                case XuetangAdd.SHOW_DATAPICK:
                    showDialog(DATE_DIALOG_ID);
                    break;
                case XuetangAdd.SHOW_TIMEPICK:
                    showDialog(TIME_DIALOG_ID);
                    break;
            }
        }

    };

    private String shiduan_text;
    private TextView shiduan_shuzhi;
    String time_interval;
    String numerical_value;

    @OnClick(R.id.addxyrecorder_tijiao)
    public void onClick() {
        shiduan_shuzhi = (TextView) findViewById(R.id.shiduanxuanze);

        shiduan_text = shiduan.getText().toString().trim();
        EditText editText = (EditText) findViewById(R.id.xuetang_edt_remark);
        String url = HttpInfo.PATH + HttpInfo.TIANJIADONGTAI;
        String dynamic_date = showDate.getText().toString();
        String dynamic_time = showTime.getText().toString();
        time_interval = shiduan_text;
        numerical_value = shuzhiXuetang.getText().toString();
        String Remark = editText.getText().toString();


        if (dynamic_date.length() <= 0 || dynamic_time.length() <= 0 || numerical_value.length() <= 0 || time_interval.length() <= 0) {
            Toast.makeText(XuetangAdd.this, "请不要留空值", Toast.LENGTH_SHORT).show();
        } else {
            //不能二次点击
            addxyrecorderTijiao.setClickable(false);

            OkHttpUtils.post()
                    .url(url)
                    .addParams("mem_account", mem_account)
                    .addParams("mem_token", mem_token)
                    .addParams("type", "9")
                    .addParams("dynamic_date", dynamic_date)
                    .addParams("dynamic_time", dynamic_time)
                    .addParams("time_interval", time_interval)
                    .addParams("numerical_value", numerical_value)
                    .addParams("remark", Remark)
                    .build()
                    .execute(new MyStringCallback());
        }
    }

    private int i;

    private class MyStringCallback extends StringCallback {
        @Override
        public void onError(Call call, Exception e) {
            Toast.makeText(XuetangAdd.this, "添加失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String response) {
            addxyrecorderTijiao.setClickable(true);
            // Toast.makeText(XuetangAdd.this, "添加成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(XuetangAdd.this, XuetangAddxiangqing.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("numerical_value", numerical_value);
            intent.putExtra("time_interval", time_interval);
            startActivity(intent);
            XuetangAdd.this.finish();
        }
    }

    //---------------------------------以下是新加2016-11-02------------------------------------
    private final BroadcastReceiver onReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BluetoothLEService_2.ACTION_BLUETOOTH_DEVICE_FOUND.equals(action)) {
                if (mBluetoothLEService != null) {
                    bt_ConnectBluetooth.setText("正在连接···");
                    bt_ConnectBluetooth.setClickable(false);
                    mBluetoothLEService.Connect(mBluetoothLEService.getDeviceAddress());
                }
            } else if (BluetoothLEService_2.ACTION_BLUETOOTH_CONNECTED.equals(action)) {
                bt_ConnectBluetooth.setText("连接成功");
                bt_ConnectBluetooth.setClickable(false);
            } else if (BluetoothLEService_2.ACTION_BLUETOOTH_DISCONNECTED.equals(action)) {
                bt_ConnectBluetooth.setText("连接设备");
                bt_ConnectBluetooth.setClickable(true);
            } else if (BluetoothLEService_2.ACTION_BLUETOOTH_DATAARRIVED.equals(action)) {
                byte bleData[] = intent.getByteArrayExtra(BluetoothLEService_2.ACTION_BLUETOOTH_DATAARRIVED);
                String yyy = "";
                for (int i = 0; i < bleData.length; i++) {
                    yyy = yyy + String.valueOf(String.format("%02x", bleData[i]));
                }
                if(yyy.length() >= 36){
                    double num_10 = Double.parseDouble(toD(yyy.substring(34, 36), 16)) / (double) 18;
                    String text_10 = String.valueOf(num_10);
                    if (text_10.length() < 5) {
                        shuzhiXuetang.setText(text_10);
                    } else {
                        shuzhiXuetang.setText(text_10.substring(0, 4));
                    }
                }
            }
        }
    };

    //获取到位置权限后做的事情--自动连接蓝牙
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //连接蓝牙
                    if (mBluetoothLEService == null) {
                        return;
                    }
                    if (mBluetoothLEService.IsOpened(XuetangAdd.this)) {
                        mBluetoothLEService.setDeviceName("BeneCheck TC-B DONGLE");
                        mBluetoothLEService.Scan(true);
                    }
                } else {
                    Toast.makeText(XuetangAdd.this, "必须通过位置授权", Toast.LENGTH_SHORT).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    // 任意进制数转为十进制数
    public String toD(String a, int b) {
        int r = 0;
        for (int i = 0; i < a.length(); i++) {
            r = (int) (r + formatting(a.substring(i, i + 1))
                    * Math.pow(b, a.length() - i - 1));
        }
        return String.valueOf(r);
    }

    // 将十六进制中的字母转为对应的数字
    public int formatting(String a) {
        int i = 0;
        for (int u = 0; u < 10; u++) {
            if (a.equals(String.valueOf(u))) {
                i = u;
            }
        }
        if (a.equals("a")) {
            i = 10;
        }
        if (a.equals("b")) {
            i = 11;
        }
        if (a.equals("c")) {
            i = 12;
        }
        if (a.equals("d")) {
            i = 13;
        }
        if (a.equals("e")) {
            i = 14;
        }
        if (a.equals("f")) {
            i = 15;
        }
        return i;
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


}
