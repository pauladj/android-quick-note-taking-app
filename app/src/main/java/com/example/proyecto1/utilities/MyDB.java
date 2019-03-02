package com.example.proyecto1.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.proyecto1.R;

public class MyDB extends SQLiteOpenHelper {

    public MyDB(@Nullable Context context, @Nullable String name,
                @Nullable SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table Users
        db.execSQL("CREATE TABLE Users ('username' CHAR(255) PRIMARY KEY NOT NULL, 'password' " +
                "CHAR(255) NOT NULL)");

        // Create table Tags
        db.execSQL("CREATE TABLE Tags ('id' INT PRIMARY KEY AUTOINCREMENT," +
                "'name' CHAR(255) NOT NULL UNIQUE)");

        // Create table Notes
        db.execSQL("CREATE TABLE Notes ('id' INT PRIMARY KEY AUTOINCREMENT, " +
                "'title' CHAR(255) NOT NULL, 'fileContent' CHAR(255) NOT NULL, 'date' DATETIME " +
                "NOT NULL DEFAULT CURRENT_TIMESTAMP, 'labelId' INT, FOREIGN KEY('label') " +
                "REFERENCES Tags('id') ON DELETE SET NULL)");
    }

    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        // Delete the existing tables
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Tags");
        db.execSQL("DROP TABLE IF EXISTS Notes");

        // Create the tables again
        onCreate(db);
    }
}
