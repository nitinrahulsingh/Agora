package com.intelegain.agora.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.intelegain.agora.R;
import com.intelegain.agora.activity.NewAboutUsActivity;
import com.intelegain.agora.activity.NewCareerActivity;
import com.intelegain.agora.activity.NewContactUsActivity;
import com.intelegain.agora.fragmments.New_Home_activity;
import com.intelegain.agora.utils.Sharedprefrences;


//import com.intelgain.activitys.ContactUsActivity;

/**
 * Created by meenal on 13/7/17.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private String[] mData = {"ABOUT US", "CONTACT US", "CAREERS", "SIGN IN"};
    private int[] mDataIcon = {R.drawable.new_about_us, R.drawable.new_contact_us, R.drawable.new_careers, R.drawable.new_signup};
    private LayoutInflater mInflater;
    private Activity activity;


    public Sharedprefrences mSharedPref;


    // data is passed into the constructor
    public HomeAdapter(Activity activity) {
        this.mInflater = LayoutInflater.from(activity);
        this.activity = activity;

    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.template_home, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String menu_name = mData[position];
        holder.text_home.setText(menu_name);
        holder.img_home.setImageResource(mDataIcon[position]);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView text_home;
        private ImageView img_home;
        private ConstraintLayout layout_home;


        public ViewHolder(View itemView) {
            super(itemView);
            text_home = itemView.findViewById(R.id.text_home);
            img_home = itemView.findViewById(R.id.img_home);
            layout_home = itemView.findViewById(R.id.layout_home);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.layout_home:
                    int position = getAdapterPosition();

                    switch (position) {
                        case 0:
                            //Toast.makeText(activity, "About Us", Toast.LENGTH_LONG).show();
                            activity.startActivity(new Intent(activity, NewAboutUsActivity.class));
                            break;
                        case 1:
                            //Toast.makeText(activity, "Contact Us", Toast.LENGTH_LONG).show();
                            // activity.startActivity(new Intent(activity, ContactUsActivity.class));
                            activity.startActivity(new Intent(activity, NewContactUsActivity.class));
                            break;
                        case 2:
                            //Toast.makeText(activity, "Careers", Toast.LENGTH_LONG).show();
                            activity.startActivity(new Intent(activity, NewCareerActivity.class));
                            break;
                        case 3:
                            ((New_Home_activity) activity).Dologin();
                            break;
                    }


            }

        }
    }


    private void webViewSection(String Url, final int type) {
        WebView webView;
        final Button webview_closebutton;

        activity.setContentView(R.layout.custom_webview);
        webView = activity.findViewById(R.id.webView1);
        webview_closebutton = activity.findViewById(R.id.webview_closebutton);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(Url);
        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                switch (type) {
                    case 0:
                        view.loadUrl(url);
                        break;
                    case 1:
                        view.loadUrl(url);
                        break;
                    case 2:
                        if (url.startsWith("mailto:")) {
                            Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                            activity.startActivity(i);
                        } else {
                            view.loadUrl(url);
                        }
                        break;
                    case 3:
                        //Dologin();
                        break;
                    default:
                        break;
                }

                return true;
            }

            public void onLoadResource(WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {
                    progressDialog.dismiss();
                    webview_closebutton.setVisibility(View.VISIBLE);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });


        webview_closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(activity, New_Home_activity.class);
                activity.startActivity(in);
                activity.finish();

            }
        });

    }


    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData[id];
    }


}





