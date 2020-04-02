package com.intelegain.agora.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intelegain.agora.R
import com.intelegain.agora.adapter.HolidayAdapter
import com.intelegain.agora.api.urls.CommonMethods
import com.intelegain.agora.constants.Constants
import com.intelegain.agora.dataFetch.RetrofitClient
import com.intelegain.agora.interfeces.WebApiInterface
import com.intelegain.agora.model.HolidayData
import com.intelegain.agora.model.HolidayMaster
import com.intelegain.agora.utils.Sharedprefrences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*

class NewHolidayActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private var toolbar: Toolbar? = null
    private var toolbar_title: TextView? = null
    private var mRecyView_for_holidays: RecyclerView? = null
    private var mobjHolidayAdapter: HolidayAdapter? = null
    private var mlstHolidayList = ArrayList<HolidayData>()
    private var retrofit: Retrofit? = null
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_holiday)
        InitializeWidget()
        setToolbar()
        setEventClickListener()
        //setHolidayAdapter();
        retrofit = RetrofitClient.getInstance(Constants.BASE_URL)
        Handler().postDelayed({
            if (CommonMethods.checkInternetConnection(this@NewHolidayActivity)) {
                holidayList
            } else {
                dismissProgressDialog()
                Toast.makeText(this@NewHolidayActivity, "Please check your internet connection!", Toast.LENGTH_SHORT).show()
            }
        }, 500)
    }

    /** Get the holiday list from server  */
    private val holidayList: Unit
        private get() {
            showProgressDialog()
            val apiInterface = retrofit!!.create(WebApiInterface::class.java)
            val mSharedPrefs = Sharedprefrences.getInstance(this)
            val strToken = mSharedPrefs.getString("Token", "WK6sDrLvypjT7c8GLKcXs8hZQGyw26TMCqSoCIU/ETJ7AA==")
            val strEmpId = mSharedPrefs.getString("emp_Id", "1000")
            val call = apiInterface.getHolidayList(strEmpId, strToken)
            call.enqueue(object : Callback<HolidayMaster?> {
                override fun onResponse(call: Call<HolidayMaster?>, response: Response<HolidayMaster?>) {
                    Log.d("TAG", response.code().toString() + "")
                    val iStatusCode = response.code()
                    if (iStatusCode == 200) {
                        val objHolidayMaster = response.body()
                        val lstHoldiayList = objHolidayMaster!!.holidayList
                        mlstHolidayList = lstHoldiayList!!
                        setHolidayAdapter()
                        Log.i(TAG, "")
                    } else {
                        Toast.makeText(this@NewHolidayActivity, "Some error occurred! please try again later..", Toast.LENGTH_SHORT).show()
                    }
                    dismissProgressDialog()
                }

                override fun onFailure(call: Call<HolidayMaster?>, t: Throwable) {
                    call.cancel()
                    dismissProgressDialog()
                    Toast.makeText(this@NewHolidayActivity, "Some error occurred! please try again later..", Toast.LENGTH_SHORT).show()
                }
            })
        }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun InitializeWidget() {
        toolbar = findViewById<View>(R.id.custom_toolbar) as Toolbar
        toolbar_title = findViewById<View>(R.id.toolbar_title) as TextView
        mRecyView_for_holidays = findViewById<View>(R.id.recyclerview_for_holidays) as RecyclerView
    }

    /**
     * Set the event click listener for views
     */
    private fun setEventClickListener() {
        toolbar!!.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * Set toolbar properties
     */
    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Holidays"
        toolbar_title!!.text = "Holidays"
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    /** Set holiday adapter  */
    private fun setHolidayAdapter() { //mlstHolidayList = createDummyDataForLeaves();
        mobjHolidayAdapter = HolidayAdapter(this, mlstHolidayList)
        mRecyView_for_holidays!!.adapter = mobjHolidayAdapter
        mRecyView_for_holidays!!.layoutManager = LinearLayoutManager(this)
    }

    /** NOT IN USED NOW, was only for testing purpose  */
    private fun createDummyDataForLeaves(): ArrayList<HolidayData> {
        val mlstLeaveData = ArrayList<HolidayData>()
        var objHolidayData: HolidayData
        for (iCount in 0..10) {
            objHolidayData = if (iCount == 0) {
                HolidayData("26", "JAN", "Republic Day", "http:dummy.com", iCount, "#f8efea")
            } else if (iCount == 1) {
                HolidayData("13", "MAR", "Holi", "http:dummy.com", iCount, "#deefff")
            } else if (iCount == 2) {
                HolidayData("14", "APR", "Good Friday", "http:dummy.com", iCount, "#e9f7f7")
            } else if (iCount == 3) {
                HolidayData("1", "MAY", "Maharashtra Day", "http:dummy.com", iCount, "#f4e9f7")
            } else if (iCount == 4) {
                HolidayData("25", "AUG", "Eid (Subject to change)", "http:dummy.com", iCount, "#edf4ec")
            } else if (iCount == 5) {
                HolidayData("15", "AUG", "Independence Day", "http:dummy.com", iCount, "#f0f0f0")
            } else if (iCount == 6) {
                HolidayData("14", "AUG", "Ganesh Chaturthi", "http:dummy.com", iCount, "#e9f7f7")
            } else if (iCount == 7) {
                HolidayData("1", "May", "Dessehra", "http:dummy.com", iCount, "#f4e9f7")
            } else if (iCount == 8) {
                HolidayData("2", "OCT", "Mahatma Gandhi Jayanti", "http:dummy.com", iCount, "#efefef")
            } else if (iCount == 9) {
                HolidayData("13", "NOV", "Diwali", "http:dummy.com", iCount, "#deefff")
            } else if (iCount == 10) {
                HolidayData("25", "DEC", "Christmas", "http:dummy.com", iCount, "#fde9eb")
            } else {
                HolidayData("31", "DEC", "New Holiday", "http:dummy.com", iCount, "#f8f8f8")
            }
            mlstLeaveData.add(objHolidayData)
        }
        return mlstLeaveData
    }

    /** Show progresss dialog  */
    private fun showProgressDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Please wait ...")
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.show()
    }

    /** Dismiss the progress dialog  */
    private fun dismissProgressDialog() {
        if (progressDialog != null) if (progressDialog!!.isShowing) progressDialog!!.dismiss()
    }
}