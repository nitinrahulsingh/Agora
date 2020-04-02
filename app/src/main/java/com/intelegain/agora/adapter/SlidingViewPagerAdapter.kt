package com.intelegain.agora.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.intelegain.agora.R
import com.intelegain.agora.model.CalanderDate
import com.intelegain.agora.utils.CalenderMonth
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author suraj.m
 */
class SlidingViewPagerAdapter(private val context: Context, private val monthYearArrayList: ArrayList<String>, var yearServerRecieved: Int) : PagerAdapter() {
    private val TAG = javaClass.simpleName
    var monthAdapter: MonthAdapter? = null
    @JvmField
    var monthAdapterArrayList: ArrayList<MonthAdapter>
    override fun getCount(): Int {
        return monthYearArrayList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v = LayoutInflater.from(context).inflate(R.layout.row_month_slide, container, false)
        val holder = MyViewHolder(v)
        holder.recyclerView.layoutManager = GridLayoutManager(context, 7)
        val dateFormat = SimpleDateFormat("MMM yyyy")
        try {
            val date = dateFormat.parse(monthYearArrayList[position])
            val calendar = Calendar.getInstance()
            calendar.time = date
            Log.e(TAG, "Date: $date")
            val monthAdapter1 = MonthAdapter((context as Activity), generateStringMonth(calendar[Calendar.MONTH], calendar[Calendar.YEAR]))
            holder.recyclerView.adapter = monthAdapter1
            monthAdapterArrayList[position] = monthAdapter1
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        v.tag = position
        container.addView(v)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    inner class MyViewHolder(v: View) {
        var recyclerView: RecyclerView

        init {
            recyclerView = v.findViewById(R.id.month_recyView)
        }
    }

    private fun generateStringMonth(month: Int, year: Int): ArrayList<CalanderDate> {
        val stringArrayList = ArrayList<CalanderDate>()
        val calendar = Calendar.getInstance()
        calendar[Calendar.MONTH] = month
        calendar[Calendar.YEAR] = year
        val previousMonth = Calendar.getInstance()
        previousMonth[Calendar.MONTH] = month
        previousMonth[Calendar.YEAR] = year
        previousMonth.add(Calendar.MONTH, -1)
        if (getFirstDay(month, year) != 1) {
            for (nos in getLastSundayDateinMonth(previousMonth[Calendar.MONTH], previousMonth[Calendar.YEAR])..previousMonth.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                previousMonth[Calendar.DATE] = nos
                val isSunday = previousMonth[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY
                stringArrayList.add(CalanderDate("" + nos, isSunday, previousMonth.time, CalenderMonth.PREVIOUS))
            }
        }
        for (no in 1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            val currentMonthDate = Calendar.getInstance()
            currentMonthDate[calendar[Calendar.YEAR], calendar[Calendar.MONTH]] = no
            val isSunday = currentMonthDate[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY
            stringArrayList.add(CalanderDate(no.toString() + "", isSunday, currentMonthDate.time, CalenderMonth.CURRENT))
        }
        val nextMonth = Calendar.getInstance()
        nextMonth[Calendar.YEAR] = year
        nextMonth[Calendar.MONTH] = month
        nextMonth.add(Calendar.MONTH, 1)
        //int satCount = (!isLastDayIsSaturday(month,year)&&(((stringArrayList.size())/7)<5.01))?2:1;
        val satCount = 6 - stringArrayList.size / 7
        for (no in 1..getSaturadayDate(nextMonth[Calendar.MONTH], nextMonth[Calendar.YEAR], satCount)) {
            nextMonth[Calendar.DATE] = no
            val isSunday = nextMonth[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY
            stringArrayList.add(CalanderDate(no.toString() + "", isSunday, nextMonth.time, CalenderMonth.NEXT))
        }
        return stringArrayList
    }

    private fun getFirstDay(month: Int, year: Int): Int {
        val c = Calendar.getInstance()
        c[Calendar.MONTH] = month
        c[Calendar.YEAR] = year
        c[Calendar.DAY_OF_MONTH] = 1
        return c[Calendar.DAY_OF_WEEK]
    }

    private fun getNoOfSundayinMonth(month: Int, year: Int): Int {
        var count = 0
        val calendar = Calendar.getInstance()
        calendar[Calendar.MONTH] = month
        calendar[Calendar.YEAR] = year
        for (no in 1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            calendar[Calendar.DATE] = no
            if (calendar[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY) {
                count++
            }
        }
        return count
    }

    private fun getLastSundayDateinMonth(month: Int, year: Int): Int {
        var count = 0
        val calendar = Calendar.getInstance()
        calendar[Calendar.MONTH] = month
        calendar[Calendar.YEAR] = year
        for (no in 1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            calendar[Calendar.DATE] = no
            if (calendar[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY) {
                count++
                if (count == getNoOfSundayinMonth(month, year)) {
                    return no
                }
            }
        }
        return count
    }

    private fun isLastDayIsSaturday(month: Int, year: Int): Boolean {
        val calendar = Calendar.getInstance()
        calendar[Calendar.MONTH] = month
        calendar[Calendar.YEAR] = year
        calendar[Calendar.DATE] = calendar.getActualMaximum(Calendar.DATE)
        return if (calendar[Calendar.DAY_OF_WEEK] == Calendar.SATURDAY) {
            true
        } else false
    }

    private fun getSaturadayDate(month: Int, year: Int, saturdayNo: Int): Int {
        var count = 0
        val calendar = Calendar.getInstance()
        calendar[Calendar.MONTH] = month
        calendar[Calendar.YEAR] = year
        for (no in calendar.getActualMinimum(Calendar.DATE)..calendar.getActualMaximum(Calendar.DATE)) {
            calendar[Calendar.DATE] = no
            if (calendar[Calendar.DAY_OF_WEEK] == Calendar.SATURDAY) {
                count++
                if (count == saturdayNo) {
                    return no
                }
            }
        }
        return 0
    }

    fun generateMonthAdapterArrayList(yearServerRecieved: Int): ArrayList<MonthAdapter> {
        monthAdapterArrayList = ArrayList()
        for (s in monthYearArrayList) {
            val calendar = Calendar.getInstance()
            calendar[Calendar.YEAR] = yearServerRecieved
            monthAdapterArrayList.add(MonthAdapter((context as Activity), generateStringMonth(calendar[Calendar.MONTH], calendar[Calendar.YEAR])))
        }
        return monthAdapterArrayList
    }

    companion object {
        @JvmField
        var bIsFirstTime = true
    }

    init {
        monthAdapterArrayList = generateMonthAdapterArrayList(yearServerRecieved)
    }
}