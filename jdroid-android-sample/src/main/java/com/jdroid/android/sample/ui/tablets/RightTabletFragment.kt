package com.jdroid.android.sample.ui.tablets

import android.view.MenuItem

import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.android.utils.ScreenUtils
import com.jdroid.android.utils.ToastUtils

class RightTabletFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.right_tablet_fragment
    }

    override fun getMenuResourceId(): Int? {
        return R.menu.right_tablet_menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.rightAdd -> {
                ToastUtils.showToast(R.string.rightAction)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun isSecondaryFragment(): Boolean {
        return ScreenUtils.is10Inches()
    }
}
