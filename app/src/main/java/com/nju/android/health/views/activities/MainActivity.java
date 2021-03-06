package com.nju.android.health.views.activities;



import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.nju.android.health.Interface.ISwitchFragment;
import com.nju.android.health.MyApplication;
import com.nju.android.health.R;
import com.nju.android.health.model.data.Pressure;
import com.nju.android.health.model.data.Step;
import com.nju.android.health.providers.DbProvider;
import com.nju.android.health.service.StepDetector;
import com.nju.android.health.service.StepService;
import com.nju.android.health.utils.BackHandledFragment;
import com.nju.android.health.utils.BackHandledInterface;
import com.nju.android.health.utils.VolleyRequestImp;
import com.nju.android.health.views.fragments.data.DataFragment;
import com.nju.android.health.views.fragments.data.DataListFragment;
import com.nju.android.health.views.fragments.doctor.DiagnoseFragment;
import com.nju.android.health.views.fragments.doctor.DoctorFragment;
import com.nju.android.health.views.fragments.health.HealthFragment;
import com.nju.android.health.views.fragments.home.HomeFragment;
import com.nju.android.health.views.fragments.setting.SettingFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class MainActivity extends AppCompatActivity implements BackHandledInterface, ISwitchFragment{

    /*private View home;
    private View health;
    private View data;
    private View doctor;
    private View setting;*/

    private HomeFragment homeFragment;
    private HealthFragment healthFragment;
    private DataFragment dataFragment;
    private SettingFragment settingFragment;
    private DiagnoseFragment diagnoseFragment;
    private DataListFragment dataListFragment;

    private Fragment mContent;

    private BottomBar mBottomBar;
    private BottomBarTab bottomBarTab;
    private ActionBar mActionBar;

    private BackHandledFragment mBackHandledFragment;
    private boolean hadIntercept;


   /* private ImageView homeimg;
    private ImageView dataimg;
    private ImageView doctorsimg;
    private ImageView settingimg;*/

    private String today;
    private SimpleDateFormat sdf;
    private Step step;
    private DbProvider provider;
    private Parcelable bottomBarState;
    private int currentIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);



//        mActionBar = getSupportActionBar();
//        mActionBar.hide();
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBarTab = mBottomBar.getTabWithId(R.id.bottomBarItemData);
        bottomBarTab.setSelected(true);

        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.bottomBarItemData:
                        setTabSelection(0);
                        break;
                    case R.id.bottomBarItemHome:
                        setTabSelection(1);
                        break;
                    case R.id.bottomBarItemDoctor:
                        setTabSelection(2);
                        break;
                    case R.id.bottomBarItemSetting:
                        setTabSelection(3);
                        break;
                }
            }
        });
        mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.bottomBarItemData:
                        setTabSelection(0);
                        break;
                    case R.id.bottomBarItemHome:
                        setTabSelection(1);
                        break;
                    case R.id.bottomBarItemDoctor:
                        setTabSelection(2);
                        break;
                    case R.id.bottomBarItemSetting:
                        setTabSelection(3);
                        break;
                }
            }
        });



        /*mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(1, 0xFF5D4037);
        mBottomBar.mapColorForTab(2, "#7B1FA2");
        mBottomBar.mapColorForTab(3, "#FF5252");
        mBottomBar.mapColorForTab(4, "#FF9800");

        BottomBarBadge unreadMessages = mBottomBar.makeBadgeForTabAt(0, "#FF0000", 11);


        unreadMessages.show();
        unreadMessages.hide();

        unreadMessages.setCount(2);
        unreadMessages.setAnimationDuration(200);
        unreadMessages.setAutoShowAfterUnSelection(true);*/

        initData();
        initService();
        Intent intent = getIntent();

        if(intent != null && savedInstanceState == null){
            currentIndex = intent.getIntExtra("tabIndex", 1);
            if (currentIndex == 0) {
                FragmentTransaction ft = getFragmentManager()
                        .beginTransaction();
                if (dataListFragment == null) {
                    dataListFragment = new DataListFragment();
                }
                switchContent(dataListFragment, "dataListFragment");
            } else {
                setTabSelection(currentIndex);
                mBottomBar.setDefaultTabPosition(currentIndex);
            }


//            currentIndex = 1;
//            mBottomBar.selectTabAtPosition(1);
//            mBottomBar.setDefaultTabPosition(1);
//            home.setBackgroundColor(Color.rgb(220, 220, 220));
        }


        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment ft = getFragmentManager().findFragmentById(R.id.content);

                System.out.println(ft);
                if (ft instanceof DataFragment) {
                    if (mBottomBar.getCurrentTabPosition() != 0)
                        mBottomBar.setDefaultTabPosition(0);
//                    mBottomBar.setDefaultTabPosition(1);
//                    mBottomBar.selectTabAtPosition(1);
                } else if (ft instanceof HealthFragment) {
                    if (mBottomBar.getCurrentTabPosition() != 1)
                        mBottomBar.setDefaultTabPosition(1);
//                    mBottomBar.selectTabAtPosition(0);
                } else if (ft instanceof DiagnoseFragment) {
                    if (mBottomBar.getCurrentTabPosition() != 2)
                        mBottomBar.setDefaultTabPosition(2);
//                    mBottomBar.selectTabAtPosition(2);
                } else if (ft instanceof SettingFragment) {
                    if (mBottomBar.getCurrentTabPosition() != 3)
                        mBottomBar.setDefaultTabPosition(3);
//                    mBottomBar.selectTabAtPosition(3);
                }
                /*if (ft instanceof HomeFragment) {
                    if (mBottomBar.getCurrentTabPosition() != 1)
                        mBottomBar.setDefaultTabPosition(1);
//                    mBottomBar.selectTabAtPosition(0);
                }
                if (ft instanceof DiagnoseFragment) {
                    if (mBottomBar.getCurrentTabPosition() != 2)
                        mBottomBar.setDefaultTabPosition(2);
//                    mBottomBar.selectTabAtPosition(2);
                }
                if (ft instanceof SettingFragment) {
                    if (mBottomBar.getCurrentTabPosition() != 3)
                        mBottomBar.setDefaultTabPosition(3);
//                    mBottomBar.selectTabAtPosition(3);
                }*/
            }
        });

    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
