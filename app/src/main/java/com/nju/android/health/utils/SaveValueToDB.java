package com.nju.android.health.utils;

import android.content.ContentValues;
import android.content.Context;

import com.nju.android.health.MyApplication;
import com.nju.android.health.providers.DbGlucose;
import com.nju.android.health.providers.DbPressure;
import com.nju.android.health.providers.DbProvider;

import java.io.Closeable;

/**
 * Created by chy on 2017/6/26.
 */

public class SaveValueToDB {

    public void savePressureValue(Context context, String time, String high, String low, String rate) {
        DbProvider provider = new DbProvider();
        provider.init(context);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbPressure.Pressure.USER_ID, Integer.parseInt(MyApplication.getInstance().getUser_id()));
        contentValues.put(DbPressure.Pressure.TIME, time);
        contentValues.put(DbPressure.Pressure.HIGH, Integer.parseInt(high));
        contentValues.put(DbPressure.Pressure.LOW, Integer.parseInt(low));
        contentValues.put(DbPressure.Pressure.RATE, Integer.parseInt(rate));

        provider.insert(DbPressure.CONTENT_URI, contentValues);


        provider.shutdown();
    }

    private void saveGluValue(Context context, String time, String type, String value) {
        DbProvider provider = new DbProvider();
        provider.init(context);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbGlucose.Glucose.USER_ID, Integer.parseInt(MyApplication.getInstance().getUser_id()));
        contentValues.put(DbGlucose.Glucose.TIME, time);
        contentValues.put(DbGlucose.Glucose.TYPE, type);
        contentValues.put(DbGlucose.Glucose.VALUE, Integer.parseInt(value));

        provider.insert(DbGlucose.CONTENT_URI, contentValues);
        provider.shutdown();
    }
}
