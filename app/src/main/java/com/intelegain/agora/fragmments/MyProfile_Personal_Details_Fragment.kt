package com.intelegain.agora.fragmments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.intelegain.agora.R
import com.intelegain.agora.activity.my_profile_edit_details_activity
import com.intelegain.agora.constants.Constants
import com.intelegain.agora.dataFetch.RetrofitClient
import com.intelegain.agora.interfeces.WebApiInterface
import com.intelegain.agora.interfeces.empProfileImageUpdate
import com.intelegain.agora.model.EmployeeProfile
import com.intelegain.agora.utils.Contants2
import com.intelegain.agora.utils.Sharedprefrences.Companion.getInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*

class MyProfile_Personal_Details_Fragment : Fragment(), View.OnClickListener {
    var mView: View? = null
    var constraint_layout: ConstraintLayout? = null

    // LinearLayout linear_layout_error_message;
    var tv_Address: TextView? = null
    var tv_contact_number: TextView? = null
    var tv_email: TextView? = null
    var tv_probabation_period: TextView? = null
    var tv_joining_date: TextView? = null
    var tv_account_number: TextView? = null
    var tv_birth_date: TextView? = null
    var tv_anniversary_date: TextView? = null
    var tv_try_again /*tv_some_error_occurred*/: TextView? = null
    var contants2: Contants2? = null
    var employeeProfile: EmployeeProfile? = null
    var EmpProfileImageUpdate: empProfileImageUpdate? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_myprofile_personal_details, container, false)
        findViews()
        setClickListener()
        initialize()
        getEmployeeProfile()
        return mView
    }

    private var retrofit: Retrofit? = null

    /**
     * finds and binds view to the repective objects
     */
    private fun findViews() {
        retrofit = RetrofitClient.getInstance(Constants.BASE_URL)
        constraint_layout = mView!!.findViewById(R.id.constraint_layout)
        constraint_layout!!.setVisibility(View.INVISIBLE)
        //        linear_layout_error_message = (LinearLayout) mView.findViewById(R.id.linear_layout_error_message);
        tv_Address = mView!!.findViewById(R.id.tv_Address)
        tv_contact_number = mView!!.findViewById(R.id.tv_contact_number)
        tv_email = mView!!.findViewById(R.id.tv_email)
        tv_probabation_period = mView!!.findViewById(R.id.tv_probabation_period)
        tv_joining_date = mView!!.findViewById(R.id.tv_joining_date)
        tv_account_number = mView!!.findViewById(R.id.tv_account_number)
        tv_birth_date = mView!!.findViewById(R.id.tv_birth_date)
        tv_anniversary_date = mView!!.findViewById(R.id.tv_anniversary_date)
        tv_try_again = mView!!.findViewById(R.id.text_view_try_again)
    }

    /**
     * sets listener for views
     */
    private fun setClickListener() {
        tv_try_again!!.setOnClickListener(this)
    }

    /**
     * initializes objects
     */
    private fun initialize() {
        EmpProfileImageUpdate = activity as empProfileImageUpdate?
        contants2 = Contants2()
    }

    /**
     * @param employeeProfile [EmployeeProfile] object which is used to set values of text view in UI
     */
    private fun setValues(employeeProfile: EmployeeProfile?) {
        tv_Address!!.text = employeeProfile!!.empAddress
        tv_contact_number!!.text = employeeProfile.empContact
        tv_email!!.text = employeeProfile.empEmail
        tv_probabation_period!!.text = employeeProfile.empProbationPeriod.toString()
        tv_joining_date!!.text = employeeProfile.empJoiningDate
        tv_account_number!!.text = employeeProfile.empAccountNo
        tv_birth_date!!.text = employeeProfile.strBirthday
        tv_anniversary_date!!.text = employeeProfile.empADate


        // calling interface method here to update the parent fragment image and other text views
        EmpProfileImageUpdate!!.updateParentFragment(employeeProfile.imageUrl, employeeProfile.empName, employeeProfile.empid, employeeProfile.designation)
    }

    /**
     * Retrofit Web Call to get employee profile details. Calls the interface through fragment to activity to update the
     * image, name, emp id, designation received here.
     */
    private fun getEmployeeProfile() {
        if (Contants2.checkInternetConnection(activity)) {
            contants2!!.showProgressDialog(activity)
            val apiInterface = retrofit!!.create(WebApiInterface::class.java)
            val mSharedPrefs = getInstance(activity)
            val strToken = mSharedPrefs.getString("Token", "")
            val strEmpId = mSharedPrefs.getString("emp_Id", "")
            val params: MutableMap<String, String?> = HashMap()
            params["EmpId"] = strEmpId
            val call = apiInterface.getEmployeeProfile(strEmpId, strToken, params)
            Log.d("WEB_URL", call.request().toString() + "")
            call.enqueue(object : Callback<EmployeeProfile?> {
                override fun onResponse(call: Call<EmployeeProfile?>, response: Response<EmployeeProfile?>) {
                    constraint_layout!!.visibility = View.VISIBLE
                    when (response.code()) {
                        200 -> {
                            hideshowConstraintLayout(false)
                            employeeProfile = response.body()
                            Handler().postDelayed({
                                setValues(employeeProfile)
                                contants2!!.dismissProgressDialog()
                            }, 100)
                        }
                        403 -> {
                            contants2!!.dismissProgressDialog()
                            hideshowConstraintLayout(false)
                            Contants2.showOkAlertDialog(activity, response.message(), "") { dialog, which -> Contants2.forceLogout(activity) }
                        }
                        500 -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true)
                            hideshowConstraintLayout(true)
                        }
                        else -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true)
                            hideshowConstraintLayout(true)
                        }
                    }
                }

                override fun onFailure(call: Call<EmployeeProfile?>, t: Throwable) {
                    call.cancel()
                    contants2!!.dismissProgressDialog()
                    Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true)
                }
            })
        } else {
            Contants2.showToastMessage(activity, getString(R.string.no_internet), true)
            hideshowConstraintLayout(true)
        }
    }

    /**
     * @param hideConstraintLayout boolean value if true then need to hide constraint layout and false will make it visible
     */
    fun hideshowConstraintLayout(hideConstraintLayout: Boolean) {
        if (hideConstraintLayout) {
            constraint_layout!!.visibility = View.GONE
            tv_try_again!!.visibility = View.VISIBLE
        } else {
            tv_try_again!!.visibility = View.GONE
            constraint_layout!!.visibility = View.VISIBLE
        }
    }

    fun callEditProfileActivity() {
        val intentEditProfile = Intent(activity, my_profile_edit_details_activity::class.java)
        intentEditProfile.putExtra("empAddress", tv_Address!!.text.toString().trim { it <= ' ' })
        intentEditProfile.putExtra("empContactNumber", tv_contact_number!!.text.toString().trim { it <= ' ' })
        intentEditProfile.putExtra("empAnniversaryDate", tv_anniversary_date!!.text.toString().trim { it <= ' ' })
        startActivityForResult(intentEditProfile, OPEN_DIALOUGE_REQUEST_CODE)
        //startActivity(intentEditProfile);
        activity!!.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_DIALOUGE_REQUEST_CODE) {
            getEmployeeProfile()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.text_view_try_again -> getEmployeeProfile()
        }
    }

    companion object {
        const val OPEN_DIALOUGE_REQUEST_CODE = 100
    }
}