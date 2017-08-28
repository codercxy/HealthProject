package com.nju.android.health.model.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nju.android.health.model.data.User;
import com.nju.android.health.providers.CommDB;

import java.util.ArrayList;

/**
 * Created by 57248 on 2016/8/28.
 */
public class UserDB {
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String RECENTLOGINTIME = "recentLoginTime";
    public static final String REGISTERTIME = "registerTime";

    //创建用户表
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE IF NOT EXISTS "+UserDB.SQLITE_TABLE+" ("+
                    UserDB.ID+" integer PRIMARY KEY autoincrement,"+
                    UserDB.NAME+" TEXT,"+
                    UserDB.PASSWORD+" TEXT);";


    private static final String TAG = "UserDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public static final String SQLITE_TABLE = "UserTable";
    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, CommDB.DATABASE_NAME, null, CommDB.DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            Log.d("SQLiteDemo","---------onCreate invoked-------------");
            db.execSQL(CREATE_TABLE_USER);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgragding database from version"+oldVersion+"to"
                    +newVersion+"which will distroy all old version");
            db.execSQL("DROP TABLE IF EXISTS"+SQLITE_TABLE);
            onCreate(db);

        }

    }
    public UserDB(Context ctx){
        this.mCtx = ctx;
    }

    public void open(){
        mDbHelper = new DatabaseHelper(mCtx, SQLITE_TABLE, null, CommDB.DATABASE_VERSION);
    }

    public void close(){
        if(mDbHelper != null){
            mDbHelper.close();
        }
    }

    public void creatUser(String name,String password){
        mDb = mDbHelper.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(NAME, name);
        initialValues.put(PASSWORD, password);
//		mDb.execSQL("insert into UserTable (_id,name,password) values(?,?,?)", new Object[]{1,name,password});
        mDb.insert(SQLITE_TABLE, null, initialValues);

    }

	/*public boolean deleteAllUsers(){9
		int doneDelete = 0;
		try{
		doneDelete = mDb.delete(SQLITE_TABLE, null, null);
		Log.w(TAG, Integer.toString(doneDelete));
		Log.e("doneDelete", doneDelete + "");
		}catch (Exception e){
			e.printStackTrace();
		}
		return doneDelete > 0;
	}

	public boolean deleteByName(String name){
		int isDelete;
		String[] tName;
		tName = new String[] {name};
		isDelete = mDb.delete(SQLITE_TABLE, null, tName);
		return isDelete > 0;
	}*/

    public ArrayList<User> fetchAll(){
        mDb = mDbHelper.getReadableDatabase();
        ArrayList<User> list = new ArrayList<User>();
        Cursor mCursor = null;
        mCursor = mDb.query(SQLITE_TABLE, new String[] {ID,NAME,PASSWORD},
                null, null, null, null, null);

        if (mCursor != null){
            if(mCursor.moveToFirst()){
                do{
                    User user = new User();
                    user.setName(mCursor.getString(mCursor.getColumnIndexOrThrow(NAME)));
                    user.setPassword(mCursor.getString(mCursor.getColumnIndexOrThrow(PASSWORD)));
                    list.add(user);
                }while(mCursor.moveToNext());
            }
            mCursor.close();
        }

        return list;
    }
}
