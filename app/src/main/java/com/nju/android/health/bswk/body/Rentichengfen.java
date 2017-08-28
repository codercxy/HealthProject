package com.nju.android.health.bswk.body;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/3/9.
 */
public class Rentichengfen extends Activity {
    private ListView listView;
    private LayoutInflater inflater;
    private List<Map<String, Object>> data;
    Intent intent = new Intent();
    private String mem_account,mem_token;
    private FileService fileService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rentichengfen_list);
        fileService = new FileService(this);
        try {
            Map<String, String> map = fileService.getUserInfo("bswk.txt");
            mem_account = map.get("mem_account");
            mem_token = map.get("mem_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        setview();
        null_layout= (RelativeLayout) findViewById(R.id.null_layout);
        back();
    }

    public void back() {
        listView= (ListView) findViewById(R.id.rentishengfen_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //                    name="人体成分";
                intent.setClass(Rentichengfen.this, Rentichengrenxiangqing.class);
                intent.putExtra("dynamic_date", list_dy.get(position).getDynamic_date());
                intent.putExtra("dynamic_time", list_dy.get(position).getDynamic_time());
                intent.putExtra("remark", list_dy.get(position).getRemark());
                intent.putExtra("weight", list_dy.get(position).getWeight());
                intent.putExtra("fat", list_dy.get(position).getFat());
                intent.putExtra("boneMass", list_dy.get(position).getBoneMass());
                intent.putExtra("muscle", list_dy.get(position).getMuscle());
                intent.putExtra("visceral_fat", list_dy.get(position).getVisceral_fat());
                intent.putExtra("body_impedance", list_dy.get(position).getBody_impedance());
                intent.putExtra("quality_index", list_dy.get(position).getQuality_index());
                intent.putExtra("moisture", list_dy.get(position).getMoisture());
                intent.putExtra("heat", list_dy.get(position).getHeat());

                startActivity(intent);
            }
        });
        RelativeLayout back = (RelativeLayout) findViewById(R.id.back_relative);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rentichengfen.this.finish();
            }
        });
    }
    public void setview() {
        listView = (ListView) findViewById(R.id.rentishengfen_list);
    }
    private List<RentichengfenBean.DataEntity.DynamicRecordsEntity> list_dy = new ArrayList<>();
    private RelativeLayout null_layout;
    @Override
    protected void onResume() {
        super.onResume();
        String url = HttpInfo.PATH + HttpInfo.GETINFORMATION + "&mem_account="+mem_account+"&mem_token="+mem_token+"&type=6";
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new RentichengfenCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(Rentichengfen.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(RentichengfenBean response) {
                        list_dy.clear();
                        if(response.getData().getDynamic_records().size() != 0){
                            null_layout.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            for (int i = 0; i < response.getData().getDynamic_records().size(); i++) {
                                RentichengfenBean.DataEntity.DynamicRecordsEntity dy = response.getData().getDynamic_records().get(i);
                                RentichengfenBean.DataEntity.DynamicRecordsEntity dys = new RentichengfenBean.DataEntity.DynamicRecordsEntity();
                                String dynamic_date = dy.getDynamic_date();
                                String dynamic_time = dy.getDynamic_time();
                                String remark =dy.getRemark();

                                String weight=dy.getWeight();
                                String fat=dy.getFat();
                                String boneMass=dy.getBoneMass();
                                String muscle=dy.getMuscle();
                                String visceral_fat=dy.getVisceral_fat();
                                String body_impedance=dy.getBody_impedance();
                                String quality_index=dy.getQuality_index();
                                String moisture=dy.getMoisture();
                                String heat=dy.getHeat();
                                dys.setFat(fat);
                                dys.setWeight(weight);
                                dys.setRemark(remark);
                                dys.setBoneMass(boneMass);
                                dys.setMuscle(muscle);
                                dys.setVisceral_fat(visceral_fat);
                                dys.setBody_impedance(body_impedance);
                                dys.setQuality_index(quality_index);
                                dys.setHeat(heat);
                                dys.setMoisture(moisture);
                                dys.setDynamic_date(dynamic_date);
                                dys.setDynamic_time(dynamic_time);
                                list_dy.add(dys);
                            }
                            RentichengfenAdapter adapter = new RentichengfenAdapter();
                            listView.setAdapter(adapter);
                        }else {

                            null_layout.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public class RentichengfenAdapter extends BaseAdapter {

        public RentichengfenAdapter() {
            super();
            inflater = LayoutInflater.from(Rentichengfen.this);
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
            ViewHolder viewHolder = null;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.rentichengrenitem, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.year = (TextView) convertView
                        .findViewById(R.id.jk_item_text_year);
                viewHolder.time = (TextView) convertView
                        .findViewById(R.id.jk_item_text_time);
                viewHolder.shuifen = (TextView) convertView
                        .findViewById(R.id.jk_item_text_data_1);
                viewHolder.reliang = (TextView) convertView
                        .findViewById(R.id.jk_item_text_data_2);



                convertView.setTag(viewHolder);

            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            viewHolder.year.setText(list_dy.get(position).getDynamic_date());
            viewHolder.time.setText(list_dy.get(position).getDynamic_time());
            viewHolder.shuifen.setText(list_dy.get(position).getMoisture());
            viewHolder.reliang.setText(list_dy.get(position).getHeat());
            return convertView;
        }


        private class ViewHolder {
            private TextView year;
            private TextView shuifen;
            private TextView reliang;
            private TextView time;
        }

    }
}
