package com.jdroid.android

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.io.Serializable

interface ActionItem : Serializable {

    fun getId(): String

    fun getIconResource(): Int

    fun getNameResource(): Int

    fun getDescriptionResource(): Int?

    fun createFragment(args: Any): Fragment?

    fun startActivity(fragmentActivity: FragmentActivity)

    fun getIntent(): Intent

    fun matchesActivity(fragmentActivity: FragmentActivity): Boolean
}
