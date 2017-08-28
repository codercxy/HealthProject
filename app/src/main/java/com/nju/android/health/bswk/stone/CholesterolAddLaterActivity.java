package com.nju.android.health.bswk.stone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.nju.android.health.R;
import com.nju.android.health.bswk.MyReceiver;
import com.nju.android.health.bswk.ServerPushService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CholesterolAddLaterActivity extends AppCompatActivity {

    @Bind(R.id.danguchun_jieguo)
    TextView danguchunJieguo;
    @Bind(R.id.danguchun_pingjia_1)
    TextView danguchunPingjia1;
    @Bind(R.id.danguchun_pingjia_2)
    TextView danguchunPingjia2;

    private double danguchun_shuzhi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cholesterol_add_later);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        danguchun_shuzhi = Double.parseDouble(intent.getStringExtra("danguchun_shuzhi"));

        if (danguchun_shuzhi >= 2.86 && danguchun_shuzhi <= 5.98){
            danguchunJieguo.setText("正常");
            danguchunPingjia1.setText("您的胆固醇在正常范围");
            danguchunPingjia2.setText("请继续保持良好的生活习惯。");
        }else{
            danguchunJieguo.setText("异常");
            danguchunPingjia1.setText("需要引起重视");
            danguchunPingjia2.setText("注意饮食或咨询医生");
        }
    }

    @OnClick({R.id.danguchun_shi, R.id.danguchun_fou, R.id.danguchun_fanhui, R.id.danguchun_zixun})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.danguchun_shi:
                // 获得AlarmManager实例,注意这里并不是new一个对象，Alarmmanager为系统级服务
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                // 实例化Intent
                Intent intent = new Intent(CholesterolAddLaterActivity.this, MyReceiver.class);
                intent.setAction("消息");
                //定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
                PendingIntent sender = PendingIntent.getBroadcast(CholesterolAddLaterActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                //开始时间
                long firstime= SystemClock.elapsedRealtime();
                //  startService(intent);
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstime+24*60*60*1000,24*60*60*1000,sender);

                Toast.makeText(CholesterolAddLaterActivity.this, "一天后将给您发送通知提示", Toast.LENGTH_LONG).show();
                break;
            case R.id.danguchun_fou:
                Toast.makeText(CholesterolAddLaterActivity.this, "已取消发送通知提示", Toast.LENGTH_SHORT).show();
                Intent intent1=new Intent(CholesterolAddLaterActivity.this, ServerPushService.class);
                stopService(intent1);
                break;
            case R.id.danguchun_fanhui:
                finish();
                break;
            case R.id.danguchun_zixun:
                break;
        }
    }
}
