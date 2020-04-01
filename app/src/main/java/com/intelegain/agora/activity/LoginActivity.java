package com.intelegain.agora.activity;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.intelegain.agora.R;

import com.intelegain.agora.api.urls.RetrofitClient;
import com.intelegain.agora.constants.Constants;
import com.intelegain.agora.fragmments.CustomLoader_constant;
import com.intelegain.agora.interfeces.WebApiInterface;
import com.intelegain.agora.model.CustomerData;
import com.intelegain.agora.model.EmpLogin;
import com.intelegain.agora.model.UserData;
import com.intelegain.agora.model.model_login.RequestLogin;
import com.intelegain.agora.model.model_login.ResponseLogin;
import com.intelegain.agora.service.DataFetchServices;
import com.intelegain.agora.service.MyAlarmService;

import com.intelegain.agora.utils.Contants2;
import com.intelegain.agora.utils.FileDownloader;
import com.intelegain.agora.utils.MyUtils;
import com.intelegain.agora.utils.Sharedprefrences;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, FileDownloader.onDownloadTaskFinish, GoogleApiClient.OnConnectionFailedListener {
    Sharedprefrences mSharedPref; // mSharedPref is a Sharedprefrences
    int Birthday,
            BirthMonth,
            value = 0;
    Date date_frm;

    CustomLoader_constant customize_dialog;// for custom progress bar(loading..)

    EditText et_empid, // Edit text for employee id
            et_paswd; // Edit text for password field
    Button btn_login;
    TextView tv_forgot_pass;
//    CircleImageView btnCustomFacebookLogin, btnCustomGoogleLogin, btnLinkInLogin;
    //    LoginButton btnFacebookLogin;
//    SignInButton btnGoogleLogin;

    String DBirth;

    Calendar calendar;
    DataFetchServices dft; // This class includes HTTp Connection call
    UserData user_data; // This class includes Strings.
    CustomerData cust_data;
    LoginAuth loginAuth;
    CustomerLoginAuth CustomerLogin; // AsyncTask
    Contants2 contants2;
    EmpLogin empLogin;
    Retrofit retrofit;
    String mToken;
    //AESCrypt aescrypt; // aescrypt is in AESCrypt class for Encrypt


    //private CheckBox cb_remb_me; // CheckBox for Remember me.
    String EMPTY_EMP_ID = "Enter Employee ID", // EMPTY_EMP_ID is a
            EMPTY_PASWD = "Enter Password", // EMPTY_PASWD is a Error
            EmpId = "", // EmpID is hold the employee id which enter in
            Password = "", // Paswd is hold the password which enter in
            OsType = "Android",
            DeviceId = "f07a13984f6d116a";


    //    CallbackManager callbackManager;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInOptions gso;
    private final int RC_SIGN_IN = 9001;
    ProgressDialog progressBar;
    LinearLayout mCenterLayout;


    private final String linkedIn_host = "api.linkedin.com",
            linkedIn_url = "https://" + linkedIn_host
                    + "/v1/people/~:" +
                    "(email-address,formatted-name,phone-numbers,picture-urls::(original))";

    Contants2 constants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mToken = Sharedprefrences.getInstance(LoginActivity.this).getString(getString(R.string.key_fcm_token), "");
        initialize(); // Calling init method.
        findViews(); // Calling findView.

        /*getIntentData();
        loginWithFacebook();
        googleSignInSetUp();*/

        //generateHashkey();
        //setDynamicViews();
    }

    /**
     * This Method is for initialization
     */

    private void initialize() {

        dft = new DataFetchServices();
        constants = new Contants2();
        user_data = new UserData();
        cust_data = new CustomerData();
        contants2 = new Contants2();

        mSharedPref = Sharedprefrences.getInstance(getApplicationContext());
        calendar = Calendar.getInstance();
        retrofit = RetrofitClient.getInstance(Constants.BASE_URL);
        // FacebookSdk.sdkInitialize(getApplicationContext());

    }

    /**
     * This Method is for declareing the id.
     */

    private void findViews() {
        tv_forgot_pass = findViewById(R.id.textview_forgot_pass);

        et_empid = findViewById(R.id.login_et_empid);
        et_paswd = findViewById(R.id.login_et_paswd);
//        btnCustomFacebookLogin = findViewById(R.id.facebook_login_custom_button);
//        btnGoogleLogin = findViewById(R.id.sign_in_button);
//        btnLinkInLogin = findViewById(R.id.linkedin_login_custom_button);
//        btnCustomGoogleLogin = findViewById(R.id.google_login_custom_button);
//        btnFacebookLogin = findViewById(R.id.btn_facebook);
        mCenterLayout = findViewById(R.id.linearLayout1);

        btn_login = findViewById(R.id.login_btn_login);

        String text = "<font color=#c8beb2>Forgot Password</font> <font color=#ef9b11> ?</font>";
        tv_forgot_pass.setText(Html.fromHtml(text));

        btn_login.setOnClickListener(this);
        tv_forgot_pass.setOnClickListener(this);

//        btnCustomFacebookLogin.setOnClickListener(this);
//        btnGoogleLogin.setOnClickListener(this);
//        btnLinkInLogin.setOnClickListener(this);
        tv_forgot_pass.setOnClickListener(this);


//        btnCustomGoogleLogin.setOnClickListener(this);


        //  cb_remb_me = (CheckBox) findViewById(R.id.login_cb_rembme);
        //     cb_remb_me.setChecked(false);
//       cb_remb_me.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//                    FingerprintManager fingerprintManager = null;
//                    fingerprintManager = getSystemService(FingerprintManager.class);
//
//                    if (!fingerprintManager.isHardwareDetected()) {
//                        if (cb_remb_me.isChecked()) {
//                            touch_view.setVisibility(View.GONE);
//                        } else {
//                            touch_view.setVisibility(View.GONE);
//                        }
//                    } else {
//
//                        if (!fingerprintManager.hasEnrolledFingerprints()) {
//
//                            if (cb_remb_me.isChecked()) {
//                                touch_view.setVisibility(View.GONE);
//                            } else {
//                                touch_view.setVisibility(View.GONE);
//                            }
//                            return;
//                        } else {
//
//                            if (cb_remb_me.isChecked()) {
//                                touch_view.setVisibility(View.VISIBLE);
//                            } else {
//                                touch_view.setVisibility(View.GONE);
//                            }
//                            return;
//                        }
//
//                    }
//
//
//                } else {
//                    touch_view.setVisibility(View.GONE);
//                }
//
//
//            }
//        });


    }


    /**
     * retrofit web call to login web api
     *
     * @param user_type if employee login then send "empLogin" and "custLogin" otherwise.
     * @param token     // FCM token to send notification
     */
    private void Login(String user_type, final String token) {
        if (Contants2.checkInternetConnection(LoginActivity.this)) {
            contants2.showProgressDialog(LoginActivity.this);
            WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
            //Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(LoginActivity.this);

            String strEmpId = et_empid.getText().toString();

            Map<String, String> params = new HashMap<>();
            params.put("EmpId", et_empid.getText().toString().trim());
            params.put("Password", et_paswd.getText().toString().trim());
            params.put("DeviceId", DeviceId);
            params.put("OsType", OsType);
            params.put("fcmToken", token);

            Call<EmpLogin> call = apiInterface.empLogin(strEmpId, params);
            call.enqueue(new Callback<EmpLogin>() {
                @Override
                public void onResponse(Call<EmpLogin> call, Response<EmpLogin> response) {

                    switch (response.code()) {
                        case 200:
                            contants2.dismissProgressDialog();
                            empLogin = response.body();

                            mSharedPref.putString(getString(R.string.key_emp_id), et_empid.getText().toString().trim());
                            mSharedPref.putString(getString(R.string.key_emp_name), empLogin.name);
                            mSharedPref.putString(getString(R.string.key_token), empLogin.token);
                            mSharedPref.putString(getString(R.string.key_fcm_token), token);
                            mSharedPref.commit();
                            downloadProfileImage(empLogin.uploadedImage, "base64");

                            break;
                        case 401:
                            contants2.dismissProgressDialog();
                            Contants2.showToastMessage(LoginActivity.this, getString(R.string.invalid_login_credentials), true);
                            break;
                        case 500:
                            contants2.dismissProgressDialog();
                            Contants2.showToastMessage(LoginActivity.this, getString(R.string.some_error_occurred), true);
                            break;
                        default:
                            contants2.dismissProgressDialog();
                            Contants2.showToastMessage(LoginActivity.this, getString(R.string.some_error_occurred), true);
                            break;
                    }
                }

                @Override
                public void onFailure(Call<EmpLogin> call, Throwable t) {

                    contants2.dismissProgressDialog();
                    Contants2.showToastMessage(LoginActivity.this, getString(R.string.some_error_occurred), true);
                }
            });
        } else {
            Contants2.showToastMessage(LoginActivity.this, getString(R.string.no_internet), true);
        }

    }

    public void downloadProfileImage(String base64Data, String fileType) {
        if (!base64Data.equals("")) {
            FileDownloader fileDownloader = new FileDownloader(this, this);
            fileDownloader.execute(base64Data, Contants2.emp_profile_image, fileType);
            mSharedPref.putboolean(getString(R.string.key_is_remove_photo), false);
        } else {
            callLoginScreen(true);
            mSharedPref.putboolean(getString(R.string.key_is_remove_photo), true);
        }
    }


    @Override
    public void onDownloadSuccess(String strResult, String fileName) {
        callLoginScreen(false);
    }

    @Override
    public void onDownloadFailed(String strResult) {

    }

    private void callLoginScreen(boolean isRemovePhoto) {
        Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
        intent.putExtra("isRemovePhoto", isRemovePhoto);
        startActivity(intent);
        finishAffinity();
    }

    private void getIntentData() {

        if (mSharedPref.getBoolean("isChecked", false)) {

            if (mSharedPref.getBoolean("CheckBox", false)) {

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//                }
                // cb_remb_me.setChecked(true);
                et_empid.setText(mSharedPref.getString(getString(R.string.key_emp_id), null));
                et_paswd.setText(mSharedPref.getString(getString(R.string.key_paswd), null));

            }
            //   cb_remb_me.setChecked(true);
            et_empid.setText(mSharedPref.getString(getString(R.string.key_emp_id), null));
            et_paswd.setText(mSharedPref.getString(getString(R.string.key_paswd), null));

        } else {

            //   cb_remb_me.setChecked(false);
            et_empid.setText("", null);
            et_paswd.setText("", null);


        }


    }

    //************************ Facebook Login ****************************

