package com.newlag.tvapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.newlag.tvapp.Database.ChannelDbHelper;
import com.newlag.tvapp.Database.ChannelDbSchema;
import com.newlag.tvapp.Database.ChannelDbSchema.ChannelTable;
import com.newlag.tvapp.Models.Channel;

import java.util.ArrayList;

public class ChannelPresenter {

    private ChannelDbHelper dbHelper;

    public ChannelPresenter(Context context) { dbHelper = new ChannelDbHelper(context); }

    public void addChannel(String name) {
        ContentValues values = new ContentValues();
        values.put(ChannelTable.COLUMN_CHANNEL, name);
        dbHelper.getWritableDatabase().insert(
            ChannelTable.TABLE_NAME,
            null,
            values
        );
    }

    public void removeChannel(String name) {
        String selection = ChannelTable.COLUMN_CHANNEL + " LIKE ?";
        String[] selectionArgs = { name };
        dbHelper.getWritableDatabase().delete(
            ChannelTable.TABLE_NAME,
            selection,
            selectionArgs
        );
    }

    public boolean checkChannel(String name) {
        String[] projection = {
            ChannelTable._ID,
            ChannelTable.COLUMN_CHANNEL
        };
        String selection = ChannelTable.COLUMN_CHANNEL + " = ?";
        String[] selectionArgs = { name };
        Cursor cursor = dbHelper.getReadableDatabase().query(
            ChannelTable.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        );
        return cursor.moveToNext();
    }

    public void loadChannels(final onChannelsLoaded callback) {
        ChannelsFirebase db = new ChannelsFirebase();
        db.loadChannels(new ChannelsFirebase.onChannelsLoaded() {
            @Override
            public void onSucess(ArrayList<Channel> channels) {
                callback.onSuccess(channels);
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
    }

    public interface onChannelsLoaded {
        void onSuccess(ArrayList<Channel> channels);
        void onFailure();
    }
}
