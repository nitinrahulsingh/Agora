package com.intelegain.agora.adapter

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.intelegain.agora.R
import com.intelegain.agora.adapter.MonthAdapter.MonthViewHolder
import com.intelegain.agora.model.AttendanceData
import com.intelegain.agora.model.CalanderDate
import com.intelegain.agora.model.CalenderEvent
import com.intelegain.agora.utils.CalendarUtil.currentDayInInteger
import com.intelegain.agora.utils.CalendarUtil.currentMonthInInteger
import com.intelegain.agora.utils.CalendarUtil.currentYearInInteger
import com.intelegain.agora.utils.CalenderMonth
import java.text.DecimalFormat
import java.util.*

/**
 * Created by suraj on 11/7/17.
 */
class MonthAdapter(private val activity: Activity, private val dateArrayList: ArrayList<CalanderDate>) : RecyclerView.Adapter<MonthViewHolder>() {
    private val eventArrayList: ArrayList<CalenderEvent>? = null
    @JvmField
    var isCalendarCollapsed = false
    private var mobjDashboardData: ArrayList<AttendanceData>? = ArrayList()
    var mIndex = 0
    var dateScrollPos = 0
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_month, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) { //Fri Jul 14 17:46:56 GMT+05:30 2017
        val objDate = dateArrayList[position].date
        val cal = Calendar.getInstance()
        cal.time = objDate
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]
        val strCurrDate = day.toString()
        var strInTime = ""
        var strOutTime = ""
        if (dateArrayList[position].calenderMonth === CalenderMonth.CURRENT) {
            if (mobjDashboardData != null && mIndex < mobjDashboardData!!.size) {
                val objDashboardData = mobjDashboardData!![mIndex]
                strInTime = objDashboardData.attInTime!!
                strOutTime = objDashboardData.attOutTime!!
                if (Integer.valueOf(objDashboardData.viewType!!) == AttendanceData.NORMAL) {
                    if (todayDate.after(objDate)) {
                        holder.dateTv.setTextColor(Color.WHITE)
                        if (objDashboardData.attStatus.equals("p", ignoreCase = true)) setTintWithRes(holder.dateCircle, R.color.orange) else if (objDashboardData.attStatus.equals("HD", ignoreCase = true)) setTintWithRes(holder.dateCircle, R.color.popup_red)
                    }
                } else if (Integer.valueOf(objDashboardData.viewType!!) == AttendanceData.LEAVE) {
                    if (objDashboardData.description.equals("Weekend", ignoreCase = true)) holder.dateTv.setTextColor(Color.GRAY) else if (objDashboardData.description.equals("Absent", ignoreCase = true)) {
                        holder.dateTv.setTextColor(Color.WHITE)
                        setTintWithRes(holder.dateCircle, R.color.popup_red)
                    } else {
                        setTintWithRes(holder.dateCircle, R.color.popup_red)
                        holder.dateTv.setTextColor(Color.WHITE)
                    }
                } else if (Integer.valueOf(objDashboardData.viewType!!) == AttendanceData.HOLIDAY) {
                    holder.dateTv.setTextColor(Color.GRAY)
                    holder.dateCircle.setImageResource(R.drawable.bg_with_dot)
                    setTintWithRes(holder.dateCircle, R.color.transparent)
                } else {
                    holder.dateTv.setTextColor(Color.WHITE)
                    setTintWithRes(holder.dateCircle, R.color.orange)
                }
                if (!TextUtils.isEmpty(strCurrDate) && todayDate.after(objDate)
                        && !objDashboardData.description.equals("Weekend", ignoreCase = true)) holder.itemView.setOnClickListener(CustomDateClickListner(position, strCurrDate, strInTime,
                        strOutTime, objDashboardData.viewType!!, objDashboardData.description!!, objDashboardData))
                mIndex = mIndex + 1
            }
        } else {
            mIndex = 0 // Reset if not for current month
            holder.dateTv.setTextColor(Color.GRAY)
            setTintWithRes(holder.dateCircle, R.color.new_dark_grey)
        }
        holder.dateTv.text = dateArrayList[position].dateStr
        // Set the current date with green background
        val iCurrentMonth = currentMonthInInteger
        if (month == iCurrentMonth) {
            val iCurrentDay = currentDayInInteger
            if (day == iCurrentDay) {
                val iCurrentYear = currentYearInInteger
                if (year == iCurrentYear) setTintWithRes(holder.dateCircle, R.color.green)
                dateScrollPos = position
            }
        }
    }

    override fun getItemCount(): Int {
        return dateArrayList.size
    }

    //    public void setEventArrayList(ArrayList<CalenderEvent> eventArrayList) {
