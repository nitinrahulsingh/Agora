package com.intelegain.agora.activity

import android.annotation.TargetApi
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.firebase.iid.FirebaseInstanceId
import com.intelegain.agora.R
import com.intelegain.agora.api.urls.RetrofitClient.getInstance
import com.intelegain.agora.constants.Constants
import com.intelegain.agora.fragmments.CustomLoader_constant
import com.intelegain.agora.interfeces.WebApiInterface
import com.intelegain.agora.model.CustomerData
import com.intelegain.agora.model.EmpLogin
import com.intelegain.agora.model.UserData
import com.intelegain.agora.service.DataFetchServices
import com.intelegain.agora.service.MyAlarmService
import com.intelegain.agora.utils.Contants2
import com.intelegain.agora.utils.FileDownloader
import com.intelegain.agora.utils.FileDownloader.onDownloadTaskFinish
import com.intelegain.agora.utils.Sharedprefrences
import com.intelegain.agora.utils.Sharedprefrences.Companion.getInstance
import kotlinx.android.synthetic.main.activity_login_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity(), View.OnClickListener, onDownloadTaskFinish, OnConnectionFailedListener {
    var mSharedPref // mSharedPref is a Sharedprefrences
            : Sharedprefrences? = null
    var Birthday = 0
    var BirthMonth = 0
    var value = 0
    var date_frm: Date? = null
    var customize_dialog // for custom progress bar(loading..)
            : CustomLoader_constant? = null
    var et_empid: EditText? = null
    // Edit text for employee id
    var et_paswd // Edit text for password field
            : EditText? = null
    var btn_login: Button? = null
    var tv_forgot_pass: TextView? = null
    //    CircleImageView btnCustomFacebookLogin, btnCustomGoogleLogin, btnLinkInLogin;
//    LoginButton btnFacebookLogin;
//    SignInButton btnGoogleLogin;
    var DBirth: String? = null
    var calendar: Calendar? = null
    var dft // This class includes HTTp Connection call
            : DataFetchServices? = null
    var user_data // This class includes Strings.
            : UserData? = null
    var cust_data: CustomerData? = null
    var loginAuth: LoginAuth? = null
    var CustomerLogin // AsyncTask
            : CustomerLoginAuth? = null
    var contants2: Contants2? = null
    var empLogin: EmpLogin? = null
    var retrofit: Retrofit? = null
    var mToken: String? = null
    //AESCrypt aescrypt; // aescrypt is in AESCrypt class for Encrypt
//private CheckBox cb_remb_me; // CheckBox for Remember me.
    var EMPTY_EMP_ID = "Enter Employee ID"
    // EMPTY_EMP_ID is a
    var EMPTY_PASWD = "Enter Password"
    // EMPTY_PASWD is a Error
    var EmpId = ""
    // EmpID is hold the employee id which enter in
    var Password = ""
    // Paswd is hold the password which enter in
    var OsType = "Android"
    var DeviceId = "f07a13984f6d116a"
    //    CallbackManager callbackManager;
    var mGoogleApiClient: GoogleApiClient? = null
    var gso: GoogleSignInOptions? = null
    private val RC_SIGN_IN = 9001
    var progressBar: ProgressDialog? = null
    var mCenterLayout: LinearLayout? = null
    private val linkedIn_host = "api.linkedin.com"
    private val linkedIn_url = ("https://" + linkedIn_host
            + "/v1/people/~:" +
            "(email-address,formatted-name,phone-numbers,picture-urls::(original))")
    var constants: Contants2? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        mToken = getInstance(this@LoginActivity).getString(getString(R.string.key_fcm_token), "")
        initialize() // Calling init method.
        findViews() // Calling findView.
        /*getIntentData();
        loginWithFacebook();
        googleSignInSetUp();*/
//generateHashkey();
//setDynamicViews();
    }

    /**
     * This Method is for initialization
     */
    private fun initialize() {
        dft = DataFetchServices()
        constants = Contants2()
        user_data = UserData()
        cust_data = CustomerData()
        contants2 = Contants2()
        mSharedPref = getInstance(applicationContext)
        calendar = Calendar.getInstance()
        retrofit = getInstance(Constants.BASE_URL)
        // FacebookSdk.sdkInitialize(getApplicationContext());
    }

    /**
     * This Method is for declareing the id.
     */
    private fun findViews() {
        tv_forgot_pass = findViewById(R.id.textview_forgot_pass)
        et_empid = findViewById(R.id.login_et_empid)
        et_paswd = findViewById(R.id.login_et_paswd)
        //        btnCustomFacebookLogin = findViewById(R.id.facebook_login_custom_button);
//        btnGoogleLogin = findViewById(R.id.sign_in_button);
//        btnLinkInLogin = findViewById(R.id.linkedin_login_custom_button);
//        btnCustomGoogleLogin = findViewById(R.id.google_login_custom_button);
//        btnFacebookLogin = findViewById(R.id.btn_facebook);
        mCenterLayout = findViewById(R.id.linearLayout1)
        btn_login = findViewById(R.id.login_btn_login)
        val text = "<font color=#c8beb2>Forgot Password</font> <font color=#ef9b11> ?</font>"
        tv_forgot_pass!!.setText(Html.fromHtml(text))
        btn_login!!.setOnClickListener(this)
        tv_forgot_pass!!.setOnClickListener(this)
        //        btnCustomFacebookLogin.setOnClickListener(this);
//        btnGoogleLogin.setOnClickListener(this);
//        btnLinkInLogin.setOnClickListener(this);
        tv_forgot_pass!!.setOnClickListener(this)



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
    private fun Login(user_type: String, token: String?) {
        if (Contants2.checkInternetConnection(this@LoginActivity)) {
            contants2!!.showProgressDialog(this@LoginActivity)
            val apiInterface = retrofit!!.create(WebApiInterface::class.java)
            //Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(LoginActivity.this);
            val strEmpId = et_empid!!.text.toString()
            val params: MutableMap<String, String?> = HashMap()
            params["EmpId"] = et_empid!!.text.toString().trim { it <= ' ' }
            params["Password"] = et_paswd!!.text.toString().trim { it <= ' ' }
            params["DeviceId"] = DeviceId
            params["OsType"] = OsType
            params["fcmToken"] = token


            if(ad_login.isChecked){
                params["IsADLogin"] = "true"
            }else{
                params["IsADLogin"] = "false"
            }


            val call = apiInterface.empLogin(strEmpId, params)
            call.enqueue(object : Callback<EmpLogin?> {
                override fun onResponse(call: Call<EmpLogin?>, response: Response<EmpLogin?>) {
                    when (response.code()) {
                        200 -> {
                            contants2!!.dismissProgressDialog()
                            empLogin = response.body()


                            mSharedPref!!.putString(getString(R.string.key_emp_id), et_empid!!.text.toString().trim { it <= ' ' })
                            mSharedPref!!.putString(getString(R.string.key_emp_name), empLogin!!.name)
                            mSharedPref!!.putString(getString(R.string.key_token), empLogin!!.token)
                            mSharedPref!!.putString(getString(R.string.key_fcm_token), token)
                            mSharedPref!!.commit()
                            downloadProfileImage(empLogin!!,empLogin!!.uploadedImage, "base64")
                        }
                        401 -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showToastMessage(this@LoginActivity, getString(R.string.invalid_login_credentials), true)
                        }
                        500 -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showToastMessage(this@LoginActivity, getString(R.string.some_error_occurred), true)
                        }
                        else -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showToastMessage(this@LoginActivity, getString(R.string.some_error_occurred), true)
                        }
                    }
                }

                override fun onFailure(call: Call<EmpLogin?>, t: Throwable) {
                    contants2!!.dismissProgressDialog()
                    Contants2.showToastMessage(this@LoginActivity, getString(R.string.some_error_occurred), true)
                }
            })
        } else {
            Contants2.showToastMessage(this@LoginActivity, getString(R.string.no_internet), true)
        }
    }

    fun downloadProfileImage(emplogin :EmpLogin,base64Data: String?, fileType: String?) {
        if (base64Data != "") {
            val fileDownloader = FileDownloader(this, this)
            fileDownloader.execute(base64Data, Contants2.emp_profile_image, fileType)
            mSharedPref!!.putboolean(getString(R.string.key_is_remove_photo), false)
        } else {
            callLoginScreen(emplogin,true)
            mSharedPref!!.putboolean(getString(R.string.key_is_remove_photo), true)
        }
    }

    override fun onDownloadSuccess(strResult: String?, fileName: String?) {
        this!!.empLogin?.let { callLoginScreen(it,false) }
    }

    override fun onDownloadFailed(strResult: String?) {}
    private fun callLoginScreen(emplogin :EmpLogin,isRemovePhoto: Boolean) {
        if(emplogin.isAdmin!!){
            val intent = Intent(this@LoginActivity, DrawerActivity::class.java)
            intent.putExtra("isRemovePhoto", isRemovePhoto)
            startActivity(intent)
            finishAffinity()
        }else{
            val intent = Intent(this@LoginActivity, DrawerActivity::class.java)
            intent.putExtra("isRemovePhoto", isRemovePhoto)
            startActivity(intent)
            finishAffinity()
        }

    }//   cb_remb_me.setChecked(false);

    //                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//                }
