package com.nju.android.health.bswk.body;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/15.
 */
public abstract class RentichengfenCallback extends Callback<RentichengfenBean> {
    @Override
    public RentichengfenBean parseNetworkResponse(Response response) throws Exception {
        String string = response.body().string();
        RentichengfenBean renti = new Gson().fromJson(string,RentichengfenBean.class);
        return renti;
    }
}
