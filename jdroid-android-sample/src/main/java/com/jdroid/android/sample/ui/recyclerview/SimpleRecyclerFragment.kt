package com.jdroid.android.sample.ui.recyclerview

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
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
import com.jdroid.java.collections.Lists
import com.jdroid.java.utils.IdGenerator

open class SimpleRecyclerFragment : AbstractRecyclerFragment() {

    private lateinit var sampleListViewModel: SampleListViewModel
    private lateinit var observer: Observer<Resource<List<String>>>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observer = object : ResourceObserver<List<String>>() {

            override fun onDataChanged(data: List<String>) {
                setAdapter(RecyclerViewAdapter(StringRecyclerViewType(), data))
            }

            override fun onStarting() {
                showLoading()
            }

            override fun onStartLoading(data: List<String>?) {
                showLoading()
            }

            override fun getFragment(): AbstractFragment {
                return this@SimpleRecyclerFragment
            }
        }
        sampleListViewModel = ViewModelProviders.of(this).get(SampleListViewModel::class.java)
        sampleListViewModel.loadStrings().observe(this, observer)
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
                getRecyclerViewAdapter().addItems(Lists.newArrayList(IdGenerator.getIntId().toString(), IdGenerator.getIntId().toString(), IdGenerator.getIntId().toString()))
                return true
            }
            R.id.replaceWithSameItems -> {
                val items = Lists.newArrayList(getRecyclerViewAdapter().items)
                getRecyclerViewAdapter().replaceItems(items, object : DiffUtil.Callback() {
                    override fun getOldListSize(): Int {
                        return items.size
                    }

                    override fun getNewListSize(): Int {
                        return items.size
                    }

                    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        return items[oldItemPosition].toString() == items[newItemPosition].toString()
                    }

                    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        return items[oldItemPosition] == items[newItemPosition]
                    }
                })
                return true
            }
            R.id.replaceWithDifferentItems -> {
                val oldItems = Lists.newArrayList(getRecyclerViewAdapter().items)
                val newItems = Lists.newArrayList<Any>(IdGenerator.getIntId().toString(), IdGenerator.getIntId().toString(), IdGenerator.getIntId().toString())
                getRecyclerViewAdapter().replaceItems(newItems, object : DiffUtil.Callback() {
                    override fun getOldListSize(): Int {
                        return oldItems.size
                    }

                    override fun getNewListSize(): Int {
                        return newItems.size
                    }

                    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        return oldItems[oldItemPosition].toString() == newItems[newItemPosition].toString()
                    }

                    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        return oldItems[oldItemPosition] == newItems[newItemPosition]
                    }
                })
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
            return this@SimpleRecyclerFragment
        }

        override fun onItemSelected(item: String, view: View) {
            getRecyclerViewAdapter().removeItem(item)
        }
    }

    class StringViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var textView: TextView
    }

    inner class SampleHeaderRecyclerViewType : HeaderRecyclerViewType() {

        override fun getLayoutResourceId(): Int {
            return R.layout.clickable_header_item
        }

        override fun isClickable(): Boolean {
            return true
        }

        override fun onItemSelected(headerItem: HeaderRecyclerViewType.HeaderItem, view: View) {
            getRecyclerViewAdapter().removeItem(headerItem)
        }

        override fun getRecyclerViewContainer(): RecyclerViewContainer {
            return this@SimpleRecyclerFragment
        }
    }

    inner class SampleFooterRecyclerViewType : FooterRecyclerViewType() {

        override fun getLayoutResourceId(): Int {
            return R.layout.clickable_footer_item
        }

        override fun isClickable(): Boolean {
            return true
        }

        override fun onItemSelected(footerItem: FooterRecyclerViewType.FooterItem, view: View) {
            getRecyclerViewAdapter().removeItem(footerItem)
        }

        override fun getRecyclerViewContainer(): RecyclerViewContainer {
            return this@SimpleRecyclerFragment
        }
    }
}
