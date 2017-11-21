package com.nju.android.health.providers;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by chy on 2017/11/21.
 */

public class DbOrigin {
    public static final String PROVIDER_NAME = "com.nju.android.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/" +
            Origin.TABLE_NAME);

    public final static String DB_NAME = "health.db";
    public final static int DB_VERSION = 1;

    public class Origin {
        public static final String TABLE_NAME = "origin";
        public static final String ID = BaseColumns._ID;
        public static final String AGE = "age";
        public static final String SEX = "sex";
        public static final String SMOKE = "smoke";
        public static final String DRINK = "drink";
        public static final String HISTORY = "history";
        public static final String WEIGHTINDEX = "weight_index";
        public static final String BLOODSUGAR = "blood_sugar";
        public static final String CHOL = "chol";

        public static final String PRESSURE = "pressure";

        public static final String HIGH = "high";
        public static final String LOW = "low";
        public static final String RATE = "rate";
        public static final String DIZZINESS = "dizziness";
        public static final String HEADACHE = "headache";
        public static final String NOSE = "nose";

        public static final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                AGE + " INTEGER," +
                SEX + " TEXT," +
                SMOKE + " TEXT," +
                DRINK + " TEXT," +
                HISTORY + " TEXT," +
                WEIGHTINDEX + " DOUBLE," +
                BLOODSUGAR + " TEXT," +
                CHOL + " TEXT," +
                PRESSURE + " TEXT," +
                HIGH + " INTEGER," +
                LOW + " INTEGER," +
                RATE + " INTEGER," +
                DIZZINESS + " TEXT," +
                HEADACHE + " TEXT," +
                NOSE + " TEXT);";


        public static final String DEFAULT_SORT_ORDER = ID + " ASC";

    }


}
