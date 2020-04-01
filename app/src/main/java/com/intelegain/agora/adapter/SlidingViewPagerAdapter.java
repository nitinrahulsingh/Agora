package com.intelegain.agora.adapter;

import android.app.Activity;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.intelegain.agora.R;
import com.intelegain.agora.model.CalanderDate;
import com.intelegain.agora.utils.CalenderMonth;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @author suraj.m
 */
public class SlidingViewPagerAdapter extends PagerAdapter {
    private String TAG = getClass().getSimpleName();
    private Context context;
    private ArrayList<String> monthYearArrayList;
    public MonthAdapter monthAdapter;
    int yearServerRecieved;

    public ArrayList<MonthAdapter> monthAdapterArrayList;
    public static boolean bIsFirstTime = true;

    public SlidingViewPagerAdapter(Context context, ArrayList<String> monthYearArrayList, int yearServerRecieved) {
        this.context = context;
        this.monthYearArrayList = monthYearArrayList;
        this.yearServerRecieved = yearServerRecieved;
        this.monthAdapterArrayList = generateMonthAdapterArrayList(yearServerRecieved);
    }


    @Override
    public int getCount() {
        return monthYearArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_month_slide, container, false);
        MyViewHolder holder = new MyViewHolder(v);
        holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 7));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        try {
            Date date = dateFormat.parse(monthYearArrayList.get(position));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Log.e(TAG, "Date: " + date.toString());

            MonthAdapter monthAdapter1 = new MonthAdapter((Activity) context, generateStringMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)));
            holder.recyclerView.setAdapter(monthAdapter1);
            monthAdapterArrayList.set(position, monthAdapter1);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        v.setTag(position);
        container.addView(v);

        return v;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public class MyViewHolder {
        RecyclerView recyclerView;

        public MyViewHolder(View v) {
            recyclerView = v.findViewById(R.id.month_recyView);
        }
    }

    private ArrayList<CalanderDate> generateStringMonth(int month, int year) {
        ArrayList<CalanderDate> stringArrayList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);

        Calendar previousMonth = Calendar.getInstance();
        previousMonth.set(Calendar.MONTH, month);
        previousMonth.set(Calendar.YEAR, year);
        previousMonth.add(Calendar.MONTH, -1);
        if (getFirstDay(month, year) != 1) {
            for (int nos = getLastSundayDateinMonth(previousMonth.get(Calendar.MONTH), previousMonth.get(Calendar.YEAR)); nos <= previousMonth.getActualMaximum(Calendar.DAY_OF_MONTH); nos++) {
                previousMonth.set(Calendar.DATE, nos);
                boolean isSunday = previousMonth.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
                stringArrayList.add(new CalanderDate("" + nos, isSunday, previousMonth.getTime(), CalenderMonth.PREVIOUS));
            }
        }

        for (int no = 1; no <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); no++) {
            Calendar currentMonthDate = Calendar.getInstance();
            currentMonthDate.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), no);
            boolean isSunday = currentMonthDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
            stringArrayList.add(new CalanderDate(no + "", isSunday, currentMonthDate.getTime(), CalenderMonth.CURRENT));
        }

        Calendar nextMonth = Calendar.getInstance();
        nextMonth.set(Calendar.YEAR, year);
        nextMonth.set(Calendar.MONTH, month);
        nextMonth.add(Calendar.MONTH, 1);

        //int satCount = (!isLastDayIsSaturday(month,year)&&(((stringArrayList.size())/7)<5.01))?2:1;
        int satCount = 6 - (stringArrayList.size() / 7);
        for (int no = 1; no <= getSaturadayDate(nextMonth.get(Calendar.MONTH), nextMonth.get(Calendar.YEAR), satCount); no++) {
            nextMonth.set(Calendar.DATE, no);
            boolean isSunday = nextMonth.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
            stringArrayList.add(new CalanderDate(no + "", isSunday, nextMonth.getTime(), CalenderMonth.NEXT));
        }
        return stringArrayList;
    }

    private int getFirstDay(int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    private int getNoOfSundayinMonth(int month, int year) {
        int count = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        for (int no = 1; no <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); no++) {
            calendar.set(Calendar.DATE, no);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                count++;
            }
        }
        return count;
    }

    private int getLastSundayDateinMonth(int month, int year) {
        int count = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        for (int no = 1; no <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); no++) {
            calendar.set(Calendar.DATE, no);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                count++;
                if (count == getNoOfSundayinMonth(month, year)) {
                    return no;
                }
            }
        }
        return count;
    }

    private boolean isLastDayIsSaturday(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return true;
        }
        return false;
    }

    private int getSaturadayDate(int month, int year, int saturdayNo) {
        int count = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        for (int no = calendar.getActualMinimum(Calendar.DATE); no <= calendar.getActualMaximum(Calendar.DATE); no++) {
            calendar.set(Calendar.DATE, no);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                count++;
                if (count == saturdayNo) {
                    return no;
                }
            }
        }
        return 0;
    }


    public ArrayList<MonthAdapter> generateMonthAdapterArrayList(int yearServerRecieved) {
        monthAdapterArrayList = new ArrayList<>();
        for (String s : monthYearArrayList) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, yearServerRecieved);
            monthAdapterArrayList.add(new MonthAdapter((Activity) context, generateStringMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR))));
        }
        return monthAdapterArrayList;
   }

}
