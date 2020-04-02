package com.intelegain.agora.activity

import android.annotation.TargetApi
import android.app.ActivityOptions
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.intelegain.agora.R
import com.intelegain.agora.api.urls.CommonMethods
import com.intelegain.agora.api.urls.CommonMethods.Companion.checkInternetConnection
import com.intelegain.agora.fragmments.CustomLoader_constant
import com.intelegain.agora.fragmments.New_Home_activity
import com.intelegain.agora.fragmments.SettingsActivity
import com.intelegain.agora.model.ChangePasswordDetails
import com.intelegain.agora.service.DataFetchServices
import com.intelegain.agora.utils.Contants2
import com.intelegain.agora.utils.Sharedprefrences
import com.intelegain.agora.utils.Sharedprefrences.Companion.getInstance

class ChangePasswordActivity : AppCompatActivity(), View.OnClickListener {
    var chng_passwrd_title_text: TextView? = null
    var text_userid: TextView? = null
    var text_oldpass: TextView? = null
    var text_new_pass: TextView? = null
    var text_confirm_pass: TextView? = null
    var ed_userid: EditText? = null
    var ed_oldpass: EditText? = null
    var ed_new_pass: EditText? = null
    var ed_confirm_pass: EditText? = null
    var back_img_chng_passwrd: ImageView? = null
    var lay_back_img_chng_passwrd: LinearLayout? = null
    var customize_dialog: CustomLoader_constant? = null
    var bt_save: Button? = null
    private var EmpId: String? = ""
    private var NewPassword = ""
    private var OldPassword = ""
    private var ConfirmPassword = ""
    private var dft: DataFetchServices? = null
    private val mCommonMethods: CommonMethods? = null
    var mSharedPref: Sharedprefrences? = null

    enum class function {
        getLeaveType, applyForLeaves, getNewPassword
    }

