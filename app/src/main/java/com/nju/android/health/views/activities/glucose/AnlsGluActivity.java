package com.nju.android.health.views.activities.glucose;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.views.dialog.GluAnlsDialog;

public class AnlsGluActivity extends AppCompatActivity {
    private final String TAG = "GluAnalyse";

    private TextView valueText, resultText, timeText, typeText;
    private ImageView look_1, look_2, look_3;
    private float value;

    private String getTime, getType, getValue;

    private String analyse;
    private String emptyText[] = {"您的空腹血糖值偏低",
                    "空腹血糖正常",
                    "空腹血糖值偏高，可能患有糖尿病",
                    ""};
    private String fullText[] = {"您的餐后血糖值正常",
            "您的血糖偏高，请注意控制饮食",
            "您可能患有糖尿病，请及时到医院检查",
            ""};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anls_glu);

        initData();

        initView();

        setLook();
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        getTime = bundle.getString("gluTime");
        getType = bundle.getString("gluType");
        getValue = bundle.getString("value");
    }

    private void initView() {
        timeText = (TextView) findViewById(R.id.glu_anls_time);
        typeText = (TextView) findViewById(R.id.glu_anls_type);
        valueText = (TextView) findViewById(R.id.glu_anls_value);

        resultText = (TextView) findViewById(R.id.glu_anls_result);

        look_1 = (ImageView) findViewById(R.id.glu_anls_img1);
        look_2 = (ImageView) findViewById(R.id.glu_anls_img2);
        look_3 = (ImageView) findViewById(R.id.glu_anls_img3);

        timeText.setText(getTime);
        typeText.setText(getType);
        valueText.setText(getValue);

        value = Float.parseFloat(getValue);
    }

    private void setLook() {
        Bundle bundle = new Bundle();

        GluAnlsDialog dialog = new GluAnlsDialog();
        switch (getType) {
            case "空腹":
                if (value < 2.8 && value > 0) {
                    look_1.setImageResource(R.drawable.look_rate_1);
                    analyse = emptyText[0];
                    bundle.putString("text", "您本次测量，血糖值偏低。");
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), GluAnlsDialog.class.getSimpleName());

                } else if (value < 7) {
                    look_2.setImageResource(R.drawable.look_rate_2);
                    analyse = emptyText[1];
                    bundle.putString("text", "您本次测量，血糖值很理想。");
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), GluAnlsDialog.class.getSimpleName());

                } else if (value < 20){
                    look_3.setImageResource(R.drawable.look_rate_4);
                    analyse = emptyText[2];
                    bundle.putString("text", "请注意，您本次测量，血糖值偏高。");
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), GluAnlsDialog.class.getSimpleName());

                } else {
                    analyse = emptyText[3];
                    bundle.putString("text", "请输入正常血糖值");
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), GluAnlsDialog.class.getSimpleName());
                }

                break;
            case "餐后2小时":

                if (value < 7.8 && value > 0) {
                    look_1.setImageResource(R.drawable.look_rate_2);
                    analyse = fullText[0];
                    bundle.putString("text", "您本次测量，血糖值很理想。");
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), GluAnlsDialog.class.getSimpleName());
                } else if (value < 11) {
                    look_2.setImageResource(R.drawable.look_rate_5);
                    analyse = fullText[1];
                    bundle.putString("text", "您本次测量，血糖值偏高。");
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), GluAnlsDialog.class.getSimpleName());

                } else if (value < 20){
                    look_3.setImageResource(R.drawable.look_rate_4);
                    analyse = fullText[2];
                    bundle.putString("text", "请注意，您可能患有糖尿病");
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), GluAnlsDialog.class.getSimpleName());

                } else {
                    analyse = emptyText[3];
                    bundle.putString("text", "请输入正常血糖值");
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), GluAnlsDialog.class.getSimpleName());

                }
                break;
            default:
                break;

        }
        resultText.setText(analyse);
    }

}
