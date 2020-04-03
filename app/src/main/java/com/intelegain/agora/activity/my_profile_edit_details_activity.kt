package com.intelegain.agora.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.intelegain.agora.R
import com.intelegain.agora.api.urls.RetrofitClient.retrofit
import com.intelegain.agora.constants.Constants
import com.intelegain.agora.dataFetch.RetrofitClient
import com.intelegain.agora.interfeces.WebApiInterface
import com.intelegain.agora.model.CommonStatusMessage
import com.intelegain.agora.utils.Contants2
import com.intelegain.agora.utils.Sharedprefrences.Companion.getInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class my_profile_edit_details_activity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {
    var img_close: ImageView? = null
    var tv_address_field_required: TextView? = null
    var tv_contact_number_field_required /*, tvAnniversaryDate*/: TextView? = null
    var et_edit_address: EditText? = null
    var et_edit_contact_number: EditText? = null
    var etAnniversaryDate: EditText? = null
    var btnSubmitEditProfile: Button? = null
    private var datePickerDialog: DatePickerDialog? = null
    var contants2: Contants2? = null
    var commonStatusMessage: CommonStatusMessage? = null
    var minDateInMillisecond: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_profile_edit_details_activity)
        findViews()
        setValuesFromIntent()
        setListeners()
        initialize()
    }

    /**
     * finds and binds views to respective object
     */
    fun findViews() {
        img_close = findViewById(R.id.img_icon_close)
        et_edit_address = findViewById(R.id.et_edit_address)
        et_edit_contact_number = findViewById(R.id.et_edit_contact_number)
        tv_address_field_required = findViewById(R.id.tv_address_field_required)
        tv_contact_number_field_required = findViewById(R.id.tv_contact_number_field_required)
        etAnniversaryDate = findViewById(R.id.etAnniversaryDate)
        btnSubmitEditProfile = findViewById(R.id.btn_edit_details_submit)
    }

    /**
     * Retrieves value from intent and sets it to edit texts accordingly.
     */
    private fun setValuesFromIntent() {
        val bundle = intent.extras
        et_edit_address!!.setText(bundle!!.getString("empAddress"))
        et_edit_contact_number!!.setText(bundle.getString("empContactNumber"))
        etAnniversaryDate!!.setText(bundle.getString("empAnniversaryDate"))
    }

    private fun initialize() {
        contants2 = Contants2()
        val format = SimpleDateFormat("dd.MM.yyyy, HH:mm")
        format.isLenient = false
        var minDate: Date? = null
        try {
            minDate = format.parse("01.01.1950, 00:01")
            minDateInMillisecond = minDate.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    /**
     * set listener for UI widgets
     */
    fun setListeners() {
        img_close!!.setOnClickListener(this)
        etAnniversaryDate!!.setOnClickListener(this)
        btnSubmitEditProfile!!.setOnClickListener(this)
        et_edit_contact_number!!.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                validateEditProfile()
            }
            false
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_icon_close -> finish()
            R.id.etAnniversaryDate ->  // show calendar with date not going beyond todays date
                showDatePickerDialog()
            R.id.btn_edit_details_submit -> validateEditProfile()
        }
    }

    /**
     * validates the edit text and if Ok then callEditProfileApi method is called respectively.
     */
    fun validateEditProfile() {
        Contants2.hideKeyBoard(this)
        tv_address_field_required!!.visibility = View.INVISIBLE
        tv_contact_number_field_required!!.visibility = View.INVISIBLE
        if (et_edit_address!!.text.toString().trim { it <= ' ' } == "" || et_edit_contact_number!!.text.toString().trim { it <= ' ' } == "" || et_edit_contact_number!!.text.toString().length < 10) {
            if (et_edit_address!!.text.toString().trim { it <= ' ' } == "") {
                tv_address_field_required!!.visibility = View.VISIBLE
                et_edit_address!!.requestFocus()
            }
            if (et_edit_contact_number!!.text.toString().trim { it <= ' ' } == "") {
                et_edit_contact_number!!.requestFocus()
                tv_contact_number_field_required!!.visibility = View.VISIBLE
            }
            if (et_edit_contact_number!!.text.toString().length < 10 && et_edit_contact_number!!.text.toString().length > 0) {
                et_edit_contact_number!!.requestFocus()
                tv_contact_number_field_required!!.visibility = View.VISIBLE
                tv_contact_number_field_required!!.setText(R.string.mobile_no)
            }
        } else callEditProfileApi()
    }

    /**
     * Retrofit Web Call to edit profile
     */
    fun callEditProfileApi() {
        if (Contants2.checkInternetConnection(this)) {
            contants2!!.showProgressDialog(this)
//            val apiInterface: WebApiInterface = retrofit!!.create(WebApiInterface::class.java)
            val apiInterface: WebApiInterface = RetrofitClient.getInstance(Constants.BASE_URL).create(WebApiInterface::class.java)
            commonStatusMessage = CommonStatusMessage()
            val mSharedPrefs = getInstance(this)
            val strToken = mSharedPrefs.getString("Token", "")
            val strEmpId = mSharedPrefs.getString(getString(R.string.key_emp_id), "")
            val params: MutableMap<String, String?> = HashMap()
            params["EmpId"] = strEmpId
            params["CurrentAddress"] = et_edit_address!!.text.toString().trim { it <= ' ' }
            params["ContactNumber"] = et_edit_contact_number!!.text.toString().trim { it <= ' ' }
            params["AnniversaryDate"] = etAnniversaryDate!!.text.toString()
            val call = apiInterface.editProfile(strEmpId, strToken, params)
            Log.d("WEB_URL", call.request().toString() + "")
            call.enqueue(object : Callback<CommonStatusMessage?> {
                override fun onResponse(call: Call<CommonStatusMessage?>, response: Response<CommonStatusMessage?>) {
                    when (response.code()) {
                        200 -> {
                            commonStatusMessage = response.body()
                            contants2!!.dismissProgressDialog()
                            if (commonStatusMessage!!.status == "1") {
                                finish()
                                Contants2.showToastMessage(this@my_profile_edit_details_activity,
                                        commonStatusMessage!!.message, true)
                            } else Contants2.showToastMessage(this@my_profile_edit_details_activity,
                                    commonStatusMessage!!.message, true)
                        }
                        403 -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showOkAlertDialog(this@my_profile_edit_details_activity, response.message(), "") { dialog, which -> Contants2.forceLogout(this@my_profile_edit_details_activity) }
                        }
                        500 -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showToastMessage(this@my_profile_edit_details_activity, getString(R.string.some_error_occurred), true)
                        }
                        else -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showToastMessage(this@my_profile_edit_details_activity, getString(R.string.some_error_occurred), true)
                        }
                    }
                }

                override fun onFailure(call: Call<CommonStatusMessage?>, t: Throwable) {
                    call.cancel()
                    contants2!!.dismissProgressDialog()
                }
            })
        } else {
            Contants2.showToastMessage(this, getString(R.string.no_internet), true)
        }
    }

    /**
     * Show date picker dialog
     */
    private fun showDatePickerDialog() {
        if (datePickerDialog == null || !datePickerDialog!!.isShowing) {
            val objCalendar = Calendar.getInstance()
            val startYear = objCalendar[Calendar.YEAR]
            val starthMonth = objCalendar[Calendar.MONTH]
            val startDay = objCalendar[Calendar.DAY_OF_MONTH]
            datePickerDialog = DatePickerDialog(this@my_profile_edit_details_activity, this, startYear, starthMonth, startDay)
            datePickerDialog!!.datePicker.maxDate = Date().time
            //if (minDateInMillisecond == null)
            datePickerDialog!!.datePicker.minDate = minDateInMillisecond
            datePickerDialog!!.show()
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val formattedDate = ((if (dayOfMonth <= 9) "0$dayOfMonth" else dayOfMonth)
                .toString() + "/" + (if (month + 1 <= 9) "0" + (month + 1) else month + 1)
                + "/" + year)
        etAnniversaryDate!!.setText(formattedDate)
    }
}