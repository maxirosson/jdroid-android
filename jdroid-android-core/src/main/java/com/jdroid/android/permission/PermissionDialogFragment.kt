package com.jdroid.android.permission

import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import com.jdroid.android.R
import com.jdroid.android.dialog.AlertDialogFragment

class PermissionDialogFragment : AlertDialogFragment() {

    private lateinit var permission: String
    private var permissionRequestCode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permission = getArgument<String>(PERMISSION_EXTRA)
        permissionRequestCode = getArgument(PERMISSION_REQUEST_CODE_EXTRA)
    }

    override fun onPositiveClick() {
        val targetFragment = this.targetFragment
        if (targetFragment != null) {
            targetFragment.requestPermissions(arrayOf(permission), permissionRequestCode)
        } else {
            ActivityCompat.requestPermissions(activity!!, arrayOf(permission), permissionRequestCode)
        }
    }

    companion object {

        const val PERMISSION_EXTRA = "permissionExtra"
        const val PERMISSION_REQUEST_CODE_EXTRA = "permissionRequestCodeExtra"

        fun show(fragmentActivity: FragmentActivity, title: String, message: CharSequence, permission: String, permissionRequestCode: Int) {
            show(fragmentActivity, null, title, message, permission, permissionRequestCode)
        }

        fun show(targetFragment: Fragment, title: String, message: CharSequence, permission: String, permissionRequestCode: Int) {
            show(targetFragment.activity, targetFragment, title, message, permission, permissionRequestCode)
        }

        private fun show(fragmentActivity: FragmentActivity?, targetFragment: Fragment?, title: String, message: CharSequence, permission: String, permissionRequestCode: Int) {
            fragmentActivity?.let {
                val fragment = PermissionDialogFragment()
                fragment.addParameter(PERMISSION_EXTRA, permission)
                fragment.addParameter(PERMISSION_REQUEST_CODE_EXTRA, permissionRequestCode)
                if (targetFragment != null) {
                    fragment.setTargetFragment(targetFragment, 0)
                }
                val screenViewName = PermissionDialogFragment::class.java.simpleName + "-" + permission
                show(fragmentActivity, fragment, null, title, message, fragmentActivity.getString(R.string.jdroid_cancel), null, fragmentActivity.getString(R.string.jdroid_ok), true, screenViewName, null)
            }
        }
    }
}
