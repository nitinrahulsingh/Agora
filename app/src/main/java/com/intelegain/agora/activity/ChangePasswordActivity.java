package com.intelegain.agora.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.intelegain.agora.R;
import com.intelegain.agora.fragmments.CustomLoader_constant;
import com.intelegain.agora.fragmments.New_Home_activity;
import com.intelegain.agora.fragmments.SettingsActivity;
import com.intelegain.agora.model.ChangePasswordDetails;
import com.intelegain.agora.service.DataFetchServices;
import com.intelegain.agora.utils.Contants2;
import com.intelegain.agora.utils.Sharedprefrences;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener
{
    TextView chng_passwrd_title_text, text_userid, text_oldpass, text_new_pass, text_confirm_pass;
    EditText ed_userid, ed_oldpass, ed_new_pass, ed_confirm_pass;
    ImageView back_img_chng_passwrd;
    LinearLayout lay_back_img_chng_passwrd;
    CustomLoader_constant customize_dialog;
    Button bt_save;
    private String EmpId = "";
    private String NewPassword = "";
    private String OldPassword = "";
    private String ConfirmPassword = "";
    private DataFetchServices dft;
    private CommonMethods mCommonMethods;
    Sharedprefrences mSharedPref;

    enum function {
        getLeaveType, applyForLeaves, getNewPassword
    }

    ;
    private String EMPTY_EMP_ID = "Enter Employee ID"; // EMPTY_EMP_ID is a
    // Error Message when
    // Validation Failed.
    private String EMPTY_OLD_PASSWORD = "Enter Old Password";
    private String EMPTY_NEW_PASSWORD = "Enter New Password";
    private ChangePass changePass;
    private ChangePasswordDetails user_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();
        findView();
        sharedpreferences();
    }

    private void sharedpreferences() {
        mSharedPref = Sharedprefrences.getInstance(getApplicationContext());
        EmpId = mSharedPref.getString("emp_Id", null);
        ed_userid.setText(EmpId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_right, R.anim.slide_out_right).toBundle();
            startActivity(intent, bundle);
            finish();
        } else {
            startActivity(intent);
            finish();
        }
    }

    /**
     * This Method is for initialization
     **/
    private void init() {
        dft = new DataFetchServices();
    }

    /**
     * This Method is for declareing the id.
     **/
    private void findView() {
        chng_passwrd_title_text = (TextView) findViewById(R.id.chng_passwrd_title_text);
        text_userid = (TextView) findViewById(R.id.text_userid);
        text_oldpass = (TextView) findViewById(R.id.text_oldpass);
        text_new_pass = (TextView) findViewById(R.id.text_new_pass);
        text_confirm_pass = (TextView) findViewById(R.id.text_confirm_pass);

        lay_back_img_chng_passwrd = (LinearLayout) findViewById(R.id.lay_back_img_chng_passwrd);

        back_img_chng_passwrd = (ImageView) findViewById(R.id.back_img_chng_passwrd);

        bt_save = (Button) findViewById(R.id.bt_save);

        ed_userid = (EditText) findViewById(R.id.ed_userid);
        ed_oldpass = (EditText) findViewById(R.id.ed_oldpass);
        ed_confirm_pass = (EditText) findViewById(R.id.ed_confirm_pass);
        ed_new_pass = (EditText) findViewById(R.id.ed_new_pass);


        bt_save.setTextSize(TypedValue.COMPLEX_UNIT_PX, 48);

        Typeface font1 = Typeface.createFromAsset(getAssets(), "fonts/segoeui.ttf");
        chng_passwrd_title_text.setTypeface(font1);
        text_userid.setTypeface(font1);
        text_oldpass.setTypeface(font1);
        text_new_pass.setTypeface(font1);
        ed_new_pass.setTypeface(font1);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/segoeuil.ttf");

        ed_userid.setTypeface(font);
        ed_oldpass.setTypeface(font);
        ed_confirm_pass.setTypeface(font);
        ed_new_pass.setTypeface(font);
        bt_save.setTypeface(font);
        chng_passwrd_title_text.setTypeface(font);
        lay_back_img_chng_passwrd.setOnClickListener(this);
        bt_save.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_back_img_chng_passwrd:

                Intent intent = new Intent(ChangePasswordActivity.this, SettingsActivity.class);
                if (android.os.Build.VERSION.SDK_INT >= 16) {
                    Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_right, R.anim.slide_out_right).toBundle();
                    startActivity(intent, bundle);
                } else {
                    startActivity(intent);
                }
                finish();
                break;

            case R.id.bt_save:

                OldPassword = ed_oldpass.getText().toString();
                NewPassword = ed_new_pass.getText().toString();
                ConfirmPassword = ed_confirm_pass.getText().toString();
                if (EmpId.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter EmpId.", Toast.LENGTH_SHORT).show();
                } else if (OldPassword.equals("")) {
                    Toast.makeText(getApplicationContext(), Contants2.Old_Password, Toast.LENGTH_SHORT).show();
                } else if (NewPassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter new password", Toast.LENGTH_SHORT).show();
                } else if (OldPassword.equalsIgnoreCase(NewPassword)) {
                    Toast.makeText(getApplicationContext(), "Old password and new password should not be same", Toast.LENGTH_SHORT).show();
                } else if (!NewPassword.equalsIgnoreCase(ConfirmPassword)) {
                    Toast.makeText(getApplicationContext(), "New password and confirm password should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    if (CommonMethods.checkInternetConnection(ChangePasswordActivity.this)) {
                        changePass = new ChangePass();
                        changePass.execute(function.getNewPassword);
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, CommonMethods.no_internet, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public class ChangePass extends AsyncTask<function, Void, function> implements DialogInterface.OnCancelListener {
        @Override
        protected function doInBackground(function... params) {
            function result = null;

            switch (params[0]) {
                case getNewPassword:
                    try {
                        changePass("changePassword");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            return params[0];
        }

        private void changePass(String methodname) throws Exception {
            user_data = (ChangePasswordDetails) dft.ChangePass(methodname, EmpId, ed_oldpass.getText().toString(), ed_new_pass.getText().toString(), ed_confirm_pass.getText().toString(), ChangePasswordActivity.this, 1);

        }

        protected void onPreExecute() {
            super.onPreExecute();
            customize_dialog = CustomLoader_constant.show(ChangePasswordActivity.this, "Loading...", true, false, false, this);
        }

        protected void onPostExecute(function result) {
            switch (result) {
                case getNewPassword:

                    if (user_data != null) {
                        if (user_data.Status == 1) {
                            Toast.makeText(ChangePasswordActivity.this, user_data.Message, Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(ChangePasswordActivity.this, SettingsActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, user_data.Message, Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        final Dialog dialog = new Dialog(ChangePasswordActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.access_denied);


                        TextView text = (TextView) dialog.findViewById(R.id.txtDiaTitle);
                        TextView img = (TextView) dialog.findViewById(R.id.txtDiaMsg);
                        Button dialogButton = (Button) dialog.findViewById(R.id.btnOk);
                        dialogButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent Logout = new Intent(ChangePasswordActivity.this, New_Home_activity.class);
                                startActivity(Logout);
                                mSharedPref.clear();
                                finish();

                            }
                        });
                        dialog.show();
                    }
                    break;
                default:
                    break;
            }
            super.onPostExecute(result);
            if (customize_dialog.isShowing())
                customize_dialog.dismiss();
        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
}

