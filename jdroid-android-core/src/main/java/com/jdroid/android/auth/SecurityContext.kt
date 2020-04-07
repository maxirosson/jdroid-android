package com.jdroid.android.auth

import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.domain.User

object SecurityContext {

    /**
     * @return The user logged in the application
     */
    var user: User? = null
        private set

    init {
        user = AbstractApplication.get().userRepository?.user
    }

    fun attach(user: User) {
        this.user = user
        AbstractApplication.get().userRepository!!.saveUser(user)
    }

    fun detachUser() {
        AbstractApplication.get().userRepository!!.removeUser()
        user = null
    }

    /**
     * @return Whether the user is authenticated or not
     */
    fun isAuthenticated(): Boolean {
        return user != null
    }
}
