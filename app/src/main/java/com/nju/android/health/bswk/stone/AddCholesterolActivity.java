package com.nju.android.health.bswk.stone;

import android.Manifest;
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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nju.android.health.R;

import com.nju.android.health.bswk.BluetoothLEService_2;
import com.nju.android.health.bswk.FileService;
import com.nju.android.health.bswk.HttpInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Calendar;
import java.util.Map;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class AddCholesterolActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 123;
    @Bind(R.id.danguchun_dateshow)
    TextView danguchunDateshow;
    @Bind(R.id.danguchun_timeshow)
    TextView danguchunTimeshow;
    @Bind(R.id.shiduanxuanze)
    TextView shiduanxuanze;
    @Bind(R.id.shuzhi_danguchun)
    EditText shuzhiDanguchun;
    @Bind(R.id.danguchun_edt_remark)
    EditText danguchunEdtRemark;
    @Bind(R.id.bt_ConnectBluetooth)
    Button btConnectBluetooth;

    private static final int SHOW_DATAPICK = 0;
    private static final int DATE_DIALOG_ID = 1;
    private static final int SHOW_TIMEPICK = 2;
    private static final int TIME_DIALOG_ID = 3;
    @Bind(R.id.danguchun_tijiao)
    TextView danguchunTijiao;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private FileService fileService;

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
        setContentView(R.layout.activity_add_cholesterol);
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

        Intent bindintent = new Intent(AddCholesterolActivity.this, BluetoothLEService_2.class);
        bindService(bindintent, mBluetoothLEConnection, BIND_AUTO_CREATE);
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLEService_2.ACTION_BLUETOOTH_CONNECTED);
        intentFilter.addAction(BluetoothLEService_2.ACTION_BLUETOOTH_DISCONNECTED);
        intentFilter.addAction(BluetoothLEService_2.ACTION_BLUETOOTH_DEVICE_FOUND);
        intentFilter.addAction(BluetoothLEService_2.ACTION_BLUETOOTH_DATAARRIVED);
        registerReceiver(onReceiver, intentFilter);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        setDateTime();
        setTimeOfDay();
    }

    @OnClick({R.id.back_danguchun, R.id.danguchun_tijiao, R.id.liebiao_danguchun, R.id.danguchun_datepick, R.id.danguchun_timepick, R.id.addshiduan_img_time, R.id.bt_ConnectBluetooth})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_danguchun:
                finish();
                break;
            case R.id.danguchun_tijiao:
                saveData();
                break;
            case R.id.liebiao_danguchun:
                Intent intent = new Intent(AddCholesterolActivity.this, CholesterolListActivity.class);
                startActivity(intent);
                break;
            case R.id.danguchun_datepick:
                Message msg = new Message();
                msg.what = AddCholesterolActivity.SHOW_DATAPICK;
                AddCholesterolActivity.this.dateandtimeHandler.sendMessage(msg);
                break;
            case R.id.danguchun_timepick:
                Message msg2 = new Message();
                msg2.what = AddCholesterolActivity.SHOW_TIMEPICK;
                AddCholesterolActivity.this.dateandtimeHandler.sendMessage(msg2);
                break;
            case R.id.addshiduan_img_time:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AddCholesterolActivity.this);
                builder1.setTitle("请选择时段：");
                //    指定下拉列表的显示数据
                final String[] choose1 = {"空腹", "餐后"};
                //    设置一个下拉的列表选择项
                builder1.setItems(choose1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shiduanxuanze.setText(choose1[which]);
                    }
                });
                builder1.show();
