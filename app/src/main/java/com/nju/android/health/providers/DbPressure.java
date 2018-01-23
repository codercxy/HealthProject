package com.nju.android.health.providers;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by 57248 on 2016/10/11.
 */

public class DbPressure {
    public static final String PROVIDER_NAME = "com.nju.android.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/" +
            Pressure.TABLE_NAME);

    public final static String DB_NAME = "health.db";
    public final static int DB_VERSION = 1;

    public class Pressure {
        public static final String TABLE_NAME = "pressure";
        public static final String ID = BaseColumns._ID;
        public static final String USER_ID = "user_id";
        public static final String TIME = "time";
        public static final String HIGH = "high";
        public static final String LOW = "low";
        public static final String RATE = "rate";
        public static final String ISSEND = "issend";

        public static final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER_ID + " INTEGER," +
                TIME + " TEXT," +
                HIGH + " INTEGER," +
                LOW + " INTEGER," +
                RATE + " INTEGER," +
                ISSEND + " TEXT);";

        public static final String DEFAULT_SORT_ORDER = ID + " ASC";
    }
}
