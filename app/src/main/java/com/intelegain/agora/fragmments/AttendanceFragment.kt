package com.intelegain.agora.fragmments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.intelegain.agora.R
import com.intelegain.agora.adapter.AttendanceAdapter
import com.intelegain.agora.api.urls.CommonMethods
import com.intelegain.agora.constants.Constants
import com.intelegain.agora.dataFetch.RetrofitClient
import com.intelegain.agora.interfeces.WebApiInterface
import com.intelegain.agora.model.AttendanceData
import com.intelegain.agora.model.AttendanceMaster
import com.intelegain.agora.utils.Contants2
import com.intelegain.agora.utils.Sharedprefrences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AttendanceFragment : Fragment(), View.OnClickListener, OnRefreshListener {
    private lateinit var mView: View
    private val TAG = javaClass.simpleName
    private var activity: Activity? = null
    private var ivPrevMonth: ImageView? = null;
    private  var ivNexMonth:ImageView? = null
    private var tvMonthTitle: TextView? = null;
    private  var tv_try_again:android.widget.TextView? = null
    private var container_for_recyclerview_header: ConstraintLayout? = null
    private var recyclerViewForAttendance: RecyclerView? = null
    private var attendanceAdapter: AttendanceAdapter? = null
    private var mlstAttendanceData: ArrayList<AttendanceData>? = null
    private var iCurrentMonth = 0
    private var iCurrentYear = 0
    private val progressDialog: ProgressDialog? = null
    private var retrofit: Retrofit? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    var startDate: String? = "";
    var endDate:String? = "";
    var currentServerDate:String? = ""
    var contants2: Contants2? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_attendance, container, false)
        activity = getActivity()
        initializeObjects()
        InitializeWidget(mView)
        setEventClickListener()
        iCurrentMonth = getCurrentMonth()
        iCurrentYear = getCurrentYear()
        return mView;
    }

    /**
     * initializing of objects is done here.
     */
    private fun initializeObjects() {
        startDate = ""
        endDate = ""
        contants2 = Contants2()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrofit = RetrofitClient.getInstance(Constants.BASE_URL)

        if (CommonMethods.checkInternetConnection(activity!!)) { // Get the Attendance data from server
            getAttendanceDataFromServer(startDate!!, endDate!!)
        } else {
            hideRecyclerView(true)
            Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true)
        }

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivPrevMonth -> {
                ivNexMonth!!.isEnabled = true
                if (CommonMethods.checkInternetConnection(activity!!)) { // Get the Attendance data from server
                    setPrevStartEndDate()
                    getAttendanceDataFromServer(startDate!!, endDate!!)
                } else {
                    dismissProgressDialog()
                    Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), false)
                }
                Log.i(TAG, "Start Date of Month: " + getStartDateOfTheMonth(iCurrentMonth, iCurrentYear))
                Log.i(TAG, "End Date of Month: " + getEndDateOfTheMonth(iCurrentMonth, iCurrentYear))
            }
            R.id.ivNexMonth -> if (hideNextMonth(endDate!!)) {
                if (CommonMethods.checkInternetConnection(activity!!)) { // Get the Attendance data from server
                    setnextStartEndDate()
                    getAttendanceDataFromServer(startDate!!, endDate!!)
                } else {
                    dismissProgressDialog()
                    Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), false)
                }
                Log.i(TAG, "Start Date of Month: " + getStartDateOfTheMonth(iCurrentMonth, iCurrentYear))
                Log.i(TAG, "End Date of Month: " + getEndDateOfTheMonth(iCurrentMonth, iCurrentYear))
            } else {
            }
            R.id.text_view_try_again -> if (CommonMethods.checkInternetConnection(activity!!)) { // Get the Attendance data from server
                getAttendanceDataFromServer(startDate!!, endDate!!)
            } else {
                Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true)
            }
            else -> {
            }
        }
    }

    override fun onRefresh() {
        if (CommonMethods.checkInternetConnection(activity!!)) { // Get the Attendance data from server
            getAttendanceDataFromServer(startDate!!, endDate!!)
        } else {
            hideRecyclerView(true)
            swipeRefreshLayout!!.isRefreshing = false
            Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true)
        }
    }

    /**
     * sets up recycler view adapter
     */
    private fun setUpRecyclerAdapter() {
        attendanceAdapter = AttendanceAdapter(activity!!, mlstAttendanceData!!)
        recyclerViewForAttendance!!.adapter = attendanceAdapter
        recyclerViewForAttendance!!.layoutManager = LinearLayoutManager(activity)
    }

    private fun getSampleArrayList(): ArrayList<AttendanceData>? {
        val items = ArrayList<AttendanceData>()
        for (i in 0..29) { //            if (i == 4)
//                items.add(new AttendanceData(AttendanceData.LEAVE));
//            else if (i == 5)
//                items.add(new AttendanceData(AttendanceData.HOLIDAY));
//            else
//                items.add(new AttendanceData(AttendanceData.NORMAL));
        }
        return items
    }

    /* Set the event click listener for views */
    private fun setEventClickListener() {
        ivPrevMonth!!.setOnClickListener(this)
        ivNexMonth!!.setOnClickListener(this)
        tv_try_again!!.setOnClickListener(this)
        swipeRefreshLayout!!.setOnRefreshListener(this)
    }

    /**
     * Initialize the widget
     *
     * @param view
     */
    private fun InitializeWidget(view: View) {
        ivPrevMonth = view.findViewById(R.id.ivPrevMonth)
        ivNexMonth = view.findViewById(R.id.ivNexMonth)
        tvMonthTitle = view.findViewById(R.id.tvMonthTitle)
        recyclerViewForAttendance = view.findViewById(R.id.recyclerViewForAttendance)
        container_for_recyclerview_header = view.findViewById(R.id.container_for_recyclerview_header)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        tv_try_again = view.findViewById(R.id.text_view_try_again)

        // Initially set invisible
        container_for_recyclerview_header!!.visibility = View.GONE
    }

    /**
     * sets startdate and enddate to be sent to server one month before of present month
     */
    fun setPrevStartEndDate() {
        var month: Int
        var year: Int
        val dateSplit: Array<String>
        val prevDate: String
        if (startDate != "") {
            dateSplit = startDate!!.split("/").toTypedArray()
            month = dateSplit[0].toInt()
            year = dateSplit[2].toInt()
            if (month == 1) {
                month = 12
                year = year - 1
                tvMonthTitle!!.text = contants2!!.getMonth3Letter(month - 1) + " " + year
            } else {
                month = month - 1
                tvMonthTitle!!.text = contants2!!.getMonth3Letter(month - 1) + " " + year
            }
            var formattedMonth = month.toString() + ""
            formattedMonth = if (formattedMonth.length > 1) formattedMonth else "0$formattedMonth"
            prevDate = dateSplit[1] + "/" + formattedMonth + "/" + year
            setStartEndDates(prevDate)
        }
    }

    /**
     * sets startdate and enddate to be sent to server one month ahead of present month
     */
    fun setnextStartEndDate() {
        var month: Int
        var year: Int
        val dateSplit: Array<String>
        val nextDate: String
        if (startDate != "") {
            dateSplit = startDate!!.split("/").toTypedArray()
            month = dateSplit[0].toInt()
            year = dateSplit[2].toInt()
            if (month == 12) {
                month = 1
                year = year + 1
                tvMonthTitle!!.text = contants2!!.getMonth3Letter(month - 1) + " " + year
            } else {
                month = month + 1
                tvMonthTitle!!.text = contants2!!.getMonth3Letter(month - 1) + " " + year
            }
            var formattedMonth = month.toString() + ""
            formattedMonth = if (formattedMonth.length > 1) formattedMonth else "0$formattedMonth"
            nextDate = dateSplit[1] + "/" + formattedMonth + "/" + year
            setStartEndDates(nextDate)
        }
    }

    /**
     * Get start date of the given month
     */
    private fun getStartDateOfTheMonth(iMonth: Int, iYear: Int): String? {
        val objCal: Calendar = getCalendar()!!
        objCal[Calendar.YEAR] = iYear
        objCal[Calendar.MONTH] = iMonth
        objCal[Calendar.DAY_OF_MONTH] = objCal.getActualMinimum(Calendar.DAY_OF_MONTH)
        val StartDate = objCal.time
        val start_date_format = SimpleDateFormat("MM/dd/yyyy")
        return start_date_format.format(StartDate)
    }

    /**
     * Get End date of the given month
     */
    private fun getEndDateOfTheMonth(iMonth: Int, iYear: Int): String? {
        val objCal: Calendar = getCalendar()!!
        objCal[Calendar.YEAR] = iYear
        objCal[Calendar.MONTH] = iMonth
        objCal[Calendar.DAY_OF_MONTH] = objCal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val EndDate = objCal.time
        val end_date_format = SimpleDateFormat("MM/dd/yyyy")
        return end_date_format.format(EndDate)
    }

    /**
     * Get current month
     */
    private fun getCurrentMonth(): Int {
        val objCal: Calendar = getCalendar()!!
        return objCal[Calendar.MONTH]
    }

    /**
     * Get current Year
     */
    private fun getCurrentYear(): Int {
        val objCal: Calendar = getCalendar()!!
        return objCal[Calendar.YEAR]
    }

    /**
     * sets the start date and end date from server recived date respectilvely
     */
    fun setStartEndDates(currentServerDate: String?) {
        val arrStartEndDate: Array<String>
        arrStartEndDate = contants2!!.setStartEndDates(currentServerDate).split("--").toTypedArray()
        startDate = arrStartEndDate[0]
        endDate = arrStartEndDate[1]
        /* SimpleDateFormat dfSource = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dfDest = new SimpleDateFormat("MM/dd/yyyy");

        try {
            Date sourceDate = dfSource.parse(currentServerDate);
            Date destDate = dfDest.parse(dfDest.format(sourceDate));

            startDate = dfDest.format(destDate);

            Calendar c = Calendar.getInstance();
            c.setTime(destDate);
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));

            Date lastDate = c.getTime();

            endDate = dfDest.format(lastDate);


        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Get the instance of the calendar
     */
    private fun getCalendar(): Calendar? {
        val objCal = Calendar.getInstance()
        objCal.time = Date()
        return objCal
    }

    private fun hideRecyclerView(hide: Boolean) {
        if (hide) {
            recyclerViewForAttendance!!.visibility = View.GONE
            tv_try_again!!.visibility = View.VISIBLE
        } else {
            tv_try_again!!.visibility = View.GONE
            recyclerViewForAttendance!!.visibility = View.VISIBLE
        }
    }


    private fun getTotalHours(strWorkingHour: String): String? {
        var strWorkingHour = strWorkingHour
        var strTotalHours = ""
        val strHour = strWorkingHour.substring(0, strWorkingHour.indexOf("hrs"))
        val intHour = strHour.replace(" ", "").toInt()
        var strMin = strWorkingHour.substring(strWorkingHour.indexOf("hrs") + 3)
        strMin = strMin.replace("mins", "")
        val intMin = strMin.replace(" ", "").toInt()
        val df = DecimalFormat("00")
        val strTotalHr = df.format(intHour.toLong())
        val strTotalMin = df.format(intMin.toLong())
        strWorkingHour = "$strTotalHr:$strTotalMin"
        strTotalHours = strWorkingHour.replace(" ", "")
        return strTotalHours
    }

    /**************************************** WEB API CALLS ENDS **********************************/

    /**************************************** WEB API CALLS ENDS  */
    /**
     * Get today date
     */
    private fun getTodayDate(): Date? {
        return Calendar.getInstance().time
    }

    /***
     * *  hide future month calender
     * @param endDate
     */
    private fun hideNextMonth(endDate: String): Boolean {
        var isNextBtnVisible = false
        val curFormater = SimpleDateFormat("MM/dd/yyyy")
        var dateObj: Date? = null
        try {
            dateObj = curFormater.parse(endDate)
            //                    Wed Jan 03 00:00:00 IST 2018
            if (dateObj.after(getTodayDate())) {
                ivNexMonth!!.isEnabled = false
                isNextBtnVisible = false
            } else {
                ivNexMonth!!.isEnabled = true
                isNextBtnVisible = true
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return isNextBtnVisible
    }

    /**
     * Dismiss the progress dialog
     */
    private fun dismissProgressDialog() {
        if (progressDialog != null) if (progressDialog.isShowing) progressDialog.dismiss()
    }


    /******************************************** WEB API CALLS ***********************************/

    /******************************************** WEB API CALLS  */
    /**
     * Get Attendance data from server
     */
    private fun getAttendanceDataFromServer(attendanceStartDate: String, attendanceEndDate: String) { // showProgressDialog();
        contants2!!.showProgressDialog(getActivity())
        swipeRefreshLayout!!.isRefreshing = false
        val apiInterface = retrofit!!.create(WebApiInterface::class.java)
        val mSharedPrefs = Sharedprefrences.getInstance(activity)
        val strToken = mSharedPrefs.getString("Token", "")
        val strEmpId = mSharedPrefs.getString("emp_Id", "")
        // Make list of parameter for sending the http request
        val params: MutableMap<String, String?> = HashMap()
        params["EmpId"] = strEmpId
        params["Startdate"] = attendanceStartDate
        params["EndDate"] = attendanceEndDate
        val call = apiInterface.getAttendanceData(strEmpId, strToken, params)
        call.enqueue(object : Callback<AttendanceMaster?> {
            @SuppressLint("DefaultLocale")
            override fun onResponse(call: Call<AttendanceMaster?>, response: Response<AttendanceMaster?>) {
                when (response.code()) {
                    200 -> {
                        val objAttendanceMaster = response.body()
                        val lstAttendanceData = objAttendanceMaster!!.attendanceData
                        mlstAttendanceData = ArrayList()
                        mlstAttendanceData = lstAttendanceData
                        if (mlstAttendanceData!!.size > 0) {
                            val iSizeAttendanceDataList = mlstAttendanceData!!.size
                            var i = 0
                            while (i < iSizeAttendanceDataList) {
                                val objAttendanceData = mlstAttendanceData!!.get(i)
                                val strTemp = "working hr : " + objAttendanceData.workinghours +
                                        " Timesheet hr : " + objAttendanceData.timesheethours +
                                        " Desc : " + objAttendanceData.description +
                                        " AttStatus : " + objAttendanceData.attStatus +
                                        " ViewType : " + objAttendanceData.viewType
                                val strAttStatus = objAttendanceData.attStatus
                                //  String strDescription = objAttendanceData.getDescription();
                                if (strAttStatus.equals("A", ignoreCase = true)) {
                                    objAttendanceData.description = "Absent"
                                }
                                val strWorkingHour = objAttendanceData.workinghours
                                if (!strWorkingHour!!.isEmpty()) {
                                    objAttendanceData.workinghours = getTotalHours(strWorkingHour!!)
                                }
                                var strTimesheetHour = objAttendanceData.timesheethours
                                if (!strTimesheetHour!!.isEmpty()) {
                                    strTimesheetHour = strTimesheetHour.substring(0, strTimesheetHour.indexOf("hrs") + 3)
                                    strTimesheetHour = strTimesheetHour.replace(" ", "")
                                    objAttendanceData.timesheethours = strTimesheetHour
                                }
                                //Log.i(TAG, "onResponse: " + strTemp);
// setting the date recieved from server
                                if (startDate == "" && endDate == "") {
                                    currentServerDate = objAttendanceData.attendanceDate
                                    setStartEndDates(currentServerDate)
                                    val presentDateSplit = currentServerDate!!.split("/").toTypedArray()
                                    tvMonthTitle!!.text = contants2!!.getMonth3Letter(presentDateSplit[1].toInt() - 1) + " " + presentDateSplit[2]
                                }
                                i++
                            }
                            // Set visible once data filled up in adapter
                            container_for_recyclerview_header!!.visibility = View.VISIBLE
                            setUpRecyclerAdapter()
                            // setMonthYearTitle();
                            hideRecyclerView(false)
                            contants2!!.dismissProgressDialog()
                        } else { //no data found
                        }
                        contants2!!.dismissProgressDialog()
                    }
                    403 -> {
                        contants2!!.dismissProgressDialog()
                        //hideshowConstraintLayout(false);
                        Contants2.showOkAlertDialog(getActivity(), response.message(), "") { dialog, which -> Contants2.forceLogout(getActivity()) }
                    }
                    500 -> {
                        hideRecyclerView(true)
                        contants2!!.dismissProgressDialog()
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                    }
                    else -> {
                        hideRecyclerView(true)
                        contants2!!.dismissProgressDialog()
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                    }
                }
                swipeRefreshLayout!!.isRefreshing = false
            }

            override
            fun onFailure(call: Call<AttendanceMaster?>, t: Throwable) {
                call.cancel()
                hideRecyclerView(true)
                swipeRefreshLayout!!.isRefreshing = false
                contants2!!.dismissProgressDialog()
                Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
            }
        })
    }


}
