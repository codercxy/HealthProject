package com.nju.android.health.views.activities;



import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.nju.android.health.R;
import com.nju.android.health.model.data.Step;
import com.nju.android.health.providers.DbProvider;
import com.nju.android.health.service.StepDetector;
import com.nju.android.health.service.StepService;
import com.nju.android.health.utils.BackHandledFragment;
import com.nju.android.health.utils.BackHandledInterface;
import com.nju.android.health.views.fragments.data.DataFragment;
import com.nju.android.health.views.fragments.data.DataListFragment;
import com.nju.android.health.views.fragments.doctor.DiagnoseFragment;
import com.nju.android.health.views.fragments.doctor.DoctorFragment;
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
import java.util.Stack;


public class MainActivity extends AppCompatActivity implements BackHandledInterface{

    /*private View home;
    private View health;
    private View data;
    private View doctor;
    private View setting;*/

    private HomeFragment homeFragment;
    private DataFragment dataFragment;
    private DoctorFragment doctorFragment;
    private SettingFragment settingFragment;
    private DiagnoseFragment diagnoseFragment;

    private Fragment mContent;
    private Stack<Integer> mContentIdStack;

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
    private Stack<Parcelable> bottomBarStack;
    private Stack<Integer> bottomIdStack;
    private int currentIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        bottomBarStack = new Stack<Parcelable>();
        bottomIdStack = new Stack<>();

        mContentIdStack = new Stack<>();

//        mActionBar = getSupportActionBar();
//        mActionBar.hide();
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBarTab = mBottomBar.getTabWithId(R.id.bottomBarItemData);
        bottomBarTab.setSelected(true);

        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                bottomIdStack.push(currentIndex);
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

        initView();
        initService();
        Intent intent = getIntent();

        bottomIdStack = new Stack<>();

        if(intent != null && savedInstanceState == null){
            mContent = null;
            currentIndex = intent.getIntExtra("tabIndex", 1);
            if (currentIndex == 0) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, new DataListFragment())
                        .addToBackStack(null)
                        .commit();
            } else {
                setTabSelection(currentIndex);
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
                } else if (ft instanceof HomeFragment) {
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

    private void initView(){


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

    private void initService() {
        sdf = new SimpleDateFormat("yyyyMMdd");
        today = sdf.format(new Date());
        provider = new DbProvider();
        provider.init(this);

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
    public void onPause() {
        super.onPause();
        saveDate();
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        saveDate();
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

    public void switchContent(Fragment from, Fragment to, int id) {
        if (mContent != to) {
            mContent = to;
//            System.out.println(mBottomBar.getCurrentTabPosition());
//
//            mContentIdStack.push(mBottomBar.getCurrentTabPosition());
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            /*if (from != null) {
                transaction.hide(from);
            }

            if (!to.isAdded()) {
                transaction.add(R.id.content, to);
            } else {
                transaction.show(to);
            }*/
            transaction.replace(R.id.content, to);

            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
    private void setTabSelection(int index){
        currentIndex = index;
        mContent = getFragmentManager().findFragmentById(R.id.content);

//        System.out.println(mContent);
//        mBottomBar.selectTabAtPosition(index);
        clearSelection();
//        bottomIdStack.push(mBottomBar.getCurrentTabPosition());
//        FragmentManager manager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = manager.beginTransaction();

        switch(index){
            case 0:
                dataFragment = new DataFragment();
                switchContent(mContent, dataFragment, 0);
//                if (!dataListFragment.isAdded()) {
//                    fragmentTransaction.add(R.id.content, dataListFragment);
//                } else {
//
//                }

                break;
            case 1:
                homeFragment = new HomeFragment();
                switchContent(mContent, homeFragment, 1);
//                fragmentTransaction.replace(R.id.content, homeFragment);
                break;
            case 2:
                /*doctorFragment = new DoctorFragment();
                fragmentTransaction.replace(R.id.content, doctorFragment);*/
                diagnoseFragment = new DiagnoseFragment();
                switchContent(mContent, diagnoseFragment, 2);

//                fragmentTransaction.replace(R.id.content, diagnoseFragment);
                break;
            case 3:
                settingFragment = new SettingFragment();
                switchContent(mContent, settingFragment, 3);

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
