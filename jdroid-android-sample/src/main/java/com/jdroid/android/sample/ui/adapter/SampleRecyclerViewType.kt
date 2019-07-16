package com.jdroid.android.sample.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jdroid.android.recycler.RecyclerViewType
import com.jdroid.android.sample.R
import com.jdroid.android.sample.database.room.SampleEntity

abstract class SampleRecyclerViewType : RecyclerViewType<SampleEntity, SampleRecyclerViewType.SampleHolder>() {

    override fun getItemClass(): Class<SampleEntity> {
        return SampleEntity::class.java
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.jdroid_default_item
    }

    override fun createViewHolderFromView(view: View): RecyclerView.ViewHolder {
        val holder = SampleHolder(view)
        holder.name = findView(view, com.jdroid.android.R.id.name)
        return holder
    }

    override fun fillHolderFromItem(item: SampleEntity, holder: SampleHolder) {
        holder.name.text = item.field
    }

    class SampleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var name: TextView
    }
}