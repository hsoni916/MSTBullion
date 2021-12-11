package com.example.mstbullion;

import static com.google.firestore.v1.StructuredQuery.CompositeFilter.Operator.AND;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DBManager {

    private DBHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;
    public static int Column_Count;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String Uname, String passkey,String phone, int Status ) {
        Log.d("SQL","insert");
        String sql = "INSERT INTO credentials (Username, Password, PhoneNumber, SignIn) VALUES (?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindString(1, Uname);
        statement.bindString(2, passkey);
        statement.bindString(3,phone);
        statement.bindDouble(4, Status);
        statement.executeInsert();
        close();
    }

    public boolean check(){
        int count;
        Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='credentials' ", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                try {
                    cursor = database.rawQuery("SELECT * FROM credentials",null);
                    cursor.moveToFirst();
                    if(cursor.getCount()>0){
                        Column_Count = cursor.getColumnCount();
                        count = cursor.getCount();
                        if (!cursor.isClosed())
                            cursor.close();
                        return count > 0;
                    }else{
                        return false;
                    }
                }catch (Error|SQLiteBindOrColumnIndexOutOfRangeException e){
                    Log.d("Error",e.getMessage());
                    return false;
                }
            }
            cursor.close();
        }
        return false;
    }

    public int getCount() {
        return Column_Count;
    }

    public boolean usersignin(String userName, String passwordString) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String select = "SELECT * FROM credentials WHERE Username ='" + userName + "' AND Password='" + passwordString + "'";
        Cursor c = db.rawQuery(select, null);
        if (c.moveToFirst()) {
            ContentValues cv = new ContentValues();
            cv.put("SignIn",0);
            db.update("credentials", cv, "Username=?", new String[]{userName});
            return true;
        }
        c.close();
        db.close();
        return false;
    }

    public boolean usersignedIn() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String select = "SELECT * FROM credentials";
        Cursor c = db.rawQuery(select,null);
        if(c.moveToFirst()){
            Log.d("Column Count:", String.valueOf(c.getColumnCount()));
            Log.d("Status", String.valueOf(c.getDouble(3)));
            if(c.getDouble(4)==0.0){
                Log.d("True","SignedIn");
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    public String getUser() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String select = "SELECT * FROM credentials";
        Cursor c = db.rawQuery(select,null);
        if(c.moveToFirst()){
            if(!c.getString(0).isEmpty()){
                return c.getString(0);
            }
        }
        return null;
    }

    public boolean deleteUser(String Uname, String passkey,String phone){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = db.delete("credentials","Username = ?"  + "Password = ?" + "PhoneNumber = ?", new String[] {Uname, passkey, phone});
        return count > 0;
    }

    public boolean logout(String userName){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String select = "SELECT * FROM credentials WHERE Username ='" + userName + "'";
        Cursor c = db.rawQuery(select, null);
        if (c.moveToFirst()) {
            Log.d("in","method");
            ContentValues cv = new ContentValues();
            cv.put("SignIn",1.0);
            int count = db.update("credentials", cv, "Username=?", new String[]{userName});
            Log.d("rows affected", String.valueOf(count));
            return true;
        }
        c.close();
        db.close();
        return false;
    }
}
