package com.intelegain.agora.database

import android.provider.BaseColumns

class NotificationReaderContract  // To prevent someone from accidentally instantiating the contract class,
// make the constructor private.
private constructor() {
    /* Inner class that defines the table contents */
    object NotificationEntry : BaseColumns {
        const val _ID = "_id"
        const val TABLE_NAME = "entry"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_SUBTITLE = "subtitle"
        const val COLUMN_NAME_STATUS = "status"
    }
}