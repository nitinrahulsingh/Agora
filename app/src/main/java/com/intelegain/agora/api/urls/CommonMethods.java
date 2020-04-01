package com.intelegain.agora.api.urls;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.intelegain.agora.Controller.VerticalSpaceItemDecoration;
import com.intelegain.agora.R;
import com.intelegain.agora.activity.LoginActivity;
import com.intelegain.agora.common.Utilities;
import com.intelegain.agora.interfeces.RecyclerItemClickListener;

import java.util.List;

public class CommonMethods implements RecyclerItemClickListener, TextWatcher {

    public static String no_internet = "Please check Internet Connectivity";
    private CommonRecyclerAdapter commonRecyclerAdapter;
    private String spinnerText;

    protected Utilities mUtilities = Utilities.getInstance();
    public static ProgressDialog progress_dialog;

    /**
     * This method is used to check whether string is null or empty
     *
     * @param str Set the str to check
     * @return true if null or empty otherwise false
     */
    public final boolean isNullorEmpty(String str) {
        return mUtilities.isEmptyOrNull(str);
    }

    /**
     * This method is used to Showing Progress Dialog setCancelable is false
     * Message Loading ....
     *
     * @return true if null or empty otherwise false
     */
    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog progress_dialog1 = new ProgressDialog(context);
        progress_dialog1.setCancelable(false);
        progress_dialog1.setMessage("Loading .... ");
        return progress_dialog1;
    }

    /**getempprofile
     * This method is used for checking internet connection if connection
     * available return true else return false
     */

    public static boolean checkInternetConnection(Context _activity) {
        ConnectivityManager conMgr = (ConnectivityManager) _activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable())
            return true;
        else
            return false;
    }

    public void callLogin(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public void customSpinner(Context mContext, String title, LayoutInflater inflater, RecyclerView recyclerView, List<String>
            dataInfo, Dialog dialog, View dialogView, RecyclerItemClickListener itemClickListener) {

        dialogView = inflater.inflate(R.layout.dialog_select_items, null);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(dialogView);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        recyclerView = dialogView.findViewById(R.id.recyclerview_select_item);
        EditText edtDialogSearchItem = dialogView.findViewById(R.id.task_manager_dialog_select_project);
        TextView txt_heading = dialogView.findViewById(R.id.txt_heading);
        txt_heading.setText(title);
        edtDialogSearchItem.addTextChangedListener(this);

        // recyclerview
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,
                false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext,
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(10));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));


        commonRecyclerAdapter = new CommonRecyclerAdapter(mContext, dataInfo, itemClickListener);
        recyclerView.setAdapter(commonRecyclerAdapter);


    }


    ///********************* Text Watcher *************************

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        commonRecyclerAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    ///********************* Recycler view click *************************

    @Override
    public void recyclerViewListClicked(int position, String itemClickText) {

        this.spinnerText = itemClickText;
    }
}
