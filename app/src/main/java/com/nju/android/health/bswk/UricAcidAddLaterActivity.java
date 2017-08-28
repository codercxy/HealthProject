package com.nju.android.health.bswk;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nju.android.health.MyApplication;
import com.nju.android.health.R;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UricAcidAddLaterActivity extends AppCompatActivity {

    @Bind(R.id.niaosuan_jieguo)
    TextView niaosuanJieguo;
    @Bind(R.id.niaosuan_pingjia_1)
    TextView niaosuanPingjia1;
    @Bind(R.id.niaosuan_pingjia_2)
    TextView niaosuanPingjia2;

    private double niaosuan_shuzhi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uric_acid_add_later);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        niaosuan_shuzhi = Double.parseDouble(intent.getStringExtra("niaosuan_shuzhi"));

        if(MyApplication.sex.equals("1")){
            if(niaosuan_shuzhi > 149 && niaosuan_shuzhi <= 461){
                niaosuanJieguo.setText("正常");
                niaosuanPingjia1.setText("您的尿酸值在正常范围");
                niaosuanPingjia2.setText("请继续保持良好的生活习惯。");
            }else{
                niaosuanJieguo.setText("异常");
                niaosuanPingjia1.setText("需要引起重视");
                niaosuanPingjia2.setText("注意饮食或咨询医生");
            }
        }else if(MyApplication.sex.equals("2")){
            if(niaosuan_shuzhi > 89 && niaosuan_shuzhi <= 375){
                niaosuanJieguo.setText("正常");
                niaosuanPingjia1.setText("您的尿酸值在正常范围");
                niaosuanPingjia2.setText("请继续保持良好的生活习惯。");
            }else{
                niaosuanJieguo.setText("异常");
                niaosuanPingjia1.setText("需要引起重视");
                niaosuanPingjia2.setText("注意饮食或咨询医生");
            }
        }
    }

    @OnClick({R.id.niaosuan_shi, R.id.niaosuan_fou, R.id.niaosuan_fanhui, R.id.niaosuan_zixun})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.niaosuan_shi:
                // 获得AlarmManager实例,注意这里并不是new一个对象，Alarmmanager为系统级服务
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                // 实例化Intent
                Intent intent = new Intent(UricAcidAddLaterActivity.this, MyReceiver.class);
                intent.setAction("消息");
                //定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
                PendingIntent sender = PendingIntent.getBroadcast(UricAcidAddLaterActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                //开始时间
                long firstime= SystemClock.elapsedRealtime();
                //  startService(intent);
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstime+24*60*60*1000,24*60*60*1000,sender);

                Toast.makeText(UricAcidAddLaterActivity.this, "一天后将给您发送通知提示", Toast.LENGTH_LONG).show();
                break;
            case R.id.niaosuan_fou:
                Toast.makeText(UricAcidAddLaterActivity.this, "已取消发送通知提示", Toast.LENGTH_SHORT).show();
                Intent intent1=new Intent(UricAcidAddLaterActivity.this, ServerPushService.class);
                stopService(intent1);
                break;
            case R.id.niaosuan_fanhui:
                finish();
                break;
            case R.id.niaosuan_zixun:
                break;
        }
    }

}
