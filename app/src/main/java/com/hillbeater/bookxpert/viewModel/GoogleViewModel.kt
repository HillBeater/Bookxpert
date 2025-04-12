package com.hillbeater.bookxpert.viewModel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.hillbeater.bookxpert.database.AppDatabase
import com.hillbeater.bookxpert.database.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GoogleViewModel(application: Application) : AndroidViewModel(application) {

    private val _signInSuccess = MutableStateFlow<GoogleSignInAccount?>(null)
    val signInSuccess: StateFlow<GoogleSignInAccount?> = _signInSuccess

    private val _signInError = MutableStateFlow<String?>(null)
    val signInError: StateFlow<String?> = _signInError

    fun handleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        viewModelScope.launch {
            try {
                val account = task.await()
                saveUserToDB(account)
                _signInSuccess.value = account
            } catch (e: Exception) {
                _signInError.value = "Google sign-in failed: ${e.localizedMessage}"
            }
        }
    }

    private fun saveUserToDB(account: GoogleSignInAccount) {
        viewModelScope.launch {
            val userInfo = UserInfo(
                userName = account.displayName ?: "Unknown",
                userEmail = account.email ?: "",
                userProfileUrl = account.photoUrl?.toString() ?: ""
            )
            AppDatabase.getDatabase(getApplication()).userInfoDao().insertUserInfo(userInfo)
        }
    }

    fun clearState() {
        _signInSuccess.value = null
        _signInError.value = null
    }
}
