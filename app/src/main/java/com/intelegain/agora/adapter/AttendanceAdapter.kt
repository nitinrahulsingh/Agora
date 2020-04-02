package com.intelegain.agora.adapter

import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.intelegain.agora.R
import com.intelegain.agora.model.AttendanceData
import java.util.*

class AttendanceAdapter(private val activity: Activity, private val mlstAttendanceData: ArrayList<AttendanceData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var strHolidayName: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        viewHolder = when (viewType) {
            AttendanceData.NORMAL -> {
                val v1 = inflater.inflate(R.layout.row_item_attendance, parent, false)
                AttendanceViewHolderRegular(v1)
            }
            AttendanceData.LEAVE -> {
                val v2 = inflater.inflate(R.layout.row_item_attendance_leave, parent, false)
                AttendanceViewHolderLeave(v2)
            }
            AttendanceData.HOLIDAY -> {
                val v3 = inflater.inflate(R.layout.row_item_attendance_holiday, parent, false)
                AttendanceViewHolderHoliday(v3)
            }
            else -> {
                val v4 = inflater.inflate(R.layout.row_item_attendance, parent, false)
                AttendanceViewHolderRegular(v4)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var objAttendanceData: AttendanceData? = null
        when (holder.itemViewType) {
            AttendanceData.NORMAL -> {
                objAttendanceData = mlstAttendanceData[position]
                val viewHolderRegular = holder as AttendanceViewHolderRegular
                viewHolderRegular.container_for_InAnOutTime.setBackgroundResource(R.drawable.patch)
                Log.d("AttendanceData.NORMAL", "objAttendanceData.getDate() = " + objAttendanceData.date)
                Log.d("AttendanceData.NORMAL", "objAttendanceData.getAttStatus() = " + objAttendanceData.attStatus)
                if (mlstAttendanceData[position].attStatus.equals("HD", ignoreCase = true)) viewHolderRegular.tvAttStatus.setBackgroundResource(R.drawable.circle_within_circle_red) else viewHolderRegular.tvAttStatus.setBackgroundResource(R.drawable.circle_within_circle_green)
                viewHolderRegular.tvAttendanceDate.text = objAttendanceData.date
                viewHolderRegular.tvAttendanceDay.text = objAttendanceData.day
                viewHolderRegular.tvAttStatus.text = objAttendanceData.attStatus
                viewHolderRegular.tvInTime.text = objAttendanceData.attInTime
                viewHolderRegular.tvOutTime.text = objAttendanceData.attOutTime
                viewHolderRegular.tvTimesheet.text = objAttendanceData.timesheethours
                viewHolderRegular.tvTotalHourWorked.text = objAttendanceData.workinghours
                if (position == mlstAttendanceData.size - 1) {
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(viewHolderRegular.constraint_layout_normal)
                    constraintSet.connect(viewHolderRegular.vwUpperLine.id, ConstraintSet.BOTTOM, viewHolderRegular.tvAttStatus.id, ConstraintSet.TOP, 0)
                    constraintSet.applyTo(viewHolderRegular.constraint_layout_normal)
                } else {
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(viewHolderRegular.constraint_layout_normal)
                    constraintSet.connect(viewHolderRegular.vwUpperLine.id, ConstraintSet.BOTTOM, viewHolderRegular.constraint_layout_normal.id, ConstraintSet.BOTTOM, 0)
                    constraintSet.applyTo(viewHolderRegular.constraint_layout_normal)
                }
            }
            AttendanceData.LEAVE -> try {
                objAttendanceData = mlstAttendanceData[position]
                val viewHolderLeave = holder as AttendanceViewHolderLeave
                setTintWithRes(viewHolderLeave.container_for_InAnOutTime, R.color.wbs_assigned_to)
                //configureViewHolder2(viewHolderLeave, position);
                viewHolderLeave.tvAttendanceDate.text = objAttendanceData.date
                viewHolderLeave.tvAttendanceDay.text = objAttendanceData.day
                //viewHolderLeave.tvAttStatus.setText(objAttendanceData.getAttStatus());
                viewHolderLeave.tvLeaveText.text = objAttendanceData.description
                if (position == mlstAttendanceData.size - 1) {
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(viewHolderLeave.constraint_layout_leave)
                    constraintSet.connect(viewHolderLeave.vwUpperLine.id, ConstraintSet.BOTTOM,
                            viewHolderLeave.tvAttStatus.id, ConstraintSet.TOP, 0)
                    constraintSet.applyTo(viewHolderLeave.constraint_layout_leave)
                } else {
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(viewHolderLeave.constraint_layout_leave)
                    constraintSet.connect(viewHolderLeave.vwUpperLine.id, ConstraintSet.BOTTOM,
                            viewHolderLeave.constraint_layout_leave.id, ConstraintSet.BOTTOM, 0)
                    constraintSet.applyTo(viewHolderLeave.constraint_layout_leave)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            AttendanceData.HOLIDAY -> try {
                objAttendanceData = mlstAttendanceData[position]
                val viewHolderHoliday = holder as AttendanceViewHolderHoliday
                //viewHolderHoliday.ivHolidayIcon.setImageResource(R.drawable.attendace_holiday);
                viewHolderHoliday.tvAttendanceDate.text = objAttendanceData.date
                viewHolderHoliday.tvAttendanceDay.text = objAttendanceData.day
                viewHolderHoliday.tvHolidayText.text = objAttendanceData.description
                //setRowForHoliday(viewHolderHoliday.container_for_InAnOutTime, viewHolderHoliday.ivHolidayImage, objAttendanceData.getDescription());
//setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.republic_day);
                strHolidayName = objAttendanceData.description
                if (strHolidayName!!.contains("Republic Day")) { // viewHolderHoliday.container_for_InAnOutTime.setBackgroundColor(Color.parseColor("#f8efea"));
                    setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.republic_day)
                    viewHolderHoliday.ivHolidayImage.setImageResource(R.drawable.republic_day)
                    viewHolderHoliday.tvHolidayText.setTextColor(ContextCompat.getColor(activity, R.color.yellow))
                    //setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.yellow);
                } else if (strHolidayName!!.contains("Holi")) { //viewHolderHoliday.container_for_InAnOutTime.setBackgroundColor(Color.parseColor("#deefff"));
                    setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.holi)
                    viewHolderHoliday.ivHolidayImage.setImageResource(R.drawable.holi)
                    viewHolderHoliday.tvHolidayText.setTextColor(ContextCompat.getColor(activity, android.R.color.holo_blue_light))
                    //setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, android.R.color.holo_blue_light);
                } else if (strHolidayName!!.contains("Good Friday")) {
                    setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.good_friday)
                    //viewHolderHoliday.container_for_InAnOutTime.setBackgroundColor(Color.parseColor("#e9f7f7"));
                    viewHolderHoliday.ivHolidayImage.setImageResource(R.drawable.good_friday)
                    viewHolderHoliday.tvHolidayText.setTextColor(ContextCompat.getColor(activity, R.color.green))
                    // setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.green);
                } else if (strHolidayName!!.contains("Maharashtra Day")) {
                    setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.maharashtra_day)
                    //viewHolderHoliday.container_for_InAnOutTime.setBackgroundColor(Color.parseColor("#f4e9f7"));
                    viewHolderHoliday.ivHolidayImage.setImageResource(R.drawable.maharashtra_day)
                    viewHolderHoliday.tvHolidayText.setTextColor(ContextCompat.getColor(activity, android.R.color.holo_red_light))
                    // setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, android.R.color.holo_red_light);
                } else if (strHolidayName!!.contains("Eid (Subject to change)")) {
                    setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.eid)
                    // viewHolderHoliday.container_for_InAnOutTime.setBackgroundColor(Color.parseColor("#edf4ec"));
                    viewHolderHoliday.ivHolidayImage.setImageResource(R.drawable.eid)
                    viewHolderHoliday.tvHolidayText.setTextColor(ContextCompat.getColor(activity, android.R.color.holo_green_dark))
                    // setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, android.R.color.holo_green_dark);
                } else if (strHolidayName!!.contains("Independence Day")) {
                    setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.independence)
                    //viewHolderHoliday.container_for_InAnOutTime.setBackgroundColor(Color.parseColor("#f0f0f0"));
                    viewHolderHoliday.ivHolidayImage.setImageResource(R.drawable.independence_day)
                    viewHolderHoliday.tvHolidayText.setTextColor(ContextCompat.getColor(activity, R.color.orange))
                    //setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.orange);
                } else if (strHolidayName!!.contains("Ganesh Chaturthi")) {
                    setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.ganesh_chaturthi)
                    //viewHolderHoliday.container_for_InAnOutTime.setBackgroundColor(Color.parseColor("#e9f7f7"));
                    viewHolderHoliday.ivHolidayImage.setImageResource(R.drawable.ganesh_chaturthi)
                    viewHolderHoliday.tvHolidayText.setTextColor(ContextCompat.getColor(activity, R.color.green))
                    //setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.green);
                } else if (strHolidayName!!.contains("Dussehra")) {
                    setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.dussehra)
                    //viewHolderHoliday.container_for_InAnOutTime.setBackgroundColor(Color.parseColor("#f4e9f7"));
                    viewHolderHoliday.ivHolidayImage.setImageResource(R.drawable.dussehra)
                    viewHolderHoliday.tvHolidayText.setTextColor(ContextCompat.getColor(activity, android.R.color.holo_red_light))
                    // setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, android.R.color.holo_red_light);
                } else if (strHolidayName!!.contains("Mahatma Gandhi Jayanthi")) { //sets tint for whole layout
                    setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.gandhi_jayanti)
                    //sets tint for circled image view
