package com.intelegain.agora.interfeces

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by pranay.s on 3/8/17.
 */
class OnLoadMoreScrollListener(private val recyclerView: RecyclerView) {
    private val visibleThreshold = 2
    private var lastVisibleItem = 0
    private var totalItemCount = 0
    private var isLoading = false
    private var onLoadMoreListener: OnLoadMoreListener? = null
    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener?) {
        onLoadMoreListener = mOnLoadMoreListener
    }

    fun setLoaded() {
        isLoading = false
    }

    // Defines the process for actually loading more data based on page
    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    init {
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayoutManager!!.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener!!.onLoadMore()
                    }
                    isLoading = true
                }
            }
        })
    }
}