//    private void loginWithFacebook() {
//        callbackManager = CallbackManager.Factory.create();
//        btnFacebookLogin.setReadPermissions("public_profile");
//        btnFacebookLogin.setReadPermissions(Arrays.asList("user_status"));
//
//        btnFacebookLogin.getLoginBehavior();
//
//
//        btnFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//
//                Toast.makeText(LoginActivity.this, " Login Success", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancel() {
//
//                Toast.makeText(LoginActivity.this, " Login Failed -> onCancel", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//                error.printStackTrace();
//            }
//        });
//    }


    //************************ Google Sign In **********************************


    private void googleSignInSetUp() {

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    private void generateHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.intelgain",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.getStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.getStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_btn_login:

                if (value == 0) {

                    EmpId = et_empid.getText().toString();
                    Password = et_paswd.getText().toString();
                    et_empid.setError(null);
                    et_empid.setError(null);
                    if (constants.isEmptyOrNull(EmpId)) {
                        et_empid.setError(EMPTY_EMP_ID);
                        // contants2.showToastMessage(LoginActivity.this, getString(R.string.fill_valid_details), true);
                    } else if (constants.isEmptyOrNull(Password)) {
                        et_paswd.setError(EMPTY_PASWD);
                    } else {
                        if (mToken.isEmpty()) {
                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    mToken = instanceIdResult.getToken();
                                }
                            });
                        }
                        Login("empLogin", mToken);
                        /*if (Contants2.checkInternetConnection(LoginActivity.this)) {
                            loginAuth = new LoginAuth();
                            loginAuth.execute(function.getLoginAuth);

                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        }*/
                    }

                } else {
                    EmpId = et_empid.getText().toString();
                    Password = et_paswd.getText().toString();

                    et_empid.setError(null);
                    et_empid.setError(null);

                    if (constants.isEmptyOrNull(EmpId)) {
                        et_empid.setError(EMPTY_EMP_ID);
                    } else if (constants.isEmptyOrNull(Password)) {
                        et_paswd.setError(EMPTY_PASWD);
                    } else {
                        if (Contants2.checkInternetConnection(LoginActivity.this)) {
                            CustomerLogin = new CustomerLoginAuth();
                            CustomerLogin.execute(function.getLoginAuth);
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        }
                    }

                }


                break;
            case R.id.textview_forgot_pass:
                /*Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);*/

                break;

