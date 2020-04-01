package com.intelegain.agora.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.intelegain.agora.R;
import com.intelegain.agora.adapter.HolidayAdapter;
import com.intelegain.agora.api.urls.CommonMethods;
import com.intelegain.agora.constants.Constants;
import com.intelegain.agora.dataFetch.RetrofitClient;
import com.intelegain.agora.interfeces.WebApiInterface;
import com.intelegain.agora.model.HolidayData;
import com.intelegain.agora.model.HolidayMaster;
import com.intelegain.agora.utils.Sharedprefrences;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewHolidayActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();
    private Toolbar toolbar;
    private TextView toolbar_title;
    private RecyclerView mRecyView_for_holidays;
    private HolidayAdapter mobjHolidayAdapter;
    private ArrayList<HolidayData> mlstHolidayList = new ArrayList<HolidayData>();
    private Retrofit retrofit;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_holiday);
        InitializeWidget();
        setToolbar();
        setEventClickListener();
        //setHolidayAdapter();
        retrofit = RetrofitClient.getInstance(Constants.BASE_URL);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(CommonMethods.checkInternetConnection(NewHolidayActivity.this)) {
                    getHolidayList();
                }
                else {
                    dismissProgressDialog();
                    Toast.makeText(NewHolidayActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        }, 500);

    }

    /** Get the holiday list from server */
    private void getHolidayList() {
        showProgressDialog();
        WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
        Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(this);
        String strToken = mSharedPrefs.getString("Token","WK6sDrLvypjT7c8GLKcXs8hZQGyw26TMCqSoCIU/ETJ7AA==");
        String strEmpId = mSharedPrefs.getString("emp_Id","1000");
        Call<HolidayMaster> call = apiInterface.getHolidayList(strEmpId,strToken);
        call.enqueue(new Callback<HolidayMaster>() {
            @Override
            public void onResponse(Call<HolidayMaster> call, Response<HolidayMaster> response) {
                Log.d("TAG",response.code()+"");

                int iStatusCode = response.code();
                if(iStatusCode == 200) {
                    HolidayMaster objHolidayMaster = response.body();
                    ArrayList<HolidayData> lstHoldiayList = objHolidayMaster.getHolidayList();
                    mlstHolidayList = lstHoldiayList;
                    setHolidayAdapter();
                    Log.i(TAG, "");
                } else {
                    Toast.makeText(NewHolidayActivity.this, "Some error occurred! please try again later..", Toast.LENGTH_SHORT).show();
                }
                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.cancel();
                dismissProgressDialog();
                Toast.makeText(NewHolidayActivity.this, "Some error occurred! please try again later..", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void getAttendanceData() {
//        WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
//        Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(this);
//        String strToken = mSharedPrefs.getString("Token","");
//        String strEmpId = mSharedPrefs.getString("emp_Id","1000");
//        // make list of parameter for sending the http request
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("EmpId", strEmpId);
//        params.put("Startdate", "09/01/2016");
//        params.put("EndDate", "09/30/2016");
//
//        Call<List<AttendanceData>> call = apiInterface.getAttendanceData(strEmpId,strToken,params);
//        call.enqueue(new Callback<List<AttendanceData>>() {
//            @Override
//            public void onResponse(Call<List<AttendanceData>> call, Response<List<AttendanceData>> response) {
//                Log.d("TAG",response.code()+"");
//
//                String displayResponse = "";
//                Log.i(TAG,"Response = "+displayResponse);
//
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//                call.cancel();
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void InitializeWidget() {
        toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        mRecyView_for_holidays = (RecyclerView) findViewById(R.id.recyclerview_for_holidays);
    }

    /**
     * Set the event click listener for views
     */
    private void setEventClickListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     *  Set toolbar properties
     */
    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Holidays");
        toolbar_title.setText("Holidays");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /** Set holiday adapter */
    private void setHolidayAdapter() {
        //mlstHolidayList = createDummyDataForLeaves();
        mobjHolidayAdapter = new HolidayAdapter(this, mlstHolidayList);
        mRecyView_for_holidays.setAdapter(mobjHolidayAdapter);
        mRecyView_for_holidays.setLayoutManager(new LinearLayoutManager(this));
    }

    /** NOT IN USED NOW, was only for testing purpose */
    private ArrayList<HolidayData> createDummyDataForLeaves() {
        ArrayList<HolidayData> mlstLeaveData = new ArrayList<>();
        HolidayData objHolidayData;
        for (int iCount = 0; iCount < 11; iCount++) {
            if (iCount == 0) {
                objHolidayData = new HolidayData("26", "JAN", "Republic Day", "http:dummy.com", iCount, "#f8efea");
            } else if (iCount == 1) {
                objHolidayData = new HolidayData("13", "MAR", "Holi", "http:dummy.com", iCount, "#deefff");
            } else if (iCount == 2) {
                objHolidayData = new HolidayData("14", "APR", "Good Friday", "http:dummy.com", iCount, "#e9f7f7");
            } else if (iCount == 3) {
                objHolidayData = new HolidayData("1", "MAY", "Maharashtra Day", "http:dummy.com", iCount, "#f4e9f7");
            } else if (iCount == 4) {
                objHolidayData = new HolidayData("25", "AUG", "Eid (Subject to change)", "http:dummy.com", iCount, "#edf4ec");
            } else if (iCount == 5) {
                objHolidayData = new HolidayData("15", "AUG", "Independence Day", "http:dummy.com", iCount, "#f0f0f0");
            } else if (iCount == 6) {
                objHolidayData = new HolidayData("14", "AUG", "Ganesh Chaturthi", "http:dummy.com", iCount, "#e9f7f7");
            } else if (iCount == 7) {
                objHolidayData = new HolidayData("1", "May", "Dessehra", "http:dummy.com", iCount, "#f4e9f7");
            } else if (iCount == 8) {
                objHolidayData = new HolidayData("2", "OCT", "Mahatma Gandhi Jayanti", "http:dummy.com", iCount, "#efefef");
            } else if (iCount == 9) {
                objHolidayData = new HolidayData("13", "NOV", "Diwali", "http:dummy.com", iCount, "#deefff");
            } else if (iCount == 10) {
                objHolidayData = new HolidayData("25", "DEC", "Christmas", "http:dummy.com", iCount, "#fde9eb");
            } else {
                objHolidayData = new HolidayData("31", "DEC", "New Holiday", "http:dummy.com", iCount, "#f8f8f8");
            }

            mlstLeaveData.add(objHolidayData);
        }
        return mlstLeaveData;
    }

    /** Show progresss dialog */
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    /** Dismiss the progress dialog */
    private void dismissProgressDialog() {
        if(progressDialog !=null)
            if(progressDialog.isShowing())
                progressDialog.dismiss();
    }
}
