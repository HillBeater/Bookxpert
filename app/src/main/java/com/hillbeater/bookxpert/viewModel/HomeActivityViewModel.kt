package com.hillbeater.bookxpert.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hillbeater.bookxpert.database.AppDatabase
import com.hillbeater.bookxpert.database.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val userInfoDao = AppDatabase.getDatabase(application).userInfoDao()

    private val _user = MutableLiveData<UserInfo?>()
    val user: LiveData<UserInfo?> = _user

    private val _logoutComplete = MutableLiveData<Boolean>()
    val logoutComplete: LiveData<Boolean> = _logoutComplete

    fun fetchUser() {
        viewModelScope.launch {
            val userData = withContext(Dispatchers.IO) {
                userInfoDao.getUserInfo()
            }
            _user.value = userData
        }
    }

    fun clearUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            userInfoDao.clearAllData()
            _logoutComplete.postValue(true)
        }
    }

    fun addTokenInDb(token: String?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userInfoDao.updateFcmToken(token)
            }
        }

    }
}