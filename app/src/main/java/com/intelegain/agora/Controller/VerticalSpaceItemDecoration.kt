package com.intelegain.agora.Controller

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * Created by Admin on 22/08/16.
 */
class VerticalSpaceItemDecoration(private val mVerticalSpaceHeight: Int) : ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        outRect.bottom = mVerticalSpaceHeight
    }

}