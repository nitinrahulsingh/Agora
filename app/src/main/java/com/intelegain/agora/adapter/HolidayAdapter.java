package com.intelegain.agora.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.intelegain.agora.R;
import com.intelegain.agora.model.HolidayData;

import java.util.ArrayList;

public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.HolidayViewHolder> {

    private String TAG = getClass().getSimpleName();
    private Activity activity;
    private ArrayList<HolidayData> mlstHolidayData;

    public HolidayAdapter(Activity activity, ArrayList<HolidayData> lstHolidayData) {
        this.activity = activity;
        this.mlstHolidayData = lstHolidayData;
    }

    @Override
    public HolidayAdapter.HolidayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_holiday, parent, false);
        return new HolidayAdapter.HolidayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HolidayAdapter.HolidayViewHolder holder, int position) {

        HolidayData objHolidayData = mlstHolidayData.get(position);
        // set Day
        holder.tvHolidayDay.setText(objHolidayData.getHolidayDay());
        // set Month
        holder.tvHolidayMonth.setText(objHolidayData.getHolidayMonth());
        // set holiday name
        String strHolidayName = objHolidayData.getHolidayName() + "";
        holder.tvHolidayName.setText(strHolidayName);
        // set holiday image , circle color and row background
        int iRowId = objHolidayData.getImageId();
        if (strHolidayName.contains("Republic Day")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#f8efea"));
            holder.ivHolidayImage.setImageResource(R.drawable.republic_day);
            setTintWithRes(holder.ivBgCircle, R.color.yellow);
        } else if (strHolidayName.contains("Holi")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#deefff"));
            holder.ivHolidayImage.setImageResource(R.drawable.holi);
            setTintWithRes(holder.ivBgCircle, android.R.color.holo_blue_light);
        } else if (strHolidayName.contains("Good Friday")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#e9f7f7"));
            holder.ivHolidayImage.setImageResource(R.drawable.good_friday);
            setTintWithRes(holder.ivBgCircle, R.color.green);
        } else if (strHolidayName.contains("Maharashtra Day")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#f4e9f7"));
            holder.ivHolidayImage.setImageResource(R.drawable.maharashtra_day);
            setTintWithRes(holder.ivBgCircle, android.R.color.holo_red_light);
        } else if (strHolidayName.contains("Eid (Subject to change)")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#edf4ec"));
            holder.ivHolidayImage.setImageResource(R.drawable.eid);
            setTintWithRes(holder.ivBgCircle, android.R.color.holo_green_dark);
        } else if (strHolidayName.contains("Independence Day")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#f0f0f0"));
            holder.ivHolidayImage.setImageResource(R.drawable.independence_day);
            setTintWithRes(holder.ivBgCircle, R.color.orange);
        } else if (strHolidayName.contains("Ganesh Chaturthi")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#e9f7f7"));
            holder.ivHolidayImage.setImageResource(R.drawable.ganesh_chaturthi);
            setTintWithRes(holder.ivBgCircle, R.color.green);
        } else if (strHolidayName.contains("Dussehra")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#f4e9f7"));
            holder.ivHolidayImage.setImageResource(R.drawable.dussehra);
            setTintWithRes(holder.ivBgCircle, android.R.color.holo_red_light);
        } else if (strHolidayName.contains("Mahatma Gandhi Jayanthi")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#efefef"));
            holder.ivHolidayImage.setImageResource(R.drawable.mahatma_gandhi_jayanthi);
            setTintWithRes(holder.ivBgCircle, R.color.orange);
        } else if (strHolidayName.contains("Diwali")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#deefff"));
            holder.ivHolidayImage.setImageResource(R.drawable.diwali);
            setTintWithRes(holder.ivBgCircle, android.R.color.holo_blue_light);
        } else if (strHolidayName.contains("Christmas")) {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#fde9eb"));
            holder.ivHolidayImage.setImageResource(R.drawable.christmas);
            setTintWithRes(holder.ivBgCircle, R.color.orange);
        } else {
            holder.holiday_row_background.setBackgroundColor(Color.parseColor("#f8f8f8"));
            holder.ivHolidayImage.setImageResource(R.drawable.holidays);
            setTintWithRes(holder.ivBgCircle, R.color.orange);
        }
    }

    @Override
    public int getItemCount() {
        return mlstHolidayData.size();
    }

    public class HolidayViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout holiday_row_background;
        private TextView tvHolidayDay;
        private TextView tvHolidayMonth;
        private TextView tvHolidayName;
        private ImageView ivHolidayImage, ivBgCircle;

        public HolidayViewHolder(View itemView) {
            super(itemView);
            holiday_row_background = itemView.findViewById(R.id.holiday_row_background);
            ivHolidayImage = itemView.findViewById(R.id.ivHolidayImage);
            ivBgCircle = itemView.findViewById(R.id.ivBgCircle);
            tvHolidayDay = itemView.findViewById(R.id.tvHolidayDay);
            tvHolidayMonth = itemView.findViewById(R.id.tvHolidayMonth);
            tvHolidayName = itemView.findViewById(R.id.tvHolidayName);
        }
    }

    public void setTintWithRes(ImageView imageView, int resId) {
        imageView.setColorFilter(ContextCompat.getColor(activity, resId));
    }
}
