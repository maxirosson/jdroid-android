package com.jdroid.android.sample.ui.google.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.material.snackbar.Snackbar
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.google.signin.GoogleSignInHelper
import com.jdroid.android.google.signin.GoogleSignInListener
import com.jdroid.android.loading.FragmentLoading
import com.jdroid.android.loading.NonBlockingLoading
import com.jdroid.android.sample.R

class GoogleSignInFragment : AbstractFragment(), GoogleSignInListener {

    private lateinit var googleSignInHelper: GoogleSignInHelper

    private lateinit var signInButton: SignInButton
    private lateinit var signOutButton: View
    private lateinit var revokeButton: View
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getContentFragmentLayout(): Int? {
        return R.layout.google_sign_in_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        status = findView(R.id.status)

        signInButton = findView(R.id.signIn)
        signInButton.setSize(SignInButton.SIZE_WIDE)
        signInButton.setOnClickListener { googleSignInHelper.signIn() }

        signOutButton = findView(R.id.signOut)
        signOutButton.setOnClickListener { googleSignInHelper.signOut() }

        revokeButton = findView(R.id.revoke)
        revokeButton.setOnClickListener { googleSignInHelper.revokeAccess() }

        googleSignInHelper = object : GoogleSignInHelper(this@GoogleSignInFragment, this@GoogleSignInFragment) {
            override fun isRequestIdTokenEnabled(): Boolean {
                return true
            }

            override fun isServerAuthCodeEnabled(): Boolean {
                return true
            }
        }
    }

    override fun onStart() {
        super.onStart()

        googleSignInHelper.verifyLastSignedInAccount()

        // TODO
        // googleSignInHelper.silentSignIn();
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googleSignInHelper.onActivityResult(requestCode, resultCode, data!!)
    }

    override fun onGoogleSignIn(googleSignInAccount: GoogleSignInAccount) {
        val builder = StringBuilder()
        builder.append("Display Name: ")
        builder.append(googleSignInAccount.displayName)
        builder.append("\nEmail: ")
        builder.append(googleSignInAccount.email)
        builder.append("\nGranted Scopes: ")
        builder.append(googleSignInAccount.grantedScopes)
        builder.append("\nId: ")
        builder.append(googleSignInAccount.id)
        builder.append("\nId Token: ")
        builder.append(if (googleSignInAccount.idToken != null) googleSignInAccount.idToken!!.substring(0, 50) else null)
        builder.append("\nServer Auth Code: ")
        builder.append(googleSignInAccount.serverAuthCode)
        status.text = builder.toString()

        signInButton.visibility = View.GONE
        signOutButton.visibility = View.VISIBLE

        Snackbar.make(findView(R.id.container), "onGoogleSignIn", Snackbar.LENGTH_SHORT).show()
    }

    override fun onGoogleSignOut() {
        status.setText(R.string.notLogged)

        signInButton.visibility = View.VISIBLE
        signOutButton.visibility = View.GONE

        Snackbar.make(findView(R.id.container), "onGoogleSignOut", Snackbar.LENGTH_SHORT).show()
    }

    override fun onGoogleAccessRevoked() {
        status.setText(R.string.notLogged)

        signInButton.visibility = View.VISIBLE
        signOutButton.visibility = View.GONE

        Snackbar.make(findView(R.id.container), "onGoogleAccessRevoked", Snackbar.LENGTH_SHORT).show()
    }

    override fun getDefaultLoading(): FragmentLoading? {
        return NonBlockingLoading()
    }
}