//        this.eventArrayList = eventArrayList;
//        notifyDataSetChanged();
//    }
    fun clearData() {
        dateArrayList.clear()
        //        if (eventArrayList != null) {
//            eventArrayList.clear();
//        }
        notifyDataSetChanged()
    }

    fun addDates(mDateArrayList: ArrayList<CalanderDate>) {
        dateArrayList.addAll(mDateArrayList)
    }

    fun setData(objDashboardData: ArrayList<AttendanceData>?) {
        mobjDashboardData = objDashboardData
        notifyDataSetChanged()
        /*if (mobjDashboardData != null && mobjDashboardData.size() > 0) {
            mobjDashboardData.clear();
            mobjDashboardData.addAll(objDashboardData);
        } else {
            mobjDashboardData.addAll(objDashboardData);
        }*/
    }

    fun setTintWithRes(imageView: ImageView, resId: Int) {
        imageView.setColorFilter(ContextCompat.getColor(activity, resId))
    }

    var mpopup: PopupWindow? = null
    /**
     * Showing popup menu when tapping on day
     */
    private fun showPopupMenu(strCurrDate: String, x: Int, y: Int, strInTime: String, strOutTime: String, viewType: Int, description: String, objDashboardData: AttendanceData) {
        val popUpView: View
        val tvDay: TextView
        val tvInTime: TextView
        val tvOutTime: TextView
        val tvLeave: TextView
        //        0 - Normal day
//        1 - leave
//        2 - holiday
//        required for current date
        if (viewType == 0) {
            popUpView = activity.layoutInflater.inflate(R.layout.popup_dialog_for_date, null)
            val rl_dt_dialog = popUpView.findViewById<RelativeLayout>(R.id.rl_dt_dialog)
            if (objDashboardData.attStatus.equals("p", ignoreCase = true)) {
                if (objDashboardData.attendanceDate!!.substring(3, 5).equals(DecimalFormat("00").format(currentMonthInInteger + 1.toLong()), ignoreCase = true)) {
                    if (objDashboardData.date.equals(currentDayInInteger.toString(), ignoreCase = true)) rl_dt_dialog.setBackgroundResource(R.drawable.popup_bg_green) //set the color for current date
                    else rl_dt_dialog.setBackgroundResource(R.drawable.popup_bg_yellow)
                } else {
                    rl_dt_dialog.setBackgroundResource(R.drawable.popup_bg_yellow)
                }
            } else if (objDashboardData.attStatus.equals("HD", ignoreCase = true)) {
                if (objDashboardData.attendanceDate!!.substring(3, 5).equals(DecimalFormat("00").format(currentMonthInInteger + 1.toLong()), ignoreCase = true)) {
                    if (objDashboardData.date.equals(currentDayInInteger.toString(), ignoreCase = true)) rl_dt_dialog.setBackgroundResource(R.drawable.popup_bg_green) //set the color for current date
                } else {
                    rl_dt_dialog.setBackgroundResource(R.drawable.popup_bg)
                }
            } else {
            }
            tvDay = popUpView.findViewById(R.id.tvDay)
            tvInTime = popUpView.findViewById(R.id.tvInTime)
            tvOutTime = popUpView.findViewById(R.id.tvOutTime)
            tvDay.text = strCurrDate
            tvInTime.text = "IN: $strInTime"
            tvOutTime.text = "OUT: $strOutTime"
            rl_dt_dialog.setOnClickListener { if (mpopup != null && mpopup!!.isShowing) mpopup!!.dismiss() }
        } else if (viewType == 2) { //holiday view inflated here
            popUpView = activity.layoutInflater.inflate(R.layout.popup_dialog_for_leave, null)
            val rl_dt_dialog = popUpView.findViewById<RelativeLayout>(R.id.rl_dt_dialog)
            tvDay = popUpView.findViewById(R.id.tvDay)
            tvLeave = popUpView.findViewById(R.id.tvLeave)
            tvLeave.text = description
            tvDay.text = strCurrDate
            rl_dt_dialog.setOnClickListener { if (mpopup != null && mpopup!!.isShowing) mpopup!!.dismiss() }
        } else if (viewType == 1) { //normal view inflated here
            popUpView = activity.layoutInflater.inflate(R.layout.popup_dialog_for_date, null)
            val rl_dt_dialog = popUpView.findViewById<RelativeLayout>(R.id.rl_dt_dialog)
            tvDay = popUpView.findViewById(R.id.tvDay)
            tvInTime = popUpView.findViewById(R.id.tvInTime)
            tvOutTime = popUpView.findViewById(R.id.tvOutTime)
            tvDay.text = strCurrDate
            tvInTime.text = "IN: $strInTime"
            tvOutTime.text = "OUT: $strOutTime"
            rl_dt_dialog.setOnClickListener { if (mpopup != null && mpopup!!.isShowing) mpopup!!.dismiss() }
        } else {
            popUpView = activity.layoutInflater.inflate(R.layout.popup_dialog_for_date, null)
            val rl_dt_dialog = popUpView.findViewById<RelativeLayout>(R.id.rl_dt_dialog)
            tvDay = popUpView.findViewById(R.id.tvDay)
            tvInTime = popUpView.findViewById(R.id.tvInTime)
            tvOutTime = popUpView.findViewById(R.id.tvOutTime)
            tvDay.text = strCurrDate
            tvInTime.text = "IN: $strInTime"
            tvOutTime.text = "OUT: $strOutTime"
            rl_dt_dialog.setOnClickListener { if (mpopup != null && mpopup!!.isShowing) mpopup!!.dismiss() }
        }
        mpopup = PopupWindow(popUpView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true) // Creation of popup
        mpopup!!.animationStyle = android.R.style.Animation_Translucent
        mpopup!!.setBackgroundDrawable(ColorDrawable(
                Color.TRANSPARENT))
        mpopup!!.showAtLocation(popUpView, Gravity.NO_GRAVITY, x, y) // Displaying popup
    }

    inner class MonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dateTv: TextView
        var dateCircle: ImageView

        init {
            dateTv = itemView.findViewById(R.id.dateTv)
            dateCircle = itemView.findViewById(R.id.img_date_circle)
        }
    }

    /**
     * On date click listener event will be handled here
     */
    private inner class CustomDateClickListner(private val iPosition: Int, private val strCurrDate: String, private val strInTime: String, private val strOutTime: String,
                                               private val viewType: Int, private val strDescription: String, var objDashboardData: AttendanceData) : View.OnClickListener {
        override fun onClick(v: View) {
            val originalPos = IntArray(2)
            v.getLocationInWindow(originalPos)
            val x = originalPos[0] + 5
            val y = originalPos[1] + 5
            showPopupMenu(strCurrDate, x, y, strInTime, strOutTime, viewType, strDescription, objDashboardData)
        }

    }

    /**
     * Get today date
     */
    private val todayDate: Date
        private get() = Calendar.getInstance().time

    companion object {
        private const val TAG = "MonthAdapter"
    }

}