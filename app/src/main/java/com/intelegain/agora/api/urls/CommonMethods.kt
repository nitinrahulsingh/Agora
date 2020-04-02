package com.intelegain.agora.api.urls

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intelegain.agora.Controller.VerticalSpaceItemDecoration
import com.intelegain.agora.R
import com.intelegain.agora.activity.LoginActivity
import com.intelegain.agora.common.Utilities
import com.intelegain.agora.interfeces.RecyclerItemClickListener

class CommonMethods : RecyclerItemClickListener, TextWatcher {
    private var commonRecyclerAdapter: CommonRecyclerAdapter? = null
    private var spinnerText: String? = null
    protected var mUtilities = Utilities.instance
    /**
     * This method is used to check whether string is null or empty
     *
     * @param str Set the str to check
     * @return true if null or empty otherwise false
     */
    fun isNullorEmpty(str: String?): Boolean {
        return mUtilities.isEmptyOrNull(str!!)
    }

    fun callLogin(context: Context) {
        val i = Intent(context, LoginActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)
    }

    fun customSpinner(mContext: Context?, title: String?, inflater: LayoutInflater, recyclerView: RecyclerView, dataInfo: List<String?>?, dialog: Dialog, dialogView: View, itemClickListener: RecyclerItemClickListener?) {
        var recyclerView = recyclerView
        var dialogView = dialogView
        dialogView = inflater.inflate(R.layout.dialog_select_items, null)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(dialogView)
        dialog.show()
        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = lp
        recyclerView = dialogView.findViewById(R.id.recyclerview_select_item)
        val edtDialogSearchItem = dialogView.findViewById<EditText>(R.id.task_manager_dialog_select_project)
        val txt_heading = dialogView.findViewById<TextView>(R.id.txt_heading)
        txt_heading.text = title
        edtDialogSearchItem.addTextChangedListener(this)
        // recyclerview
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,
                false)
        val dividerItemDecoration = DividerItemDecoration(mContext,
                LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(VerticalSpaceItemDecoration(10))
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        commonRecyclerAdapter = CommonRecyclerAdapter(mContext, dataInfo, itemClickListener)
        recyclerView.adapter = commonRecyclerAdapter
    }

    ///********************* Text Watcher *************************
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        commonRecyclerAdapter!!.filter.filter(s)
    }

    override fun afterTextChanged(s: Editable) {}
    ///********************* Recycler view click *************************
    override fun recyclerViewListClicked(position: Int, itemClickText: String?) {
        spinnerText = itemClickText
    }

    companion object {
        @JvmField
        var no_internet = "Please check Internet Connectivity"
        var progress_dialog: ProgressDialog? = null
        /**
         * This method is used to Showing Progress Dialog setCancelable is false
         * Message Loading ....
         *
         * @return true if null or empty otherwise false
         */
        fun getProgressDialog(context: Context?): ProgressDialog {
            val progress_dialog1 = ProgressDialog(context)
            progress_dialog1.setCancelable(false)
            progress_dialog1.setMessage("Loading .... ")
            return progress_dialog1
        }

        /**getempprofile
         * This method is used for checking internet connection if connection
         * available return true else return false
         */
        @JvmStatic
        fun checkInternetConnection(_activity: Context): Boolean {
            val conMgr = _activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (conMgr.activeNetworkInfo != null && conMgr.activeNetworkInfo.isAvailable) true else false
        }
    }
}