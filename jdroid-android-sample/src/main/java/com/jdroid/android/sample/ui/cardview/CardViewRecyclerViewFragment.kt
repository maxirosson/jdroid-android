package com.jdroid.android.sample.ui.cardview

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jdroid.android.recycler.AbstractRecyclerFragment
import com.jdroid.android.recycler.RecyclerViewAdapter
import com.jdroid.android.recycler.RecyclerViewContainer
import com.jdroid.android.recycler.RecyclerViewType
import com.jdroid.android.sample.R

class CardViewRecyclerViewFragment : AbstractRecyclerFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewTypes = listOf<RecyclerViewType<*, *>>(CardViewRecyclerViewType(), CardViewClickableRecyclerViewType())
        setAdapter(RecyclerViewAdapter(recyclerViewTypes, listOf("1", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)))
    }

    inner class CardViewRecyclerViewType : RecyclerViewType<Int, SampleViewHolder>() {

        override fun getLayoutResourceId(): Int {
            return R.layout.simple_cardview
        }

        override fun getItemClass(): Class<Int> {
            return Int::class.java
        }

        override fun createViewHolderFromView(view: View): RecyclerView.ViewHolder {
            return SampleViewHolder(view)
        }

        override fun fillHolderFromItem(item: Int?, holder: SampleViewHolder) {}

        override fun getRecyclerViewContainer(): RecyclerViewContainer {
            return this@CardViewRecyclerViewFragment
        }

        override fun isClickable(): Boolean {
            return false
        }
    }

    inner class CardViewClickableRecyclerViewType : RecyclerViewType<String, SampleViewHolder>() {

        override fun getLayoutResourceId(): Int {
            return R.layout.simple_clickable_cardview
        }

        override fun getItemClass(): Class<String> {
            return String::class.java
        }

        override fun createViewHolderFromView(view: View): RecyclerView.ViewHolder {
            return SampleViewHolder(view)
        }

        override fun fillHolderFromItem(item: String, holder: SampleViewHolder) {}

        override fun getRecyclerViewContainer(): RecyclerViewContainer {
            return this@CardViewRecyclerViewFragment
        }

        public override fun onItemSelected(item: String, view: View) {}
    }

    class SampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
