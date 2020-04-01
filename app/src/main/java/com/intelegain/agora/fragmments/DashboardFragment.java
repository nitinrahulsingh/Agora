package com.intelegain.agora.fragmments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.intelegain.agora.R;
import com.intelegain.agora.activity.NewHolidayActivity;
import com.intelegain.agora.adapter.CipSessionPagerAdapter;
import com.intelegain.agora.adapter.MonthAdapter;
import com.intelegain.agora.adapter.NewsPagerAdapter;
import com.intelegain.agora.adapter.OccassionPagerAdapter;
import com.intelegain.agora.adapter.SlidingViewPagerAdapter;
import com.intelegain.agora.api.urls.CommonMethods;
import com.intelegain.agora.constants.Constants;
import com.intelegain.agora.dataFetch.RetrofitClient;
import com.intelegain.agora.interfeces.WebApiInterface;
import com.intelegain.agora.model.AttendanceData;
import com.intelegain.agora.model.AttendanceMaster;
import com.intelegain.agora.model.CIPSession;
import com.intelegain.agora.model.DashboardMaster;
import com.intelegain.agora.model.DocumentData;
import com.intelegain.agora.model.News;
import com.intelegain.agora.model.OccassionsData;
import com.intelegain.agora.utils.Contants2;
import com.intelegain.agora.utils.FileDownloader;
import com.intelegain.agora.utils.Sharedprefrences;
import com.rd.PageIndicatorView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class DashboardFragment extends Fragment implements View.OnClickListener, FileDownloader.onDownloadTaskFinish {
    private String TAG = getClass().getSimpleName();
    private OccassionPagerAdapter objOccAdapter;
    private NewsPagerAdapter objNewsAdapter;
    private CipSessionPagerAdapter objCipSessionPagerAdapter;
    private Calendar calendar;
    private ImageView imgBack, imgFront, imgResizeCal;
    private AppBarLayout app_bar;
    private TextView txtmonthLbl, tvOccassions, tvnNews, tvCipSession;
    private RelativeLayout section_holidays, section_hr_manual, section_mediclaim_policy,
            section_anti_sexual_policy;
    private ViewPager viewPagerEvents;
    private PageIndicatorView pageIndicatorView;
    private boolean bIsCalendarCollapsed = false;
    private Activity activity;
    private ViewPager viewPager;
    private SlidingViewPagerAdapter viewPagerAdapter;
    private int miViewPagerCalendarheight;
    ArrayList<AttendanceData> arrayListDashboardData;

    private final int MI_NO_OF_MONTHS_TO_GENERATE = 24;
    NestedScrollView nestedScrollView;

    String startDate = "", endDate = "", currentServerDate = "";
    //value for this varaible will be set from server recieved date
    int year;
    int month;

    private boolean bIsScrollByCollapse = false;
    /**
     * To store the occasion data list of occasion adapter
     */
    private ArrayList<OccassionsData> mOccassionsDataList = new ArrayList<>();
    private ArrayList<AttendanceData> mobjDashboardData;
    private ArrayList<News> mNewsDataList = new ArrayList<>();
    private ArrayList<CIPSession> mCipSessionDataList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Retrofit retrofit;
    private int iCurrentMonth;
    private int iCurrentYear;
    private boolean bHasCalledBySwipe = true;
    private int miLastVisibleItem;
    private Contants2 contants2;
    ArrayList<String> stringArrayList;
    Context context;
    RelativeLayout rr_calendar;

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);    // to retain the state of fragment when activity recreates
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        activity = getActivity();
        InitializeWidget(view);
        setEventClickListener();
        setCalendarInstance();
        initialize();
        // Call the web api to get the dashboard data from server
        //setMonthDataInRecyclerView();
        setOccassionViewPagerData(getDummyDataForOccasion());
        setImageIndicator();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (CommonMethods.checkInternetConnection(activity)) {
            getDashboardData();
        } else {
            imgFront.setVisibility(View.GONE);
            imgBack.setVisibility(View.GONE);
            Contants2.showToastMessage(activity, getString(R.string.no_internet), true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SlidingViewPagerAdapter.bIsFirstTime = true;
    }

    private void initialize() {
        retrofit = RetrofitClient.getInstance(Constants.BASE_URL);
        contants2 = new Contants2();
    }

    /**
     * Set the event click listener for views
     */
    private void setEventClickListener() {
        imgFront.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgResizeCal.setOnClickListener(this);
        tvnNews.setOnClickListener(this);
        tvOccassions.setOnClickListener(this);
        tvCipSession.setOnClickListener(this);

        section_holidays.setOnClickListener(this);
        section_hr_manual.setOnClickListener(this);
        section_mediclaim_policy.setOnClickListener(this);
        section_anti_sexual_policy.setOnClickListener(this);

        app_bar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.COLLAPSED) {
                    Log.i(TAG, "State Collapsed");

                    bIsScrollByCollapse = true;

                } else if (state == State.EXPANDED) {
                    Log.i(TAG, "State Expanded");

                    bIsScrollByCollapse = false;
                } else if (state == State.IDLE) {
                    Log.i(TAG, "State Idle");
                }

            }
        });
    }

    /**
     * Initialize the widget
     *
     * @param view
     */
    private void InitializeWidget(View view) {
        //monthRecyView = (RecyclerView) view.findViewById(R.id.month_recyView);
        app_bar = view.findViewById(R.id.app_bar);
        imgBack = view.findViewById(R.id.img_back);
        imgFront = view.findViewById(R.id.img_front);
        imgResizeCal = view.findViewById(R.id.img_Resize_cal);
        section_holidays = view.findViewById(R.id.section_holidays);
        section_hr_manual = view.findViewById(R.id.section_hr_manual);
        section_mediclaim_policy = view.findViewById(R.id.section_mediclaim_policy);
        section_anti_sexual_policy = view.findViewById(R.id.section_anti_sexual_policy);

        txtmonthLbl = view.findViewById(R.id.text_month_lbl);
        tvOccassions = view.findViewById(R.id.tvOccassions);
        tvnNews = view.findViewById(R.id.tvnNews);
        tvCipSession = view.findViewById(R.id.tvCipSession);
        viewPagerEvents = view.findViewById(R.id.viewPagerEvents);
        pageIndicatorView = view.findViewById(R.id.imgCirIndicator);
        viewPager = view.findViewById(R.id.viewPager);
        nestedScrollView = view.findViewById(R.id.nested_scroll_parent);
        rr_calendar = view.findViewById(R.id.rr_cal);
        // pageIndicatorView.setFillColor(ContextCompat.getColor(activity, R.color.orange));
        pageIndicatorView.setSelectedColor(ContextCompat.getColor(activity, R.color.orange));
        // By default set the occasion to selected
        tvOccassions.setBackgroundResource(R.drawable.rounded_button_selected);
        tvOccassions.setTextColor(ContextCompat.getColor(activity, R.color.white));
    }

    /**
     * set the calendar instance in module level
     */
    private void setCalendarInstance() {
        calendar = Calendar.getInstance();
    }

    /**
     * @param year year value which is one less than current year recieved from server to prepare custome calendar
     */
    private void setUpCalendarViewPager(int year, ArrayList<AttendanceData> arrayListDashboardData) {
        viewPagerAdapter = new SlidingViewPagerAdapter(activity, stringArrayList, year);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(getCurrentMonthPosition());

        // first time Last item of view pager is selected
        // so below visibility are sets to views.
        imgFront.setVisibility(View.GONE);
        imgBack.setVisibility(View.VISIBLE);

        int viewPagerPosition = viewPager.getCurrentItem();
        miLastVisibleItem = viewPagerPosition;
        getItemFromPosition(viewPagerPosition);

        updateMonthLabel();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Log.d(TAG, "onPageScrolled: "+position);
            }

            @Override
            public void onPageSelected(int position) {
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
                            if (viewPager.getCurrentItem() == (viewPager.getAdapter().getCount() - 1)) {
                                imgFront.setVisibility(View.GONE);
                            } else {
                                imgBack.setVisibility(View.VISIBLE);
                            }
                            //Log.w("DATE_PARAM", "s:" + startDate + " e:" + endDate);
                            getAttendanceFromServer(startDate, endDate);
                        } else
                            Contants2.showToastMessage(activity, getString(R.string.no_internet), true);

                    } else {
                        Log.i(TAG, "onPageScrolled: Right Swipe");
                        if (CommonMethods.checkInternetConnection(activity)) {
                            //setPrevMonthYear();
                            setPreviousMonthDates(startDate);
                            /*if (viewPager.getCurrentItem() > 0) {
                                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                            }*/
                            if (viewPager.getCurrentItem() == 0) {
                                imgBack.setVisibility(View.GONE);
                            } else {
                                imgFront.setVisibility(View.VISIBLE);
                            }
                            //Log.w("DATE_PARAM", "s:" + startDate + " e:" + endDate);
                            getAttendanceFromServer(startDate, endDate);
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

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Tree observer to measure the miViewPagerCalendarheight of viewpager once view is created
        viewPager.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                viewPager.getViewTreeObserver().removeOnPreDrawListener(this);
                miViewPagerCalendarheight = viewPager.getHeight();
                // collapse the calendar on start of the app
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        collapse_calendar(false);
                        contants2.dismissProgressDialog();
                    }
                }, 2000);

                Log.i(TAG, "View pager height = " + miViewPagerCalendarheight);
                return false;
            }
        });
    }

    /**
     * Set Occassion ViewPager data
     */
    private void setOccassionViewPagerData(ArrayList<OccassionsData> objOccassionsData) {
        objOccAdapter = new OccassionPagerAdapter(activity, objOccassionsData);
        viewPagerEvents.setClipChildren(false);
        viewPagerEvents.setClipToPadding(false);
        viewPagerEvents.setPadding(80, 0, 80, 0);
        viewPagerEvents.setPageMargin(40);
        viewPagerEvents.setAdapter(objOccAdapter);
    }

    /**
     * Set News ViewPager data
     */
    private void setNewsViewPagerData(ArrayList<News> objNewsDataList) {
        objNewsAdapter = new NewsPagerAdapter(activity, objNewsDataList);
        viewPagerEvents.setClipChildren(false);
        viewPagerEvents.setClipToPadding(false);
        viewPagerEvents.setPadding(80, 0, 80, 0);
        viewPagerEvents.setPageMargin(40);
        viewPagerEvents.setAdapter(objNewsAdapter);
    }

    /**
     * Set Cip Session ViewPager data
     */
    private void setCipSessionViewPagerData(ArrayList<CIPSession> objCipSessionDataList) {
        objCipSessionPagerAdapter = new CipSessionPagerAdapter(activity, objCipSessionDataList);
        viewPagerEvents.setClipChildren(false);
        viewPagerEvents.setClipToPadding(false);
        viewPagerEvents.setPadding(80, 0, 80, 0);
        viewPagerEvents.setPageMargin(40);
        viewPagerEvents.setAdapter(objCipSessionPagerAdapter);
    }

    /**
     * Show image indicator below the view pager
     */


    private void setImageIndicator() {
        // Set View Pager Indicator properties
        final float density = getResources().getDisplayMetrics().density;
        pageIndicatorView.setRadius(5 * density);
        pageIndicatorView.setStrokeWidth(1 * density);
        pageIndicatorView.setViewPager(viewPagerEvents);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:

                imgFront.setEnabled(true);
                bHasCalledBySwipe = false;
                if (viewPager.getCurrentItem() == 0) {
                    imgBack.setVisibility(View.GONE);
                } else {
//                    imgBack.setVisibility(View.GONE);
                    imgFront.setVisibility(View.VISIBLE);
                }
                if (CommonMethods.checkInternetConnection(activity)) {
                    setPreviousMonthDates(startDate);

                    if (viewPager.getCurrentItem() > 0) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                        if (viewPager.getCurrentItem() == 0) {
                            imgBack.setVisibility(View.GONE);
                        }
                    }
                    imgFront.setVisibility(View.VISIBLE);
                    //Log.w("DATE_PARAM_B", "s:" + startDate + " e:" + endDate);
                    // Get the dashboard data from server
                    getAttendanceFromServer(startDate, endDate);
                } else {
                    contants2.dismissProgressDialog();
                    Contants2.showToastMessage(activity, getString(R.string.no_internet), true);
                }
                //updateMonthLabel();
                break;
            case R.id.img_front:
                //calendar.add(Calendar.MONTH, 1);
                //updateMonthLabel();


                bHasCalledBySwipe = false;


