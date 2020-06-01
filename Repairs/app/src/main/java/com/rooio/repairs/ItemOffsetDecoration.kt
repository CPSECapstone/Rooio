package com.rooio.repairs

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

// used to add padding to notable job widgets
class ItemOffsetDecoration private constructor(private val mItemOffset: Int) : ItemDecoration() {

    constructor(context: Context, @DimenRes itemOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId)) {}

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect[mItemOffset, mItemOffset, mItemOffset] = mItemOffset
    }

}