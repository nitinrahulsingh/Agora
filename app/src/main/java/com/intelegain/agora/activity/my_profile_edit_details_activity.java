package com.intelegain.agora.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.intelegain.agora.R;
import com.intelegain.agora.interfeces.WebApiInterface;
import com.intelegain.agora.model.CommonStatusMessage;
import com.intelegain.agora.utils.Contants2;
import com.intelegain.agora.utils.Sharedprefrences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.intelegain.agora.api.urls.RetrofitClient.retrofit;

public class my_profile_edit_details_activity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    ImageView img_close;
    TextView tv_address_field_required, tv_contact_number_field_required/*, tvAnniversaryDate*/;
    EditText et_edit_address, et_edit_contact_number, etAnniversaryDate;
    Button btnSubmitEditProfile;
    private DatePickerDialog datePickerDialog;
    Contants2 contants2;
    CommonStatusMessage commonStatusMessage;

    long minDateInMillisecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_edit_details_activity);

        findViews();
        setValuesFromIntent();
        setListeners();
        initialize();
    }


    /**
     * finds and binds views to respective object
     */
    public void findViews() {
        img_close = findViewById(R.id.img_icon_close);
        et_edit_address = findViewById(R.id.et_edit_address);
        et_edit_contact_number = findViewById(R.id.et_edit_contact_number);

        tv_address_field_required = findViewById(R.id.tv_address_field_required);
        tv_contact_number_field_required = findViewById(R.id.tv_contact_number_field_required);
        etAnniversaryDate = findViewById(R.id.etAnniversaryDate);
        btnSubmitEditProfile = findViewById(R.id.btn_edit_details_submit);
    }


    /**
     * Retrieves value from intent and sets it to edit texts accordingly.
     */
    private void setValuesFromIntent() {
        Bundle bundle = getIntent().getExtras();
        et_edit_address.setText(bundle.getString("empAddress"));
        et_edit_contact_number.setText(bundle.getString("empContactNumber"));
        etAnniversaryDate.setText(bundle.getString("empAnniversaryDate"));

    }

    private void initialize() {
        contants2 = new Contants2();

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        format.setLenient(false);
        Date minDate = null;
        try {
            minDate = format.parse("01.01.1950, 00:01");
            minDateInMillisecond = minDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * set listener for UI widgets
     */
    public void setListeners() {
        img_close.setOnClickListener(this);
        etAnniversaryDate.setOnClickListener(this);
        btnSubmitEditProfile.setOnClickListener(this);

        et_edit_contact_number.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    validateEditProfile();
                }
                return false;
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_icon_close:
                finish();
                break;

            case R.id.etAnniversaryDate:
                // show calendar with date not going beyond todays date
                showDatePickerDialog();
                break;
            case R.id.btn_edit_details_submit:
                validateEditProfile();
                break;
        }
    }

    /**
     * validates the edit text and if Ok then callEditProfileApi method is called respectively.
     */
    public void validateEditProfile() {
        Contants2.hideKeyBoard(this);
        tv_address_field_required.setVisibility(View.INVISIBLE);
        tv_contact_number_field_required.setVisibility(View.INVISIBLE);
        if (et_edit_address.getText().toString().trim().equals("") || et_edit_contact_number.getText().toString().trim().equals("")
                || et_edit_contact_number.getText().toString().length() < 10) {
            if (et_edit_address.getText().toString().trim().equals("")) {
                tv_address_field_required.setVisibility(View.VISIBLE);
                et_edit_address.requestFocus();
            }
            if (et_edit_contact_number.getText().toString().trim().equals("")) {
                et_edit_contact_number.requestFocus();
                tv_contact_number_field_required.setVisibility(View.VISIBLE);
            }
            if (et_edit_contact_number.getText().toString().length() < 10 && et_edit_contact_number.getText().toString().length() > 0) {
                et_edit_contact_number.requestFocus();
                tv_contact_number_field_required.setVisibility(View.VISIBLE);
                tv_contact_number_field_required.setText(R.string.mobile_no);
            }
        } else
            callEditProfileApi();
    }

    /**
     * Retrofit Web Call to edit profile
     */
    public void callEditProfileApi() {
        if (Contants2.checkInternetConnection(this)) {
            contants2.showProgressDialog(this);
            WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
            commonStatusMessage = new CommonStatusMessage();

            Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(this);
            String strToken = mSharedPrefs.getString("Token", "");
            String strEmpId = mSharedPrefs.getString(getString(R.string.key_emp_id), "");


            Map<String, String> params = new HashMap<>();
            params.put("EmpId", strEmpId);
            params.put("CurrentAddress", et_edit_address.getText().toString().trim());
            params.put("ContactNumber", et_edit_contact_number.getText().toString().trim());
            params.put("AnniversaryDate", etAnniversaryDate.getText().toString());


            Call<CommonStatusMessage> call = apiInterface.editProfile(strEmpId, strToken, params);
            Log.d("WEB_URL", call.request() + "");
            call.enqueue(new Callback<CommonStatusMessage>() {
                @Override
                public void onResponse(Call<CommonStatusMessage> call, Response<CommonStatusMessage> response) {
                    switch (response.code()) {
                        case 200:
                            commonStatusMessage = response.body();
                            contants2.dismissProgressDialog();
                            if (commonStatusMessage.status.equals("1")) {
                                finish();
                                Contants2.showToastMessage(my_profile_edit_details_activity.this,
                                        commonStatusMessage.message, true);
                            } else
                                Contants2.showToastMessage(my_profile_edit_details_activity.this,
                                        commonStatusMessage.message, true);

                            break;
                        case 403:
                            contants2.dismissProgressDialog();
                            Contants2.showOkAlertDialog(my_profile_edit_details_activity.this, response.message(), "", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Contants2.forceLogout(my_profile_edit_details_activity.this);
                                }
                            });
                            break;
                        case 500:
                            contants2.dismissProgressDialog();
                            Contants2.showToastMessage(my_profile_edit_details_activity.this, getString(R.string.some_error_occurred), true);

                            break;
                        default:
                            contants2.dismissProgressDialog();
                            Contants2.showToastMessage(my_profile_edit_details_activity.this, getString(R.string.some_error_occurred), true);

                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    call.cancel();
                    contants2.dismissProgressDialog();
                }
            });
        } else {
            Contants2.showToastMessage(this, getString(R.string.no_internet), true);

        }

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
            datePickerDialog = new DatePickerDialog(my_profile_edit_details_activity.this, this, startYear, starthMonth, startDay);
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());

            //if (minDateInMillisecond == null)
            datePickerDialog.getDatePicker().setMinDate(minDateInMillisecond);

            datePickerDialog.show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String formattedDate = (dayOfMonth <= 9 ? "0" + dayOfMonth : dayOfMonth)
                + "/" + ((month + 1) <= 9 ? "0" + (month + 1) : (month + 1))
                + "/" + year;
        etAnniversaryDate.setText(formattedDate);
    }
}
