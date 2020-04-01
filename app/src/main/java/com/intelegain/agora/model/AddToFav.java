package com.intelegain.agora.model;

import java.util.ArrayList;

/**
 * 
 * This Class Use for 3 Asynctask. addToFav, getFavEmployee, getTimeSheetDates.
 * 
 **/

public class AddToFav {

	public String Message = "", Error = "", StatusForLeaveType = "",MissedDate="";
	public int Status = 0;

	
	/**
	 * ArrayList use for getFavEmployee in FavEmpActivity
	 **/

	public ArrayList<GetFavEmpDeatils> getfavemp_arraylist = new ArrayList<GetFavEmpDeatils>();
	
	/**
	 * ArrayList use for getTimeSheetDates in CalenderViewActivity
	 **/

	public ArrayList<CalenderDataDetails> gettimsheetdates_arraylist = new ArrayList<CalenderDataDetails>();
	
	
	/**
	 * ArrayList use for geting available leaves
	 */
	
	public ArrayList<GetAvailableLeavesDetails> getAvailableLeaves_arraylist = new ArrayList<GetAvailableLeavesDetails>();
	

	/**
	 * ArrayList use for geting leaves type
	 */
	
	public ArrayList<GetLeaveTypeDetails> getLeaveType_arraylist = new ArrayList<GetLeaveTypeDetails>();
	
	
	/**
	 * ArrayList use for geting leaves status
	 */
	
	public ArrayList<GetAllLeaveStatusDetails> getleaveStatus_arraylist = new ArrayList<GetAllLeaveStatusDetails>();
	
	
	/**
	 * ArrayList use for geting leaves status details
	 */
	
	public ArrayList<GetLeaveStatusInDetails> getleaveStatusInDetails_arraylist = new ArrayList<GetLeaveStatusInDetails>();
	
	
	/**
	 * ArrayList use for geting leaves for approval details
	 */
	
	public ArrayList<GetLeavesForApprovalDetails> getleaveForApproval_arraylist = new ArrayList<GetLeavesForApprovalDetails>();
	
	/**
	 * ArrayList use for geting approval
	 */
	
	public ArrayList<GetApproveLeaveDetails> getapproval_arraylist = new ArrayList<GetApproveLeaveDetails>();
	
	/**
	 * ArrayList use for geting module name
	 */
	
	public ArrayList<GetModuleNameDetails> getmodulename_arraylist = new ArrayList<GetModuleNameDetails>();
	
	/**
	 * ArrayList use for geting Project title
	 */
	
	public ArrayList<GetProjectTitleDetails> getProjectTitle_arraylist = new ArrayList<GetProjectTitleDetails>();
}