//                03/01/2018
//                Mon Mar 12 13:28:14 IST 2018

//

                if (hideNextMonth(endDate)) {

                    if (CommonMethods.checkInternetConnection(activity)) {
                        setNextMonthDates(startDate);
                        if (viewPager.getCurrentItem() < MI_NO_OF_MONTHS_TO_GENERATE) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                            if (viewPager.getCurrentItem() == (viewPager.getAdapter().getCount() - 1)) {
                                imgFront.setVisibility(View.GONE);
                            }
                        }
                        imgBack.setVisibility(View.VISIBLE);
                        //Log.w("DATE_PARAM_B", "s:" + startDate + " e:" + endDate);
                        // Get the dashboard data from server
                        getAttendanceFromServer(startDate, endDate);

                    } else {
                        contants2.dismissProgressDialog();
                        Contants2.showToastMessage(activity, getString(R.string.no_internet), true);
                    }
                } else {
                    //Toast.makeText(activity, "Can not see future date", Toast.LENGTH_SHORT).show();
                }

                //updateMonthLabel();
                break;
            case R.id.img_Resize_cal:

                //ResizeCalendar();

                if (bIsCalendarCollapsed) {
                    // app_bar.setExpanded(true);
                    expand_calendar();
                } else {
                    // app_bar.setExpanded(false);
                    if (miLastVisibleItem == getCurrentMonthPosition())
                        collapse_calendar(false);
                    else
                        collapse_calendar(true);
                }
                break;
            case R.id.tvOccassions:
                setOccassionViewPagerData(mOccassionsDataList);
                tvOccassions.setBackgroundResource(R.drawable.rounded_button_selected);
                tvnNews.setBackgroundResource(R.drawable.rounded_button);
                tvCipSession.setBackgroundResource(R.drawable.rounded_button);

                tvOccassions.setTextColor(ContextCompat.getColor(activity, R.color.white));
                tvnNews.setTextColor(ContextCompat.getColor(activity, R.color.color_viewpager_text_not_selected));
                tvCipSession.setTextColor(ContextCompat.getColor(activity, R.color.color_viewpager_text_not_selected));
                break;
            case R.id.tvnNews:
                setNewsViewPagerData(mNewsDataList);
                tvOccassions.setBackgroundResource(R.drawable.rounded_button);
                tvnNews.setBackgroundResource(R.drawable.rounded_button_selected);
                tvCipSession.setBackgroundResource(R.drawable.rounded_button);

                tvnNews.setTextColor(ContextCompat.getColor(activity, R.color.white));
                tvOccassions.setTextColor(ContextCompat.getColor(activity, R.color.color_viewpager_text_not_selected));
                tvCipSession.setTextColor(ContextCompat.getColor(activity, R.color.color_viewpager_text_not_selected));
                break;
            case R.id.tvCipSession:
                setCipSessionViewPagerData(mCipSessionDataList);
                tvOccassions.setBackgroundResource(R.drawable.rounded_button);
                tvnNews.setBackgroundResource(R.drawable.rounded_button);
                tvCipSession.setBackgroundResource(R.drawable.rounded_button_selected);

                tvCipSession.setTextColor(ContextCompat.getColor(activity, R.color.white));
                tvnNews.setTextColor(ContextCompat.getColor(activity, R.color.color_viewpager_text_not_selected));
                tvOccassions.setTextColor(ContextCompat.getColor(activity, R.color.color_viewpager_text_not_selected));
                break;
            case R.id.section_holidays:
                startActivity(new Intent(activity, NewHolidayActivity.class));
                break;
            case R.id.section_hr_manual:
                if (!contants2.pdfFileExists(getActivity(), Contants2.hr_manual_file_name))
                    getPdf(Contants2.HRManualContentCode);
                else
                    try {
                        contants2.viewPdfFile(getActivity(), Contants2.hr_manual_file_name);
                    } catch (ActivityNotFoundException e) {
                        Contants2.showToastMessage(getActivity(), getString(R.string.no_application_found), false);
                    }
                break;
            case R.id.section_mediclaim_policy:
                if (!contants2.pdfFileExists(getActivity(), Contants2.mediclaim_policy_file_name))
                    getPdf(Contants2.MediclaimPolicyContentCode);
                else {
                    try {
                        contants2.viewPdfFile(getActivity(), Contants2.mediclaim_policy_file_name);
                    } catch (ActivityNotFoundException e) {
                        Contants2.showToastMessage(getActivity(), getString(R.string.no_application_found), false);
                    }
                }
                break;
            case R.id.section_anti_sexual_policy:
                if (!contants2.pdfFileExists(getActivity(), Contants2.ASH_policy_file_name))
                    getPdf(Contants2.AntiSexualPolicyContentCode);
                else
                    try {
                        contants2.viewPdfFile(getActivity(), Contants2.ASH_policy_file_name);
                    } catch (ActivityNotFoundException e) {
                        Contants2.showToastMessage(getActivity(), getString(R.string.no_application_found), false);
                    }
                break;
            default:
                break;
        }
    }


    private int iCurrentDtPos = -1;

    private void scrollToCurrentDate(boolean callScrollToWithWebApi) {
        bHasCalledBySwipe = false;
        viewPager.setCurrentItem(getCurrentMonthPosition());
        int iCurrentSelectedPos = viewPager.getCurrentItem();//getCurrentMonthPosition();
        if (iCurrentSelectedPos == getCurrentMonthPosition()) {
            View view = viewPager.findViewWithTag(iCurrentSelectedPos);
            if (view != null) {
                RecyclerView recyclerView = view.findViewById(R.id.month_recyView);
                if (recyclerView != null)
                    if (recyclerView.getAdapter() != null) {
                        MonthAdapter monthAdapter = (MonthAdapter) recyclerView.getAdapter();
                        if (iCurrentDtPos == -1)
                            iCurrentDtPos = monthAdapter.getDateScrollPos();
                        if (monthAdapter.getItemCount() > iCurrentDtPos) {
                            recyclerView.scrollToPosition(iCurrentDtPos);


                            viewPagerAdapter.monthAdapterArrayList.get(viewPager.getCurrentItem()).isCalendarCollapsed = true;
                            if (callScrollToWithWebApi) {
                                currentServerDate = getCurrentMonthDate(year + 1);
                                if (!startDate.split("/")[0].equals(currentServerDate.split("/")[1])) {
                                    setStartDateEndDate(currentServerDate);
                                    getAttendanceFromServer(startDate, endDate);
                                }
                            }

                        }

                    }
            }
        }

    }

    /**
     * collpase calendar
     */
    private void collapse_calendar(boolean callScrollToWithApi) {
        // To set recyclerView miViewPagerCalendarheight parameter
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
    private void expand_calendar() {
        /*// To set recyclerView miViewPagerCalendarheight parameter
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        // Expand calendar

        params.height = miViewPagerCalendarheight;
        viewPager.setLayoutParams(params);
        // rotate arrow image to 180 degree i.e to normal mode
        imgResizeCal.setRotation(180);
        //slide_down(activity,viewPager);

        *//************************************START*****************************************//*
         *//******* Added later for rebinding the data after coming to state expand **********//*
        int iCurrentSelectedPos = viewPager.getCurrentItem();//getCurrentMonthPosition();
        View view = viewPager.findViewWithTag(iCurrentSelectedPos);
        RecyclerView recyclerView = view.findViewById(R.id.month_recyView);
        recyclerView.scrollToPosition(0);   // scroll from 0th position
        //viewPagerAdapter.monthAdapter.setData(mobjDashboardData);
        viewPagerAdapter.monthAdapterArrayList.get(viewPager.getCurrentItem()).setData(arrayListDashboardData);
        *//*************************************END******************************************//*
        updateMonthLabel();
        bIsCalendarCollapsed = false;*/
    }

    /**
     * Update the calendar data
     */
    private void updateMonthLabel() {
        txtmonthLbl.setText(getItemFromPosition(viewPager.getCurrentItem()));
    }

    private ArrayList<String> generateCalendarArrayList(int year, int month) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);

        for (int no = 0; no < MI_NO_OF_MONTHS_TO_GENERATE; no++) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
            stringArrayList.add(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.MONTH, -1);
        }
        Collections.reverse(stringArrayList);
        return stringArrayList;
    }


    private int getCurrentMonthPosition() {
//        ArrayList<String> stringArrayList = generateCalendarArrayList(year, month);

        Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.YEAR, year + 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        String currentMonth = dateFormat.format(calendar.getTime());

        for (int no = 0; no < stringArrayList.size(); no++) {
            if (currentMonth.equals(stringArrayList.get(no))) {
                return no;
            }
        }
        return 0;
    }


    /**
     * @param position position of view pager
     * @return MMMM yyyy is returned
     */
    private String getItemFromPosition(int position) {
//        ArrayList<String> stringArrayList = generateCalendarArrayList(year, month);
        String monthYear = stringArrayList.get(position);
        txtmonthLbl.setText(monthYear);
        return monthYear;
    }

    public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

        private State mCurrentState = State.IDLE;

        @Override
        public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            if (i == 0) {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED);
                }
                mCurrentState = State.EXPANDED;
            } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED);
                }
                mCurrentState = State.COLLAPSED;
            } else {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(appBarLayout, State.IDLE);
                }
                mCurrentState = State.IDLE;
            }
        }

        public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
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
     * Get current month
     */
    private String getCurrentMonthDate(int year) {
        calendar.set(Calendar.YEAR, year);
        SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        String existingDate = df1.format(calendar.getTime());
        String[] dateSplit = existingDate.split("/");
        existingDate = "01/" + dateSplit[1] + "/" + dateSplit[2];
        return existingDate;
    }

    /**
     * Get current Year
     */
    private int getCurrentYear() {
        Calendar objCal = getCalendar();

        return objCal.get(Calendar.YEAR);
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


    /******************************************** WEB API CALLS ***********************************/

    /**
     * get dashboard deta from server
     */
    private void getDashboardData() {
        if (Contants2.checkInternetConnection(getActivity())) {
            contants2.showProgressDialog(getActivity());
            WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
            Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(activity);
            String strToken = mSharedPrefs.getString("Token", "");
            String strEmpId = mSharedPrefs.getString("emp_Id", "");

            // Make list of parameter for sending the http request
            Map<String, String> params = new HashMap<String, String>();
            params.put("EmpId", strEmpId);
            params.put("Days", "7");
            params.put("LocationID", "0");
            params.put("Startdate", ""); // sending blank parameter to get current data and date from server.

            Call<DashboardMaster> call = apiInterface.getDashboardData(strEmpId, strToken, params);
            call.enqueue(new Callback<DashboardMaster>() {
                @Override
                public void onResponse(Call<DashboardMaster> call, Response<DashboardMaster> response) {
                    switch (response.code()) {
                        case 200:
                            DashboardMaster objDashboardMaster = response.body();

                            arrayListDashboardData = objDashboardMaster.getDashboardData();
                            ArrayList<OccassionsData> arrayListOccassionsData = objDashboardMaster.getOccassionsData();
                            ArrayList<News> arrayListNewsDataList = objDashboardMaster.getNews();
                            ArrayList<CIPSession> arrayListCipSessions = objDashboardMaster.getCIPSessions();

                            if (arrayListOccassionsData == null || arrayListOccassionsData.size() == 0) {
                                OccassionsData occassionsData = new OccassionsData();
                                occassionsData.setOccassionName("");
                                occassionsData.setOccassionDate("");
                                occassionsData.setOccassionFor("No Occasion Found");
                                arrayListOccassionsData.add(occassionsData);
                            }

                            if (arrayListNewsDataList == null || arrayListNewsDataList.size() == 0) {
                                News news = new News();
                                news.setNewsTitle("");
                                news.setNewsDate("No news available.");
                                arrayListNewsDataList.add(news);
                            }

                            if (arrayListCipSessions == null || arrayListCipSessions.size() == 0) {
                                CIPSession cipSession = new CIPSession();
                                cipSession.setCIPSessionsInfo("No CIP sessions available.");
                                arrayListCipSessions.add(cipSession);
                            }

                            mOccassionsDataList = arrayListOccassionsData;
                            mNewsDataList = arrayListNewsDataList;
                            mCipSessionDataList = arrayListCipSessions;
                            mobjDashboardData = arrayListDashboardData;
                            setOccassionViewPagerData(arrayListOccassionsData);
                            setImageIndicator();

                            // if start date and end date blank means freshly screen loaded,
                            // hence need to set the start and end date as per date recieved from server

                            if (startDate.equals("") && endDate.equals("")) {
                                // below bug found on local - http://52.172.192.203
                                // this if condition is for when current date is 01 of month, that time arrayListDashboardData.size() = 0
                                if (arrayListDashboardData.size() > 0) {
                                    currentServerDate = arrayListDashboardData.get(0).getAttendanceDate();
                                    setStartDateEndDate(currentServerDate);
                                    year = Integer.parseInt(currentServerDate.split("/")[2]);
                                    month = Integer.parseInt(currentServerDate.split("/")[1]) - 1;
                                    stringArrayList = generateCalendarArrayList(year, month);
                                    //create month with blank dates
                                    setUpCalendarViewPager(year, arrayListDashboardData);
                                } else {
                                    rr_calendar.setVisibility(View.GONE);
                                    Contants2.showToastMessage(context, "Dashboard data not available now, try later!", true);
                                    contants2.dismissProgressDialog();
                                }
                            }
                            /*  set Data of dates for current month by .setData() method */
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // set DashboardData if present
                                    if (arrayListDashboardData.size() > 1) {
                                        viewPagerAdapter.monthAdapterArrayList.get(viewPager.getCurrentItem()).setData(arrayListDashboardData);
                                    }
                                }
                            }, 2000);

                            break;
                        case 403:
                            contants2.dismissProgressDialog();
                            Contants2.showOkAlertDialog(getActivity(), response.message(), "", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Contants2.forceLogout(getActivity());
                                }
                            });
                            break;
                        case 500:
                            contants2.dismissProgressDialog();
                            Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                            break;
                        default:
                            contants2.dismissProgressDialog();
                            Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);

                    }
                    bHasCalledBySwipe = true;
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    call.cancel();
                    contants2.dismissProgressDialog();
                    bHasCalledBySwipe = true;
                    Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                }
            });
        } else {
            Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true);
        }
    }


    /**
     * Get Attendance data from server
     */
    private void getAttendanceFromServer(String attendanceStartDate, String attendanceEndDate) {
        Log.d("getAttendanceFromServer", "getAttendanceFromServer");
        // showProgressDialog();
        contants2.showProgressDialog(getActivity());

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
            @Override
            public void onResponse(Call<AttendanceMaster> call, Response<AttendanceMaster> response) {
                switch (response.code()) {
                    case 200:
                        updateMonthLabel();
                        AttendanceMaster objAttendanceMaster = response.body();
                        arrayListDashboardData = objAttendanceMaster.getAttendanceData();
                        if (arrayListDashboardData.size() > 0) {
                            for (AttendanceData attendanceData : arrayListDashboardData) {
                                if (attendanceData.getAttStatus().equalsIgnoreCase("A"))
                                    attendanceData.setDescription("Absent");
                            }
                            viewPagerAdapter.monthAdapterArrayList.get(viewPager.getCurrentItem()).setData(arrayListDashboardData);
                            // viewPagerAdapter.monthAdapterArrayList.get(viewPager.getCurrentItem()).notifyDataSetChanged();
                        }
                        contants2.dismissProgressDialog();
                        break;
                    case 403:
                        contants2.dismissProgressDialog();
                        Contants2.showOkAlertDialog(getActivity(), response.message(), "", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Contants2.forceLogout(getActivity());
                            }
                        });
                        break;
                    case 500:
                        contants2.dismissProgressDialog();
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                        //hideshowConstraintLayout(true);
                        break;
                    default:
                        contants2.dismissProgressDialog();
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);//hideshowConstraintLayout(true);
                }
                bHasCalledBySwipe = true;
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                bHasCalledBySwipe = true;
                call.cancel();
                contants2.dismissProgressDialog();
                Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                Toast.makeText(activity, "Some error occurred! please try again later..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAttendanceCalendar(ArrayList<AttendanceData> arrayListAttendanceData) {

    }

    /**
     * sets the local variable "startDate" & "endDate"
     *
     * @param currentDate String date which is the current date
     */
    private void setStartDateEndDate(String currentDate) {
        String[] arrStartEndDate;
        arrStartEndDate = contants2.setStartEndDates(currentDate).split("--");
        startDate = arrStartEndDate[0];
        endDate = arrStartEndDate[1];
    }

    /**
     * sets start and end date if prev month is shown
     * both dates set in format MM/dd/yyyy
     */
    private void setPreviousMonthDates(String currentDate) {
        String[] arrStartEndDate;
        arrStartEndDate = contants2.setPrevMonthStartEndDate(currentDate, txtmonthLbl).split("--");
        startDate = arrStartEndDate[0];
        endDate = arrStartEndDate[1];

    }

    /**
     * sets start and end date if next month is shown
     * * both dates set in format MM/dd/yyyy
     */
    private void setNextMonthDates(String currentDate) {
        String[] arrStartEndDate;
        arrStartEndDate = contants2.setNextMonthStartEndDate(currentDate, txtmonthLbl).split("--");
        startDate = arrStartEndDate[0];
        endDate = arrStartEndDate[1];
    }

    /**
     * Create a dummy data for occasion list
     */
    private ArrayList<OccassionsData> getDummyDataForOccasion() {
        OccassionsData objOccassionsData = new OccassionsData();
        objOccassionsData.setOccassionFor("No Occasion Found");
        objOccassionsData.setOccassionDate("");
        objOccassionsData.setOccassionName("");
        objOccassionsData.setOccasionImageUrl("http:/dummy.com/1.png");
        ArrayList<OccassionsData> objOccassionsDataArrayList = new ArrayList<>();
        objOccassionsDataArrayList.add(objOccassionsData);

        return objOccassionsDataArrayList;
    }

    /**
     * common web call to get all 3 pdf and store it in respective places
     *
     * @param pdfType String value specifying the type of pdf to be donwloaded
     */
    private void getPdf(final String pdfType) {
        if (Contants2.checkInternetConnection(activity)) {
            contants2.showProgressDialog(activity);
            WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
            Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(activity);
            String strToken = mSharedPrefs.getString("Token", "");
            String strEmpId = mSharedPrefs.getString("emp_Id", "");

            // Make list of parameter for sending the http request
            Map<String, String> params = new HashMap<>();
            params.put("EmpId", strEmpId);
            params.put("Code", pdfType);

            Call<DocumentData> call = apiInterface.getDocument(strEmpId, strToken, params);
            call.enqueue(new Callback<DocumentData>() {
                @Override
                public void onResponse(Call<DocumentData> call, Response<DocumentData> response) {

                    switch (response.code()) {
                        case 200:
                            FileDownloader fileDownloader = new FileDownloader(getActivity(), DashboardFragment.this);
                            String pdfFileName;
                            if (pdfType.equals(Contants2.HRManualContentCode)) {
                                pdfFileName = Contants2.hr_manual_file_name;
                            } else if (pdfType.equals(Contants2.MediclaimPolicyContentCode)) {
                                pdfFileName = Contants2.mediclaim_policy_file_name;
                            } else {
                                pdfFileName = Contants2.ASH_policy_file_name;
                            }
                            fileDownloader.execute(response.body().docData, pdfFileName);
                            break;
                        case 403:
                            contants2.dismissProgressDialog();
                            Contants2.showOkAlertDialog(getActivity(), response.message(), "", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Contants2.forceLogout(getActivity());
                                }
                            });
                            break;
                        case 500:
                            contants2.dismissProgressDialog();
                            Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                            break;
                    }


                }

                @Override
                public void onFailure(Call<DocumentData> call, Throwable t) {
                    contants2.dismissProgressDialog();
                    Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                }
            });
        } else {
            Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true);
        }
    }

    @Override
    public void onDownloadSuccess(String strResult, String fileName) {
        contants2.dismissProgressDialog();
        try {
            contants2.viewPdfFile(getActivity(), fileName);
        } catch (ActivityNotFoundException e) {
            Contants2.showToastMessage(getActivity(), getString(R.string.no_application_found), true);
        }


    }

    @Override
    public void onDownloadFailed(String strResult) {
        contants2.dismissProgressDialog();
        Contants2.showToastMessage(getActivity(), getString(R.string.downloadFailed), true);
    }

    public int getFirstDay(int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public ArrayList<String> generateYearArraylist() {
        ArrayList<String> yearArrayList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        yearArrayList.add("Year");
        do {
            yearArrayList.add(calendar.get(Calendar.YEAR) + "");
            calendar.add(Calendar.YEAR, -1);
        } while (calendar.get(Calendar.YEAR) > 1991);
        return yearArrayList;
    }

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
        boolean isNextBtnVisible = false;
        SimpleDateFormat curFormater = new SimpleDateFormat("MM/dd/yyyy");
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(endDate);
//                    Wed Jan 03 00:00:00 IST 2018
            if (dateObj.after(getTodayDate())) {
                imgFront.setEnabled(false);
                isNextBtnVisible = false;
            } else {
                imgFront.setEnabled(true);
                isNextBtnVisible = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isNextBtnVisible;
    }
}
