package com.jdroid.android.recycler;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;


public interface RecyclerViewContainer {

	RecyclerView getRecyclerView();

	RecyclerViewAdapter getRecyclerViewAdapter();

	Activity getActivity();
}
