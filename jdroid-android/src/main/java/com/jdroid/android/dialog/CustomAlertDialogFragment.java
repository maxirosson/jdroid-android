package com.jdroid.android.dialog;

import java.io.Serializable;
import java.util.Map;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.jdroid.android.R;
import com.jdroid.java.collections.Maps;

/**
 * 
 * @author Maxi Rosson
 */
public class CustomAlertDialogFragment extends AbstractDialogFragment {
	
	private static final String TITLE_EXTRA = "titleExtra";
	private static final String MESSAGE_EXTRA = "messageExtra";
	private static final String POSITIVE_BUTTON_TEXT_EXTRA = "positiveButtonTextExtra";
	private static final String NEGATIVE_BUTTON_TEXT_EXTRA = "negativeButtonTextExtra";
	private static final String CANCELABLE_EXTRA = "cancelableExtra";
	
	private String title;
	private String message;
	private String positiveButtonText;
	private String negativeButtonText;
	private Boolean cancelable;
	private Map<String, Serializable> parameters = Maps.newHashMap();
	
	public static void show(Fragment fragment, String title, String message, String positiveButtonText,
			String negativeButtonText, Boolean cancelable) {
		show(fragment.getActivity(), new CustomAlertDialogFragment(), title, message, positiveButtonText,
			negativeButtonText, cancelable);
	}
	
	public static void show(FragmentActivity fragmentActivity, CustomAlertDialogFragment alertDialogFragment,
			String title, String message, String positiveButtonText, String negativeButtonText, Boolean cancelable) {
		show(fragmentActivity.getSupportFragmentManager(), alertDialogFragment, title, message, positiveButtonText,
			negativeButtonText, cancelable);
	}
	
	public static void show(FragmentManager fragmentManager, CustomAlertDialogFragment alertDialogFragment,
			String title, String message, String positiveButtonText, String negativeButtonText, Boolean cancelable) {
		
		Bundle bundle = new Bundle();
		bundle.putSerializable(TITLE_EXTRA, title);
		bundle.putSerializable(MESSAGE_EXTRA, message);
		bundle.putSerializable(POSITIVE_BUTTON_TEXT_EXTRA, positiveButtonText);
		bundle.putSerializable(NEGATIVE_BUTTON_TEXT_EXTRA, negativeButtonText);
		bundle.putSerializable(CANCELABLE_EXTRA, cancelable);
		for (Map.Entry<String, Serializable> entry : alertDialogFragment.parameters.entrySet()) {
			bundle.putSerializable(entry.getKey(), entry.getValue());
		}
		alertDialogFragment.setArguments(bundle);
		
		alertDialogFragment.show(fragmentManager, alertDialogFragment.getClass().getSimpleName());
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		title = getArgument(TITLE_EXTRA);
		message = getArgument(MESSAGE_EXTRA);
		positiveButtonText = getArgument(POSITIVE_BUTTON_TEXT_EXTRA);
		negativeButtonText = getArgument(NEGATIVE_BUTTON_TEXT_EXTRA);
		cancelable = getArgument(CANCELABLE_EXTRA);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.alert_dialog_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		TextView contentText = findView(R.id.contentText);
		contentText.setText(message);
		
		if (positiveButtonText != null) {
			Button positiveButton = findView(R.id.rightButton);
			positiveButton.setText(positiveButtonText);
			positiveButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onPositiveClick();
				}
			});
		}
		if (negativeButtonText != null) {
			Button negativeButton = findView(R.id.leftButton);
			negativeButton.setText(negativeButtonText);
			negativeButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onNegativeClick();
				}
			});
		}
		
		getDialog().setTitle(title);
		getDialog().setCancelable(cancelable);
	}
	
	protected void onPositiveClick() {
		// Do nothing by default
	}
	
	protected void onNegativeClick() {
		dismiss();
	}
	
	public void addParameter(String key, Serializable value) {
		parameters.put(key, value);
	}
}
