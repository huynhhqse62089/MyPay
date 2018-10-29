package com.example.dev00.myall.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.example.dev00.myall.R
import com.example.dev00.myall.helpers.Constants.Companion.RC_SIGN_IN
import com.example.dev00.myall.models.GoogleApiClient_Singleton
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

    val TAG: String = "LoginActivity"
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var mAuthListerner: FirebaseAuth.AuthStateListener
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleApiClient: GoogleApiClient
    private var doubleBackToExitPressedOnce: Boolean = false

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
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

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
        GoogleApiClient_Singleton.getInstance().setGoogleApiClient(mGoogleApiClient)
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

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finish()
            moveTaskToBack(true)
            System.exit(0)
        } else {
            doubleBackToExitPressedOnce = true
            Utils.createToast(this@LoginActivity, "Nhấn Back thêm lần nữa để thoát")
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }

    private fun saveUser(email: String, phone: String?, type: Int){

    }

}
