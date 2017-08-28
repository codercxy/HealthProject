package com.nju.android.health.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nju.android.health.MyApplication;
import com.nju.android.health.model.data.Individual;
import com.nju.android.health.model.data.Pressure;
import com.nju.android.health.model.data.User;
import com.nju.android.health.model.dataBase.IndividualDB;
import com.nju.android.health.model.dataBase.PressureDB;
import com.nju.android.health.model.dataBase.SugarDB;
import com.nju.android.health.model.dataBase.UserDB;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by 57248 on 2016/8/28.
 */
public class CommDB {
    public static final String DATABASE_NAME = "localDatabase";
    public static final int DATABASE_VERSION = 1;
    //创建用户表
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE IF NOT EXISTS "+UserDB.SQLITE_TABLE+" ("+
                    UserDB.ID+" integer PRIMARY KEY,"+
                    UserDB.NAME+" TEXT,"+
                    UserDB.PASSWORD+" TEXT,"+
                    UserDB.PHONE+" TEXT,"+
                    UserDB.ADDRESS+" TEXT,"+
                    UserDB.RECENTLOGINTIME+" TEXT,"+
                    UserDB.REGISTERTIME+" TEXT);";
    //创建用户信息表
    private static final String CREATE_TABLE_INDIVIDUAL =
            "CREATE TABLE if not exists "+IndividualDB.SQLITE_TABLE+" ("+
                    IndividualDB.USER_ID+" INTEGER PRIMARY KEY,"+
                    IndividualDB.NAME+" TEXT,"+
                    IndividualDB.AGE+" INTEGER,"+
                    IndividualDB.SEX+" TEXT,"+
                    IndividualDB.BIRTH+" TEXT,"+
                    IndividualDB.HEIGHT+" INTEGER,"+
                    IndividualDB.WEIGHT+" INTEGER,"+
                    IndividualDB.OCCUPATION+" TEXT,"+
                    IndividualDB.LOCATION+" TEXT,"+
                    IndividualDB.ADDRESS+" TEXT,"+
                    IndividualDB.FAMILYHISTORY+" TEXT,"+
                    IndividualDB.MARRIAGE+" TEXT,"+
                    IndividualDB.CHILD+" TEXT);";
    //创建血压表
    private static final String CREAT_TABLE_PRESSURE =
            "CREATE TABLE if not exists "+ PressureDB.SQLITE_TABLE+" ("+
                    PressureDB.ID+" integer PRIMARY KEY autoincrement,"+
                    PressureDB.USER_ID+" integer,"+
                    PressureDB.HIGH+" INTEGER,"+
                    PressureDB.LOW+" INTEGER,"+
                    PressureDB.RATE+" INTEGER,"+
                    PressureDB.TIME+" TEXT,"+
                    PressureDB.TAG_SEND+" INTEGER);";
    //创建血糖表
    private static final String CREAT_TABLE_SUGAR =
            "CREATE TABLE if not exists "+ SugarDB.SQLITE_TABLE+" ("+
                    SugarDB.ID+" integer PRIMARY KEY autoincrement,"+
                    SugarDB.USER_ID+" INTEGER,"+
                    SugarDB.VALUE_PRE+" INTEGER,"+
                    SugarDB.VALUE_AFTER+" INTEGER,"+
                    SugarDB.TIME+" TEXT,"+
                    SugarDB.TAG_SEND+" INTEGER);";
    //创建尿酸表
   /* private static final String CREAT_TABLE_UA =
            "CREATE TABLE if not exists "+UaDB.SQLITE_TABLE+" ("+
                    UaDB.ID+" integer PRIMARY KEY autoincrement,"+
                    UaDB.USER_ID+" INTEGER,"+
                    UaDB.VALUE+" INTEGER);";*/
    //创建胆固醇表
    /*private static final String CREAT_TABLE_CHOL =
            "CREATE TABLE if not exists "+CholDB.SQLITE_TABLE+" ("+
                    CholDB.ID+" integer PRIMARY KEY autoincrement,"+
                    CholDB.USER_ID+" INTEGER,"+
                    CholDB.VALUE+" INTEGER);";*/
    //创建血氧表
   /* private static final String CREAT_TABLE_OXYGEN =
            "CREATE TABLE if not exists "+OxygenDB.SQLITE_TABLE+" ("+
                    OxygenDB.ID+" integer PRIMARY KEY autoincrement,"+
                    OxygenDB.USER_ID+" INTEGER,"+
                    OxygenDB.VALUE+" INTEGER);";*/
    //创建胎心率表
    /*private static final String CREAT_TABLE_FHR =
            "CREATE TABLE if not exists "+FhrDB.SQLITE_TABLE+" ("+
                    FhrDB.ID+" integer PRIMARY KEY autoincrement,"+
                    FhrDB.USER_ID+" INTEGER,"+
                    FhrDB.VALUE+" INTEGER);";*/

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_USER);
            db.execSQL(CREATE_TABLE_INDIVIDUAL);
            db.execSQL(CREAT_TABLE_PRESSURE);
            db.execSQL(CREAT_TABLE_SUGAR);
            /*db.execSQL(CREAT_TABLE_UA);
            db.execSQL(CREAT_TABLE_CHOL);
            db.execSQL(CREAT_TABLE_OXYGEN);
            db.execSQL(CREAT_TABLE_FHR);*/
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS"+UserDB.SQLITE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS"+IndividualDB.SQLITE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS"+PressureDB.SQLITE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS"+SugarDB.SQLITE_TABLE);
            /*db.execSQL("DROP TABLE IF EXISTS"+UaDB.SQLITE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS"+CholDB.SQLITE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS"+OxygenDB.SQLITE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS"+FhrDB.SQLITE_TABLE);*/
            onCreate(db);
        }

    }

    public CommDB(Context ctx){
        this.context = ctx;
    }
    public void open(){
        DBHelper = new DatabaseHelper(this.context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void close(){
        DBHelper.close();
    }
    //创建一个用户
    public boolean creatUser(String name, String password, String phone, String address,
                             Date recenttime, Date registertime){
        long result;
        db = DBHelper.getWritableDatabase();

        String rec = formatter.format(recenttime);
        String reg = formatter.format(registertime);
        String user_id = MyApplication.getInstance().getUser_id();

        ContentValues initialValues = new ContentValues();
        initialValues.put(UserDB.ID, user_id);
        initialValues.put(UserDB.NAME, name);
        initialValues.put(UserDB.PASSWORD, password);
        initialValues.put(UserDB.PHONE, phone);
        initialValues.put(UserDB.ADDRESS, address);
        initialValues.put(UserDB.RECENTLOGINTIME, rec);
        initialValues.put(UserDB.REGISTERTIME, reg);
        result = db.insert(UserDB.SQLITE_TABLE, null, initialValues);
        if (result!=-1)
            return true;
        else
            return false;
    }
    //更新用户
    public int updaterUser(long user_id,String name,String password,String phone,String address,
                           Date recenttime,Date registertime){
        int result;
        db = DBHelper.getWritableDatabase();

        String rec = formatter.format(recenttime);
        String reg = formatter.format(registertime);
        ContentValues initialValues = new ContentValues();
        initialValues.put(UserDB.NAME, name);
        initialValues.put(UserDB.PASSWORD, password);
        initialValues.put(UserDB.PHONE, phone);
        initialValues.put(UserDB.ADDRESS, address);
        initialValues.put(UserDB.RECENTLOGINTIME, rec);
        initialValues.put(UserDB.REGISTERTIME, reg);
        String[] args = {String.valueOf(user_id)};
        result = db.update(UserDB.SQLITE_TABLE, initialValues,"_id = ?", args);
        return result;
    }
    //获取所有用户
    public ArrayList<User> fetchUser(){
        db = DBHelper.getReadableDatabase();
        ArrayList<User> list = new ArrayList<User>();
        Cursor mCursor = null;
        mCursor = db.query(UserDB.SQLITE_TABLE, new String[] {UserDB.ID,UserDB.NAME,UserDB.PASSWORD,
                UserDB.PHONE,UserDB.ADDRESS,UserDB.RECENTLOGINTIME,UserDB.REGISTERTIME},null, null, null, null, null);

//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (mCursor != null){
            if(mCursor.moveToFirst()){
                do{
                    User user = new User();
                    user.setName(mCursor.getString(mCursor.getColumnIndexOrThrow(UserDB.NAME)));
                    user.setPassword(mCursor.getString(mCursor.getColumnIndexOrThrow(UserDB.PASSWORD)));
                    user.setPhone(mCursor.getString(mCursor.getColumnIndexOrThrow(UserDB.PHONE)));
                    user.setAddress(mCursor.getString(mCursor.getColumnIndexOrThrow(UserDB.ADDRESS)));

//					recDate = Date.valueOf(recString);
                    user.setRecentLoginTime(mCursor.getString(mCursor.getColumnIndexOrThrow(UserDB.RECENTLOGINTIME)));

//					regDate = java.sql.Date.valueOf(regString);
                    user.setRegisterTime(mCursor.getString(mCursor.getColumnIndexOrThrow(UserDB.REGISTERTIME)));

                    list.add(user);
                }while(mCursor.moveToNext());
            }
            mCursor.close();
        }

        return list;
    }
    //创建用户信息
    public	long creatIndividual(long user_id,String name,int age,String sex,Date birth,
                                   int height,int weight,String occupation,String location,String address,
                                   String familyHistory,String marriage,String child){
        db = DBHelper.getWritableDatabase();
        String birthString = formatter.format(birth);

        ContentValues initialValues = new ContentValues();
        initialValues.put(IndividualDB.USER_ID, user_id);
        initialValues.put(IndividualDB.NAME, name);
        initialValues.put(IndividualDB.AGE, age);
        initialValues.put(IndividualDB.SEX, sex);
        initialValues.put(IndividualDB.BIRTH, birthString);
        initialValues.put(IndividualDB.HEIGHT, height);
        initialValues.put(IndividualDB.WEIGHT, weight);
        initialValues.put(IndividualDB.OCCUPATION, occupation);
        initialValues.put(IndividualDB.LOCATION, location);
        initialValues.put(IndividualDB.ADDRESS, address);
        initialValues.put(IndividualDB.FAMILYHISTORY, familyHistory);
        initialValues.put(IndividualDB.MARRIAGE, marriage);
        initialValues.put(IndividualDB.CHILD, child);

        long result = db.insert(IndividualDB.SQLITE_TABLE, null, initialValues);
        return result;
    }
    //更新用户信息
    public int updateIndividual(long user_id,String name,int age,String sex,Date birth,
                                int height,int weight,String occupation,String location,String address,
                                String familyHistory,String marriage,String child){
        db = DBHelper.getWritableDatabase();
        String birthString = formatter.format(birth);

        ContentValues initialValues = new ContentValues();
        initialValues.put(IndividualDB.NAME, name);
        initialValues.put(IndividualDB.AGE, age);
        initialValues.put(IndividualDB.SEX, sex);
        initialValues.put(IndividualDB.BIRTH, birthString);
        initialValues.put(IndividualDB.HEIGHT, height);
        initialValues.put(IndividualDB.WEIGHT, weight);
        initialValues.put(IndividualDB.OCCUPATION, occupation);
        initialValues.put(IndividualDB.LOCATION, location);
        initialValues.put(IndividualDB.ADDRESS, address);
        initialValues.put(IndividualDB.FAMILYHISTORY, familyHistory);
        initialValues.put(IndividualDB.MARRIAGE, marriage);
        initialValues.put(IndividualDB.CHILD, child);
        String[] args = {String.valueOf(user_id)};
        int result = db.update(IndividualDB.SQLITE_TABLE, initialValues, "user_id = ?", args);
        return result;
    }
    //根据ID获取用户信息
    public Individual getIndividualById(long user_id){
        Individual ind = new Individual();

        db = DBHelper.getReadableDatabase();
        String[] args = {String.valueOf(user_id)};
        Cursor c = db.query(IndividualDB.SQLITE_TABLE, new String[]{IndividualDB.NAME,IndividualDB.AGE,
                IndividualDB.SEX,IndividualDB.BIRTH,IndividualDB.HEIGHT,IndividualDB.WEIGHT,
                IndividualDB.OCCUPATION,IndividualDB.LOCATION,IndividualDB.ADDRESS,
                IndividualDB.FAMILYHISTORY,IndividualDB.MARRIAGE,IndividualDB.CHILD}, "user_id = ?", args, null, null, null);
        ind.setName(c.getString(c.getColumnIndexOrThrow(IndividualDB.NAME)));
        ind.setAge(c.getInt(c.getColumnIndexOrThrow(IndividualDB.AGE)));
        ind.setSex(c.getString(c.getColumnIndexOrThrow(IndividualDB.SEX)));
        ind.setBirth(c.getString(c.getColumnIndexOrThrow(IndividualDB.BIRTH)));
        ind.setHeight(c.getInt(c.getColumnIndexOrThrow(IndividualDB.HEIGHT)));
        ind.setWeight(c.getInt(c.getColumnIndexOrThrow(IndividualDB.WEIGHT)));
        ind.setOccupation(c.getString(c.getColumnIndexOrThrow(IndividualDB.OCCUPATION)));
        ind.setLocation(c.getString(c.getColumnIndexOrThrow(IndividualDB.LOCATION)));
        ind.setAddress(c.getString(c.getColumnIndexOrThrow(IndividualDB.ADDRESS)));
        ind.setFamilyHistory(c.getString(c.getColumnIndexOrThrow(IndividualDB.FAMILYHISTORY)));
        ind.setMarriage(c.getString(c.getColumnIndexOrThrow(IndividualDB.MARRIAGE)));
        ind.setChild(c.getString(c.getColumnIndexOrThrow(IndividualDB.CHILD)));
        c.close();
        return ind;
    }
    //新建一条血压记录
    public long creatPressure(long user_id,int high,int low,int rate,Date time){
        String t = formatter.format(time);

        db = DBHelper.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(PressureDB.USER_ID, user_id);
        initialValues.put(PressureDB.HIGH, high);
        initialValues.put(PressureDB.LOW, low);
        initialValues.put(PressureDB.RATE, rate);
        initialValues.put(PressureDB.TIME, t);
        initialValues.put(PressureDB.TAG_SEND, 0);
        long result = db.insert(PressureDB.SQLITE_TABLE, null, initialValues);

        Log.e("pressureid", Long.toString(result));
        return result;
    }
    //返回pressure状态
    public boolean isSendState_pressure(long user_id,long pressure_id,boolean isSend){
        db = DBHelper.getWritableDatabase();
        String send = String.valueOf(isSend);
        String[] args = {String.valueOf(user_id),String.valueOf(pressure_id)};

        ContentValues value = new ContentValues();
        value.put(PressureDB.TAG_SEND,send);

        int result = db.update(PressureDB.SQLITE_TABLE, value, "user_id = ? AND _id = ?", args);
        if(result==1)
            return true;
        else
            return false;
    }
    //删除一条血压记录
    public int delePressure(long user_id,long pressure_id){
        db = DBHelper.getWritableDatabase();
        String[] args = {String.valueOf(pressure_id),String.valueOf(user_id)};
        int result = db.delete(PressureDB.SQLITE_TABLE, "_id = ? AND user_id = ?", args);
        return result;
    }
    //获取所有Pressure
    public ArrayList<Pressure> fetchPressure(long user_id){
        db = DBHelper.getReadableDatabase();
        ArrayList<Pressure> list = new ArrayList<Pressure>();
        Cursor c = null;
        String[] args = {String.valueOf(user_id)};
        c = db.query(PressureDB.SQLITE_TABLE, new String[] {PressureDB.HIGH,PressureDB.LOW,PressureDB.RATE,
                        PressureDB.TIME,PressureDB.TAG_SEND},
                "user_id = ?", args, null, null, null);
        if (c != null){
            if(c.moveToFirst()){
                do{
                    Pressure p = new Pressure();
                    p.setId(c.getInt(c.getColumnIndexOrThrow(PressureDB.ID)));
                    p.setHigh(c.getInt(c.getColumnIndexOrThrow(PressureDB.HIGH)));
                    p.setLow(c.getInt(c.getColumnIndexOrThrow(PressureDB.LOW)));
                    p.setRate(c.getInt(c.getColumnIndexOrThrow(PressureDB.RATE)));
                    p.setTime(c.getString(c.getColumnIndexOrThrow(PressureDB.TIME)));
                    list.add(p);
                }while(c.moveToNext());
            }
            c.close();
        }
        return list;
    }
    //返回isSend为False的pressure
    public ArrayList<Pressure> fetchSendFalsePressure(long user_id){
        db = DBHelper.getReadableDatabase();
        ArrayList<Pressure> list = new ArrayList<Pressure>();
        Cursor c = null;
        String[] args = {String.valueOf(user_id),String.valueOf("false")};
        c = db.query(PressureDB.SQLITE_TABLE, new String[] {PressureDB.HIGH,PressureDB.LOW,PressureDB.RATE,
                        PressureDB.TIME,PressureDB.TAG_SEND},
                "user_id = ? and tag_send = ?", args, null, null, null);
        if (c != null){
            if(c.moveToFirst()){
                do{
                    Pressure p = new Pressure();
                    p.setId(c.getInt(c.getColumnIndexOrThrow(PressureDB.ID)));
                    p.setHigh(c.getInt(c.getColumnIndexOrThrow(PressureDB.HIGH)));
                    p.setLow(c.getInt(c.getColumnIndexOrThrow(PressureDB.LOW)));
                    p.setRate(c.getInt(c.getColumnIndexOrThrow(PressureDB.RATE)));
                    p.setTime(c.getString(c.getColumnIndexOrThrow(PressureDB.TIME)));
                    list.add(p);
                }while(c.moveToNext());
            }
            c.close();
        }
        return list;
    }
    public ArrayList<Pressure> getPressureByTime(long user_id,Date start,Date end){
        db = DBHelper.getReadableDatabase();
        ArrayList<Pressure> list = new ArrayList<Pressure>();
        Long st = start.getTime();
        Long en = start.getTime();
        Cursor c = null;
        String[] args = {String.valueOf(user_id),String.valueOf(st),String.valueOf(en)};
        c = db.query(PressureDB.SQLITE_TABLE, new String[] {PressureDB.HIGH,PressureDB.LOW,PressureDB.RATE},
                "user_id = ? and time >= ? and time <= ?", args, null, null, null);
        if (c != null){
            if(c.moveToFirst()){
                do{
                    Pressure p = new Pressure();
                    p.setId(c.getInt(c.getColumnIndexOrThrow(PressureDB.ID)));
                    p.setHigh(c.getInt(c.getColumnIndexOrThrow(PressureDB.HIGH)));
                    p.setLow(c.getInt(c.getColumnIndexOrThrow(PressureDB.LOW)));
                    p.setRate(c.getInt(c.getColumnIndexOrThrow(PressureDB.RATE)));
                    p.setTime(c.getString(c.getColumnIndexOrThrow(PressureDB.TIME)));
                    list.add(p);
                }while(c.moveToNext());
            }
            c.close();
        }
        return list;
    }
    //是否存在isSend为False的pressure
    public boolean isSend_pressure(){
        db = DBHelper.getReadableDatabase();
        Cursor c = null;
        String args[] = {"false"};
        c = db.query(PressureDB.SQLITE_TABLE, new String[]{PressureDB.TAG_SEND}, "tag_send = ?", args, null, null, null);
        if(c!=null){
            return false;
        }
        return true;
    }
    public long creatSugar(long user_id,int pre,int after,boolean tag_send){
        Date time = new Date(System.currentTimeMillis());
        String t = formatter.format(time);
        int tag;
        if(tag_send){
            tag = 1;
        }else{
            tag = 0;
        }
        db = DBHelper.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(SugarDB.USER_ID, user_id);
        initialValues.put(SugarDB.VALUE_PRE, pre);
        initialValues.put(SugarDB.VALUE_AFTER, after);
        initialValues.put(SugarDB.TIME, t);
        initialValues.put(SugarDB.TAG_SEND, tag);
        long result = db.insert(SugarDB.SQLITE_TABLE, null, initialValues);
        return result;
    }
}
