package com.intelegain.agora.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.intelegain.agora.R;
import com.intelegain.agora.constants.Constants;
import com.intelegain.agora.dataFetch.RetrofitClient;
import com.intelegain.agora.interfeces.WebApiInterface;
import com.intelegain.agora.model.ApplyLeave;
import com.intelegain.agora.utils.Contants2;
import com.intelegain.agora.utils.Sharedprefrences;

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

public class NewApplyLeaveActivity extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener//, TextInputEditText.OnFocusChangeListener  {
{
    private String TAG = getClass().getSimpleName();
    private Spinner spinnerLeaveType;
    private Button btnApplyLeave;
    private ImageView ivClose;
    private TextView tvLeaveFromDate, tvLeaveToDate;
    //private TextInputEditText edLeaveFromDate,edLeaveToDate,edLeaveReason;
    private EditText edLeaveReason;
    private DatePickerDialog datePickerDialog;
    private ArrayList<String> mlstLeaveType = new ArrayList<String>();
    private ArrayList<String> mlstLeaveShortForm = new ArrayList<String>();

    private boolean bIsFirstTimeFocus = true;
    private boolean bIsStartDateClicked = false;
    private String[] leaveType;  //{"", "CL", "SL", "PL", "CO", "WL"};
    private Retrofit retrofit;
    private Contants2 contants2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_apply_leave);

        initializeObjects();
        InitializeWidget();
        setEventClickListener();
        FillLeaveTypeSpinner(getIntent());
    }

    private void initializeObjects() {
        retrofit = RetrofitClient.getInstance(Constants.BASE_URL);
        contants2 = new Contants2();
        mlstLeaveShortForm.add("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_in_down);
    }

    private void FillLeaveTypeSpinner(Intent intent) {
        //, "CL", "SL", "PL", "CO", "WL"};
        mlstLeaveType.add("Select");
        if (intent.hasExtra("balanceCl") && Integer.parseInt(intent.getStringExtra("balanceCl")) > 0) {
            mlstLeaveType.add("Casual Leave - CL");
            mlstLeaveShortForm.add("CL");
        }

        if (intent.hasExtra("balanceSl") && Integer.parseInt(intent.getStringExtra("balanceSl")) > 0) {
            mlstLeaveType.add("Sick Leave - SL");
            mlstLeaveShortForm.add("SL");
        }

        if (intent.hasExtra("balancePl") && Integer.parseInt(intent.getStringExtra("balancePl")) > 0) {
            mlstLeaveType.add("Paid Leave - PL");
            mlstLeaveShortForm.add("PL");
        }

        if (intent.hasExtra("balanceCo") && Integer.parseInt(intent.getStringExtra("balanceCo")) > 0) {
            mlstLeaveType.add("Comp Off - CO");
            mlstLeaveShortForm.add("CO");
        }

        mlstLeaveType.add("Leave (Without Pay)");
        mlstLeaveShortForm.add("WL");

        ArrayAdapter<String> leaveAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mlstLeaveType);
        spinnerLeaveType.setAdapter(leaveAdapter);
    }

    /**
     * Initialize all the widget
     */
    private void InitializeWidget() {
        ivClose = findViewById(R.id.img_icon_close);
        btnApplyLeave = findViewById(R.id.btnApplyLeave);
        spinnerLeaveType = findViewById(R.id.spinnerLeaveType);
        tvLeaveFromDate = findViewById(R.id.tvLeaveStartDate);
        tvLeaveToDate = findViewById(R.id.tvLeaveEndDate);
        edLeaveReason = findViewById(R.id.edLeaveReason);
    }

    /**
     * Set the event click listener for views
     */
    private void setEventClickListener() {
        btnApplyLeave.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        tvLeaveFromDate.setOnClickListener(this);
        tvLeaveToDate.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_icon_close:
                overridePendingTransition(R.anim.stay, R.anim.slide_in_down);
                onBackPressed();
                break;
            case R.id.btnApplyLeave:
                if (IsValidInputByUser()) {
                    //String strLeaveType = leaveType[spinnerLeaveType.getSelectedItemPosition()];
                    String strLeaveType = mlstLeaveShortForm.get(spinnerLeaveType.getSelectedItemPosition());

                    String strLeaveFrom = tvLeaveFromDate.getText().toString();
                    String strLeaveTo = tvLeaveToDate.getText().toString();
                    String strLeaveReason = edLeaveReason.getText().toString();
                    if (Contants2.checkInternetConnection(this))
                        // call web api to apply leave on server
                        applyLeave(strLeaveType, strLeaveFrom, strLeaveTo, strLeaveReason);
                    else
                        Contants2.showToastMessage(this, getString(R.string.no_internet), true);
                } else {
                    // Contants2.showToastMessage(this, getString(R.string.valid_details), false); kalpesh
                }
                break;
            case R.id.tvLeaveStartDate:
                bIsStartDateClicked = true;
                showDatePickerDialog();
                break;
            case R.id.tvLeaveEndDate:
                bIsStartDateClicked = false;
                showDatePickerDialog();
                break;
            default:
                break;
        }
    }

    /**
     * Is valid input by user
     */
    private boolean IsValidInputByUser() {
        if (spinnerLeaveType.getSelectedItemPosition() == 0 ||
                tvLeaveFromDate.getText().toString().equalsIgnoreCase("From Date") ||
                tvLeaveToDate.getText().toString().equalsIgnoreCase("To Date") ||
                TextUtils.isEmpty(edLeaveReason.getText().toString())) {
            Contants2.showToastMessage(this, getString(R.string.valid_details), false); //kalpesh
            return false;
        } else {
            if (validateDate(tvLeaveFromDate.getText().toString()).before(validateDate(tvLeaveToDate.getText().toString()))
                    || validateDate(tvLeaveFromDate.getText().toString()).equals(validateDate(tvLeaveToDate.getText().toString()))) {
                return true;
            } else {
                Toast.makeText(this, "From date should be less than or equal to To date", Toast.LENGTH_SHORT).show();
                return false;
            }
        }/*
        if (TextUtils.isEmpty(tvLeaveFromDate.getText().toString())) {
            return false;
        } else
            return !TextUtils.isEmpty(tvLeaveToDate.getText().toString());*/
    }

    /**
     * Show date picker dialog
     */
    private void showDatePickerDialog() {
        if (datePickerDialog == null || !datePickerDialog.isShowing()) {
            Calendar objCalendar = Calendar.getInstance();
            int startYear = objCalendar.get(Calendar.YEAR);
            int starthMonth = objCalendar.get(Calendar.MONTH);
            int startDay = objCalendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(
                    this, NewApplyLeaveActivity.this, startYear, starthMonth, startDay);
            datePickerDialog.show();
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

    private Date validateDate(String frmDate) {


        SimpleDateFormat curFormater = new SimpleDateFormat("MM/dd/yyyy");
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(frmDate);

        } catch (
                ParseException e) {
            e.printStackTrace();
        }
        return dateObj;

    }

    /**
     * Apply leave to server
     */
    private void applyLeave(String strLeaveType, String strLeaveFrom, String strLeaveTo, String
            strReason) {
        contants2.showProgressDialog(this);
        WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
        Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(this);
        String strToken = mSharedPrefs.getString("Token", "");
        String strEmpId = mSharedPrefs.getString("emp_Id", "");
        // make list of parameter for sending the http request
        Map<String, String> params = new HashMap<String, String>();
        params.put("EmpId", strEmpId);
        params.put("LeaveType", strLeaveType);
        params.put("LeaveFrom", strLeaveFrom);
        params.put("LeaveTo", strLeaveTo);
        params.put("Reason", strReason);

        Call<ApplyLeave> call = apiInterface.applyLeave(strEmpId, strToken, params);
        call.enqueue(new Callback<ApplyLeave>() {
            @Override
            public void onResponse(Call<ApplyLeave> call, Response<ApplyLeave> response) {
                switch (response.code()) {
                    case 200:
                        ApplyLeave objApplyLeave = response.body();
                        //String strStatus = objApplyLeave.getStatus();
                        String strMessage = objApplyLeave.getMessage();
                        Contants2.showToastMessage(NewApplyLeaveActivity.this, strMessage, true);

                        contants2.dismissProgressDialog();
                        overridePendingTransition(R.anim.stay, R.anim.slide_in_down);
                        setResult(RESULT_OK);
                        onBackPressed();
                        break;
                    case 403:
                        //contants2.dismissProgressDialog();
                        Contants2.showOkAlertDialog(NewApplyLeaveActivity.this, response.message(), "", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Contants2.forceLogout(NewApplyLeaveActivity.this);
                            }
                        });
                        contants2.dismissProgressDialog();
                        break;
                    case 500:
                        // contants2.dismissProgressDialog();
                        Contants2.showToastMessage(NewApplyLeaveActivity.this, getString(R.string.some_error_occurred), true);
                        contants2.dismissProgressDialog();
                        break;
                    default:
                        contants2.dismissProgressDialog();
                        Contants2.showToastMessage(NewApplyLeaveActivity.this, getString(R.string.some_error_occurred), true);
                        contants2.dismissProgressDialog();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.cancel();
                contants2.dismissProgressDialog();
                Contants2.showToastMessage(NewApplyLeaveActivity.this, getString(R.string.some_error_occurred), false);
            }
        });
    }

//    @Override
//    public void onFocusChange(View v, boolean hasFocus) {
//        switch(v.getId()) {
//            case R.id.edLeaveFromDate:
//                if(bIsFirstTimeFocus) {
//                    bIsFirstTimeFocus = false;
//                } else {
//                    showDatePickerDialog();
//                }
//                break;
//            case R.id.edLeaveToDate:
//                showDatePickerDialog();
//                break;
//            case R.id.edLeaveReason:
//                break;
//            default:
//                break;
//        }
//    }
}
