package com.jdroid.android.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;

import com.jdroid.java.collections.Maps;
import com.jdroid.java.utils.StringUtils;

import java.io.Serializable;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class AlertDialogFragment extends AbstractDialogFragment {

	private static final String TITLE_EXTRA = "titleExtra";
	private static final String MESSAGE_EXTRA = "messageExtra";
	private static final String IS_HTML_MESSAGE_EXTRA = "isHtmlMessageExtra";
	private static final String NEGATIVE_BUTTON_TEXT_EXTRA = "negativeButtonTextExtra";
	private static final String NEUTRAL_BUTTON_TEXT_EXTRA = "neutralButtonTextExtra";
	private static final String POSITIVE_BUTTON_TEXT_EXTRA = "positiveButtonTextExtra";
	private static final String SCREEN_VIEW_NAME = "screenViewName";


	private String title;
	private CharSequence message;
	private String negativeButtonText;
	private String neutralButtonText;
	private String positiveButtonText;
	private Map<String, Serializable> parameters = Maps.INSTANCE.newHashMap();
	private String screenViewName = null;

	public static void show(Fragment fragment, String title, CharSequence message, String negativeButtonText,
							String neutralButtonText, String positiveButtonText, Boolean cancelable) {
		show(fragment.getActivity(), new AlertDialogFragment(), title, message, negativeButtonText, neutralButtonText,
			positiveButtonText, cancelable);
	}

	public static void show(FragmentActivity fragmentActivity, AlertDialogFragment alertDialogFragment, String title,
							CharSequence message, String negativeButtonText, String neutralButtonText, String positiveButtonText,
							Boolean cancelable) {
		show(fragmentActivity.getSupportFragmentManager(), alertDialogFragment, null, title, message,
			negativeButtonText, neutralButtonText, positiveButtonText, cancelable, null, null);
	}

	public static void show(FragmentActivity fragmentActivity, AlertDialogFragment alertDialogFragment,
							Fragment targetFragment, String title, CharSequence message, String negativeButtonText, String neutralButtonText,
							String positiveButtonText, Boolean cancelable, String screenViewName, String tag) {
		show(fragmentActivity.getSupportFragmentManager(), alertDialogFragment, targetFragment, title, message,
			negativeButtonText, neutralButtonText, positiveButtonText, cancelable, screenViewName, tag);
	}

	public static void show(FragmentManager fragmentManager, AlertDialogFragment alertDialogFragment,
							Fragment targetFragment, String title, CharSequence message, String negativeButtonText, String neutralButtonText,
							String positiveButtonText, Boolean cancelable, String screenViewName, String tag) {

		Bundle bundle = new Bundle();
		bundle.putSerializable(TITLE_EXTRA, title);
		if (message instanceof Spanned) {
			bundle.putSerializable(MESSAGE_EXTRA, Html.toHtml((Spanned)message));
			bundle.putBoolean(IS_HTML_MESSAGE_EXTRA, true);
		} else if (message != null) {
			bundle.putSerializable(MESSAGE_EXTRA, message.toString());
		}
		bundle.putSerializable(NEGATIVE_BUTTON_TEXT_EXTRA, negativeButtonText);
		bundle.putSerializable(NEUTRAL_BUTTON_TEXT_EXTRA, neutralButtonText);
		bundle.putSerializable(POSITIVE_BUTTON_TEXT_EXTRA, positiveButtonText);
		bundle.putSerializable(SCREEN_VIEW_NAME, screenViewName);
		for (Map.Entry<String, Serializable> entry : alertDialogFragment.parameters.entrySet()) {
			bundle.putSerializable(entry.getKey(), entry.getValue());
		}
		alertDialogFragment.setArguments(bundle);
		alertDialogFragment.setCancelable(cancelable);
		if (targetFragment != null) {
			alertDialogFragment.setTargetFragment(targetFragment, 0);
		}

		String dialogTag;
		if (StringUtils.isNotBlank(tag)) {
			dialogTag = tag;
		} else {
			dialogTag = alertDialogFragment.getClass().getSimpleName();
		}

		alertDialogFragment.show(fragmentManager, dialogTag);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		title = getArgument(TITLE_EXTRA);
		if (getArgument(IS_HTML_MESSAGE_EXTRA, false)) {
			message = StringUtils.removeTrailingWhitespaces(Html.fromHtml((String)getArgument(MESSAGE_EXTRA)));
		} else {
			message = getArgument(MESSAGE_EXTRA);
		}
		positiveButtonText = getArgument(POSITIVE_BUTTON_TEXT_EXTRA);
		neutralButtonText = getArgument(NEUTRAL_BUTTON_TEXT_EXTRA);
		negativeButtonText = getArgument(NEGATIVE_BUTTON_TEXT_EXTRA);
		screenViewName = getArgument(SCREEN_VIEW_NAME);
	}

	@NonNull
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
			dialogBuilder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					onNegativeClick();
				}
			});
		}
		if (neutralButtonText != null) {
			dialogBuilder.setNeutralButton(neutralButtonText, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					onNeutralClick();
				}
			});
		}
		if (positiveButtonText != null) {
			dialogBuilder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					onPositiveClick();
				}
			});
		}

		AlertDialog dialog = dialogBuilder.create();
		if ((positiveButtonText != null) && !dismissOnPositiveButtonClick()) {
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

	protected boolean dismissOnPositiveButtonClick() {
		return true;
	}

	protected View createContentView() {
		return null;
	}

	@NonNull
	@Override
	public String getScreenViewName() {
		return screenViewName != null ? screenViewName : super.getScreenViewName();
	}
}