//              //实例化弹出框
//                xuetangshiduan = new Xuetangshiduan(XuetangAdd.this, itemsOnClick);
//              //显示窗口
//                xuetangshiduan.showAtLocation(XuetangAdd.this.findViewById(R.id.xuetangadd), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.bt_ConnectBluetooth:
                //判断是否有权限
                if (ContextCompat.checkSelfPermission(AddCholesterolActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //请求权限
                    ActivityCompat.requestPermissions(AddCholesterolActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                    //判断是否需要 向用户解释，为什么要申请该权限
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddCholesterolActivity.this,
                            Manifest.permission.READ_CONTACTS)) {
                        Toast.makeText(AddCholesterolActivity.this, "should Show Request Permission Rationale", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (mBluetoothLEService == null) {
                        return;
                    }
                    if (mBluetoothLEService.IsOpened(AddCholesterolActivity.this)) {
                        mBluetoothLEService.setDeviceName("BeneCheck TC-B DONGLE");
                        mBluetoothLEService.Scan(true);
                    }
                }
                break;
        }
    }

    //提交保存数据
    private void saveData() {
        String url = HttpInfo.PATH + HttpInfo.TIANJIADONGTAI;
        String danguchun_date = danguchunDateshow.getText().toString();           //日期
        String danguchun_time = danguchunTimeshow.getText().toString();           //时间
        String danguchun_shiduan = shiduanxuanze.getText().toString();           //时段
        String danguchun_shuzhi = shuzhiDanguchun.getText().toString();           //胆固醇数值
        String danguchun_beizhu = danguchunEdtRemark.getText().toString();        //备注
        String message = "";

        if (Double.parseDouble(danguchun_shuzhi) >= 2.86 && Double.parseDouble(danguchun_shuzhi) <= 5.98) {
            message = "正常,您的胆固醇在正常范围,请继续保持良好的生活习惯。";
        } else {
            message = "异常,需要引起重视,注意饮食或咨询医生。";
        }

        if (danguchun_date.length() <= 0 || danguchun_time.length() <= 0 || danguchun_shiduan.length() <= 0 || danguchun_shuzhi.length() <= 0) {
            Toast.makeText(AddCholesterolActivity.this, "请不要留空值", Toast.LENGTH_SHORT).show();
        } else {
            //不能二次点击
            danguchunTijiao.setClickable(false);

            OkHttpUtils.post()
                    .url(url)
                    .addParams("mem_account", mem_account)
                    .addParams("mem_token", mem_token)
                    .addParams("type", "13")
                    .addParams("dynamic_date", danguchun_date)
                    .addParams("dynamic_time", danguchun_time)
                    .addParams("time_interval", danguchun_shiduan)
                    .addParams("numerical_value", danguchun_shuzhi)
                    .addParams("remark", message + "\n" + danguchun_beizhu)
                    .build()
                    .execute(new MyStringCallback());
        }
    }

    private class MyStringCallback extends StringCallback {
        @Override
        public void onError(Call call, Exception e) {
            Toast.makeText(AddCholesterolActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String response) {
            danguchunTijiao.setClickable(true);
            Toast.makeText(AddCholesterolActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddCholesterolActivity.this, CholesterolAddLaterActivity.class);
            intent.putExtra("danguchun_shuzhi", shuzhiDanguchun.getText().toString());
            startActivity(intent);
        }
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
        danguchunDateshow.setText(new StringBuilder().append(mYear).append("-")
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
        danguchunTimeshow.setText(new StringBuilder().append(mHour).append(":")
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
                case AddCholesterolActivity.SHOW_DATAPICK:
                    showDialog(DATE_DIALOG_ID);
                    break;
                case AddCholesterolActivity.SHOW_TIMEPICK:
                    showDialog(TIME_DIALOG_ID);
                    break;
            }
        }

    };


    //---------------------------------以下是新加2016-11-14------------------------------------
    private final BroadcastReceiver onReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BluetoothLEService_2.ACTION_BLUETOOTH_DEVICE_FOUND.equals(action)) {
                if (mBluetoothLEService != null) {
                    btConnectBluetooth.setText("正在连接···");
                    btConnectBluetooth.setClickable(false);
                    mBluetoothLEService.Connect(mBluetoothLEService.getDeviceAddress());
                }
            } else if (BluetoothLEService_2.ACTION_BLUETOOTH_CONNECTED.equals(action)) {
                btConnectBluetooth.setText("连接成功");
                btConnectBluetooth.setClickable(false);
            } else if (BluetoothLEService_2.ACTION_BLUETOOTH_DISCONNECTED.equals(action)) {
                btConnectBluetooth.setText("连接设备");
                btConnectBluetooth.setClickable(true);
            } else if (BluetoothLEService_2.ACTION_BLUETOOTH_DATAARRIVED.equals(action)) {
                byte bleData[] = intent.getByteArrayExtra(BluetoothLEService_2.ACTION_BLUETOOTH_DATAARRIVED);
                String yyy = "";
                for (int i = 0; i < bleData.length; i++) {
                    yyy = yyy + String.valueOf(String.format("%02x", bleData[i]));
                }
                if (yyy.length() >= 36) {
                    double num_10 = Double.parseDouble(toD(yyy.substring(34, 36), 16)) / (double) 38.66;
                    String text_10 = String.valueOf(num_10);
                    if (text_10.length() < 5) {
                        shuzhiDanguchun.setText(text_10);
                    } else {
                        shuzhiDanguchun.setText(text_10.substring(0, 4));
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
                    if (mBluetoothLEService.IsOpened(AddCholesterolActivity.this)) {
                        mBluetoothLEService.setDeviceName("BeneCheck TC-B DONGLE");
                        mBluetoothLEService.Scan(true);
                    }
                } else {
                    Toast.makeText(AddCholesterolActivity.this, "必须通过位置授权", Toast.LENGTH_SHORT).show();
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
