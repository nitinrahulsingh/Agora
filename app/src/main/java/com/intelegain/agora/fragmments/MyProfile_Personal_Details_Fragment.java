package com.intelegain.agora.fragmments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.intelegain.agora.R;
import com.intelegain.agora.activity.my_profile_edit_details_activity;
import com.intelegain.agora.constants.Constants;
import com.intelegain.agora.dataFetch.RetrofitClient;
import com.intelegain.agora.interfeces.WebApiInterface;
import com.intelegain.agora.interfeces.empProfileImageUpdate;
import com.intelegain.agora.model.EmployeeProfile;
import com.intelegain.agora.utils.Contants2;
import com.intelegain.agora.utils.Sharedprefrences;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


import static com.intelegain.agora.dataFetch.RetrofitClient.retrofit;

public class MyProfile_Personal_Details_Fragment extends Fragment implements View.OnClickListener {

    View view;
    ConstraintLayout constraint_layout;
    // LinearLayout linear_layout_error_message;
    TextView tv_Address, tv_contact_number, tv_email, tv_probabation_period, tv_joining_date,
            tv_account_number, tv_birth_date, tv_anniversary_date, tv_try_again/*tv_some_error_occurred*/;

    Contants2 contants2;
    EmployeeProfile employeeProfile;
    empProfileImageUpdate EmpProfileImageUpdate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_myprofile_personal_details, container, false);

        findViews();
        setClickListener();
        initialize();

        getEmployeeProfile();

        return view;
    }
    private Retrofit retrofit;

    /**
     * finds and binds view to the repective objects
     */
    private void findViews() {
        retrofit = RetrofitClient.getInstance(Constants.BASE_URL);
        constraint_layout = view.findViewById(R.id.constraint_layout);
        constraint_layout.setVisibility(View.INVISIBLE);
//        linear_layout_error_message = (LinearLayout) view.findViewById(R.id.linear_layout_error_message);

        tv_Address = view.findViewById(R.id.tv_Address);
        tv_contact_number = view.findViewById(R.id.tv_contact_number);
        tv_email = view.findViewById(R.id.tv_email);
        tv_probabation_period = view.findViewById(R.id.tv_probabation_period);
        tv_joining_date = view.findViewById(R.id.tv_joining_date);
        tv_account_number = view.findViewById(R.id.tv_account_number);
        tv_birth_date = view.findViewById(R.id.tv_birth_date);
        tv_anniversary_date = view.findViewById(R.id.tv_anniversary_date);

        tv_try_again = view.findViewById(R.id.text_view_try_again);
    }

    /**
     * sets listener for views
     */
    private void setClickListener() {
        tv_try_again.setOnClickListener(this);
    }

    /**
     * initializes objects
     */
    private void initialize() {
        EmpProfileImageUpdate = (empProfileImageUpdate) getActivity();
        contants2 = new Contants2();

    }

    /**
     * @param employeeProfile {@link EmployeeProfile} object which is used to set values of text view in UI
     */
    private void setValues(EmployeeProfile employeeProfile) {
        tv_Address.setText(employeeProfile.empAddress);
        tv_contact_number.setText(employeeProfile.empContact);
        tv_email.setText(employeeProfile.empEmail);
        tv_probabation_period.setText(employeeProfile.empProbationPeriod.toString());
        tv_joining_date.setText(employeeProfile.empJoiningDate);
        tv_account_number.setText(employeeProfile.empAccountNo);
        tv_birth_date.setText(employeeProfile.strBirthday);
        tv_anniversary_date.setText(employeeProfile.empADate);


        // calling interface method here to update the parent fragment image and other text views
        EmpProfileImageUpdate.updateParentFragment(employeeProfile.imageUrl, employeeProfile.empName, employeeProfile.empid, employeeProfile.designation);
    }


    /**
     * Retrofit Web Call to get employee profile details. Calls the interface through fragment to activity to update the
     * image, name, emp id, designation received here.
     */


    private void getEmployeeProfile() {
        if (Contants2.checkInternetConnection(getActivity())) {
            contants2.showProgressDialog(getActivity());

            WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
            Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(getActivity());
            String strToken = mSharedPrefs.getString("Token", "");
            String strEmpId = mSharedPrefs.getString("emp_Id", "");

            Map<String, String> params = new HashMap<>();
            params.put("EmpId", strEmpId);

            Call<EmployeeProfile> call = apiInterface.getEmployeeProfile(strEmpId, strToken, params);
            Log.d("WEB_URL", call.request() + "");
            call.enqueue(new Callback<EmployeeProfile>() {
                @Override
                public void onResponse(Call<EmployeeProfile> call, Response<EmployeeProfile> response) {
                    constraint_layout.setVisibility(View.VISIBLE);
                    switch (response.code()) {
                        case 200:
                            hideshowConstraintLayout(false);
                            employeeProfile = response.body();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setValues(employeeProfile);
                                    contants2.dismissProgressDialog();
                                }
                            }, 100);
                            break;
                        case 403:
                            contants2.dismissProgressDialog();
                            hideshowConstraintLayout(false);
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
                            hideshowConstraintLayout(true);
                            break;
                        default:
                            contants2.dismissProgressDialog();
                            Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                            hideshowConstraintLayout(true);
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    call.cancel();
                    contants2.dismissProgressDialog();
                    Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                }
            });
        } else {
            Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true);
            hideshowConstraintLayout(true);
        }
    }

    /**
     * @param hideConstraintLayout boolean value if true then need to hide constraint layout and false will make it visible
     */
    public void hideshowConstraintLayout(boolean hideConstraintLayout) {
        if (hideConstraintLayout) {
            constraint_layout.setVisibility(View.GONE);
            tv_try_again.setVisibility(View.VISIBLE);
        } else {
            tv_try_again.setVisibility(View.GONE);
            constraint_layout.setVisibility(View.VISIBLE);
        }

    }

    public void callEditProfileActivity() {
        Intent intentEditProfile = new Intent(getActivity(), my_profile_edit_details_activity.class);
        intentEditProfile.putExtra("empAddress", tv_Address.getText().toString().trim());
        intentEditProfile.putExtra("empContactNumber", tv_contact_number.getText().toString().trim());
        intentEditProfile.putExtra("empAnniversaryDate", tv_anniversary_date.getText().toString().trim());

        startActivityForResult(intentEditProfile, OPEN_DIALOUGE_REQUEST_CODE);
        //startActivity(intentEditProfile);
        getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

    }

    public static final int OPEN_DIALOUGE_REQUEST_CODE = 100;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OPEN_DIALOUGE_REQUEST_CODE) {
            getEmployeeProfile();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_view_try_again:
                getEmployeeProfile();
                break;
        }
    }


}