//        mBottomBar.onSaveInstanceState();
    }

    private void initData(){

        SharedPreferences sp = getSharedPreferences("share", MODE_PRIVATE);
        boolean isFirstRun = sp.getBoolean("isFirstRun", true);
        System.out.println("isFirstRun : " + isFirstRun);
        SharedPreferences.Editor editor = sp.edit();
        if (isFirstRun) {
            SyncServerData();
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        } else {
            checkAndSendData();
        }

        /*home = findViewById(R.id.menu_home);
        health = findViewById(R.id.menu_health);
        data = findViewById(R.id.menu_data);
        doctor = findViewById(R.id.menu_doctor);
        setting = findViewById(R.id.menu_setting);

        homeimg = (ImageView)findViewById(R.id.img_home);
        dataimg = (ImageView)findViewById(R.id.img_data);

        data.setOnClickListener(this);
        health.setOnClickListener(this);
        home.setOnClickListener(this);
        doctor.setOnClickListener(this);
        setting.setOnClickListener(this);*/
    }
    private void SyncServerData() {
        Map<String, String> param = new HashMap<>();
        param.put("url", "bloodpressure");
        param.put("action", "get_data");
        param.put("type", "times");
        param.put("parameter1", "4");
        param.put("parameter2", "");
        param.put("user_id", MyApplication.getInstance().getUser_id());
        param.put("session_id", MyApplication.getInstance().getSession_id());
        System.out.println(MyApplication.getInstance().getUser_id());
        VolleyRequestImp myVolley = new VolleyRequestImp(param);
        myVolley.myVolleyRequestPressure_GET(this);
    }

    private void checkAndSendData() {
        DbProvider provider = new DbProvider();
        provider.init(getApplicationContext());
        List<Pressure> dataList = provider.getUnSendPressure();

        for (int i = 0; i < dataList.size(); i++) {
            Pressure p = dataList.get(i);
            Map<String, String> param = new HashMap<>();
            param.put("url", "bloodpressure");
            param.put("action", "upload_data");
            param.put("clientid", MyApplication.getInstance().getUser_id());
            param.put("SBP", String.valueOf(p.getHigh()));
            param.put("DBP", String.valueOf(p.getLow()));
            param.put("HR", String.valueOf(p.getRate()));
            param.put("measureTime", String.valueOf(p.getTime()));

            VolleyRequestImp volleyRequest = new VolleyRequestImp(param);
            volleyRequest.myVolleyRequestPressure_POST(this);
        }
        provider.database.close();

    }

    private void initService() {
        sdf = new SimpleDateFormat("yyyyMMdd");
        today = sdf.format(new Date());
        provider = new DbProvider();
        provider.init(getApplicationContext());

        step = provider.loadStep(today);

        if (step != null) {
            StepDetector.SENSITIVITY = 10;
            StepDetector.CURRENT_STEP = step.getNumber();
        } else {
            StepDetector.SENSITIVITY = 10;
            StepDetector.CURRENT_STEP = 0;
        }
        Intent intent = new Intent(MainActivity.this, StepService.class);
        this.startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!provider.database.isOpen()) {
            provider.init(getApplicationContext());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveDate();
        provider.database.close();
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    private void saveDate() {
        step = provider.loadStep(today);
        Log.e("runActivity_2", String.valueOf(StepDetector.CURRENT_STEP));
        if (step != null) {
            step.setNumber(StepDetector.CURRENT_STEP);
            provider.updateStep(step);
        } else {
            step = new Step();
            step.setNumber(StepDetector.CURRENT_STEP);
            step.setDate(today);
            provider.saveStep(step);
        }

    }

    /*public void onClick(View v) {
        switch(v.getId()){
            case R.id.menu_data:
                setTabSelection(0);
                data.setBackgroundColor(Color.rgb(220, 220, 220));
                break;

            case R.id.menu_health:
                setTabSelection(1);
                health.setBackgroundColor(Color.rgb(220, 220, 220));
                break;
            case R.id.menu_home:
                setTabSelection(2);
                home.setBackgroundColor(Color.rgb(220, 220, 220));
                break;
            case R.id.menu_doctor:
                setTabSelection(3);
                doctor.setBackgroundColor(Color.rgb(220, 220, 220));
                break;
            case R.id.menu_setting:
                setTabSelection(4);
                setting.setBackgroundColor(Color.rgb(220, 220, 220));
                break;
        }
    }*/



    public void switchContent(Fragment to, String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (mContent != null) {
            if (mContent != to) {

//            System.out.println(mBottomBar.getCurrentTabPosition());
//            mContentIdStack.push(mBottomBar.getCurrentTabPosition());

                if (!to.isAdded()) {
                    transaction.hide(mContent).add(R.id.content, to, tag).commit();
                } else {
                    transaction.hide(mContent).show(to).commit();
                }
//            transaction.replace(R.id.content, to);
                mContent = to;
//            transaction.addToBackStack(null);
//            transaction.commit();
            }
        } else {
            if (!to.isAdded()) {
                transaction.add(R.id.content, to, to.getTag()).commit();
            } else {
                transaction.show(to).commit();
            }
            mContent = to;
        }




    }
    private void setTabSelection(int index){
        currentIndex = index;

//        System.out.println(mContent);
//        mBottomBar.selectTabAtPosition(index);
        clearSelection();
//        bottomIdStack.push(mBottomBar.getCurrentTabPosition());
//        FragmentManager manager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = manager.beginTransaction();

        switch(index){
            case 0:
                if (dataFragment == null) {
                    dataFragment = new DataFragment();
                }

                switchContent(dataFragment, "dataFragment");
//                if (!dataListFragment.isAdded()) {
//                    fragmentTransaction.add(R.id.content, dataListFragment);
//                } else {
//
//                }

                break;
            case 1:
                if (healthFragment == null) {
                    healthFragment = new HealthFragment();
                }

                switchContent(healthFragment, "healthFragment");
//                fragmentTransaction.replace(R.id.content, homeFragment);
                break;
            case 2:
                /*doctorFragment = new DoctorFragment();
                fragmentTransaction.replace(R.id.content, doctorFragment);*/
                if (diagnoseFragment == null) {
                    diagnoseFragment = new DiagnoseFragment();
                }
                switchContent(diagnoseFragment, "diagnoseFragment");

//                fragmentTransaction.replace(R.id.content, diagnoseFragment);
                break;
            case 3:
                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                }
                switchContent(settingFragment, "settingFragment");

//                fragmentTransaction.replace(R.id.content, settingFragment);
                break;
            default:
                break;
        }

//        System.out.println("fr_isAddtoBackStack:" + fragmentTransaction.isAddToBackStackAllowed());
//        fragmentTransaction.addToBackStack(null);
////        System.out.println("fr_isAddtoBackStack:" + fragmentTransaction.isAddToBackStackAllowed());
//        fragmentTransaction.commit();

        /*bottomBarStack.push(mBottomBar.onSaveInstanceState());*/

    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandledFragment = selectedFragment;
    }


    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Fragment ft = getFragmentManager().findFragmentById(R.id.content);
            if (ft instanceof HomeFragment) {
//                mBottomBar.setDefaultTabPosition(1);
                mBottomBar.selectTabAtPosition(1);
            } else if (ft instanceof DataListFragment) {
//                mBottomBar.setDefaultTabPosition(1);
                mBottomBar.selectTabAtPosition(0);
            } else if (ft instanceof DiagnoseFragment) {
//                mBottomBar.setDefaultTabPosition(1);
                mBottomBar.selectTabAtPosition(2);
            } else if (ft instanceof SettingFragment) {
//                mBottomBar.setDefaultTabPosition(1);
                mBottomBar.selectTabAtPosition(3);
            }
            return true;
        }


        return super.onKeyDown(keyCode, event);
    }
