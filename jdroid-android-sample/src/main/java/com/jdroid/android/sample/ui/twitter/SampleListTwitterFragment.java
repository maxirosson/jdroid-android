package com.jdroid.android.sample.ui.twitter;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.twitter.ListTwitterHelper;
import com.jdroid.android.twitter.TwitterListFragment;
import com.twitter.sdk.android.tweetui.SearchTimeline;

public class SampleListTwitterFragment extends TwitterListFragment {

	@Override
	protected ListTwitterHelper createTwitterHelper() {
		return new ListTwitterHelper() {

			@Override
			protected SearchTimeline createSearchTimeline() {
				SearchTimeline.Builder searchTimelineBuilder = new SearchTimeline.Builder();
				StringBuilder queryBuilder = new StringBuilder();
				queryBuilder.append("android");
				searchTimelineBuilder.maxItemsPerRequest(15);
				searchTimelineBuilder.languageCode("en");
				searchTimelineBuilder.query(queryBuilder.toString());
				return searchTimelineBuilder.build();
			}

			@Override
			public AbstractFragment getAbstractFragment() {
				return SampleListTwitterFragment.this;
			}
		};
	}
}
