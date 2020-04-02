package com.intelegain.agora.model

class GetFavEmpDeatils {
    var Message = ""
    var Error = ""
    var Status = 0
    // emp_name = "", phone_number = "", email = "", flag = "", Status = "";
    @kotlin.jvm.JvmField
    var FavouriteEmpContact = ""
    @kotlin.jvm.JvmField
    var FavouriteEmpEmail = ""
    @kotlin.jvm.JvmField
    var FavouriteEmpName = ""
    @kotlin.jvm.JvmField
    var FavouriteFlag = ""
    var emp_name = ""
    var phone_number = ""
    var email = ""
    var flag = ""
    var emp_id: Int? = null
    @kotlin.jvm.JvmField
    var FavouriteEmpId: Int? = null /*
	public String Message = "", Error = "";
	public Integer emp_id;
	public String emp_name = "", phone_number = "", email = "", flag = "",Status = "";
	public String description = "";*/
}