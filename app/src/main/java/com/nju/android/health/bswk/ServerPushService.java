package com.nju.android.health.bswk;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.nju.android.health.R;
import com.nju.android.health.views.activities.MainActivity;


public class ServerPushService extends Service {


    public ServerPushService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        System.out.println("========ServerPushService=========onCreate========");
        super.onCreate();


    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        flags = START_STICKY;
        System.out.println("========ServerPushService========onStartCommand=========");

        if (intent.getAction().equals("qingdu_service")){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if (intent.getAction().equals("qingdu_service")){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(ServerPushService.this);
        Intent newintent = new Intent(Intent.ACTION_MAIN);
        newintent.addCategory(Intent.CATEGORY_LAUNCHER);
        newintent.setClass(this, MainActivity.class);
        newintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        Context mContext = getApplicationContext();
        PendingIntent contentIndent = PendingIntent.getActivity(mContext, 0, newintent, 0);
        builder.setContentIntent(contentIndent).setSmallIcon(R.mipmap.logo)//设置状态栏里面的图标（小图标） 　　　　　　　　　　　　　　　　　
                .setWhen(System.currentTimeMillis())//设置时间发生时间
                .setAutoCancel(true)//设置可以清除
                .setDefaults(Notification.DEFAULT_VIBRATE)//设置改通知优先级
                .setPriority(Notification.PRIORITY_DEFAULT)//向通知添加声音，闪灯和震动效果
                .setContentTitle("百世维康友情提示")//设置下拉列表里的标题
                .setContentText("测量时间到了，点击跳转到应用")//设置提示内容
                .build();//设置上下文内容
        Notification notification = builder.getNotification();
        //加i是为了显示多条Notification
        notificationManager.notify(0, notification);

        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("========ServerPushService=========onDestroy========");
    }
}
