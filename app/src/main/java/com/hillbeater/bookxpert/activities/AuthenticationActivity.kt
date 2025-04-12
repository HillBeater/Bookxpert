package com.hillbeater.bookxpert.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.hillbeater.bookxpert.R
import com.hillbeater.bookxpert.databinding.ActivityAuthenticationBinding
import com.hillbeater.bookxpert.viewModel.GoogleViewModel
import kotlinx.coroutines.flow.collectLatest

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val googleViewModel: GoogleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGoogleSignIn()
        observeViewModel()

        binding.signInBtn.setOnClickListener {
            val signInIntent: Intent = googleSignInClient.signInIntent
            resultLauncher.launch(signInIntent)
        }
    }

    private fun setupGoogleSignIn() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            binding.progressBar.visibility = View.VISIBLE
            googleViewModel.handleSignInResult(result.data)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            googleViewModel.signInSuccess.collectLatest { account ->
                account?.let {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@AuthenticationActivity, "Sign-In Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@AuthenticationActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            googleViewModel.signInError.collectLatest { error ->
                error?.let {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@AuthenticationActivity, it, Toast.LENGTH_SHORT).show()
                    googleViewModel.clearState()
                }
            }
        }
    }
}
