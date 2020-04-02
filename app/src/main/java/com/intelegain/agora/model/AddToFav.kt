package com.intelegain.agora.model

import java.util.*

/**
 *
 * This Class Use for 3 Asynctask. addToFav, getFavEmployee, getTimeSheetDates.
 *
 */
class AddToFav {
    @kotlin.jvm.JvmField
    var Message = ""
    @kotlin.jvm.JvmField
    var Error = ""
    @kotlin.jvm.JvmField
    var StatusForLeaveType = ""
    var MissedDate = ""
    @kotlin.jvm.JvmField
    var Status = 0
    /**
     * ArrayList use for getFavEmployee in FavEmpActivity
     */
    @kotlin.jvm.JvmField
    var getfavemp_arraylist = ArrayList<GetFavEmpDeatils>()
    /**
     * ArrayList use for getTimeSheetDates in CalenderViewActivity
     */
    @kotlin.jvm.JvmField
    var gettimsheetdates_arraylist = ArrayList<CalenderDataDetails>()
    /**
     * ArrayList use for geting available leaves
     */
    @kotlin.jvm.JvmField
    var getAvailableLeaves_arraylist = ArrayList<GetAvailableLeavesDetails>()
    /**
     * ArrayList use for geting leaves type
     */
    @kotlin.jvm.JvmField
    var getLeaveType_arraylist = ArrayList<GetLeaveTypeDetails>()
    /**
     * ArrayList use for geting leaves status
     */
    @kotlin.jvm.JvmField
    var getleaveStatus_arraylist = ArrayList<GetAllLeaveStatusDetails>()
    /**
     * ArrayList use for geting leaves status details
     */
    var getleaveStatusInDetails_arraylist = ArrayList<GetLeaveStatusInDetails>()
    /**
     * ArrayList use for geting leaves for approval details
     */
    @kotlin.jvm.JvmField
    var getleaveForApproval_arraylist = ArrayList<GetLeavesForApprovalDetails>()
    /**
     * ArrayList use for geting approval
     */
    var getapproval_arraylist = ArrayList<GetApproveLeaveDetails>()
    /**
     * ArrayList use for geting module name
     */
    var getmodulename_arraylist = ArrayList<GetModuleNameDetails>()
    /**
     * ArrayList use for geting Project title
     */
    @kotlin.jvm.JvmField
    var getProjectTitle_arraylist = ArrayList<GetProjectTitleDetails>()
}