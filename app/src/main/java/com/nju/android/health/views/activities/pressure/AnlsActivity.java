package com.nju.android.health.views.activities.pressure;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.views.dialog.PressureAnlsDialog;

import java.util.Locale;

public class AnlsActivity extends AppCompatActivity {
    private final String TAG = "PreAnalyse";
    private TextView highText, lowText, rateText, diffText, resultText, timeText;
    private float diff, rate, high, low;
    private TextToSpeech mTextToSpeech = null;
    private ImageButton voice;

    private String analyse;
    private String presAnls[] = {"1、您本次测量，血压理想，请注意补充营养，适当运动。\n",
            "1、您本次测量，血压很理想，请继续保持。\n",
            "1、您本次测量血压很正常，但是相对偏高，还是需要多多注意，坚持测量哦~\n",
            "1、您本次测量血压偏高，可能是轻度高血压，一定要注意了！！！！\n",
            "1、您本次测量血压偏高，可能是中度高血压，一定要及时就医哦~\n",
            "1、您本次测量血压偏高，可能是重度高血压，一定要及时就医、按时服药，加油哦~\n",
            ""};
    private String rateAnls[] = {"2、您的心率略微偏慢，请注意。\n",
            "2、您的心率正常，请继续保持。\n",
            "2、您的心率过快，请注意。\n",
            "2、请输入正确的心率值\n"};
    private String diffAnls[] = {"3、脉压较小。\n", "3、脉压正常。\n", "3、脉压较大。\n", ""};

    private String get_time, get_high, get_low, get_rate;
    private int get_times;
    private int monitertime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pres_anls);

        initData();
        initView();

        initSpeech();

        setLook();
    }

    private void initData() {
        Intent intentpre = getIntent();
        Bundle bundle = intentpre.getBundleExtra("bundle");
        get_time = bundle.getString("pretime");
        get_high = bundle.getString("highpre");
        get_low = bundle.getString("lowpre");
        get_rate = bundle.getString("rate");
        get_times = bundle.getInt("times");
    }

    private void initView() {
        timeText = (TextView) findViewById(R.id.pres_anls_time);
        highText = (TextView) findViewById(R.id.pres_anls_high);
        lowText = (TextView) findViewById(R.id.pres_anls_low);
        rateText = (TextView) findViewById(R.id.pres_anls_rate);
        diffText = (TextView) findViewById(R.id.pres_anls_diff);
        resultText = (TextView) findViewById(R.id.pres_anls_result);
        voice = (ImageButton) findViewById(R.id.pres_anls_voice);


            timeText.setText(get_time);
            highText.setText(get_high);
            lowText.setText(get_low);
            rateText.setText(get_rate);

            diff = Float.parseFloat(get_high) - Float.parseFloat(get_low);
            diffText.setText(String.valueOf(diff));
            rate = Float.parseFloat(get_rate);
            high = Float.parseFloat(get_high);
            low = Float.parseFloat(get_low);
            monitertime = get_times;


    }

    private void initSpeech() {
        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS) {
                    //设置朗读语言
                    int supported = mTextToSpeech.setLanguage(Locale.US);
                    if ((supported != TextToSpeech.LANG_AVAILABLE) && (supported != TextToSpeech.LANG_COUNTRY_AVAILABLE)) {
                        displayToast("不支持当前语言！");
                    }
                }
            }

        });
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //朗读EditText里的内容
                mTextToSpeech.speak("高血压是" + highText.getText().toString() + "低血压是" + lowText.getText().toString() + "Heart rate is" + rateText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }

        });
    }

    private void setLook() {
        Bundle bundle = new Bundle();
        PressureAnlsDialog dialog = new PressureAnlsDialog();
        //血压
        if (high < 0 || low < 0 ) {
            analyse = presAnls[6];
            bundle.putString("text", "请输入正确的血压值。");
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), PressureAnlsDialog.class.getSimpleName());

        } else if (high < 120 && low < 80) {
            //理想

            analyse = presAnls[0];
            bundle.putString("text", "您本次测量，血压很理想。");
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), PressureAnlsDialog.class.getSimpleName());

        } else if (high < 130 && low < 85) {
            //正常
            analyse = presAnls[1];

            bundle.putString("text", "您本次测量，血压正常。");
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), PressureAnlsDialog.class.getSimpleName());

        } else if (high >= 130 && high < 140 && low >= 85 && low < 89) {
            //正常高

            analyse = presAnls[2];

            bundle.putString("text", "您本次测量，血压正常，但是相对偏高哦，请多多注意。");
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), PressureAnlsDialog.class.getSimpleName());

        } else if (high >= 140 && high < 160 && low >= 90 && low < 100) {
            //1级高压

            analyse = presAnls[3];

            bundle.putString("text", "您本次测量，血压处于轻度高压状态，请多多注意。");
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), PressureAnlsDialog.class.getSimpleName());

        }else if (high >= 160 && high < 180 && low >= 100 && low < 110) {
            //2级高压

            analyse = presAnls[4];
            bundle.putString("text", "您现在是中度高血压，建议您及时就医！");
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), PressureAnlsDialog.class.getSimpleName());

        } else if (high >= 180 && low >= 110) {
            //3级高压

            analyse = presAnls[5];
            bundle.putString("text", "您现在是重度高血压，建议及时就医，按时服药！");
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), PressureAnlsDialog.class.getSimpleName());

        } else if (high >= 140 && low <90) {
            //单纯收缩性高血压
            analyse = "1、您的血压处于单纯收缩性高血压状态，请及时向医生咨询。\n";
            bundle.putString("text", "您现在是单纯收缩性高血压，请及时向医生咨询。");
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), PressureAnlsDialog.class.getSimpleName());

        } else if (high >= 90 && high <= 140 && low < 90) {
            //临界高血压
            analyse = "1、您的血压处于临界高血压状态，请及时向医生咨询。\n";
            bundle.putString("text", "您现在是处于临界高血压状态，需要在医生指导下进行调理。");
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), PressureAnlsDialog.class.getSimpleName());

        } else if (high < 220 || low < 130){
            //无此情况
            analyse = "1、暂无此类血压记录，请及时就诊。\n";
            bundle.putString("text", "暂无此类血压情况。");
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), PressureAnlsDialog.class.getSimpleName());
        } else {
            analyse = presAnls[6];
            bundle.putString("text", "请输入正确的血压值。");
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), PressureAnlsDialog.class.getSimpleName());
        }

        //心率
        if (rate < 60 && rate > 0) {
            Log.e("rate_here", String.valueOf(rate));

            analyse += rateAnls[0];
        } else if (rate >= 60 && rate <= 100) {

            analyse += rateAnls[1];
        } else if (rate < 220){

            analyse += rateAnls[2];
        } else  {
            analyse += rateAnls[3];
        }

        //脉压差
        if (diff < 20) {
            analyse += diffAnls[0];
        } else if (diff >= 20 && diff <= 60) {
            analyse += diffAnls[1];
        } else {
            analyse += diffAnls[2];
        }
        resultText.setText(analyse);


    }

    private void setDiscription(String disc, int loc) {

    }

    protected void displayToast(String string) {
        // TODO Auto-generated method stub

    }
}
