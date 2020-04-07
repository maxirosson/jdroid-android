package com.jdroid.android.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

/**
 * Text view used to show a badge with a number of notifications. If the notifications are less than zero, the badge
 * isn't visible.
 */
class BadgeView(context: Context, attrs: AttributeSet? = null) : AppCompatTextView(context, attrs) {

    companion object {
        private const val DEFAULT_MAXIMUM = 9
    }

    var maximum = DEFAULT_MAXIMUM

    init {
        if (isInEditMode) {
            text = "1"
        } else {
            // hide by default
            visibility = View.GONE
        }

        // Adding styles
        setPadding(8, 4, 8, 4)
        setTextColor(Color.WHITE)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        typeface = Typeface.DEFAULT_BOLD
    }

    /**
     * Sets a notification number in the badge.
     *
     * @param notifications
     */
    @SuppressLint("SetTextI18n")
    fun setNotifications(notifications: Int?) {
        if (notifications != null && notifications > 0) {
            visibility = View.VISIBLE
            if (notifications > maximum) {
                this.text = "$maximum+"
            } else {
                this.text = notifications.toString()
            }
        } else {
            visibility = View.GONE
        }
    }
}
