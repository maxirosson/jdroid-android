package com.jdroid.android.debug.info

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.jdroid.android.R
import com.jdroid.android.recycler.RecyclerViewType

abstract class PairItemRecyclerViewType : RecyclerViewType<Pair<*, *>, PairItemRecyclerViewType.PairItemHolder>() {

    override fun getItemClass(): Class<Pair<*, *>> {
        return Pair::class.java
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.jdroid_pair_item
    }

    override fun createViewHolderFromView(view: View): RecyclerView.ViewHolder {
        val holder = PairItemHolder(view)
        holder.name = findView(view, R.id.name)
        return holder
    }

    @SuppressLint("SetTextI18n")
    override fun fillHolderFromItem(item: Pair<*, *>, holder: PairItemHolder) {
        holder.name.text = item.first.toString() + ": " + item.second
        holder.name.setTextIsSelectable(isTextSelectable())
    }

    protected open fun isTextSelectable(): Boolean {
        return false
    }

    class PairItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var name: TextView
    }
}
