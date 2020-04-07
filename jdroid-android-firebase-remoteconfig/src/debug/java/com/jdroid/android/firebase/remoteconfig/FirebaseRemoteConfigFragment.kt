package com.jdroid.android.firebase.remoteconfig

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.recycler.AbstractRecyclerFragment
import com.jdroid.android.recycler.RecyclerViewAdapter
import com.jdroid.android.recycler.RecyclerViewContainer
import com.jdroid.android.recycler.RecyclerViewType
import com.jdroid.java.date.DateUtils
import com.jdroid.java.remoteconfig.RemoteConfigParameter
import com.jdroid.java.utils.StringUtils
import java.util.Date

class FirebaseRemoteConfigFragment : AbstractRecyclerFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = mutableListOf<Any>()
        items.add("")
        for (appModule in AbstractApplication.get().appModules) {
            items.addAll(appModule.getRemoteConfigParameters())
        }
        items.addAll(AbstractApplication.get().debugContext.getRemoteConfigParameters())
        val recyclerViewTypes = listOf(HeaderRecyclerViewType(), RemoteConfigParameterRecyclerViewType())
        setAdapter(RecyclerViewAdapter(recyclerViewTypes, items))
    }

    inner class HeaderRecyclerViewType : RecyclerViewType<String, HeaderViewHolder>() {

        override fun getLayoutResourceId(): Int {
            return R.layout.jdroid_firebase_remote_config_header
        }

        override fun getItemClass(): Class<String> {
            return String::class.java
        }

        override fun createViewHolderFromView(view: View): RecyclerView.ViewHolder {
            val viewHolder = HeaderViewHolder(view)
            viewHolder.fetchTimeMillis = findView(view, R.id.fetchTimeMillis)
            viewHolder.lastFetchStatus = findView(view, R.id.lastFetchStatus)
            viewHolder.fetch = findView(view, R.id.fetch)
            viewHolder.mocksEnabled = findView(view, R.id.mocksEnabled)
            return viewHolder
        }

        override fun fillHolderFromItem(item: String, holder: HeaderViewHolder) {
            holder.fetch.isEnabled = !MockRemoteConfigLoader.isMocksEnabled()
            holder.fetch.setOnClickListener {
                FirebaseRemoteConfigLoader.get()
                    .fetch(false) { executeOnUIThread(Runnable { getRecyclerViewAdapter().notifyDataSetChanged() }) }
            }

            holder.mocksEnabled.setOnCheckedChangeListener(null)
            holder.mocksEnabled.isChecked = MockRemoteConfigLoader.isMocksEnabled()
            holder.mocksEnabled.setOnCheckedChangeListener { _, isChecked ->
                MockRemoteConfigLoader.setMocksEnabled(isChecked)
                getRecyclerViewAdapter().notifyDataSetChanged()
            }

            if (!MockRemoteConfigLoader.isMocksEnabled()) {
                val firebaseRemoteConfig = FirebaseRemoteConfigLoader.get().firebaseRemoteConfig
                if (firebaseRemoteConfig != null) {
                    val fetchDate = DateUtils.formatDateTime(Date(firebaseRemoteConfig.info.fetchTimeMillis))
                    holder.fetchTimeMillis.text = getString(R.string.jdroid_firebaseRemoteConfigFetchDate, fetchDate)
                    val status = firebaseRemoteConfig.info.lastFetchStatus
                    var statusValue = "Unknown"
                    if (status == -1) {
                        statusValue = "Success"
                    } else if (status == 0) {
                        statusValue = "No fetch yet"
                    } else if (status == 1) {
                        statusValue = "Failure"
                    } else if (status == 2) {
                        statusValue = "Throttled"
                    }
                    holder.lastFetchStatus.text = getString(R.string.jdroid_firebaseRemoteConfigLastFetchStatus, statusValue)
                }
            }
        }

        override fun getRecyclerViewContainer(): RecyclerViewContainer {
            return this@FirebaseRemoteConfigFragment
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var fetchTimeMillis: TextView
        lateinit var lastFetchStatus: TextView
        lateinit var fetch: Button
        lateinit var mocksEnabled: CheckBox
    }

    inner class RemoteConfigParameterRecyclerViewType :
        RecyclerViewType<RemoteConfigParameter, RemoteConfigParameterItemHolder>() {

        override fun getItemClass(): Class<RemoteConfigParameter> {
            return RemoteConfigParameter::class.java
        }

        override fun getLayoutResourceId(): Int {
            return R.layout.jdroid_firebase_remote_config_param
        }

        override fun createViewHolderFromView(view: View): RecyclerView.ViewHolder {
            val holder = RemoteConfigParameterItemHolder(view)
            holder.key = findView(view, R.id.key)
            holder.specs = findView(view, R.id.specs)
            holder.source = findView(view, R.id.source)
            holder.value = findView(view, R.id.value)
            holder.save = findView(view, R.id.save)
            return holder
        }

        override fun fillHolderFromItem(item: RemoteConfigParameter, holder: RemoteConfigParameterItemHolder) {
            holder.key.text = item.getKey()
            holder.specs.text = getString(R.string.jdroid_firebaseRemoteConfigSpec, item.getDefaultValue())

            if (MockRemoteConfigLoader.isMocksEnabled()) {
                holder.source.visibility = View.GONE
            } else {
                holder.source.text = getString(R.string.jdroid_firebaseRemoteConfigSource, FirebaseRemoteConfigLoader.get().getSourceName(item))
                holder.source.visibility = View.VISIBLE
            }

            holder.value.setText(AbstractApplication.get().remoteConfigLoader!!.getString(item))
            holder.value.isEnabled = MockRemoteConfigLoader.isMocksEnabled()

            holder.save.visibility = if (MockRemoteConfigLoader.isMocksEnabled()) View.VISIBLE else View.GONE
            holder.save.setOnClickListener {
                MockRemoteConfigLoader.get().saveRemoteConfigParameter(item, StringUtils.getNotEmptyString(holder.value.text.toString()))
            }
        }

        override fun matchViewType(item: Any): Boolean? {
            return item is RemoteConfigParameter
        }

        override fun getRecyclerViewContainer(): AbstractRecyclerFragment {
            return this@FirebaseRemoteConfigFragment
        }
    }

    class RemoteConfigParameterItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var key: TextView
        lateinit var specs: TextView
        lateinit var source: TextView
        lateinit var value: EditText
        lateinit var save: Button
    }
}
