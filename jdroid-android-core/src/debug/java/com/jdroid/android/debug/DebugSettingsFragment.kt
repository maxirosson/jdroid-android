package com.jdroid.android.debug

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jdroid.android.R
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.recycler.AbstractRecyclerFragment
import com.jdroid.android.recycler.RecyclerViewAdapter
import com.jdroid.android.recycler.RecyclerViewContainer
import com.jdroid.android.recycler.RecyclerViewType
import com.jdroid.java.collections.Lists

class DebugSettingsFragment : AbstractRecyclerFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val debugContext = AbstractApplication.get().debugContext

        val appenders = mutableListOf<PreferencesAppender>()
        addAppender(appenders, debugContext.createServersDebugPrefsAppender())
        addAppender(appenders, debugContext.createHttpMocksDebugPrefsAppender())
        addAppender(appenders, debugContext.createNavDrawerDebugPrefsAppender())
        addAppender(appenders, debugContext.createHttpCacheDebugPrefsAppender())
        addAppender(appenders, debugContext.createExceptionHandlingDebugPrefsAppender())
        addAppender(appenders, debugContext.createInfoDebugPrefsAppender())
        addAppender(appenders, debugContext.createUsageStatsDebugPrefsAppender())
        addAppender(appenders, debugContext.createUriMapperPrefsAppender())
        addAppender(appenders, debugContext.createNotificationsDebugPrefsAppender())

        for (preferencesAppender in DebugSettingsHelper.getPreferencesAppenders()) {
            addAppender(appenders, preferencesAppender)
        }

        appenders.addAll(debugContext.getCustomPreferencesAppenders())

        setAdapter(RecyclerViewAdapter(PreferencesAppenderRecyclerViewType(), Lists.newArrayList(appenders)))
    }

    private fun addAppender(appenders: MutableList<PreferencesAppender>, preferencesAppender: PreferencesAppender?) {
        if (preferencesAppender != null && preferencesAppender.isEnabled()) {
            appenders.add(preferencesAppender)
        }
    }

    inner class PreferencesAppenderRecyclerViewType : RecyclerViewType<PreferencesAppender, PreferenceAppenderHolder>() {

        override fun getItemClass(): Class<PreferencesAppender> {
            return PreferencesAppender::class.java
        }

        override fun getLayoutResourceId(): Int {
            return R.layout.jdroid_default_item
        }

        override fun createViewHolderFromView(view: View): RecyclerView.ViewHolder {
            val holder = PreferenceAppenderHolder(view)
            holder.name = findView(view, R.id.name)
            return holder
        }

        override fun fillHolderFromItem(item: PreferencesAppender, holder: PreferenceAppenderHolder) {
            holder.name.setText(item.getNameResId())
        }

        override fun onItemSelected(item: PreferencesAppender, view: View) {
            PreferenceAppenderActivity.startActivity(activity, item)
        }

        override fun getRecyclerViewContainer(): RecyclerViewContainer {
            return this@DebugSettingsFragment
        }
    }

    class PreferenceAppenderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var name: TextView
    }
}