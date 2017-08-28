package com.nju.android.health.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.nju.android.health.views.activities.pressure.AlarmAlertActivity;

/**
 * Created by 57248 on 2016/8/29.
 */
public class CallAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent i = new Intent(context, AlarmAlertActivity.class);

        Bundle bundleRet = new Bundle();
        bundleRet.putString("STR_CALLER", "");
        i.putExtras(bundleRet);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
