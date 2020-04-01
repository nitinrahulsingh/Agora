package com.intelegain.agora.fragmments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.intelegain.agora.R;


public class ContactUsFragment extends Fragment {
    private WebView webView;
    private Button webview_closebutton;
    private Activity activity;
    private final String MSTR_URL_FOR_CONTACT_US = "http://dev.dweb.in/locations/?mobileview=true";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);    // to retain the state of fragment when activity recreates
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.custom_webview, container, false);
        activity = getActivity();

        showContactUsScreen(view);

        return view;
    }

    /** Show Contact us page in web view mode */
    private void showContactUsScreen(View view) {
        //setContentView(R.layout.custom_webview);
        webView = view.findViewById(R.id.webView1);
        webview_closebutton = view.findViewById(R.id.webview_closebutton);
        // temporary we are hiding close button
        webview_closebutton.setVisibility(View.GONE);

        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        // Setting cache enable
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAppCachePath(activity.getCacheDir().getPath());//cash dir path
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webView.loadUrl(MSTR_URL_FOR_CONTACT_US);
        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }

            public void onLoadResource(WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {
                    progressDialog.dismiss();
                    //webview_closebutton.setVisibility(View.VISIBLE);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });


        webview_closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }


}
