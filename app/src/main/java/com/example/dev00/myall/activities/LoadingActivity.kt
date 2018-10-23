package com.example.dev00.myall.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.dev00.myall.R
import com.example.dev00.myall.utils.Utils
import kotlinx.android.synthetic.main.activity_loading.*
import java.lang.Exception

class LoadingActivity : AppCompatActivity() {

    private var tag = "LoadingActivity"
    private var animation: Animation? = null
    private var mHandler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            var intent = Intent()
            intent.setClass(this@LoadingActivity, LoginActivity::class.java)
            startActivity(intent)
            this@LoadingActivity.finish()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_loading)
            var window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.md_grey_100))
            animation = AnimationUtils.loadAnimation(this, R.anim.from_bottom)
            ivDown.startAnimation(animation)
            animation = AnimationUtils.loadAnimation(this, R.anim.from_top)
            ivUp.startAnimation(animation)
            mHandler.sendEmptyMessageDelayed(1, 3500)
        } catch (e: Exception) {
            Utils.writeLog(tag, e.message)
        }

    }

}