    private val EMPTY_EMP_ID = "Enter Employee ID" // EMPTY_EMP_ID is a
    // Error Message when
// Validation Failed.
    private val EMPTY_OLD_PASSWORD = "Enter Old Password"
    private val EMPTY_NEW_PASSWORD = "Enter New Password"
    private var changePass: ChangePass? = null
    private var user_data: ChangePasswordDetails? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        init()
        findView()
        sharedpreferences()
    }

    private fun sharedpreferences() {
        mSharedPref = getInstance(applicationContext)
        EmpId = mSharedPref!!.getString("emp_Id", null)
        ed_userid!!.setText(EmpId)
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(applicationContext, DrawerActivity::class.java)
        if (Build.VERSION.SDK_INT >= 16) {
            val bundle = ActivityOptions.makeCustomAnimation(applicationContext, R.anim.slide_in_right, R.anim.slide_out_right).toBundle()
            startActivity(intent, bundle)
            finish()
        } else {
            startActivity(intent)
            finish()
        }
    }

    /**
     * This Method is for initialization
     */
    private fun init() {
        dft = DataFetchServices()
    }

    /**
     * This Method is for declareing the id.
     */
    private fun findView() {
        chng_passwrd_title_text = findViewById<View>(R.id.chng_passwrd_title_text) as TextView
        text_userid = findViewById<View>(R.id.text_userid) as TextView
        text_oldpass = findViewById<View>(R.id.text_oldpass) as TextView
        text_new_pass = findViewById<View>(R.id.text_new_pass) as TextView
        text_confirm_pass = findViewById<View>(R.id.text_confirm_pass) as TextView
        lay_back_img_chng_passwrd = findViewById<View>(R.id.lay_back_img_chng_passwrd) as LinearLayout
        back_img_chng_passwrd = findViewById<View>(R.id.back_img_chng_passwrd) as ImageView
        bt_save = findViewById<View>(R.id.bt_save) as Button
        ed_userid = findViewById<View>(R.id.ed_userid) as EditText
        ed_oldpass = findViewById<View>(R.id.ed_oldpass) as EditText
        ed_confirm_pass = findViewById<View>(R.id.ed_confirm_pass) as EditText
        ed_new_pass = findViewById<View>(R.id.ed_new_pass) as EditText
        bt_save!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, 48f)
        val font1 = Typeface.createFromAsset(assets, "fonts/segoeui.ttf")
        chng_passwrd_title_text!!.typeface = font1
        text_userid!!.typeface = font1
        text_oldpass!!.typeface = font1
        text_new_pass!!.typeface = font1
        ed_new_pass!!.typeface = font1
        val font = Typeface.createFromAsset(assets, "fonts/segoeuil.ttf")
        ed_userid!!.typeface = font
        ed_oldpass!!.typeface = font
        ed_confirm_pass!!.typeface = font
        ed_new_pass!!.typeface = font
        bt_save!!.typeface = font
        chng_passwrd_title_text!!.typeface = font
        lay_back_img_chng_passwrd!!.setOnClickListener(this)
        bt_save!!.setOnClickListener(this)
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onClick(v: View) {
        when (v.id) {
            R.id.lay_back_img_chng_passwrd -> {
                val intent = Intent(this@ChangePasswordActivity, SettingsActivity::class.java)
                if (Build.VERSION.SDK_INT >= 16) {
                    val bundle = ActivityOptions.makeCustomAnimation(applicationContext, R.anim.slide_in_right, R.anim.slide_out_right).toBundle()
                    startActivity(intent, bundle)
                } else {
                    startActivity(intent)
                }
                finish()
            }
            R.id.bt_save -> {
                OldPassword = ed_oldpass!!.text.toString()
                NewPassword = ed_new_pass!!.text.toString()
                ConfirmPassword = ed_confirm_pass!!.text.toString()
                if (EmpId == "") {
                    Toast.makeText(applicationContext, "Please Enter EmpId.", Toast.LENGTH_SHORT).show()
                } else if (OldPassword == "") {
                    Toast.makeText(applicationContext, Contants2.Old_Password, Toast.LENGTH_SHORT).show()
                } else if (NewPassword == "") {
                    Toast.makeText(applicationContext, "Please enter new password", Toast.LENGTH_SHORT).show()
                } else if (OldPassword.equals(NewPassword, ignoreCase = true)) {
                    Toast.makeText(applicationContext, "Old password and new password should not be same", Toast.LENGTH_SHORT).show()
                } else if (!NewPassword.equals(ConfirmPassword, ignoreCase = true)) {
                    Toast.makeText(applicationContext, "New password and confirm password should not be same", Toast.LENGTH_SHORT).show()
                } else {
                    if (checkInternetConnection(this@ChangePasswordActivity)) {
                        changePass = ChangePass()
                        changePass!!.execute(function.getNewPassword)
                    } else {
                        Toast.makeText(this@ChangePasswordActivity, CommonMethods.no_internet, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    inner class ChangePass : AsyncTask<function?, Void?, function>(), DialogInterface.OnCancelListener {

        @Throws(Exception::class)
        private fun changePass(methodname: String) {
            user_data = dft!!.ChangePass(methodname, EmpId, ed_oldpass!!.text.toString(), ed_new_pass!!.text.toString(), ed_confirm_pass!!.text.toString(), this@ChangePasswordActivity, 1) as ChangePasswordDetails
        }

        override fun onPreExecute() {
            super.onPreExecute()
            customize_dialog = CustomLoader_constant.show(this@ChangePasswordActivity, "Loading...", true, false, false, this)
        }

        override fun onPostExecute(result: function) {
            when (result) {
                function.getNewPassword -> if (user_data != null) {
                    if (user_data!!.Status == 1) {
                        Toast.makeText(this@ChangePasswordActivity, user_data!!.Message, Toast.LENGTH_SHORT).show()
                        val i = Intent(this@ChangePasswordActivity, SettingsActivity::class.java)
                        startActivity(i)
                    } else {
                        Toast.makeText(this@ChangePasswordActivity, user_data!!.Message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val dialog = Dialog(this@ChangePasswordActivity)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setContentView(R.layout.access_denied)
                    val text = dialog.findViewById<View>(R.id.txtDiaTitle) as TextView
                    val img = dialog.findViewById<View>(R.id.txtDiaMsg) as TextView
                    val dialogButton = dialog.findViewById<View>(R.id.btnOk) as Button
                    dialogButton.setOnClickListener {
                        val Logout = Intent(this@ChangePasswordActivity, New_Home_activity::class.java)
                        startActivity(Logout)
                        mSharedPref!!.clear()
                        finish()
                    }
                    dialog.show()
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
                function.getNewPassword -> try {
                    changePass("changePassword")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                else -> {
                }
            }
            return params[0]
        }
    }
}