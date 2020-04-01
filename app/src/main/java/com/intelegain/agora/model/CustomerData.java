package com.intelegain.agora.model;

import java.util.ArrayList;

/**
 * Created by kiran.p on 3/1/17.
 */

public class CustomerData {

    public String UserMasterID = "", FName = "", LName = "",Email ="",Error = "";

    public ArrayList<CustomerData> customer_login_data = new ArrayList<CustomerData>();
    public int Status = 0;
}
