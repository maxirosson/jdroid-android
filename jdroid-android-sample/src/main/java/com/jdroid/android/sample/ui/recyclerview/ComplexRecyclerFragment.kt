package com.jdroid.android.sample.ui.recyclerview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.jdroid.android.androidx.lifecycle.Resource
import com.jdroid.android.androidx.lifecycle.ResourceObserver
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.recycler.AbstractRecyclerFragment
import com.jdroid.android.recycler.FooterRecyclerViewType
import com.jdroid.android.recycler.HeaderRecyclerViewType
import com.jdroid.android.recycler.RecyclerViewAdapter
import com.jdroid.android.recycler.RecyclerViewContainer
import com.jdroid.android.recycler.RecyclerViewType
import com.jdroid.android.sample.R
import com.jdroid.android.sample.androidx.SampleListViewModel
import com.jdroid.java.utils.IdGenerator

class ComplexRecyclerFragment : AbstractRecyclerFragment() {

    private lateinit var sampleListViewModel: SampleListViewModel
    private lateinit var observer: Observer<Resource<List<Any>>>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observer = object : ResourceObserver<List<Any>>() {

            override fun onDataChanged(data: List<Any>) {
                val recyclerViewTypes = listOf<RecyclerViewType<*, *>>(
                    StringRecyclerViewType(),
                    IntegerRecyclerViewType(),
                    BooleanRecyclerViewType()
                )
                setAdapter(RecyclerViewAdapter(recyclerViewTypes, data))
            }

            override fun onStarting() {
                showLoading()
            }

            override fun onStartLoading(data: List<Any>?) {
                showLoading()
            }

            override fun getFragment(): AbstractFragment {
                return this@ComplexRecyclerFragment
            }
        }
        sampleListViewModel = ViewModelProviders.of(this).get(SampleListViewModel::class.java)
        sampleListViewModel.loadMixedTypes().observe(this, observer)
    }

    override fun getMenuResourceId(): Int? {
        return R.menu.recycler_menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addItem -> {
                getRecyclerViewAdapter().addItem(IdGenerator.getIntId().toString())
                return true
            }
            R.id.addItems -> {
                getRecyclerViewAdapter().addItems(listOf(IdGenerator.getIntId().toString(), IdGenerator.getIntId().toString(), IdGenerator.getIntId().toString()))
                return true
            }
            R.id.clearItems -> {
                getRecyclerViewAdapter().clear()
                return true
            }
            R.id.removeFirstItem -> {
                getRecyclerViewAdapter().removeItemByPosition(0)
                return true
            }
            R.id.removeSecondItem -> {
                getRecyclerViewAdapter().removeItemByPosition(1)
                return true
            }
            R.id.addHeader -> {
                getRecyclerViewAdapter().setHeader(R.layout.header_item)
                return true
            }
            R.id.addClickableHeader -> {
                getRecyclerViewAdapter().setHeader(SampleHeaderRecyclerViewType())
                return true
            }
            R.id.removeHeader -> {
                getRecyclerViewAdapter().removeHeader()
                return true
            }
            R.id.addFooter -> {
                getRecyclerViewAdapter().setFooter(R.layout.footer_item)
                return true
            }
            R.id.addClickableFooter -> {
                getRecyclerViewAdapter().setFooter(SampleFooterRecyclerViewType())
                return true
            }
            R.id.removeFooter -> {
                getRecyclerViewAdapter().removeFooter()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun isDividerItemDecorationEnabled(): Boolean {
        return true
    }

    inner class StringRecyclerViewType : RecyclerViewType<String, StringViewHolder>() {

        override fun getLayoutResourceId(): Int {
            return R.layout.item
        }

        override fun getItemClass(): Class<String> {
            return String::class.java
        }

        override fun createViewHolderFromView(view: View): RecyclerView.ViewHolder {
            val viewHolder = StringViewHolder(view)
            viewHolder.textView = findView(view, R.id.name)
            return viewHolder
        }

        override fun fillHolderFromItem(item: String, holder: StringViewHolder) {
            holder.textView.text = item
        }

        override fun getRecyclerViewContainer(): RecyclerViewContainer {
            return this@ComplexRecyclerFragment
        }

        override fun onItemSelected(item: String, view: View) {
            getRecyclerViewAdapter().removeItem(item)
        }
    }

    class StringViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var textView: TextView
    }

    inner class IntegerRecyclerViewType : RecyclerViewType<Int, IntegerViewHolder>() {

        override fun getLayoutResourceId(): Int {
            return R.layout.item
        }

        override fun getItemClass(): Class<Int> {
            return Int::class.java
        }

        override fun createViewHolderFromView(view: View): RecyclerView.ViewHolder {
            val viewHolder = IntegerViewHolder(view)
            viewHolder.textView = findView(view, R.id.name)
            return viewHolder
        }

        @SuppressLint("SetTextI18n")
        override fun fillHolderFromItem(item: Int, holder: IntegerViewHolder) {
            holder.textView.text = item.toString()
        }

        override fun isSelectable(): Boolean {
            return true
        }

        override fun getRecyclerViewContainer(): RecyclerViewContainer {
            return this@ComplexRecyclerFragment
        }
    }

    class IntegerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var textView: TextView
    }

    inner class BooleanRecyclerViewType : RecyclerViewType<Boolean, BooleanViewHolder>() {

        override fun getLayoutResourceId(): Int {
            return R.layout.item
        }

        override fun getItemClass(): Class<Boolean> {
            return Boolean::class.java
        }

        override fun createViewHolderFromView(view: View): RecyclerView.ViewHolder {
            val viewHolder = BooleanViewHolder(view)
            viewHolder.textView = findView(view, R.id.name)
            return viewHolder
        }

        override fun fillHolderFromItem(item: Boolean, holder: BooleanViewHolder) {
            holder.textView.text = item.toString()
        }

        override fun isClickable(): Boolean {
            return false
        }

        override fun getRecyclerViewContainer(): RecyclerViewContainer {
            return this@ComplexRecyclerFragment
        }
    }

    class BooleanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var textView: TextView
    }

    inner class SampleHeaderRecyclerViewType : HeaderRecyclerViewType() {

        override fun getLayoutResourceId(): Int {
            return R.layout.clickable_header_item
        }

        override fun isClickable(): Boolean {
            return true
        }

        override fun onItemSelected(headerItem: HeaderItem, view: View) {
            getRecyclerViewAdapter().removeItem(headerItem)
        }

        override fun getRecyclerViewContainer(): RecyclerViewContainer {
            return this@ComplexRecyclerFragment
        }
    }

    inner class SampleFooterRecyclerViewType : FooterRecyclerViewType() {

        override fun getLayoutResourceId(): Int {
            return R.layout.clickable_footer_item
        }

        override fun isClickable(): Boolean {
            return true
        }

        override fun onItemSelected(footerItem: FooterItem, view: View) {
            getRecyclerViewAdapter().removeItem(footerItem)
        }

        override fun getRecyclerViewContainer(): RecyclerViewContainer {
            return this@ComplexRecyclerFragment
        }
    }
}
