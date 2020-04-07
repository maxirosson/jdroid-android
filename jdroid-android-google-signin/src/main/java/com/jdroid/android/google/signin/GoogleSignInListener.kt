package com.jdroid.android.google.signin

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface GoogleSignInListener {

    fun onGoogleSignIn(googleSignInAccount: GoogleSignInAccount)

    fun onGoogleSignOut()

    fun onGoogleAccessRevoked()
}
