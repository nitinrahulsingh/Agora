/**
 *
 */
package com.intelegain.agora.model

/**
 * Jun 13, 2014
 * @author shreeraj
 */
class GetAllLeaveStatusDetails {
    var Message = ""
    var Error = ""
    var Status = 0
    var apply_date = ""
    var leave_status = ""
    var emp_id: Int? = null
    @kotlin.jvm.JvmField
    var leaveFromDate: String? = null
    @kotlin.jvm.JvmField
    var leaveToDate: String? = null
    @kotlin.jvm.JvmField
    var leaveType: String? = null
    @kotlin.jvm.JvmField
    var statusDesc: String? = null
    @kotlin.jvm.JvmField
    var adminComment: String? = null
    @kotlin.jvm.JvmField
    var leaveDesc: String? = null
    @kotlin.jvm.JvmField
    var LeaveId: String? = null
}