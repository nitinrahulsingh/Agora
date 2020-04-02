package com.intelegain.agora.adapter

import android.app.Activity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.intelegain.agora.R
import com.intelegain.agora.adapter.NewLeavesAdapter.LeaveViewHolder
import com.intelegain.agora.interfeces.OnItemLongClickListener
import com.intelegain.agora.model.LeaveData
import java.util.*

/**
 * Created by suraj.m on 18/7/17.
 */
class NewLeavesAdapter(private val activity: Activity, mlstLeaveData: ArrayList<LeaveData>, onItemLongClickListener: OnItemLongClickListener) : RecyclerView.Adapter<LeaveViewHolder>() {
    private val TAG = javaClass.simpleName
    var mlstLeaveData: ArrayList<LeaveData>
    @JvmField
    var filteredList: ArrayList<LeaveData>?
    var onItemLongClickListener: OnItemLongClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.leave_row_item, parent, false)
        return LeaveViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaveViewHolder, position: Int) {
        val strLeaveStatus = filteredList!![position].leaveStatus
        if (strLeaveStatus.equals("Pending", ignoreCase = true)) {
            holder.imgBtnLeaveStatus.setImageResource(R.drawable.pending)
            holder.imgBtnLeaveStatus.setBackgroundResource(R.drawable.bg_cell_gray)
        } else if (strLeaveStatus.equals("Approved", ignoreCase = true)) {
            holder.imgBtnLeaveStatus.setImageResource(R.drawable.approved)
            holder.imgBtnLeaveStatus.setBackgroundResource(R.drawable.bg_cell_green)
        } else if (strLeaveStatus.equals("Rejected", ignoreCase = true)) {
            holder.imgBtnLeaveStatus.setImageResource(R.drawable.rejected)
            holder.imgBtnLeaveStatus.setBackgroundResource(R.drawable.bg_cell_gray)
        } else { // Do nothing
        }
        // To set leave Type
        val strLeaveType = filteredList!![position].leaveType.toString()
        var strLeaveTypeShortName = ""
        if (strLeaveType.equals("Paid Leave", ignoreCase = true)) {
            strLeaveTypeShortName = "PL"
        } else if (strLeaveType.equals("Comp Off", ignoreCase = true)) {
            strLeaveTypeShortName = "CO"
        } else if (strLeaveType.equals("Casual Leave", ignoreCase = true)) {
            strLeaveTypeShortName = "CL"
        } else if (strLeaveType.equals("Sick Leave", ignoreCase = true)) {
            strLeaveTypeShortName = "SL"
        } else if (strLeaveType.equals("Leave (Without Pay)", ignoreCase = true)) {
            strLeaveTypeShortName = "WL"
        }
        // Set leave Start date
        holder.tvLeaveStartDate.text = filteredList!![position].leaveFrom
        // Set leave End date
        holder.tvLeaveEndDate.text = filteredList!![position].leaveTo
        // Set leave reason title text
        holder.tvLeaveTitle_text.text = filteredList!![position].leaveStatus
        // Set leave reason detail text
//holder.tvLeaveDetail_text.setText(filteredList.get(position).getLeaveDesc());
        holder.tvLeaveDetail_text.text = filteredList!![position].leaveDesc
        // Set leave type text
        holder.tvLeaveType.text = strLeaveTypeShortName
        if (filteredList!![position].adminComments != null && filteredList!![position].adminComments != "") holder.tvLeaveAdminComments.text = filteredList!![position].adminComments else holder.tvLeaveAdminComments.visibility = View.GONE
        if (strLeaveStatus.equals("Pending", ignoreCase = true) /*|| strLeaveStatus.equalsIgnoreCase("Rejected")*/) { //            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    onItemLongClickListener.onItemLongClicked(position, filteredList.get(position).getLeaveId());
//                    return true;
//                }
//            });
            holder.tvSwipeToDelete.visibility = View.VISIBLE
        } else {
            holder.tvSwipeToDelete.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return if (null != filteredList) filteredList!!.size else 0
        //return mlstLeaveData.size();
    }

    fun deleteRow(position: Int) {
        onItemLongClickListener.onItemLongClicked(position, filteredList!![position].leaveId)
    }

    fun removeItem(position: Int) {
        filteredList!!.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class LeaveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBtnLeaveStatus: ImageButton
        val tvLeaveType: TextView
        val tvLeaveStartDate: TextView
        val tvLeaveEndDate: TextView
        val tvLeaveTitle_text: TextView
        val tvLeaveDetail_text: TextView
        val tvLeaveAdminComments: TextView
        val tvSwipeToDelete: TextView

        init {
            imgBtnLeaveStatus = itemView.findViewById(R.id.imgBtnLeaveStatus)
            tvLeaveType = itemView.findViewById(R.id.tvLeaveType)
            tvLeaveStartDate = itemView.findViewById(R.id.tvLeaveStartDate)
            tvLeaveEndDate = itemView.findViewById(R.id.tvLeaveEndDate)
            tvLeaveTitle_text = itemView.findViewById(R.id.tvLeaveTitle_text)
            tvLeaveDetail_text = itemView.findViewById(R.id.tvLeaveDetail_text)
            tvLeaveAdminComments = itemView.findViewById(R.id.tvLeaveAdminComments)
            tvSwipeToDelete = itemView.findViewById(R.id.tvSwipeToDelete)
        }
    }

    // Do Search...
    fun filter(text: String) { // Searching could be complex..so we will dispatch it to a different thread...
        Thread(Runnable {
            // Clear the filter list
            filteredList!!.clear()
            // If there is no search value, then add all original list items to filter list
            if (TextUtils.isEmpty(text)) {
                filteredList!!.addAll(mlstLeaveData)
            } else { // Iterate in the original List and add it to filter list...
                for (item in mlstLeaveData) {
                    if (item.leaveStatus!!.toLowerCase().contains(text.toLowerCase()) ||
                            item.leaveStatus!!.toLowerCase().contains(text.toLowerCase())) {
                        filteredList!!.add(item)
                    }
                }
            }
            // Set on UI Thread
            activity.runOnUiThread {
                // Notify the List that the DataSet has changed...
                notifyDataSetChanged()
            }
        }).start()
    }

    init {
        filteredList = ArrayList()
        this.mlstLeaveData = mlstLeaveData
        // we copy the original list to the filter list and use it for setting row values
        filteredList!!.addAll(this.mlstLeaveData)
        this.onItemLongClickListener = onItemLongClickListener
    }
}