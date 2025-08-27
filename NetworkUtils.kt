package com.practice.ui.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object NetworkUtils {

    private val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    fun registerNetworkCallback(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _isConnected.value = true
            }

            override fun onLost(network: Network) {
                _isConnected.value = false
            }
        }

        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        val activeNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(activeNetwork)
        _isConnected.value = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}
