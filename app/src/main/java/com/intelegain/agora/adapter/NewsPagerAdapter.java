package com.intelegain.agora.adapter;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.intelegain.agora.R;
import com.intelegain.agora.model.News;


import java.util.ArrayList;

/**
 * Created by suraj.m on 15/7/17.
 */

public class NewsPagerAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;
    News news;
    String[] mNewsTitle = {
            "Win cash prizes at Mock Trading",
            "Win cash prizes at Mock Trading",
            "Win cash prizes at Mock Trading"
    };

    String[] mNewsDate = {
            "28 July 2017",
            "29 August 2017",
            "30 September 2017"
    };

    ArrayList<News> mNewsDataList = new ArrayList<>();

    public NewsPagerAdapter(Context context, ArrayList<News> objNewsDataList) {
        mContext = context;
        mNewsDataList = objNewsDataList;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mNewsDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.news_view, container, false);

        TextView tvNewsTitle = itemView.findViewById(R.id.tvNewsTitle);
        TextView tvNewsDate = itemView.findViewById(R.id.tvNewsDate);

        if (mNewsDataList.size() == 1) {
            if (mNewsDataList.get(0).getNewsTitle().equals("") && mNewsDataList.get(0).getNewsDate().equals("No news available.")) {
                tvNewsDate.setText(mNewsDataList.get(0).getNewsDate());
                tvNewsTitle.setVisibility(View.GONE);
            } else {
                //String str = "Win cash prizes at <a href='https://www.w3schools.com'>Mock Trading </a>";
                //tvNewsTitle.setText(Html.fromHtml(str));
                try {
                    tvNewsTitle.setText(Html.fromHtml(mNewsDataList.get(0).getNewsTitle()));
                    tvNewsTitle.setClickable(true);
                    if(mNewsDataList.get(0).getNewsTitle().contains("http")) {
                        tvNewsTitle.setMovementMethod(LinkMovementMethod.getInstance());
                    }else
                    {
                       // Toast.makeText(mContext, ""+ Contants2.NEWS_LINK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
                    }
                    tvNewsDate.setText(mNewsDataList.get(0).getNewsDate());
                    tvNewsTitle.setVisibility(View.VISIBLE);
                }catch (RuntimeException e){
                    e.printStackTrace();
                }
            }
        } else if (mNewsDataList.size() > 0) {
            try {
            tvNewsTitle.setVisibility(View.VISIBLE);
            news = new News();
            news = mNewsDataList.get(position);
            tvNewsTitle.setText(Html.fromHtml(mNewsDataList.get(0).getNewsTitle()));
            tvNewsTitle.setClickable(true);
            if(mNewsDataList.get(0).getNewsTitle().contains("http")) {
                tvNewsTitle.setMovementMethod(LinkMovementMethod.getInstance());
            }else
            {
               // Toast.makeText(mContext, ""+Contants2.NEWS_LINK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
            }
            tvNewsDate.setText(mNewsDataList.get(position).getNewsDate());
        }catch (RuntimeException e){
            e.printStackTrace();
        }
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}