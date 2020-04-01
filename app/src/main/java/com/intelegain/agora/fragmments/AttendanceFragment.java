package com.intelegain.agora.fragmments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.intelegain.agora.R;
import com.intelegain.agora.adapter.AttendanceAdapter;
import com.intelegain.agora.api.urls.CommonMethods;
import com.intelegain.agora.constants.Constants;
import com.intelegain.agora.dataFetch.RetrofitClient;
import com.intelegain.agora.interfeces.WebApiInterface;
import com.intelegain.agora.model.AttendanceData;
import com.intelegain.agora.model.AttendanceMaster;
import com.intelegain.agora.utils.Contants2;
import com.intelegain.agora.utils.Sharedprefrences;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AttendanceFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private String TAG = getClass().getSimpleName();
    private Activity activity;
    private ImageView ivPrevMonth, ivNexMonth;
    private TextView tvMonthTitle, tv_try_again;
    private ConstraintLayout container_for_recyclerview_header;
    private RecyclerView recyclerViewForAttendance;
    private AttendanceAdapter attendanceAdapter;
    private ArrayList<AttendanceData> mlstAttendanceData;
    private int iCurrentMonth;
    private int iCurrentYear;
    private ProgressDialog progressDialog;
    private Retrofit retrofit;
    private SwipeRefreshLayout swipeRefreshLayout;
    String startDate, endDate, currentServerDate;
    Contants2 contants2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);    // to retain the state of fragment when activity recreates
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        activity = getActivity();
        initializeObjects();
        InitializeWidget(view);
        setEventClickListener();
        iCurrentMonth = getCurrentMonth();
        iCurrentYear = getCurrentYear();

        return view;
    }

    /**
     * initializing of objects is done here.
     */
    private void initializeObjects() {
        startDate = "";
        endDate = "";
        contants2 = new Contants2();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        retrofit = RetrofitClient.getInstance(Constants.BASE_URL);

        if (CommonMethods.checkInternetConnection(activity)) {
            // Get the Attendance data from server
            getAttendanceDataFromServer(startDate, endDate);
        } else {
            hideRecyclerView(true);
            Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true);
        }
    }

    @Override
    public void onRefresh() {
        if (CommonMethods.checkInternetConnection(activity)) {
            // Get the Attendance data from server
            getAttendanceDataFromServer(startDate, endDate);
        } else {
            hideRecyclerView(true);
            swipeRefreshLayout.setRefreshing(false);
            Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true);
        }
    }

    /**
     * sets up recycler view adapter
     */
    private void setUpRecyclerAdapter() {

        attendanceAdapter = new AttendanceAdapter(activity, mlstAttendanceData);
        recyclerViewForAttendance.setAdapter(attendanceAdapter);
        recyclerViewForAttendance.setLayoutManager(new LinearLayoutManager(activity));
    }

    private ArrayList<AttendanceData> getSampleArrayList() {
        ArrayList<AttendanceData> items = new ArrayList<AttendanceData>();
        for (int i = 0; i < 30; i++) {
//            if (i == 4)
//                items.add(new AttendanceData(AttendanceData.LEAVE));
//            else if (i == 5)
//                items.add(new AttendanceData(AttendanceData.HOLIDAY));
//            else
//                items.add(new AttendanceData(AttendanceData.NORMAL));
        }
        return items;
    }

    /* Set the event click listener for views */
    private void setEventClickListener() {
        ivPrevMonth.setOnClickListener(this);
        ivNexMonth.setOnClickListener(this);
        tv_try_again.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPrevMonth:
                ivNexMonth.setEnabled(true);
                if (CommonMethods.checkInternetConnection(activity)) {
                    // Get the Attendance data from server
                    setPrevStartEndDate();

                    getAttendanceDataFromServer(startDate, endDate);
                } else {
                    dismissProgressDialog();
                    Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), false);
                }
                Log.i(TAG, "Start Date of Month: " + getStartDateOfTheMonth(iCurrentMonth, iCurrentYear));
                Log.i(TAG, "End Date of Month: " + getEndDateOfTheMonth(iCurrentMonth, iCurrentYear));
                break;
            case R.id.ivNexMonth:

                if(hideNextMonth(endDate)) {
                    if (CommonMethods.checkInternetConnection(activity)) {
                        // Get the Attendance data from server
                        setnextStartEndDate();
                        getAttendanceDataFromServer(startDate, endDate);
                    } else {
                        dismissProgressDialog();
                        Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), false);
                    }
                    Log.i(TAG, "Start Date of Month: " + getStartDateOfTheMonth(iCurrentMonth, iCurrentYear));
                    Log.i(TAG, "End Date of Month: " + getEndDateOfTheMonth(iCurrentMonth, iCurrentYear));
                }else {}
                break;
            case R.id.text_view_try_again:
                if (CommonMethods.checkInternetConnection(activity)) {
                    // Get the Attendance data from server
                    getAttendanceDataFromServer(startDate, endDate);
                } else {

                    Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true);
                }
                break;
            default:
                break;
        }
    }

    /**
     * Initialize the widget
     *
     * @param view
     */
    private void InitializeWidget(View view) {
        ivPrevMonth = view.findViewById(R.id.ivPrevMonth);
        ivNexMonth = view.findViewById(R.id.ivNexMonth);
        tvMonthTitle = view.findViewById(R.id.tvMonthTitle);
        recyclerViewForAttendance = view.findViewById(R.id.recyclerViewForAttendance);
        container_for_recyclerview_header = view.findViewById(R.id.container_for_recyclerview_header);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        tv_try_again = view.findViewById(R.id.text_view_try_again);
        // Initially set invisible
        container_for_recyclerview_header.setVisibility(View.GONE);
    }

    /**
     * Set previous month and year
     */
    private void setPrevMonthYear() {
        if (iCurrentMonth == 0) {
            iCurrentMonth = 11;
            iCurrentYear = iCurrentYear - 1;
        } else
            iCurrentMonth = iCurrentMonth - 1;
    }

    /**
     * Set next month and year
     */
    private void setNextMonthYear() {
        if (iCurrentMonth == 11) {
            iCurrentMonth = 0;
            iCurrentYear = iCurrentYear + 1;
        } else
            iCurrentMonth = iCurrentMonth + 1;
    }

    /**
     * sets startdate and enddate to be sent to server one month before of present month
     */
    public void setPrevStartEndDate() {
        int month, year;
        String[] dateSplit;
        String prevDate;
        if (!startDate.equals("")) {
            dateSplit = startDate.split("/");
            month = Integer.parseInt(dateSplit[0]);
            year = Integer.parseInt(dateSplit[2]);

            if (month == 1) {
                month = 12;
                year = year - 1;
                tvMonthTitle.setText(contants2.getMonth3Letter(month - 1) + " " + year);
            } else {
                month = month - 1;
                tvMonthTitle.setText(contants2.getMonth3Letter(month - 1) + " " + year);
            }

            String formattedMonth = month + "";
            formattedMonth = formattedMonth.length() > 1 ? formattedMonth : "0" + formattedMonth;
            prevDate = dateSplit[1] + "/" + formattedMonth + "/" + year;
            setStartEndDates(prevDate);
        }

    }

    /**
     * sets startdate and enddate to be sent to server one month ahead of present month
     */
    public void setnextStartEndDate() {
        int month, year;
        String[] dateSplit;
        String nextDate;
        if (!startDate.equals("")) {
            dateSplit = startDate.split("/");
            month = Integer.parseInt(dateSplit[0]);
            year = Integer.parseInt(dateSplit[2]);

            if (month == 12) {
                month = 1;
                year = year + 1;
                tvMonthTitle.setText(contants2.getMonth3Letter(month - 1) + " " + year);
            } else {
                month = month + 1;
                tvMonthTitle.setText(contants2.getMonth3Letter(month - 1) + " " + year);
            }

            String formattedMonth = month + "";
            formattedMonth = formattedMonth.length() > 1 ? formattedMonth : "0" + formattedMonth;
            nextDate = dateSplit[1] + "/" + formattedMonth + "/" + year;
            setStartEndDates(nextDate);
        }

    }

    /**
     * Set Month and year label
     */
    private void setMonthYearTitle() {
        //  tvMonthTitle.setText(getMonthName(iCurrentMonth) + " " + iCurrentYear);
    }

    /**
     * Get start date of the given month
     */
    private String getStartDateOfTheMonth(int iMonth, int iYear) {
        Calendar objCal = getCalendar();
        objCal.set(Calendar.YEAR, iYear);
        objCal.set(Calendar.MONTH, iMonth);
        objCal.set(Calendar.DAY_OF_MONTH, objCal.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date StartDate = objCal.getTime();
        SimpleDateFormat start_date_format = new SimpleDateFormat("MM/dd/yyyy");
        String strStartDate = start_date_format.format(StartDate);

        return strStartDate;
    }

    /**
     * Get End date of the given month
     */
    private String getEndDateOfTheMonth(int iMonth, int iYear) {
        Calendar objCal = getCalendar();
        objCal.set(Calendar.YEAR, iYear);
        objCal.set(Calendar.MONTH, iMonth);
        objCal.set(Calendar.DAY_OF_MONTH, objCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date EndDate = objCal.getTime();
        SimpleDateFormat end_date_format = new SimpleDateFormat("MM/dd/yyyy");
        String strEndDate = end_date_format.format(EndDate);

        return strEndDate;
    }

    /**
     * Get current month
     */
    private int getCurrentMonth() {
        Calendar objCal = getCalendar();

        return objCal.get(Calendar.MONTH);
    }

    /**
     * Get current Year
     */
    private int getCurrentYear() {
        Calendar objCal = getCalendar();

        return objCal.get(Calendar.YEAR);
    }

    /**
     * sets the start date and end date from server recived date respectilvely
     */
    public void setStartEndDates(String currentServerDate) {
        String[] arrStartEndDate;
        arrStartEndDate = contants2.setStartEndDates(currentServerDate).split("--");
        startDate = arrStartEndDate[0];
        endDate = arrStartEndDate[1];

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
     * Get current month from server date recieved
     */
    private void getCurrentMonthFromServerDate(String currentDate) {
        Date date = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");

        try {
            date = simpleDateFormat.parse(currentDate);
            //startDate = date.getMonth()+"/"
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    /**
     * Get current Year from server date
     */
    private int getCurrentYearFromServerDate() {
        Calendar objCal = getCalendar();

        return objCal.get(Calendar.YEAR);
    }

    /**
     * Get month name from given month number
     */
    private String getMonthName(int iMonth) {
        Calendar objCal = getCalendar();
        objCal.set(Calendar.MONTH, iMonth);
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(objCal.getTime());

        return month_name;
    }

    /**
     * Get the instance of the calendar
     */
    private Calendar getCalendar() {
        Calendar objCal = Calendar.getInstance();
        objCal.setTime(new Date());

        return objCal;
    }

    /**
     * Show progresss dialog
     */
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please wait ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    /**
     * Dismiss the progress dialog
     */
    private void dismissProgressDialog() {
        if (progressDialog != null)
            if (progressDialog.isShowing())
                progressDialog.dismiss();
    }

    /******************************************** WEB API CALLS ***********************************/

    /**
     * Get Attendance data from server
     */
    private void getAttendanceDataFromServer(String attendanceStartDate, String attendanceEndDate) {
        // showProgressDialog();
        contants2.showProgressDialog(getActivity());
        swipeRefreshLayout.setRefreshing(false);
        WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
        Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(activity);
        String strToken = mSharedPrefs.getString("Token", "");
        String strEmpId = mSharedPrefs.getString("emp_Id", "");

        // Make list of parameter for sending the http request
        Map<String, String> params = new HashMap<>();
        params.put("EmpId", strEmpId);
        params.put("Startdate", attendanceStartDate);
        params.put("EndDate", attendanceEndDate);

        Call<AttendanceMaster> call = apiInterface.getAttendanceData(strEmpId, strToken, params);
        call.enqueue(new Callback<AttendanceMaster>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onResponse(Call<AttendanceMaster> call, Response<AttendanceMaster> response) {
                switch (response.code()) {
                    case 200:

                        AttendanceMaster objAttendanceMaster = response.body();
                        ArrayList<AttendanceData> lstAttendanceData = objAttendanceMaster.getAttendanceData();
                        mlstAttendanceData= new ArrayList<>();
                        mlstAttendanceData = lstAttendanceData;
                        if (mlstAttendanceData.size() > 0) {
                            int iSizeAttendanceDataList = mlstAttendanceData.size();
                            for (int i = 0; i < iSizeAttendanceDataList; i++) {
                                AttendanceData objAttendanceData = mlstAttendanceData.get(i);
                                String strTemp = "working hr : " + objAttendanceData.getWorkinghours() +
                                        " Timesheet hr : " + objAttendanceData.getTimesheethours() +
                                        " Desc : " + objAttendanceData.getDescription() +
                                        " AttStatus : " + objAttendanceData.getAttStatus() +
                                        " ViewType : " + objAttendanceData.getViewType();

                                String strAttStatus = objAttendanceData.getAttStatus();
                                //  String strDescription = objAttendanceData.getDescription();

                                if (strAttStatus.equalsIgnoreCase("A")) {
                                    objAttendanceData.setDescription("Absent");
                                }

                                String strWorkingHour = objAttendanceData.getWorkinghours();
                                if (!strWorkingHour.isEmpty()) {

                                    objAttendanceData.setWorkinghours(getTotalHours(strWorkingHour));
                                }

                                String strTimesheetHour = objAttendanceData.getTimesheethours();
                                if (!strTimesheetHour.isEmpty()) {
                                    strTimesheetHour = strTimesheetHour.substring(0, strTimesheetHour.indexOf("hrs") + 3);
                                    strTimesheetHour = strTimesheetHour.replace(" ", "");
                                    objAttendanceData.setTimesheethours(strTimesheetHour);
                                }

                                //Log.i(TAG, "onResponse: " + strTemp);

                                // setting the date recieved from server
                                if (startDate.equals("") && endDate.equals("")) {
                                    currentServerDate = objAttendanceData.getAttendanceDate();
                                    setStartEndDates(currentServerDate);
                                    String[] presentDateSplit = currentServerDate.split("/");
                                    tvMonthTitle.setText(contants2.getMonth3Letter(Integer.parseInt(presentDateSplit[1]) - 1) + " " + presentDateSplit[2]);
                                }
                            }
                            // Set visible once data filled up in adapter
                            container_for_recyclerview_header.setVisibility(View.VISIBLE);
                            setUpRecyclerAdapter();
                            // setMonthYearTitle();
                            hideRecyclerView(false);
                            contants2.dismissProgressDialog();
                        } else {
                            //no data found
                        }
                        contants2.dismissProgressDialog();
                        break;
                    case 403:
                        contants2.dismissProgressDialog();
                        //hideshowConstraintLayout(false);
                        Contants2.showOkAlertDialog(getActivity(), response.message(), "", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Contants2.forceLogout(getActivity());
                            }
                        });
                        break;
                    case 500:
                        hideRecyclerView(true);
                        contants2.dismissProgressDialog();
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                        //hideshowConstraintLayout(true);
                        break;
                    default:
                        hideRecyclerView(true);
                        contants2.dismissProgressDialog();
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                        //hideshowConstraintLayout(true);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.cancel();
                hideRecyclerView(true);
                swipeRefreshLayout.setRefreshing(false);
                contants2.dismissProgressDialog();
                Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
            }
        });
    }

    private void hideRecyclerView(boolean hide) {
        if (hide) {
            recyclerViewForAttendance.setVisibility(View.GONE);
            tv_try_again.setVisibility(View.VISIBLE);
        } else {
            tv_try_again.setVisibility(View.GONE);
            recyclerViewForAttendance.setVisibility(View.VISIBLE);
        }
    }


    private String getTotalHours(String strWorkingHour){
        String strTotalHours="";

        String strHour = strWorkingHour.substring(0, strWorkingHour.indexOf("hrs"));
        int intHour=Integer.parseInt(strHour.replace(" ",""));

        String strMin=strWorkingHour.substring(strWorkingHour.indexOf("hrs")+3);
        strMin=strMin.replace("mins","");
        int intMin= Integer.parseInt(strMin.replace(" ",""));

        DecimalFormat df = new DecimalFormat("00");
        String strTotalHr = df.format(intHour);
        String strTotalMin = df.format(intMin);

        strWorkingHour=strTotalHr+":"+strTotalMin;

        strTotalHours = strWorkingHour.replace(" ", "");

        return strTotalHours;
    }

    /**************************************** WEB API CALLS ENDS **********************************/

    /**
     * Get today date
     */
    private Date getTodayDate() {
        return Calendar.getInstance().getTime();
    }

    /***
     * *  hide future month calender
     * @param endDate
     */
    private boolean hideNextMonth(String endDate) {
        boolean isNextBtnVisible=false;
        SimpleDateFormat curFormater = new SimpleDateFormat("MM/dd/yyyy");
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(endDate);
//                    Wed Jan 03 00:00:00 IST 2018
            if (dateObj.after(getTodayDate())) {
                ivNexMonth.setEnabled(false);
                isNextBtnVisible=false;
            } else {
                ivNexMonth.setEnabled(true);
                isNextBtnVisible=true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isNextBtnVisible;
    }
}
