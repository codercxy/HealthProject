package com.nju.android.health.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.nju.android.health.MyApplication;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chy on 2017/10/16.
 */

public class VolleyRequestImp {
    public static final String VOLLEY_TAG = "tag_volley_request";
    private static int DEFAULT_TIMEOUT_MS = 10000;
    private static int DEFAULT_MAX_RETRIES = 3;

    public static final String JUHE_API_URL = "https://github.com/VDerGoW/AndroidMain/blob/master/volley-demo/src/main/java/tips/volleytest/VolleyDemoActivity.java";

    private String url = "http://114.212.190.79:12080/action.php";

    private Map<String, String> param = new HashMap<>();
    public VolleyRequestImp(Map<String, String> p) {
        param = p;

    }

    public void myVolleyRequestDemo_POST(Context context) {
        MyVolleyRequest myVolleyRequest = new MyVolleyRequest(context, new MyVolleyRequest.Callback() {
            @Override
            public void onSuccess(String response) {
                Log.i("### onSuccess", "POST_MyVolleyRequest" + response);
                System.out.println("volley success");
            }

            @Override
            public void onError(VolleyError error) {
                error.printStackTrace();
                System.out.println("volley error:" + error);
            }
        });

        myVolleyRequest.requestPost(url, "my_post_" + VOLLEY_TAG, param);
    }

    public void volleyJsonObjectRequestDome_POST() {


        //产生JsonObject类型的参数
        JSONObject jsonParam = new JSONObject(param);

        // TODO: 错误的请求KEY!
        Log.i("DEBUG ###", jsonParam.toString());
        //DEBUG ###: {"postcode":"210044","key":"effd958a513778eadd21f8d29a675644"}

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParam, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // TODO: 处理返回结果
                Log.i("### onResponse", "POST_JsonObjectRequest:" + response.toString());
                System.out.println("volley json success");
                //POST_JsonObjectRequest:{"error_code":10001,"result":null,"reason":"错误的请求KEY!","resultcode":"101"}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: 处理错误
                Log.e("### onErrorResponse", "POST_JsonObjectRequest:" + error.toString());
                System.out.println("volley json error : " + error.toString());
            }
        });

        jsonObjectRequest.setTag(VOLLEY_TAG);//JsonObjectRequestTest_POST
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        MyApplication.getRequestQueue().add(jsonObjectRequest);
    }

    /**
     * Volley的StringRequest使用示例
     * HTTP method : POST
     * 内部注释和方法volleyStringRequestDemo_GET()相同
     */
    public void volleyStringRequestDome_POST() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // TODO: 处理返回结果
                Log.i("### onResponse", "POST_StringRequest:" + response);
                System.out.println("volley string success");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: 处理错误
                Log.e("### onErrorResponse", "POST_StringRequest:" + error.toString());
                System.out.println("volley string error : " + error.toString());
            }
        }) {
            /**
             * 返回含有POST或PUT请求的参数的Map
             */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> paramMap = new HashMap<>();
                // TODO: 处理POST参数
                paramMap.put("username", "chy");
                paramMap.put("password", "chy");
                paramMap.put("action", "login");
                paramMap.put("url", "user");
                return paramMap;
            }
        };

        stringRequest.setTag(VOLLEY_TAG);//StringRequest_POST
        /*stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));*/
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        MyApplication.getRequestQueue().add(stringRequest);
    }

}
