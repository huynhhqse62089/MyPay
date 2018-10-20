package com.example.dev00.myall.utils

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
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
        fun createToast(context: Context, message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        /**
         * HuynhHQ
         * Write Log
         */
        fun writeLog(tag: String?, msg: String?) = Log.d(tag, msg)


    }
}