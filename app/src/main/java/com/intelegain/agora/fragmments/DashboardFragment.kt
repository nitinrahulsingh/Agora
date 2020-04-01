package com.intelegain.agora.fragmments

import android.app.Activity
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.intelegain.agora.R
import com.intelegain.agora.activity.NewHolidayActivity
import com.intelegain.agora.adapter.*
import com.intelegain.agora.api.urls.CommonMethods
import com.intelegain.agora.constants.Constants
import com.intelegain.agora.dataFetch.RetrofitClient
import com.intelegain.agora.interfeces.WebApiInterface
import com.intelegain.agora.model.*
import com.intelegain.agora.utils.Contants2
import com.intelegain.agora.utils.FileDownloader
import com.intelegain.agora.utils.FileDownloader.onDownloadTaskFinish
import com.intelegain.agora.utils.Sharedprefrences
import com.rd.PageIndicatorView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment(), View.OnClickListener, onDownloadTaskFinish {
    private lateinit var mView: View
    private val TAG = javaClass.simpleName
    private var objOccAdapter: OccassionPagerAdapter? = null
    private var objNewsAdapter: NewsPagerAdapter? = null
    private var objCipSessionPagerAdapter: CipSessionPagerAdapter? = null
    private var calendar: Calendar? = null
    private var imgBack: ImageView? = null
    private  var imgFront:android.widget.ImageView? = null
    private  var imgResizeCal:android.widget.ImageView? = null
    private var app_bar: AppBarLayout? = null
    private var txtmonthLbl: TextView? = null
    private  var tvOccassions:android.widget.TextView? = null
    private  var tvnNews:android.widget.TextView? = null
    private  var tvCipSession:android.widget.TextView? = null
    private var section_holidays: RelativeLayout? = null
    private  var section_hr_manual:android.widget.RelativeLayout? = null
    private  var section_mediclaim_policy:android.widget.RelativeLayout? = null
    private var section_anti_sexual_policy: RelativeLayout? = null
    private var viewPagerEvents: ViewPager? = null
    private var pageIndicatorView: PageIndicatorView? = null
    private val bIsCalendarCollapsed = false
    private var activity: Activity? = null
    private var viewPager: ViewPager? = null
    private var viewPagerAdapter: SlidingViewPagerAdapter? = null
    private var miViewPagerCalendarheight = 0
    var arrayListDashboardData: ArrayList<AttendanceData>? = null

    private val MI_NO_OF_MONTHS_TO_GENERATE = 24
    var nestedScrollView: NestedScrollView? = null

    var startDate = ""
    var endDate:kotlin.String? = ""
    var currentServerDate:kotlin.String? = ""
    //value for this varaible will be set from server recieved date
    var year = 0
    var month = 0

    private var bIsScrollByCollapse = false
    /**
     * To store the occasion data list of occasion adapter
     */
    private var mOccassionsDataList = ArrayList<OccassionsData>()
    private var mobjDashboardData: ArrayList<AttendanceData>? = null
    private var mNewsDataList = ArrayList<News>()
    private var mCipSessionDataList = ArrayList<CIPSession>()
    private val progressDialog: ProgressDialog? = null
    private var retrofit: Retrofit? = null
    private val iCurrentMonth = 0
    private val iCurrentYear = 0
    private var bHasCalledBySwipe = true
    private var miLastVisibleItem = 0
    private var contants2: Contants2? = null
    var stringArrayList: ArrayList<String?>? = null
    var rr_calendar: RelativeLayout? = null

    enum class State {
        EXPANDED, COLLAPSED, IDLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_dashboard, container, false)

        activity = getActivity()
        InitializeWidget(mView)
        setEventClickListener()
        setCalendarInstance()
        initialize()
        // Call the web api to get the dashboard data from server
        //setMonthDataInRecyclerView();
        // Call the web api to get the dashboard data from server
        //setMonthDataInRecyclerView();
        setOccassionViewPagerData(getDummyDataForOccasion())
        setImageIndicator()

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (CommonMethods.checkInternetConnection(activity)) {
            getDashboardData()
        } else {
            imgFront!!.visibility = View.GONE
            imgBack!!.visibility = View.GONE
            Contants2.showToastMessage(activity, getString(R.string.no_internet), true)
        }
    }

    private fun initialize() {
        retrofit = RetrofitClient.getInstance(Constants.BASE_URL)
        contants2 = Contants2()
    }

    /**
     * Set the event click listener for views
     */
    private fun setEventClickListener() {
        imgFront!!.setOnClickListener(this)
        imgBack!!.setOnClickListener(this)
        imgResizeCal!!.setOnClickListener(this)
        tvnNews!!.setOnClickListener(this)
        tvOccassions!!.setOnClickListener(this)
        tvCipSession!!.setOnClickListener(this)
        section_holidays!!.setOnClickListener(this)
        section_hr_manual!!.setOnClickListener(this)
        section_mediclaim_policy!!.setOnClickListener(this)
        section_anti_sexual_policy!!.setOnClickListener(this)
        app_bar!!.addOnOffsetChangedListener(object :AppBarStateChangeListener(){
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: DashboardFragment.State?) {
                if (state == DashboardFragment.State.COLLAPSED) {
                    Log.i(TAG, "State Collapsed")
                    bIsScrollByCollapse = true
                } else if (state == DashboardFragment.State.EXPANDED) {
                    Log.i(TAG, "State Expanded")
                    bIsScrollByCollapse = false
                } else if (state == DashboardFragment.State.IDLE) {
                    Log.i(TAG, "State Idle")
                }
            }

        })
    }

    /**
     * Initialize the widget
     *
     * @param view
     */
    private fun InitializeWidget(view: View) { //monthRecyView = (RecyclerView) view.findViewById(R.id.month_recyView);
        app_bar = view.findViewById(R.id.app_bar)
        imgBack = view.findViewById(R.id.img_back)
        imgFront = view.findViewById(R.id.img_front)
        imgResizeCal = view.findViewById(R.id.img_Resize_cal)
        section_holidays = view.findViewById(R.id.section_holidays)
        section_hr_manual = view.findViewById(R.id.section_hr_manual)
        section_mediclaim_policy = view.findViewById(R.id.section_mediclaim_policy)
        section_anti_sexual_policy = view.findViewById(R.id.section_anti_sexual_policy)
        txtmonthLbl = view.findViewById(R.id.text_month_lbl)
        tvOccassions = view.findViewById(R.id.tvOccassions)
        tvnNews = view.findViewById(R.id.tvnNews)
        tvCipSession = view.findViewById(R.id.tvCipSession)
        viewPagerEvents = view.findViewById(R.id.viewPagerEvents)
        pageIndicatorView = view.findViewById(R.id.imgCirIndicator)
        viewPager = view.findViewById(R.id.viewPager)
        nestedScrollView = view.findViewById(R.id.nested_scroll_parent)
        rr_calendar = view.findViewById(R.id.rr_cal)
        // pageIndicatorView.setFillColor(ContextCompat.getColor(activity, R.color.orange));
        pageIndicatorView!!.setSelectedColor(ContextCompat.getColor(activity!!, R.color.orange))
        // By default set the occasion to selected
        tvOccassions!!.setBackgroundResource(R.drawable.rounded_button_selected)
        tvOccassions!!.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
    }

    /**
     * set the calendar instance in module level
     */
    private fun setCalendarInstance() {
        calendar = Calendar.getInstance()
    }

     /**
     * @param year year value which is one less than current year recieved from server to prepare custome calendar
     */
    private fun setUpCalendarViewPager(year: Int, arrayListDashboardData:ArrayList<AttendanceData>) {
         viewPagerAdapter = SlidingViewPagerAdapter(activity, stringArrayList, year)
         viewPager!!.adapter = (viewPagerAdapter);
         viewPager!!.currentItem = (getCurrentMonthPosition());

         // first time Last item of view pager is selected
         // so below visibility are sets to views.
         imgFront!!.visibility = View.GONE;
         imgBack!!.visibility = View.VISIBLE;

         var viewPagerPosition : Int = viewPager!!.currentItem
         miLastVisibleItem = viewPagerPosition;
         getItemFromPosition(viewPagerPosition);

        updateMonthLabel();

         viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
             override fun onPageScrollStateChanged(state: Int) {
                 TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
             }

             override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                 // Log.d(TAG, "onPageScrolled: "+position);
             }

             override fun onPageSelected(position: Int) {
                 SlidingViewPagerAdapter.bIsFirstTime = true;
                 if (bHasCalledBySwipe) {
                     if (miLastVisibleItem < position) {
                         Log.i(TAG, "onPageScrolled: Left Swipe");
                         if (CommonMethods.checkInternetConnection(activity)) {
                             //setNextMonthYear();

                             setNextMonthDates(startDate);
                             /*if (viewPager.getCurrentItem() < MI_NO_OF_MONTHS_TO_GENERATE) {
                                 viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                             }*/
                             if (viewPager!!.currentItem == (viewPager!!.adapter!!.count - 1)) {
                                 imgFront!!.setVisibility(View.GONE);
                             } else {
                                 imgBack!!.setVisibility(View.VISIBLE);
                             }
                             //Log.w("DATE_PARAM", "s:" + startDate + " e:" + endDate);
                             getAttendanceFromServer(startDate, endDate!!)
                         } else
                             Contants2.showToastMessage(activity, getString(R.string.no_internet), true)

                     } else {
                         Log.i(TAG, "onPageScrolled: Right Swipe");
                         if (CommonMethods.checkInternetConnection(activity)) {
                             //setPrevMonthYear();
                             setPreviousMonthDates(startDate);
                             /*if (viewPager.getCurrentItem() > 0) {
                                 viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                             }*/
                             if (viewPager!!.currentItem == 0) {
                                 imgBack!!.setVisibility(View.GONE);
                             } else {
                                 imgFront!!.setVisibility(View.VISIBLE);
                             }
                             //Log.w("DATE_PARAM", "s:" + startDate + " e:" + endDate);
                             getAttendanceFromServer(startDate, endDate!!);
                         } else
                             Contants2.showToastMessage(activity, getString(R.string.no_internet), true);

                     }

                     // Call the web api to get the dashboard data from server

                 }
                 miLastVisibleItem = position;
                 getItemFromPosition(position);
                 if (getCurrentMonthPosition() == position && bIsCalendarCollapsed)
                     scrollToCurrentDate(false);
             }

         })

        // Tree observer to measure the miViewPagerCalendarheight of viewpager once view is created
         viewPager!!.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener{
             override fun onPreDraw(): Boolean {
                 viewPager!!.viewTreeObserver.removeOnPreDrawListener(this);
                 miViewPagerCalendarheight = viewPager!!.getHeight();
                 // collapse the calendar on start of the app
                 Handler().apply {
                     val runnable = object : Runnable {
                         override fun run() {
                             collapse_calendar(false);
                             contants2!!.dismissProgressDialog();
//                             postDelayed(this, 2000)
                         }
                     }
                     postDelayed(runnable, 2000)
                 }
                 Log.i(TAG, "View pager height = " + miViewPagerCalendarheight);
                 return false;
             }

         })
    }

    /**
     * Set Occassion ViewPager data
     */
    private fun setOccassionViewPagerData(objOccassionsData: ArrayList<OccassionsData>?) {
        objOccAdapter = OccassionPagerAdapter(activity, objOccassionsData)
        viewPagerEvents!!.clipChildren = false
        viewPagerEvents!!.clipToPadding = false
        viewPagerEvents!!.setPadding(80, 0, 80, 0)
        viewPagerEvents!!.pageMargin = 40
        viewPagerEvents!!.adapter = objOccAdapter
    }

    /**
     * Set News ViewPager data
     */
    private fun setNewsViewPagerData(objNewsDataList: ArrayList<News>) {
        objNewsAdapter = NewsPagerAdapter(activity, objNewsDataList)
        viewPagerEvents!!.clipChildren = false
        viewPagerEvents!!.clipToPadding = false
        viewPagerEvents!!.setPadding(80, 0, 80, 0)
        viewPagerEvents!!.pageMargin = 40
        viewPagerEvents!!.adapter = objNewsAdapter
    }

    /**
     * Set Cip Session ViewPager data
     */
    private fun setCipSessionViewPagerData(objCipSessionDataList: ArrayList<CIPSession>) {
        objCipSessionPagerAdapter = CipSessionPagerAdapter(activity, objCipSessionDataList)
        viewPagerEvents!!.clipChildren = false
        viewPagerEvents!!.clipToPadding = false
        viewPagerEvents!!.setPadding(80, 0, 80, 0)
        viewPagerEvents!!.pageMargin = 40
        viewPagerEvents!!.adapter = objCipSessionPagerAdapter
    }

    /**
     * Show image indicator below the view pager
     */
    private fun setImageIndicator() { // Set View Pager Indicator properties
        val density = resources.displayMetrics.density
        pageIndicatorView!!.setRadius(5 * density)
        pageIndicatorView!!.setStrokeWidth(1 * density)
        pageIndicatorView!!.setViewPager(viewPagerEvents)
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_back -> {
                imgFront!!.isEnabled = true
                bHasCalledBySwipe = false
                if (viewPager!!.currentItem == 0) {
                    imgBack!!.visibility = View.GONE
                } else { //                    imgBack.setVisibility(View.GONE);
                    imgFront!!.visibility = View.VISIBLE
                }
                if (CommonMethods.checkInternetConnection(activity)) {
                    setPreviousMonthDates(startDate)
                    if (viewPager!!.currentItem > 0) {
                        viewPager!!.currentItem = viewPager!!.currentItem - 1
                        if (viewPager!!.currentItem == 0) {
                            imgBack!!.visibility = View.GONE
                        }
                    }
                    imgFront!!.visibility = View.VISIBLE
                    //Log.w("DATE_PARAM_B", "s:" + startDate + " e:" + endDate);
// Get the dashboard data from server
                    getAttendanceFromServer(startDate, endDate!!)
                } else {
                    contants2!!.dismissProgressDialog()
                    Contants2.showToastMessage(activity, getString(R.string.no_internet), true)
                }
            }
            R.id.img_front -> {
                //calendar.add(Calendar.MONTH, 1);
//updateMonthLabel();
                bHasCalledBySwipe = false
                //                03/01/2018
//                Mon Mar 12 13:28:14 IST 2018
//
                if (hideNextMonth(endDate!!)) {
                    if (CommonMethods.checkInternetConnection(activity)) {
                        setNextMonthDates(startDate)
                        if (viewPager!!.currentItem < MI_NO_OF_MONTHS_TO_GENERATE) {
                            viewPager!!.currentItem = viewPager!!.currentItem + 1
                            if (viewPager!!.currentItem == viewPager!!.adapter!!.count - 1) {
                                imgFront!!.visibility = View.GONE
                            }
                        }
                        imgBack!!.visibility = View.VISIBLE
                        //Log.w("DATE_PARAM_B", "s:" + startDate + " e:" + endDate);
// Get the dashboard data from server
                        getAttendanceFromServer(startDate, endDate!!)
                    } else {
                        contants2!!.dismissProgressDialog()
                        Contants2.showToastMessage(activity, getString(R.string.no_internet), true)
                    }
                } else { //Toast.makeText(activity, "Can not see future date", Toast.LENGTH_SHORT).show();
                }
            }
            R.id.img_Resize_cal ->  //ResizeCalendar();
                if (bIsCalendarCollapsed) { // app_bar.setExpanded(true);
                    expand_calendar()
                } else { // app_bar.setExpanded(false);
                    if (miLastVisibleItem == getCurrentMonthPosition()) collapse_calendar(false) else collapse_calendar(true)
                }
            R.id.tvOccassions -> {
                setOccassionViewPagerData(mOccassionsDataList)
                tvOccassions!!.setBackgroundResource(R.drawable.rounded_button_selected)
                tvnNews!!.setBackgroundResource(R.drawable.rounded_button)
                tvCipSession!!.setBackgroundResource(R.drawable.rounded_button)
                tvOccassions!!.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
                tvnNews!!.setTextColor(ContextCompat.getColor(activity!!, R.color.color_viewpager_text_not_selected))
                tvCipSession!!.setTextColor(ContextCompat.getColor(activity!!, R.color.color_viewpager_text_not_selected))
            }
            R.id.tvnNews -> {
                setNewsViewPagerData(mNewsDataList)
                tvOccassions!!.setBackgroundResource(R.drawable.rounded_button)
                tvnNews!!.setBackgroundResource(R.drawable.rounded_button_selected)
                tvCipSession!!.setBackgroundResource(R.drawable.rounded_button)
                tvnNews!!.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
                tvOccassions!!.setTextColor(ContextCompat.getColor(activity!!, R.color.color_viewpager_text_not_selected))
                tvCipSession!!.setTextColor(ContextCompat.getColor(activity!!, R.color.color_viewpager_text_not_selected))
            }
            R.id.tvCipSession -> {
                setCipSessionViewPagerData(mCipSessionDataList)
                tvOccassions!!.setBackgroundResource(R.drawable.rounded_button)
                tvnNews!!.setBackgroundResource(R.drawable.rounded_button)
                tvCipSession!!.setBackgroundResource(R.drawable.rounded_button_selected)
                tvCipSession!!.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
                tvnNews!!.setTextColor(ContextCompat.getColor(activity!!, R.color.color_viewpager_text_not_selected))
                tvOccassions!!.setTextColor(ContextCompat.getColor(activity!!, R.color.color_viewpager_text_not_selected))
            }
            R.id.section_holidays -> startActivity(Intent(activity, NewHolidayActivity::class.java))
            R.id.section_hr_manual -> if (!contants2!!.pdfFileExists(getActivity(), Contants2.hr_manual_file_name)) getPdf(Contants2.HRManualContentCode) else try {
                contants2!!.viewPdfFile(getActivity(), Contants2.hr_manual_file_name)
            } catch (e: ActivityNotFoundException) {
                Contants2.showToastMessage(getActivity(), getString(R.string.no_application_found), false)
            }
            R.id.section_mediclaim_policy -> if (!contants2!!.pdfFileExists(getActivity(), Contants2.mediclaim_policy_file_name)) getPdf(Contants2.MediclaimPolicyContentCode) else {
                try {
                    contants2!!.viewPdfFile(getActivity(), Contants2.mediclaim_policy_file_name)
                } catch (e: ActivityNotFoundException) {
                    Contants2.showToastMessage(getActivity(), getString(R.string.no_application_found), false)
                }
            }
            R.id.section_anti_sexual_policy -> if (!contants2!!.pdfFileExists(getActivity(), Contants2.ASH_policy_file_name)) getPdf(Contants2.AntiSexualPolicyContentCode) else try {
                contants2!!.viewPdfFile(getActivity(), Contants2.ASH_policy_file_name)
            } catch (e: ActivityNotFoundException) {
                Contants2.showToastMessage(getActivity(), getString(R.string.no_application_found), false)
            }
            else -> {
            }
        }
    }

    private var iCurrentDtPos = -1

    private fun scrollToCurrentDate(callScrollToWithWebApi: Boolean) {
        bHasCalledBySwipe = false
        viewPager!!.currentItem = getCurrentMonthPosition()
        val iCurrentSelectedPos = viewPager!!.currentItem //getCurrentMonthPosition();
        if (iCurrentSelectedPos == getCurrentMonthPosition()) {
            val view = viewPager!!.findViewWithTag<View>(iCurrentSelectedPos)
            if (view != null) {
                val recyclerView: RecyclerView = view.findViewById(R.id.month_recyView)
                if (recyclerView != null) if (recyclerView.adapter != null) {
                    val monthAdapter = recyclerView.adapter as MonthAdapter?
                    if (iCurrentDtPos == -1) iCurrentDtPos = monthAdapter!!.dateScrollPos
                    if (monthAdapter!!.itemCount > iCurrentDtPos) {
                        recyclerView.scrollToPosition(iCurrentDtPos)
                        viewPagerAdapter!!.monthAdapterArrayList[viewPager!!.currentItem].isCalendarCollapsed = true
                        if (callScrollToWithWebApi) {
                            currentServerDate = getCurrentMonthDate(year + 1)
                            if (startDate.split("/").toTypedArray()[0] != currentServerDate!!.split("/").toTypedArray()[1]) {
                                setStartDateEndDate(currentServerDate!!)
                                getAttendanceFromServer(startDate, endDate!!)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Update the calendar data
     */
    private fun updateMonthLabel() {
        txtmonthLbl!!.text = getItemFromPosition(viewPager!!.currentItem)
    }

    private fun generateCalendarArrayList(year: Int, month: Int): ArrayList<String?>? {
        val stringArrayList = ArrayList<String?>()
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        for (no in 0 until MI_NO_OF_MONTHS_TO_GENERATE) {
            val dateFormat = SimpleDateFormat("MMMM yyyy")
            stringArrayList.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.MONTH, -1)
        }
        Collections.reverse(stringArrayList)
        return stringArrayList
    }


    private fun getCurrentMonthPosition(): Int { //        ArrayList<String> stringArrayList = generateCalendarArrayList(year, month);
        val calendar = Calendar.getInstance()
        //calendar.set(Calendar.YEAR, year + 1);
        val dateFormat = SimpleDateFormat("MMMM yyyy")
        val currentMonth = dateFormat.format(calendar.time)
        for (no in stringArrayList!!.indices) {
            if (currentMonth == stringArrayList!![no]) {
                return no
            }
        }
        return 0
    }


    /**
     * @param position position of view pager
     * @return MMMM yyyy is returned
     */
    private fun getItemFromPosition(position: Int): String? { //        ArrayList<String> stringArrayList = generateCalendarArrayList(year, month);
        val monthYear = stringArrayList!![position]
        txtmonthLbl!!.text = monthYear
        return monthYear
    }

    override fun onDownloadFailed(strResult: String?) {
        contants2!!.dismissProgressDialog()
        Contants2.showToastMessage(getActivity(), getString(R.string.downloadFailed), true)
    }

    override fun onDownloadSuccess(strResult: String?, fileName: String?) {
        contants2!!.dismissProgressDialog()
        try {
            contants2!!.viewPdfFile(getActivity(), fileName)
        } catch (e: ActivityNotFoundException) {
            Contants2.showToastMessage(getActivity(), getString(R.string.no_application_found), true)
        }
    }

    /**
     * Get the instance of the calendar
     */
    private fun getCalendar(): Calendar? {
        val objCal = Calendar.getInstance()
        objCal.time = Date()
        return objCal
    }

    /**
     * Get current month
     */
    private fun getCurrentMonthDate(year: Int): String? {
        calendar!![Calendar.YEAR] = year
        val df1 = SimpleDateFormat("dd/MM/yyyy")
        var existingDate = df1.format(calendar!!.time)
        val dateSplit = existingDate.split("/").toTypedArray()
        existingDate = "01/" + dateSplit[1] + "/" + dateSplit[2]
        return existingDate
    }

    /******************************************** WEB API CALLS ***********************************/

    /******************************************** WEB API CALLS  */
    /**
     * get dashboard deta from server
     */
    private fun getDashboardData() {
        if (Contants2.checkInternetConnection(getActivity())) {
            contants2!!.showProgressDialog(getActivity())
            val apiInterface = retrofit!!.create(WebApiInterface::class.java)
            val mSharedPrefs = Sharedprefrences.getInstance(activity)
            val strToken = mSharedPrefs.getString("Token", "")
            val strEmpId = mSharedPrefs.getString("emp_Id", "")
            // Make list of parameter for sending the http request
            val params: MutableMap<String, String> = HashMap()
            params["EmpId"] = strEmpId
            params["Days"] = "7"
            params["LocationID"] = "0"
            params["Startdate"] = "" // sending blank parameter to get current data and date from server.
            val call = apiInterface.getDashboardData(strEmpId, strToken, params)
            call.enqueue(object : Callback<DashboardMaster?> {
                override fun onResponse(call: Call<DashboardMaster?>, response: Response<DashboardMaster?>) {
                    when (response.code()) {
                        200 -> {
                            val objDashboardMaster = response.body()
                            arrayListDashboardData = objDashboardMaster!!.dashboardData
                            val arrayListOccassionsData = objDashboardMaster.occassionsData
                            val arrayListNewsDataList = objDashboardMaster.news
                            val arrayListCipSessions = objDashboardMaster.cipSessions
                            if (arrayListOccassionsData == null || arrayListOccassionsData.size == 0) {
                                val occassionsData = OccassionsData()
                                occassionsData.occassionName = ""
                                occassionsData.occassionDate = ""
                                occassionsData.occassionFor = "No Occasion Found"
                                arrayListOccassionsData.add(occassionsData)
                            }
                            if (arrayListNewsDataList == null || arrayListNewsDataList.size == 0) {
                                val news = News()
                                news.newsTitle = ""
                                news.newsDate = "No news available."
                                arrayListNewsDataList.add(news)
                            }
                            if (arrayListCipSessions == null || arrayListCipSessions.size == 0) {
                                val cipSession = CIPSession()
                                cipSession.cipSessionsInfo = "No CIP sessions available."
                                arrayListCipSessions.add(cipSession)
                            }
                            mOccassionsDataList = arrayListOccassionsData
                            mNewsDataList = arrayListNewsDataList
                            mCipSessionDataList = arrayListCipSessions
                            mobjDashboardData = arrayListDashboardData
                            setOccassionViewPagerData(arrayListOccassionsData)
                            setImageIndicator()
                            // if start date and end date blank means freshly screen loaded,
// hence need to set the start and end date as per date recieved from server
                            if (startDate == "" && endDate == "") { // below bug found on local - http://52.172.192.203
// this if condition is for when current date is 01 of month, that time arrayListDashboardData.size() = 0
                                if (arrayListDashboardData!!.size > 0) {
                                    currentServerDate = arrayListDashboardData!!.get(0).attendanceDate
                                    setStartDateEndDate(currentServerDate!!)
                                    year = currentServerDate!!.split("/").toTypedArray()[2].toInt()
                                    month = currentServerDate!!.split("/").toTypedArray()[1].toInt() - 1
                                    stringArrayList = generateCalendarArrayList(year, month)
                                    //create month with blank dates
                                    setUpCalendarViewPager(year, arrayListDashboardData!!)
                                } else {
                                    rr_calendar!!.visibility = View.GONE
                                    Contants2.showToastMessage(context, "Dashboard data not available now, try later!", true)
                                    contants2!!.dismissProgressDialog()
                                }
                            }
                            /*  set Data of dates for current month by .setData() method */Handler().postDelayed({
                                // set DashboardData if present
                                if (arrayListDashboardData!!.size > 1) {
                                    viewPagerAdapter!!.monthAdapterArrayList[viewPager!!.currentItem].setData(arrayListDashboardData)
                                }
                            }, 2000)
                        }
                        403 -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showOkAlertDialog(getActivity(), response.message(), "") { dialog, which -> Contants2.forceLogout(getActivity()) }
                        }
                        500 -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                        }
                        else -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                        }
                    }
                    bHasCalledBySwipe = true
                }

                override
                fun onFailure(call: Call<DashboardMaster?>, t: Throwable) {
                    call.cancel()
                    contants2!!.dismissProgressDialog()
                    bHasCalledBySwipe = true
                    Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                }
            })
        } else {
            Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true)
        }
    }


    /**
     * Get Attendance data from server
     */
    private fun getAttendanceFromServer(attendanceStartDate: String, attendanceEndDate: String) {
        Log.d("getAttendanceFromServer", "getAttendanceFromServer")
        // showProgressDialog();
        contants2!!.showProgressDialog(getActivity())
        val apiInterface = retrofit!!.create(WebApiInterface::class.java)
        val mSharedPrefs = Sharedprefrences.getInstance(activity)
        val strToken = mSharedPrefs.getString("Token", "")
        val strEmpId = mSharedPrefs.getString("emp_Id", "")
        // Make list of parameter for sending the http request
        val params: MutableMap<String, String> = HashMap()
        params["EmpId"] = strEmpId
        params["Startdate"] = attendanceStartDate
        params["EndDate"] = attendanceEndDate
        val call = apiInterface.getAttendanceData(strEmpId, strToken, params)
        call.enqueue(object : Callback<AttendanceMaster?> {
            override fun onResponse(call: Call<AttendanceMaster?>, response: Response<AttendanceMaster?>) {
                when (response.code()) {
                    200 -> {
                        updateMonthLabel()
                        val objAttendanceMaster = response.body()
                        arrayListDashboardData = objAttendanceMaster!!.attendanceData
                        if (arrayListDashboardData!!.size > 0) {
                            for (attendanceData in arrayListDashboardData!!) {
                                if (attendanceData.attStatus.equals("A", ignoreCase = true)) attendanceData.description = "Absent"
                            }
                            viewPagerAdapter!!.monthAdapterArrayList[viewPager!!.currentItem].setData(arrayListDashboardData)
                            // viewPagerAdapter.monthAdapterArrayList.get(viewPager.getCurrentItem()).notifyDataSetChanged();
                        }
                        contants2!!.dismissProgressDialog()
                    }
                    403 -> {
                        contants2!!.dismissProgressDialog()
                        Contants2.showOkAlertDialog(getActivity(), response.message(), "") { dialog, which -> Contants2.forceLogout(getActivity()) }
                    }
                    500 -> {
                        contants2!!.dismissProgressDialog()
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                    }
                    else -> {
                        contants2!!.dismissProgressDialog()
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true) //hideshowConstraintLayout(true);
                    }
                }
                bHasCalledBySwipe = true
            }

            override
            fun onFailure(call: Call<AttendanceMaster?>, t: Throwable) {
                bHasCalledBySwipe = true
                call.cancel()
                contants2!!.dismissProgressDialog()
                Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                Toast.makeText(activity, "Some error occurred! please try again later..", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * sets the local variable "startDate" & "endDate"
     *
     * @param currentDate String date which is the current date
     */
    private fun setStartDateEndDate(currentDate: String) {
        val arrStartEndDate: Array<String>
        arrStartEndDate = contants2!!.setStartEndDates(currentDate).split("--").toTypedArray()
        startDate = arrStartEndDate[0]
        endDate = arrStartEndDate[1]
    }

    /**
     * sets start and end date if prev month is shown
     * both dates set in format MM/dd/yyyy
     */
    private fun setPreviousMonthDates(currentDate: String) {
        val arrStartEndDate: Array<String>
        arrStartEndDate = contants2!!.setPrevMonthStartEndDate(currentDate, txtmonthLbl).split("--").toTypedArray()
        startDate = arrStartEndDate[0]
        endDate = arrStartEndDate[1]
    }

    /**
     * sets start and end date if next month is shown
     * * both dates set in format MM/dd/yyyy
     */
    private fun setNextMonthDates(currentDate: String) {
        val arrStartEndDate: Array<String>
        arrStartEndDate = contants2!!.setNextMonthStartEndDate(currentDate, txtmonthLbl).split("--").toTypedArray()
        startDate = arrStartEndDate[0]
        endDate = arrStartEndDate[1]
    }

    /**
     * Create a dummy data for occasion list
     */
    private fun getDummyDataForOccasion(): ArrayList<OccassionsData>? {
        val objOccassionsData = OccassionsData()
        objOccassionsData.occassionFor = "No Occasion Found"
        objOccassionsData.occassionDate = ""
        objOccassionsData.occassionName = ""
        objOccassionsData.occasionImageUrl = "http:/dummy.com/1.png"
        val objOccassionsDataArrayList = ArrayList<OccassionsData>()
        objOccassionsDataArrayList.add(objOccassionsData)
        return objOccassionsDataArrayList
    }

    /**
     * common web call to get all 3 pdf and store it in respective places
     *
     * @param pdfType String value specifying the type of pdf to be donwloaded
     */
    private fun getPdf(pdfType: String) {
        if (Contants2.checkInternetConnection(activity)) {
            contants2!!.showProgressDialog(activity)
            val apiInterface = retrofit!!.create(WebApiInterface::class.java)
            val mSharedPrefs = Sharedprefrences.getInstance(activity)
            val strToken = mSharedPrefs.getString("Token", "")
            val strEmpId = mSharedPrefs.getString("emp_Id", "")
            // Make list of parameter for sending the http request
            val params: MutableMap<String, String> = HashMap()
            params["EmpId"] = strEmpId
            params["Code"] = pdfType
            val call = apiInterface.getDocument(strEmpId, strToken, params)
            call.enqueue(object : Callback<DocumentData> {
                override fun onResponse(call: Call<DocumentData>, response: Response<DocumentData>) {
                    when (response.code()) {
                        200 -> {
                            val fileDownloader = FileDownloader(getActivity(), this@DashboardFragment)
                            val pdfFileName: String
                            pdfFileName = if (pdfType == Contants2.HRManualContentCode) {
                                Contants2.hr_manual_file_name
                            } else if (pdfType == Contants2.MediclaimPolicyContentCode) {
                                Contants2.mediclaim_policy_file_name
                            } else {
                                Contants2.ASH_policy_file_name
                            }
                            fileDownloader.execute(response.body()!!.docData, pdfFileName)
                        }
                        403 -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showOkAlertDialog(getActivity(), response.message(), "") { dialog, which -> Contants2.forceLogout(getActivity()) }
                        }
                        500 -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                        }
                    }
                }

                override fun onFailure(call: Call<DocumentData>, t: Throwable) {
                    contants2!!.dismissProgressDialog()
                    Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                }
            })
        } else {
            Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true)
        }
    }

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
                imgFront!!.isEnabled = false
                isNextBtnVisible = false
            } else {
                imgFront!!.isEnabled = true
                isNextBtnVisible = true
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return isNextBtnVisible
    }

    /**
     * collpase calendar
     */
    private fun collapse_calendar(callScrollToWithApi: Boolean) { // To set recyclerView miViewPagerCalendarheight parameter
/*ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        // Collapse calendar
        params.height = miViewPagerCalendarheight / 5;
        viewPager.setLayoutParams(params);
        // rotate arrow image to 0 degree i.e to collapsible mode
        imgResizeCal.setRotation(0);
        //slide_down(activity,viewPager);


        scrollToCurrentDate(callScrollToWithApi);
        updateMonthLabel();
        bIsCalendarCollapsed = true;
        if (bIsScrollByCollapse) {
            app_bar.setExpanded(true);
            expand_calendar();
        }*/
    }

    /**
     * expand calendar
     */
    private fun expand_calendar() { /*// To set recyclerView miViewPagerCalendarheight parameter
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        // Expand calendar

        params.height = miViewPagerCalendarheight;
        viewPager.setLayoutParams(params);
        // rotate arrow image to 180 degree i.e to normal mode
        imgResizeCal.setRotation(180);
        //slide_down(activity,viewPager);

        */
        /************************************START */ /*
         */
        /******* Added later for rebinding the data after coming to state expand  */ /*
        int iCurrentSelectedPos = viewPager.getCurrentItem();//getCurrentMonthPosition();
        View view = viewPager.findViewWithTag(iCurrentSelectedPos);
        RecyclerView recyclerView = view.findViewById(R.id.month_recyView);
        recyclerView.scrollToPosition(0);   // scroll from 0th position
        //viewPagerAdapter.monthAdapter.setData(mobjDashboardData);
        viewPagerAdapter.monthAdapterArrayList.get(viewPager.getCurrentItem()).setData(arrayListDashboardData);
        */
        /*************************************END */ /*
        updateMonthLabel();
        bIsCalendarCollapsed = false;*/
    }

    abstract class AppBarStateChangeListener : OnOffsetChangedListener {
        private var mCurrentState = DashboardFragment.State.IDLE
        override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
            mCurrentState = if (i == 0) {
                if (mCurrentState != DashboardFragment.State.EXPANDED) {
                    onStateChanged(appBarLayout, DashboardFragment.State.EXPANDED)
                }
                DashboardFragment.State.EXPANDED
            } else if (Math.abs(i) >= appBarLayout.totalScrollRange) {
                if (mCurrentState != DashboardFragment.State.COLLAPSED) {
                    onStateChanged(appBarLayout, DashboardFragment.State.COLLAPSED)
                }
                DashboardFragment.State.COLLAPSED
            } else {
                if (mCurrentState != DashboardFragment.State.IDLE) {
                    onStateChanged(appBarLayout, DashboardFragment.State.IDLE)
                }
                DashboardFragment.State.IDLE
            }
        }

        abstract fun onStateChanged(appBarLayout: AppBarLayout?, state: DashboardFragment.State?)
    }

}