// cb_remb_me.setChecked(true);
    //   cb_remb_me.setChecked(true);
    private val intentData: Unit
        private get() {
            if (mSharedPref!!.getBoolean("isChecked", false)) {
                if (mSharedPref!!.getBoolean("CheckBox", false)) { //                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//                }
// cb_remb_me.setChecked(true);
                    et_empid!!.setText(mSharedPref!!.getString(getString(R.string.key_emp_id), null))
                    et_paswd!!.setText(mSharedPref!!.getString(getString(R.string.key_paswd), null))
                }
                //   cb_remb_me.setChecked(true);
                et_empid!!.setText(mSharedPref!!.getString(getString(R.string.key_emp_id), null))
                et_paswd!!.setText(mSharedPref!!.getString(getString(R.string.key_paswd), null))
            } else { //   cb_remb_me.setChecked(false);
                et_empid!!.setText("", null)
                et_paswd!!.setText("", null)
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
    private fun googleSignInSetUp() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso!!)
                .build()
    }

    private fun generateHashKey() {
        try {
            val info = packageManager.getPackageInfo(
                    "com.intelgain",
                    PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.stackTrace
        } catch (e: NoSuchAlgorithmException) {
            e.stackTrace
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onClick(v: View) {
        when (v.id) {
            R.id.login_btn_login -> if (value == 0) {
                EmpId = et_empid!!.text.toString()
                Password = et_paswd!!.text.toString()
                et_empid!!.error = null
                et_empid!!.error = null
                if (constants!!.isEmptyOrNull(EmpId)) {
                    et_empid!!.error = EMPTY_EMP_ID
                    // contants2.showToastMessage(LoginActivity.this, getString(R.string.fill_valid_details), true);
                } else if (constants!!.isEmptyOrNull(Password)) {
                    et_paswd!!.error = EMPTY_PASWD
                } else {
                    if (mToken!!.isEmpty()) {
                        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult -> mToken = instanceIdResult.token }
                    }
                    Login("empLogin", mToken)
                    /*if (Contants2.checkInternetConnection(LoginActivity.this)) {
                            loginAuth = new LoginAuth();
                            loginAuth.execute(function.getLoginAuth);

                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        }*/
                }
            } else {
                EmpId = et_empid!!.text.toString()
                Password = et_paswd!!.text.toString()
                et_empid!!.error = null
                et_empid!!.error = null
                if (constants!!.isEmptyOrNull(EmpId)) {
                    et_empid!!.error = EMPTY_EMP_ID
                } else if (constants!!.isEmptyOrNull(Password)) {
                    et_paswd!!.error = EMPTY_PASWD
                } else {
                    if (Contants2.checkInternetConnection(this@LoginActivity)) {
                        CustomerLogin = CustomerLoginAuth()
                        CustomerLogin!!.execute(function.getLoginAuth)
                    } else {
                        Toast.makeText(this@LoginActivity, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.textview_forgot_pass -> {
                intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
            else -> {
            }
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
    private fun generateHashkey() {
        try {
            val info = packageManager.getPackageInfo("com.intelgain", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val strHashKey = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                Log.e("HashKey", "" + strHashKey)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("Name not found", e.message, e)
        } catch (e: NoSuchAlgorithmException) {
            Log.e("Error", e.message, e)
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        if (progressBar != null) progressBar!!.dismiss()
        if (result.isSuccess) { // Signed in successfully, show authenticated UI.
            val acct = result.signInAccount
            Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@LoginActivity, "Google Login failed", Toast.LENGTH_SHORT).show()
        }
    }

    ///**************************** Dialog Loader ***************************
    private fun showProgressDialog() {
        progressBar = ProgressDialog(this@LoginActivity)
        progressBar!!.setMessage("Signing ...")
        progressBar!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressBar!!.show()
    }

    @Throws(Exception::class)
    private fun login() {
        user_data = dft!!.LoginAuth("login", EmpId, Password, DeviceId, OsType, this@LoginActivity, 1) as UserData
    }

    @Throws(Exception::class)
    private fun Customerlogin() {
        cust_data = dft!!.CustomerLoginAuth("Login", EmpId, Password, DeviceId, OsType, this@LoginActivity, 1) as CustomerData
    }

    fun setNotification() {
        val calNow = Calendar.getInstance()
        val cal = calNow.clone() as Calendar
        DBirth = mSharedPref!!.getString("emp_birthDate", null)
        Log.d("Date_of_Birth", "Date_of_Birth$DBirth")
        if (DBirth != null) {
            val inputFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            val outputFormat: DateFormat = SimpleDateFormat("dd")
            val outputFormat1: DateFormat = SimpleDateFormat("MM")
            try {
                date_frm = inputFormat.parse(DBirth)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            Birthday = outputFormat.format(date_frm).toInt()
            BirthMonth = outputFormat1.format(date_frm).toInt()
            println("JoiningDateBirthMonth:- $BirthMonth")
            println("JoiningDate:- $Birthday")
        }
        cal[Calendar.DATE] = Birthday
        cal[Calendar.MONTH] = BirthMonth - 1
        cal[Calendar.HOUR_OF_DAY] = 18 //HOUR
        cal[Calendar.MINUTE] = 47 //MIN
        cal[Calendar.SECOND] = 0 //SEC
        if (cal.compareTo(calNow) <= 0) {
        } else {
            val intent = Intent(this@LoginActivity, MyAlarmService::class.java)
            val pendingIntent = PendingIntent.getService(this@LoginActivity, 0, intent, 0)
            val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            am[AlarmManager.RTC_WAKEUP, cal.timeInMillis] = pendingIntent
        }
    }

    enum class function // contains getLoginAuth.
    {
        getLoginAuth
    }

    inner class LoginAuth : AsyncTask<function?, Void?, function>(), DialogInterface.OnCancelListener {
        override fun onPreExecute() {
            super.onPreExecute()
            customize_dialog = CustomLoader_constant.show(this@LoginActivity, "Loading...", true, false, false, this)
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        override fun onPostExecute(result: function) {
            when (result) {
                function.getLoginAuth -> if (user_data!!.user_login_data != null) {
                    if (user_data!!.user_login_data.size != 0) {
                        mSharedPref!!.putString(getString(R.string.key_emp_id), et_empid!!.text.toString().trim { it <= ' ' })
                        mSharedPref!!.putString(getString(R.string.key_emp_name), user_data!!.user_login_data[0].Name)
                        val intent = Intent(this@LoginActivity, DrawerActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
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
                        Toast.makeText(this@LoginActivity, "Please enter correct Empid and Password", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                }
            }
            super.onPostExecute(result)
            if (customize_dialog!!.isShowing) customize_dialog!!.dismiss()
        }

        override fun onCancel(dialog: DialogInterface) {}
        override fun doInBackground(vararg params: function?): function? {
            val result: function? = null
            when (params[0]) {
                function.getLoginAuth -> try {
                    login()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                else -> {
                }
            }
            return params[0]
        }
    }

    inner class CustomerLoginAuth : AsyncTask<function?, Void?, function>(), DialogInterface.OnCancelListener {
        override fun onPreExecute() {
            super.onPreExecute()
            customize_dialog = CustomLoader_constant.show(this@LoginActivity, "Loading...", true, false, false, this)
        }

        override fun doInBackground(vararg params: function?): function? {
            val result: function? = null
            when (params[0]) {
                function.getLoginAuth -> try {
                    Customerlogin()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                else -> {
                }
            }
            return params[0]
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        override fun onPostExecute(result: function) {
            when (result) {
                function.getLoginAuth -> if (cust_data!!.customer_login_data != null) {
                    if (cust_data!!.customer_login_data.size != 0) {
                        mSharedPref!!.putboolean(getString(R.string.key_remember_me), true)
                        if (mSharedPref!!.getBoolean(getString(R.string.key_remember_me), true)) { /*  if (cb_remb_me.isChecked()) {
                                    mSharedPref.putboolean("isChecked", true);
                                    mSharedPref.putString(getString(R.string.key_emp_id), et_empid.getText().toString());
                                    mSharedPref.putString(getString(R.string.key_paswd), et_paswd.getText().toString());


                                } else {
                                    mSharedPref.putboolean("isChecked", false);
                                    mSharedPref.putString(getString(R.string.key_emp_id), et_empid.getText().toString());
                                    mSharedPref.putString(getString(R.string.key_paswd), et_paswd.getText().toString());

                                }*/
                        }
                        val intent = Intent(this@LoginActivity, DrawerActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Please enter correct Empid and Password", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                }
            }
            super.onPostExecute(result)
            if (customize_dialog!!.isShowing) customize_dialog!!.dismiss()
        }

        override fun onCancel(dialog: DialogInterface) {}
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //        callbackManager.onActivityResult(requestCode, resultCode, data);
//        LISessionManager.getInstance(LoginActivity.this)
//                .onActivityResult(this,
//                        requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    } /*
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