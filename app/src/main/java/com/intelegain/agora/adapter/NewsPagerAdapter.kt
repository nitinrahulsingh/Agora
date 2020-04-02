package com.intelegain.agora.adapter

import android.content.Context
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.intelegain.agora.R
import com.intelegain.agora.model.News
import java.util.*

/**
 * Created by suraj.m on 15/7/17.
 */
class NewsPagerAdapter(var mContext: Context, objNewsDataList: ArrayList<News>) : PagerAdapter() {
    var mLayoutInflater: LayoutInflater
    var news: News? = null
    var mNewsTitle = arrayOf(
            "Win cash prizes at Mock Trading",
            "Win cash prizes at Mock Trading",
            "Win cash prizes at Mock Trading"
    )
    var mNewsDate = arrayOf(
            "28 July 2017",
            "29 August 2017",
            "30 September 2017"
    )
    var mNewsDataList = ArrayList<News>()
    override fun getCount(): Int {
        return mNewsDataList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = mLayoutInflater.inflate(R.layout.news_view, container, false)
        val tvNewsTitle = itemView.findViewById<TextView>(R.id.tvNewsTitle)
        val tvNewsDate = itemView.findViewById<TextView>(R.id.tvNewsDate)
        if (mNewsDataList.size == 1) {
            if (mNewsDataList[0].newsTitle == "" && mNewsDataList[0].newsDate == "No news available.") {
                tvNewsDate.text = mNewsDataList[0].newsDate
                tvNewsTitle.visibility = View.GONE
            } else { //String str = "Win cash prizes at <a href='https://www.w3schools.com'>Mock Trading </a>";
//tvNewsTitle.setText(Html.fromHtml(str));
                try {
                    tvNewsTitle.text = Html.fromHtml(mNewsDataList[0].newsTitle)
                    tvNewsTitle.isClickable = true
                    if (mNewsDataList[0].newsTitle!!.contains("http")) {
                        tvNewsTitle.movementMethod = LinkMovementMethod.getInstance()
                    } else { // Toast.makeText(mContext, ""+ Contants2.NEWS_LINK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
                    }
                    tvNewsDate.text = mNewsDataList[0].newsDate
                    tvNewsTitle.visibility = View.VISIBLE
                } catch (e: RuntimeException) {
                    e.printStackTrace()
                }
            }
        } else if (mNewsDataList.size > 0) {
            try {
                tvNewsTitle.visibility = View.VISIBLE
                news = News()
                news = mNewsDataList[position]
                tvNewsTitle.text = Html.fromHtml(mNewsDataList[0].newsTitle)
                tvNewsTitle.isClickable = true
                if (mNewsDataList[0].newsTitle!!.contains("http")) {
                    tvNewsTitle.movementMethod = LinkMovementMethod.getInstance()
                } else { // Toast.makeText(mContext, ""+Contants2.NEWS_LINK_UNAVAILABLE, Toast.LENGTH_SHORT).show();
                }
                tvNewsDate.text = mNewsDataList[position].newsDate
            } catch (e: RuntimeException) {
                e.printStackTrace()
            }
        }
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    init {
        mNewsDataList = objNewsDataList
        mLayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}