package com.jdroid.android.sample.ui.loading

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jdroid.android.androidx.lifecycle.Resource
import com.jdroid.android.androidx.lifecycle.ResourceObserver
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.recycler.RecyclerViewAdapter
import com.jdroid.android.recycler.RecyclerViewContainer
import com.jdroid.android.recycler.SwipeRecyclerFragment
import com.jdroid.android.sample.R
import com.jdroid.android.sample.androidx.SampleListViewModel
import com.jdroid.android.sample.database.room.SampleEntity
import com.jdroid.android.sample.ui.adapter.SampleRecyclerViewType

class SwipeRefreshLoadingFragment : SwipeRecyclerFragment() {

    private lateinit var sampleListViewModel: SampleListViewModel
    private lateinit var observer: Observer<Resource<List<SampleEntity>>>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observer = object : ResourceObserver<List<SampleEntity>>() {

            override fun onDataChanged(data: List<SampleEntity>) {
                setAdapter(RecyclerViewAdapter(object : SampleRecyclerViewType() {

                    public override fun onItemSelected(item: SampleEntity, view: View) {
                        sampleListViewModel.removeItem(item.id)
                    }

                    override fun getRecyclerViewContainer(): RecyclerViewContainer {
                        return this@SwipeRefreshLoadingFragment
                    }
                }, data))
            }

            override fun onStarting() {
                showLoading()
            }

            override fun onStartLoading(data: List<SampleEntity>?) {
                showLoading()
            }

            override fun getFragment(): AbstractFragment {
                return this@SwipeRefreshLoadingFragment
            }
        }
        sampleListViewModel = ViewModelProviders.of(this).get(SampleListViewModel::class.java)
        execute(false)
    }

    override fun onRefresh() {
        execute(true)
    }

    private fun execute(forceRefresh: Boolean) {
        if (sampleListViewModel.sampleEntities != null && forceRefresh) {
            sampleListViewModel.sampleEntities!!.removeObserver(observer)
        }
        sampleListViewModel.load(forceRefresh, false).observe(this, observer)
    }

    override fun getMenuResourceId(): Int? {
        return R.menu.recycler_menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addItem -> {
                sampleListViewModel.addItem()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
