package com.intelegain.agora.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NotificationDbHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) { // This database is only a cache for online data, so its upgrade policy is
// to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FeedReader.db"
        private var SQL_CREATE_ENTRIES = "CREATE TABLE " + NotificationReaderContract.NotificationEntry.TABLE_NAME + " (" +
                NotificationReaderContract.NotificationEntry._ID + " INTEGER PRIMARY KEY," +
                NotificationReaderContract.NotificationEntry.COLUMN_NAME_TITLE + " TEXT," +
                NotificationReaderContract.NotificationEntry.COLUMN_NAME_SUBTITLE + " TEXT," +
                NotificationReaderContract.NotificationEntry.COLUMN_NAME_STATUS + " TEXT)"
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + NotificationReaderContract.NotificationEntry.TABLE_NAME
    }
}