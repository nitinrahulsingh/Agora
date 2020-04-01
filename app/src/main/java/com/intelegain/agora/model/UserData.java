package com.intelegain.agora.model;

import java.util.ArrayList;

public class UserData
{
	public String Message = "", Error = "", Token = "",JoiningDate ="",ConfirmationDate = "",AccountNo = "";
	public String Name = "",EmployeeID = "",BDate="",Address = "",Contact = "",EmailID = "", ProbationPeriod = "",Designation ="",
			IsSuperAdmin= "";

	public ArrayList<UserData> user_login_data = new ArrayList<UserData>();


	public int Status = 0;
}
