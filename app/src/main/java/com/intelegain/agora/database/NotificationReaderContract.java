package com.intelegain.agora.database;

import android.provider.BaseColumns;

public final class NotificationReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private NotificationReaderContract() {}

    /* Inner class that defines the table contents */
    public static class NotificationEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
        public static final String COLUMN_NAME_STATUS = "status";
    }
}
