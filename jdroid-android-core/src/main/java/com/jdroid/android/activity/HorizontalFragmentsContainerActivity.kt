package com.jdroid.android.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.jdroid.android.R
import com.jdroid.android.fragment.FragmentIf

abstract class HorizontalFragmentsContainerActivity : AbstractFragmentActivity() {

    override fun getContentView(): Int {
        return if (isNavDrawerEnabled()) R.layout.jdroid_nav_horizontal_fragments_container_activity else R.layout.jdroid_horizontal_fragments_container_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null && !isFinishing) {

            val fragmentTransaction = supportFragmentManager.beginTransaction()

            val leftFragment = createNewLeftFragment()
            fragmentTransaction.add(R.id.leftFragmentContainer, leftFragment, leftFragment.javaClass.simpleName)

            val rightFragment = createNewRightFragment()
            fragmentTransaction.add(R.id.rightFragmentContainer, rightFragment, rightFragment.javaClass.simpleName)

            if (addToBackStack()!!) {
                fragmentTransaction.addToBackStack(rightFragment.javaClass.simpleName)
            }
            fragmentTransaction.commit()
        }
    }

    protected fun addToBackStack(): Boolean? {
        return false
    }

    protected open fun createNewLeftFragment(): Fragment {
        return instanceFragment(getLeftFragmentClass(), intent.extras)
    }

    protected open fun getLeftFragmentClass(): Class<out Fragment>? {
        return null
    }

    protected open fun createNewRightFragment(): Fragment {
        return instanceFragment(getRightFragmentClass(), intent.extras)
    }

    protected open fun getRightFragmentClass(): Class<out Fragment>? {
        return null
    }

    fun getLeftFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.leftFragmentContainer)
    }

    fun getRightFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.rightFragmentContainer)
    }

    override fun getMenuResourceId(): Int? {
        val menuResourceId = super.getMenuResourceId()
        if (menuResourceId == null) {
            val fragment = getLeftFragment()
            if (fragment != null && fragment is FragmentIf) {
                return (fragment as FragmentIf).getMenuResourceId()
            }
        }
        return menuResourceId
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item) || getLeftFragment()!!.onOptionsItemSelected(item) || getRightFragment()!!.onOptionsItemSelected(item)
    }
}
