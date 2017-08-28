package com.nju.android.health.bswk.body;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nju.android.health.R;
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

/**
 * Created by Administrator on 2016/3/10.
 */
public class RentichengfenAdd extends Activity {

    @Bind(R.id.rtchengfeng_tijiao)
    TextView rtchengfengTijiao;
    @Bind(R.id.rtchengfeng_tizhong)
    EditText rtchengfengTizhong;
    @Bind(R.id.rtchengfeng_zhifang)
    EditText rtchengfengZhifang;
    @Bind(R.id.rtchengfeng_guliang)
    EditText rtchengfengGuliang;
    @Bind(R.id.rtchengfeng_jirou)
    EditText rtchengfengJirou;
    @Bind(R.id.rtchengfeng_neizang)
    EditText rtchengfengNeizang;
    @Bind(R.id.rtchengfeng_zukang)
    EditText rtchengfengZukang;
    @Bind(R.id.rtchengfeng_zhiliang)
    EditText rtchengfengZhiliang;
    @Bind(R.id.person_zhiliang)
    ImageView personZhiliang;
    @Bind(R.id.rtchengfeng_shuifen)
    EditText rtchengfengShuifen;
    @Bind(R.id.rtchengfeng_reliang)
    EditText rtchengfengReliang;
    @Bind(R.id.rtchengfeng_remark)
    EditText rtchengfengRemark;
    private TextView showDate = null;
    private ImageView pickDate = null;
    private TextView showTime = null;
    private ImageView pickTime = null;
    private TextView liebiao_chengfen;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpersonchengfen);
        ButterKnife.bind(this);
        fileService = new FileService(this);
        try {
            Map<String, String> map = fileService.getUserInfo("bswk.txt");
            mem_account = map.get("mem_account");
            mem_token = map.get("mem_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    }


    public void back() {
        RelativeLayout back = (RelativeLayout) findViewById(R.id.relativeLayout117);
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
        showDate = (TextView) findViewById(R.id.person_dateshow);
        pickDate = (ImageView) findViewById(R.id.person_datepick);
        showTime = (TextView) findViewById(R.id.person_timeshow);
        pickTime = (ImageView) findViewById(R.id.person_timepick);
        liebiao_chengfen = (TextView) findViewById(R.id.liebiao_chengfen);

        pickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Message msg = new Message();
                if (pickDate.equals((ImageView) v)) {
                    msg.what = RentichengfenAdd.SHOW_DATAPICK;
                }
                RentichengfenAdd.this.dateandtimeHandler.sendMessage(msg);
            }
        });

        pickTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Message msg = new Message();
                if (pickTime.equals((ImageView) v)) {
                    msg.what = RentichengfenAdd.SHOW_TIMEPICK;
                }
                RentichengfenAdd.this.dateandtimeHandler.sendMessage(msg);
            }
        });

        //记录列表按钮
        liebiao_chengfen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RentichengfenAdd.this,Rentichengfen.class);
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


        System.out.println("-------------设置时间-----" + mHour + mMinute);
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
                case RentichengfenAdd.SHOW_DATAPICK:
                    showDialog(DATE_DIALOG_ID);
                    break;
                case RentichengfenAdd.SHOW_TIMEPICK:
                    showDialog(TIME_DIALOG_ID);
                    break;
            }
        }

    };

    private String mem_account, mem_token;

    @OnClick(R.id.rtchengfeng_tijiao)
    public void onClick() {

        String url = HttpInfo.PATH + HttpInfo.TIANJIADONGTAI;
        String dynamic_date = showDate.getText().toString();
        String dynamic_time = showTime.getText().toString();
        String weight = rtchengfengTizhong.getText().toString();
        String fat = rtchengfengZhifang.getText().toString();
        String boneMass = rtchengfengGuliang.getText().toString();
        String muscle = rtchengfengJirou.getText().toString();
        String visceral_fat = rtchengfengNeizang.getText().toString();
        String body_impedance = rtchengfengZukang.getText().toString();
        String quality_index = rtchengfengZhiliang.getText().toString();
        String moisture = rtchengfengShuifen.getText().toString();
        String heat = rtchengfengReliang.getText().toString();
        String remark=rtchengfengRemark.getText().toString();

        if (dynamic_date.length() <= 0 || dynamic_time.length() <= 0 || weight.length() <= 0 || fat.length() <= 0 || boneMass.length() <= 0 ||
                muscle.length() <= 0 || visceral_fat.length() <= 0 || body_impedance.length() <= 0 || quality_index.length() <= 0
                || moisture.length() <= 0 || heat.length() <= 0) {
            Toast.makeText(RentichengfenAdd.this, "请不要留空值", Toast.LENGTH_SHORT).show();
        } else {
            //不能二次点击
            rtchengfengTijiao.setClickable(false);

            OkHttpUtils.post()
                    .url(url)
                    .addParams("mem_account", mem_account)
                    .addParams("mem_token", mem_token)
                    .addParams("type", "6")
                    .addParams("dynamic_date", dynamic_date)
                    .addParams("dynamic_time", dynamic_time)
                    .addParams("remark",remark)
                    .addParams("weight", weight)
                    .addParams("fat", fat)
                    .addParams("boneMass", boneMass)
                    .addParams("muscle", muscle)
                    .addParams("visceral_fat", visceral_fat)
                    .addParams("body_impedance", body_impedance)
                    .addParams("quality_index", quality_index)
                    .addParams("moisture", moisture)
                    .addParams("heat", heat)

                    .build()
                    .execute(new MyStringCallback());
        }
    }

    private class MyStringCallback extends StringCallback {

        @Override
        public void onError(Call call, Exception e) {
            Toast.makeText(RentichengfenAdd.this, "添加失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String response) {
            rtchengfengTijiao.setClickable(true);
            Toast.makeText(RentichengfenAdd.this, "添加成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RentichengfenAdd.this, Rentichengfen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            RentichengfenAdd.this.finish();


        }
    }
}

