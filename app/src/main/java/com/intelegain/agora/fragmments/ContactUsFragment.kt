package com.intelegain.agora.fragmments

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.fragment.app.Fragment
import com.intelegain.agora.R

class ContactUsFragment : Fragment() {
    private lateinit var webView: WebView
    private lateinit var webview_closebutton: Button
    private var activity: Activity? = null
    private val MSTR_URL_FOR_CONTACT_US = "http://dev.dweb.in/locations/?mobileview=true"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true // to retain the state of fragment when activity recreates
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.custom_webview, container, false)
        activity = getActivity()
        showContactUsScreen(view)
        return view
    }

    /** Show Contact us page in web view mode  */
    private fun showContactUsScreen(view: View) { //setContentView(R.layout.custom_webview);
        webView = view.findViewById(R.id.webView1)
        webview_closebutton = view.findViewById(R.id.webview_closebutton)
        // temporary we are hiding close button
        webview_closebutton.setVisibility(View.GONE)
        webView.getSettings().useWideViewPort = true
        webView.getSettings().javaScriptEnabled = true
        webView.getSettings().domStorageEnabled = true
        // Setting cache enable
        webView.getSettings().setAppCacheEnabled(true)
        webView.getSettings().setAppCachePath(activity!!.cacheDir.path) //cash dir path
        webView.getSettings().cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webView.loadUrl(MSTR_URL_FOR_CONTACT_US)
        webView.setWebViewClient(object : WebViewClient() {
            var progressDialog: ProgressDialog? = null
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onLoadResource(view: WebView, url: String) {
                if (progressDialog == null) { // in standard case YourActivity.this
                    progressDialog = ProgressDialog(activity)
                    progressDialog!!.setMessage("Loading...")
                    progressDialog!!.setCanceledOnTouchOutside(false)
                    progressDialog!!.show()
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                try {
                    progressDialog!!.dismiss()
                    //webview_closebutton.setVisibility(View.VISIBLE);
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        })
        webview_closebutton.setOnClickListener(View.OnClickListener { activity!!.finish() })
    }
}