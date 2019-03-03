package com.example.proyecto1.utilities;

import android.content.Context;
import android.database.Cursor;
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
        db.execSQL("CREATE TABLE Tags ('id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                " 'name' CHAR(255) NOT NULL UNIQUE, 'username' INTEGER, FOREIGN KEY('username') "+
                " REFERENCES Users('username') ON DELETE CASCADE)");

        // Create table Notes
        db.execSQL("CREATE TABLE Notes ('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'title' CHAR(255) NOT NULL, 'fileContent' CHAR(255) NOT NULL, 'date' DATETIME " +
                "NOT NULL DEFAULT CURRENT_TIMESTAMP, 'labelId' INTEGER, 'username' INTEGER, " +
                "FOREIGN KEY('labelId') REFERENCES Tags('id') ON DELETE SET NULL, " +
                "FOREIGN KEY('username') REFERENCES Users('username') ON DELETE CASCADE)");

        // Insert dummy data
        db.execSQL("INSERT INTO Users VALUES ('admin', '1111')");
        db.execSQL("INSERT INTO Tags(id, name, username) VALUES (1, 'tagPrueba', 'admin')");
        db.execSQL("INSERT INTO Notes(fileContent, labelId, title, username) VALUES ('prueba', 1, 'this is the title', 'admin')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        // Delete the existing tables
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Tags");
        db.execSQL("DROP TABLE IF EXISTS Notes");

        // Create the tables again
        onCreate(db);
    }

    /*
     * Checks if a username exists in database
     * */
    public Boolean checkIfUsernameExists(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT username FROM Users WHERE username='" + username + "'", null);

        if (c.moveToNext() != false) {
            // there is a user with these data
            return true;
        }
        c.close();
        db.close();
        return false;
    }

    /*
    * Checks is a username exists with that password
    * */
    public Boolean checkIfUserCanBeLoggedIn(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT username FROM Users WHERE username='" + username + "' AND password='"+password + "'", null);

        if (c.moveToNext() != false) {
            // there is a user with these data
            return true;
        }
        c.close();
        db.close();
        return false;
    }
}
