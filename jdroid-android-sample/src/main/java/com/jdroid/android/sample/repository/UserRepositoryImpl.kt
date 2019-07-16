package com.jdroid.android.sample.repository

import com.jdroid.android.domain.User
import com.jdroid.android.repository.UserRepository

class UserRepositoryImpl : UserRepository {

    override fun getUser(): User? {
        return null
    }

    override fun saveUser(user: User) {
    }

    override fun removeUser() {
    }
}
