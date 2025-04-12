package com.hillbeater.bookxpert.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.messaging.FirebaseMessaging
import com.hillbeater.bookxpert.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDarkMode()
        setPushNotification()
    }

    private fun setDarkMode() {
        val isDarkMode = (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        binding.switchDarkMode.isChecked = isDarkMode
        updateDarkModeDescription(isDarkMode)

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            val mode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)

            updateDarkModeDescription(isChecked)
        }
    }

    private fun updateDarkModeDescription(isDark: Boolean) {
        binding.txtDarkModeDescription.text = if (isDark) {
            "Dark mode is now enabled for a darker UI experience."
        } else {
            "Dark mode is turned off. UI will use light theme."
        }
    }

    private fun setPushNotification() {
        val context = requireContext()
        val sharedPref = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)

        val areNotificationsEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
        val isUserEnabled = sharedPref.getBoolean("is_notification_enabled", true)

        binding.switchNotifications.isChecked = areNotificationsEnabled && isUserEnabled
        updatePushNotificationDescription(binding.switchNotifications.isChecked)

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->

            with(sharedPref.edit()) {
                putBoolean("is_notification_enabled", isChecked)
                apply()
            }

            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
                        return@setOnCheckedChangeListener
                    }
                }

                FirebaseMessaging.getInstance().subscribeToTopic("all")
                updatePushNotificationDescription(true)
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("all")
                updatePushNotificationDescription(false)
            }
        }
    }

    private fun updatePushNotificationDescription(enabled: Boolean) {
        binding.txtNotificationsDescription.text = if (enabled) {
            "Push notifications are enabled. You’ll receive updates."
        } else {
            "Push notifications are turned off. You won’t receive any alerts."
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
