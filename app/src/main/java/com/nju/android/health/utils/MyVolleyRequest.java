package com.nju.android.health.utils;

/**
 * Created by chy on 2017/10/16.
 */
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nju.android.health.MyApplication;

import java.util.HashMap;
import java.util.Map;

public class MyVolleyRequest {
    private Context mContext;
    private Response.Listener<String> mListener;
    private Response.ErrorListener mErrorListener;
    private static int MY_TIMEOUT_MS = 500000;
    public MyVolleyRequest(Context context, final Callback callback) {

        mContext = context;

        mListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        };

        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        };
    }

    public void requestGet(String url, String tag) {

        //先取消已有的网络请求，避免重复请求
        MyApplication.getRequestQueue().cancelAll(tag);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, mListener, mErrorListener);
        stringRequest.setTag(tag);

        MyApplication.getRequestQueue().add(stringRequest);
        //启动该全局队列中的调度程序(dispatchers)
        MyApplication.getRequestQueue().start();
    }

    public void requestPost(String url, String tag, final Map<String, String> param) {

        MyApplication.getRequestQueue().cancelAll(tag);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, mListener, mErrorListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap localHashMap = new HashMap();
                localHashMap.put("user_id", MyApplication.getInstance().getUser_id());
                localHashMap.put("session_id", MyApplication.getInstance().getSession_id());

                return localHashMap;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return param;
            }

        };
        stringRequest.setTag(tag);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        MyApplication.getRequestQueue().add(stringRequest).setShouldCache(false);
        MyApplication.getRequestQueue().start();
    }

    public interface Callback {
        void onSuccess(String response);

        void onError(VolleyError error);
    }
}
