package com.nju.android.health.views.activities.pressure;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nju.android.health.R;

public class TwiceMoniActivity extends AppCompatActivity {
    Button remindbtn,measurebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pres_twice_moni);

        measurebtn = (Button)findViewById(R.id.button4);
        //remindbtn = (Button)findViewById(R.id.button5);

        final long delay=3000;


	        	/*remindbtn.setOnClickListener(new OnClickListener()
	   		 {
	   			@Override
	   		    public void onClick(View v)
	   			{

	   				// TODO Auto-generated method stub

	   			    NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
	   			         Notification.Builder builder = new Notification.Builder(Twicemonitor.this);
	   			         builder.setOngoing(true);
	   			         builder.setTicker("我的健康APP");//设置title
	   			         builder.setWhen(System.currentTimeMillis());
	   			         builder.setContentTitle("健康软件APP");//设置内容
	   			         builder.setContentText("您现在可以继续测血压啦，亲！！");
	   			         builder.setAutoCancel(true);//点击消失
	   			         builder.setDefaults(Notification.DEFAULT_VIBRATE);
	   			         builder.setSmallIcon(R.drawable.img_bloodp1);
	   			         builder.setVibrate(new long[] {0,300,500,700});
	   			         Intent notificationIntent = new Intent(Twicemonitor.this, Twicemonitor.class);
	   			         builder.setContentIntent(PendingIntent.getActivity(Twicemonitor.this, 0, notificationIntent, 0));//这句和点击消失那句是“Notification点击消失但不会跳转”的必须条件，如果只有点击消失那句，这个功能是不能实现的
	   			         Notification noti = builder.getNotification();
	   			         mNotificationManager.notify(1,noti);



	   			}

	   		 });*/




        measurebtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new Handler().postDelayed(new Runnable(){
                    public void run() {

                        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification.Builder builder = new Notification.Builder(TwiceMoniActivity.this);
                        builder.setOngoing(true);
                        builder.setTicker("我的健康APP");//设置title
                        builder.setWhen(System.currentTimeMillis());
                        builder.setContentTitle("健康软件APP");//设置内容
                        builder.setContentText("您现在可以继续测血压啦，亲！！");
                        builder.setAutoCancel(true);//点击消失
                        builder.setDefaults(Notification.DEFAULT_VIBRATE);
                        builder.setSmallIcon(R.drawable.img_blood);
                        builder.setVibrate(new long[] {0,300,500,700});
                        Intent notificationIntent = new Intent(TwiceMoniActivity.this, TwiceMoniSeActivity.class);
                        builder.setContentIntent(PendingIntent.getActivity(TwiceMoniActivity.this, 0, notificationIntent, 0));//这句和点击消失那句是“Notification点击消失但不会跳转”的必须条件，如果只有点击消失那句，这个功能是不能实现的
                        Notification noti = builder.getNotification();
                        mNotificationManager.notify(1,noti);

                    }
                }, delay);
            }
        });
    }
}
