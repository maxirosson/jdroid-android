package com.jdroid.android.dialog

import java.io.Serializable

interface ChoiceItem : Serializable {

    fun getResourceTitleId(): Int
}
