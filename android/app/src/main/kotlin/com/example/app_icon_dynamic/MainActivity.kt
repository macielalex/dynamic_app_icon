package com.example.app_icon_dynamic

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {

    private val CHANNEL = "com.example.app_icon_dynamic/icon_changer"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Iniciar o serviço para detectar fechamento do app por arrasto
        val serviceIntent = Intent(this, AppCloseService::class.java)
        startService(serviceIntent)
        Log.d("MainActivity", "Serviço AppCloseService iniciado.")
    }
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // Configurar o MethodChannel
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "saveIconPreference" -> {
                    val iconName = call.argument<String>("iconName")
                    if (iconName != null) {
                        saveIconPreference(iconName)
                        result.success("Preferência de ícone salva: $iconName")
                    } else {
                        result.error("INVALID_ARGUMENT", "Icon name is null", null)
                    }
                }
                "applySavedIcon" -> {
                    applySavedIcon()
                    result.success("Ícone salvo aplicado com sucesso.")
                }
                "updateIcon" -> {
                    val iconName = call.argument<String>("iconName")
                    if (iconName != null) {
                        changeIcon(iconName)
                        result.success("Ícone alterado para: $iconName")
                    } else {
                        result.error("INVALID_ARGUMENT", "Icon name is null", null)
                    }
                }
                else -> result.notImplemented()
            }
        }
    }

    private fun saveIconPreference(iconName: String) {
        val sharedPreferences = this.getSharedPreferences("IconPrefs", MODE_PRIVATE)
        sharedPreferences.edit().putString("selected_icon", iconName).apply()
        Log.d("MainActivity", "Ícone preferido salvo: $iconName")
    }

    private fun applySavedIcon() {
        val sharedPreferences = getSharedPreferences("IconPrefs", MODE_PRIVATE)
        val selectedIcon = sharedPreferences.getString("selected_icon", "MainActivity")

        if (selectedIcon != null) {
            changeIcon(selectedIcon)
            Log.d("MainActivity", "Ícone salvo aplicado: $selectedIcon")
        } else {
            Log.e("MainActivity", "Nenhum ícone salvo encontrado.")
        }
    }


    private fun changeIcon(newActivity: String) {
        val packageManager = this.packageManager
        val newComponentName = ComponentName(this, "$packageName.$newActivity")

        // Lista de aliases definidos
        val aliases = listOf("PremiumActivity", "LemonActivity", "OrangeActivity", "MainActivity")

        try {
            // Ativar o alias correto
            packageManager.setComponentEnabledSetting(
                    newComponentName,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
            )
            Log.d("changeIcon", "Alias habilitado: $newActivity")

            // Desativar os outros aliases
            aliases.forEach { alias ->
                if (alias != newActivity) {
                    val componentName = ComponentName(this, "$packageName.$alias")
                    packageManager.setComponentEnabledSetting(
                            componentName,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                    )
                    Log.d("changeIcon", "Alias desabilitado: $alias")
                }
            }
        } catch (e: Exception) {
            Log.e("changeIcon", "Erro ao alterar o ícone: ${e.message}")
        }
    }
}
