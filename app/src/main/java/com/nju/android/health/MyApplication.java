package com.nju.android.health;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.wxlib.util.SysUtil;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.leakcanary.LeakCanary;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 57248 on 2016/8/26.
 */
public class MyApplication extends Application{

    public final String APP_KEY = "23015524";
    public final String userid = "testpro1";

    public static String sex = "1";         //男1，女2
    public static String age = "60";        //年龄
    public static String height = "170";        //身高

    //Volley的全局请求队列
    public static RequestQueue sRequestQueue;

    private Context _context;
    //单例模式的入口
    private static MyApplication instance;
    // 存储用户名id
    private String user_id = null;
    // 存储seesionid
    private String session_id = null;

    // 存储应用锁打开的Activity，方便退出应用的时候回收
    private List<Activity> mList = new LinkedList<Activity>();


    /**
     * @return Volley全局请求队列
     */
    public static RequestQueue getRequestQueue() {
        return sRequestQueue;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        _context = getApplicationContext();

        //实例化Volley全局请求队列
        sRequestQueue = Volley.newRequestQueue(getApplicationContext());


        SysUtil.setApplication(this);
        if (SysUtil.isTCMSServiceProcess(this)) {
            return;
        }
        YWAPI.init(this, APP_KEY);

//        SpeechUtility.createUtility(MyApplication.this, SpeechConstant.APPID + getString(R.string.xfei_appid));

    }

    // 启动一个Activity的时候记录当前Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    // 程序退出的方法，退出所有Activity
    public void exit() {
        LogoutThread t = new LogoutThread();
        t.start();
    }

    public String getUser_id() {
        return user_id;
    }

    public String getSession_id() {
        return session_id;
    }



    // Application类，不能使用private属性，否则会报错
    public MyApplication() {
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }



    public synchronized static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    /**
     * 描述：退出系统的线程
     *
     */
    class LogoutThread extends Thread {
        @Override
        public void run() {

        }
    }
}
