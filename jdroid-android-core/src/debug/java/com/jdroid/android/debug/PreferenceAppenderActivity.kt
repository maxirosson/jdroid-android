package com.jdroid.android.debug

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.jdroid.android.R
import com.jdroid.android.activity.AbstractFragmentActivity
import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.fragment.AbstractPreferenceFragment
import com.jdroid.java.exception.UnexpectedException

class PreferenceAppenderActivity : AbstractFragmentActivity() {

    companion object {

        const val APPENDER_EXTRA = "prefAppender"

        fun startActivity(activity: Activity?, preferencesAppender: PreferencesAppender) {
            val intent = Intent(activity, PreferenceAppenderActivity::class.java)
            intent.putExtra(APPENDER_EXTRA, preferencesAppender)
            ActivityLauncher.startActivity(activity, intent)
        }
    }

    override fun getContentView(): Int {
        return R.layout.jdroid_fragment_container_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.fragmentContainer, createNewFragment())
            fragmentTransaction.commit()
        }
    }

    protected fun createNewFragment(): AbstractPreferenceFragment {
        return instanceAbstractPreferenceFragment(PreferenceAppenderFragment::class.java, intent.extras)
    }

    private fun <E : AbstractPreferenceFragment> instanceAbstractPreferenceFragment(fragmentClass: Class<E>, bundle: Bundle?): E {
        val fragment: E
        try {
            fragment = fragmentClass.newInstance()
        } catch (e: InstantiationException) {
            throw UnexpectedException(e)
        } catch (e: IllegalAccessException) {
            throw UnexpectedException(e)
        }

        fragment.arguments = bundle
        return fragment
    }

    override fun getMenuResourceId(): Int? {
        return null
    }
}