package com.jdroid.android.sample.ui.facebook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.Profile
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.material.snackbar.Snackbar
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.loading.FragmentLoading
import com.jdroid.android.loading.NonBlockingLoading
import com.jdroid.android.sample.R

class FacebookSignInFragment : AbstractFragment() {

    private lateinit var status: TextView

    private var callbackManager: CallbackManager? = null

    override fun getContentFragmentLayout(): Int? {
        return R.layout.facebook_signin_fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callbackManager = CallbackManager.Factory.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        status = findView(R.id.status)

        val signInButton: LoginButton = findView(R.id.signIn)
        signInButton.fragment = this
        signInButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                setLoggedStatus()
                Snackbar.make(findView(R.id.container), "onSignIn", Snackbar.LENGTH_SHORT).show()
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                status.text = exception.toString()
                Snackbar.make(findView(R.id.container), "onError", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun setLoggedStatus() {
        val builder = StringBuilder()
        builder.append("Name: ")
        builder.append(Profile.getCurrentProfile().name)
        builder.append("\nId: ")
        builder.append(Profile.getCurrentProfile().id)
        builder.append("\nAccess Token: ")
        builder.append(AccessToken.getCurrentAccessToken())
        status.text = builder.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }

    override fun getDefaultLoading(): FragmentLoading? {
        return NonBlockingLoading()
    }
}
