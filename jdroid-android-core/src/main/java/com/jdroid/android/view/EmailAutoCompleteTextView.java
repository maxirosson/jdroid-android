package com.jdroid.android.view;

import android.Manifest;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.RequiresPermission;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;

import com.jdroid.android.utils.AndroidUtils;

public class EmailAutoCompleteTextView extends AppCompatAutoCompleteTextView {

	@RequiresPermission(Manifest.permission.GET_ACCOUNTS)
	public EmailAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@RequiresPermission(Manifest.permission.GET_ACCOUNTS)
	public EmailAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@RequiresPermission(Manifest.permission.GET_ACCOUNTS)
	public EmailAutoCompleteTextView(Context context) {
		super(context);
		init();
	}

	@RequiresPermission(Manifest.permission.GET_ACCOUNTS)
	private void init() {
		if (!isInEditMode()) {
			ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
					android.R.layout.simple_dropdown_item_1line, AndroidUtils.getAccountsEmails());
			setAdapter(adapter);
			setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
					| InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			setImeOptions(EditorInfo.IME_ACTION_NEXT);
		}
	}
	
	@Override
	public boolean enoughToFilter() {
		return true;
	}
	
	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
		
		if ((getWindowVisibility() != View.GONE) && focused) {
			performFiltering(getText(), 0);
			showDropDown();
		}
	}
	
}
