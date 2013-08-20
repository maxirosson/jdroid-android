package com.jdroid.android.dialog;

import java.io.Serializable;
import java.util.Map;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import com.jdroid.java.collections.Maps;

/**
 * 
 * @author Maxi Rosson
 */
public class AlertDialogFragment extends AbstractDialogFragment {
	
	private static final String TITLE_EXTRA = "titleExtra";
	private static final String MESSAGE_EXTRA = "messageExtra";
	private static final String NEGATIVE_BUTTON_TEXT_EXTRA = "negativeButtonTextExtra";
	private static final String NEUTRAL_BUTTON_TEXT_EXTRA = "neutralButtonTextExtra";
	private static final String POSITIVE_BUTTON_TEXT_EXTRA = "positiveButtonTextExtra";
	
	private String title;
	private String message;
	private String negativeButtonText;
	private String neutralButtonText;
	private String positiveButtonText;
	private Map<String, Serializable> parameters = Maps.newHashMap();
	
	public static void show(Fragment fragment, String title, String message, String negativeButtonText,
			String neutralButtonText, String positiveButtonText, Boolean cancelable) {
		show(fragment.getActivity(), new AlertDialogFragment(), title, message, negativeButtonText, neutralButtonText,
			positiveButtonText, cancelable);
	}
	
	public static void show(FragmentActivity fragmentActivity, AlertDialogFragment alertDialogFragment, String title,
			String message, String negativeButtonText, String neutralButtonText, String positiveButtonText,
			Boolean cancelable) {
		show(fragmentActivity.getSupportFragmentManager(), alertDialogFragment, title, message, negativeButtonText,
			neutralButtonText, positiveButtonText, cancelable);
	}
	
	public static void show(FragmentManager fragmentManager, AlertDialogFragment alertDialogFragment, String title,
			String message, String negativeButtonText, String neutralButtonText, String positiveButtonText,
			Boolean cancelable) {
		
		Bundle bundle = new Bundle();
		bundle.putSerializable(TITLE_EXTRA, title);
		bundle.putSerializable(MESSAGE_EXTRA, message);
		bundle.putSerializable(NEGATIVE_BUTTON_TEXT_EXTRA, negativeButtonText);
		bundle.putSerializable(NEUTRAL_BUTTON_TEXT_EXTRA, neutralButtonText);
		bundle.putSerializable(POSITIVE_BUTTON_TEXT_EXTRA, positiveButtonText);
		for (Map.Entry<String, Serializable> entry : alertDialogFragment.parameters.entrySet()) {
			bundle.putSerializable(entry.getKey(), entry.getValue());
		}
		alertDialogFragment.setArguments(bundle);
		alertDialogFragment.setCancelable(cancelable);
		
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
		neutralButtonText = getArgument(NEUTRAL_BUTTON_TEXT_EXTRA);
		negativeButtonText = getArgument(NEGATIVE_BUTTON_TEXT_EXTRA);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		dialogBuilder.setTitle(title);
		
		View contentView = createContentView();
		if (contentView != null) {
			dialogBuilder.setView(contentView);
		} else if (message != null) {
			dialogBuilder.setMessage(message);
		}
		
		if (negativeButtonText != null) {
			dialogBuilder.setNegativeButton(negativeButtonText, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					onNegativeClick();
				}
			});
		}
		if (neutralButtonText != null) {
			dialogBuilder.setNeutralButton(neutralButtonText, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					onNeutralClick();
				}
			});
		}
		if (positiveButtonText != null) {
			dialogBuilder.setPositiveButton(positiveButtonText, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					onPositiveClick();
				}
			});
		}
		
		AlertDialog dialog = dialogBuilder.create();
		if ((positiveButtonText != null) && !dismissOnPositivoButtonClick()) {
			// As workaround, to override default dismiss behavior the listener is directly set on the button.
			dialog.setOnShowListener(new DialogInterface.OnShowListener() {
				
				@Override
				public void onShow(final DialogInterface dialogInterface) {
					Button possitiveButton = ((AlertDialog)dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE);
					possitiveButton.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							onPositiveClick();
						}
					});
				}
			});
		}
		
		return dialog;
	}
	
	protected void onNegativeClick() {
		// Do nothing by default
	}
	
	protected void onPositiveClick() {
		// Do nothing by default
	}
	
	protected void onNeutralClick() {
		// Do nothing by default
	}
	
	public void addParameter(String key, Serializable value) {
		parameters.put(key, value);
	}
	
	protected boolean dismissOnPositivoButtonClick() {
		return true;
	}
	
	protected View createContentView() {
		return null;
	}
	
}
