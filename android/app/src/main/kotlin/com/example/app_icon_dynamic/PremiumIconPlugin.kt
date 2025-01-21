package com.example.app_icon_dynamic

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel

class PremiumIconPlugin(private val context: Context) {

    private lateinit var channel: MethodChannel
    private val packageName = context.packageName

    fun initWith(binaryMessenger: BinaryMessenger) {
        channel = MethodChannel(binaryMessenger, "premium_icon")
        channel.setMethodCallHandler { call, result ->
            when (call.method) {
                "updateToPremiumIcon" -> {
                    updateToPremiumIcon()
                    result.success(true)
                }
                "updateToDefaultIcon" -> {
                    updateToDefaultIcon()
                    result.success(true)
                }
                else -> result.notImplemented()
            }
        }
    }

    private fun updateToPremiumIcon() {
        changeIcon("PremiumActivity", "MainActivity")
    }

    private fun updateToDefaultIcon() {
        changeIcon("MainActivity", "PremiumActivity")
    }

    private fun changeIcon(newActivity: String, oldActivity: String) {
        val packageManager = context.packageManager
        val newComponentName = ComponentName(context, "$packageName.$newActivity")
        val oldComponentName = ComponentName(context, "$packageName.$oldActivity")

        // Enable the new activity alias
        packageManager.setComponentEnabledSetting(
                newComponentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        )

        // Disable the old activity alias
        packageManager.setComponentEnabledSetting(
                oldComponentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
        )
    }
}
