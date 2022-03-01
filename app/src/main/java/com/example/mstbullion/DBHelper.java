package com.example.mstbullion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DBHelper extends SQLiteOpenHelper {

    // Database Information
    static final String Username = "";
    static final String Password = "";
    static final String DB_NAME = "MSTGOLD.USERS";

    // database version
    static final int DB_VERSION = 1;

    private static final String CREATE_TABLE = "create table if not exists " + "credentials" +
            "(" + "Username" + " TEXT NOT NULL, " + "Business" + "TEXT NOT NULL," + "Password" + " TEXT NOT NULL, " + "PhoneNumber" + " TEXT NOT NULL, " + "GST" + " TEXT, " + "SignIn" + " INTEGER NOT NUll CHECK (SignIn IN (0, 1)));";


    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME,null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "credentials");
        onCreate(db);
    }
}
