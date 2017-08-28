package com.nju.android.health.views.activities.pressure;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nju.android.health.R;

public class AlarmAlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AlertDialog.Builder(AlarmAlertActivity.this)
                .setIcon(R.drawable.img_clock)
                .setTitle("闹钟响了!!")
                .setMessage("到时间测量了，亲！！！")
                .setNegativeButton("知道啦", null)
                .setPositiveButton("去测量",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        Intent intent = new Intent(AlarmAlertActivity.this,SaveDaymonitorActivity.class);
                        startActivity(intent) ;
                        AlarmAlertActivity.this.finish();
                    }

                })
                .show();
    }
}
