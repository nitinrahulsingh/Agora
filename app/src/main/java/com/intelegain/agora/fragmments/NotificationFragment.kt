package com.intelegain.agora.fragmments

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intelegain.agora.R
import com.intelegain.agora.database.NotificationDbHelper
import com.intelegain.agora.database.NotificationReaderContract
import com.intelegain.agora.model.NotificationItem
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [NotificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationFragment
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
    : Fragment() {
    private var activity: Activity? = null
    private var mContext: Context? = null
    private lateinit var recyclerView: RecyclerView
    var ITEMS: MutableList<NotificationItem>? = null
    // TODO: Customize parameters
    private var mColumnCount = 1
    private var mListener: OnNotificationListener? = null
    var adapter: MyNotificationRecyclerViewAdapter? = null
    var appendChatScreenMsgReceiver: BroadcastReceiver? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach: ")
        if (context is OnNotificationListener) {
            mListener = context
            mContext = context;
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        if (arguments != null) {
            mColumnCount = arguments!!.getInt(ARG_COLUMN_COUNT)
        }
    }

    private lateinit var textViewNoData: TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView: ")
        retainInstance = true // to retain the state of fragment when activity recreates
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notification, container, false)
        activity = getActivity()
        mContext = view.context
        // Set the adapter
        textViewNoData = view.findViewById(R.id.text_no_data)
        textViewNoData.setVisibility(View.GONE)
        recyclerView = view.findViewById(R.id.listNotification)
        recyclerView.setLayoutManager(LinearLayoutManager(context))
        //            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
// receive broadcast when new notification arrived if this fragment is created.
        appendChatScreenMsgReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val b = intent.extras
                if (b != null) {
                    Log.d(TAG, "onReceive: \nnewRowId:" + b.getString("newRowId"))
                    val model = NotificationItem("" + b.getString("title"), "" + b.getString("message"), "" + b.getString("status"))
                    ITEMS!!.add(0, model)
                    if (adapter != null) {
                        adapter!!.notifyDataSetChanged()
                    } else {
                        adapter = MyNotificationRecyclerViewAdapter(context, ITEMS, mListener)
                        if (recyclerView == null) recyclerView.setAdapter(adapter)
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }
        }
        getActivity()!!.registerReceiver(appendChatScreenMsgReceiver, IntentFilter("appendChatScreenMsg"))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        ITEMS = ArrayList()
        val mDbHelper = NotificationDbHelper(context)
        val db = mDbHelper.readableDatabase
        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        val projection = arrayOf(
                NotificationReaderContract.NotificationEntry._ID,
                NotificationReaderContract.NotificationEntry.COLUMN_NAME_TITLE,
                NotificationReaderContract.NotificationEntry.COLUMN_NAME_SUBTITLE,
                NotificationReaderContract.NotificationEntry.COLUMN_NAME_STATUS
        )
        // Filter results WHERE "title" = 'My Title'
        val selection = NotificationReaderContract.NotificationEntry.COLUMN_NAME_TITLE + " = ?"
        val selectionArgs = arrayOf("My Title")
        // How you want the results sorted in the resulting Cursor
        val sortOrder = NotificationReaderContract.NotificationEntry._ID + " DESC"
        val cursor = db.query(
                NotificationReaderContract.NotificationEntry.TABLE_NAME,  // The table to query
                projection,  // The array of columns to return (pass null to get all)
                null,  // The columns for the WHERE clause
                null,  // The values for the WHERE clause
                null,  // don't group the rows
                null,  // don't filter by row groups
                sortOrder // The sort order
        )
        //        List itemIds = new ArrayList<>();
        while (cursor.moveToNext()) { //            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(NotificationReaderContract.NotificationEntry._ID));
//            itemIds.add(itemId);
            val title = cursor.getString(1)
            val message = cursor.getString(2)
            val status = cursor.getString(3)
            (ITEMS as ArrayList<NotificationItem>).add(NotificationItem(title, message, status))
        }
        cursor.close()
        if ((ITEMS as ArrayList<NotificationItem>).size > 0) {
            textViewNoData!!.visibility = View.GONE
            recyclerView!!.visibility = View.VISIBLE
            adapter = MyNotificationRecyclerViewAdapter(context, ITEMS, mListener)
            recyclerView!!.adapter = adapter
        } else {
            textViewNoData!!.visibility = View.VISIBLE
            textViewNoData!!.text = "Notifications not available"
            recyclerView!!.visibility = View.GONE
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated: ")
    }

    override fun onStart() {
        super.onStart()
        /*  // get current FCM token
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();
                Log.e(TAG, "Token" + mToken);
            }
        });*/Log.d(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onResume: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        getActivity()!!.unregisterReceiver(appendChatScreenMsgReceiver)
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDestroy: ")
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnNotificationListener {
        // TODO: Update argument type and name
        fun onNotification(item: String?)
    }

    companion object {
        private const val TAG = "NotificationFragment"
        // TODO: Customize parameter argument names
        private const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        fun newInstance(columnCount: Int): NotificationFragment {
            val fragment = NotificationFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}