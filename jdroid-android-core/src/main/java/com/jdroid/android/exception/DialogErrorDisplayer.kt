package com.jdroid.android.exception

import com.jdroid.java.exception.AbstractException
import androidx.fragment.app.FragmentActivity

open class DialogErrorDisplayer(val goBackOnErrorByDefault: Boolean = true) : AbstractErrorDisplayer() {

    public override fun onDisplayError(activity: FragmentActivity?, title: String, description: String, throwable: Throwable) {
        if (activity != null) {
            ErrorDialogFragment.show(activity, title, description, getErrorDialogStrategy(throwable))
        }
    }

    private fun getErrorDialogStrategy(throwable: Throwable): ErrorDialogStrategy {
        return if (throwable is AbstractException) {
            if (throwable.hasParameter(ERROR_DIALOG_STRATEGY_KEY))
                throwable.getParameter<ErrorDialogStrategy>(ERROR_DIALOG_STRATEGY_KEY)
            else
                getDefaultErrorDialogStrategy(throwable)
        } else getDefaultErrorDialogStrategy(throwable)
    }

    private fun getDefaultErrorDialogStrategy(throwable: Throwable): DefaultErrorDialogStrategy {
        val defaultStrategy = DefaultErrorDialogStrategy()
        if (throwable is AbstractException) {
            defaultStrategy.setGoBackOnError(
                if (throwable.hasParameter(GO_BACK_KEY))
                    throwable.getParameter(GO_BACK_KEY)
                else
                    goBackOnErrorByDefault(throwable)
            )
        } else {
            defaultStrategy.setGoBackOnError(goBackOnErrorByDefault(throwable))
        }
        return defaultStrategy
    }

    protected open fun goBackOnErrorByDefault(throwable: Throwable): Boolean {
        return goBackOnErrorByDefault
    }

    companion object {

        private const val GO_BACK_KEY = "goBack"
        private const val ERROR_DIALOG_STRATEGY_KEY = "errorDialogStrategy"

        fun setErrorDialogStrategy(abstractException: AbstractException, strategy: ErrorDialogStrategy) {
            abstractException.addParameter(ERROR_DIALOG_STRATEGY_KEY, strategy)
        }

        fun markAsGoBackOnError(abstractException: AbstractException) {
            abstractException.addParameter(GO_BACK_KEY, true)
        }

        fun markAsNotGoBackOnError(abstractException: AbstractException) {
            abstractException.addParameter(GO_BACK_KEY, false)
        }
    }
}
