package com.nju.android.health.providers;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by chy on 2017/9/14.
 */

public class DbUser {
    public static final String PROVIDER_NAME = "com.nju.android.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/" +
            DbUser.User.TABLE_NAME);

    public final static String DB_NAME = "health.db";
    public final static int DB_VERSION = 1;

    public class User {
        public static final String TABLE_NAME = "user";
        public static final String ID = BaseColumns._ID;
        public static final String USER_ID = "user_id";
        public static final String NAME = "user_name";

        public static final String CREATE_SQL =  "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER_ID + " INTEGER," +
                NAME + " TEXT);";
        public static final String DEFAULT_SORT_ORDER = ID + " ASC";
    }
}