*/
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        /*mContent = getFragmentManager().findFragmentById(R.id.content);

        if (mContent instanceof HomeFragment) {
            mBottomBar.setDefaultTabPosition(1);
        } else if (mContent instanceof DataListFragment) {
            mBottomBar.setDefaultTabPosition(0);
        } else if (mContent instanceof DiagnoseFragment) {
            mBottomBar.setDefaultTabPosition(2);
        } else if (mContent instanceof SettingFragment) {
            mBottomBar.setDefaultTabPosition(3);
        }*/
        /*try {
            Thread.sleep(1000);
        } catch (Exception e){
            e.printStackTrace();
        }*/


        /*Fragment ft = getFragmentManager().findFragmentById(R.id.content);
        if (ft instanceof HomeFragment) {
//                mBottomBar.setDefaultTabPosition(1);
            mBottomBar.selectTabAtPosition(1);
        } else if (ft instanceof DataListFragment) {
//                mBottomBar.setDefaultTabPosition(1);
            mBottomBar.selectTabAtPosition(0);
        } else if (ft instanceof DiagnoseFragment) {
//                mBottomBar.setDefaultTabPosition(1);
            mBottomBar.selectTabAtPosition(2);
        } else if (ft instanceof SettingFragment) {
//                mBottomBar.setDefaultTabPosition(1);
            mBottomBar.selectTabAtPosition(3);
        }*/





