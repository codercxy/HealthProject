package com.nju.android.health.bswk.acid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.nju.android.health.R;
import com.nju.android.health.bswk.FileService;
import com.nju.android.health.bswk.HttpInfo;
import com.nju.android.health.bswk.XuetangBean;
import com.nju.android.health.bswk.XuetangCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class UricAcidActivity extends AppCompatActivity {
    private ListView listView;
    private LayoutInflater inflater;
    private List<Map<String, Object>> data;
    Intent intent = new Intent();

    private String mem_account,mem_token;
    private FileService fileService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uric_acid);
        setview();
        back();
        null_layout= (RelativeLayout) findViewById(R.id.null_layout);

        fileService = new FileService(this);
        try {
            Map<String, String> map = fileService.getUserInfo("bswk.txt");
            mem_account = map.get("mem_account");
            mem_token = map.get("mem_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mem_account.length() <= 0 || mem_token.length() <= 0) {
            Toast.makeText(UricAcidActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
        }

    }


    public void setview() {
        listView = (ListView) findViewById(R.id.niaosuan_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    public void back() {
        RelativeLayout back = (RelativeLayout) findViewById(R.id.back_relative);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public class NiaoSuanAdapter extends BaseAdapter {

        public NiaoSuanAdapter() {
            super();
            inflater = LayoutInflater.from(UricAcidActivity.this);
        }
        public int getCount() {
            return list_dy.size();
        }

        public Object getItem(int position) {
            return position;
        }
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            UricAcidActivity.NiaoSuanAdapter.ViewHolder viewHolder=null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_uricacid, parent, false);
                viewHolder=new UricAcidActivity.NiaoSuanAdapter.ViewHolder();
                viewHolder.year = (TextView) convertView
                        .findViewById(R.id.jk_item_text_year);
                viewHolder.time = (TextView) convertView
                        .findViewById(R.id.jk_item_text_time);
                viewHolder.shiduan = (TextView) convertView
                        .findViewById(R.id.jk_item_text_shiduan);
                viewHolder.niaosuan = (TextView) convertView
                        .findViewById(R.id.jk_item_text_niaosuan);
                convertView.setTag(viewHolder);

            }else {
                viewHolder= (UricAcidActivity.NiaoSuanAdapter.ViewHolder) convertView.getTag();
            }


            viewHolder.year.setText(list_dy.get(position).getDynamic_date());
            viewHolder.time.setText(list_dy.get(position).getDynamic_time());
            viewHolder.shiduan.setText(list_dy.get(position).getTime_interval());
            viewHolder.niaosuan.setText(list_dy.get(position).getNumerical_value());
            return convertView;
        }
        private class ViewHolder {
            private TextView year;
            private TextView niaosuan;
            private TextView shiduan;
            private TextView time;
            private RelativeLayout xiangqing;

        }

    }
    private RelativeLayout null_layout;
    private List<XuetangBean.DataEntity.DynamicRecordsEntity> list_dy = new ArrayList<>();
    @Override
    protected void onResume() {
        super.onResume();
        String url = HttpInfo.PATH + HttpInfo.GETINFORMATION + "&mem_account="+mem_account+"&mem_token="+mem_token+"&type=12";
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new XuetangCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(UricAcidActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(XuetangBean response) {
                        list_dy.clear();
                        if (response.getData().getDynamic_records().size() != 0) {
                            null_layout.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            for (int i = 0; i < response.getData().getDynamic_records().size(); i++) {
                                XuetangBean.DataEntity.DynamicRecordsEntity dy = response.getData().getDynamic_records().get(i);
                                XuetangBean.DataEntity.DynamicRecordsEntity dys = new XuetangBean.DataEntity.DynamicRecordsEntity();
                                String dynamic_date = dy.getDynamic_date();
                                String dynamic_time = dy.getDynamic_time();
                                String time_interval = dy.getTime_interval();
                                String numerical_value = dy.getNumerical_value();
                                String remark=dy.getRemark();
                                dys.setRemark(remark);
                                dys.setTime_interval(time_interval);
                                dys.setNumerical_value(numerical_value);
                                dys.setDynamic_date(dynamic_date);
                                dys.setDynamic_time(dynamic_time);
                                list_dy.add(dys);
                            }
                            UricAcidActivity.NiaoSuanAdapter adapter = new UricAcidActivity.NiaoSuanAdapter();
                            listView.setAdapter(adapter);
                        }else {

                            null_layout.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        }
                    }
                });
    }
}

