package com.nju.android.health.bswk;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.nju.android.health.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/3/23.
 */
public class XuetangAddxiangqing extends Activity {
    @Bind(R.id.imageView5)
    ImageView imageView5;
    @Bind(R.id.back_relative)
    RelativeLayout backRelative;
    @Bind(R.id.textView8)
    TextView textView8;
    @Bind(R.id.xiangqingbiaoti_1)
    RelativeLayout xiangqingbiaoti1;
    @Bind(R.id.textView45)
    TextView textView45;
    @Bind(R.id.dian1)
    CircleImageView dian1;
    @Bind(R.id.dian1_text1)
    TextView dian1Text1;
    @Bind(R.id.dian1_data1)
    TextView dian1Data1;
    @Bind(R.id.relativeLayout_jieguo)
    RelativeLayout relativeLayoutJieguo;
    @Bind(R.id.dian2)
    CircleImageView dian2;
    @Bind(R.id.dian2_text1)
    TextView dian2Text1;
    @Bind(R.id.relativeLayout_jiance)
    RelativeLayout relativeLayoutJiance;
    @Bind(R.id.dian3)
    CircleImageView dian3;
    @Bind(R.id.dian3_text1)
    TextView dian3Text1;
    @Bind(R.id.relativeLayout_jianyi)
    RelativeLayout relativeLayoutJianyi;
    @Bind(R.id.dian7_text)
    TextView dian7Text;
    @Bind(R.id.shi_button)
    Button shiButton;
    @Bind(R.id.fou_button)
    Button fouButton;
    @Bind(R.id.beijing1)
    RelativeLayout beijing1;
    @Bind(R.id.lvxian)
    ImageView lvxian;
    @Bind(R.id.fanhui)
    TextView fanhui;
    @Bind(R.id.zixunyisheng)
    TextView zixunyisheng;
    ImageView tishi_ima;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xueyangxiangqing_add);
        ButterKnife.bind(this);
        tishi_ima = (ImageView) findViewById(R.id.tishi_image);
        Intent intent = getIntent();
        String numerical_value = intent.getStringExtra("numerical_value");
        String time_interval = intent.getStringExtra("time_interval");
        double i = Double.parseDouble(numerical_value);

        if ("空腹".equals(intent.getStringExtra("time_interval"))) {
            if (i < 6.1 && 2.8 < i) {
                dian1Data1.setText("血糖正常，");
                dian1Text1.setTextColor(Color.BLUE);
                dian2Text1.setText("请继续保持良好的饮食和作息习惯，保持良好的心态");
                relativeLayoutJianyi.setVisibility(View.INVISIBLE);
                tishi_ima.setVisibility(View.GONE);
                shiButton.setVisibility(View.GONE);
                fouButton.setVisibility(View.GONE);
                dian7Text.setVisibility(View.GONE);
            } else if (i > 7.0) {
                dian1Data1.setText("您的血糖偏高，");
                dian1Text1.setTextColor(Color.RED);
                dian2Text1.setText("建议连续监测3天以上，如有三多一少即多尿.多饮.多食和体重减轻的症状，您可能已患有糖尿病，");
                dian3Text1.setText("如无以上症状血糖值不变的话 ，建议立即到医院详细检查原因，并在医生的指导下合理使用降糖药。");
                tishi_ima.setVisibility(View.GONE);
                shiButton.setVisibility(View.GONE);
                fouButton.setVisibility(View.GONE);
                dian7Text.setVisibility(View.GONE);
            } else if (i <= 2.8) {
                dian1Data1.setText("您的血糖偏低，");
                dian1Text1.setTextColor(Color.RED);
                dian2Text1.setText("您是否过量服用降糖药物、未按时进食或进食过少，长期剧烈运动，酒精摄入，");
                dian3Text1.setText("如无以上症状血糖值不变的话 ，建议立即到医院详细检查原因，并在医生的指导下合理使用降糖药。(注：接受药物治疗的糖尿病患者只要血糖水平≤3.9mmol/L就属低血糖范畴)");
                tishi_ima.setVisibility(View.GONE);
                shiButton.setVisibility(View.GONE);
                fouButton.setVisibility(View.GONE);
                dian7Text.setVisibility(View.GONE);
            }else if (i <= 7.0&&6.1<=i) {
                dian1Data1.setText("空腹血糖受损，");
                dian1Text1.setTextColor(Color.RED);
                dian2Text1.setText("您为糖尿病前期，建议患者控制体重、饮食控制，适当运动，来减少发生糖尿病的风险；");
                dian3Text1.setText("定期检查血糖；同时密切关注心血管疾病危险因素（如吸烟、高血压和 血脂紊乱等），并给予适当治疗。");
                tishi_ima.setVisibility(View.GONE);
                shiButton.setVisibility(View.GONE);
                fouButton.setVisibility(View.GONE);
                dian7Text.setVisibility(View.GONE);
            } else if (i <= 7.0) {
                dian1Data1.setText("糖耐量降低，");
                dian1Text1.setTextColor(Color.RED);
                dian2Text1.setText("您为糖尿病前期，建议患者控制体重、饮食控制，适当运动，来减少发生糖尿病的风险；");
                dian3Text1.setText("定期检查血糖；同时密切关注心血管疾病危险因素（如吸烟、高血压和 血脂紊乱等），并给予适当治疗。");
                tishi_ima.setVisibility(View.GONE);
                shiButton.setVisibility(View.GONE);
                fouButton.setVisibility(View.GONE);
                dian7Text.setVisibility(View.GONE);
            }else {
                dian1Data1.setText("暂无分析数据，");
                relativeLayoutJianyi.setVisibility(View.INVISIBLE);
                relativeLayoutJieguo.setVisibility(View.INVISIBLE);
                tishi_ima.setVisibility(View.GONE);
                shiButton.setVisibility(View.GONE);
                fouButton.setVisibility(View.GONE);
                dian7Text.setVisibility(View.GONE);
            }
        }

        if ("餐后".equals(intent.getStringExtra("time_interval"))) {
            if (i < 7.8&&2.8<=i) {
                dian1Data1.setText("血糖可能受损，");
                dian1Text1.setTextColor(Color.RED);
                dian2Text1.setText("您为糖尿病前期，建议患者控制体重、饮食控制，适当运动，来减少发生糖尿病的风险；");
                dian3Text1.setText("定期检查血糖；同时密切关注心血管疾病危险因素（如吸烟、高血压和 血脂紊乱等），并给予适当治疗。");
                tishi_ima.setVisibility(View.GONE);
                shiButton.setVisibility(View.GONE);
                fouButton.setVisibility(View.GONE);
                dian7Text.setVisibility(View.GONE);
            } else if (11.1<=i) {
                dian1Data1.setText("血糖偏高，");
                dian1Text1.setTextColor(Color.BLUE);
                dian2Text1.setText("如有三多一少即多尿.多饮.多食和体重减轻的症状，您可能已患有糖尿病，");
                dian3Text1.setText("如无以上症状,请连续检测三天以上，同时增加蔬菜进食量，减少酒精和单糖摄入，出现症状建议到医院做详细检查，并听从医生的指导");
                tishi_ima.setVisibility(View.GONE);
                shiButton.setVisibility(View.GONE);
                fouButton.setVisibility(View.GONE);
                dian7Text.setVisibility(View.GONE);
            } else if (i <= 2.8) {
                dian1Data1.setText("您的血糖偏低，");
                dian1Text1.setTextColor(Color.RED);
                dian2Text1.setText("您是否过量服用降糖药物、未按时进食或进食过少，长期剧烈运动，酒精摄入，");
                dian3Text1.setText("如无以上症状血糖值不变的话 ，建议立即到医院详细检查原因，并在医生的指导下合理使用降糖药。(注：接受药物治疗的糖尿病患者只要血糖水平≤3.9mmol/L就属低血糖范畴)");
                tishi_ima.setVisibility(View.GONE);
                shiButton.setVisibility(View.GONE);
                fouButton.setVisibility(View.GONE);
                dian7Text.setVisibility(View.GONE);
            }else if (i <= 11.1&&7.8<=i) {
                dian1Data1.setText("糖耐量降低，");
                dian1Text1.setTextColor(Color.RED);
                dian2Text1.setText("您为糖尿病前期，建议患者控制体重、饮食控制，适当运动，来减少发生糖尿病的风险；");
                dian3Text1.setText("定期检查血糖；同时密切关注心血管疾病危险因素（如吸烟、高血压和 血脂紊乱等），并给予适当治疗。");
                tishi_ima.setVisibility(View.GONE);
                shiButton.setVisibility(View.GONE);
                fouButton.setVisibility(View.GONE);
                dian7Text.setVisibility(View.GONE);
            }
            else {
                dian1Data1.setText("暂无分析数据，");
                dian2Text1.setText("请使用专业测量仪器。认真填写测量数据，仔细核对是否有错误。");
                relativeLayoutJianyi.setVisibility(View.INVISIBLE);

                tishi_ima.setVisibility(View.GONE);
                shiButton.setVisibility(View.GONE);
                fouButton.setVisibility(View.GONE);
                dian7Text.setVisibility(View.GONE);
            }
        }
        back();

    }


    private void back() {
        backRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.shi_button, R.id.fou_button, R.id.fanhui, R.id.zixunyisheng})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shi_button:
                break;
            case R.id.fou_button:
                break;
            case R.id.fanhui:
                finish();
                break;
            case R.id.zixunyisheng:
                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(XuetangAddxiangqing.this);
                //    设置Title的内容
                builder.setTitle("温馨提示");
                //    设置Content来显示一个信息
                builder.setMessage("拨打电话按正常资费由运营商收取：025-024555454");
                //    设置一个PositiveButton
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Uri uri= Uri.parse("tel:025-024555454");
                        Intent intent=new Intent();
                        intent.setData(uri);
                        XuetangAddxiangqing.this.startActivity(intent);
                    }
                });
                //    设置一个NegativeButton
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                });
                //    显示出该对话框
                builder.show();
                break;
            default:
                break;
        }
    }
}
