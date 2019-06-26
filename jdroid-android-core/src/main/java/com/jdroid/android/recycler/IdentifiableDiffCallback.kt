package com.jdroid.android.recycler

import androidx.recyclerview.widget.DiffUtil
import com.jdroid.java.collections.Lists

abstract class IdentifiableDiffCallback<T>(recyclerViewAdapter: RecyclerViewAdapter, newItems: List<T>) : DiffUtil.Callback() {

    private val oldItems: List<T>
    private val newItems: List<T>

    init {
        this.oldItems = Lists.newArrayList(recyclerViewAdapter.items as List<T>)
        this.newItems = Lists.newArrayList(newItems)
    }

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return getId(oldItems[oldItemPosition]) == getId(newItems[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    protected abstract fun getId(item: T): String
}
