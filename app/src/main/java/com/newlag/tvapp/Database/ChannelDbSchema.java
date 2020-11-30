package com.newlag.tvapp.Database;

import android.provider.BaseColumns;

public class ChannelDbSchema {

    public static final class ChannelTable implements BaseColumns {
        public static final String TABLE_NAME = "channels";
        public static final String COLUMN_CHANNEL = "name";
    }

}
