package com.example.dev00.myall.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.example.dev00.myall.R
import com.example.dev00.myall.helpers.Constants.Companion.RC_SIGN_IN
import com.example.dev00.myall.models.User
import com.example.dev00.myall.utils.Utils
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Author: HuynhHQ
 * A login screen that offers login via google account or create a account
 */
class LoginActivity : FragmentActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var mAuthListerner: FirebaseAuth.AuthStateListener
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleApiClient: GoogleApiClient
    var context = this
    val TAG: String = "LoginActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupVariable()
        setupAction()
    }

    private fun setupVariable() {
        //Disk persistence
        //enable disk persistence that automatically stores
        // the data offline in case of no internet connection
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        firebaseAuth = FirebaseAuth.getInstance()
        mAuthListerner = FirebaseAuth.AuthStateListener {
            if (firebaseAuth.currentUser != null) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }
        }
        mDatabase = FirebaseDatabase.getInstance().reference
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(this@LoginActivity)
                .enableAutoManage(this@LoginActivity) {
                    Utils.createToast(this, "We got error")
                }
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    private fun setupAction() {
        sign_in_button.setOnClickListener {
            signIn()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Utils.writeLog(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Utils.writeLog(TAG, "signInWithCredential:success")
                        var currentUser = User();
//                        currentUser.Email = acct.email
                    } else {
                        // If sign in fails, display a message to the user.
                        Utils.createToast(this, "Could not log in!!!")
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                    }
                }
    }

    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(mAuthListerner)
    }
//    private fun validInfomation(): ValidationRespond {
//        var result = ValidationRespond()
//        // Reset errors.
//        email.error = null
//        txt_password.error = null
//
//        // Store values at the time of the login attempt.
//        val emailStr = email.text.toString()
//        val passwordStr = txt_password.text.toString()
//
//        var cancel = false
//
//
//        // Check for a valid email address.
//        if (TextUtils.isEmpty(emailStr)) {
//            email.error = getString(R.string.error_field_required)
//            result.FocusView = email
//            result.IsCancel = true
//            cancel = true
//        } else if (!isEmailValid(emailStr)) {
//            email.error = getString(R.string.error_invalid_email)
//            result.FocusView = email
//            result.IsCancel = true
//            cancel = true
//        }
//
//        if (TextUtils.isEmpty(passwordStr) && !cancel) {
//            txt_password.error = getString(R.string.error_field_password_required)
//            result.FocusView = txt_password
//            result.IsCancel = true
//        } else if (!isPasswordValid(passwordStr) && !cancel) {
//            txt_password.error = getString(R.string.error_invalid_password)
//            result.FocusView = txt_password
//            result.IsCancel = true
//        }
//
//        return result
//    }


}
