package com.example.greaper.mediaplayer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.greaper.mediaplayer.model.SongModel;

/**
 * Created by GReaper on 2/2/2018.
 */

public class SongSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "song.db";
    public static final String TABLE_NAME = "current_song";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PATH = "path";
    public static final String KEY_POSITION = "position";
    public static final int DATABASE_VERSION = 1;
    private static final String CREATE_DATABASE = "CREATE TABLE " + TABLE_NAME + "( "
                                                                + KEY_ID + " INTEGER PRIMARY KEY autoincrement,"
                                                                + KEY_NAME + " text not null,"
                                                                + KEY_PATH + " text not null,"
                                                                + KEY_POSITION + " INTEGER not null);";

    public SongSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
