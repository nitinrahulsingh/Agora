package com.intelegain.agora.model

class NotificationItem {
    @kotlin.jvm.JvmField
    var title: String? = null
    var message: String? = null
    var status: String? = null

    constructor() {}
    constructor(title: String?, message: String?, status: String?) {
        this.title = title
        this.message = message
        this.status = status
    }

}