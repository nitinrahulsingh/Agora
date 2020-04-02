package com.intelegain.agora.fragmments

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.intelegain.agora.R
import com.intelegain.agora.adapter.Contacts_recycler_view_adapter
import com.intelegain.agora.api.urls.RetrofitClient
import com.intelegain.agora.constants.Constants
import com.intelegain.agora.interfeces.WebApiInterface
import com.intelegain.agora.model.Employee_contacts
import com.intelegain.agora.utils.Contants2
import com.intelegain.agora.utils.Sharedprefrences.Companion.getInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*

class ContactsFragment : Fragment(), View.OnClickListener, OnRefreshListener {

    var recyclerView_contacts: RecyclerView? = null
    var contactsRecyclerViewAdapter: Contacts_recycler_view_adapter? = null
    var empContactsList: ArrayList<Employee_contacts>? = null
    var appCompatTextView: AppCompatTextView? = null
    var imgBoardMail: ImageView? = null
    var imgBoardPhone: ImageView? = null
    var imgHrMail: ImageView? = null
    var imgHrPhone: ImageView? = null
    var imgAccountsMail: ImageView? = null
    var imgAccountsPhone: ImageView? = null
    var img_filter: ImageView? = null
    var editTextSearch: EditText? = null
    var tv_try_again: TextView? = null
    var progressBar: ProgressBar? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    var intentToCall: Intent? = null
    var intentToEmail: Intent? = null
    var retrofitClient: Retrofit? = null
    var contants2: Contants2? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        findViews(view)
        if (Contants2.checkInternetConnection(activity)) companyContacts else {
            Contants2.showToastMessage(activity, getString(R.string.no_internet), true)
            hideRecyclerView(true)
        }
        //generateDemoList();
        setListenersForViews()
    }

    private fun initialize() {
        empContactsList = ArrayList()
        retrofitClient = RetrofitClient.getInstance(Constants.BASE_URL)
        contants2 = Contants2()
    }

    private fun findViews(view:View) {
        swipeRefreshLayout = view!!.findViewById(R.id.refreshlayout)
        img_filter = view!!.findViewById(R.id.img_filter)
        editTextSearch = view!!.findViewById(R.id.etSearchByName)
        imgBoardMail = view!!.findViewById(R.id.img_board_mail)
        imgBoardPhone = view!!.findViewById(R.id.img_board_call)
        imgHrMail = view!!.findViewById(R.id.img_hr_mail)
        imgHrPhone = view!!.findViewById(R.id.img_hr_call)
        imgAccountsMail = view!!.findViewById(R.id.img_accounts_mail)
        imgAccountsPhone = view!!.findViewById(R.id.img_accounts_call)
        progressBar = view!!.findViewById(R.id.progress_bar_contacts)
        recyclerView_contacts = view!!.findViewById(R.id.recycler_contacts)
        tv_try_again = view!!.findViewById(R.id.text_view_try_again)
    }

    // progressBar.setVisibility(View.GONE);
    private val companyContacts: Unit
        private get() {
            contants2!!.showProgressDialog(activity)
            swipeRefreshLayout!!.isRefreshing = false
            val apiInterface = RetrofitClient.retrofit.create(WebApiInterface::class.java)
            val mSharedPrefs = getInstance(activity)
            val strToken = mSharedPrefs.getString("Token", "")
            val strEmpId = mSharedPrefs.getString("emp_Id", "")
            val params: MutableMap<String, String?> = HashMap()
            params["EmpId"] = strEmpId
            val call = apiInterface.getAllEmployeeContacts(strEmpId, strToken, params)
            call.enqueue(object : Callback<List<Employee_contacts>?> {
                override fun onResponse(call: Call<List<Employee_contacts>?>, response: Response<List<Employee_contacts>?>) {
                    when (response.code()) {
                        200 -> {
                            swipeRefreshLayout!!.isRefreshing = false
                            if (empContactsList != null && empContactsList!!.size > 0) empContactsList!!.clear()
                            if (response.body() != null && response.body()!!.size > 0) {
                                val iListSize = response.body()!!.size
                                var index = 0
                                while (index < iListSize) {
                                    when (index) {
                                        0 -> {
                                            imgBoardMail!!.tag = if (TextUtils.isEmpty(response.body()!![index].empEmail)) "" else response.body()!![index].empEmail
                                            imgBoardPhone!!.tag = if (TextUtils.isEmpty(response.body()!![index].empContact)) "" else response.body()!![index].empContact
                                        }
                                        1 -> {
                                            imgHrMail!!.tag = if (TextUtils.isEmpty(response.body()!![index].empEmail)) "" else response.body()!![index].empEmail
                                            imgHrPhone!!.tag = if (TextUtils.isEmpty(response.body()!![index].empContact)) "" else response.body()!![index].empContact
                                        }
                                        2 -> {
                                            imgAccountsMail!!.tag = if (TextUtils.isEmpty(response.body()!![index].empEmail)) "" else response.body()!![index].empEmail
                                            imgAccountsPhone!!.tag = if (TextUtils.isEmpty(response.body()!![index].empContact)) "" else response.body()!![index].empContact
                                        }
                                        else -> empContactsList!!.add(response.body()!![index])
                                    }
                                    index++
                                }
                                Handler().postDelayed({
                                    setUpRecyclerView()
                                    hideRecyclerView(false)
                                    // progressBar.setVisibility(View.GONE);
                                }, 100)
                            } else {
                                Contants2.showToastMessageAtCenter(activity, getString(R.string.no_data_found), true)
                                hideRecyclerView(true)
                            }
                            contants2!!.dismissProgressDialog()
                        }
                        403 -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showOkAlertDialog(activity, response.message(), "") { dialog, which -> Contants2.forceLogout(activity) }
                        }
                        500 -> {
                            contants2!!.dismissProgressDialog()
                            hideRecyclerView(true)
                            Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true)
                        }
                        else -> {
                            hideRecyclerView(true)
                            contants2!!.dismissProgressDialog()
                            Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Employee_contacts>?>, t: Throwable) {
                    call.cancel()
                    contants2!!.dismissProgressDialog()
                    swipeRefreshLayout!!.isRefreshing = false
                    hideRecyclerView(true)
                    Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true)
                }
            })
        }

    private fun hideRecyclerView(hide: Boolean) {
        if (hide) {
            recyclerView_contacts!!.visibility = View.GONE
            tv_try_again!!.visibility = View.VISIBLE
        } else {
            tv_try_again!!.visibility = View.GONE
            recyclerView_contacts!!.visibility = View.VISIBLE
        }
    }

    private fun setUpRecyclerView() { // recycler
        recyclerView_contacts!!.setHasFixedSize(true)
        recyclerView_contacts!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,
                false)
        val dividerItemDecoration = DividerItemDecoration(recyclerView_contacts!!.context,
                LinearLayoutManager.VERTICAL)
        recyclerView_contacts!!.addItemDecoration(dividerItemDecoration)
        recyclerView_contacts!!.itemAnimator = DefaultItemAnimator()
        recyclerView_contacts!!.layoutManager = LinearLayoutManager(activity)
        contactsRecyclerViewAdapter = Contacts_recycler_view_adapter(activity, empContactsList)
        recyclerView_contacts!!.adapter = contactsRecyclerViewAdapter
        setUpItemTouchHelper()
    }

    private fun setListenersForViews() {
        swipeRefreshLayout!!.setOnRefreshListener(this)
        img_filter!!.setOnClickListener(this)
        editTextSearch!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { //mTaskManagerAdapter.getFilter().filter(s);
                contactsRecyclerViewAdapter!!.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })
        imgBoardMail!!.setOnClickListener(this)
        imgBoardPhone!!.setOnClickListener(this)
        imgHrMail!!.setOnClickListener(this)
        imgHrPhone!!.setOnClickListener(this)
        imgAccountsMail!!.setOnClickListener(this)
        imgAccountsPhone!!.setOnClickListener(this)
        tv_try_again!!.setOnClickListener(this)
    }

    /**
     * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method
     * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view
     * background will be visible. That is rarely an desired effect.
     */
    private fun setUpItemTouchHelper() {
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            var background: Drawable? = null
            var xMark: Drawable? = null
            var xMarkMargin = 0
            var initiated = false
            // LinearLayout.LayoutParams layoutParams;
            private fun init() {
                background = ColorDrawable(ContextCompat.getColor(activity!!, R.color.yellow))
                xMark = ContextCompat.getDrawable(activity!!, R.drawable.ic_phone_call_white)
                //xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = activity!!.resources.getDimension(R.dimen.ic_clear_margin).toInt()
                initiated = true
            }

            // not important, we don't want drag & drop
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int { //int position = viewHolder.getAdapterPosition();
/*if (isSelected)
                    return 0;*/
/*  TestAdapter testAdapter = (TestAdapter)recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }*/
                return super.getSwipeDirs(recyclerView, viewHolder)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) { ///set here view swiped
                val swipedPosition = viewHolder.adapterPosition
                contactsRecyclerViewAdapter!!.contactsList[swipedPosition].isSwiped = true
                contactsRecyclerViewAdapter!!.notifyItemChanged(swipedPosition)
                //NotificationAdapter adapter = (NotificationAdapter) recyclerView_contacts.getAdapter();
/*adapter.pendingRemoval(swipedPosition);
                closeAnyOpenState(swipedPosition);*/
// boolean undoOn = adapter.isUndoOn();
//if (undoOn) {
// } else {
//   adapter.remove(swipedPosition);
// }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                val itemView = viewHolder.itemView
                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.adapterPosition == -1) { // not interested in those
                    return
                }
                if (!initiated) {
                    init()
                }
                // draw red background
                background!!.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                background!!.draw(c)
                // draw x mark
                val itemHeight = itemView.bottom - itemView.top
                val intrinsicWidth = xMark!!.intrinsicWidth
                val intrinsicHeight = xMark!!.intrinsicWidth
                val xMarkLeft = itemView.right - xMarkMargin - intrinsicWidth
                val xMarkRight = itemView.right - xMarkMargin
                val xMarkTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                val xMarkBottom = xMarkTop + intrinsicHeight
                xMark!!.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom)
                xMark!!.draw(c)
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        val mItemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        mItemTouchHelper.attachToRecyclerView(recyclerView_contacts)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_filter -> Contants2.showToastMessage(activity, activity!!.getString(R.string.under_development), false)
            R.id.img_board_mail -> {
                intentToEmail = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + imgBoardMail!!.tag))
                /*intentToEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"hr@intelegain.com"});
                intentToEmail.setType("message/rfc822");*/activity!!.startActivity(intentToEmail)
            }
            R.id.img_board_call -> {
                intentToCall = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + imgBoardPhone!!.tag))
                activity!!.startActivity(intentToCall)
            }
            R.id.img_hr_mail -> {
                intentToEmail = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + imgHrMail!!.tag))
                /*intentToEmail.setData(Uri.parse("mailto: hr@intelegain.com"));
                intentToEmail.setType("text/plain");*/activity!!.startActivity(intentToEmail)
            }
            R.id.img_hr_call -> {
                intentToCall = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + imgHrPhone!!.tag))
                activity!!.startActivity(intentToCall)
            }
            R.id.img_accounts_mail -> {
                intentToEmail = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + imgAccountsMail!!.tag))
                /*intentToEmail.setData(Uri.parse("mailto: hr@intelegain.com"));
                intentToEmail.setType("text/plain");*/activity!!.startActivity(intentToEmail)
            }
            R.id.img_accounts_call -> {
                intentToCall = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + imgAccountsPhone!!.tag))
                activity!!.startActivity(intentToCall)
            }
            R.id.text_view_try_again -> if (Contants2.checkInternetConnection(activity)) companyContacts else {
                Contants2.showToastMessage(activity, getString(R.string.no_internet), true)
                hideRecyclerView(true)
            }
            else -> {
            }
        }
    }

    private var progressDialog: ProgressDialog? = null
    /**
     * Show progresss dialog
     */
    private fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(activity)
            progressDialog!!.setMessage("Please wait ...")
            progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog!!.setCanceledOnTouchOutside(false)
        }
        if (!progressDialog!!.isShowing) {
            progressDialog!!.show()
        }
    }

    /**
     * Dismiss the progress dialog
     */
    private fun dismissProgressDialog() {
        if (progressDialog != null) if (progressDialog!!.isShowing) progressDialog!!.dismiss()
    }

    override fun onRefresh() {
        if (Contants2.checkInternetConnection(activity)) {
            companyContacts
            if (editTextSearch != null) editTextSearch!!.setText("")
        } else {
            swipeRefreshLayout!!.isRefreshing = false
            Contants2.showToastMessage(activity, getString(R.string.no_internet), true)
            hideRecyclerView(true)
        }
    }
}