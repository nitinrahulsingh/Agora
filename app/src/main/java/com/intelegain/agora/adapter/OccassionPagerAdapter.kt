package com.intelegain.agora.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.intelegain.agora.R
import com.intelegain.agora.model.OccassionsData
import java.util.*

class OccassionPagerAdapter(private val mContext: Context, occassionsData: ArrayList<OccassionsData>) : PagerAdapter() {
    private val mLayoutInflater: LayoutInflater
    private var objOccassionsData: OccassionsData? = null
    private val mResources = intArrayOf(
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher
    )
    var mOccassionsDataList = ArrayList<OccassionsData>()
    override fun getCount(): Int {
        return mOccassionsDataList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = mLayoutInflater.inflate(R.layout.occassions_view, container, false)
        val imgOccProfile = itemView.findViewById<ImageView>(R.id.imgOccProfile)
        val tvOccassionFor = itemView.findViewById<TextView>(R.id.tvOccassionFor)
        val tvOccassionDate = itemView.findViewById<TextView>(R.id.tvOccassionDate)
        val tvOccassionName = itemView.findViewById<TextView>(R.id.tvOccassionName)
        val tvNoOccassionFound = itemView.findViewById<TextView>(R.id.tvNoOccassionFound)
        val linearLayoutTitleContainer = itemView.findViewById<LinearLayout>(R.id.linear_title_container)
        val imgBirthdayIcon = itemView.findViewById<ImageView>(R.id.imgBirthdayIcon)
        if (mOccassionsDataList.size == 1) {
            if (mOccassionsDataList[0].occassionFor == "No Occasion Found" && mOccassionsDataList[0].occassionDate == "" && mOccassionsDataList[0].occassionName == "") {
                linearLayoutTitleContainer.visibility = View.GONE
                imgOccProfile.visibility = View.GONE
                imgBirthdayIcon.visibility = View.GONE
                tvNoOccassionFound.visibility = View.VISIBLE
            } else if (mOccassionsDataList.size > 0) {
                objOccassionsData = mOccassionsDataList[position]
                tvOccassionFor.text = objOccassionsData!!.occassionFor
                tvOccassionName.text = objOccassionsData!!.occassionName
                tvOccassionDate.text = objOccassionsData!!.occassionDate
                linearLayoutTitleContainer.visibility = View.VISIBLE
                imgOccProfile.visibility = View.VISIBLE
                imgBirthdayIcon.visibility = View.VISIBLE
                tvNoOccassionFound.visibility = View.GONE
            }
        } else {
            objOccassionsData = mOccassionsDataList[position]
            tvOccassionFor.text = objOccassionsData!!.occassionFor
            tvOccassionName.text = objOccassionsData!!.occassionName
            tvOccassionDate.text = objOccassionsData!!.occassionDate
            linearLayoutTitleContainer.visibility = View.VISIBLE
            imgOccProfile.visibility = View.VISIBLE
            imgBirthdayIcon.visibility = View.VISIBLE
            tvNoOccassionFound.visibility = View.GONE
        }
        //imgOccProfile.setImageResource(objOccassionsData.getOccasionImageUrl()); // Set with glide
//imgOccProfile.setImageResource(mResources[position]);
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

    init {
        mOccassionsDataList = occassionsData
        mLayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}