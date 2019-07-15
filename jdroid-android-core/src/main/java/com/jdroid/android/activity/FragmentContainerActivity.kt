package com.jdroid.android.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.jdroid.android.R
import com.jdroid.android.fragment.FragmentIf

abstract class FragmentContainerActivity : AbstractFragmentActivity() {

    override fun getContentView(): Int {
        return if (isNavDrawerEnabled()) R.layout.jdroid_nav_fragment_container_activity else R.layout.jdroid_fragment_container_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null && !isFinishing) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment = createNewFragment()
            fragmentTransaction.add(getFragmentContainerId(), fragment, fragment.javaClass.simpleName)
            if (addToBackStack()) {
                fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
            }
            fragmentTransaction.commit()
        }
    }

    protected open fun getFragmentContainerId(): Int {
        return R.id.fragmentContainer
    }

    fun commitFragment(fragment: Fragment) {
        commitFragment(getFragmentContainerId(), fragment)
    }

    protected open fun addToBackStack(): Boolean {
        return false
    }

    protected open fun createNewFragment(): Fragment {
        return instanceFragment(getFragmentClass(), getFragmentExtras())
    }

    protected open fun getFragmentExtras(): Bundle {
        return intent.extras ?: Bundle()
    }

    protected open fun getFragmentClass(): Class<out Fragment>? {
        return null
    }

    fun getFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(getFragmentContainerId())
    }

    fun getFragmentIf(): FragmentIf? {
        val fragment = getFragment()
        return if (fragment != null && fragment is FragmentIf) {
            fragment
        } else null
    }

    override fun getMenuResourceId(): Int? {
        val menuResourceId = super.getMenuResourceId()
        if (menuResourceId == null) {
            val fragment = getFragment()
            if (fragment != null && fragment is FragmentIf) {
                return (fragment as FragmentIf).getMenuResourceId()
            }
        }
        return menuResourceId
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val fragment = getFragment()
        fragment?.onPrepareOptionsMenu(menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val fragment = getFragment()
        return super.onOptionsItemSelected(item) || fragment != null && fragment.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fragment = getFragment()
        fragment?.onActivityResult(requestCode, resultCode, data)
    }

    @CallSuper
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val fragment = getFragmentIf()
        fragment?.onNewIntent(intent)
    }

    override fun onBackPressedHandled(): Boolean {
        var handled = super.onBackPressedHandled()
        if (!handled) {
            val fragment = getFragmentIf()
            if (fragment != null) {
                handled = fragment.onBackPressedHandled()
            }
        }
        return handled
    }
}
