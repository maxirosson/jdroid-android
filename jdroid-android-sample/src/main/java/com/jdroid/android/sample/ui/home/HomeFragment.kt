package com.jdroid.android.sample.ui.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jdroid.android.recycler.AbstractRecyclerFragment
import com.jdroid.android.recycler.RecyclerViewAdapter
import com.jdroid.android.recycler.RecyclerViewContainer
import com.jdroid.android.recycler.RecyclerViewType
import com.jdroid.android.sample.R

class HomeFragment : AbstractRecyclerFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter(RecyclerViewAdapter(HomeRecyclerViewType(), listOf(*HomeItem.values())))
    }

    override fun isCardViewDecorationEnabled(): Boolean? {
        return true
    }

    override fun isDividerItemDecorationEnabled(): Boolean {
        return true
    }

    inner class HomeRecyclerViewType : RecyclerViewType<HomeItem, HomeHolder>() {

        override fun getItemClass(): Class<HomeItem> {
            return HomeItem::class.java
        }

        override fun getLayoutResourceId(): Int {
            return R.layout.jdroid_default_item
        }

        override fun createViewHolderFromView(view: View): RecyclerView.ViewHolder {
            val holder = HomeHolder(view)
            holder.image = findView(view, com.jdroid.android.R.id.image)
            holder.name = findView(view, com.jdroid.android.R.id.name)
            return holder
        }

        override fun fillHolderFromItem(item: HomeItem, holder: HomeHolder) {
            holder.image.setImageResource(item.getIconResource())
            holder.name.setText(item.getNameResource())
        }

        public override fun onItemSelected(item: HomeItem, view: View) {
            item.startActivity(requireActivity())
        }

        override fun getRecyclerViewContainer(): RecyclerViewContainer {
            return this@HomeFragment
        }
    }

    class HomeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var image: ImageView
        lateinit var name: TextView
    }
}