//        System.out.println("mContentId:" + mContentId);
//
//        mBottomBar.setDefaultTabPosition(mContentIdStack.pop());


        /*if (mBackHandledFragment == null || mBackHandledFragment.onBackPressed()) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }*/


//        int index = bottomIdStack.pop();
        /*
        if (index == currentIndex && !bottomIdStack.isEmpty()) {
            index = bottomIdStack.pop();
            mBottomBar.setDefaultTabPosition(index);
            setTabSelection(index);
        } else if (index != currentIndex) {
            mBottomBar.setDefaultTabPosition(index);
            setTabSelection(index);
        } else {
            super.onBackPressed();
        }*/
        /*if (getSupportFragmentManager() == null || bottomIdStack.isEmpty()) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }*/

//        if (!bottomIdStack.isEmpty()) {
//            int index = bottomIdStack.pop();
//            mBottomBar.setDefaultTabPosition(index);
//            setTabSelection(index);
//        } else {
//            super.onBackPressed();
//        }


//        mBottomBar.onSaveInstanceState();
//        super.onBackPressed();

       /* if (!bottomIdStack.empty()) {
            mBottomBar.selectTabAtPosition(bottomIdStack.pop());
        }*/
        /*if (!bottomBarStack.empty()) {
            mBottomBar.onRestoreInstanceState(bottomBarStack.pop());
        }*/

    }

    class MainActThread extends Thread{
        @Override
        public void run(){
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5 * 1000);
            HttpClient client = new DefaultHttpClient(httpParams);

            HttpPost httpPost = new HttpPost("http://114.212.190.79:12080/action.php");
            httpPost.setParams(httpParams);
        }
    }
    private void clearSelection(){
        /*data.setBackgroundColor(Color.rgb(255, 255, 255));
        health.setBackgroundColor(Color.rgb(255,255,255));
        home.setBackgroundColor(Color.rgb(255, 255, 255));
        doctor.setBackgroundColor(Color.rgb(255, 255, 255));
        setting.setBackgroundColor(Color.rgb(255, 255, 255));*/
    }
}
