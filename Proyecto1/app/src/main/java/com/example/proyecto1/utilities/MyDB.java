package com.example.proyecto1.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyDB extends SQLiteOpenHelper {

    public MyDB(@Nullable Context context, @Nullable String name,
                @Nullable SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create table Tags
        db.execSQL("CREATE TABLE Tags ('id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                " 'name' CHAR(255) NOT NULL, 'username' INTEGER)");

        // Create table Notes
        db.execSQL("CREATE TABLE Notes ('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'title' CHAR(255) NOT NULL, 'fileContent' CHAR(255) NOT NULL UNIQUE, 'date' " +
                "DATETIME " +
                "NOT NULL DEFAULT (datetime('now','localtime')), 'labelId' INTEGER, 'username' CHAR(255)" +
                ", " +
                "FOREIGN KEY('labelId') REFERENCES Tags('id') ON DELETE SET NULL)");

        // Create table NoteImage
        db.execSQL("CREATE TABLE NoteImage ('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'noteId' CHAR(255) NOT NULL, 'imagePath' CHAR(255) NOT NULL, " +
                "FOREIGN KEY('noteId') REFERENCES Notes('id') ON DELETE CASCADE)");

        // Insert dummy data
        db.execSQL("INSERT INTO Tags(id, name, username) VALUES (1, 'tagPrueba', 'admin')");
        db.execSQL("INSERT INTO Tags(id, name, username) VALUES (2, 'tagPrueba2', 'admin')");
        db.execSQL("INSERT INTO Tags(id, name, username) VALUES (3, 'tagPrueba3', 'admin')");

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

    /**
     * Check if a username exists in database
     * @param username
     * @return True - exists, False - does not exist
     */
    public boolean checkIfUsernameExists(String username){
        SQLiteDatabase db = null;
        Cursor c = null;
        boolean exists;
        try {
            db = this.getReadableDatabase();
            c = db.rawQuery("SELECT username FROM Users WHERE username='" + username + "'", null);
            if (c.moveToNext()) {
                // there is a user with these data
                exists = true;
            }else{
                exists = false;
            }
        }catch (SQLException e){
            exists = true;
        }finally {
            if (c != null) {
                c.close();
            }
            if (db != null){
                db.close();
            }
        }

        return exists;
    }

    /**
     * Checks if a username exists with that password
     * @param username
     * @param password
     * @return True or False
     */
    public boolean checkIfUserCanBeLoggedIn(String username, String password){
        SQLiteDatabase db = null;
        Cursor c = null;
        boolean exists;
        try {
            db = this.getReadableDatabase();
            c = db.rawQuery("SELECT username FROM Users WHERE username='" + username + "' AND password='" + password + "'", null);
            if (c.moveToNext()) {
                // there is a user with these data
                exists = true;
            }else{
                exists = false;
            }
        }catch (SQLException e){
            exists = true;
        }finally {
            if (c != null) {
                c.close();
            }
            if (db != null){
                db.close();
            }
        }
        return exists;
    }

    /**
     * Sets the username as active
     * @param username
     * @return true if the user has been set as active
     */
    public boolean setUsernameAsActive(String username){
        SQLiteDatabase db = null;
        boolean changed;
        try{
            db = this.getWritableDatabase();
            ContentValues modification = new ContentValues();
            modification.put("active", 1);
            db.update("Users", modification, "username='" + username + "'", null);
            changed = true;
        }catch (SQLException e){
            changed = false;
        }finally {
            if (db != null){
                db.close();
            }
        }
        return changed;
    }

    /**
     * Sets the username as active
     * @param username
     * @return true if the username has been set as inactive
     */
    public boolean setUsernameAsInactive(String username){
        SQLiteDatabase db = null;
        boolean changed;
        try {
            db = this.getWritableDatabase();
            ContentValues modification = new ContentValues();
            modification.put("active", 0);
            db.update("Users", modification, "username='" + username + "'", null);
            changed = true;
        }catch (SQLException e){
            changed = false;
        }finally {
            if (db != null){
                db.close();
            }
        }
        return changed;
    }

    /**
     * Gets the active username if there's one
     * @return the active username or null
     */
    public String getActiveUsername(){
        SQLiteDatabase db = null;
        Cursor c = null;
        String username = null;
        try {
            db = this.getReadableDatabase();
            c = db.rawQuery("SELECT username FROM Users WHERE active=1", null);

            if (c.moveToNext()) {
                // there is a user with these data
                username = c.getString(0);
            }
        }catch (SQLException e){
            //
        }finally {
            if (c != null) {
                c.close();
            }
            if (db != null){
                db.close();
            }
        }
        return username;
    }

    /**
     * Get notes data to show on the main screen
     * @param username - of which we have to get the notes
     * @return ids, titles, dates and tags of the notes
     */
    public ArrayList<ArrayList<String>> getNotesDataByUser(String username){
        SQLiteDatabase db = null;
        Cursor c = null;
        ArrayList<ArrayList<String>> notesData = null;

        try {
            db = this.getReadableDatabase();
            c = db.rawQuery("SELECT Notes.id, title, date, name FROM Notes LEFT JOIN Tags ON " +
                            "Notes.labelId=Tags.id WHERE Notes.username='" + username + "' ORDER BY date ASC",
                    null);

            ArrayList<String> notesIds = new ArrayList<>();
            ArrayList<String> notesTitles = new ArrayList<>();
            ArrayList<String> notesDates = new ArrayList<>();
            ArrayList<String> notesTagsNames = new ArrayList<>();

            while (c.moveToNext()) {
                // there is a user with these data
                String id = c.getString(0);
                String title = c.getString(1);
                String date = c.getString(2);
                String tagName = null;
                if (c.getColumnCount() == 4){
                    tagName = c.getString(3);
                }

                notesIds.add(id);
                notesTitles.add(title);
                notesDates.add(date);
                notesTagsNames.add(tagName);
            }
            notesData = new ArrayList<>();
            notesData.add(notesIds);
            notesData.add(notesTitles);
            notesData.add(notesDates);
            notesData.add(notesTagsNames);
        }catch (SQLException e){
            //
        }finally {
            if (c != null) {
                c.close();
            }
            if (db != null){
                db.close();
            }
        }
        return notesData;
    }

    /**
     * Get a note filename where the content is saved
     * @param noteId
     * @return the filename of the note
     */
    public String getNoteFileName(int noteId){
        SQLiteDatabase db = null;
        Cursor c = null;
        String fileName = null;
        try {
            db = this.getReadableDatabase();

            c =
                    db.rawQuery("SELECT fileContent FROM Notes WHERE id=" + String.valueOf(noteId),
                            null);
            if (c.moveToNext()) {
                // there is a note with this data
                fileName = c.getString(0);
            }
        }catch (SQLException e){
            //
        }finally {
            if (c != null) {
                c.close();
            }
            if (db != null){
                db.close();
            }
        }
        return fileName;
    }

    /**
     * Update note after editing it
     * @param noteId - the id of the note to update
     * @param title - the new title of the note
     * @param chosenTagId - the chosen tag id
     * @return true if the note has been updated
     */
    public boolean updateNote(int noteId, String title, int chosenTagId){
        SQLiteDatabase db = null;
        boolean changed;
        try {
            db = this.getWritableDatabase();
            ContentValues modification = new ContentValues();
            modification.put("title", title);
            modification.put("labelId", chosenTagId);
            db.update("Notes", modification, "id=" + noteId, null);
            changed = true;
        }catch (SQLException e){
            changed = false;
        }finally {
            if (db != null){
                db.close();
            }
        }
        return changed;
    }

    /**
     * Get a note data so the user can see it on the editor
     * @param noteId
     * @return
     */
    public String[] getNoteData(int noteId){
        String[] data = null;
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = this.getReadableDatabase();
            c =
                    db.rawQuery("SELECT title, fileContent, name, Tags.id, date FROM Notes LEFT JOIN " +
                                    "Tags " +
                                    "ON " +
                                    "Notes.labelId=Tags.id WHERE Notes.id=" + String.valueOf(noteId),
                            null);

            if (c.moveToNext()) {
                data = new String[5];
                // there is a note with this data
                data[0] = c.getString(0); // note title
                data[1] = c.getString(1); // note fileContent
                data[2] = null;
                data[3] = null;
                if (c.getColumnCount() == 5){
                    // there's a tag
                    data[2] = c.getString(2); // tag name
                    data[3] = c.getString(3); // tag id
                }
                data[4] = c.getString(4); // date
            }
        }catch (SQLException e){
            data = null;
        }finally{
            if (c != null) {
                c.close();
            }
            if (db != null){
                db.close();
            }
        }
        return data;
    }

    /**
     * Obtener las imágenes de una nota sabiendo su id
     * @param noteId - el id de la nota
     * @return - lista con el path de las imágenes
     */
    public ArrayList<String> getNoteImages(int noteId){
        ArrayList<String> imagesPath = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = this.getReadableDatabase();
            c =
                    db.rawQuery("SELECT imagePath FROM NotesImages WHERE noteId=" + String.valueOf(noteId),
                            null);

            while (c.moveToNext()) {
                imagesPath.add(c.getString(0));
            }
        }catch (SQLException e){
            //
        }finally{
            if (c != null) {
                c.close();
            }
            if (db != null){
                db.close();
            }
        }
        return imagesPath;
    }

    /**
     * Delete a note by knowing the id
     * @param noteId - the id of the note to delete
     * @return true, the note has been successfully deleted
     */
    public boolean deleteANote(int noteId){
        SQLiteDatabase db = null;
        boolean result;
        try {
            db = getWritableDatabase();
            db.delete("Notes", "id=" + noteId, null);
            result = true;
        }catch (SQLException e){
            result = false;
        }finally {
            if (db != null){
                db.close();
            }
        }
        return result;
    }

    /**
     * Get tags by user
     * @param username - of which we have to get the notes
     * @return ids, names of the tags
     */
    public ArrayList<ArrayList<String>> getTagsByUser(String username){
        SQLiteDatabase db = null;
        Cursor c = null;
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        try {
            db = this.getReadableDatabase();
            c = db.rawQuery("SELECT id, name FROM Tags WHERE username='" + username +
                    "'", null);

            ArrayList<String> tagsIds = new ArrayList<>();
            ArrayList<String> tagsNames = new ArrayList<>();

            while (c.moveToNext()) {
                // there is a user with these data
                String id = c.getString(0);
                String tagName = c.getString(1);

                tagsIds.add(id);
                tagsNames.add(tagName);
            }

            data.add(tagsIds);
            data.add(tagsNames);
        }catch(SQLException e){
            data = null;
        }finally {
            if (c != null) {
                c.close();
            }
            if (db != null){
                db.close();
            }
        }
        return data;
    }

    /**
     * Add new tag for user
     * @param username - the user that the new tag belongs to
     * @param nameTag - the name of the new tag
     * @return true if the tag has been added
     */
    public boolean addTag(String username, String nameTag){
        SQLiteDatabase db = null;
        boolean changed;
        try {
            db = this.getWritableDatabase();

            db.execSQL("INSERT INTO Tags ('name', 'username') VALUES ('" + nameTag + "', '" + username +
                    "')");
            changed = true;
        }catch (SQLException e){
            changed = false;
        }finally {
            if (db != null){
                db.close();
            }
        }
        return changed;
    }

    /**
     * Check if the tag exists
     * @param username - the user
     * @param nameTag - the name of the tag
     * @return - True if the user has this tag, false if he doesn't
     */
    public boolean tagExists(String username, String nameTag){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =
                db.rawQuery("SELECT id FROM Tags WHERE name='"+ nameTag+"' AND username='" + username + "'", null);
        boolean exists = false;
        if (c.moveToNext()){
            // the tag exists
            exists = true;
        }
        db.close();
        return exists;
    }

    /**
     * Remove tag by id
     * @param tagId - the id of the tag to delete
     * @return - list of post id with that tag
     */
    public ArrayList<Integer> removeTag(int tagId){
        SQLiteDatabase db = null;
        Cursor c = null;
        ArrayList<Integer> tagsIds = new ArrayList<>();

        try {
            db = this.getWritableDatabase();
            c = db.rawQuery("SELECT id FROM Notes WHERE labelId=" + tagId, null);

            while (c.moveToNext()) {
                // there is a note with this tag id
                int id = c.getInt(0);
                tagsIds.add(id);
            }
            db.execSQL("DELETE FROM Tags WHERE id=" + tagId);
        }catch (SQLException e){
            tagsIds = null;
        }finally {
            if (c != null) {
                c.close();
            }
            if (db != null){
                db.close();
            }
        }
        return tagsIds;
    }

    /**
     * Insert new note
     * @param title - title of the new note
     * @param fileContent - the filename where the new note content is
     * @param labelId - the label id
     * @param username - the user who has created the note
     * @return true if it has been inserted
     */
    public boolean insertNewNote(String title, String fileContent, int labelId, String username){
        SQLiteDatabase db = null;
        boolean added;
        try {
            db = this.getWritableDatabase();
            String label = String.valueOf(labelId);
            if (labelId == -1) {
                label = null;
            }
            db.execSQL("INSERT INTO Notes ('title', 'fileContent', 'labelId', 'username') VALUES ('" + title +
                    "', '" + fileContent + "', " + label + ", '" + username + "')");
            added = true;
        }catch (SQLException e){
            added = false;
        }finally {
            if (db != null){
                db.close();
            }
        }
        return added;
    }

    /**
     * Get last inserted note data knowing the filename (it's unique)
     * @return - list with id, title, date, tag
     */
    public ArrayList<String> getLastAddedNoteData(String fileName) {
        SQLiteDatabase db = null;
        Cursor c = null;
        ArrayList<String> data;
        try{
            db = this.getReadableDatabase();
            c =
                    db.rawQuery("SELECT Notes.id, title, date, name FROM Notes LEFT JOIN Tags ON " +
                            "Notes.labelId=Tags.id WHERE Notes.fileContent='" + fileName + "'", null);


            data = new ArrayList<>();

            if (c.moveToFirst()){
                do {
                    String id = c.getString(0);
                    String title = c.getString(1);
                    String date = c.getString(2);
                    String tagName = null;
                    if (c.getColumnCount() == 4){
                        tagName = c.getString(3);
                    }

                    data.add(id);
                    data.add(title);
                    data.add(date);
                    data.add(tagName);
                }while(c.moveToNext());
            }
        }catch (SQLException e){
            data = null;
        }finally {
            if (c != null) {
                c.close();
            }
            if (db != null){
                db.close();
            }
        }
        return data;
    }
}
