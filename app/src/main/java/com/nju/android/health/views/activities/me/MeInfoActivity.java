package com.nju.android.health.views.activities.me;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.utils.CircleImg;
import com.nju.android.health.utils.PictureUtil;

import java.util.Calendar;

import static com.nju.android.health.views.fragments.setting.SettingFragment.REQUEST_IMAGE;
import static com.nju.android.health.views.fragments.setting.SettingFragment.REQUEST_IMAGE_OLD;

public class MeInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_name;
    private EditText et_phonoe;
    private EditText et_username;

//    private Spinner sexSpinner;
//    private Spinner historySpinner;
    private Toolbar toolbar;

    private Boolean isEditable;

    private Uri outputFileUri;

    private CircleImg headImg;
    private boolean isRun=false;
    private String addString=" ";
    public static MeInfoActivity instance;

//    private ArrayAdapter<String> sexAdapter = null;
//    private ArrayAdapter<String> historyAdapter = null;
//    private static final String[] sexStr = {"男", "女"};
//    private static final String[] historyStr = {"无", "有"};

    //dataPicker
//    private DatePicker dp = null;
//    private Calendar calendar = null;
//    private int year, month, day;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_info);

        instance = this;
        initView();
        initToolBar();
        initData();
    }



    private void initView() {
        et_username = (EditText) findViewById(R.id.me_info_username);
        et_name = (EditText) findViewById(R.id.me_info_name);
        et_phonoe = (EditText) findViewById(R.id.me_info_phone);

//        sexSpinner = (Spinner) findViewById(R.id.me_spinner_sex);
//        historySpinner = (Spinner) findViewById(R.id.me_spinner_history);

        /*sexAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, sexStr);
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/

        headImg = (CircleImg) findViewById(R.id.me_info_circleimg);
        headImg.setOnClickListener(this);

        /*sexSpinner.setAdapter(sexAdapter);
        sexSpinner.setVisibility(View.VISIBLE);
        sexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        historyAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, historyStr);
        historyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        historySpinner.setAdapter(historyAdapter);
        historySpinner.setVisibility(View.VISIBLE);
        historySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        et_username.setEnabled(false);
        et_name.setEnabled(false);
        et_phonoe.setEnabled(false);
        headImg.setEnabled(false);


        toolbar = (Toolbar) findViewById(R.id.toolbar_me_info);

        //initDatePicker
        /*dp = (DatePicker) findViewById(R.id.me_date_picker);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);*/

//        et_date.setOnClickListener(this);
    }

    private void initToolBar() {
        isEditable = false;
        toolbar.inflateMenu(R.menu.me_info);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.me_info_edit:
                        if (!isEditable) {
                            isEditable = true;

                            toolbar.getMenu().getItem(0).setIcon(R.drawable.ic_done_white_24dp);

                            et_username.setEnabled(true);
                            et_name.setEnabled(true);
                            et_phonoe.setEnabled(true);
                            headImg.setEnabled(true);
//                            et_date.setEnabled(true);
                            /*et_history.setEnabled(true);
                            et_sex.setEnabled(true);*/
//                            et_age.setEnabled(true);

                        } else {
                            isEditable = false;

                            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.username), et_username.getText().toString());
                            editor.putString(getString(R.string.name), et_name.getText().toString());
                            editor.putString(getString(R.string.phone), et_phonoe.getText().toString());
//                            editor.putString(getString(R.string.sex), et_sex.getText().toString());
//                            editor.putString(getString(R.string.age), et_age.getText().toString());
//                            editor.putString(getString(R.string.birth), et_date.getText().toString());
//                            editor.putString(getString(R.string.history), et_history.getText().toString());
                            editor.commit();

//                            et_name.setText(et_name.getText());

                            toolbar.getMenu().getItem(0).setIcon(R.drawable.ic_mode_edit_white_24dp);

                            et_username.setEnabled(false);
                            et_name.setEnabled(false);
                            et_phonoe.setEnabled(false);
                            headImg.setEnabled(false);
//                            et_date.setEnabled(false);
                            /*et_history.setEnabled(false);
                            et_sex.setEnabled(false);*/
//                            et_age.setEnabled(false);
                        }



                        break;
                }
                return true;
            }
        });
    }
    private void initData() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        et_username.setText(sharedPref.getString(getString(R.string.username), ""));
        et_name.setText(sharedPref.getString(getString(R.string.name), ""));
        et_phonoe.setText(sharedPref.getString(getString(R.string.phone), ""));
//        sexSpinner.setText(sharedPref.getString(getString(R.string.sex), ""));
//        et_age.setText(sharedPref.getString(getString(R.string.age), ""));
//        et_date.setText(sharedPref.getString(getString(R.string.birth), ""));
//        et_history.setText(sharedPref.getString(getString(R.string.history), ""));
        //init Image
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        String uri = sp.getString(getString(R.string.headimg), "");
        if (uri != "") {
            outputFileUri = Uri.parse(uri);
            headImg.setImageBitmap(PictureUtil
                    .getSmallBitmap(getRealPathFromURI(outputFileUri), 480, 800));
        }

        initPhoneText();

    }
    private void initPhoneText() {
        et_phonoe.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.i("tag", "onTextChanged()之前");
                if(isRun){//这几句要加，不然每输入一个值都会执行两次onTextChanged()，导致堆栈溢出，原因不明
                    isRun = false;
                    return;
                }
                isRun = true;
//                Log.i("tag", "onTextChanged()");

                String finalString="";
                int index=0;
                String telString=s.toString().replace(" ", "");
                if ((index+3)<telString.length()) {
                    finalString+=(telString.substring(index, index+3)+addString);
                    index+=3;
                }
                while ((index+4)<telString.length()) {

                    finalString+=(telString.substring(index, index+4)+addString);
                    index+=4;

                }
                finalString+=telString.substring(index,telString.length());
                et_phonoe.setText(finalString);
                //此语句不可少，否则输入的光标会出现在最左边，不会随输入的值往右移动
                et_phonoe.setSelection(finalString.length());

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            /*case R.id.me_info_age:
                break;*/
            case R.id.me_info_circleimg:
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    startActivityForResult(intent, REQUEST_IMAGE);
                } else {
                    startActivityForResult(intent, REQUEST_IMAGE_OLD);
                }
                break;
            /*case R.id.me_info_date:

                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        setTitle(year + "-" + (month + 1) + "-" + dayOfMonth);
                        et_date.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
                        et_age.setText(String.valueOf(cal_age(year, month, dayOfMonth, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))) + "岁ce");
                    }
                }, year, calendar.get(Calendar.MONTH), day).show();
                break;*/

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE) {
            outputFileUri = data.getData();

            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.headimg), outputFileUri.toString());
            editor.commit();

            headImg.setImageBitmap(PictureUtil
                    .getSmallBitmap(getRealPathFromURI(outputFileUri), 480, 800));
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private int cal_age(int y1, int m1, int d1, int y2, int m2, int d2) {
        if (y1 >= y2)
            return 0;
        int age = y2 - y1;
        if (m2 > m1)
            return age + 1;
        else
            return age;
    }
}