/*setTintWithRes(viewHolderHoliday.ivHolidayIcon, R.color.orange);*/viewHolderHoliday.tvHolidayText.setTextColor(ContextCompat.getColor(activity, R.color.outer_border))
                    viewHolderHoliday.ivHolidayImage.setImageResource(R.drawable.mahatma_gandhi_jayanthi)
                    viewHolderHoliday.tvHolidayText.setTextColor(ContextCompat.getColor(activity, R.color.orange))
                    //setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.orange);
                } else if (strHolidayName!!.contains("Diwali")) { //sets tint for whole layout
                    setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.diwali)
                    //viewHolderHoliday.container_for_InAnOutTime.setBackgroundColor(Color.parseColor("#deefff"));
//sets tint for circled image view
/*setTintWithRes(viewHolderHoliday.ivHolidayIcon, R.color.orange);*/viewHolderHoliday.ivHolidayImage.setImageResource(R.drawable.diwali)
                    viewHolderHoliday.tvHolidayText.setTextColor(ContextCompat.getColor(activity, android.R.color.holo_blue_light))
                    //setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, android.R.color.holo_blue_light);
                } else if (strHolidayName!!.contains("Christmas")) {
                    setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.christmas)
                    //viewHolderHoliday.container_for_InAnOutTime.setBackgroundColor(Color.parseColor("#fde9eb"));
                    viewHolderHoliday.ivHolidayImage.setImageResource(R.drawable.christmas)
                    viewHolderHoliday.tvHolidayText.setTextColor(ContextCompat.getColor(activity, R.color.orange))
                    //setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.orange);
                } else {
                    setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.defaultColor)
                    // viewHolderHoliday.container_for_InAnOutTime.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    viewHolderHoliday.ivHolidayImage.setImageResource(R.drawable.holidays)
                    viewHolderHoliday.tvHolidayText.setTextColor(ContextCompat.getColor(activity, R.color.orange))
                    // setTintWithRes(viewHolderHoliday.container_for_InAnOutTime, R.color.orange);
                }
                if (position == mlstAttendanceData.size - 1) {
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(viewHolderHoliday.constraint_layout_holiday)
                    constraintSet.connect(viewHolderHoliday.vwUpperLine.id, ConstraintSet.BOTTOM, viewHolderHoliday.tvAttStatus.id, ConstraintSet.TOP, 0)
                    constraintSet.applyTo(viewHolderHoliday.constraint_layout_holiday)
                } else {
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(viewHolderHoliday.constraint_layout_holiday)
                    constraintSet.connect(viewHolderHoliday.vwUpperLine.id, ConstraintSet.BOTTOM, viewHolderHoliday.constraint_layout_holiday.id, ConstraintSet.BOTTOM, 0)
                    constraintSet.applyTo(viewHolderHoliday.constraint_layout_holiday)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            else -> {
                objAttendanceData = mlstAttendanceData[position]
                val vh4 = holder as AttendanceViewHolderRegular
                setTintWithRes(vh4.container_for_InAnOutTime, R.color.white)
            }
        }
    }

    fun setTintWithRes(view: View, resId: Int) {
        val bg = DrawableCompat.wrap(view.background)
        // Need to set the background with the wrapped drawable
        view.background = bg
        //imageView.setPadding(5,0,0,0);
// You can now tint the drawable
        DrawableCompat.setTint(bg, ContextCompat.getColor(activity, resId))
        //imageView.setBackgroundTintList(ContextCompat.getColor(activity,resId));
//imageView.setBackgroundTintList(ContextCompat.getColorStateList(activity, resId));
//
//        NinePatchDrawable ninepatch = null;
//        Bitmap image = BitmapFactory.decodeResource(activity.getResources(),R.drawable.patch);
//        if (image.getNinePatchChunk()!=null){
//            byte[] chunk = image.getNinePatchChunk();
//            Rect paddingRectangle = new Rect(0, 0, 0, 0);
//            ninepatch = new NinePatchDrawable(activity.getResources(), image, chunk, paddingRectangle, null);
//            ninepatch.setColorFilter(resId, PorterDuff.Mode.SRC_IN);
//        }
//        int sdk = android.os.Build.VERSION.SDK_INT;
//        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//            imageView.setBackgroundDrawable(ninepatch);
//        } else {
//            imageView.setBackground(ninepatch);
//        }
//        DrawableCompat.setTint(activity.getResources().getDrawable(R.drawable.patch,null), ContextCompat.getColor(activity, resId));
//        DrawableCompat.setTintList(activity.getResources().getDrawable(R.drawable.patch,null), ContextCompat.getColorStateList(activity, resId));
    }

    override fun getItemViewType(position: Int): Int {
        return if (mlstAttendanceData[position].viewType == AttendanceData.NORMAL) {
            AttendanceData.NORMAL
        } else if (mlstAttendanceData[position].viewType == AttendanceData.LEAVE) {
            AttendanceData.LEAVE
        } else if (mlstAttendanceData[position].viewType == AttendanceData.HOLIDAY) {
            AttendanceData.HOLIDAY
        } else {
            AttendanceData.NORMAL
        }
    }

    override fun getItemCount(): Int {
        return mlstAttendanceData.size
    }

    inner class AttendanceViewHolderRegular(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val container_for_InAnOutTime: RelativeLayout
        val tvAttendanceDate: TextView
        val tvAttendanceDay: TextView
        val tvAttStatus: TextView
        val tvInTime: TextView
        val tvOutTime: TextView
        val tvTotalHourWorked: TextView
        val tvTimesheet: TextView
        val vwUpperLine: View
        val constraint_layout_normal: ConstraintLayout

        init {
            container_for_InAnOutTime = itemView.findViewById(R.id.container_for_InAnOutTime)
            tvAttendanceDate = itemView.findViewById(R.id.tvAttendanceDate)
            tvAttendanceDay = itemView.findViewById(R.id.tvAttendanceDay)
            tvAttStatus = itemView.findViewById(R.id.tvAttStatus)
            tvInTime = itemView.findViewById(R.id.tvInTime)
            tvOutTime = itemView.findViewById(R.id.tvOutTime)
            tvTotalHourWorked = itemView.findViewById(R.id.tvTotalHourWorked)
            tvTimesheet = itemView.findViewById(R.id.tvTimesheet)
            vwUpperLine = itemView.findViewById(R.id.upperLine)
            constraint_layout_normal = itemView.findViewById(R.id.constraint_layout_normal)
        }
    }

    inner class AttendanceViewHolderLeave(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val container_for_InAnOutTime: RelativeLayout
        val tvAttendanceDate: TextView
        val tvAttendanceDay: TextView
        val tvAttStatus: TextView
        val tvLeaveText: TextView
        val vwUpperLine: View
        val constraint_layout_leave: ConstraintLayout

        init {
            container_for_InAnOutTime = itemView.findViewById(R.id.container_for_InAnOutTime)
            tvAttendanceDate = itemView.findViewById(R.id.tvAttendanceDate)
            tvAttendanceDay = itemView.findViewById(R.id.tvAttendanceDay)
            tvAttStatus = itemView.findViewById(R.id.tvAttStatus)
            tvLeaveText = itemView.findViewById(R.id.tvLeaveText)
            vwUpperLine = itemView.findViewById(R.id.upperLine)
            constraint_layout_leave = itemView.findViewById(R.id.constraint_layout_leave)
        }
    }

    inner class AttendanceViewHolderHoliday(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val container_for_InAnOutTime: RelativeLayout
        val tvAttendanceDate: TextView
        val tvAttStatus: TextView
        val tvAttendanceDay: TextView
        val   /*ivHolidayIcon,*/ivHolidayImage: ImageView
        val tvHolidayText: TextView
        val vwUpperLine: View
        val constraint_layout_holiday: ConstraintLayout

        init {
            container_for_InAnOutTime = itemView.findViewById(R.id.container_for_InAnOutTime)
            tvAttendanceDate = itemView.findViewById(R.id.tvAttendanceDate)
            tvAttendanceDay = itemView.findViewById(R.id.tvAttendanceDay)
            tvAttStatus = itemView.findViewById(R.id.tvAttStatus)
            //ivHolidayIcon = itemView.findViewById(R.id.ivHolidayIcon);
            tvHolidayText = itemView.findViewById(R.id.tvHolidayText)
            ivHolidayImage = itemView.findViewById(R.id.ivHolidayImage)
            vwUpperLine = itemView.findViewById(R.id.upperLine)
            constraint_layout_holiday = itemView.findViewById(R.id.constraint_layout_holiday)
        }
    } /*private void setRowForHoliday(RelativeLayout rowContainer, ImageView imageViewRightHoliday, String strHolidayName) {
        if (strHolidayName.contains("Republic Day")) {
            //rowContainer.setBackgroundColor(Color.parseColor("#f8efea"));
            imageViewRightHoliday.setImageResource(R.drawable.republic_day);
            setTintWithRes(rowContainer, R.color.republic_day);
        } else if (strHolidayName.contains("Holi")) {
            // rowContainer.setBackgroundColor(Color.parseColor("#deefff"));
            imageViewRightHoliday.setImageResource(R.drawable.holi);
            setTintWithRes(rowContainer, android.R.color.holo_blue_light);
        } else if (strHolidayName.contains("Good Friday")) {
            // rowContainer.setBackgroundColor(Color.parseColor("#e9f7f7"));
            imageViewRightHoliday.setImageResource(R.drawable.good_friday);
            setTintWithRes(rowContainer, R.color.green);
        } else if (strHolidayName.contains("Maharashtra Day")) {
            //  rowContainer.setBackgroundColor(Color.parseColor("#f4e9f7"));
            imageViewRightHoliday.setImageResource(R.drawable.maharashtra_day);
            setTintWithRes(rowContainer, android.R.color.holo_red_light);
        } else if (strHolidayName.contains("Eid (Subject to change)")) {
            // rowContainer.setBackgroundColor(Color.parseColor("#edf4ec"));
            imageViewRightHoliday.setImageResource(R.drawable.eid);
            setTintWithRes(rowContainer, android.R.color.holo_green_dark);
        } else if (strHolidayName.contains("Independence Day")) {
            // rowContainer.setBackgroundColor(Color.parseColor("#f0f0f0"));
            imageViewRightHoliday.setImageResource(R.drawable.independence_day);
            setTintWithRes(rowContainer, R.color.orange);
        } else if (strHolidayName.contains("Ganesh Chaturthi")) {
            // rowContainer.setBackgroundColor(Color.parseColor("#e9f7f7"));
            imageViewRightHoliday.setImageResource(R.drawable.ganesh_chaturthi);
            setTintWithRes(rowContainer, R.color.green);
        } else if (strHolidayName.contains("Dussehra")) {
            // rowContainer.setBackgroundColor(Color.parseColor("#f4e9f7"));
            imageViewRightHoliday.setImageResource(R.drawable.dussehra);
            setTintWithRes(rowContainer, android.R.color.holo_red_light);
        } else if (strHolidayName.contains("Mahatma Gandhi Jayanthi")) {
            // rowContainer.setBackgroundColor(Color.parseColor("#efefef"));
            imageViewRightHoliday.setImageResource(R.drawable.mahatma_gandhi_jayanthi);
            setTintWithRes(rowContainer, R.color.orange);
            setTintWithRes(imageViewRightHoliday, R.color.orange);
        } else if (strHolidayName.contains("Diwali")) {
            // rowContainer.setBackgroundColor(Color.parseColor("#deefff"));
            imageViewRightHoliday.setImageResource(R.drawable.diwali);
            setTintWithRes(rowContainer, android.R.color.holo_blue_light);
        } else if (strHolidayName.contains("Christmas")) {
            // rowContainer.setBackgroundColor(Color.parseColor("#fde9eb"));
            imageViewRightHoliday.setImageResource(R.drawable.christmas);
            setTintWithRes(rowContainer, R.color.orange);
        } else {
            //rowContainer.setBackgroundColor(Color.parseColor("#f8f8f8"));
            imageViewRightHoliday.setImageResource(R.drawable.holidays);
            setTintWithRes(rowContainer, R.color.orange);
        }

    }*/

    companion object {
        fun setTint(drawable: Drawable?, color: Int): Drawable {
            val newDrawable = DrawableCompat.wrap(drawable!!)
            DrawableCompat.setTint(newDrawable, color)
            return newDrawable
        }
    }

}