package com.jdroid.android.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.jdroid.android.application.AbstractApplication
import com.jdroid.java.utils.LoggerUtils

/**
 * Launcher for all the activities of the application
 */
object ActivityLauncher {

    private val LOGGER = LoggerUtils.getLogger(ActivityLauncher::class.java)

    /**
     * Launches the [AbstractApplication.getHomeActivityClass]
     */
    fun startHomeActivity(activity: Activity?) {
        if (activity != null) {
            if (activity.javaClass != AbstractApplication.get().homeActivityClass) {
                val intent = Intent(activity, AbstractApplication.get().homeActivityClass)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                activity.startActivity(intent)
            }
        } else {
            LOGGER.warn("Null activity. Ignoring launch of " + AbstractApplication.get().homeActivityClass.simpleName)
        }
    }

    /**
     * Launches a new [Activity]
     *
     * @param targetActivityClass The target [Activity] class to launch
     * @param bundle The extra [Bundle]
     */
    fun startActivity(activity: Activity?, targetActivityClass: Class<out Activity>, bundle: Bundle?) {
        if (activity != null) {
            if (activity.javaClass != targetActivityClass) {
                val intent = Intent(activity, targetActivityClass)
                if (bundle != null) {
                    intent.putExtras(bundle)
                }
                activity.startActivity(intent)
            }
        } else {
            LOGGER.warn("Null activity. Ignoring launch of " + targetActivityClass.simpleName)
        }
    }

    /**
     * Launches a new [Activity]
     *
     * @param targetActivityClass The target [Activity] class to launch
     * @param bundle The extra [Bundle]
     * @param requestCode If >= 0, this code will be returned in onActivityResult() when the activity exits.
     */
    fun startActivity(activity: Activity?, targetActivityClass: Class<out Activity>, bundle: Bundle, requestCode: Int) {
        if (activity != null) {
            if (activity.javaClass != targetActivityClass) {
                val intent = Intent(activity, targetActivityClass)
                intent.putExtras(bundle)
                activity.startActivityForResult(intent, requestCode)
            }
        } else {
            LOGGER.warn("Null activity. Ignoring launch of " + targetActivityClass.simpleName)
        }
    }

    fun startActivityNewTask(intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        AbstractApplication.get().startActivity(intent)
    }

    /**
     * Launches a new [Activity]
     *
     * @param targetActivityClass The target [Activity] class to launch
     */
    fun startActivity(activity: Activity?, targetActivityClass: Class<out Activity>) {
        if (activity != null) {
            if (activity.javaClass != targetActivityClass) {
                val intent = Intent(activity, targetActivityClass)
                activity.startActivity(intent)
            }
        } else {
            LOGGER.warn("Null activity. Ignoring launch of " + targetActivityClass.simpleName)
        }
    }

    fun startActivity(activity: Activity?, intent: Intent) {
        if (activity != null) {
            activity.startActivity(intent)
        } else {
            LOGGER.warn("Null activity. Ignoring starting activity for intent: $intent")
        }
    }

    fun startActivityForResult(activity: Activity?, intent: Intent, requestCode: Int) {
        if (activity != null) {
            activity.startActivityForResult(intent, requestCode)
        } else {
            LOGGER.warn("Null activity. Ignoring starting activity for intent: $intent")
        }
    }
}
