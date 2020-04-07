package com.jdroid.android.sample.ui.google.admob

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdSize
import com.jdroid.android.androidx.lifecycle.Resource
import com.jdroid.android.androidx.lifecycle.ResourceObserver
import com.jdroid.android.firebase.admob.AdViewType
import com.jdroid.android.firebase.admob.helpers.AdViewHelper
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.recycler.AbstractRecyclerFragment
import com.jdroid.android.recycler.RecyclerViewAdapter
import com.jdroid.android.recycler.RecyclerViewContainer
import com.jdroid.android.recycler.RecyclerViewType
import com.jdroid.android.sample.R
import com.jdroid.android.sample.androidx.SampleListViewModel
import com.jdroid.android.sample.application.AndroidAppContext
import com.jdroid.android.sample.ui.recyclerview.SimpleRecyclerFragment

class AdRecyclerFragment : AbstractRecyclerFragment() {

    private lateinit var sampleListViewModel: SampleListViewModel
    private lateinit var observer: Observer<Resource<List<String>>>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observer = object : ResourceObserver<List<String>>() {

            override fun onDataChanged(data: List<String>) {
                val recyclerViewTypes = listOf(StringRecyclerViewType(), MyAdViewType())

                val items = mutableListOf<Any>()
                for (each in data) {
                    items.add(each)
                    if (each == "three") {
                        val baseAdViewHelper = AdViewHelper()
                        baseAdViewHelper.adSize = AdSize(AdSize.FULL_WIDTH, 80)
                        baseAdViewHelper.setAdUnitId(AndroidAppContext.SAMPLE_BANNER_AD_UNIT_ID)
                        items.add(baseAdViewHelper)
                    }
                }
                setAdapter(RecyclerViewAdapter(recyclerViewTypes, items))
            }

            override fun onStarting() {
                showLoading()
            }

            override fun onStartLoading(data: List<String>?) {
                showLoading()
            }

            override fun getFragment(): AbstractFragment {
                return this@AdRecyclerFragment
            }
        }
        sampleListViewModel = ViewModelProviders.of(this).get(SampleListViewModel::class.java)
        sampleListViewModel.loadStrings().observe(this, observer)
    }

    inner class StringRecyclerViewType : RecyclerViewType<String, SimpleRecyclerFragment.StringViewHolder>() {

        override fun getLayoutResourceId(): Int {
            return R.layout.item
        }

        override fun getItemClass(): Class<String> {
            return String::class.java
        }

        override fun createViewHolderFromView(view: View): RecyclerView.ViewHolder {
            val viewHolder = SimpleRecyclerFragment.StringViewHolder(view)
            viewHolder.textView = findView(view, R.id.name)
            return viewHolder
        }

        override fun fillHolderFromItem(item: String, holder: SimpleRecyclerFragment.StringViewHolder) {
            holder.textView.text = item
        }

        override fun getRecyclerViewContainer(): RecyclerViewContainer {
            return this@AdRecyclerFragment
        }

        public override fun onItemSelected(item: String, view: View) {
            getRecyclerViewAdapter().removeItem(item)
        }
    }

    inner class MyAdViewType : AdViewType() {

        override fun getLayoutResourceId(): Int {
            return R.layout.ad
        }

        override fun getRecyclerViewContainer(): RecyclerViewContainer {
            return this@AdRecyclerFragment
        }
    }
}
