package com.hillbeater.bookxpert.viewModel

import androidx.lifecycle.*
import com.hillbeater.bookxpert.database.AppDatabase
import com.hillbeater.bookxpert.database.PhoneDataItem
import com.hillbeater.bookxpert.database.PhoneDatabase
import com.hillbeater.bookxpert.instance.NotificationAPI
import com.hillbeater.bookxpert.instance.RetrofitClient
import com.hillbeater.bookxpert.model.FCMRequest
import com.hillbeater.bookxpert.model.Message
import com.hillbeater.bookxpert.utils.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.*
import java.io.IOException

class ApiDataFragmentViewModel(
    private val phoneDatabase: PhoneDatabase,
    private val userDatabase: AppDatabase
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _phoneData = MutableLiveData<List<PhoneDataItem>>()
    val phoneData: LiveData<List<PhoneDataItem>> get() = _phoneData

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun fetchDataFromApi() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = withContext(Dispatchers.IO) { RetrofitClient.api.getPhones() }

                if (response.isNotEmpty()) {
                    val phoneEntities = response.map { it.toEntity() }

                    withContext(Dispatchers.IO) {
                        phoneDatabase.phoneDao().deleteAll()
                        phoneDatabase.phoneDao().insertAll(phoneEntities)
                    }

                    _toastMessage.value = "Data fetched and stored successfully"
                    loadFromDatabase()
                } else {
                    _toastMessage.value = "No phone data found"
                    _loading.value = false
                }
            } catch (e: IOException) {
                _toastMessage.value = "Network error: ${e.localizedMessage}"
                _loading.value = false
            } catch (e: Exception) {
                _toastMessage.value = "Unexpected error: ${e.localizedMessage}"
                _loading.value = false
            }
        }
    }

    private fun loadFromDatabase() {
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                phoneDatabase.phoneDao().getAllItems()
            }
            _phoneData.value = data
            _loading.value = false
        }
    }

    fun deletePhone(phoneItem: PhoneDataItem) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    phoneDatabase.phoneDao().deleteItem(phoneItem)
                }
                _toastMessage.value = "Deleted successfully"
                loadFromDatabase()
                sendPushNotification(phoneItem)

            } catch (e: Exception) {
                _toastMessage.value = "Error deleting: ${e.localizedMessage}"
            }
        }
    }

    private suspend fun sendPushNotification(phoneItem: PhoneDataItem) {
        val userInfo = userDatabase.userInfoDao().getUserInfo()
        val deviceToken = userInfo?.fcmToken

        val message = Message(
            token = deviceToken.toString(),
            data = mapOf(
                "title" to phoneItem.name,
                "body" to "This item is deleted"
            )
        )


        val fcmRequest = FCMRequest(message = message)

        NotificationAPI.notificationService.sendNotification(fcmRequest).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        println("Notification sent successfully!")
                    } else {
                        println("Failed to send notification: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("Error: ${t.message}")
                }
            }
        )
    }

    fun updatePhoneItem(updatedItem: PhoneDataItem) {
        viewModelScope.launch {
            phoneDatabase.phoneDao().updateItem(updatedItem)
            loadFromDatabase()
        }
    }
}

class ApiDataViewModelFactory(
    private val db: PhoneDatabase,
    private val userDatabase: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ApiDataFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            ApiDataFragmentViewModel(db,userDatabase) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
