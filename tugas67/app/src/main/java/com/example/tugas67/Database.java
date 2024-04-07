package com.example.tugas67;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "kontak.db";
    private static final int DATABASE_VERSION = 1;

    public Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table kontak(no integer primary key,nama text null,tgl text null,alamat text null);";
        Log.d("Data","onCreate: " + sql); db.execSQL(sql);
        sql = "INSERT INTO kontak (no,nama,tgl,alamat) VALUES ('0895381191380','Fatih Indra','140903','Kudus');"; db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db0, int db1, int db2) {

    }
}
