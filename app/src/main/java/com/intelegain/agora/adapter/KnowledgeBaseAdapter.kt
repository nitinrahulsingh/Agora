package com.intelegain.agora.adapter

import android.app.Activity
import android.graphics.Paint
import android.text.Html
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.intelegain.agora.R
import com.intelegain.agora.adapter.KnowledgeBaseAdapter.KnowledgeBaseViewHolder
import com.intelegain.agora.interfeces.IKnlodedgeBaseAttachment
import com.intelegain.agora.model.KnowledgeBaseData
import com.intelegain.agora.utils.Contants2
import java.util.*

class KnowledgeBaseAdapter(private val activity: Activity, lstKnowledgeBaseData: ArrayList<KnowledgeBaseData>?, token: String, empId: String, iKnlodedgeBaseAttachment: IKnlodedgeBaseAttachment) : RecyclerView.Adapter<KnowledgeBaseViewHolder>() {
    private val TAG = javaClass.simpleName
    private val mlstKnowledgeBaseData: ArrayList<KnowledgeBaseData>?
    private val filteredList: ArrayList<KnowledgeBaseData>?
    private var expandedPosition = -1
    private val strToken: String
    private val strEmpId: String
    private val iKnlodedgeBaseAttachment: IKnlodedgeBaseAttachment
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnowledgeBaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_knowledge_base, parent, false)
        return KnowledgeBaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: KnowledgeBaseViewHolder, position: Int) {
        val knowledgeBaseData = filteredList!![position]
        holder.knowledge_title.text = knowledgeBaseData.knowledgeTitle
        holder.knowledge_project_name.text = knowledgeBaseData.knowledgeProjectTitle
        holder.knowledge_technology.text = knowledgeBaseData.knowledgeTechnology
        holder.knowledge_posted_by.text = knowledgeBaseData.knowledgePostedBy
        holder.knowledge_posted_date.text = knowledgeBaseData.knowledgePostedDate
        if (knowledgeBaseData.knowledgeDescription != null && knowledgeBaseData.knowledgeDescription != ""
                && knowledgeBaseData.knowledgeDescription != "null") {
            holder.knowledge_description.visibility = View.VISIBLE
            //holder.knowledge_description.setText(knowledgeBaseData.getKnowledgeDescription());
            holder.knowledge_description.text = Html.fromHtml(knowledgeBaseData.knowledgeDescription)
        } else {
            holder.knowledge_description.visibility = View.GONE
        }
        if (knowledgeBaseData.knowledegeFileName != null && knowledgeBaseData.knowledegeFileName != "null"
                && knowledgeBaseData.knowledegeFileName != "") { // holder.knowledge_file_name.setVisibility(View.VISIBLE);
            val strFileName = knowledgeBaseData.knowledegeFileName.split(",").toTypedArray()
            val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            val ll_file_tag = LinearLayout(activity)
            ll_file_tag.layoutParams = params //Layout params for Button
            for (i in strFileName.indices) {
                val fileName = TextView(activity)
                val imageView = ImageView(activity)
                imageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_attachment))
                imageView.setPadding(0, 3, 0, 0)
                //fileName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_attachment,0,0,0);
//fileName.setCompoundDrawablePadding(1);
                fileName.id = i //Setting Id for using in future
                fileName.text = strFileName[i]
                fileName.setPadding(4, 0, 10, 0)
                fileName.paintFlags = fileName.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                ll_file_tag.orientation = LinearLayout.HORIZONTAL //Setting Layout orientation
                ll_file_tag.addView(imageView)
                ll_file_tag.addView(fileName) //Finally adding view
                fileName.setOnClickListener {
                    iKnlodedgeBaseAttachment.getAttachment(knowledgeBaseData.knowledgeId, fileName.text.toString())
                    /*
                                // create interface to do same as done and check file type
                                Toast.makeText(activity, "downloading file "+fileName.getText(), Toast.LENGTH_SHORT).show();
                                Intent startIntent = new Intent(activity,
                                        AttachmentDownloderService.class);
                                startIntent.putExtra(Contants2.KBID, knowledgeBaseData.getKnowledgeId());
                                startIntent.putExtra(Contants2.KB_FILE_NAME,fileName.getText());
                                startIntent.putExtra(Contants2.EMP_ID,strEmpId);
                                startIntent.putExtra(Contants2.TOKEN,strToken);
                                activity.startService(startIntent);*/
                }
            }
            holder.ll_knowledge_file.removeAllViews()
            holder.ll_knowledge_file.addView(ll_file_tag)
            //            holder.ll_knowledge_file.setVisibility(View.VISIBLE);
        } else {
            holder.ll_knowledge_file.visibility = View.GONE
        }
        if (knowledgeBaseData.knowledgeUrl != null && knowledgeBaseData.knowledgeUrl != "") {
            holder.knowledge_url.visibility = View.VISIBLE
            holder.knowledge_url.text = knowledgeBaseData.knowledgeUrl
        } else {
            holder.knowledge_url.visibility = View.GONE
        }
        val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val linearLayoutForTags = LinearLayout(activity)
        linearLayoutForTags.layoutParams = params
        val knowledgeTagList = knowledgeBaseData.knowledgeTagList
        if (knowledgeTagList != null) {
            val iTagListSize = knowledgeTagList.size
            for (iCnt in 0 until iTagListSize) {
                val paramForTextview = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                paramForTextview.setMargins(0, 10, 5, 10)
                val tvKnowledgeTags = TextView(activity)
                tvKnowledgeTags.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
                tvKnowledgeTags.setPadding(15, 5, 15, 5)
                tvKnowledgeTags.gravity = Gravity.CENTER
                tvKnowledgeTags.textSize = 12f
                tvKnowledgeTags.setTextColor(ContextCompat.getColor(activity, R.color.new_dark_grey))
                tvKnowledgeTags.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.rounded_button_tag))
                tvKnowledgeTags.layoutParams = paramForTextview
                tvKnowledgeTags.text = knowledgeTagList[iCnt].tagName
                linearLayoutForTags.addView(tvKnowledgeTags)
            }
            holder.linearLayoutForTags.removeAllViews()
            holder.linearLayoutForTags.addView(linearLayoutForTags)
        }
        /** expand collapse view */
        if (position == expandedPosition) {
            holder.ll_knowledge_detail.visibility = View.VISIBLE
            holder.img_expand_collapse.setImageResource(R.drawable.ic_arrow_up)
        } else {
            holder.ll_knowledge_detail.visibility = View.GONE
            holder.img_expand_collapse.setImageResource(R.drawable.ic_arrow_down)
        }
        holder.knowledge_row.setOnClickListener {
            // Check for an expanded view, collapse if you find one
            if (expandedPosition >= 0) {
                val prev = expandedPosition
                notifyItemChanged(prev)
            }
            if (holder.ll_knowledge_detail.visibility == View.VISIBLE) {
                expandedPosition = -1
            } else { // Set the current position to "expanded"
                expandedPosition = holder.position
                notifyItemChanged(expandedPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredList?.size ?: 0
        //return mlstKnowledgeBaseData.size();
    }

    // Do Search...
    fun filter(text: String, filterType: Int) { // Searching could be complex..so we will dispatch it to a different thread...
        Thread(Runnable {
            // Clear the filter list
            filteredList!!.clear()
            // If there is no search value, then add all original list items to filter list
            if (TextUtils.isEmpty(text)) {
                if (mlstKnowledgeBaseData != null) filteredList.addAll(mlstKnowledgeBaseData)
            } else {
                if (mlstKnowledgeBaseData != null) { // Iterate in the original List and add it to filter list...
                    for (item in mlstKnowledgeBaseData) {
                        if (filterType == FILTER_WITH_TAG_LIST) {
                            val KnowledgeTagList = item.knowledgeTagList
                            if (KnowledgeTagList != null) {
                                val iTagListLength = KnowledgeTagList.size
                                for (iCnt in 0 until iTagListLength) { // filtered on basis of  title, categories, tags & creator
                                    if (item.knowledgeTagList[iCnt].tagName!!.toLowerCase().contains(text.toLowerCase()) ||
                                            item.knowledgeTitle.toLowerCase().contains(text.toLowerCase()) ||
                                            item.knowledgePostedBy.toLowerCase().contains(text.toLowerCase()) ||
                                            item.knowledgeProjectTitle.toLowerCase().contains(text.toLowerCase())) { // Adding Matched items
                                        filteredList.add(item)
                                    }
                                }
                            }
                        } else if (filterType == FILTER_WITH_TECHNOLOGY) {
                            val strTechnology = item.knowledgeTechnology
                            if (!strTechnology.isEmpty()) {
                                if (strTechnology.toLowerCase().contains(text.toLowerCase()) ||
                                        strTechnology.toLowerCase().contains(text.toLowerCase())) { // Adding Matched items
                                    filteredList.add(item)
                                }
                            }
                        }
                    }
                }
            }
            // Set on UI Thread
            activity.runOnUiThread {
                // Notify the List that the DataSet has changed...
                if (filteredList.size == 0) Contants2.showToastMessageAtCenter(activity, activity.getString(R.string.no_data_found), false)
                notifyDataSetChanged()
            }
        }).start()
    }

    inner class KnowledgeBaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val knowledge_title: TextView
        val knowledge_project_name: TextView
        val knowledge_technology: TextView
        val knowledge_posted_by: TextView
        val knowledge_posted_date: TextView
        val knowledge_description: TextView
        val knowledge_url: TextView
        val linearLayoutForTags: LinearLayout
        val ll_knowledge_detail: LinearLayout
        val knowledge_row: LinearLayout
        val ll_knowledge_file: LinearLayout
        val img_expand_collapse: ImageView

        init {
            //horizontalScrollViewForTags = (HorizontalScrollView) itemView.findViewById(R.id.horizontal_scrollview_for_tags);
            knowledge_title = itemView.findViewById<View>(R.id.knowledge_title) as TextView
            knowledge_project_name = itemView.findViewById<View>(R.id.knowledge_project_name) as TextView
            knowledge_technology = itemView.findViewById<View>(R.id.knowledge_technology) as TextView
            knowledge_posted_by = itemView.findViewById<View>(R.id.knowledge_posted_by) as TextView
            knowledge_posted_date = itemView.findViewById<View>(R.id.knowledge_posted_date) as TextView
            linearLayoutForTags = itemView.findViewById<View>(R.id.linearLayoutForTags) as LinearLayout
            ll_knowledge_detail = itemView.findViewById<View>(R.id.ll_knowledge_detail) as LinearLayout
            img_expand_collapse = itemView.findViewById<View>(R.id.img_expand_collapse) as ImageView
            knowledge_row = itemView.findViewById<View>(R.id.knowledge_row) as LinearLayout
            knowledge_description = itemView.findViewById<View>(R.id.knowledge_description) as TextView
            //knowledge_file_name = (TextView) itemView.findViewById(R.id.knowledge_file_name);
            knowledge_url = itemView.findViewById<View>(R.id.knowledge_url) as TextView
            ll_knowledge_file = itemView.findViewById<View>(R.id.ll_knowledge_file) as LinearLayout
        }
    }

    companion object {
        var FILTER_WITH_TAG_LIST = 0
        var FILTER_WITH_TECHNOLOGY = 1
    }

    init {
        filteredList = ArrayList()
        mlstKnowledgeBaseData = lstKnowledgeBaseData
        // we copy the original list to the filter list and use it for setting row values
        filteredList.addAll(mlstKnowledgeBaseData!!)
        strToken = token
        strEmpId = empId
        this.iKnlodedgeBaseAttachment = iKnlodedgeBaseAttachment
    }
}