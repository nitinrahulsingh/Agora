package com.intelegain.agora.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.intelegain.agora.R;
import com.intelegain.agora.model.CIPSession;


import java.util.ArrayList;

/**
 * Created by suraj.m on 15/7/17.
 */

public class CipSessionPagerAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;
    CIPSession cipSession;

    ArrayList<CIPSession> mCipSessionDataList = new ArrayList<>();

    public CipSessionPagerAdapter(Context context, ArrayList<CIPSession> objCipSessionDataList) {
        mContext = context;
        mCipSessionDataList = objCipSessionDataList;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mCipSessionDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.cip_session_view, container, false);
        TextView tvCipSessionTitle = itemView.findViewById(R.id.tvCipSessionTitle);

        if (mCipSessionDataList.size() == 1) {
            if (mCipSessionDataList.get(0).getCIPSessionsInfo().equals("No CIP sessions available.")) {
                tvCipSessionTitle.setText(mCipSessionDataList.get(0).getCIPSessionsInfo());
            } else {
                tvCipSessionTitle.setText(mCipSessionDataList.get(0).getCIPSessionsInfo());
            }
        } else if (mCipSessionDataList.size() > 0) {
            cipSession = mCipSessionDataList.get(position);
            tvCipSessionTitle.setText(cipSession.getCIPSessionsInfo());
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
