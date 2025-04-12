package com.hillbeater.bookxpert.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hillbeater.bookxpert.database.AppDatabase
import com.hillbeater.bookxpert.databinding.ActivitySplashBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)

        checkUserInfoAndNavigate()
    }

    private fun checkUserInfoAndNavigate() {
        lifecycleScope.launch {
            val userInfo = withContext(Dispatchers.IO) {
                db.userInfoDao().getUserInfo()
            }

            val nextActivity = if (userInfo != null) {
                HomeActivity::class.java
            } else {
                AuthenticationActivity::class.java
            }

            startActivity(Intent(this@SplashActivity, nextActivity))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}
