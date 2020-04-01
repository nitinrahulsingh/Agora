package com.intelegain.agora.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.intelegain.agora.R;
import com.intelegain.agora.model.AttendanceData;
import com.intelegain.agora.model.CalanderDate;
import com.intelegain.agora.model.CalenderEvent;
import com.intelegain.agora.utils.CalendarUtil;
import com.intelegain.agora.utils.CalenderMonth;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by suraj on 11/7/17.
 */

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MonthViewHolder> {

    private static final String TAG = "MonthAdapter";
    private Activity activity;
    private ArrayList<CalanderDate> dateArrayList;
    private ArrayList<CalenderEvent> eventArrayList;
    public boolean isCalendarCollapsed = false;
    private ArrayList<AttendanceData> mobjDashboardData = new ArrayList<>();

    public int mIndex = 0;

    private int iCurrentDatePos;

    public MonthAdapter(Activity activity, ArrayList<CalanderDate> dateArrayList) {
        this.activity = activity;
        this.dateArrayList = dateArrayList;
    }

    @Override
    public MonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_month, parent, false);
        return new MonthViewHolder(view);
    }

    public int getDateScrollPos() {
        return iCurrentDatePos;
    }

    @Override
    public void onBindViewHolder(MonthViewHolder holder, int position) {
        //Fri Jul 14 17:46:56 GMT+05:30 2017
        Date objDate = dateArrayList.get(position).getDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(objDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String strCurrDate = String.valueOf(day);
        String strInTime = "";
        String strOutTime = "";
        if (dateArrayList.get(position).getCalenderMonth() == CalenderMonth.CURRENT) {
            if (mobjDashboardData != null && mIndex < mobjDashboardData.size()) {

                AttendanceData objDashboardData = mobjDashboardData.get(mIndex);
                strInTime = objDashboardData.attInTime;
                strOutTime = objDashboardData.attOutTime;

                if (Integer.valueOf(objDashboardData.getViewType()) == AttendanceData.NORMAL) {
                    if (getTodayDate().after(objDate)) {
                        holder.dateTv.setTextColor(Color.WHITE);
                        if (objDashboardData.getAttStatus().equalsIgnoreCase("p"))
                            setTintWithRes(holder.dateCircle, R.color.orange);
                        else if (objDashboardData.getAttStatus().equalsIgnoreCase("HD"))
                            setTintWithRes(holder.dateCircle, R.color.popup_red);
                    }
                } else if (Integer.valueOf(objDashboardData.getViewType()) == AttendanceData.LEAVE) {
                    if (objDashboardData.getDescription().equalsIgnoreCase("Weekend"))
                        holder.dateTv.setTextColor(Color.GRAY);
                    else if (objDashboardData.getDescription().equalsIgnoreCase("Absent")) {
                        holder.dateTv.setTextColor(Color.WHITE);
                        setTintWithRes(holder.dateCircle, R.color.popup_red);
                    } else {
                        setTintWithRes(holder.dateCircle, R.color.popup_red);
                        holder.dateTv.setTextColor(Color.WHITE);
                    }
                } else if (Integer.valueOf(objDashboardData.getViewType()) == AttendanceData.HOLIDAY) {
                    holder.dateTv.setTextColor(Color.GRAY);
                    holder.dateCircle.setImageResource(R.drawable.bg_with_dot);
                    setTintWithRes(holder.dateCircle, R.color.transparent);
                } else {
                    holder.dateTv.setTextColor(Color.WHITE);
                    setTintWithRes(holder.dateCircle, R.color.orange);
                }

                if ((!TextUtils.isEmpty(strCurrDate)) && (getTodayDate().after(objDate))
                        && !objDashboardData.getDescription().equalsIgnoreCase("Weekend"))
                    holder.itemView.setOnClickListener(new CustomDateClickListner(position, strCurrDate, strInTime,
                            strOutTime, objDashboardData.getViewType(), objDashboardData.getDescription(), objDashboardData));
                mIndex = mIndex + 1;
            }
        } else {
            mIndex = 0; // Reset if not for current month
            holder.dateTv.setTextColor(Color.GRAY);
            setTintWithRes(holder.dateCircle, R.color.new_dark_grey);
        }
        holder.dateTv.setText(dateArrayList.get(position).getDateStr());

        // Set the current date with green background
        int iCurrentMonth = CalendarUtil.getCurrentMonthInInteger();
        if (month == iCurrentMonth) {
            int iCurrentDay = CalendarUtil.getCurrentDayInInteger();
            if (day == iCurrentDay) {
                int iCurrentYear = CalendarUtil.getCurrentYearInInteger();
                if (year == iCurrentYear)
                    setTintWithRes(holder.dateCircle, R.color.green);
                iCurrentDatePos = position;
            }
        }

    }

    @Override
    public int getItemCount() {
        return dateArrayList.size();
    }

//    public void setEventArrayList(ArrayList<CalenderEvent> eventArrayList) {
//        this.eventArrayList = eventArrayList;
//        notifyDataSetChanged();
//    }

    public void clearData() {
        dateArrayList.clear();
//        if (eventArrayList != null) {
//            eventArrayList.clear();
//        }
        notifyDataSetChanged();
    }

    public void addDates(ArrayList<CalanderDate> mDateArrayList) {
        dateArrayList.addAll(mDateArrayList);
    }

    public void setData(ArrayList<AttendanceData> objDashboardData) {

        mobjDashboardData = objDashboardData;
        notifyDataSetChanged();
        /*if (mobjDashboardData != null && mobjDashboardData.size() > 0) {
            mobjDashboardData.clear();
            mobjDashboardData.addAll(objDashboardData);
        } else {
            mobjDashboardData.addAll(objDashboardData);
        }*/


    }

    public void setTintWithRes(ImageView imageView, int resId) {
        imageView.setColorFilter(ContextCompat.getColor(activity, resId));
    }

    PopupWindow mpopup = null;
    /**
     * Showing popup menu when tapping on day
     */
    private void showPopupMenu(String strCurrDate, int x, int y, String strInTime, String strOutTime, int viewType, String description, AttendanceData objDashboardData) {

        View popUpView;
        TextView tvDay, tvInTime, tvOutTime, tvLeave;

        AttendanceData dashboardData = objDashboardData;
//        0 - Normal day
//        1 - leave
//        2 - holiday
//        required for current date

        if (viewType == 0) {

            popUpView = activity.getLayoutInflater().inflate(R.layout.popup_dialog_for_date, null);
            RelativeLayout rl_dt_dialog = popUpView.findViewById(R.id.rl_dt_dialog);

            if (dashboardData.getAttStatus().equalsIgnoreCase("p")) {

                if (dashboardData.getAttendanceDate().substring(3, 5).equalsIgnoreCase(new DecimalFormat("00").format(CalendarUtil.getCurrentMonthInInteger() + 1))) {
                    if (dashboardData.getDate().equalsIgnoreCase(String.valueOf(CalendarUtil.getCurrentDayInInteger())))
                        rl_dt_dialog.setBackgroundResource(R.drawable.popup_bg_green);//set the color for current date
                    else
                        rl_dt_dialog.setBackgroundResource(R.drawable.popup_bg_yellow);
                } else {
                    rl_dt_dialog.setBackgroundResource(R.drawable.popup_bg_yellow);
                }

            } else if (dashboardData.getAttStatus().equalsIgnoreCase("HD")) {
                if (dashboardData.getAttendanceDate().substring(3, 5).equalsIgnoreCase(new DecimalFormat("00").format(CalendarUtil.getCurrentMonthInInteger() + 1))) {
                    if (dashboardData.getDate().equalsIgnoreCase(String.valueOf(CalendarUtil.getCurrentDayInInteger())))
                        rl_dt_dialog.setBackgroundResource(R.drawable.popup_bg_green);//set the color for current date
                } else {
                    rl_dt_dialog.setBackgroundResource(R.drawable.popup_bg);
                }
            } else {
            }
            tvDay = popUpView.findViewById(R.id.tvDay);
            tvInTime = popUpView.findViewById(R.id.tvInTime);
            tvOutTime = popUpView.findViewById(R.id.tvOutTime);
            tvDay.setText(strCurrDate);
            tvInTime.setText("IN: " + strInTime);
            tvOutTime.setText("OUT: " + strOutTime);

            rl_dt_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mpopup != null && mpopup.isShowing())
                        mpopup.dismiss();
                }
            });

        } else if (viewType == 2) {
            //holiday view inflated here
            popUpView = activity.getLayoutInflater().inflate(R.layout.popup_dialog_for_leave, null);

            RelativeLayout rl_dt_dialog = popUpView.findViewById(R.id.rl_dt_dialog);

            tvDay = popUpView.findViewById(R.id.tvDay);
            tvLeave = popUpView.findViewById(R.id.tvLeave);
            tvLeave.setText(description);
            tvDay.setText(strCurrDate);

            rl_dt_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mpopup != null && mpopup.isShowing())
                        mpopup.dismiss();
                }
            });

        } else if (viewType == 1) {
            //normal view inflated here
            popUpView = activity.getLayoutInflater().inflate(R.layout.popup_dialog_for_date, null);

            RelativeLayout rl_dt_dialog = popUpView.findViewById(R.id.rl_dt_dialog);
            tvDay = popUpView.findViewById(R.id.tvDay);
            tvInTime = popUpView.findViewById(R.id.tvInTime);
            tvOutTime = popUpView.findViewById(R.id.tvOutTime);
            tvDay.setText(strCurrDate);

            tvInTime.setText("IN: " + strInTime);
            tvOutTime.setText("OUT: " + strOutTime);

            rl_dt_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mpopup != null && mpopup.isShowing())
                        mpopup.dismiss();
                }
            });

        } else {
            popUpView = activity.getLayoutInflater().inflate(R.layout.popup_dialog_for_date, null);

            RelativeLayout rl_dt_dialog = popUpView.findViewById(R.id.rl_dt_dialog);
            tvDay = popUpView.findViewById(R.id.tvDay);
            tvInTime = popUpView.findViewById(R.id.tvInTime);
            tvOutTime = popUpView.findViewById(R.id.tvOutTime);
            tvDay.setText(strCurrDate);

            tvInTime.setText("IN: " + strInTime);
            tvOutTime.setText("OUT: " + strOutTime);

            rl_dt_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mpopup != null && mpopup.isShowing())
                        mpopup.dismiss();
                }
            });
        }

        mpopup = new PopupWindow(popUpView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true); // Creation of popup

        mpopup.setAnimationStyle(android.R.style.Animation_Translucent);
        mpopup.setBackgroundDrawable(new ColorDrawable(
                Color.TRANSPARENT));
        mpopup.showAtLocation(popUpView, Gravity.NO_GRAVITY, x, y); // Displaying popup
    }

    public class MonthViewHolder extends RecyclerView.ViewHolder {
        TextView dateTv;
        ImageView dateCircle;

        public MonthViewHolder(View itemView) {
            super(itemView);
            dateTv = itemView.findViewById(R.id.dateTv);
            dateCircle = itemView.findViewById(R.id.img_date_circle);
        }
    }

    /**
     * On date click listener event will be handled here
     */
    private class CustomDateClickListner implements View.OnClickListener {
        private int iPosition, viewType;
        private String strCurrDate;
        private String strInTime, strOutTime, strDescription;

        AttendanceData objDashboardData;

        public CustomDateClickListner(int iPosition, String strCurrDate, String strInTime, String strOutTime,
                                      int viewType, String description, AttendanceData objDashboardData) {
            this.iPosition = iPosition;
            this.strCurrDate = strCurrDate;
            this.strInTime = strInTime;
            this.strOutTime = strOutTime;
            this.viewType = viewType;
            this.strDescription = description;
            this.objDashboardData = objDashboardData;
        }

        @Override
        public void onClick(View v) {
            int[] originalPos = new int[2];
            v.getLocationInWindow(originalPos);
            int x = originalPos[0] + 5;
            int y = originalPos[1] + 5;

            showPopupMenu(strCurrDate, x, y, strInTime, strOutTime, viewType, strDescription, objDashboardData);
        }
    }

    /**
     * Get today date
     */
    private Date getTodayDate() {
        return Calendar.getInstance().getTime();
    }
}
