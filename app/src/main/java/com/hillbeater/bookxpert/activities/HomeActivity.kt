package com.hillbeater.bookxpert.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.hillbeater.bookxpert.R
import com.hillbeater.bookxpert.adapter.ItemAdapter
import com.hillbeater.bookxpert.databinding.ActivityHomeBinding
import com.hillbeater.bookxpert.viewModel.HomeActivityViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var viewModel: HomeActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestNotificationPermission()

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        FirebaseMessaging.getInstance().subscribeToTopic("all")

        getFCMToken()

        viewModel = ViewModelProvider(this)[HomeActivityViewModel::class.java]

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        observeViewModel()
        viewModel.fetchUser()
        setupRecyclerView()
        setupLogout()
    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                viewModel.addTokenInDb(token)
            } else {
                Log.w("FCM_TOKEN", "Fetching FCM token failed", task.exception)
            }
        }
    }

    private fun requestNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }

    private fun observeViewModel() {
        viewModel.user.observe(this) { user ->
            val userName = user?.userName ?: "Guest"
            binding.welcomeText.text = getString(R.string.welcome_user, userName)
        }

        viewModel.logoutComplete.observe(this) { isLoggedOut ->
            if (isLoggedOut) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AuthenticationActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setupLogout() {
        binding.logoutIcon.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setTitle("Confirm Logout")
                .setMessage("Are you sure you want to logout? All local data will be deleted.")
                .setPositiveButton("Logout") { dialog, _ ->
                    binding.progressBar.visibility = View.VISIBLE
                    mGoogleSignInClient.signOut().addOnCompleteListener {
                        viewModel.clearUserData()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .create()

            dialog.show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.googleBlue))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.black))
        }

    }

    private fun setupRecyclerView() {
        itemAdapter = ItemAdapter { menuItem ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("menuId", menuItem.id)
            startActivity(intent)
        }

        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = itemAdapter
        }
    }
}

