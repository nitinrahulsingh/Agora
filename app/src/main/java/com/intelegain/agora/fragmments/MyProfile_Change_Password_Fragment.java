package com.intelegain.agora.fragmments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.intelegain.agora.R;
import com.intelegain.agora.interfeces.WebApiInterface;
import com.intelegain.agora.model.ChangePassword;
import com.intelegain.agora.utils.Contants2;
import com.intelegain.agora.utils.Sharedprefrences;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.intelegain.agora.api.urls.RetrofitClient.retrofit;

public class MyProfile_Change_Password_Fragment extends Fragment {
    View view;
    ChangePassword changePassword;
    private Contants2 contants2;

    EditText et_old_password, et_new_password, et_confirm_password;
    TextView tv_confirm_pass_field_required, tv_new_pass_field_required, tv_old_pass_field_required;

    String strOldNewNotSame = "Old password and New password cannot be the same.",
            strNewConfirmNotMatch = "New password and Confirm password does not match.",
            oldPassword, newPassword, confirmPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_myprofile_change_password, container, false);

        findViews();
        initialize();
        setListener();
        /*setDropDown();
        //initSwipe();
        generateDemoList();
*/


        return view;
    }

    /**
     * finds and initialize views to corresponding objects
     */
    private void findViews() {
        et_old_password = (EditText) view.findViewById(R.id.et_old_password);
        et_new_password = (EditText) view.findViewById(R.id.et_new_password);
        et_confirm_password = (EditText) view.findViewById(R.id.et_confirm_password);

        tv_old_pass_field_required = (TextView) view.findViewById(R.id.tv_old_pass_field_required);
        tv_new_pass_field_required = (TextView) view.findViewById(R.id.tv_new_pass_field_required);
        tv_confirm_pass_field_required = (TextView) view.findViewById(R.id.tv_confirm_pass_field_required);
    }

    private void initialize() {
        contants2 = new Contants2();
    }

    /**
     * sets listener for confirm password edit text.
     */
    private void setListener() {
        et_confirm_password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    validateAndMakeApiCall();
                }
                return false;
            }

        });
    }

    /*{
        "EmpId":"1024",
            "OldPassword":"dynamic123",
            "NewPassword":"dynamic111",
            "ConfirmPassword":"dynamic111"
    }*/

    /**
     * Retrofit Web Call to to change user password
     */
    private void changePassword() {
        if (Contants2.checkInternetConnection(getActivity())) {
            contants2.showProgressDialog(getActivity());
            WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
            Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(getActivity());
            String strToken = mSharedPrefs.getString("Token", "");
            String strEmpId = mSharedPrefs.getString("emp_Id", "1000");


            Map<String, String> params = new HashMap<>();
            params.put("EmpId", strEmpId);
            params.put("OldPassword", et_old_password.getText().toString().trim());
            params.put("NewPassword", et_new_password.getText().toString().trim());
            params.put("ConfirmPassword", et_confirm_password.getText().toString().trim());

            Call<ChangePassword> call = apiInterface.changePassword(strEmpId, strToken, params);
            call.enqueue(new Callback<ChangePassword>() {
                @Override
                public void onResponse(Call<ChangePassword> call, Response<ChangePassword> response) {
                    switch (response.code()) {
                        case 200:
                            contants2.dismissProgressDialog();
                            changePassword = response.body();
                            contants2.showToastMessage(getActivity(), changePassword.message, true);
                            if (changePassword.status.equals("1")) {
                                et_old_password.setText("");
                                et_new_password.setText("");
                                et_confirm_password.setText("");

                                et_old_password.clearFocus();
                                et_new_password.clearFocus();
                                et_confirm_password.clearFocus();
                            }


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
                            contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);

                            break;
                        default:
                            contants2.dismissProgressDialog();
                            contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);

                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    call.cancel();
                    contants2.dismissProgressDialog();
                }
            });
        } else {
            contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true);

        }
    }

    /**
     * Validates the edit text fields for blank check and new password and confirm password are of same length,
     * if all good api call to change password is made.
     */
    public void validateAndMakeApiCall() {
        Contants2.hideKeyBoard(getActivity());
        tv_old_pass_field_required.setVisibility(View.GONE);
        tv_new_pass_field_required.setVisibility(View.GONE);
        tv_confirm_pass_field_required.setVisibility(View.GONE);


        oldPassword = et_old_password.getText().toString().trim();
        newPassword = et_new_password.getText().toString().trim();
        confirmPassword = et_confirm_password.getText().toString().trim();

        if (oldPassword.equals("") || newPassword.equals("") || confirmPassword.equals("")) {

            //contants2.showToastMessage(getActivity(), strErrorValidation, true);
            if (oldPassword.equals(""))
                tv_old_pass_field_required.setVisibility(View.VISIBLE);
            if (newPassword.equals(""))
                tv_new_pass_field_required.setVisibility(View.VISIBLE);
            if (confirmPassword.trim().equals(""))
                tv_confirm_pass_field_required.setVisibility(View.VISIBLE);

        } else if (oldPassword.length() > 0 && newPassword.length() > 0 && confirmPassword.length() > 0)
            if (oldPassword.equals(newPassword))
                contants2.showToastMessage(getActivity(), strOldNewNotSame, true);
            else if (!newPassword.equals(confirmPassword))
                contants2.showToastMessage(getActivity(), strNewConfirmNotMatch, true);
            else
                changePassword();

    }


}
