package com.nju.android.health.bswk.brain;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.nju.android.health.R;
import com.nju.android.health.bswk.FileService;
import com.nju.android.health.bswk.HttpInfo;

import java.util.Map;

public class NaozuzhongActivity extends Activity {
    private WebView wv_naozuzhong;
    private LinearLayout back;
    private TextView tv_exit;
    private FileService fileService;
    private String mem_account, mem_token;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naozuzhong);

        fileService = new FileService(this);
        try {
            Map<String, String> map = fileService.getUserInfo("bswk.txt");
            mem_account = map.get("mem_account");
            mem_token = map.get("mem_token");
        } catch (Exception e) {
            e.printStackTrace();
        }

        findView();
        setView();          //加载网页
        click();
    }

    private void setView() {
        wv_naozuzhong.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
                // 构建一个Builder来显示网页中的alert对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(NaozuzhongActivity.this);
                builder.setTitle("提示");
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                builder.setCancelable(false);
                builder.create();
                builder.show();
                return true;
            };

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(NaozuzhongActivity.this)
                        .setTitle("提示")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.cancel();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            };

            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

        });
        wv_naozuzhong.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView view,
                                                    final String url) {
                view.loadUrl(url);// 载入网页
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        WebSettings webSettings = wv_naozuzhong.getSettings();
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);  //支持js
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setSupportZoom(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        AndroidJSInterface aj = new AndroidJSInterface(mContext);
        wv_naozuzhong.addJavascriptInterface(aj,aj.getInterface());
        wv_naozuzhong.loadUrl(HttpInfo.PATH_YM+"user_android/form/record.jsp?mem_account=" + mem_account + "&mem_token=" + mem_token);
    }

    private void click() {
        //返回键
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wv_naozuzhong.canGoBack()){
                    wv_naozuzhong.goBack();
                }else{
                    finish();
                }
            }
        });
        //退出键
        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findView() {
        wv_naozuzhong = (WebView) findViewById(R.id.wv_naozuzhong);
        back = (LinearLayout) findViewById(R.id.back);
        tv_exit = (TextView) findViewById(R.id.tv_exit);
    }

    final class AndroidJSInterface{
        public AndroidJSInterface(Context context){
            mContext = context;
        }

        //返回
        @JavascriptInterface
        public void goExit() {
            Toast.makeText(NaozuzhongActivity.this,"提交成功", Toast.LENGTH_SHORT).show();
            finish();
        }

        //toast提示
        @JavascriptInterface
        public void toastDisplay(final String message){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(NaozuzhongActivity.this,message, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @JavascriptInterface
        public String getInterface() {
            return "android_js_interface";
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(wv_naozuzhong.canGoBack()){
            wv_naozuzhong.goBack();
        }else{
            finish();
        }
    }
}
