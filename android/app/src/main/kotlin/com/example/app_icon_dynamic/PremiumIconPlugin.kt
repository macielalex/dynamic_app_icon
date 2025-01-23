package com.example.app_icon_dynamic

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
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

        android.util.Log.d("PremiumIconPlugin", "Disabling old alias: $oldActivity")
        android.util.Log.d("PremiumIconPlugin", "Enabling new alias: $newActivity")

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

        Handler(context.mainLooper).postDelayed({
            restartApp()
        }, 2000)

    }
//    private fun restartApp() {
//        try {
//            // Crie um Intent explícito para MainActivity
//            val intent = Intent(context, MainActivity::class.java).apply {
//                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            }
//
//            // Inicie a atividade principal
//            context.startActivity(intent)
//
//            // Finalize o processo atual para garantir que ele seja reiniciado
//            Runtime.getRuntime().exit(0)
//        } catch (e: Exception) {
//            android.util.Log.e("PremiumIconPlugin", "Error restarting app: ${e.message}")
//        }
//    }

    private fun restartApp() {
        try {
            val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            context.startActivity(intent)

            // Finaliza o processo atual sem usar Runtime.exit
            android.os.Process.killProcess(android.os.Process.myPid())
        } catch (e: Exception) {
            android.util.Log.e("PremiumIconPlugin", "Error restarting app: ${e.message}")
        }
    }



}
