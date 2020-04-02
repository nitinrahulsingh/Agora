package com.intelegain.agora.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.intelegain.agora.R
import com.intelegain.agora.model.CIPSession
import java.util.*

/**
 * Created by suraj.m on 15/7/17.
 */
class CipSessionPagerAdapter(var mContext: Context, objCipSessionDataList: ArrayList<CIPSession>) : PagerAdapter() {
    var mLayoutInflater: LayoutInflater
    var cipSession: CIPSession? = null
    var mCipSessionDataList = ArrayList<CIPSession>()
    override fun getCount(): Int {
        return mCipSessionDataList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = mLayoutInflater.inflate(R.layout.cip_session_view, container, false)
        val tvCipSessionTitle = itemView.findViewById<TextView>(R.id.tvCipSessionTitle)
        if (mCipSessionDataList.size == 1) {
            if (mCipSessionDataList[0].cIPSessionsInfo == "No CIP sessions available.") {
                tvCipSessionTitle.text = mCipSessionDataList[0].cIPSessionsInfo
            } else {
                tvCipSessionTitle.text = mCipSessionDataList[0].cIPSessionsInfo
            }
        } else if (mCipSessionDataList.size > 0) {
            cipSession = mCipSessionDataList[position]
            tvCipSessionTitle.text = cipSession!!.cIPSessionsInfo
        }
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    init {
        mCipSessionDataList = objCipSessionDataList
        mLayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}