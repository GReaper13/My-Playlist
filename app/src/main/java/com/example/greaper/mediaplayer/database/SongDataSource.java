package com.example.greaper.mediaplayer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.greaper.mediaplayer.model.SongModel;

import java.util.ArrayList;

/**
 * Created by GReaper on 2/2/2018.
 */

public class SongDataSource {
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private static final String KEY_ID = SongSQLiteOpenHelper.KEY_ID;
    private static final String KEY_NAME = SongSQLiteOpenHelper.KEY_NAME;
    private static final String KEY_PATH = SongSQLiteOpenHelper.KEY_PATH;
    private static final String TABLE_NAME = SongSQLiteOpenHelper.TABLE_NAME;
    private static final String KEY_POSITION = SongSQLiteOpenHelper.KEY_POSITION;
    private String[] allCollumns = {KEY_ID, KEY_NAME, KEY_PATH, KEY_POSITION};

    private Context context;

    public SongDataSource(Context context) {
        this.context = context;
        sqLiteOpenHelper = new SongSQLiteOpenHelper(context);
    }

    public void open() throws SQLiteException {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close() throws SQLiteException {
        sqLiteOpenHelper.close();
    }

    public void addNewSong(SongModel songModel) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, songModel.getTitle());
        values.put(KEY_PATH, songModel.getPath());
        values.put(KEY_POSITION, songModel.getPosition());
        sqLiteDatabase.insert(TABLE_NAME, null, values);
    }

    public void deleteSong(SongModel songModel) {
        sqLiteDatabase.delete(TABLE_NAME, KEY_NAME + "=?", new String[] {songModel.getTitle()});
    }

    public ArrayList<SongModel> getCurrentSong() {
        ArrayList<SongModel> arrayList = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, allCollumns, null, null, null, null, KEY_POSITION + " ASC");

        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            SongModel song = cursorToModel(cursor);
            arrayList.add(song);
            cursor.moveToNext();
        }
        return arrayList;
    }

    public void updateSong(String title, int position) {
        ContentValues values = new ContentValues();
        values.put(KEY_POSITION, position);
////        int update_index = sqLiteDatabase.update(TABLE_NAME, values, KEY_NAME + "=?", new String[] {title});
//        if (update_index == 1) {
//
//        } else {
//
//        }
        sqLiteDatabase.update(TABLE_NAME, values, KEY_NAME + "=?", new String[] {title});
    }

    public void deleteAllSong() {
        sqLiteDatabase.delete(TABLE_NAME, null, null);
    }

    private SongModel cursorToModel (Cursor cursor) {
        SongModel song = new SongModel(cursor.getString(1), cursor.getString(2), cursor.getInt(3));
        return song;
    }
}
