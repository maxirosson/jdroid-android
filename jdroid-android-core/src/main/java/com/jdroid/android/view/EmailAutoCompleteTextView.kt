package com.jdroid.android.view

import android.Manifest
import android.content.Context
import android.graphics.Rect
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.annotation.RequiresPermission
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import com.jdroid.android.utils.AndroidUtils

class EmailAutoCompleteTextView : AppCompatAutoCompleteTextView {

    @RequiresPermission(Manifest.permission.GET_ACCOUNTS)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    @RequiresPermission(Manifest.permission.GET_ACCOUNTS)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    @RequiresPermission(Manifest.permission.GET_ACCOUNTS)
    constructor(context: Context) : super(context) {
        init()
    }

    @RequiresPermission(Manifest.permission.GET_ACCOUNTS)
    private fun init() {
        if (!isInEditMode) {
            val adapter = ArrayAdapter(
                context,
                android.R.layout.simple_dropdown_item_1line, AndroidUtils.accountsEmails
            )
            setAdapter(adapter)
            inputType = (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
            imeOptions = EditorInfo.IME_ACTION_NEXT
        }
    }

    override fun enoughToFilter(): Boolean {
        return true
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)

        if (windowVisibility != View.GONE && focused) {
            performFiltering(text, 0)
            showDropDown()
        }
    }
}