//            case R.id.facebook_login_custom_button:
//
//                Contants2.showToastMessage(LoginActivity.this, getString(R.string.under_development), false);
//
//                /*if (Contants2.checkInternetConnection(LoginActivity.this)) {
//
//                    if (AccessToken.getCurrentAccessToken() != null) {
//                        AccessToken.setCurrentAccessToken(null);
//                        btnFacebookLogin.performClick();
//
//                    } else {
//                        btnFacebookLogin.performClick();
//                    }
//
//                } else {
//                    Toast.makeText(LoginActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
//                }*/
//
//
//                break;


//            case R.id.google_login_custom_button:
//                Contants2.showToastMessage(LoginActivity.this, getString(R.string.under_development), false);
//
//                /*showProgressDialog();
//
//                btnGoogleLogin.performClick();
//                signInWithGoogle();*/
//
//                break;

//            case R.id.linkedin_login_custom_button:
//                Contants2.showToastMessage(LoginActivity.this, getString(R.string.under_development), false);
//
//                // loginWithLinkedIn();
//
//                /*showProgressDialog();
//
//                btnGoogleLogin.performClick();
//                signInWithGoogle();*/
//
//                break;

            /* case R.id.Employee_Login:

                if (v == btn_Employee_Login) {


                    btn_Cusrtomer_Login.setBackgroundResource(R.drawable.toggle_black_right);
                    btn_Employee_Login.setBackgroundResource(R.drawable.toggle_orange_left);
                    btn_Employee_Login.setTextColor(Color.parseColor("#ffffff"));
                    btn_Cusrtomer_Login.setTextColor(Color.parseColor("#cd8612"));
                    et_empid.setHint("Enter Employee ID");
                    value = 0;

                }
                break;


            case R.id.Cusrtomer_Login:
                if (v == btn_Cusrtomer_Login) {
                    btn_Cusrtomer_Login.setBackgroundResource(R.drawable.toggle_orange_right);
                    btn_Employee_Login.setBackgroundResource(R.drawable.toggle_black_left);
                    btn_Cusrtomer_Login.setTextColor(Color.parseColor("#ffffff"));
                    btn_Employee_Login.setTextColor(Color.parseColor("#cd8612"));
                    et_empid.setHint("Enter Customer ID");

                    value = 1;
                }
                break;*/


            default:
                break;
        }
    }


    //************************ LinkedIn Sign In **********************************

