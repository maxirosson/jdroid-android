package com.jdroid.android.google.signin

import android.content.Intent
import androidx.annotation.CallSuper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.java.utils.LoggerUtils
import com.jdroid.java.utils.RandomUtils

open class GoogleSignInHelper(protected val abstractFragment: AbstractFragment, private var googleSignInListener: GoogleSignInListener?) {

    companion object {
        private val LOGGER = LoggerUtils.getLogger(GoogleSignInHelper::class.java)
        private val SIGN_IN_REQUEST_CODE = RandomUtils.get16BitsInt()
    }

    private val googleSignInClient: GoogleSignInClient

    init {
        googleSignInClient = GoogleSignIn.getClient(abstractFragment.requireContext(), getGoogleSignInOptionsBuilder().build())
    }

    protected fun getGoogleSignInOptionsBuilder(): GoogleSignInOptions.Builder {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val builder = GoogleSignInOptions.Builder(getGoogleSignInOptions())
        builder.requestEmail()
        if (isRequestIdTokenEnabled()) {
            builder.requestIdToken(AbstractApplication.get().appContext.serverClientId)
        }
        if (isServerAuthCodeEnabled()) {
            builder.requestServerAuthCode(AbstractApplication.get().appContext.serverClientId, false)
        }
        return builder
    }

    protected open fun getGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.DEFAULT_SIGN_IN
    }

    protected open fun isRequestIdTokenEnabled(): Boolean {
        return false
    }

    protected open fun isServerAuthCodeEnabled(): Boolean {
        return false
    }

    fun getLastSignedInAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(abstractFragment.requireContext())
    }

    fun verifyLastSignedInAccount() {
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(abstractFragment.requireContext())

        if (googleSignInAccount != null) {
            onGoogleSignIn(googleSignInAccount)
        } else {
            onGoogleSignOut()
        }
    }

    fun silentSignIn() {
        // Attempt to silently refresh the GoogleSignInAccount. If the GoogleSignInAccount
        // already has a valid token this method may complete immediately.
        //
        // If the user has not previously signed in on this device or the sign-in has expired,
        // this asynchronous branch will attempt to sign in the user silently and get a valid
        // ID token. Cross-device single sign on will occur in this branch.
        val task = googleSignInClient.silentSignIn()
        if (task.isSuccessful) {
            // There's immediate result available.
            handleSignInResult(task)
        } else {
            showLoading()
            task.addOnCompleteListener(abstractFragment.requireActivity()) {
                handleSignInResult(it)
                dismissLoading()
            }
        }
    }

    protected fun showLoading() {
        abstractFragment.showLoading()
    }

    protected fun dismissLoading() {
        abstractFragment.dismissLoading()
    }

    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        ActivityLauncher.startActivityForResult(abstractFragment.activity, signInIntent, SIGN_IN_REQUEST_CODE)
    }

    fun signOut() {
        googleSignInClient.signOut().addOnCompleteListener(abstractFragment.requireActivity()) {
            LOGGER.debug("SignOut")
            onGoogleSignOut()
        }
    }

    fun revokeAccess() {
        googleSignInClient.revokeAccess().addOnCompleteListener(abstractFragment.requireActivity()) {
            LOGGER.debug("RevokeAccess")
            onGoogleAccessRevoked()
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            // The Task returned from this call is always completed, no need to attach a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)!!
            if (isRequestIdTokenEnabled()) {
                if (!isTokenValid(account.idToken)) {
                    revokeAccess()
                    return
                }
            }

            LOGGER.debug("SignIn valid")
            onGoogleSignIn(account)
        } catch (e: ApiException) {
            AbstractApplication.get().exceptionHandler.logHandledException(e)
            onGoogleSignOut()
        }
    }

    @CallSuper
    protected open fun onGoogleSignIn(account: GoogleSignInAccount) {
        googleSignInListener?.onGoogleSignIn(account)
    }

    @CallSuper
    protected open fun onGoogleSignOut() {
        googleSignInListener?.onGoogleSignOut()
    }

    @CallSuper
    protected open fun onGoogleAccessRevoked() {
        googleSignInListener?.onGoogleAccessRevoked()
    }

    protected fun isTokenValid(idToken: String?): Boolean {
        // https://developers.google.com/identity/sign-in/android/backend-auth
        return true
    }

    fun setGoogleSignInListener(googleSignInListener: GoogleSignInListener) {
        this.googleSignInListener = googleSignInListener
    }
}
