package com.jdroid.android.loading

import com.jdroid.android.fragment.FragmentIf

interface FragmentLoading {

    fun onViewCreated(fragmentIf: FragmentIf)

    fun show(fragmentIf: FragmentIf)

    fun dismiss(fragmentIf: FragmentIf)
}
