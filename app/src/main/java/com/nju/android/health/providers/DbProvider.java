package com.nju.android.health.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;
import android.util.Log;

import com.nju.android.health.MyApplication;
import com.nju.android.health.model.data.Glucose;
import com.nju.android.health.model.data.OriginData;
import com.nju.android.health.model.data.Pressure;
import com.nju.android.health.model.data.Step;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by 57248 on 2016/10/11.
 */

public class DbProvider extends ContentProvider{

    private static final int PRESSURE = 0;
    private static final int GLUCOSE = 1;
    private static final int STEP = 2;
    private static final int USER = 3;
    private static final int ORIGIN = 4;

    private long user_id;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(DbPressure.PROVIDER_NAME, DbPressure.Pressure.TABLE_NAME, PRESSURE);
        uriMatcher.addURI(DbGlucose.PROVIDER_NAME, DbGlucose.Glucose.TABLE_NAME, GLUCOSE);
        uriMatcher.addURI(DbStep.PROVIDER_NAME, DbStep.Step.TABLE_NAME, STEP);
        uriMatcher.addURI(DbUser.PROVIDER_NAME, DbUser.User.TABLE_NAME, USER);
        uriMatcher.addURI(DbOrigin.PROVIDER_NAME, DbOrigin.Origin.TABLE_NAME, ORIGIN);
    }

    public SQLiteDatabase database;
    private Context context;

    @Override
    public boolean onCreate() {
        return false;
    }


    public void init(Context context) {
        this.context = context;
        DbHelper dbHelper = new DbHelper(context);
        database = dbHelper.getWritableDatabase();
        user_id = Integer.parseInt(MyApplication.getInstance().getUser_id());
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long rowId;

        switch (uriMatcher.match(uri)) {
            case PRESSURE:

                rowId = database.insert(DbPressure.Pressure.TABLE_NAME, null, contentValues);
                if (rowId > 0) {
                    uri = ContentUris.withAppendedId(DbPressure.CONTENT_URI, rowId);
                    context.getContentResolver().notifyChange(uri, null);
                }
                break;
            case GLUCOSE:

                rowId = database.insert(DbGlucose.Glucose.TABLE_NAME, null, contentValues);
                if (rowId > 0) {
                    uri = ContentUris.withAppendedId(DbGlucose.CONTENT_URI, rowId);
                    context.getContentResolver().notifyChange(uri, null);
                }
                break;

            case STEP:

                rowId = database.insert(DbStep.Step.TABLE_NAME, null, contentValues);
                if (rowId > 0) {
                    uri = ContentUris.withAppendedId(DbStep.CONTENT_URI, rowId);
                    context.getContentResolver().notifyChange(uri, null);
                }
                break;
            case USER:

                rowId = database.insert(DbUser.User.TABLE_NAME, null, contentValues);
                if (rowId > 0) {
                    uri = ContentUris.withAppendedId(DbUser.CONTENT_URI, rowId);
                    context.getContentResolver().notifyChange(uri, null);
                }
                break;
            case ORIGIN:

                rowId = database.insert(DbOrigin.Origin.TABLE_NAME, null, contentValues);
                if (rowId > 0) {
                    uri = ContentUris.withAppendedId(DbOrigin.CONTENT_URI, rowId);
                    context.getContentResolver().notifyChange(uri, null);
                }
                break;


            default:
                Log.e("db_product", "exception!");
                throw new SQLException("Failed to insert row into" + uri);
        }
        return uri;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case PRESSURE:
                queryBuilder.setTables(DbPressure.Pressure.TABLE_NAME);
                if (sortOrder == null) {
                    sortOrder = DbPressure.Pressure.DEFAULT_SORT_ORDER;
                }
                break;
            case GLUCOSE:
                queryBuilder.setTables(DbGlucose.Glucose.TABLE_NAME);
                if (sortOrder == null) {
                    sortOrder = DbGlucose.Glucose.DEFAULT_SORT_ORDER;
                }
                break;
            case STEP:
                queryBuilder.setTables(DbStep.Step.TABLE_NAME);
                if (sortOrder == null) {
                    sortOrder = DbStep.Step.DEFAULT_SORT_ORDER;
                }
                break;
            case USER:
                queryBuilder.setTables(DbUser.User.TABLE_NAME);
                if (sortOrder == null) {
                    sortOrder = DbUser.User.DEFAULT_SORT_ORDER;
                }
                break;
            case ORIGIN:
                queryBuilder.setTables(DbOrigin.Origin.TABLE_NAME);
                if (sortOrder == null) {
                    sortOrder = DbOrigin.Origin.DEFAULT_SORT_ORDER;
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Cursor c = queryBuilder.query(database, projection, selection, selectionArgs,
                null, null, sortOrder);
        c.setNotificationUri(context.getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = 0;

        switch (uriMatcher.match(uri)) {
            case PRESSURE:
                rowsDeleted = database.delete(DbPressure.Pressure.TABLE_NAME, selection, selectionArgs);
                break;
            case GLUCOSE:
                rowsDeleted = database.delete(DbGlucose.Glucose.TABLE_NAME, selection, selectionArgs);
                break;
            case STEP:
                rowsDeleted = database.delete(DbStep.Step.TABLE_NAME, selection, selectionArgs);
                break;
            case USER:
                rowsDeleted = database.delete(DbUser.User.TABLE_NAME, selection, selectionArgs);
                break;
            case ORIGIN:
                rowsDeleted = database.delete(DbOrigin.Origin.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri" + uri);
        }
        context.getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int rowsUpdated = 0;

        switch (uriMatcher.match(uri)) {
            case PRESSURE:
                rowsUpdated = database.update(DbPressure.Pressure.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case GLUCOSE:
                rowsUpdated = database.update(DbGlucose.Glucose.TABLE_NAME, contentValues, selection, selectionArgs);
                break;

            case STEP:
                rowsUpdated = database.update(DbStep.Step.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case USER:
                rowsUpdated = database.update(DbUser.User.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case ORIGIN:
                rowsUpdated = database.update(DbOrigin.Origin.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri" + uri);
        }
        context.getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    public String getUserName() {
        String[] projection = new String[] {DbUser.User.USER_ID,
                DbUser.User.NAME};
        String[] args = {String.valueOf(user_id)};
        Cursor c = query(DbUser.CONTENT_URI, projection, "user_id = ?", args, null);
        if (c.moveToFirst()) {
            return c.getString(c.getColumnIndexOrThrow(DbUser.User.NAME));
        } else {
            return "";
        }
    }

    public void UpdateUser(String name) {
        String[] projection = new String[] {DbUser.User.USER_ID,
                DbUser.User.NAME};
        String[] args = {String.valueOf(user_id)};
        Cursor c = query(DbUser.CONTENT_URI, projection, "user_id = ?", args, null);
        ContentValues values = new ContentValues();
        values.put("user_id", String.valueOf(user_id));
        values.put("user_name", name);
        if (!c.moveToFirst()) {
            insert(DbUser.CONTENT_URI, values);
            System.out.println("Insert success");
        } else {
            update(DbUser.CONTENT_URI, values, "user_id = ?", args);
            System.out.println("update success");
        }
        c.close();
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    public Pressure getLastPressure() {
        Pressure pressure = null;

        String[] projection = new String[] {DbPressure.Pressure.TIME,
                DbPressure.Pressure.HIGH,
                DbPressure.Pressure.LOW,
                DbPressure.Pressure.RATE};
        String[] args = {String .valueOf(user_id)};
        Cursor c = query(DbPressure.CONTENT_URI, projection, "user_id = ?", args, null);

        if (c != null) {
            if (c.moveToLast()) {
                pressure = new Pressure();
                pressure.setTime(c.getString(c.getColumnIndexOrThrow(DbPressure.Pressure.TIME)));
                pressure.setHigh(c.getInt(c.getColumnIndexOrThrow(DbPressure.Pressure.HIGH)));
                pressure.setLow(c.getInt(c.getColumnIndexOrThrow(DbPressure.Pressure.LOW)));
                pressure.setRate(c.getInt(c.getColumnIndexOrThrow(DbPressure.Pressure.RATE)));

            }
        }
        c.close();

        return pressure;
    }
    public Glucose getLastGlucose(String type) {
        Glucose glucose = null;

        String[] projection = new String[] {DbGlucose.Glucose.TIME,
                DbGlucose.Glucose.TYPE,
                DbGlucose.Glucose.VALUE};
        String[] args = {type, String.valueOf(user_id)};
        Cursor c = query(DbGlucose.CONTENT_URI, projection, "type = ? and user_id = ?", args, null);

        if (c != null) {
            if (c.moveToLast()) {
                glucose = new Glucose();
                glucose.setTime(c.getString(c.getColumnIndexOrThrow(DbGlucose.Glucose.TIME)));
                glucose.setType(c.getString(c.getColumnIndexOrThrow(DbGlucose.Glucose.TYPE)));
                glucose.setValue(c.getInt(c.getColumnIndexOrThrow(DbGlucose.Glucose.VALUE)));
            }
        }
        c.close();

        return glucose;
    }

    public List<Pressure> getPressure(String date) {
        String[] args = null;
        switch (date) {
            case "day":
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                args = new String[]{sdf.format(new Date()), String.valueOf(user_id)};
                break;
            case "week":
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, -7);
                Date d = c.getTime();
                args = new String[]{sdf2.format(d), String.valueOf(user_id)};
                break;
            case "month":
                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy年MM月dd日");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -30);
                Date newDate = calendar.getTime();
                args = new String[]{sdf3.format(newDate), String.valueOf(user_id)};
                break;
        }



        List<Pressure> pressureList = new ArrayList<Pressure>();

        String[] projection = new String[] {DbPressure.Pressure.TIME,
                DbPressure.Pressure.HIGH,
                DbPressure.Pressure.LOW,
                DbPressure.Pressure.RATE};

        Cursor c = query(DbPressure.CONTENT_URI, projection, "time > ? and user_id = ?", args, null);

        if (c != null) {

            if (c.moveToFirst()) {
                Log.e("getPressureByDay", "here");
                do {
                    Pressure pressure = new Pressure();
                    pressure.setTime(c.getString(c.getColumnIndexOrThrow(DbPressure.Pressure.TIME)));
                    pressure.setHigh(c.getInt(c.getColumnIndexOrThrow(DbPressure.Pressure.HIGH)));
                    pressure.setLow(c.getInt(c.getColumnIndexOrThrow(DbPressure.Pressure.LOW)));
                    pressure.setRate(c.getInt(c.getColumnIndexOrThrow(DbPressure.Pressure.RATE)));

                    pressureList.add(pressure);
                } while (c.moveToNext());
            }
            c.close();
        }
        //倒序输出
        Collections.reverse(pressureList);
        return pressureList;
    }

    public List<Glucose> getGlucose(String date, String type) {
        String[] args = null;
        switch (date) {
            case "day":
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                args = new String[]{sdf.format(new Date()), type, String.valueOf(user_id)};
                break;
            case "week":
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, -7);
                Date d = c.getTime();
                args = new String[]{sdf2.format(d), type, String.valueOf(user_id)};
                break;
            case "month":
                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy年MM月");
                args = new String[]{sdf3.format(new Date()), type, String.valueOf(user_id)};
                break;
        }


        List<Glucose> glucoseList = new ArrayList<Glucose>();

        String[] projection = new String[] {DbGlucose.Glucose.TIME,
                DbGlucose.Glucose.TYPE,
                DbGlucose.Glucose.VALUE};
        Cursor c = query(DbGlucose.CONTENT_URI, projection, "time > ? and type = ? and user_id = ?", args, null);

        if (c != null) {

            if (c.moveToFirst()) {
                do {
                    Glucose glucose = new Glucose();
                    glucose.setTime(c.getString(c.getColumnIndexOrThrow(DbGlucose.Glucose.TIME)));
                    glucose.setType(c.getString(c.getColumnIndexOrThrow(DbGlucose.Glucose.TYPE)));
                    glucose.setValue(c.getInt(c.getColumnIndexOrThrow(DbGlucose.Glucose.VALUE)));


                    glucoseList.add(glucose);
                } while (c.moveToNext());
            }
            c.close();
        }
        //倒序输出
        Collections.reverse(glucoseList);
        return glucoseList;
    }

    public void saveStep(Step step) {
        if (step != null) {
            ContentValues values = new ContentValues();
            values.put("user_id", user_id);
            values.put("number", step.getNumber());
            values.put("date", step.getDate());
            insert(DbStep.CONTENT_URI, values);
        }
    }
    public void updateStep(Step step) {
        if (step != null) {
            ContentValues values = new ContentValues();
            values.put("number", step.getNumber());
            values.put("date", step.getDate());
            String[] args = {step.getDate(), String.valueOf(user_id)};
            update(DbStep.CONTENT_URI, values, "date = ? and user_id = ?", args);
        }
    }

    public Step loadStep(String date) {
        Step step = null;
        String[] projection = new String[] {DbStep.Step.NUMBER,
                DbStep.Step.DATE};
        Log.e("user_id", String.valueOf(user_id));
        String[] args = {date, String.valueOf(user_id)};
        Cursor c = query(DbStep.CONTENT_URI, projection, "date = ? and user_id = ?", args, null);
        if (c.moveToFirst()) {
            do {
                step = new Step();
                step.setNumber(c.getInt(c.getColumnIndexOrThrow("number")));
                step.setDate(c.getString(c.getColumnIndexOrThrow("date")));
            } while (c.moveToNext());
        }
        c.close();
        return step;
    }

    public List<Step> getStepByMonth() {
        List<Step> stepList = new ArrayList<>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        date = calendar.getTime();

        String start_date = sdf.format(date);

        Step step = null;
        String[] projection = new String[] {DbStep.Step.NUMBER,
                DbStep.Step.DATE};

        String[] args = {start_date, String.valueOf(user_id)};
        Cursor c = query(DbStep.CONTENT_URI, projection, "date >= ? and user_id = ?", args, null);
        if (c.moveToFirst()) {
            do {
                step = new Step();
                step.setNumber(c.getInt(c.getColumnIndexOrThrow("number")));
                step.setDate(c.getString(c.getColumnIndexOrThrow("date")));
                stepList.add(step);
            } while (c.moveToNext());
        }
        c.close();
        //倒序输出
        Collections.reverse(stepList);
        return stepList;

    }
    public List<OriginData> getOrigindata() {
        List<OriginData> dataList = new ArrayList<>();
        OriginData origin = null;
        String[] projection = new String[] {DbOrigin.Origin.AGE,
                DbOrigin.Origin.SEX, DbOrigin.Origin.SMOKE, DbOrigin.Origin.DRINK,
                DbOrigin.Origin.HISTORY, DbOrigin.Origin.WEIGHTINDEX, DbOrigin.Origin.BLOODSUGAR,
                DbOrigin.Origin.CHOL, DbOrigin.Origin.PRESSURE, DbOrigin.Origin.HIGH,
                DbOrigin.Origin.LOW, DbOrigin.Origin.RATE, DbOrigin.Origin.DIZZINESS,
                DbOrigin.Origin.HEADACHE, DbOrigin.Origin.NOSE};
        Cursor c = query(DbOrigin.CONTENT_URI, projection, null, null, null);
        if (c.moveToFirst()) {
            do {
                origin = new OriginData();
                origin.setAge(c.getInt(c.getColumnIndexOrThrow(DbOrigin.Origin.AGE)));
                origin.setSex(c.getString(c.getColumnIndexOrThrow(DbOrigin.Origin.SEX)));
                origin.setSmoke(c.getString(c.getColumnIndexOrThrow(DbOrigin.Origin.SMOKE)));
                origin.setDrink(c.getString(c.getColumnIndexOrThrow(DbOrigin.Origin.DRINK)));
                origin.setHistory(c.getString(c.getColumnIndexOrThrow(DbOrigin.Origin.HISTORY)));
                origin.setWeightindex(c.getDouble(c.getColumnIndexOrThrow(DbOrigin.Origin.WEIGHTINDEX)));
                origin.setBloodsugar(c.getString(c.getColumnIndexOrThrow(DbOrigin.Origin.BLOODSUGAR)));
                origin.setChol(c.getString(c.getColumnIndexOrThrow(DbOrigin.Origin.CHOL)));
                origin.setPressure(c.getString(c.getColumnIndexOrThrow(DbOrigin.Origin.PRESSURE)));
                origin.setHigh(c.getInt(c.getColumnIndexOrThrow(DbOrigin.Origin.HIGH)));
                origin.setLow(c.getInt(c.getColumnIndexOrThrow(DbOrigin.Origin.LOW)));
                origin.setRate(c.getInt(c.getColumnIndexOrThrow(DbOrigin.Origin.RATE)));
                origin.setDizziness(c.getString(c.getColumnIndexOrThrow(DbOrigin.Origin.DIZZINESS)));
                origin.setHeadache(c.getString(c.getColumnIndexOrThrow(DbOrigin.Origin.HEADACHE)));
                origin.setNose(c.getString(c.getColumnIndexOrThrow(DbOrigin.Origin.NOSE)));

                dataList.add(origin);
            } while (c.moveToNext());
        }
        c.close();

        return dataList;
    }




}
