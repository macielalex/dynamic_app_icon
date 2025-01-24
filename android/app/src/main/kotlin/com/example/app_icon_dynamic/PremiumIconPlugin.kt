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
                "updateToLemonIcon" -> {
                    updateToLemonIcon()
                    result.success(true)
                }
                "updateToOrangeIcon" -> {
                    updateToOrangeIcon()
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

    private fun updateToLemonIcon() {
        changeIcon("LemonActivity", "MainActivity")
    }

    private fun updateToOrangeIcon() {
        changeIcon("OrangeActivity", "MainActivity")
    }

    private fun changeIcon(newActivity: String, oldActivity: String) {
        val packageManager = context.packageManager
        val activities = listOf("PremiumActivity", "LemonActivity", "OrangeActivity", "MainActivity")

        // Habilitar o novo alias específico primeiro
        val newComponentName = ComponentName(context, "$packageName.$newActivity")
        packageManager.setComponentEnabledSetting(
                newComponentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        )
        val iconUpdatedIntent = Intent("com.example.app_icon_dynamic.ICON_UPDATED")
        context.sendBroadcast(iconUpdatedIntent)

        // Desabilitar todos os aliases antigos em seguida
        activities.forEach { activity ->
            if (activity != newActivity) { // Evitar desabilitar o novo alias
                val componentName = ComponentName(context, "$packageName.$activity")
                packageManager.setComponentEnabledSetting(
                        componentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                )
            }
        }

        // Garantir que a reinicialização ocorra após a troca, com tempo suficiente
        Handler(context.mainLooper).postDelayed({
            restartApp()
        }, 10000) // Aumentar o tempo de atraso
    }

    private fun restartApp() {
        try {
            val intent = Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK) // Removido FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            android.util.Log.e("PremiumIconPlugin", "Erro ao reiniciar o app: ${e.message}")
        }
    }






}
