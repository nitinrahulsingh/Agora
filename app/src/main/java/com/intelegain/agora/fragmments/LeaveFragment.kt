package com.intelegain.agora.fragmments

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.intelegain.agora.R
import com.intelegain.agora.activity.NewApplyLeaveActivity
import com.intelegain.agora.adapter.NewLeavesAdapter
import com.intelegain.agora.api.urls.CommonMethods
import com.intelegain.agora.api.urls.RetrofitClient
import com.intelegain.agora.constants.Constants
import com.intelegain.agora.interfeces.OnItemLongClickListener
import com.intelegain.agora.interfeces.WebApiInterface
import com.intelegain.agora.model.*
import com.intelegain.agora.utils.Contants2
import com.intelegain.agora.utils.Sharedprefrences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class LeaveFragment : Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener, OnItemLongClickListener,
        OnRefreshListener {
    private val TAG = javaClass.simpleName
    private var activity: Activity? = null
    private var app_bar: AppBarLayout? = null
    private var mView: View? = null

    val applyLeave = 1
    // Widget declaration
    private var tvTotalCL: TextView? = null  // Widget declaration
    private var tvTotalCLTillDate: TextView? = null  // Widget declaration
    private var tvConsumedCL: TextView? = null  // Widget declaration
    private var tvBalanceCL: TextView? = null  // Widget declaration
    private var tvTotalSL: TextView? = null  // Widget declaration
    private var tvTotalSLTillDate: TextView? = null
    // Widget declaration
    private var tvConsumedSL: TextView? = null  // Widget declaration
    private var tvBalanceSL: TextView? = null  // Widget declaration
    private var tvTotalPL: TextView? = null  // Widget declaration
    private var tvTotalPLTillDate: TextView? = null  // Widget declaration
    private var tvConsumedPL: TextView? = null  // Widget declaration
    private var tvCurrentYearSlap: TextView? = null
    // Widget declaration
    private var tvBalancePL: TextView? = null  // Widget declaration
    private var tvTotalCO: TextView? = null  // Widget declaration
    private var tvTotalCOTillDate: TextView? = null  // Widget declaration
    private var tvConsumedCO: TextView? = null  // Widget declaration
    private var tvBalanceCO: TextView? = null  // Widget declaration
    private val tvLeaveFromDate: TextView? = null  // Widget declaration
    private val tvLeaveToDate: TextView? = null

    private var tv_try_again: TextView? = null

    private var ivLeaveFilter /*, iv_Resize_table*/: ImageView? = null
    private  var ivPrevYear:android.widget.ImageView? = null
    private  var ivNextYear:android.widget.ImageView? = null
    private var fabApplyForLeave: FloatingActionButton? = null
    private var table_container: TableLayout? = null
    private var mRecyViewForLeaves: RecyclerView? = null
    private var mobjLeavesAdapter: NewLeavesAdapter? = null
    private val mlstLeaveData: ArrayList<LeaveData>? = null
    private var mlstAppliedLeaveDetails: ArrayList<AppliedLeaveDetails.LeaveData>? = null
    private var mLeaveData: ArrayList<LeaveData>? = null
    private var miTableContainerheight = 0
    private var bIsTableContainerCollapsed = false


    private val mlstLeaveType = ArrayList<String>()
    private val bIsStartDateClicked = false
    private var linearLayoutManager: LinearLayoutManager? = null
    private val progressDialog: ProgressDialog? = null
    private var retrofit: Retrofit? = null
    // NOTE: first element deliberately set to blank to match the size with spinner
    private val leaveType = arrayOf("")
    private var dialog: Dialog? = null
    private val dialog_view: View? = null
    private var inflater: LayoutInflater? = null
    private val dialogRecyclerView: RecyclerView? = null
    private val mlstLeaveFilterList = ArrayList<String>()
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    var contants2: Contants2? = null
    private var fiscalYear = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_leaves, container, false)

        activity = getActivity()

        InitializeWidget(mView!!)
        initialize()
        setEventClickListener()

        return mView
    }

    private fun initialize() {
        contants2 = Contants2()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tree observer to measure the miViewPagerCalendarheight of viewpager once view is created
        table_container!!.viewTreeObserver.addOnPreDrawListener(object :ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                table_container!!.getViewTreeObserver().removeOnPreDrawListener(this);
                miTableContainerheight = table_container!!.getHeight();
                Log.i(TAG, "View pager height = " + miTableContainerheight);
                return false;
            }

        })

        PrepareLeaveFilterList();
        retrofit = RetrofitClient.getInstance(Constants.BASE_URL);
        // Get the leaves data from server
        // getLeaveDataFromServer();
        if (CommonMethods.checkInternetConnection(activity))
            //getAvailableLeaves();
            getAllLeaveDetails("0");
        else {
            Contants2.showToastMessage(activity, getString(R.string.no_internet), false);
            hideRecycler(true, "getAvailableLeaves");
        }

    }

    private fun InitializeWidget(view: View) {
        tvTotalCL = view.findViewById(R.id.tvTotalCL)
        tvTotalCLTillDate = view.findViewById(R.id.tvTotalCLTillDate)
        tvConsumedCL = view.findViewById(R.id.tvConsumedCL)
        tvBalanceCL = view.findViewById(R.id.tvBalanceCL)
        tvTotalSL = view.findViewById(R.id.tvTotalSL)
        tvTotalSLTillDate = view.findViewById(R.id.tvTotalSLTillDate)
        tvConsumedSL = view.findViewById(R.id.tvConsumedSL)
        tvBalanceSL = view.findViewById(R.id.tvBalanceSL)
        tvTotalPL = view.findViewById(R.id.tvTotalPL)
        tvTotalPLTillDate = view.findViewById(R.id.tvTotalPLTillDate)
        tvConsumedPL = view.findViewById(R.id.tvConsumedPL)
        tvBalancePL = view.findViewById(R.id.tvBalancePL)
        tvTotalCO = view.findViewById(R.id.tvTotalCO)
        tvTotalCOTillDate = view.findViewById(R.id.tvTotalCOTillDate)
        tvConsumedCO = view.findViewById(R.id.tvConsumedCO)
        tvBalanceCO = view.findViewById(R.id.tvBalanceCO)
        tvCurrentYearSlap = view.findViewById(R.id.tvCurrentYearSlap)
        app_bar = view.findViewById(R.id.app_bar)
        table_container = view.findViewById(R.id.table_container)
        mRecyViewForLeaves = view.findViewById(R.id.recyclerViewForLeaves)
        ivLeaveFilter = view.findViewById(R.id.ivLeaveFilter)
        // iv_Resize_table = view.findViewById(R.id.iv_Resize_table);
        ivPrevYear = view.findViewById(R.id.ivPrevYear)
        ivNextYear = view.findViewById(R.id.ivNextYear)
        fabApplyForLeave = view.findViewById(R.id.fabApply)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        tv_try_again = view.findViewById(R.id.text_view_try_again)
        inflater = getActivity()!!.layoutInflater
        dialog = Dialog(getActivity()!!, R.style.MyAlertDialogStyle)
    }

    /**
     * Set the event click listener for views
     */
    private fun setEventClickListener() { //iv_Resize_table.setOnClickListener(this);
        ivLeaveFilter!!.setOnClickListener(this)
        ivPrevYear!!.setOnClickListener(this)
        ivNextYear!!.setOnClickListener(this)
        tv_try_again!!.setOnClickListener(this)
        fabApplyForLeave!!.setOnClickListener(this)
        swipeRefreshLayout!!.setOnRefreshListener(this)
        app_bar!!.addOnOffsetChangedListener(object  : LeaveFragment.AppBarStateChangeListener(){
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: DashboardFragment.State?) {
                if (state == DashboardFragment.State.COLLAPSED) {
                    Log.i(TAG, "State Collapsed")
                    // iv_Resize_table.setRotation(0);
                    bIsTableContainerCollapsed = true
                } else if (state == DashboardFragment.State.EXPANDED) {
                    Log.i(TAG, "State Expanded")
                    // iv_Resize_table.setRotation(180);
                    bIsTableContainerCollapsed = false
                } else if (state == DashboardFragment.State.IDLE) {
                    Log.i(TAG, "State Idle")
                } else { // do nothing
                }
            }

        })
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivLeaveFilter -> showLeaveTypeFilterDialog()
            R.id.ivPrevYear -> if (Contants2.checkInternetConnection(activity)) {
                fiscalYear = fiscalYear - 1
                getAllLeaveDetails(fiscalYear.toString() + "")
            } else {
                Contants2.showToastMessage(activity, getString(R.string.no_internet), false)
            }
            R.id.ivNextYear -> if (Contants2.checkInternetConnection(activity)) {
                fiscalYear = fiscalYear + 1
                getAllLeaveDetails(fiscalYear.toString() + "")
            } else {
                Contants2.showToastMessage(activity, getString(R.string.no_internet), false)
            }
            R.id.text_view_try_again -> if (CommonMethods.checkInternetConnection(activity)) getAllLeaveDetails(fiscalYear.toString() + "") else {
                swipeRefreshLayout!!.isRefreshing = false
                Contants2.showToastMessage(activity, getString(R.string.no_internet), false)
            }
            R.id.fabApply -> {
                val intent = Intent(getActivity(), NewApplyLeaveActivity::class.java)
                intent.putExtra("balanceCl", tvBalanceCL!!.text.toString().trim { it <= ' ' })
                intent.putExtra("balanceSl", tvBalanceSL!!.text.toString().trim { it <= ' ' })
                intent.putExtra("balancePl", tvBalancePL!!.text.toString().trim { it <= ' ' })
                intent.putExtra("balanceCo", tvBalanceCO!!.text.toString().trim { it <= ' ' })
                startActivityForResult(intent, LeavesFragment.applyLeave)
                getActivity()!!.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val formattedDate = ((if (dayOfMonth <= 9) "0$dayOfMonth" else dayOfMonth)
                .toString() + "/" + (if (month + 1 <= 9) "0" + (month + 1) else month + 1)
                + "/" + year)
        if (bIsStartDateClicked) {
            tvLeaveFromDate!!.text = formattedDate
        } else {
            tvLeaveToDate!!.text = formattedDate
        }
    }

    override fun onItemLongClicked(position: Int, leaveId: Int): Boolean {
        Contants2.showAlertDialog(getActivity(), getString(R.string.delete_leave), "Delete Leave", { dialogInterface, i -> deleteLeave(position, leaveId) }, { dialogInterface, i ->
            mobjLeavesAdapter!!.notifyDataSetChanged()
            dialogInterface.dismiss()
        }, false)

        return false
    }

    override fun onRefresh() {
        if (CommonMethods.checkInternetConnection(activity)) getAllLeaveDetails(fiscalYear.toString() + "") else {
            swipeRefreshLayout!!.isRefreshing = false
            Contants2.showToastMessage(activity, getString(R.string.no_internet), false)
        }
    }


    /******************************************** WEB API CALLS ***********************************/

    /******************************************** WEB API CALLS  */
    /**
     * Apply leave to server
     */
    private fun deleteLeave(position: Int, leaveId: Int) {
        contants2!!.showProgressDialog(getActivity())
        val apiInterface = retrofit!!.create(WebApiInterface::class.java)
        val mSharedPrefs = Sharedprefrences.getInstance(getActivity())
        val strToken = mSharedPrefs.getString("Token", "")
        val strEmpId = mSharedPrefs.getString("emp_Id", "")
        // make list of parameter for sending the http request
        val params: MutableMap<String, String> = HashMap()
        // params.put("EmpId", strEmpId);
        params["EmpLeaveId"] = leaveId.toString() + ""
        val call = apiInterface.deleteLeave(strEmpId, strToken, params)
        call.enqueue(object : Callback<CommonStatusMessage?> {
            override fun onResponse(call: Call<CommonStatusMessage?>, response: Response<CommonStatusMessage?>) {
                when (response.code()) {
                    200 -> {
                        val commonStatusMessage = response.body()
                        Contants2.showToastMessage(getActivity(), commonStatusMessage!!.message, true)
                        contants2!!.dismissProgressDialog()
                        mobjLeavesAdapter!!.removeItem(position)
                    }
                    403 -> {
                        Contants2.showOkAlertDialog(getActivity(), response.message(), "") { dialog, which -> Contants2.forceLogout(getActivity()) }
                        contants2!!.dismissProgressDialog()
                    }
                    500 -> {
                        // contants2.dismissProgressDialog();
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                        contants2!!.dismissProgressDialog()
                    }
                    else -> {
                        contants2!!.dismissProgressDialog()
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                    }
                }
            }

            override
            fun onFailure(call: Call<CommonStatusMessage?>, t: Throwable) {
                call.cancel()
                contants2!!.dismissProgressDialog()
                Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), false)
            }
        })
    }

    /**
     * get leave details (i.e no of leaves allowed, balance etc ) from server
     */
    private fun getAllLeaveDetails(year: String) {
        contants2!!.showProgressDialog(getActivity())
        val apiInterface = retrofit!!.create(WebApiInterface::class.java)
        val mSharedPrefs = Sharedprefrences.getInstance(activity)
        val strToken = mSharedPrefs.getString("Token", "")
        val strEmpId = mSharedPrefs.getString("emp_Id", "")
        // make list of parameter for sending the http request
        val params: MutableMap<String, String> = HashMap()
        params["EmpId"] = strEmpId
        params["LeaveType"] = "0"
        params["LeaveStatus"] = "0"
        params["Year"] = year
        val call = apiInterface.getAllLeaveDetails(strEmpId, strToken, params)
        call.enqueue(object : Callback<LeaveMaster?> {
            override fun onResponse(call: Call<LeaveMaster?>, response: Response<LeaveMaster?>) {
                when (response.code()) {
                    200 -> {
                        hideRecycler(false, "getAppliedLeaveDetails")
                        val leaveMaster = response.body()
                        fiscalYear = leaveMaster!!.fiscalYear
                        val fullFiscalYear = "Apr - " + fiscalYear + " To Mar - " + (fiscalYear + 1)
                        tvCurrentYearSlap!!.text = fullFiscalYear
                        //Leave objAvailableLeave = leaveMaster.getLeaveType();// here we're retrieving 0th object because it only contain 1 json object
                        fillAvailableLeavesSection(leaveMaster.leaveType)
                        mLeaveData = leaveMaster.leaveData
                        val iSizeOfAppliedLeave = mLeaveData!!.size
                        if (iSizeOfAppliedLeave <= 0) {
                            Contants2.showToastMessage(getActivity(), getString(R.string.no_data_found), true)
                            hideRecycler(true, "getAppliedLeaveDetails")
                        } else {
                            var i = 0
                            while (i < iSizeOfAppliedLeave) {
                                val objAppliedLeaveDetails = mLeaveData!!.get(i)
                                val strLeaveFrom = convertDate(objAppliedLeaveDetails.getLeaveFrom())
                                objAppliedLeaveDetails.setLeaveFrom(strLeaveFrom)
                                val strLeaveTo = convertDate(objAppliedLeaveDetails.getLeaveTo())
                                objAppliedLeaveDetails.setLeaveTo(strLeaveTo)
                                i++
                            }
                            FillAdapterData()
                        }
                        contants2!!.dismissProgressDialog()
                    }
                    403 -> {
                        Contants2.showOkAlertDialog(getActivity(), response.message(), "") { dialog, which -> Contants2.forceLogout(getActivity()) }
                        contants2!!.dismissProgressDialog()
                    }
                    500 -> {
                        hideRecycler(true, "getAppliedLeaveDetails")
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                        contants2!!.dismissProgressDialog()
                    }
                    else -> {
                        hideRecycler(true, "getAppliedLeaveDetails")
                        contants2!!.dismissProgressDialog()
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                    }
                }
                swipeRefreshLayout!!.isRefreshing = false
            }

            override
            fun onFailure(call: Call<LeaveMaster?>, t: Throwable) {
                call.cancel()
                hideRecycler(true, "getAppliedLeaveDetails")
                contants2!!.dismissProgressDialog()
                swipeRefreshLayout!!.isRefreshing = false
                Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true)
            }
        })
    }


    fun fillAvailableLeavesSection(objAvailableLeave: Leave) {
        val iTotalCL = objAvailableLeave.totalCL
        val iConsumedCL = objAvailableLeave.consumedCL
        val iBalanceCL = objAvailableLeave.balanceCL
        val iTotalCLTillDate = objAvailableLeave.totalCLTillDate
        tvTotalCL!!.text = iTotalCL.toString()
        tvConsumedCL!!.text = iConsumedCL.toString()
        tvBalanceCL!!.text = iBalanceCL.toString()
        tvTotalCLTillDate!!.text = iTotalCLTillDate.toString()
        // Sick leave section
        val iTotalSL = objAvailableLeave.totalSL
        val iConsumedSL = objAvailableLeave.consumedSL
        val iBalanceSL = objAvailableLeave.balanceSL
        val iTotalSLTillDate = objAvailableLeave.totalSLTillDate
        tvTotalSL!!.text = iTotalSL.toString()
        tvConsumedSL!!.text = iConsumedSL.toString()
        tvBalanceSL!!.text = iBalanceSL.toString()
        tvTotalSLTillDate!!.text = iTotalSLTillDate.toString()
        // Paid leave section
        val iTotalPL = objAvailableLeave.totalPL
        val iConsumedPL = objAvailableLeave.consumedPL
        val iBalancePL = objAvailableLeave.balancePL
        val iTotalPLTillDate = objAvailableLeave.totalPLTillDate
        tvTotalPL!!.text = iTotalPL.toString()
        tvConsumedPL!!.text = iConsumedPL.toString()
        tvBalancePL!!.text = iBalancePL.toString()
        tvTotalPLTillDate!!.text = iTotalPLTillDate.toString()
        // Comp off leave section
        val iTotalCO = objAvailableLeave.totalCO
        val iConsumedCO = objAvailableLeave.consumedCO
        val iBalanceCO = objAvailableLeave.balanceCO
        val iTotalCOTillDate = objAvailableLeave.totalCOTillDate
        tvTotalCO!!.text = iTotalCO.toString()
        tvConsumedCO!!.text = iConsumedCO.toString()
        tvBalanceCO!!.text = iBalanceCO.toString()
        tvTotalCOTillDate!!.text = iTotalCOTillDate.toString()
    }

    /**
     * @param hide       if true hides recycler view and shows try again button and vice versa of false
     * @param calledFrom used to determine which api to call in case of no network or failed web call
     */
    private fun hideRecycler(hide: Boolean, calledFrom: String) {
        tv_try_again!!.tag = calledFrom
        if (hide) {
            tv_try_again!!.visibility = View.VISIBLE
            mRecyViewForLeaves!!.visibility = View.GONE
        } else {
            mRecyViewForLeaves!!.visibility = View.VISIBLE
            tv_try_again!!.visibility = View.GONE
        }
    }

    /**
     * Fill adapter data with leaveData list
     */
    private fun FillAdapterData() {
        mobjLeavesAdapter = NewLeavesAdapter(activity, mLeaveData, this)
        mRecyViewForLeaves!!.adapter = mobjLeavesAdapter
        linearLayoutManager = LinearLayoutManager(getActivity())
        mRecyViewForLeaves!!.layoutManager = linearLayoutManager
        setUpItemTouchHelper()
    }

    private fun convertDate(strDateToConvert: String): String {
        var strOutputDate = ""
        val inputFormat: DateFormat = SimpleDateFormat("MM/dd/yyyy")
        val outputFormat: DateFormat = SimpleDateFormat("dd-MMM-yyyy")
        try {
            val objDate = inputFormat.parse(strDateToConvert)
            strOutputDate = outputFormat.format(objDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return strOutputDate
    }

    /**
     * Prepare Leave filter list
     */
    private fun PrepareLeaveFilterList() {
        if (mlstLeaveFilterList.size == 0) {
            mlstLeaveFilterList.add("See All")
            mlstLeaveFilterList.add("Pending")
            mlstLeaveFilterList.add("Approved")
            mlstLeaveFilterList.add("Rejected")
        }
    }

    /**
     * Show Leave Type Filter list dialog
     */
    private fun showLeaveTypeFilterDialog() {
        CommonMethods().customSpinner(activity, "Select Leave Status", inflater, dialogRecyclerView,
                mlstLeaveFilterList, dialog, dialog_view
        ) { position, itemClickText ->
            if (mobjLeavesAdapter != null) {
                if (itemClickText.equals("See All", ignoreCase = true)) mobjLeavesAdapter!!.filter("") else mobjLeavesAdapter!!.filter(itemClickText)
            } else {
                Contants2.showToastMessage(getActivity(), getString(R.string.no_data_found), true)
            }
            dialog!!.hide()
        }
    }

    /**************************************** WEB API CALLS ENDS **********************************/

    /**************************************** WEB API CALLS ENDS  */
    /**
     * Get available leave from server
     */
    private fun getAvailableLeaves() { //showProgressDialog();
        contants2!!.showProgressDialog(activity)
        swipeRefreshLayout!!.isRefreshing = true
        val apiInterface = retrofit!!.create(WebApiInterface::class.java)
        val mSharedPrefs = Sharedprefrences.getInstance(activity)
        val strToken = mSharedPrefs.getString("Token", "")
        val strEmpId = mSharedPrefs.getString("emp_Id", "")
        // make list of parameter for sending the http request
        val params: MutableMap<String, String> = HashMap()
        params["EmpId"] = strEmpId
        val call = apiInterface.getAvailableLeave(strEmpId, strToken, params)
        call.enqueue(object : Callback<ArrayList<AvailableLeave>> {
            override fun onResponse(call: Call<ArrayList<AvailableLeave>>, response: Response<ArrayList<AvailableLeave>>) {
                val iStatusCode = response.code()
                Log.i(TAG, "getAvailableLeaves onResponse: $iStatusCode")
                when (iStatusCode) {
                    200 -> {
                        //contants2.dismissProgressDialog();
                        val objAvailableLeave = response.body()!![0] // here we're retrieving 0th object because it only contain 1 json object
                        // Casual leave section
                        val iTotalCL = objAvailableLeave.totalCL
                        val iConsumedCL = objAvailableLeave.consumedCL
                        val iBalanceCL = objAvailableLeave.balanceCL
                        val iTotalCLTillDate = objAvailableLeave.totalCLTillDate
                        tvTotalCL!!.text = iTotalCL.toString()
                        tvConsumedCL!!.text = iConsumedCL.toString()
                        tvBalanceCL!!.text = iBalanceCL.toString()
                        tvTotalCLTillDate!!.text = iTotalCLTillDate.toString()
                        // Sick leave section
                        val iTotalSL = objAvailableLeave.totalSL
                        val iConsumedSL = objAvailableLeave.consumedSL
                        val iBalanceSL = objAvailableLeave.balanceSL
                        val iTotalSLTillDate = objAvailableLeave.totalSLTillDate
                        tvTotalSL!!.text = iTotalSL.toString()
                        tvConsumedSL!!.text = iConsumedSL.toString()
                        tvBalanceSL!!.text = iBalanceSL.toString()
                        tvTotalSLTillDate!!.text = iTotalSLTillDate.toString()
                        // Paid leave section
                        val iTotalPL = objAvailableLeave.totalPL
                        val iConsumedPL = objAvailableLeave.consumedPL
                        val iBalancePL = objAvailableLeave.balancePL
                        val iTotalPLTillDate = objAvailableLeave.totalPLTillDate
                        tvTotalPL!!.text = iTotalPL.toString()
                        tvConsumedPL!!.text = iConsumedPL.toString()
                        tvBalancePL!!.text = iBalancePL.toString()
                        tvTotalPLTillDate!!.text = iTotalPLTillDate.toString()
                        // Comp off leave section
                        val iTotalCO = objAvailableLeave.totalCO
                        val iConsumedCO = objAvailableLeave.consumedCO
                        val iBalanceCO = objAvailableLeave.balanceCO
                        val iTotalCOTillDate = objAvailableLeave.totalCOTillDate
                        tvTotalCO!!.text = iTotalCO.toString()
                        tvConsumedCO!!.text = iConsumedCO.toString()
                        tvBalanceCO!!.text = iBalanceCO.toString()
                        tvTotalCOTillDate!!.text = iTotalCOTillDate.toString()
                        // dismissProgressDialog();//  dismiss progress dialog
// Now call Applied leave detail service
                        if (CommonMethods.checkInternetConnection(activity)) { //getAppliedLeaveDetails("0");
                        } else {
                            contants2!!.dismissProgressDialog()
                            swipeRefreshLayout!!.isRefreshing = false
                            hideRecycler(true, "getAppliedLeaveDetails")
                            Contants2.showToastMessage(activity, getString(R.string.no_internet), true)
                        }
                    }
                    403 -> {
                        contants2!!.dismissProgressDialog()
                        Contants2.showOkAlertDialog(activity, response.message(), "") { dialog, which -> Contants2.forceLogout(activity) }
                    }
                    500 -> {
                        contants2!!.dismissProgressDialog()
                        Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true)
                        hideRecycler(true, "getAvailableLeaves")
                    }
                    else -> {
                        contants2!!.dismissProgressDialog()
                        Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true)
                        hideRecycler(true, "getAvailableLeaves")
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<AvailableLeave>>, t: Throwable) {
                Log.i(TAG, "onFailure: Error while executing getAvailableLeave request!")
                call.cancel()
                contants2!!.dismissProgressDialog()
                swipeRefreshLayout!!.isRefreshing = false
                hideRecycler(true, "getAvailableLeaves")
                Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true)
            }
        })
    }


    /**
     * @param year value in yyyy to get leaves for the fiscal year
     */
    private fun getAppliedLeaveDetails(year: String) { // TODO: 8/11/17 : take a parameter year and pass the current year and on arrow click pass that respective year
        val apiInterface = retrofit!!.create(WebApiInterface::class.java)
        val mSharedPrefs = Sharedprefrences.getInstance(activity)
        val strToken = mSharedPrefs.getString("Token", "")
        val strEmpId = mSharedPrefs.getString("emp_Id", "")
        // make list of parameter for sending the http request
        val params: MutableMap<String, String> = HashMap()
        params["EmpId"] = strEmpId
        params["LeaveType"] = "0"
        params["LeaveStatus"] = "0"
        params["Year"] = year
        val call = apiInterface.getAppliedLeaveDetails(strEmpId, strToken, params)
        call.enqueue(object : Callback<AppliedLeaveDetails> {
            override fun onResponse(call: Call<AppliedLeaveDetails>, response: Response<AppliedLeaveDetails>) {
                val iStatusCode = response.code()
                when (iStatusCode) {
                    200 -> {
                        hideRecycler(false, "getAppliedLeaveDetails")
                        val appliedLeaveDetails = response.body()
                        fiscalYear = response.body()!!.fiscalYear
                        val fullFiscalYear = "Apr - " + fiscalYear + " To Mar - " + (fiscalYear + 1)
                        tvCurrentYearSlap!!.text = fullFiscalYear
                        mlstAppliedLeaveDetails = appliedLeaveDetails!!.leaveData
                        val iSizeOfAppliedLeave = mlstAppliedLeaveDetails!!.size
                        if (iSizeOfAppliedLeave <= 0) {
                            Contants2.showToastMessage(getActivity(), getString(R.string.no_data_found), true)
                            hideRecycler(true, "getAppliedLeaveDetails")
                        } else {
                            var i = 0
                            while (i < iSizeOfAppliedLeave) {
                                val objAppliedLeaveDetails = mlstAppliedLeaveDetails!!.get(i)
                                val strLeaveFrom = convertDate(objAppliedLeaveDetails.getLeaveFrom())
                                objAppliedLeaveDetails.setLeaveFrom(strLeaveFrom)
                                val strLeaveTo = convertDate(objAppliedLeaveDetails.getLeaveTo())
                                objAppliedLeaveDetails.setLeaveTo(strLeaveTo)
                                i++
                            }
                            FillAdapterData()
                        }
                        contants2!!.dismissProgressDialog()
                    }
                    403 -> {
                        Contants2.showOkAlertDialog(getActivity(), response.message(), "") { dialog, which -> Contants2.forceLogout(getActivity()) }
                        contants2!!.dismissProgressDialog()
                    }
                    500 -> {
                        hideRecycler(true, "getAppliedLeaveDetails")
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                        contants2!!.dismissProgressDialog()
                    }
                    else -> {
                        hideRecycler(true, "getAppliedLeaveDetails")
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                        contants2!!.dismissProgressDialog()
                    }
                }
                swipeRefreshLayout!!.isRefreshing = false
            }

            override fun onFailure(call: Call<AppliedLeaveDetails>, t: Throwable) {
                Log.i(TAG, "onFailure: Error while executing getAppliedLeaveDetails request!")
                call.cancel()
                hideRecycler(true, "getAppliedLeaveDetails")
                contants2!!.dismissProgressDialog()
                swipeRefreshLayout!!.isRefreshing = false
                Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true)
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LeavesFragment.applyLeave) if (resultCode == Activity.RESULT_OK) {
            getAllLeaveDetails(fiscalYear.toString() + "")
        }
    }


    /**
     * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method
     * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view
     * background will be visible. That is rarely an desired effect.
     */
    private fun setUpItemTouchHelper() {
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            var background: Drawable? = null
            var xMark: Drawable? = null
            var xMarkMargin = 0
            var initiated = false
            // LinearLayout.LayoutParams layoutParams;
            private fun init() {
                background = ColorDrawable(ContextCompat.getColor(getActivity()!!, R.color.yellow))
                xMark = ContextCompat.getDrawable(getActivity()!!, R.drawable.delete)
                //xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = getActivity()!!.resources.getDimension(R.dimen.ic_clear_margin).toInt()
                initiated = true
            }

            // not important, we don't want drag & drop
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int { //int position = viewHolder.getAdapterPosition();
/*if (isSelected)
                    return 0;*/
/*  TestAdapter testAdapter = (TestAdapter)recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }*/
                return super.getSwipeDirs(recyclerView, viewHolder)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) { ///set here view swiped
                val swipedPosition = viewHolder.adapterPosition
                if (mobjLeavesAdapter!!.filteredList[swipedPosition].getLeaveStatus().equals("Pending", ignoreCase = true)) mobjLeavesAdapter!!.deleteRow(swipedPosition)
                //                contactsRecyclerViewAdapter.contactsList.get(swipedPosition).isSwiped = true;
//                contactsRecyclerViewAdapter.notifyItemChanged(swipedPosition);
                //NotificationAdapter adapter = (NotificationAdapter) recyclerView_contacts.getAdapter();
                /*adapter.pendingRemoval(swipedPosition);
                                closeAnyOpenState(swipedPosition);*/
                // boolean undoOn = adapter.isUndoOn();
                //if (undoOn) {
                // } else {
                //   adapter.remove(swipedPosition);
                // }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                val itemView = viewHolder.itemView
                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.adapterPosition == -1) { // not interested in those
                    return
                }
                if (!mobjLeavesAdapter!!.filteredList[viewHolder.adapterPosition].getLeaveStatus().equals("Pending", ignoreCase = true)) return
                if (!initiated) {
                    init()
                }
                // draw red background
                background!!.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                background!!.draw(c)
                // draw x mark
                val itemHeight = itemView.bottom - itemView.top
                val intrinsicWidth = xMark!!.intrinsicWidth
                val intrinsicHeight = xMark!!.intrinsicWidth
                val xMarkLeft = itemView.right - xMarkMargin - intrinsicWidth
                val xMarkRight = itemView.right - xMarkMargin
                val xMarkTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                val xMarkBottom = xMarkTop + intrinsicHeight
                xMark!!.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom)
                xMark!!.draw(c)
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        val mItemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        mItemTouchHelper.attachToRecyclerView(mRecyViewForLeaves)
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