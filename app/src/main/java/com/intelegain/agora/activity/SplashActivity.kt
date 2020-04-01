package com.intelegain.agora.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.intelegain.agora.R
import com.intelegain.agora.fragmments.New_Home_activity
import com.intelegain.agora.utils.Sharedprefrences

class SplashActivity : AppCompatActivity() {
    /* var sessionManager: SessionManager? = null
    private var mContext: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this
        sessionManager = SessionManager(mContext as MainActivity)
        /* if(sessionManager.getTOKEN()!=null)
        {
            MyUtils.showToast(mContext,sessionManager.getUserInfo().get("Name"));
        }
        else
        {

        }
*/
    }*/

    private var mContext: Context? = null
    /* if (!Sharedprefrences.getInstance(SplashScreenActivity.this).getString(getString(R.string.key_emp_id), "").equalsIgnoreCase("")) {
                        Intent intent = new Intent(SplashScreenActivity.this, DrawerActivity.class);
                        Intent inData = getIntent();
                        if (inData.hasExtra("isFromNotification")) {
                            intent.putExtra("menuFragment", "NotificationFragment");
                        }
                        startActivity(intent);
                        finish();  //Go directly to drawer activity.
                    } else {

                        startActivity(new Intent(SplashScreenActivity.this, New_Home_activity.class));
                        finish();
                    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mContext = this

        Handler().postDelayed({


            if (!Sharedprefrences.getInstance(mContext as SplashActivity).getString(getString(R.string.key_emp_id), "")!!.isEmpty()) {
                val intent = Intent(mContext as SplashActivity, DrawerActivity::class.java)
                val inData = getIntent()
                if (inData.hasExtra("isFromNotification")) {
                    intent.putExtra("menuFragment", "NotificationFragment")
                }
                startActivity(intent)
                finish() //Go directly to drawer activity.
            } else {
                startActivity(Intent(mContext as SplashActivity, New_Home_activity::class.java))
                finish()
            }


            /*

               if (sessionManager!!.isUserLogin)
               {
                   val intent = Intent(this, DrawerActivity::class.java)
                   val inData = getIntent()
                   if (inData.hasExtra("isFromNotification")) {
                       intent.putExtra("menuFragment", "NotificationFragment")
                   }
                   startActivity(intent)
                   finish()
               } else {
                   val intent = Intent(this, New_Home_activity::class.java)
                   startActivity(intent)
                   finish()
               }*/
        }, 2000)


    } /*
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);


    }
    */
}