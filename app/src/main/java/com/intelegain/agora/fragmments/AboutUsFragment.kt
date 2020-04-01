package com.intelegain.agora.fragmments

import android.app.Activity
import android.app.ProgressDialog
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.fragment.app.Fragment
import com.intelegain.agora.R

class AboutUsFragment : Fragment() {
    private lateinit var webView: WebView
    private lateinit var webview_closebutton: Button
    private var activity: Activity? = null
    private val MSTR_URL_FOR_ABOUT_US = "http://dev.dweb.in/portfolio/?mobileview=true"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true // to retain the state of fragment when activity recreates
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.custom_webview, container, false)
        activity = getActivity()
        showAboutUsScreen(view)
        return view
    }

    /** Show About us page in web view mode  */
    private fun showAboutUsScreen(view: View) { //setContentView(R.layout.custom_webview);
        webView = view.findViewById(R.id.webView1)
        webview_closebutton = view.findViewById(R.id.webview_closebutton)
        webview_closebutton.visibility = GONE

        webView.getSettings().useWideViewPort = true
        webView.getSettings().javaScriptEnabled = true
        webView.getSettings().domStorageEnabled = true
        webView.loadUrl(MSTR_URL_FOR_ABOUT_US)
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