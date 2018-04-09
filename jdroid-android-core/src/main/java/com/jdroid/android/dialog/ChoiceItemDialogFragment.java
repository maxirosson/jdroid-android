package com.jdroid.android.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import java.io.Serializable;
import java.util.List;

public class ChoiceItemDialogFragment extends AbstractDialogFragment {
	
	public static final String ITEMS_EXTRA = "itemsExtra";
	public static final String CURRENT_ITEM_EXTRA = "currentItemExtra";
	public static final String RESOURCE_TITLE_ID_EXTRA = "resourceTitleId";
	private List<ChoiceItem> values;
	private ChoiceItem selectedItem;

	/**
	 * @param targetFragment The fragment that invokes the dialog
	 * @param values The list must implements ChoiceItem
	 * @param currentItem
	 * @param resourceTitleId
	 */
	public static void show(Fragment targetFragment, List<? extends ChoiceItem> values, ChoiceItem currentItem,
			int resourceTitleId) {
		FragmentManager fm = targetFragment.getFragmentManager();
		ChoiceItemDialogFragment fragment = new ChoiceItemDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(ITEMS_EXTRA, (Serializable)values);
		bundle.putSerializable(CURRENT_ITEM_EXTRA, currentItem);
		bundle.putInt(RESOURCE_TITLE_ID_EXTRA, resourceTitleId);
		fragment.setArguments(bundle);
		fragment.setTargetFragment(targetFragment, 0);
		if (fm.findFragmentByTag(ChoiceItemDialogFragment.class.getSimpleName()) == null) {
			fragment.show(fm, ChoiceItemDialogFragment.class.getSimpleName());
		}
		
	}
	
	@NonNull
	@SuppressWarnings("unchecked")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		values = (List<ChoiceItem>)getArguments().getSerializable(ITEMS_EXTRA);
		selectedItem = (ChoiceItem)getArguments().getSerializable(CURRENT_ITEM_EXTRA);
		int resourceTitleId = getArguments().getInt(RESOURCE_TITLE_ID_EXTRA);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(resourceTitleId);
		builder.setSingleChoiceItems(getAvailableItems(), values.indexOf(selectedItem),
			new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ChoiceItemListener<ChoiceItem> listener = (ChoiceItemListener<ChoiceItem>)getTargetFragment();
					if (values.get(which).equals(selectedItem)) {
						listener.onCurrentItemSelected(selectedItem);
					} else {
						listener.onNewItemSelected(values.get(which));
					}
					dismiss();
				}
			});
		
		Dialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}
	
	/**
	 * @see android.support.v4.app.DialogFragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		
		// This is for rotation change, to preserve the dialog
		if ((getDialog() != null) && getRetainInstance()) {
			getDialog().setDismissMessage(null);
		}
		super.onDestroyView();
	}
	
	private CharSequence[] getAvailableItems() {
		CharSequence[] availableModes = new CharSequence[values.size()];
		for (int i = 0; i < values.size(); i++) {
			availableModes[i] = getString(values.get(i).getResourceTitleId());
		}
		return availableModes;
	}
}
