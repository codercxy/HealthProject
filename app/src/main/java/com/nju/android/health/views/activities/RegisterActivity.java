package com.nju.android.health.views.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nju.android.health.R;
import com.nju.android.health.model.data.User;
import com.nju.android.health.providers.CommDB;
import com.nju.android.health.utils.MD5Util;

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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView back;
    private EditText edit_username;
    private EditText edit_passwd;
    private EditText edit_phone;
    private Button submit;
    private Button reset;

    private String username;
    private String pass;
    private String phone;
    private String passwd;

    private RegisterHandler handler = new RegisterHandler();
    private CommDB commDB;
    private Date date;
    private List<User> userList = new ArrayList<User>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        back = (TextView) findViewById(R.id.register_back);
        edit_username = (EditText) findViewById(R.id.edit_reg_username);
        edit_passwd = (EditText) findViewById(R.id.edit_reg_passwd);
        edit_phone = (EditText) findViewById(R.id.edit_reg_phone);

        submit = (Button) findViewById(R.id.button_reg_submit);
        reset = (Button) findViewById(R.id.button_reg_reset);

        back.setOnClickListener(this);
        submit.setOnClickListener(this);
        reset.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()){
            case R.id.button_reg_submit:
                commDB = new CommDB(this);
                commDB.open();

                Thread t = new RegisterThread();
                t.start();
                break;
            case R.id.button_reg_reset:
                edit_username.setText("");
                edit_passwd.setText("");
                edit_phone.setText("");
                break;
            case R.id.register_back:
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this,LoginActivity.class );
                startActivity(intent);
        }
    }
    class RegisterThread extends Thread{
        @Override
        public void run(){
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5 * 1000);
            HttpClient client = new DefaultHttpClient(httpParams);

            HttpPost httpPost = new HttpPost("http://114.212.190.79:12080/action.php");
            httpPost.setParams(httpParams);
            try{
                username = edit_username.getText().toString();
                pass = edit_passwd.getText().toString();
                phone = edit_phone.getText().toString();

                passwd = MD5Util.makeMD5(username+pass);

                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                nvps.add(new BasicNameValuePair("username", username));
                nvps.add(new BasicNameValuePair("password", passwd));
                nvps.add(new BasicNameValuePair("phone", phone));
                nvps.add(new BasicNameValuePair("type", "phone"));
                nvps.add(new BasicNameValuePair("action", "register"));
                nvps.add(new BasicNameValuePair("url", "user"));
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            }catch(UnsupportedEncodingException e1){
                showMessage(e1.toString());
            }
            try {
                HttpResponse response = client.execute(httpPost);

                String responseBody = EntityUtils.toString(response.getEntity());

                JSONObject jsons = new JSONObject(responseBody);
                boolean isOK = jsons.getBoolean("success");
                String result = jsons.getString("resultText");
                if(isOK){
                    showMessage("注册成功！");
                    date = new Date(System.currentTimeMillis());
                    commDB.creatUser(username, pass, phone, "", date, date);

                    Intent intent = new Intent();
                    intent.setClass(RegisterActivity.this,LoginActivity.class );
//					intent.putExtra("username", edit_user.getText().toString());
                    startActivity(intent);
                    finish();
                }else{
                    showMessage(result);
                }


            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                showMessage(e.getMessage());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                showMessage("无法连接服务器");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
    //关闭数据库
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(commDB != null){
            commDB.close();
        }
    }
    private void showMessage(String message) {
        Message msg = Message
                .obtain(handler, RegisterHandler.SHOW_MESSAGE);
        msg.obj = message;
        msg.sendToTarget();
    }

    class RegisterHandler extends Handler {
        // 显示信息
        public static final int SHOW_MESSAGE = 0x0004;
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SHOW_MESSAGE) {
                Toast.makeText(RegisterActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }
}
