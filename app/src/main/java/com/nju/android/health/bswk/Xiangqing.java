package com.nju.android.health.bswk;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.nju.android.health.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/3/21.
 */
public class Xiangqing extends Activity {


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

    public static int forNum = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiangqing);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String Dynamic_max_bp = intent.getStringExtra("max_blood_pressure");
        String Dynamic_heart_rate = intent.getStringExtra("heart_rate");
        String Dynamic_min_bp = intent.getStringExtra("min_blood_pressure");
        System.out.println("dynamic_max_bp=" + Dynamic_max_bp);
        tishi_ima = (ImageView) findViewById(R.id.tishi_image);

        int gaoya = Integer.valueOf(Dynamic_max_bp);
        int diya = Integer.valueOf(Dynamic_min_bp);

        if(180.0 <= gaoya || 110.0 <= diya){
            dian1Data1.setText("重度高血压");
            dian2Text1.setText("建议您尽快到医院就医，调整血压，同时检查有无并发症的发生。");
            dian3Text1.setText("");
            relativeLayoutJianyi.setVisibility(View.INVISIBLE);
            tishi_ima.setVisibility(View.GONE);
            shiButton.setVisibility(View.GONE);
            fouButton.setVisibility(View.GONE);
            dian7Text.setVisibility(View.GONE);
        }else if((160.0 <= gaoya && gaoya <= 179.0) || (100.0 <= diya && diya <= 109.0)){
            dian1Data1.setText("中度高血压");
            dian2Text1.setText("连续监测7天以上如无变化建议您增加锻炼");
            dian3Text1.setText("低盐饮食.多吃鲜果蔬菜.减少脂肪的摄入.（油.动物脂肪）戒烟酒.保持好心态。同时要就医选择适当的药物治疗");
            dian7Text.setText("是否每天提醒一次测量血压");
        }else if((140.0 <= gaoya && gaoya <= 159.0) || (90.0 <= diya && diya <= 99.0)){
            dian1Data1.setText("轻度高血压");
            dian2Text1.setText("请每半小时检测一次频率监测");
            dian3Text1.setText("3次以上如无变化，建议您控制体重，适当增加锻炼。低盐饮食，戒烟限酒，保持良好的心态。(如果连续多次出现此种情况，建议治疗)");
            dian7Text.setText("是否每半小时提醒一次测量血压");
        }else if(90.0 > gaoya || 60.0 > diya){
            dian1Data1.setText("血压偏低");
            dian2Text1.setText("如有疲乏无力、头晕头痛，视物黑朦，甚至晕厥，或冷汗、心悸等症状，建议您到医院做全面检查。");
            dian3Text1.setText("如无上述症状建议您高营养、富含蛋白质饮食，适当提高饮水量适当参加体育锻炼，增加心肺功能。");
            tishi_ima.setVisibility(View.GONE);
            shiButton.setVisibility(View.GONE);
            fouButton.setVisibility(View.GONE);
            dian7Text.setVisibility(View.GONE);
        }else if((131.0 <= gaoya && gaoya <= 139.0) || (85.0 <= diya && diya <= 89.0)){
            dian1Data1.setText("正常的高值血压");
            dian1Text1.setTextColor(Color.BLUE);
            dian2Text1.setText("请注意调整饮食和作息习惯!");
            dian3Text1.setText("");
            relativeLayoutJianyi.setVisibility(View.INVISIBLE);
            tishi_ima.setVisibility(View.GONE);
            shiButton.setVisibility(View.GONE);
            fouButton.setVisibility(View.GONE);
            dian7Text.setVisibility(View.GONE);
        }else if(90.0 <= gaoya && gaoya <= 130.0 && 60.0 <= diya && diya <= 84.0){
            dian1Data1.setText("血压正常");
            dian1Text1.setTextColor(Color.BLUE);
            dian2Text1.setText("你的血压正常,请继续保持！");
            dian3Text1.setText("");
            relativeLayoutJianyi.setVisibility(View.INVISIBLE);
            tishi_ima.setVisibility(View.GONE);
            shiButton.setVisibility(View.GONE);
            fouButton.setVisibility(View.GONE);
            dian7Text.setVisibility(View.GONE);
        }else{
            dian1Data1.setText("血压不准确");
            dian2Text1.setText("建议您重新测量。");
            dian3Text1.setText("");
            relativeLayoutJianyi.setVisibility(View.INVISIBLE);
            tishi_ima.setVisibility(View.GONE);
            shiButton.setVisibility(View.GONE);
            fouButton.setVisibility(View.GONE);
            dian7Text.setVisibility(View.GONE);
        }
        if(gaoya - diya >= 60.0){
            relativeLayoutJianyi.setVisibility(View.VISIBLE);
            dian3Text1.setText(dian3Text1.getText().toString()+"\n" +
                    "压差过大，存在动脉硬化的风险，请操作多次测量，测量时保持心态平和，测量间隔5分钟再次测量，如持续增高请咨询医师。");
        }else if(gaoya - diya <= 20.0){
            relativeLayoutJianyi.setVisibility(View.VISIBLE);
            dian3Text1.setText(dian3Text1.getText().toString()+"\n" +
                    "压差过小，存在心力衰竭的风险，请操作多次测量，测量时保持心态平和，测量间隔5分钟再次测量，如持续变小请咨询医师。");
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

        Resources res = getBaseContext().getResources();
        Drawable dra = res.getDrawable(R.drawable.xiangqingqueding_bai);
        switch (view.getId()) {
            case R.id.shi_button:
                if ("轻度高血压，".equals(dian1Data1.getText().toString().trim())) {

                    // 获得AlarmManager实例,注意这里并不是new一个对象，Alarmmanager为系统级服务
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    // 实例化Intent
                    Intent intent = new Intent(Xiangqing.this, MyReceiver.class);
                    intent.setAction("qingdu");
                    //定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
                    PendingIntent sender = PendingIntent.getBroadcast(Xiangqing.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    //开始时间
                    long firstime= SystemClock.elapsedRealtime();
                    //  startService(intent);
                    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstime+30*60*1000,30*60*1000,sender);

                    Toast.makeText(Xiangqing.this, "半小时后将给您发送通知提示", Toast.LENGTH_LONG).show();
                } else if ("中度高血压，".equals(dian1Data1.getText().toString().trim())) {

                    // 获得AlarmManager实例,注意这里并不是new一个对象，Alarmmanager为系统级服务
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    // 实例化Intent
                    Intent intent = new Intent(Xiangqing.this, MyReceiver.class);
                    intent.setAction("zhongdu");
                    //定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
                    PendingIntent sender = PendingIntent.getBroadcast(Xiangqing.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    //开始时间
                    long firstime= SystemClock.elapsedRealtime();
                    //  startService(intent);
                    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstime+24*60*60*1000,24*60*60*1000,sender);

                    Toast.makeText(Xiangqing.this, "一天后将给您发送通知提示", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.fou_button:
                Toast.makeText(Xiangqing.this, "已取消发送通知提示", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Xiangqing.this, ServerPushService.class);
                stopService(intent);
                break;
            case R.id.fanhui:
                finish();
                break;
            case R.id.zixunyisheng:
                android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(Xiangqing.this);  //先得到构造器
                builder.setTitle("提示"); //设置标题
                builder.setMessage("拨打该电话按照正常资费由运营商收取：025-86756188"); //设置内容
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //关闭dialog
//                        Handler handler = new Handler();
//                        handler.post(new TimerTask() {
//                            @Override
//                            public void run() {
//                                //拨打电话，因为线程安全问题，子线程操作
//                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:025-86756188"));
//                                if (ActivityCompat.checkSelfPermission(Xiangqing.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                                    // TODO: Consider calling
//                                    //    ActivityCompat#requestPermissions
//                                    // here to request the missing permissions, and then overriding
//                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                    //                                          int[] grantResults)
//                                    // to handle the case where the user grants the permission. See the documentation
//                                    // for ActivityCompat#requestPermissions for more details.
//                                    return;
//                                }
//                                startActivity(intent);
//                            }
//                        });
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //参数都设置完成了，创建并显示出来
                builder.create().show();
                break;
        }
    }
}
