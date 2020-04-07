package com.jdroid.android.provider;

import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.jdroid.android.R;
import com.jdroid.android.utils.LocalizationUtils;

import androidx.core.view.ActionProvider;

public abstract class TwoStateActionProvider extends ActionProvider {

	private static final int FIRST_STATE = 0;
	private static final int LOADING_STATE = 1;
	private static final int SECOND_STATE = 2;

	private ViewFlipper viewFlipper;
	private TwoStateClickListener onItemClickListener;

	public TwoStateActionProvider(final Context context) {
		super(context);

		viewFlipper = (ViewFlipper)LayoutInflater.from(context).inflate(getLayoutResId(), null);

		ImageView firstStateView = (ImageView)viewFlipper.findViewById(R.id.firstState);
		firstStateView.setOnClickListener(new StateViewClickListener());
		firstStateView.setOnLongClickListener(new LongClickListener(context));
		firstStateView.setImageResource(getFirstStateImageResId());
		viewFlipper.setDisplayedChild(FIRST_STATE);

		if (getSecondStateImageResId() != null) {
			ImageView secondStateView = (ImageView)viewFlipper.findViewById(R.id.secondState);
			secondStateView.setOnClickListener(new StateViewClickListener());
			secondStateView.setOnLongClickListener(new LongClickListener(context));
			secondStateView.setImageResource(getSecondStateImageResId());
			secondStateView.setVisibility(View.VISIBLE);
		}
	}

	public void stopLoading(boolean firstState) {
		viewFlipper.setDisplayedChild(firstState ? FIRST_STATE : SECOND_STATE);
	}

	public void startLoading() {
		viewFlipper.setDisplayedChild(LOADING_STATE);
	}

	protected Integer getLayoutResId() {
		return R.layout.jdroid_two_state_action_item;
	}

	protected abstract Integer getFirstStateImageResId();

	protected abstract Integer getFirstStateCheatSheetResId();

	protected Integer getSecondStateImageResId() {
		return null;
	}

	protected Integer getSecondStateCheatSheetResId() {
		return null;
	}

	protected String getFirstStateCheatSheet() {
		return LocalizationUtils.INSTANCE.getString(getFirstStateCheatSheetResId());
	}

	protected String getSecondStateCheatSheet() {
		return LocalizationUtils.INSTANCE.getString(getSecondStateCheatSheetResId());
	}

	@Override
	public View onCreateActionView() {
		return viewFlipper;
	}

	private class LongClickListener implements OnLongClickListener {

		private final Context context;

		private LongClickListener(Context context) {
			this.context = context;
		}

		@Override
		public boolean onLongClick(View view) {
			final int[] screenPos = new int[2];
			final Rect displayFrame = new Rect();
			view.getLocationOnScreen(screenPos);
			view.getWindowVisibleDisplayFrame(displayFrame);

			final int width = view.getWidth();
			final int height = view.getHeight();
			final int midy = screenPos[1] + (height / 2);
			final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

			String title = viewFlipper.getDisplayedChild() == FIRST_STATE ? getFirstStateCheatSheet()
				: getSecondStateCheatSheet();
			Toast cheatSheet = Toast.makeText(context, title, Toast.LENGTH_SHORT);
			if (midy < displayFrame.height()) {
				// Show along the top; follow action buttons
				cheatSheet.setGravity(Gravity.TOP | Gravity.RIGHT, screenWidth - screenPos[0] - (width / 2), height);
			} else {
				// Show along the bottom center
				cheatSheet.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, height);
			}
			cheatSheet.show();
			return true;
		}
	}

	private final class StateViewClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			viewFlipper.setDisplayedChild(LOADING_STATE);

			if (onItemClickListener != null) {
				onItemClickListener.onClick();
			}
		}
	}

	/**
	 * @param onItemClickListener the onItemClickListener to set
	 */
	public void setOnItemClickListener(TwoStateClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}
}
