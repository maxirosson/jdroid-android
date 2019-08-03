package com.jdroid.android.exception

import android.content.DialogInterface
import androidx.fragment.app.FragmentActivity
import com.jdroid.android.R
import com.jdroid.android.dialog.AlertDialogFragment
import com.jdroid.android.kotlin.getRequiredSerializableArgument

class ErrorDialogFragment : AlertDialogFragment() {

    override fun onPositiveClick() {
        handleStrategy()
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        handleStrategy()
    }

    private fun handleStrategy() {
        (getRequiredSerializableArgument(ERROR_DIALOG_STRATEGY_EXTRA) as ErrorDialogStrategy).onPositiveClick(activity)
    }

    companion object {

        private const val ERROR_DIALOG_STRATEGY_EXTRA = "errorDialogStrategyExtra"

        fun show(activity: FragmentActivity, title: String, message: String, goBackOnError: Boolean) {
            val defaultErrorDialogStrategy = DefaultErrorDialogStrategy()
            defaultErrorDialogStrategy.setGoBackOnError(goBackOnError)
            show(activity, title, message, defaultErrorDialogStrategy)
        }

        fun show(activity: FragmentActivity, title: String, message: String, errorDialogStrategy: ErrorDialogStrategy) {

            // This code is intentionally left out of the "if" statement to consume time before the call to
            // "findFragmentByTag" and minimize the possibility of showing the dialog twice
            val fragment = ErrorDialogFragment()
            fragment.addParameter(ERROR_DIALOG_STRATEGY_EXTRA, errorDialogStrategy)

            val okButton = activity.getString(R.string.jdroid_ok)
            val dialogTag = generateDialogTag(title, message, errorDialogStrategy)
            val currentErrorDialogFragment = activity.supportFragmentManager.findFragmentByTag(dialogTag)
            if (currentErrorDialogFragment == null) {
                show(activity, fragment, null, title, message, null, null, okButton, true, null, dialogTag)
            }
        }

        private fun generateDialogTag(title: String?, message: String?, errorDialogStrategy: ErrorDialogStrategy): String {
            val builder = StringBuilder()
            if (title != null) {
                builder.append(title)
            }
            if (message != null) {
                builder.append(message)
            }
            builder.append(errorDialogStrategy.javaClass.simpleName)
            return builder.toString().hashCode().toString()
        }
    }
}
