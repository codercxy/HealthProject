package com.nju.android.health.utils;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.nju.android.health.R;
import com.nju.android.health.views.activities.MainActivity;
import com.nju.android.health.views.activities.pressure.ManualActivity;
import com.ta.utdid2.android.utils.SystemUtils;

import static android.app.Notification.FLAG_AUTO_CANCEL;

/**
 * Created by 57248 on 2016/10/20.
 */

public class AlarmReceiver extends BroadcastReceiver{

    private static final String TAG = "RepeatReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "here");
        /*Intent broadcastIntent = new Intent(context, NotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);*/

        Intent broadcastIntent = new Intent(context, ManualActivity.class);

        PendingIntent pendingIntent = PendingIntent.
                getActivity(context, 0, broadcastIntent, PendingIntent.FLAG_ONE_SHOT);
//
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("通知头")
                .setTicker("Ticker")
                .setContentIntent(pendingIntent)

                .setSmallIcon(android.R.drawable.ic_lock_idle_charging);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = builder.build();
        notify.flags = FLAG_AUTO_CANCEL;

        manager.notify(0, notify);
//        Notification n = new Notification(R.drawable.ic_person_grey600_24dp, "get new", System.currentTimeMillis());
//        n.flags = Notification.FLAG_AUTO_CANCEL;
//        n.defaults = Notification.DEFAULT_ALL;
//
//        Intent i = new Intent(context, ManualActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pi = PendingIntent.getActivity(context, 0, i,
//                PendingIntent.FLAG_CANCEL_CURRENT);


//        manager.notify(R.string.app_name, n);



    }
}