//    private void loginWithLinkedIn() {
//
//        LISessionManager.getInstance(LoginActivity.this)
//                .init(this, buildScope(), new AuthListener() {
//                    @Override
//                    public void onAuthSuccess() {
//
//                        Toast.makeText(getApplicationContext(), "success",
//                                Toast.LENGTH_LONG).show();
//
//                    }
//
//                    @Override
//                    public void onAuthError(LIAuthError error) {
//
//                        Log.e("ErrorMsg", error.toString());
//
//                        Toast.makeText(getApplicationContext(), "failed "
//                                        + error.toString(),
//                                Toast.LENGTH_LONG).show();
//                    }
//                }, true);
//
//    }
//
//    private static Scope buildScope() {
//        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
//    }


    private void generateHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.intelgain", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String strHashKey = Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                Log.e("HashKey", "" + strHashKey);

            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.e("Error", e.getMessage(), e);
        }
    }

    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (progressBar != null)
            progressBar.dismiss();

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(LoginActivity.this, "Google Login failed", Toast.LENGTH_SHORT).show();

        }
    }
    ///**************************** Dialog Loader ***************************


    private void showProgressDialog() {

        progressBar = new ProgressDialog(LoginActivity.this);
        progressBar.setMessage("Signing ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
    }


    private void login() throws Exception {
        user_data = (UserData) dft.LoginAuth("login", EmpId, Password, DeviceId, OsType, LoginActivity.this, 1);
    }

    private void Customerlogin() throws Exception {
        cust_data = (CustomerData) dft.CustomerLoginAuth("Login", EmpId, Password, DeviceId, OsType, LoginActivity.this, 1);
    }

    public void setNotification() {

        Calendar calNow = Calendar.getInstance();
        Calendar cal = (Calendar) calNow.clone();
        DBirth = mSharedPref.getString("emp_birthDate", null);
        Log.d("Date_of_Birth", "Date_of_Birth" + DBirth);

        if (DBirth != null) {
            DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat outputFormat = new SimpleDateFormat("dd");
            DateFormat outputFormat1 = new SimpleDateFormat("MM");


            try {
                date_frm = inputFormat.parse(DBirth);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            Birthday = Integer.parseInt(outputFormat.format(date_frm));
            BirthMonth = Integer.parseInt(outputFormat1.format(date_frm));
            System.out.println("JoiningDateBirthMonth:- " + BirthMonth);
            System.out.println("JoiningDate:- " + Birthday);
        }


        cal.set(Calendar.DATE, Birthday);
        cal.set(Calendar.MONTH, BirthMonth - 1);
        cal.set(Calendar.HOUR_OF_DAY, 18);  //HOUR
        cal.set(Calendar.MINUTE, 47);       //MIN
        cal.set(Calendar.SECOND, 00);       //SEC


        if (cal.compareTo(calNow) <= 0) {

        } else {

            Intent intent = new Intent(LoginActivity.this, MyAlarmService.class);
            PendingIntent pendingIntent = PendingIntent.getService(LoginActivity.this, 0, intent, 0);
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

        }


    }


    enum function // contains getLoginAuth.
    {
        getLoginAuth
    }


    public class LoginAuth extends AsyncTask<function, Void, function> implements DialogInterface.OnCancelListener {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customize_dialog = CustomLoader_constant.show(LoginActivity.this, "Loading...", true, false, false, this);
        }

        @Override
        protected function doInBackground(function... params) {
            function result = null;

            switch (params[0]) {
                case getLoginAuth:
                    try {
                        login();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            return params[0];
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(function result) {
            switch (result) {
                case getLoginAuth:


                    if (user_data.user_login_data != null) {
                        if (user_data.user_login_data.size() != 0) {

                            mSharedPref.putString(getString(R.string.key_emp_id), et_empid.getText().toString().trim());
                            mSharedPref.putString(getString(R.string.key_emp_name), user_data.user_login_data.get(0).Name);


                            Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
                            startActivity(intent);
                            finishAffinity();

                            // mSharedPref.putboolean(getString(R.string.key_remember_me), true);

           /*if (mSharedPref.getBoolean(getString(R.string.key_remember_me), true)) {

           if (cb_remb_me.isChecked()) {
                                    mSharedPref.putboolean("isChecked", true);
                                    mSharedPref.putString(getString(R.string.key_emp_id), et_empid.getText().toString());
                                    mSharedPref.putString(getString(R.string.key_paswd), et_paswd.getText().toString());


                                } else {
                                    mSharedPref.putboolean("isChecked", false);
                                    mSharedPref.putString(getString(R.string.key_emp_id), et_empid.getText().toString());
                                    mSharedPref.putString(getString(R.string.key_paswd), et_paswd.getText().toString());

                                }
                            }*/


                        } else {
                            Toast.makeText(LoginActivity.this, "Please enter correct Empid and Password", Toast.LENGTH_SHORT).show();
                        }
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


    public class CustomerLoginAuth extends AsyncTask<function, Void, function> implements DialogInterface.OnCancelListener {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customize_dialog = CustomLoader_constant.show(LoginActivity.this, "Loading...", true, false, false, this);

        }

        @Override
        protected function doInBackground(function... params) {
            function result = null;

            switch (params[0]) {
                case getLoginAuth:
                    try {
                        Customerlogin();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            return params[0];
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(function result) {
            switch (result) {
                case getLoginAuth:


                    if (cust_data.customer_login_data != null) {
                        if (cust_data.customer_login_data.size() != 0) {


                            mSharedPref.putboolean(getString(R.string.key_remember_me), true);

                            if (mSharedPref.getBoolean(getString(R.string.key_remember_me), true)) {

                                  /*  if (cb_remb_me.isChecked()) {
                                    mSharedPref.putboolean("isChecked", true);
                                    mSharedPref.putString(getString(R.string.key_emp_id), et_empid.getText().toString());
                                    mSharedPref.putString(getString(R.string.key_paswd), et_paswd.getText().toString());


                                } else {
                                    mSharedPref.putboolean("isChecked", false);
                                    mSharedPref.putString(getString(R.string.key_emp_id), et_empid.getText().toString());
                                    mSharedPref.putString(getString(R.string.key_paswd), et_paswd.getText().toString());

                                }*/
                            }

                            Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, "Please enter correct Empid and Password", Toast.LENGTH_SHORT).show();
                        }
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);

//        LISessionManager.getInstance(LoginActivity.this)
//                .onActivityResult(this,
//                        requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


/*
    private void setDynamicViews() {
        ViewTreeObserver vto = mCenterLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mCenterLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                width = mCenterLayout.getMeasuredWidth();
                height = mCenterLayout.getMeasuredHeight();
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


    }*/

}