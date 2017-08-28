package com.nju.android.health.views.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.nju.android.health.MyApplication;
import com.nju.android.health.R;
import com.nju.android.health.utils.NetworkUtils;
import com.nju.android.health.utils.MD5Util;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "login_activity";

    private EditText edit_user;
    private EditText edit_passwd;
    private Button button_login;
    private TextView button_regist;
    private LoginActivityHandler handler = new LoginActivityHandler();
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MyApplication.getInstance().addActivity(this);

        boolean success = NetworkUtils.isNetworkAvailable(this);
        if(!success){
            Toast.makeText(this, "请检查网络！", Toast.LENGTH_SHORT).show();
            return;
        }


        edit_user = (EditText) findViewById(R.id.edit_user);
        edit_passwd = (EditText) findViewById(R.id.edit_passwd);
        button_login = (Button) findViewById(R.id.button_login);
        button_regist = (TextView) findViewById(R.id.button_register);

        button_login.setOnClickListener(this);
        button_regist.setOnClickListener(this);

        sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);

        edit_user.setText(sp.getString("USER_NAME", ""));
        edit_passwd.setText(sp.getString("PASSWORD", ""));
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_login:

                if (!edit_user.getText().toString().isEmpty()){

                    /*Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,MainActivity.class );
                    intent.putExtra("username", edit_user.getText().toString());
                    startActivity(intent);*/

                    //
                    /*Thread t = new LoginThread();
                    t.start();*/

                    //start
                    MyApplication.getInstance().setUser_id("10001");
                    YWIMKit mIMKit = YWAPI.getIMKitInstance();

                    String userid = "testpro1";
                    String password = "taobao1234";
                    IYWLoginService loginService = mIMKit.getLoginService();
                    YWLoginParam loginParam = YWLoginParam.createLoginParam(userid, password);
                    loginService.login(loginParam, new IWxCallback() {
                        @Override
                        public void onSuccess(Object... objects) {

//                            Log.e(TAG, "ywim success");
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                            if (!isOpen) {
                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("USER_NAME", edit_user.getText().toString());
                            editor.putString("PASSWORD", edit_passwd.getText().toString());
                            editor.commit();

                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this,MainActivity.class );
                            intent.putExtra("username", edit_user.getText().toString());
                            startActivity(intent);
                            finish();
                            showMessage("登陆成功");
                        }

                        @Override
                        public void onError(int i, String s) {
                            showMessage("请检查网络！");
                            return;

                        }

                        @Override
                        public void onProgress(int i) {
                            showMessage("正在登陆...");
                        }
                    });

                    //end



                } else {
                    showMessage("请输入用户名和密码");
                }

                break;

            case R.id.button_register:
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }

    }
    class LoginThread extends Thread{
        @Override
        public void run() {
            //设置连接超时时间
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5 * 1000);

            //实现Get请求
            //实现Post发送请求处理
//			HttpPost httpPost = new HttpPost("http://www.inkstand.cn/library/action.php?action=login");
            HttpPost httpPost = new HttpPost("http://114.212.190.79:12080/action.php");
            httpPost.setParams(httpParams);
            //用户名密码发送给服务器
            try{
                String username = edit_user.getText().toString();
                String pass = edit_passwd.getText().toString();

                String passwd = MD5Util.makeMD5(username+pass);

                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                nvps.add(new BasicNameValuePair("username", username));
                nvps.add(new BasicNameValuePair("password", passwd));
                nvps.add(new BasicNameValuePair("action", "login"));
                nvps.add(new BasicNameValuePair("url", "user"));
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            }catch(UnsupportedEncodingException e1){
                showMessage(e1.toString());
            }

            HttpClient client = new DefaultHttpClient();
            try {
                HttpResponse response = client.execute(httpPost);

                Header[] header = response.getHeaders("Set-Cookie");
//                showMessage(String.valueOf(header[0].getValue().toString()));
                if(header.length > 0){
                    String temp1 = header[1].getValue().toString();
                    String user_id = temp1.substring(temp1.indexOf("user_id") + 8, temp1.indexOf(";"));

                    String temp2 = header[0].getValue().toString();
                    String session_id = temp2.substring(temp2.indexOf("PHPSESSID")+10, temp2.indexOf(";"));

                    MyApplication.getInstance().setUser_id(user_id);
                    MyApplication.getInstance().setSession_id(session_id);
                }

                //返回响应体
                String responseBody = EntityUtils.toString(response.getEntity());
                Log.e(TAG, responseBody);
                JSONObject jsons = new JSONObject(responseBody);
                boolean isOK = jsons.getBoolean("success");
//                showMessage(String.valueOf(isOK));
//                Log.e(TAG, String.valueOf(isOK));
                String result = jsons.getString("resultText");
                if (isOK){

                    YWIMKit mIMKit = YWAPI.getIMKitInstance();

                    String userid = "testpro1";
                    String password = "taobao1234";
                    IYWLoginService loginService = mIMKit.getLoginService();
                    YWLoginParam loginParam = YWLoginParam.createLoginParam(userid, password);
                    loginService.login(loginParam, new IWxCallback() {
                        @Override
                        public void onSuccess(Object... objects) {

//                            Log.e(TAG, "ywim success");
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                            if (isOpen) {
                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("USER_NAME", edit_user.getText().toString());
                            editor.putString("PASSWORD", edit_passwd.getText().toString());
                            editor.commit();

                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this,MainActivity.class );
                            intent.putExtra("username", edit_user.getText().toString());
                            startActivity(intent);
                            finish();
                            showMessage("登陆成功");
                        }

                        @Override
                        public void onError(int i, String s) {
                            showMessage("请检查网络！");
                            return;

                        }

                        @Override
                        public void onProgress(int i) {
                            showMessage("正在登陆...");
                        }
                    });


                }else{
                    //显示用户名密码错误
                    showMessage(result);
                }
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                showMessage(e.getMessage());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                showMessage("无法连接服务器");
            }catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }catch (ArrayIndexOutOfBoundsException e) {
                showMessage(e.toString());
            }
        }

    }

    /*
     * 如果登陆错误，则调用本方法 通过handler对象，将错误提示信息发送到主线程
     */
    private void showMessage(String message) {
        Message msg = Message
                .obtain(handler, LoginActivityHandler.SHOW_MESSAGE);
        msg.obj = message;
        msg.sendToTarget();
    }
    class LoginActivityHandler extends Handler {
        // 显示信息
        public static final int SHOW_MESSAGE = 0x0003;
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SHOW_MESSAGE) {
                Toast.makeText(LoginActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
