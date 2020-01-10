package com.jdroid.android.sample.ui.navdrawer

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.jdroid.android.activity.FragmentContainerActivity
import com.jdroid.android.auth.SecurityContext
import com.jdroid.android.domain.User
import com.jdroid.java.utils.RandomUtils

class UserNavDrawerActivity : FragmentContainerActivity() {

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = object : User {
            override fun getId(): Long? {
                return RandomUtils.getLong()
            }

            override fun getUserName(): String? {
                return null
            }

            override fun getFullname(): String {
                return "Tony Stark"
            }

            override fun getEmail(): String {
                return "tonystark@ironmail.com"
            }

            override fun getFirstName(): String? {
                return null
            }

            override fun getLastName(): String? {
                return null
            }

            override fun getUserToken(): String? {
                return null
            }

            override fun getProfilePictureUrl(): String? {
                return null
            }

            override fun getCoverPictureUrl(): String? {
                return null
            }
        }
        SecurityContext.attach(user)
    }

    override fun onStart() {
        super.onStart()

        SecurityContext.attach(user)
    }

    override fun onStop() {
        super.onStop()

        SecurityContext.detachUser()
    }

    override fun getFragmentClass(): Class<out Fragment> {
        return UserNavDrawerFragment::class.java
    }
}
