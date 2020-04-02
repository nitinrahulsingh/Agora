package com.intelegain.agora.fragmments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.intelegain.agora.R;
import com.intelegain.agora.activity.NewApplyLeaveActivity;
import com.intelegain.agora.adapter.NewLeavesAdapter;
import com.intelegain.agora.constants.Constants;
import com.intelegain.agora.dataFetch.RetrofitClient;
import com.intelegain.agora.interfeces.OnItemLongClickListener;
import com.intelegain.agora.interfeces.RecyclerItemClickListener;
import com.intelegain.agora.interfeces.WebApiInterface;
import com.intelegain.agora.model.AppliedLeaveDetails;
import com.intelegain.agora.model.AvailableLeave;
import com.intelegain.agora.model.CommonStatusMessage;
import com.intelegain.agora.model.Leave;
import com.intelegain.agora.model.LeaveData;
import com.intelegain.agora.model.LeaveMaster;
import com.intelegain.agora.utils.Contants2;
import com.intelegain.agora.utils.Sharedprefrences;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;


public class LeavesFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, OnItemLongClickListener,
        SwipeRefreshLayout.OnRefreshListener {
    private String TAG = getClass().getSimpleName();
    private Activity activity;
    private AppBarLayout app_bar;

    public static final int applyLeave = 1;
    // Widget declaration
    private TextView tvTotalCL, tvTotalCLTillDate, tvConsumedCL, tvBalanceCL, tvTotalSL, tvTotalSLTillDate,
            tvConsumedSL, tvBalanceSL, tvTotalPL, tvTotalPLTillDate, tvConsumedPL, tvCurrentYearSlap,
            tvBalancePL, tvTotalCO, tvTotalCOTillDate, tvConsumedCO, tvBalanceCO, tvLeaveFromDate, tvLeaveToDate;

    private TextView tv_try_again;

    private ImageView ivLeaveFilter/*, iv_Resize_table*/, ivPrevYear, ivNextYear;
    private FloatingActionButton fabApplyForLeave;
    private TableLayout table_container;
    private RecyclerView mRecyViewForLeaves;
    private NewLeavesAdapter mobjLeavesAdapter;
    private ArrayList<LeaveData> mlstLeaveData;
    private ArrayList<AppliedLeaveDetails.LeaveData> mlstAppliedLeaveDetails;
    private ArrayList<LeaveData> mLeaveData;
    private int miTableContainerheight;
    private boolean bIsTableContainerCollapsed = false;


    private ArrayList<String> mlstLeaveType = new ArrayList<String>();
    private boolean bIsStartDateClicked = false;
    private LinearLayoutManager linearLayoutManager;
    private ProgressDialog progressDialog;
    private Retrofit retrofit;
    // NOTE: first element deliberately set to blank to match the size with spinner
    private String[] leaveType = {""};
    private Dialog dialog;
    private View dialog_view;
    private LayoutInflater inflater;
    private RecyclerView dialogRecyclerView;
    private ArrayList<String> mlstLeaveFilterList = new ArrayList<String>();
    private SwipeRefreshLayout swipeRefreshLayout;
    Contants2 contants2;
    private int fiscalYear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);    // to retain the state of fragment when activity recreates
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaves, container, false);
        activity = getActivity();

        InitializeWidget(view);
        initialize();
        setEventClickListener();

        return view;
    }

    private void initialize() {
        contants2 = new Contants2();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tree observer to measure the miViewPagerCalendarheight of viewpager once view is created
        table_container.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                table_container.getViewTreeObserver().removeOnPreDrawListener(this);
                miTableContainerheight = table_container.getHeight();
                Log.i(TAG, "View pager height = " + miTableContainerheight);
                return false;
            }
        });

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


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onRefresh() {
        if (CommonMethods.checkInternetConnection(activity))
            getAllLeaveDetails(fiscalYear + "");
        else {
            swipeRefreshLayout.setRefreshing(false);
            Contants2.showToastMessage(activity, getString(R.string.no_internet), false);
        }

    }

    private void InitializeWidget(View view) {
        tvTotalCL = view.findViewById(R.id.tvTotalCL);
        tvTotalCLTillDate = view.findViewById(R.id.tvTotalCLTillDate);
        tvConsumedCL = view.findViewById(R.id.tvConsumedCL);
        tvBalanceCL = view.findViewById(R.id.tvBalanceCL);
        tvTotalSL = view.findViewById(R.id.tvTotalSL);
        tvTotalSLTillDate = view.findViewById(R.id.tvTotalSLTillDate);
        tvConsumedSL = view.findViewById(R.id.tvConsumedSL);
        tvBalanceSL = view.findViewById(R.id.tvBalanceSL);
        tvTotalPL = view.findViewById(R.id.tvTotalPL);
        tvTotalPLTillDate = view.findViewById(R.id.tvTotalPLTillDate);
        tvConsumedPL = view.findViewById(R.id.tvConsumedPL);
        tvBalancePL = view.findViewById(R.id.tvBalancePL);
        tvTotalCO = view.findViewById(R.id.tvTotalCO);
        tvTotalCOTillDate = view.findViewById(R.id.tvTotalCOTillDate);
        tvConsumedCO = view.findViewById(R.id.tvConsumedCO);
        tvBalanceCO = view.findViewById(R.id.tvBalanceCO);
        tvCurrentYearSlap = view.findViewById(R.id.tvCurrentYearSlap);
        app_bar = view.findViewById(R.id.app_bar);
        table_container = view.findViewById(R.id.table_container);
        mRecyViewForLeaves = view.findViewById(R.id.recyclerViewForLeaves);
        ivLeaveFilter = view.findViewById(R.id.ivLeaveFilter);
        // iv_Resize_table = view.findViewById(R.id.iv_Resize_table);
        ivPrevYear = view.findViewById(R.id.ivPrevYear);
        ivNextYear = view.findViewById(R.id.ivNextYear);
        fabApplyForLeave = view.findViewById(R.id.fabApply);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        tv_try_again = view.findViewById(R.id.text_view_try_again);

        inflater = getActivity().getLayoutInflater();
        dialog = new Dialog(getActivity(), R.style.MyAlertDialogStyle);
    }

    /**
     * Set the event click listener for views
     */
    private void setEventClickListener() {
        //iv_Resize_table.setOnClickListener(this);
        ivLeaveFilter.setOnClickListener(this);
        ivPrevYear.setOnClickListener(this);
        ivNextYear.setOnClickListener(this);
        tv_try_again.setOnClickListener(this);
        fabApplyForLeave.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);

        app_bar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, DashboardFragment.State state) {
                if (state == DashboardFragment.State.COLLAPSED) {
                    Log.i(TAG, "State Collapsed");
                    // iv_Resize_table.setRotation(0);
                    bIsTableContainerCollapsed = true;
                } else if (state == DashboardFragment.State.EXPANDED) {
                    Log.i(TAG, "State Expanded");

                    // iv_Resize_table.setRotation(180);
                    bIsTableContainerCollapsed = false;

                } else if (state == DashboardFragment.State.IDLE) {
                    Log.i(TAG, "State Idle");
                } else {
                    // do nothing
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeaveFilter:
                showLeaveTypeFilterDialog();
                break;
            case R.id.ivPrevYear:
                if (Contants2.checkInternetConnection(activity)) {
                    fiscalYear = fiscalYear - 1;
                    getAllLeaveDetails(fiscalYear + "");
                } else {
                    Contants2.showToastMessage(activity, getString(R.string.no_internet), false);
                }
                break;
            case R.id.ivNextYear:
                if (Contants2.checkInternetConnection(activity)) {
                    fiscalYear = fiscalYear + 1;
                    getAllLeaveDetails(fiscalYear + "");
                } else {
                    Contants2.showToastMessage(activity, getString(R.string.no_internet), false);
                }
                break;
            case R.id.text_view_try_again:
                if (CommonMethods.checkInternetConnection(activity))
                    getAllLeaveDetails(fiscalYear + "");
                else {
                    swipeRefreshLayout.setRefreshing(false);
                    Contants2.showToastMessage(activity, getString(R.string.no_internet), false);
                }
                break;
            case R.id.fabApply:
                Intent intent = new Intent(getActivity(), NewApplyLeaveActivity.class);
                intent.putExtra("balanceCl", tvBalanceCL.getText().toString().trim());
                intent.putExtra("balanceSl", tvBalanceSL.getText().toString().trim());
                intent.putExtra("balancePl", tvBalancePL.getText().toString().trim());
                intent.putExtra("balanceCo", tvBalanceCO.getText().toString().trim());

                startActivityForResult(intent, applyLeave);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String formattedDate = (dayOfMonth <= 9 ? "0" + dayOfMonth : dayOfMonth)
                + "/" + ((month + 1) <= 9 ? "0" + (month + 1) : (month + 1))
                + "/" + year;
        if (bIsStartDateClicked) {
            tvLeaveFromDate.setText(formattedDate);
        } else {
            tvLeaveToDate.setText(formattedDate);
        }
    }

    /**
     * @param position long clicked item position in recycler view
     * @return boolean value
     */
    @Override
    public boolean onItemLongClicked(final int position, final int leaveId) {
        Contants2.showAlertDialog(getActivity(), getString(R.string.delete_leave), "Delete Leave", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteLeave(position, leaveId);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mobjLeavesAdapter.notifyDataSetChanged();
                dialogInterface.dismiss();
            }
        }, false);

        return false;
    }


    /**
     * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method
     * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view
     * background will be visible. That is rarely an desired effect.
     */
    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background, xMark;

            int xMarkMargin;
            boolean initiated;
            // LinearLayout.LayoutParams layoutParams;

            private void init() {
                background = new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.yellow));

                xMark = ContextCompat.getDrawable(getActivity(), R.drawable.delete);
                //xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) getActivity().getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //int position = viewHolder.getAdapterPosition();

                /*if (isSelected)
                    return 0;*/
              /*  TestAdapter testAdapter = (TestAdapter)recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }*/
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                ///set here view swiped

                int swipedPosition = viewHolder.getAdapterPosition();

                if (mobjLeavesAdapter.filteredList.get(swipedPosition).getLeaveStatus().equalsIgnoreCase("Pending"))
                    mobjLeavesAdapter.deleteRow(swipedPosition);
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

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away


                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!mobjLeavesAdapter.filteredList.get(viewHolder.getAdapterPosition()).getLeaveStatus().equalsIgnoreCase("Pending"))
                    return;

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyViewForLeaves);

    }

    public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

        private DashboardFragment.State mCurrentState = DashboardFragment.State.IDLE;

        @Override
        public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            if (i == 0) {
                if (mCurrentState != DashboardFragment.State.EXPANDED) {
                    onStateChanged(appBarLayout, DashboardFragment.State.EXPANDED);
                }
                mCurrentState = DashboardFragment.State.EXPANDED;
            } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
                if (mCurrentState != DashboardFragment.State.COLLAPSED) {
                    onStateChanged(appBarLayout, DashboardFragment.State.COLLAPSED);
                }
                mCurrentState = DashboardFragment.State.COLLAPSED;
            } else {
                if (mCurrentState != DashboardFragment.State.IDLE) {
                    onStateChanged(appBarLayout, DashboardFragment.State.IDLE);
                }
                mCurrentState = DashboardFragment.State.IDLE;
            }
        }

        public abstract void onStateChanged(AppBarLayout appBarLayout, DashboardFragment.State state);
    }

    /**
     * Show progresss dialog
     */
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please wait ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        if (!progressDialog.isShowing())
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
     * Apply leave to server
     */
    private void deleteLeave(final int position, int leaveId) {
        contants2.showProgressDialog(getActivity());
        WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
        Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(getActivity());

        String strToken = mSharedPrefs.getString("Token", "");
        String strEmpId = mSharedPrefs.getString("emp_Id", "");

        // make list of parameter for sending the http request
        Map<String, String> params = new HashMap<String, String>();
        // params.put("EmpId", strEmpId);
        params.put("EmpLeaveId", leaveId + "");


        Call<CommonStatusMessage> call = apiInterface.deleteLeave(strEmpId, strToken, params);
        call.enqueue(new Callback<CommonStatusMessage>() {
            @Override
            public void onResponse(Call<CommonStatusMessage> call, Response<CommonStatusMessage> response) {
                switch (response.code()) {
                    case 200:
                        CommonStatusMessage commonStatusMessage = response.body();

                        Contants2.showToastMessage(getActivity(), commonStatusMessage.message, true);

                        contants2.dismissProgressDialog();
                        mobjLeavesAdapter.removeItem(position);


                        break;
                    case 403:
                        Contants2.showOkAlertDialog(getActivity(), response.message(), "", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Contants2.forceLogout(getActivity());
                            }
                        });
                        contants2.dismissProgressDialog();
                        break;
                    case 500:
                        // contants2.dismissProgressDialog();
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                        contants2.dismissProgressDialog();
                        break;
                    default:
                        contants2.dismissProgressDialog();
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);

                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.cancel();
                contants2.dismissProgressDialog();
                Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), false);
            }
        });
    }

    /**
     * get leave details (i.e no of leaves allowed, balance etc ) from server
     */
    private void getAllLeaveDetails(String year) {
        contants2.showProgressDialog(getActivity());
        WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);

        Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(activity);
        String strToken = mSharedPrefs.getString("Token", "");
        String strEmpId = mSharedPrefs.getString("emp_Id", "");

        // make list of parameter for sending the http request
        Map<String, String> params = new HashMap<String, String>();
        params.put("EmpId", strEmpId);
        params.put("LeaveType", "0");
        params.put("LeaveStatus", "0");
        params.put("Year", year);

        Call<LeaveMaster> call = apiInterface.getAllLeaveDetails(strEmpId, strToken, params);
        call.enqueue(new Callback<LeaveMaster>() {
            @Override
            public void onResponse(Call<LeaveMaster> call, Response<LeaveMaster> response) {
                switch (response.code()) {
                    case 200:
                        hideRecycler(false, "getAppliedLeaveDetails");
                        LeaveMaster leaveMaster = response.body();
                        fiscalYear = leaveMaster.fiscalYear;
                        String fullFiscalYear = "Apr - " + (fiscalYear) + " To Mar - " + (fiscalYear + 1);
                        tvCurrentYearSlap.setText(fullFiscalYear);

                        //Leave objAvailableLeave = leaveMaster.getLeaveType();// here we're retrieving 0th object because it only contain 1 json object
                        fillAvailableLeavesSection(leaveMaster.getLeaveType());

                        mLeaveData = leaveMaster.getLeaveData();
                        int iSizeOfAppliedLeave = mLeaveData.size();
                        if (iSizeOfAppliedLeave <= 0) {
                            Contants2.showToastMessage(getActivity(), getString(R.string.no_data_found), true);
                            hideRecycler(true, "getAppliedLeaveDetails");
                        } else {
                            for (int i = 0; i < iSizeOfAppliedLeave; i++) {
                                LeaveData objAppliedLeaveDetails = mLeaveData.get(i);
                                String strLeaveFrom = convertDate(objAppliedLeaveDetails.getLeaveFrom());
                                objAppliedLeaveDetails.setLeaveFrom(strLeaveFrom);

                                String strLeaveTo = convertDate(objAppliedLeaveDetails.getLeaveTo());
                                objAppliedLeaveDetails.setLeaveTo(strLeaveTo);
                            }
                            FillAdapterData();
                        }
                        contants2.dismissProgressDialog();
                        break;
                    case 403:
                        Contants2.showOkAlertDialog(getActivity(), response.message(), "", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Contants2.forceLogout(getActivity());
                            }
                        });
                        contants2.dismissProgressDialog();
                        break;
                    case 500:
                        hideRecycler(true, "getAppliedLeaveDetails");
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                        contants2.dismissProgressDialog();
                        break;
                    default:
                        hideRecycler(true, "getAppliedLeaveDetails");
                        contants2.dismissProgressDialog();
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);

                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.cancel();
                hideRecycler(true, "getAppliedLeaveDetails");
                contants2.dismissProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true);
            }
        });
    }


    public void fillAvailableLeavesSection(Leave objAvailableLeave) {
        int iTotalCL = objAvailableLeave.getTotalCL();
        int iConsumedCL = objAvailableLeave.getConsumedCL();
        int iBalanceCL = objAvailableLeave.getBalanceCL();
        int iTotalCLTillDate = objAvailableLeave.getTotalCLTillDate();
        tvTotalCL.setText(String.valueOf(iTotalCL));
        tvConsumedCL.setText(String.valueOf(iConsumedCL));
        tvBalanceCL.setText(String.valueOf(iBalanceCL));
        tvTotalCLTillDate.setText(String.valueOf(iTotalCLTillDate));
        // Sick leave section
        int iTotalSL = objAvailableLeave.getTotalSL();
        int iConsumedSL = objAvailableLeave.getConsumedSL();
        int iBalanceSL = objAvailableLeave.getBalanceSL();
        int iTotalSLTillDate = objAvailableLeave.getTotalSLTillDate();
        tvTotalSL.setText(String.valueOf(iTotalSL));
        tvConsumedSL.setText(String.valueOf(iConsumedSL));
        tvBalanceSL.setText(String.valueOf(iBalanceSL));
        tvTotalSLTillDate.setText(String.valueOf(iTotalSLTillDate));
        // Paid leave section
        int iTotalPL = objAvailableLeave.getTotalPL();
        int iConsumedPL = objAvailableLeave.getConsumedPL();
        int iBalancePL = objAvailableLeave.getBalancePL();
        int iTotalPLTillDate = objAvailableLeave.getTotalPLTillDate();
        tvTotalPL.setText(String.valueOf(iTotalPL));
        tvConsumedPL.setText(String.valueOf(iConsumedPL));
        tvBalancePL.setText(String.valueOf(iBalancePL));
        tvTotalPLTillDate.setText(String.valueOf(iTotalPLTillDate));
        // Comp off leave section
        int iTotalCO = objAvailableLeave.getTotalCO();
        int iConsumedCO = objAvailableLeave.getConsumedCO();
        int iBalanceCO = objAvailableLeave.getBalanceCO();
        int iTotalCOTillDate = objAvailableLeave.getTotalCOTillDate();
        tvTotalCO.setText(String.valueOf(iTotalCO));
        tvConsumedCO.setText(String.valueOf(iConsumedCO));
        tvBalanceCO.setText(String.valueOf(iBalanceCO));
        tvTotalCOTillDate.setText(String.valueOf(iTotalCOTillDate));
    }

    /**
     * @param hide       if true hides recycler view and shows try again button and vice versa of false
     * @param calledFrom used to determine which api to call in case of no network or failed web call
     */
    private void hideRecycler(boolean hide, String calledFrom) {
        tv_try_again.setTag(calledFrom);
        if (hide) {
            tv_try_again.setVisibility(View.VISIBLE);
            mRecyViewForLeaves.setVisibility(View.GONE);
        } else {
            mRecyViewForLeaves.setVisibility(View.VISIBLE);
            tv_try_again.setVisibility(View.GONE);
        }


    }

    /**
     * Fill adapter data with leaveData list
     */
    private void FillAdapterData() {
        mobjLeavesAdapter = new NewLeavesAdapter(activity, mLeaveData, this);
        mRecyViewForLeaves.setAdapter(mobjLeavesAdapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyViewForLeaves.setLayoutManager(linearLayoutManager);
        setUpItemTouchHelper();
    }

    private String convertDate(String strDateToConvert) {
        String strOutputDate = "";
        DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date objDate = inputFormat.parse(strDateToConvert);
            strOutputDate = outputFormat.format(objDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strOutputDate;
    }

    /**
     * Prepare Leave filter list
     */
    private void PrepareLeaveFilterList() {
        if (mlstLeaveFilterList.size() == 0) {
            mlstLeaveFilterList.add("See All");
            mlstLeaveFilterList.add("Pending");
            mlstLeaveFilterList.add("Approved");
            mlstLeaveFilterList.add("Rejected");
        }
    }

    /**
     * Show Leave Type Filter list dialog
     */
    private void showLeaveTypeFilterDialog() {
        new CommonMethods().customSpinner(activity, "Select Leave Status", inflater, dialogRecyclerView,
                mlstLeaveFilterList, dialog, dialog_view,
                new RecyclerItemClickListener() {
                    @Override
                    public void recyclerViewListClicked(int position, String itemClickText) {
                        if (mobjLeavesAdapter != null) {
                            if (itemClickText.equalsIgnoreCase("See All"))
                                mobjLeavesAdapter.filter("");
                            else
                                mobjLeavesAdapter.filter(itemClickText);
                        } else {
                            Contants2.showToastMessage(getActivity(), getString(R.string.no_data_found), true);
                        }
                        dialog.hide();
                    }
                });
    }

    /**************************************** WEB API CALLS ENDS **********************************/

    /**
     * Get available leave from server
     */
    private void getAvailableLeaves() {
        //showProgressDialog();
        contants2.showProgressDialog(activity);
        swipeRefreshLayout.setRefreshing(true);
        WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
        Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(activity);
        String strToken = mSharedPrefs.getString("Token", "");
        String strEmpId = mSharedPrefs.getString("emp_Id", "");

        // make list of parameter for sending the http request
        Map<String, String> params = new HashMap<>();
        params.put("EmpId", strEmpId);

        Call<ArrayList<AvailableLeave>> call = apiInterface.getAvailableLeave(strEmpId, strToken, params);
        call.enqueue(new Callback<ArrayList<AvailableLeave>>() {
            @Override
            public void onResponse(Call<ArrayList<AvailableLeave>> call, Response<ArrayList<AvailableLeave>> response) {
                int iStatusCode = response.code();
                Log.i(TAG, "getAvailableLeaves onResponse: " + iStatusCode);

                switch (iStatusCode) {
                    case 200:
                        //contants2.dismissProgressDialog();

                        AvailableLeave objAvailableLeave = response.body().get(0);// here we're retrieving 0th object because it only contain 1 json object
                        // Casual leave section
                        int iTotalCL = objAvailableLeave.getTotalCL();
                        int iConsumedCL = objAvailableLeave.getConsumedCL();
                        int iBalanceCL = objAvailableLeave.getBalanceCL();
                        int iTotalCLTillDate = objAvailableLeave.getTotalCLTillDate();
                        tvTotalCL.setText(String.valueOf(iTotalCL));
                        tvConsumedCL.setText(String.valueOf(iConsumedCL));
                        tvBalanceCL.setText(String.valueOf(iBalanceCL));
                        tvTotalCLTillDate.setText(String.valueOf(iTotalCLTillDate));
                        // Sick leave section
                        int iTotalSL = objAvailableLeave.getTotalSL();
                        int iConsumedSL = objAvailableLeave.getConsumedSL();
                        int iBalanceSL = objAvailableLeave.getBalanceSL();
                        int iTotalSLTillDate = objAvailableLeave.getTotalSLTillDate();
                        tvTotalSL.setText(String.valueOf(iTotalSL));
                        tvConsumedSL.setText(String.valueOf(iConsumedSL));
                        tvBalanceSL.setText(String.valueOf(iBalanceSL));
                        tvTotalSLTillDate.setText(String.valueOf(iTotalSLTillDate));
                        // Paid leave section
                        int iTotalPL = objAvailableLeave.getTotalPL();
                        int iConsumedPL = objAvailableLeave.getConsumedPL();
                        int iBalancePL = objAvailableLeave.getBalancePL();
                        int iTotalPLTillDate = objAvailableLeave.getTotalPLTillDate();
                        tvTotalPL.setText(String.valueOf(iTotalPL));
                        tvConsumedPL.setText(String.valueOf(iConsumedPL));
                        tvBalancePL.setText(String.valueOf(iBalancePL));
                        tvTotalPLTillDate.setText(String.valueOf(iTotalPLTillDate));
                        // Comp off leave section
                        int iTotalCO = objAvailableLeave.getTotalCO();
                        int iConsumedCO = objAvailableLeave.getConsumedCO();
                        int iBalanceCO = objAvailableLeave.getBalanceCO();
                        int iTotalCOTillDate = objAvailableLeave.getTotalCOTillDate();
                        tvTotalCO.setText(String.valueOf(iTotalCO));
                        tvConsumedCO.setText(String.valueOf(iConsumedCO));
                        tvBalanceCO.setText(String.valueOf(iBalanceCO));
                        tvTotalCOTillDate.setText(String.valueOf(iTotalCOTillDate));

                        // dismissProgressDialog();//  dismiss progress dialog
                        // Now call Applied leave detail service
                        if (CommonMethods.checkInternetConnection(activity)) {
                            //getAppliedLeaveDetails("0");
                        } else {
                            contants2.dismissProgressDialog();
                            swipeRefreshLayout.setRefreshing(false);
                            hideRecycler(true, "getAppliedLeaveDetails");
                            Contants2.showToastMessage(activity, getString(R.string.no_internet), true);
                        }
                        break;
                    case 403:
                        contants2.dismissProgressDialog();
                        Contants2.showOkAlertDialog(activity, response.message(), "", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Contants2.forceLogout(activity);
                            }
                        });
                        break;
                    case 500:
                        contants2.dismissProgressDialog();
                        Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true);
                        hideRecycler(true, "getAvailableLeaves");
                        break;
                    default:
                        contants2.dismissProgressDialog();
                        Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true);
                        hideRecycler(true, "getAvailableLeaves");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AvailableLeave>> call, Throwable t) {
                Log.i(TAG, "onFailure: Error while executing getAvailableLeave request!");
                call.cancel();
                contants2.dismissProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                hideRecycler(true, "getAvailableLeaves");
                Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true);
            }
        });

    }


    /**
     * @param year value in yyyy to get leaves for the fiscal year
     */
    private void getAppliedLeaveDetails(String year) {// TODO: 8/11/17 : take a parameter year and pass the current year and on arrow click pass that respective year

        WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
        Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(activity);
        String strToken = mSharedPrefs.getString("Token", "");
        String strEmpId = mSharedPrefs.getString("emp_Id", "");

        // make list of parameter for sending the http request
        Map<String, String> params = new HashMap<>();
        params.put("EmpId", strEmpId);
        params.put("LeaveType", "0");
        params.put("LeaveStatus", "0");
        params.put("Year", year);

        Call<AppliedLeaveDetails> call = apiInterface.getAppliedLeaveDetails(strEmpId, strToken, params);
        call.enqueue(new Callback<AppliedLeaveDetails>() {
            @Override
            public void onResponse(Call<AppliedLeaveDetails> call, Response<AppliedLeaveDetails> response) {
                int iStatusCode = response.code();

                switch (iStatusCode) {
                    case 200:
                        hideRecycler(false, "getAppliedLeaveDetails");
                        AppliedLeaveDetails appliedLeaveDetails = response.body();

                        fiscalYear = response.body().fiscalYear;
                        String fullFiscalYear = "Apr - " + (fiscalYear) + " To Mar - " + (fiscalYear + 1);

                        tvCurrentYearSlap.setText(fullFiscalYear);

                        mlstAppliedLeaveDetails = appliedLeaveDetails.leaveData;
                        int iSizeOfAppliedLeave = mlstAppliedLeaveDetails.size();
                        if (iSizeOfAppliedLeave <= 0) {
                            Contants2.showToastMessage(getActivity(), getString(R.string.no_data_found), true);
                            hideRecycler(true, "getAppliedLeaveDetails");
                        } else {
                            for (int i = 0; i < iSizeOfAppliedLeave; i++) {
                                AppliedLeaveDetails.LeaveData objAppliedLeaveDetails = mlstAppliedLeaveDetails.get(i);
                                String strLeaveFrom = convertDate(objAppliedLeaveDetails.getLeaveFrom());
                                objAppliedLeaveDetails.setLeaveFrom(strLeaveFrom);

                                String strLeaveTo = convertDate(objAppliedLeaveDetails.getLeaveTo());
                                objAppliedLeaveDetails.setLeaveTo(strLeaveTo);
                            }
                            FillAdapterData();
                        }
                        contants2.dismissProgressDialog();
                        break;
                    case 403:
                        Contants2.showOkAlertDialog(getActivity(), response.message(), "", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Contants2.forceLogout(getActivity());
                            }
                        });
                        contants2.dismissProgressDialog();
                        break;
                    case 500:
                        hideRecycler(true, "getAppliedLeaveDetails");
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                        contants2.dismissProgressDialog();
                        break;
                    default:
                        hideRecycler(true, "getAppliedLeaveDetails");
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                        contants2.dismissProgressDialog();
                }

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<AppliedLeaveDetails> call, Throwable t) {
                Log.i(TAG, "onFailure: Error while executing getAppliedLeaveDetails request!");

                call.cancel();
                hideRecycler(true, "getAppliedLeaveDetails");
                contants2.dismissProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == applyLeave)
            if (resultCode == RESULT_OK) {
                getAllLeaveDetails(fiscalYear + "");
            }
    }
}