package com.example.dev00.myall.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

class Utils {

    companion object {
        /**
         * HuynhHQ
         * Valid email with pattern
         */
        @JvmStatic
        fun checkEmail(emailStr: String): Boolean {
            return Pattern.compile(
                    "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
            ).matcher(emailStr).matches()
        }

        /**
         * HuynhHQ
         * Create Toast
         */
        @JvmStatic
        fun createToast(context: Context
                        , message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        /**
         * HuynhHQ
         * Write Log
         */
        @JvmStatic
        fun writeLog(tag: String?, msg: String?) = Log.d(tag, msg)

        @JvmStatic
        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
        fun showProgressWithForm(show: Boolean
                             , progressBar: ProgressBar
                             , hideForm: ScrollView
                             , resources: Resources) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

                progressBar.visibility = if (show) View.GONE else View.VISIBLE
                hideForm.animate()
                        .setDuration(shortAnimTime)
                        .alpha((if (show) 0 else 1).toFloat())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                hideForm.visibility = if (show) View.GONE else View.VISIBLE
                            }
                        })

                progressBar.visibility = if (show) View.VISIBLE else View.GONE
                progressBar.animate()
                        .setDuration(shortAnimTime)
                        .alpha((if (show) 1 else 0).toFloat())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                progressBar.visibility = if (show) View.VISIBLE else View.GONE
                            }
                        })
            } else {
                // The ViewPropertyAnimator APIs are not available, so simply show
                // and hide the relevant UI components.
                progressBar.visibility = if (show) View.VISIBLE else View.GONE
                hideForm.visibility = if (show) View.GONE else View.VISIBLE
            }
        }

    }
}