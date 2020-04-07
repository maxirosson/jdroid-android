package com.jdroid.android.recycler

import android.app.Activity

import androidx.recyclerview.widget.RecyclerView

interface RecyclerViewContainer {

    fun getRecyclerView(): RecyclerView

    fun getRecyclerViewAdapter(): RecyclerViewAdapter

    fun getActivity(): Activity
}
