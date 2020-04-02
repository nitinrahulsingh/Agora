package com.intelegain.agora.adapter

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.intelegain.agora.R
import com.intelegain.agora.activity.NewAboutUsActivity
import com.intelegain.agora.activity.NewCareerActivity
import com.intelegain.agora.activity.NewContactUsActivity
import com.intelegain.agora.fragmments.New_Home_activity
import com.intelegain.agora.utils.Sharedprefrences

//import com.intelgain.activitys.ContactUsActivity;
/**
 * Created by meenal on 13/7/17.
 */
class HomeAdapter(activity: Activity) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private val mData = arrayOf("ABOUT US", "CONTACT US", "CAREERS", "SIGN IN")
    private val mDataIcon = intArrayOf(R.drawable.new_about_us, R.drawable.new_contact_us, R.drawable.new_careers, R.drawable.new_signup)
    private val mInflater: LayoutInflater
    private val activity: Activity
    var mSharedPref: Sharedprefrences? = null
    // inflates the cell layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.template_home, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the textview in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menu_name = mData[position]
        holder.text_home.text = menu_name
        holder.img_home.setImageResource(mDataIcon[position])
    }

    // total number of cells
    override fun getItemCount(): Int {
        return mData.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val text_home: TextView
        val img_home: ImageView
        private val layout_home: ConstraintLayout
        override fun onClick(view: View) {
            when (view.id) {
                R.id.layout_home -> {
                    val position = adapterPosition
                    when (position) {
                        0 ->  //Toast.makeText(activity, "About Us", Toast.LENGTH_LONG).show();
                            activity.startActivity(Intent(activity, NewAboutUsActivity::class.java))
                        1 ->  //Toast.makeText(activity, "Contact Us", Toast.LENGTH_LONG).show();
// activity.startActivity(new Intent(activity, ContactUsActivity.class));
                            activity.startActivity(Intent(activity, NewContactUsActivity::class.java))
                        2 ->  //Toast.makeText(activity, "Careers", Toast.LENGTH_LONG).show();
                            activity.startActivity(Intent(activity, NewCareerActivity::class.java))
                        3 -> (activity as New_Home_activity).Dologin()
                    }
                }
            }
        }

        init {
            text_home = itemView.findViewById(R.id.text_home)
            img_home = itemView.findViewById(R.id.img_home)
            layout_home = itemView.findViewById(R.id.layout_home)
            itemView.setOnClickListener(this)
        }
    }

    private fun webViewSection(Url: String, type: Int) {
        val webView: WebView
        val webview_closebutton: Button
        activity.setContentView(R.layout.custom_webview)
        webView = activity.findViewById(R.id.webView1)
        webview_closebutton = activity.findViewById(R.id.webview_closebutton)
        webView.settings.useWideViewPort = true
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.loadUrl(Url)
        webView.webViewClient = object : WebViewClient() {
            var progressDialog: ProgressDialog? = null
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                when (type) {
                    0 -> view.loadUrl(url)
                    1 -> view.loadUrl(url)
                    2 -> if (url.startsWith("mailto:")) {
                        val i = Intent(Intent.ACTION_SENDTO, Uri.parse(url))
                        activity.startActivity(i)
                    } else {
                        view.loadUrl(url)
                    }
                    3 -> {
                    }
                    else -> {
                    }
                }
                return true
            }

            override fun onLoadResource(view: WebView, url: String) {
                if (progressDialog == null) { // in standard case YourActivity.this
                    progressDialog = ProgressDialog(activity)
                    progressDialog!!.setMessage("Loading...")
                    progressDialog!!.show()
                    progressDialog!!.setCancelable(false)
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                try {
                    progressDialog!!.dismiss()
                    webview_closebutton.visibility = View.VISIBLE
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        }
        webview_closebutton.setOnClickListener {
            val `in` = Intent(activity, New_Home_activity::class.java)
            activity.startActivity(`in`)
            activity.finish()
        }
    }

    // convenience method for getting data at click position
    fun getItem(id: Int): String {
        return mData[id]
    }

    // data is passed into the constructor
    init {
        mInflater = LayoutInflater.from(activity)
        this.activity = activity
    }
}