package com.intelegain.agora.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;


import com.intelegain.agora.R;
import com.intelegain.agora.model.OccassionsData;

import java.util.ArrayList;

public class OccassionPagerAdapter extends PagerAdapter {


    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OccassionsData objOccassionsData;
    private int[] mResources = {
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher
    };

    ArrayList<OccassionsData> mOccassionsDataList = new ArrayList<>();

    public OccassionPagerAdapter(Context context, ArrayList<OccassionsData> occassionsData) {
        mContext = context;
        mOccassionsDataList = occassionsData;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mOccassionsDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.occassions_view, container, false);

        ImageView imgOccProfile = itemView.findViewById(R.id.imgOccProfile);

        TextView tvOccassionFor = itemView.findViewById(R.id.tvOccassionFor);
        TextView tvOccassionDate = itemView.findViewById(R.id.tvOccassionDate);
        TextView tvOccassionName = itemView.findViewById(R.id.tvOccassionName);
        TextView tvNoOccassionFound = itemView.findViewById(R.id.tvNoOccassionFound);
        LinearLayout linearLayoutTitleContainer = itemView.findViewById(R.id.linear_title_container);

        ImageView imgBirthdayIcon = itemView.findViewById(R.id.imgBirthdayIcon);


        if (mOccassionsDataList.size() == 1) {
            if (mOccassionsDataList.get(0).getOccassionFor().equals("No Occasion Found")
                    && mOccassionsDataList.get(0).getOccassionDate().equals("")
                    && mOccassionsDataList.get(0).getOccassionName().equals("")) {
                linearLayoutTitleContainer.setVisibility(View.GONE);
                imgOccProfile.setVisibility(View.GONE);
                imgBirthdayIcon.setVisibility(View.GONE);
                tvNoOccassionFound.setVisibility(View.VISIBLE);
            } else if (mOccassionsDataList.size() > 0) {
                objOccassionsData = mOccassionsDataList.get(position);
                tvOccassionFor.setText(objOccassionsData.getOccassionFor());
                tvOccassionName.setText(objOccassionsData.getOccassionName());
                tvOccassionDate.setText(objOccassionsData.getOccassionDate());

                linearLayoutTitleContainer.setVisibility(View.VISIBLE);
                imgOccProfile.setVisibility(View.VISIBLE);
                imgBirthdayIcon.setVisibility(View.VISIBLE);
                tvNoOccassionFound.setVisibility(View.GONE);
            }
        } else {
            objOccassionsData = mOccassionsDataList.get(position);
            tvOccassionFor.setText(objOccassionsData.getOccassionFor());
            tvOccassionName.setText(objOccassionsData.getOccassionName());
            tvOccassionDate.setText(objOccassionsData.getOccassionDate());

            linearLayoutTitleContainer.setVisibility(View.VISIBLE);
            imgOccProfile.setVisibility(View.VISIBLE);
            imgBirthdayIcon.setVisibility(View.VISIBLE);
            tvNoOccassionFound.setVisibility(View.GONE);
        }

        //imgOccProfile.setImageResource(objOccassionsData.getOccasionImageUrl()); // Set with glide
        //imgOccProfile.setImageResource(mResources[position]);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
