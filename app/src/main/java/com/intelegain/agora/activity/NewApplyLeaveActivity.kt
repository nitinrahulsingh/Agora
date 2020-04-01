package com.intelegain.agora.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.intelegain.agora.R
import com.intelegain.agora.constants.Constants
import com.intelegain.agora.dataFetch.RetrofitClient
import com.intelegain.agora.interfeces.WebApiInterface
import com.intelegain.agora.model.ApplyLeave
import com.intelegain.agora.utils.Contants2
import com.intelegain.agora.utils.Sharedprefrences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NewApplyLeaveActivity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener //, TextInputEditText.OnFocusChangeListener  {
{
    private val TAG = javaClass.simpleName
    private var spinnerLeaveType: Spinner? = null
    private var btnApplyLeave: Button? = null
    private var ivClose: ImageView? = null
    private var tvLeaveFromDate: TextView? = null
    private var tvLeaveToDate: TextView? = null
    //private TextInputEditText edLeaveFromDate,edLeaveToDate,edLeaveReason;
    private var edLeaveReason: EditText? = null
    private var datePickerDialog: DatePickerDialog? = null
    private val mlstLeaveType = ArrayList<String>()
    private val mlstLeaveShortForm = ArrayList<String>()
    private val bIsFirstTimeFocus = true
    private var bIsStartDateClicked = false
    private var retrofit: Retrofit? = null
    private var contants2: Contants2? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_apply_leave)
        initializeObjects()
        InitializeWidget()
        setEventClickListener()
        FillLeaveTypeSpinner(intent)
    }

    private fun initializeObjects() {
        retrofit = RetrofitClient.getInstance(Constants.BASE_URL)
        contants2 = Contants2()
        mlstLeaveShortForm.add("")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.stay, R.anim.slide_in_down)
    }

    private fun FillLeaveTypeSpinner(intent: Intent) { //, "CL", "SL", "PL", "CO", "WL"};
        mlstLeaveType.add("Select")
        if (intent.hasExtra("balanceCl") && intent.getStringExtra("balanceCl").toInt() > 0) {
            mlstLeaveType.add("Casual Leave - CL")
            mlstLeaveShortForm.add("CL")
        }
        if (intent.hasExtra("balanceSl") && intent.getStringExtra("balanceSl").toInt() > 0) {
            mlstLeaveType.add("Sick Leave - SL")
            mlstLeaveShortForm.add("SL")
        }
        if (intent.hasExtra("balancePl") && intent.getStringExtra("balancePl").toInt() > 0) {
            mlstLeaveType.add("Paid Leave - PL")
            mlstLeaveShortForm.add("PL")
        }
        if (intent.hasExtra("balanceCo") && intent.getStringExtra("balanceCo").toInt() > 0) {
            mlstLeaveType.add("Comp Off - CO")
            mlstLeaveShortForm.add("CO")
        }
        mlstLeaveType.add("Leave (Without Pay)")
        mlstLeaveShortForm.add("WL")
        val leaveAdapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, mlstLeaveType)
        spinnerLeaveType!!.adapter = leaveAdapter
    }

    /**
     * Initialize all the widget
     */
    private fun InitializeWidget() {
        ivClose = findViewById(R.id.img_icon_close)
        btnApplyLeave = findViewById(R.id.btnApplyLeave)
        spinnerLeaveType = findViewById(R.id.spinnerLeaveType)
        tvLeaveFromDate = findViewById(R.id.tvLeaveStartDate)
        tvLeaveToDate = findViewById(R.id.tvLeaveEndDate)
        edLeaveReason = findViewById(R.id.edLeaveReason)
    }

    /**
     * Set the event click listener for views
     */
    private fun setEventClickListener() {
        btnApplyLeave!!.setOnClickListener(this)
        ivClose!!.setOnClickListener(this)
        tvLeaveFromDate!!.setOnClickListener(this)
        tvLeaveToDate!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_icon_close -> {
                overridePendingTransition(R.anim.stay, R.anim.slide_in_down)
                onBackPressed()
            }
            R.id.btnApplyLeave -> if (IsValidInputByUser()) { //String strLeaveType = leaveType[spinnerLeaveType.getSelectedItemPosition()];
                val strLeaveType = mlstLeaveShortForm[spinnerLeaveType!!.selectedItemPosition]
                val strLeaveFrom = tvLeaveFromDate!!.text.toString()
                val strLeaveTo = tvLeaveToDate!!.text.toString()
                val strLeaveReason = edLeaveReason!!.text.toString()
                if (Contants2.checkInternetConnection(this)) // call web api to apply leave on server
                    applyLeave(strLeaveType, strLeaveFrom, strLeaveTo, strLeaveReason) else Contants2.showToastMessage(this, getString(R.string.no_internet), true)
            } else { // Contants2.showToastMessage(this, getString(R.string.valid_details), false); kalpesh
            }
            R.id.tvLeaveStartDate -> {
                bIsStartDateClicked = true
                showDatePickerDialog()
            }
            R.id.tvLeaveEndDate -> {
                bIsStartDateClicked = false
                showDatePickerDialog()
            }
            else -> {
            }
        }
    }

    /**
     * Is valid input by user
     */
    private fun IsValidInputByUser(): Boolean {
        return if (spinnerLeaveType!!.selectedItemPosition == 0 ||
                tvLeaveFromDate!!.text.toString().equals("From Date", ignoreCase = true) ||
                tvLeaveToDate!!.text.toString().equals("To Date", ignoreCase = true) ||
                TextUtils.isEmpty(edLeaveReason!!.text.toString())) {
            Contants2.showToastMessage(this, getString(R.string.valid_details), false) //kalpesh
            false
        } else {
            if (validateDate(tvLeaveFromDate!!.text.toString())!!.before(validateDate(tvLeaveToDate!!.text.toString()))
                    || validateDate(tvLeaveFromDate!!.text.toString()) == validateDate(tvLeaveToDate!!.text.toString())) {
                true
            } else {
                Toast.makeText(this, "From date should be less than or equal to To date", Toast.LENGTH_SHORT).show()
                false
            }
        } /*
        if (TextUtils.isEmpty(tvLeaveFromDate.getText().toString())) {
            return false;
        } else
            return !TextUtils.isEmpty(tvLeaveToDate.getText().toString());*/
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
            datePickerDialog = DatePickerDialog(
                    this, this@NewApplyLeaveActivity, startYear, starthMonth, startDay)
            datePickerDialog!!.show()
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val formattedDate = ((if (dayOfMonth <= 9) "0$dayOfMonth" else dayOfMonth)
                .toString() + "/" + (if (month + 1 <= 9) "0" + (month + 1) else month + 1)
                + "/" + year)
        if (bIsStartDateClicked) {
            tvLeaveFromDate!!.text = formattedDate
        } else {
            tvLeaveToDate!!.text = formattedDate
        }
    }

    private fun validateDate(frmDate: String): Date? {
        val curFormater = SimpleDateFormat("MM/dd/yyyy")
        var dateObj: Date? = null
        try {
            dateObj = curFormater.parse(frmDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dateObj
    }

    /**
     * Apply leave to server
     */
    private fun applyLeave(strLeaveType: String, strLeaveFrom: String, strLeaveTo: String, strReason: String) {
        contants2!!.showProgressDialog(this)
        val apiInterface = retrofit!!.create(WebApiInterface::class.java)
        val mSharedPrefs = Sharedprefrences.getInstance(this)
        val strToken = mSharedPrefs.getString("Token", "")
        val strEmpId = mSharedPrefs.getString("emp_Id", "")
        // make list of parameter for sending the http request
        val params: MutableMap<String, String> = HashMap()
        params["EmpId"] = strEmpId.toString()
        params["LeaveType"] = strLeaveType
        params["LeaveFrom"] = strLeaveFrom
        params["LeaveTo"] = strLeaveTo
        params["Reason"] = strReason
        val call = apiInterface.applyLeave(strEmpId, strToken, params)
        call.enqueue(object : Callback<ApplyLeave?> {
            override fun onResponse(call: Call<ApplyLeave?>, response: Response<ApplyLeave?>) {
                when (response.code()) {
                    200 -> {
                        val objApplyLeave = response.body()
                        //String strStatus = objApplyLeave.getStatus();
                        val strMessage = objApplyLeave!!.message
                        Contants2.showToastMessage(this@NewApplyLeaveActivity, strMessage, true)
                        contants2!!.dismissProgressDialog()
                        overridePendingTransition(R.anim.stay, R.anim.slide_in_down)
                        setResult(Activity.RESULT_OK)
                        onBackPressed()
                    }
                    403 -> {
                        //contants2.dismissProgressDialog();
                        Contants2.showOkAlertDialog(this@NewApplyLeaveActivity, response.message(), "") { dialog, which -> Contants2.forceLogout(this@NewApplyLeaveActivity) }
                        contants2!!.dismissProgressDialog()
                    }
                    500 -> {
                        // contants2.dismissProgressDialog();
                        Contants2.showToastMessage(this@NewApplyLeaveActivity, getString(R.string.some_error_occurred), true)
                        contants2!!.dismissProgressDialog()
                    }
                    else -> {
                        contants2!!.dismissProgressDialog()
                        Contants2.showToastMessage(this@NewApplyLeaveActivity, getString(R.string.some_error_occurred), true)
                        contants2!!.dismissProgressDialog()
                    }
                }
            }

            override fun onFailure(call: Call<ApplyLeave?>, t: Throwable) {
                call.cancel()
                contants2!!.dismissProgressDialog()
                Contants2.showToastMessage(this@NewApplyLeaveActivity, getString(R.string.some_error_occurred), false)
            }
        })
    }
}