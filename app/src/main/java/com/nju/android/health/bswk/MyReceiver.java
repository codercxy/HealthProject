package com.nju.android.health.bswk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        System.out.println("======================onReceive====================");
        if (intent.getAction().equals("qingdu")) {
            Xiangqing.forNum++;
            if (Xiangqing.forNum > 3) {
                //  关闭一个Service
               Intent serviceIntent = new Intent(context, ServerPushService.class);
                intent.setAction("qingdu_service");
               context.stopService(serviceIntent);
                System.out.println("========停止一个Service半小时=================");
            } else {
                //  启动一个Service
                Intent serviceIntent = new Intent(context, ServerPushService.class);
                context.startService(serviceIntent);
                System.out.println("========启动一个Service半小时=================");
            }
        } else if (intent.getAction().equals("zhongdu")) {
            Xiangqing.forNum++;
            if (Xiangqing.forNum >7){
                //  关闭一个Service
                intent.setAction("zhongdu_service");
                Intent serviceIntent = new Intent(context, ServerPushService.class);
                context.stopService(serviceIntent);
                System.out.println("========停止一个Service一天=================");
            }else {
                //  启动一个Service
                Intent serviceIntent = new Intent(context, ServerPushService.class);
                context.startService(serviceIntent);
                System.out.println("========启动一个Service一天=================");
            }
        }
    }
}
