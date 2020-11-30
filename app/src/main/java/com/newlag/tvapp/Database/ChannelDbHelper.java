package com.newlag.tvapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.newlag.tvapp.Database.ChannelDbSchema.ChannelTable;

public class ChannelDbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "channelBase.db";

    public ChannelDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE " + ChannelTable.TABLE_NAME + " (" +
            ChannelTable._ID + " INTEGER PRIMARY KEY, " +
            ChannelTable.COLUMN_CHANNEL + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
