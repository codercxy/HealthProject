package com.nju.android.health.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nju.android.health.views.activities.MainActivity;
import com.nju.android.health.views.activities.pressure.ManualActivity;

/**
 * Created by 57248 on 2016/10/20.
 */

public class NotificationReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("RepeatReceiver_notifi", "here");
        /*Intent mainIntent = new Intent(context, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/

        Intent measureIntent = new Intent(context, ManualActivity.class);
        measureIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(measureIntent);

        /*Intent[] intents = {mainIntent, measureIntent};

        context.startActivities(intents);*/
    }
}
