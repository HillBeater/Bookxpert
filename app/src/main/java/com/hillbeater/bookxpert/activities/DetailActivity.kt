package com.hillbeater.bookxpert.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hillbeater.bookxpert.R
import com.hillbeater.bookxpert.databinding.ActivityDetailBinding
import com.hillbeater.bookxpert.fragments.ApiDataFragment
import com.hillbeater.bookxpert.fragments.ImageFragment
import com.hillbeater.bookxpert.fragments.PdfFragment
import com.hillbeater.bookxpert.fragments.SettingsFragment

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        val menuId = intent.getIntExtra("menuId", -1)

        val fragment = when (menuId) {
            1 -> PdfFragment()
            2 -> ImageFragment()
            3 -> ApiDataFragment()
            4 -> SettingsFragment()
            else -> {PdfFragment()}
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()

    }
}