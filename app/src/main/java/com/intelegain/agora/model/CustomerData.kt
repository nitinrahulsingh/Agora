package com.intelegain.agora.model

import java.util.*

/**
 * Created by kiran.p on 3/1/17.
 */
class CustomerData {
    @kotlin.jvm.JvmField
    var UserMasterID = ""
    @kotlin.jvm.JvmField
    var FName = ""
    @kotlin.jvm.JvmField
    var LName = ""
    @kotlin.jvm.JvmField
    var Email = ""
    @kotlin.jvm.JvmField
    var Error = ""
    @kotlin.jvm.JvmField
    var customer_login_data = ArrayList<CustomerData>()
    var Status = 0
}