package com.example.app_icon_dynamic

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
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
                "saveSelectedIcon" -> {
                    val iconName = call.argument<String>("iconName")
                    if (iconName != null) {
                        saveSelectedIcon(iconName)
                        result.success(true)
                    } else {
                        result.error("INVALID_ARGUMENT", "Ícone inválido", null)
                    }
                }
                "applySavedIcon" -> { // Novo caso para aplicar o ícone salvo
                    applySavedIcon()
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
        val newComponentName = ComponentName(context, "$packageName.$newActivity")

        try {
            // Ativar o novo alias
            packageManager.setComponentEnabledSetting(
                    newComponentName,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
            )
            android.util.Log.d("PremiumIconPlugin", "Novo alias habilitado: $newActivity")

            // Desativar outros aliases
            val aliases = listOf("PremiumActivity", "LemonActivity", "OrangeActivity", "MainActivity")
            aliases.forEach { alias ->
                if (alias != newActivity) {
                    val componentName = ComponentName(context, "$packageName.$alias")
                    packageManager.setComponentEnabledSetting(
                            componentName,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                    )
                    android.util.Log.d("PremiumIconPlugin", "Alias desabilitado: $alias")
                }
            }

            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.sendBroadcast(intent)
            Log.d("AppLifecycleObserver", "Notificação enviada ao launcher para atualização do ícone.")

        } catch (e: Exception) {
            android.util.Log.e("PremiumIconPlugin", "Erro ao alterar o ícone: ${e.message}")
        }
    }

    private fun saveSelectedIcon(iconName: String) {
        val sharedPreferences = context.getSharedPreferences("IconPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("selected_icon", iconName).apply()
        android.util.Log.d("PremiumIconPlugin", "Ícone salvo: $iconName")
    }

    private fun applySavedIcon() {
        val sharedPreferences = context.getSharedPreferences("IconPrefs", Context.MODE_PRIVATE)
        val selectedIcon = sharedPreferences.getString("selected_icon", "MainActivity")
        if (selectedIcon != null) {
            changeIcon(selectedIcon, "MainActivity")
            android.util.Log.d("PremiumIconPlugin", "Ícone aplicado: $selectedIcon")
        } else {
            android.util.Log.e("PremiumIconPlugin", "Nenhum ícone selecionado encontrado para aplicar.")
        }
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
