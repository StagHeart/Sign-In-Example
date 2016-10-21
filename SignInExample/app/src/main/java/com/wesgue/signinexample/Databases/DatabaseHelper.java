package com.wesgue.signinexample.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wesgue.signinexample.Databases.Models.Session;
import com.wesgue.signinexample.Databases.Models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wesley Gue on 10/10/2016.
 * <p>
 * Server Database Helper
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1; // Database Version Number

    private static final String DATABASE_NAME = "Server.db"; // Database Name

    /*
    User
     */

    // USER
    public static final String TABLE_USER = "user_table"; // user table
    public static final String USER_PRIMARY_ID = "ID";  // unique ID
    public static final String USER_SERVER_ID = "USER_SERVER_ID";  // unique ID
    public static final String USER_EMAIL = "EMAIL"; // user email
    public static final String USER_SESSION_ID = "SESSION_SERVER_ID";  // session ID

    // User Columns
    private String[] USER_COLUMNS = {USER_PRIMARY_ID,
            USER_SERVER_ID,
            USER_EMAIL,
            USER_SESSION_ID};

    // TABLE_USER execute
    private String table_user = "create table " + TABLE_USER + "(" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "USER_SERVER_ID TEXT," +
            "EMAIL TEXT," +
            "SESSION_SERVER_ID TEXT)";

    /*
    Session
     */

    //Session
    public static final String TABLE_SESSION = "session_table"; // user session
    public static final String SESSION_PRIMARY_ID = "ID";
    public static final String SESSION_SERVER_ID = "SESSION_SERVER_ID";

    // Session Columns
    private String[] SESSION_COLUMNS = {SESSION_PRIMARY_ID,
            SESSION_SERVER_ID};

    // TABLE_SESSION execute
    private String table_session = "create table " + TABLE_SESSION + "(" +
            "ID INTEGER PRIMARY KEY," +
            "SESSION_SERVER_ID TEXT," +
            "FOREIGN KEY(SESSION_SERVER_ID) REFERENCES user_table(SESSION_SERVER_ID))";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(table_user);
        db.execSQL(table_session);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);
        onCreate(db);
    }


    // Adding new user
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_SERVER_ID, user.getUser_id());
        values.put(USER_EMAIL, user.getEmail());
        values.put(USER_SESSION_ID, user.getSession_id());

        //Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }


    // Get single user
    public User getUser(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, USER_COLUMNS, USER_SERVER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));

        return user;
    }


    // Get User by session ID
    public User getUserBySessionId(String sessionId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, USER_COLUMNS, USER_SESSION_ID + "=?",
                new String[]{String.valueOf(sessionId)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));

        return user;
    }


    // Get all users
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_USER, null, null, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUser_id(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setSession_id(cursor.getString(3));

                // Adding contact to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return contact list
        return userList;
    }


    // Getting user count
    public int getUserCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, null, null, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }


    // Updating single user
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_SERVER_ID, user.getUser_id());
        values.put(USER_EMAIL, user.getEmail());
        values.put(USER_SESSION_ID, user.getSession_id());

        //updating row
        return db.update(TABLE_USER,
                values,
                USER_PRIMARY_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
    }


    // Updating single users session to null
    public int setUserSessionToNull(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_SERVER_ID, user.getUser_id());
        values.put(USER_EMAIL, user.getEmail());
        values.putNull(USER_SESSION_ID);

        //updating row
        return db.update(TABLE_USER,
                values,
                USER_PRIMARY_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
    }


    // Deleting single user
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, USER_PRIMARY_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }


    public boolean checkIfUserIdExist(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, USER_COLUMNS,
                USER_SERVER_ID + "=?",
                new String[]{userId}, null, null, null, null);


        if (cursor == null || cursor.getCount() == 0)
            return false;

        else
            return true;

    }


    // Adding new session
    public void addSession(Session session) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SESSION_SERVER_ID, session.getSession_server_id());

        //Inserting Row
        db.insert(TABLE_SESSION, null, values);
        db.close();
    }


    // Getting single session
    public Session getSession(int sessionId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SESSION, SESSION_COLUMNS, SESSION_PRIMARY_ID + "=?",
                new String[]{String.valueOf(sessionId)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Session session = new Session(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1));

        return session;
    }


    // Deleting single session
    public void deleteSession(Session session) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_SESSION, SESSION_PRIMARY_ID + " = ?",
                new String[]{String.valueOf(session.getId())});
        db.close();
    }

    // Getting session count
    public int getSessionCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SESSION, null, null, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public boolean doesSessionExist() {

        int sessionCount = getSessionCount();
        if (sessionCount == 0) {
            return false;
        } else {
            return true;
        }

    }


    public boolean isTableExists(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();

        boolean isExist = false;
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                isExist = true;
            }
            cursor.close();
        }
        return isExist;

    }
}

