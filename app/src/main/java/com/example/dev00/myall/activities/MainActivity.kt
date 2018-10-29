package com.example.dev00.myall.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.dev00.myall.R
import com.example.dev00.myall.models.GoogleApiClient_Singleton
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.example.dev00.myall.utils.Utils
import com.google.android.gms.common.api.GoogleApiClient
import android.support.annotation.NonNull
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG: String = "MainActivity"
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    private lateinit var mGoogleApiClient: GoogleApiClient
    private var doubleBackToExitPressedOnce: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupVariable()
        setupFirebaseListener()
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
//        if (backPressedTime + 2000 > System.currentTimeMillis()) {
//            super.onBackPressed()
//            return;
//        } else {
//
//        }
//        backPressedTime = System.currentTimeMillis()

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finish()
            moveTaskToBack(true)
            System.exit(0)
        } else {
            doubleBackToExitPressedOnce = true
            Utils.createToast(this@MainActivity, "Nhấn Back thêm lần nữa để thoát")
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_main_screen -> {

            }

            R.id.nav_fast_recharge_card -> {

            }

            R.id.nav_logout -> {
                logout()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        mGoogleApiClient.connect()
        mGoogleApiClient.registerConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
            override fun onConnected(bundle: Bundle?) {

                FirebaseAuth.getInstance().signOut()
                if (mGoogleApiClient.isConnected) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback { status ->
                        if (status.isSuccess()) {
                            Log.d(TAG, "User Logged out")
                            val intent = Intent(this@MainActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }

            override fun onConnectionSuspended(i: Int) {
                Log.d(TAG, "Google API Client Connection Suspended")
            }
        })
    }

    private fun setupFirebaseListener() {
        Log.d(TAG, "setupFirebaseListener: setting up auth state listener.")
        mAuthStateListener = FirebaseAuth.AuthStateListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                Utils.writeLog(TAG, "onAuthStateChanged: signed_in: " + user.uid)
            } else {
                Log.d(TAG, "onAuthStateChanged: signed_out")
                intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener)
        }
    }

    private fun setupVariable() {
        mGoogleApiClient = GoogleApiClient_Singleton.getInstance().getGoogleApiClient()!!
    }

}
