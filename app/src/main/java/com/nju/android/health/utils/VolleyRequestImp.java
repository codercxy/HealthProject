package com.nju.android.health.utils;

import android.content.ContentValues;
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
import com.google.gson.JsonObject;
import com.nju.android.health.MyApplication;
import com.nju.android.health.providers.DbPressure;
import com.nju.android.health.providers.DbProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
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

    private String result = null;

    private Map<String, String> param = new HashMap<>();
    public VolleyRequestImp() {

    }
    public VolleyRequestImp(Map<String, String> p) {
        param = p;

    }
    public void myVolleyRequestPressure_GET(final Context context) {
        MyVolleyRequest request = new MyVolleyRequest(context, new MyVolleyRequest.Callback() {
            @Override
            public void onSuccess(String response) {
                System.out.println("my_volley_get_pressure_success : " + response.toString());
                try {
                    JSONObject json = new JSONObject(response);
                    boolean success = json.getBoolean("success");
                    System.out.println("json success : " + success);
                    if (success) {
                        JSONArray jsonArray = json.getJSONArray("data");
                        int n = jsonArray.length();
                        if (n > 0) {
                            DbProvider provider = new DbProvider();
                            provider.init(context);
                            for (int i = 0; i < n; i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                ContentValues contentValues = new ContentValues();
                                contentValues.put(DbPressure.Pressure.USER_ID, Integer.parseInt(MyApplication.getInstance().getUser_id()));
                                contentValues.put(DbPressure.Pressure.TIME, data.getString("measureTime"));
                                contentValues.put(DbPressure.Pressure.HIGH, Integer.parseInt(data.getString("SBP")));
                                contentValues.put(DbPressure.Pressure.LOW, Integer.parseInt(data.getString("DBP")));
                                contentValues.put(DbPressure.Pressure.RATE, Integer.parseInt(data.getString("HR")));
                                contentValues.put(DbPressure.Pressure.ISSEND, "true");
                                provider.insert(DbPressure.CONTENT_URI, contentValues);
                            }
                            System.out.println("Database has been initialized!");
                        }

                    }


                } catch (JSONException e) {
                    System.out.println("json error, at pressure_get_method");
                }
            }

            @Override
            public void onError(VolleyError error) {
                error.printStackTrace();
                System.out.println("my_volley_get_pressure_failed : " + error);
            }
        });
        request.requestPost(url, "my_get_" + VOLLEY_TAG, param);
    }

    public void myVolleyRequestPressure_POST(final Context context) {
        MyVolleyRequest myVolleyRequest = new MyVolleyRequest(context, new MyVolleyRequest.Callback() {
            @Override
            public void onSuccess(String response) {

                DbProvider provider = new DbProvider();
                provider.init(context);

                ContentValues contentValues = new ContentValues();
                contentValues.put(DbPressure.Pressure.USER_ID, Integer.parseInt(MyApplication.getInstance().getUser_id()));
                contentValues.put(DbPressure.Pressure.TIME, param.get("measureTime"));
                contentValues.put(DbPressure.Pressure.HIGH, Integer.parseInt(param.get("SBP")));
                contentValues.put(DbPressure.Pressure.LOW, Integer.parseInt(param.get("DBP")));
                contentValues.put(DbPressure.Pressure.RATE, Integer.parseInt(param.get("HR")));
                contentValues.put(DbPressure.Pressure.ISSEND, "true");

                provider.insert(DbPressure.CONTENT_URI, contentValues);
                provider.database.close();

                Log.i("### onSuccess", "POST_MyVolleyRequest" + response);
                System.out.println("volley success : " + response);
            }

            @Override
            public void onError(VolleyError error) {
                DbProvider provider = new DbProvider();
                provider.init(context);

                ContentValues contentValues = new ContentValues();
                contentValues.put(DbPressure.Pressure.USER_ID, Integer.parseInt(MyApplication.getInstance().getUser_id()));
                contentValues.put(DbPressure.Pressure.TIME, param.get("measureTime"));
                contentValues.put(DbPressure.Pressure.HIGH, Integer.parseInt(param.get("SBP")));
                contentValues.put(DbPressure.Pressure.LOW, Integer.parseInt(param.get("DBP")));
                contentValues.put(DbPressure.Pressure.RATE, Integer.parseInt(param.get("HR")));
                contentValues.put(DbPressure.Pressure.ISSEND, "false");

                provider.insert(DbPressure.CONTENT_URI, contentValues);
                provider.database.close();

                error.printStackTrace();
                System.out.println("volley error:" + error);
            }
        });
        myVolleyRequest.requestPost(url, "my_post_" + VOLLEY_TAG, param);
    }

    public String myVolleyRequestSearch_POST(Context context) {

        MyVolleyRequest myVolleyRequest = new MyVolleyRequest(context, new MyVolleyRequest.Callback() {
            @Override
            public void onSuccess(String response) {
                Log.i("### onSuccess", "POST_MyVolleyRequest" + response);
                System.out.println("volley success");
                result = response;
            }

            @Override
            public void onError(VolleyError error) {
                error.printStackTrace();
                System.out.println("volley error:" + error);
                result = error.toString();
            }
        });
        myVolleyRequest.requestPost(url, "my_post_" + VOLLEY_TAG, param);
        return result;
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
