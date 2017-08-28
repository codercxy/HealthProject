package com.nju.android.health.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.nju.android.health.MyApplication;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 57248 on 2016/8/26.
 */
public class NetworkUtils {
    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // 判断网络是否链接
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isSend_Pressure(int high, int low, int rate, Date time, Long pressure_id){
        String t = formatter.format(time);
        String p_id = Long.toString(pressure_id);
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 5 * 1000);

        HttpPost httpPost = new HttpPost("http://114.212.190.79:12080/action.php");
        httpPost.setParams(httpParams);

        try {
            String h = String.valueOf(high);
            String l = String.valueOf(low);
            String r = String.valueOf(rate);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("url", "bloodpressure"));
            nvps.add(new BasicNameValuePair("action", "upload_data"));
            nvps.add(new BasicNameValuePair("clientid", p_id));
            nvps.add(new BasicNameValuePair("SBP", h));
            nvps.add(new BasicNameValuePair("DBP",l));
            nvps.add(new BasicNameValuePair("HR",r));
            nvps.add(new BasicNameValuePair("measureTime",t));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        } catch (Exception e) {
            e.printStackTrace();
//			Log.d("error","无法上传！");
        }
        String user_id = MyApplication.getInstance().getUser_id();
        String session_id = MyApplication.getInstance().getSession_id();

        String cookie = "user_id=" + user_id + ";" + "PHPSESSID="
                + session_id;
        httpPost.addHeader(new BasicHeader("Cookie", cookie));

        HttpClient client = new DefaultHttpClient();
        try {
            HttpResponse response = client.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            Log.d("response", responseBody);
            JSONObject jsons = new JSONObject(responseBody);

            boolean isOK = jsons.getBoolean("success");
            String s = jsons.getString("resultText");
            if (isOK){
                return true;
            }else{
                return false;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}
