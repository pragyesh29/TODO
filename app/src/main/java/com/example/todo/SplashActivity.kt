package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//         If by default action bar of activity is present then hide it
        supportActionBar?.hide()

        intent = Intent(this, MainActivity::class.java)
        Handler(Looper.getMainLooper()).postDelayed({
//             Move to MainActivity after 1s elapsed
            startActivity(intent)
//             Then finish this SplashActivity since we'll not come back to this.
            finish()
        },1000)
    }
}