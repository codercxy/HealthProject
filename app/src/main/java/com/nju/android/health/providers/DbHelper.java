package com.nju.android.health.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 57248 on 2016/10/11.
 */

public class DbHelper extends SQLiteOpenHelper{

    public DbHelper(Context context) {
        super(context, DbPressure.DB_NAME, null, DbPressure.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (db.isReadOnly()) {
            db = getWritableDatabase();
        }
        db.execSQL(DbPressure.Pressure.CREATE_SQL);
        db.execSQL(DbGlucose.Glucose.CREATE_SQL);
        db.execSQL(DbStep.Step.CREATE_SQL);
        db.execSQL("create table if not exists records(id integer primary key autoincrement,name varchar(200));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
