package com.jdroid.android.sample.ui.hero

import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R

class HeroFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.hero_fragment
    }

    override fun isHeroImageEnabled(): Boolean? {
        return true
    }

    override fun getHeroImageId(): Int? {
        return R.id.heroImage
    }

    override fun getHeroImageContainerId(): Int? {
        return R.id.heroImageContainer
    }

    override fun getParallaxScrollViewId(): Int? {
        return R.id.scrollView
    }
}
