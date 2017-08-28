package com.nju.android.health.providers;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Administrator on 2016/10/12.
 */
public class DbGlucose {
    public static final String PROVIDER_NAME = "com.nju.android.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/" +
            Glucose.TABLE_NAME);

    public final static String DB_NAME = "health.db";
    public final static int DB_VERSION = 1;

    public class Glucose {
        public static final String TABLE_NAME = "glucose";
        public static final String ID = BaseColumns._ID;
        public static final String USER_ID = "user_id";
        public static final String TIME = "time";
        public static final String TYPE = "type";
        public static final String VALUE = "value";


        public static final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER_ID + " INTEGER," +
                TIME + " TEXT," +
                TYPE + " TEXT," +
                VALUE + " INTEGER);";

        public static final String DEFAULT_SORT_ORDER = ID + " ASC";
    }
}
