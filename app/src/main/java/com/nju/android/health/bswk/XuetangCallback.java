package com.nju.android.health.bswk;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/16.
 */
public class XuetangCallback extends Callback<XuetangBean> {
    @Override
    public XuetangBean parseNetworkResponse(Response response) throws Exception {
        String string = response.body().string();
        XuetangBean xuetangBean = new Gson().fromJson(string, XuetangBean.class);
        return xuetangBean;
    }

    @Override
    public void onError(Call call, Exception e) {

    }

    @Override
    public void onResponse(XuetangBean response) {

    }
}
