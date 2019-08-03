package com.jdroid.android.navdrawer

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.google.android.material.navigation.NavigationView
import com.jdroid.android.R
import com.jdroid.android.images.loader.ImageViewLoader

open class NavDrawerHeader(navigationView: NavigationView) {

    private val navDrawerHeader: View by lazy {
        navigationView.inflateHeaderView(getNavDrawerHeaderLayout())
    }

    @LayoutRes
    protected open fun getNavDrawerHeaderLayout(): Int {
        return R.layout.jdroid_nav_drawer_header
    }

    fun setTitle(title: String) {
        (navDrawerHeader.findViewById<View>(R.id.title) as TextView).text = title
    }

    fun setSubTitle(subTitle: String) {
        (navDrawerHeader.findViewById<View>(R.id.subTitle) as TextView).text = subTitle
    }

    fun setBackground(@DrawableRes imageResId: Int?) {
        (navDrawerHeader.findViewById<View>(R.id.cover) as ImageView).setImageResource(imageResId!!)
    }

    fun setBackground(imageUrl: String?, ttl: Long?, imageViewLoader: ImageViewLoader) {
        if (imageUrl != null) {
            imageViewLoader.displayImage(imageUrl, navDrawerHeader.findViewById<View>(R.id.cover) as ImageView, null, ttl)
        }
    }

    fun setMainImage(@DrawableRes imageResId: Int?) {
        (navDrawerHeader.findViewById<View>(R.id.photo) as ImageView).setImageResource(imageResId!!)
    }

    fun setMainImage(imageUrl: String?, ttl: Long?, imageViewLoader: ImageViewLoader) {
        if (imageUrl != null) {
            imageViewLoader.displayImage(
                imageUrl,
                navDrawerHeader.findViewById<View>(R.id.photo) as ImageView,
                R.drawable.jdroid_person_default,
                ttl
            )
        }
    }
}
