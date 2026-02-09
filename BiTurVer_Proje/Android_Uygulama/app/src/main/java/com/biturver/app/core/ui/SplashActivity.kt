package com.biturver.app.core.ui

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.biturver.app.core.ui.MainActivity
import com.biturver.app.R
import com.biturver.app.feature.auth.presentation.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val mode = prefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(mode)

        setContentView(R.layout.activity_splash)

        val deepLink: Uri? = intent?.data
        val pb = findViewById<ProgressBar>(R.id.pbLoading)

        ValueAnimator.ofInt(0, 100).apply {
            duration = 900
            interpolator = DecelerateInterpolator()
            addUpdateListener { pb.progress = it.animatedValue as Int }
            start()
            doOnEnd {

                val loggedIn = getSharedPreferences("auth", MODE_PRIVATE)
                    .getBoolean("logged_in", false)

                val next = if (loggedIn) MainActivity::class.java else LoginActivity::class.java

                val i = Intent(this@SplashActivity, next).apply { data = deepLink }
                startActivity(i)
                finish()
            }
        }
    }
}


private fun ValueAnimator.doOnEnd(block: () -> Unit) {
    addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationEnd(animation: Animator) = block()
        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })
}