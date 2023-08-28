package com.desrielkiki.consignmentapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.desrielkiki.consignmentapp.R
import com.desrielkiki.consignmentapp.activity.sharedPreference.UserPreference

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        val textView = findViewById<TextView>(R.id.tv_appName)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.from_left_splash)
        textView.startAnimation(slideAnimation)

        Handler().postDelayed({
            startActivity(Intent(this, UserPreference::class.java))
            finish()
        }, SPLASH_DELAY)
    }

    companion object {
        private const val SPLASH_DELAY: Long = 3000 // Durasi splash screen dalam milidetik
    }
}