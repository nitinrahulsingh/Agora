package com.intelegain.agora.activity

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.intelegain.agora.R
import com.intelegain.agora.api.urls.CommonMethods
import com.intelegain.agora.api.urls.CommonMethods.Companion.checkInternetConnection
import com.intelegain.agora.fragmments.CustomLoader_constant
import com.intelegain.agora.model.ForgotPasswordDetails
import com.intelegain.agora.service.DataFetchServices
import com.intelegain.agora.utils.Contants2
import com.intelegain.agora.utils.Sharedprefrences
import kotlinx.android.synthetic.main.activity_forgot_password.*


class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {
    enum class function {
        forgot_passwrd
    }

    var email : String = ""
    var username : String = ""
    private var user_data : ForgotPasswordDetails? = null
    var forgotPass // AsyncTask
            : ForgotPass? = null
    private var dft // This class includes HTTp Connection call
            : DataFetchServices? = null
    // and Object LoginAuth.
    private var EmpId = ""
    var mSharedPref: Sharedprefrences? = null
    var EmailID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        findviews()
        init()
    }

    private fun init() {
        dft = DataFetchServices()
    }

    private fun findviews() {
        val font: Typeface = Typeface.createFromAsset(assets, "fonts/segoeui.ttf")
        txt_forgot_pass.setTypeface(font)
        txt_forgot_pass_desc.setTypeface(font)
        val font1: Typeface = Typeface.createFromAsset(assets, "fonts/segoeuil.ttf")
        btn_submit.setTypeface(font1)
        btn_cancel.setTypeface(font1)
        btn_cancel.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
        ed_email.setOnClickListener(this)
        ed_email.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
        usename_edittext.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_submit -> {
                email = ed_email.text.toString()
                username = usename_edittext.getText().toString()
                if (username == "" && email == "") {
                    Toast.makeText(this@ForgotPasswordActivity, "Please enter both the fields", Toast.LENGTH_SHORT).show()
                } else if (username == "") {
                    Toast.makeText(this@ForgotPasswordActivity, "Please enter user id", Toast.LENGTH_SHORT).show()
                } else if (!Contants2.isValidEmail(email)) {
                    Toast.makeText(this@ForgotPasswordActivity, "Please enter the e-mail id", Toast.LENGTH_SHORT).show()
                } else {
                    if (checkInternetConnection(this@ForgotPasswordActivity)) {
                        forgotPass = ForgotPass(this@ForgotPasswordActivity)
                        forgotPass!!.execute(function.forgot_passwrd)
                    } else {
                        Toast.makeText(this@ForgotPasswordActivity, CommonMethods.no_internet, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.btn_cancel -> {
//                val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
//                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
            }
            else -> {
            }
        }
    }

    companion object {
        class ForgotPass(private var activity: ForgotPasswordActivity?) : AsyncTask<function?, Void?, function?>(), DialogInterface.OnCancelListener {
            private var user_data : ForgotPasswordDetails? = null
            var dft : DataFetchServices =  DataFetchServices()
            var customize_dialog: CustomLoader_constant? = null
            override fun doInBackground(vararg params: function?): function? {
                val result: function? = null
                when (params[0]) {
                    function.forgot_passwrd -> try {
                        forgotpass("forgotPassword")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    else -> {
                    }
                }
                return params[0]
            }

            @Throws(Exception::class)
            private fun forgotpass(methodname: String) {
                user_data = dft.ForgotPass(methodname, activity!!.usename_edittext.getText().toString(), activity!!.ed_email.getText().toString(), activity) as ForgotPasswordDetails
            }

            override fun onPreExecute() {
                super.onPreExecute()
                customize_dialog = CustomLoader_constant.show(activity, "Loading...", true, false, false, this)
            }

            override fun onPostExecute(result: function?) {
                when (result) {
                    function.forgot_passwrd -> if (user_data!!.Status == 1) {
                        Toast.makeText(activity, user_data!!.Message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity, LoginActivity::class.java)
                        activity!!.startActivity(intent)
                    } else {
                        Toast.makeText(activity, user_data!!.Message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                    }
                }
                super.onPostExecute(result)
                if (customize_dialog!!.isShowing()) customize_dialog!!.dismiss()
            }

            override fun onCancel(dialog: DialogInterface?) {}
        }
    }

}
