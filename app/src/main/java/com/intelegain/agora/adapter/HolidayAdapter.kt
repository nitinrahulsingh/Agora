package com.intelegain.agora.adapter

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.intelegain.agora.R
import com.intelegain.agora.adapter.HolidayAdapter.HolidayViewHolder
import com.intelegain.agora.model.HolidayData
import java.util.*

class HolidayAdapter(private val activity: Activity, private val mlstHolidayData: ArrayList<HolidayData>) : RecyclerView.Adapter<HolidayViewHolder>() {
    private val TAG = javaClass.simpleName
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_holiday, parent, false)
        return HolidayViewHolder(view)
    }

    override fun onBindViewHolder(holder: HolidayViewHolder, position: Int) {
        val objHolidayData = mlstHolidayData[position]
        // set Day
        holder.tvHolidayDay.text = objHolidayData.holidayDay
        // set Month
        holder.tvHolidayMonth.text = objHolidayData.holidayMonth
        // set holiday name
        val strHolidayName = objHolidayData.holidayName + ""
        holder.tvHolidayName.text = strHolidayName
        // set holiday image , circle color and row background
        val iRowId = objHolidayData.imageId
        if (strHolidayName.contains("Republic Day")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#f8efea"))
            holder.ivHolidayImage.setImageResource(R.drawable.republic_day)
            setTintWithRes(holder.ivBgCircle, R.color.yellow)
        } else if (strHolidayName.contains("Holi")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#deefff"))
            holder.ivHolidayImage.setImageResource(R.drawable.holi)
            setTintWithRes(holder.ivBgCircle, android.R.color.holo_blue_light)
        } else if (strHolidayName.contains("Good Friday")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#e9f7f7"))
            holder.ivHolidayImage.setImageResource(R.drawable.good_friday)
            setTintWithRes(holder.ivBgCircle, R.color.green)
        } else if (strHolidayName.contains("Maharashtra Day")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#f4e9f7"))
            holder.ivHolidayImage.setImageResource(R.drawable.maharashtra_day)
            setTintWithRes(holder.ivBgCircle, android.R.color.holo_red_light)
        } else if (strHolidayName.contains("Eid (Subject to change)")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#edf4ec"))
            holder.ivHolidayImage.setImageResource(R.drawable.eid)
            setTintWithRes(holder.ivBgCircle, android.R.color.holo_green_dark)
        } else if (strHolidayName.contains("Independence Day")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#f0f0f0"))
            holder.ivHolidayImage.setImageResource(R.drawable.independence_day)
            setTintWithRes(holder.ivBgCircle, R.color.orange)
        } else if (strHolidayName.contains("Ganesh Chaturthi")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#e9f7f7"))
            holder.ivHolidayImage.setImageResource(R.drawable.ganesh_chaturthi)
            setTintWithRes(holder.ivBgCircle, R.color.green)
        } else if (strHolidayName.contains("Dussehra")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#f4e9f7"))
            holder.ivHolidayImage.setImageResource(R.drawable.dussehra)
            setTintWithRes(holder.ivBgCircle, android.R.color.holo_red_light)
        } else if (strHolidayName.contains("Mahatma Gandhi Jayanthi")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#efefef"))
            holder.ivHolidayImage.setImageResource(R.drawable.mahatma_gandhi_jayanthi)
            setTintWithRes(holder.ivBgCircle, R.color.orange)
        } else if (strHolidayName.contains("Diwali")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#deefff"))
            holder.ivHolidayImage.setImageResource(R.drawable.diwali)
            setTintWithRes(holder.ivBgCircle, android.R.color.holo_blue_light)
        } else if (strHolidayName.contains("Christmas")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#fde9eb"))
            holder.ivHolidayImage.setImageResource(R.drawable.christmas)
            setTintWithRes(holder.ivBgCircle, R.color.orange)
        } else {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#f8f8f8"))
            holder.ivHolidayImage.setImageResource(R.drawable.holidays)
            setTintWithRes(holder.ivBgCircle, R.color.orange)
        }
    }

    override fun getItemCount(): Int {
        return mlstHolidayData.size
    }

    inner class HolidayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val holiday_row_background: RelativeLayout
        val tvHolidayDay: TextView
        val tvHolidayMonth: TextView
        val tvHolidayName: TextView
        val ivHolidayImage: ImageView
        val ivBgCircle: ImageView

        init {
            holiday_row_background = itemView.findViewById(R.id.holiday_row_background)
            ivHolidayImage = itemView.findViewById(R.id.ivHolidayImage)
            ivBgCircle = itemView.findViewById(R.id.ivBgCircle)
            tvHolidayDay = itemView.findViewById(R.id.tvHolidayDay)
            tvHolidayMonth = itemView.findViewById(R.id.tvHolidayMonth)
            tvHolidayName = itemView.findViewById(R.id.tvHolidayName)
        }
    }

    fun setTintWithRes(imageView: ImageView, resId: Int) {
        imageView.setColorFilter(ContextCompat.getColor(activity, resId))
    }

}