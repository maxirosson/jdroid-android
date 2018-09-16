package com.jdroid.android.sample.ui.twitter;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.twitter.CyclingTwitterHelper;
import com.jdroid.android.twitter.TwitterHelper;
import com.jdroid.android.twitter.TwitterListFragment;
import com.twitter.sdk.android.tweetui.SearchTimeline;

public class CyclingTwitterFragment extends TwitterListFragment {

	@Override
	protected TwitterHelper createTwitterHelper() {
		return new CyclingTwitterHelper() {

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
				return CyclingTwitterFragment.this;
			}
		};
	}
}
