package com.intelegain.agora.api.urls

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.intelegain.agora.R
import com.intelegain.agora.api.urls.CommonRecyclerAdapter.CommonRecyclerViewHolder
import com.intelegain.agora.interfeces.RecyclerItemClickListener
import java.util.*

/**
 * Created by pranay.s on 14/7/17.
 */
class CommonRecyclerAdapter(private val mContext: Context, private var dataList: List<String>, itemClickListener: RecyclerItemClickListener) : RecyclerView.Adapter<CommonRecyclerViewHolder>(), Filterable {
    private val dataListResult: List<String>
    private val itemClickListener: RecyclerItemClickListener
    private var mFilter = ItemFilter()
    private val TAG = "CommonRecyclerAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonRecyclerViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.common_recyclerview_row, null)
        return CommonRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommonRecyclerViewHolder, position: Int) {
        holder.itemName.text = dataList[position]
        holder.itemName.setOnClickListener { itemClickListener.recyclerViewListClicked(position, dataList[position]) }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getFilter(): Filter {
        if (mFilter == null) {
            mFilter = ItemFilter()
        }
        return mFilter
    }

    //******************* View Holder ***************************************
    inner class CommonRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName: TextView

        init {
            itemName = itemView.findViewById<View>(R.id.txt_recycler_item) as TextView
        }
    }

    //******************* Filterable ***************************************
    inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val results = FilterResults()
            Log.e(TAG, "constraint:  $constraint")
            if (constraint == null || constraint.length == 0 || constraint.length < 2) {
                results.values = dataListResult
                results.count = dataListResult.size
            } else {
                val filteredTask: MutableList<String?> = ArrayList<String?>()
                for (c in dataList) {
                    if (c.toUpperCase().contains(constraint.toString().toUpperCase())) { // if `contains` == true then add it
// to our filtered list
                        filteredTask.add(c)
                    }
                }
                results.values = filteredTask
                results.count = filteredTask.size
            }
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            dataList = results.values as List<String>
            notifyDataSetChanged()
        }
    }

    init {
        dataListResult = dataList
        this.itemClickListener = itemClickListener
    }
}