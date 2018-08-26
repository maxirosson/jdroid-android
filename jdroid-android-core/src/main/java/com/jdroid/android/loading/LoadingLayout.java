package com.jdroid.android.loading;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.jdroid.android.R;
import com.jdroid.android.fragment.FragmentIf;

public class LoadingLayout extends FrameLayout {

	private ProgressBar progressLoading;
	private Button retryButton;
	private Boolean isLoading = false;

	public LoadingLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public LoadingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LoadingLayout(Context context) {
		super(context);
		init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.jdroid_non_blocking_loading, this, true);
		progressLoading = (ProgressBar)this.findViewById(R.id.loadingProgressBar);
	}

	private void updateViewState() {
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if (child == progressLoading) {
				child.setVisibility(isLoading ? VISIBLE : GONE);
			} else {
				child.setVisibility(isLoading ? GONE : VISIBLE);
			}
		}
	}

	public void setLoading(boolean loading) {
		isLoading = loading;
		updateViewState();
	}

	// TODO Retry. WIP
	@SuppressWarnings("unused")
	private void setRetry() {
		if (retryButton != null) {
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.jdroid_retry, this, true);
			retryButton = (Button)this.findViewById(R.id.retry);
			retryButton.setVisibility(View.VISIBLE);
		}
	}

	public void showLoading(FragmentIf fragmentIf) {
		if (isLoading) {
			return;
		}
		isLoading = true;
		if (fragmentIf != null) {
			fragmentIf.executeOnUIThread(new Runnable() {

				@Override
				public void run() {
					for (int i = 0; i < getChildCount(); i++) {
						View child = getChildAt(i);
						if (child == progressLoading) {
							animateVisibility(child, VISIBLE);
						} else {
							animateVisibility(child, GONE);
						}
					}
				}
			});
		}
	}

	public void dismissLoading(FragmentIf fragmentIf) {
		if (!isLoading) {
			return;
		}
		if (fragmentIf != null) {
			fragmentIf.executeOnUIThread(new Runnable() {

				@Override
				public void run() {
					for (int i = 0; i < getChildCount(); i++) {
						View child = getChildAt(i);
						if (child == progressLoading) {
							animateVisibility(child, GONE);
						} else {
							animateVisibility(child, VISIBLE);
						}
					}
				}
			});
		}
		isLoading = false;
	}

	public boolean isLoadingVisible() {
		return progressLoading.getVisibility() == View.VISIBLE;
	}

	public void animateVisibility(final View view, final int visibility) {
		if ((visibility == VISIBLE) && (view.getVisibility() != VISIBLE)) {
			view.clearAnimation();
			view.setVisibility(VISIBLE);
			Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
			animation.setFillBefore(true);
			view.startAnimation(animation);
		} else if ((visibility != VISIBLE) && (view.getVisibility() == VISIBLE)) {
			view.clearAnimation();
			view.setVisibility(visibility);
			Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
			animation.setFillBefore(true);
			view.startAnimation(animation);
		}
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		return new SavedState(superState, isLoading);
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState)state;
		super.onRestoreInstanceState(ss.getSuperState());
		isLoading = ss.isLoading;
		updateViewState();
	}

	/**
	 * Class for managing state storing/restoring.
	 */
	private static class SavedState extends BaseSavedState {

		private Boolean isLoading;

		/**
		 * Constructor called from {@link DatePicker#onSaveInstanceState()}
		 */
		private SavedState(Parcelable superState, Boolean isLoading) {
			super(superState);
			this.isLoading = isLoading;
		}

		/**
		 * Constructor called from {@link #CREATOR}
		 */
		private SavedState(Parcel in) {
			super(in);
			isLoading = in.readInt() == 1;
		}

		@Override
		public void writeToParcel(@NonNull Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(isLoading ? 1 : 0);
		}

		@SuppressWarnings({ "hiding", "unused" })
		public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}
