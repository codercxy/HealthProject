package com.nju.android.health.providers;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by 57248 on 2016/10/19.
 */

public class DbStep {
    public static final String PROVIDER_NAME = "com.nju.android.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/" +
            Step.TABLE_NAME);

    public final static String DB_NAME = "health.db";
    public final static int DB_VERSION = 1;

    public class Step {
        public static final String TABLE_NAME = "step";
        public static final String ID = BaseColumns._ID;
        public static final String USER_ID = "user_id";
        public static final String NUMBER = "number";
        public static final String DATE = "date";

        public static final String CREATE_SQL =  "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER_ID + " INTEGER," +
                NUMBER + " INTEGER," +
                DATE + " TEXT);";
        public static final String DEFAULT_SORT_ORDER = ID + " ASC";
    }
}
