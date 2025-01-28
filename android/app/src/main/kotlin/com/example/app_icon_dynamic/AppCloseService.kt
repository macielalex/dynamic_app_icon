package com.example.app_icon_dynamic

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log

class AppCloseService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d("AppCloseService", "App fechado por arrasto. Verificando ícone salvo.")

        // Obter o ícone salvo no SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("IconPrefs", MODE_PRIVATE)
        val selectedIcon = sharedPreferences.getString("selected_icon", "MainActivity")
        Log.d("AppCloseService", "Ícone salvo no SharedPreferences: $selectedIcon")

        // Alterar para o ícone salvo
        if (selectedIcon != null) {
            changeIcon(selectedIcon)
        } else {
            Log.e("AppCloseService", "Nenhum ícone encontrado no SharedPreferences.")
        }

        // Encerrar o serviço
        stopSelf()
    }

    private fun changeIcon(newActivity: String) {
        val packageManager = packageManager
        val newComponentName = ComponentName(this, "$packageName.$newActivity")

        try {
            // Ativar o novo alias
            packageManager.setComponentEnabledSetting(
                    newComponentName,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
            )
            Log.d("AppCloseService", "Alias habilitado: $newActivity")

            // Desativar outros aliases
            val aliases = listOf("PremiumActivity", "LemonActivity", "OrangeActivity", "MainActivity")
            aliases.forEach { alias ->
                if (alias != newActivity) {
                    val componentName = ComponentName(this, "$packageName.$alias")
                    packageManager.setComponentEnabledSetting(
                            componentName,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                    )
                    Log.d("AppCloseService", "Alias desabilitado: $alias")
                }
            }
        } catch (e: Exception) {
            Log.e("AppCloseService", "Erro ao alterar o ícone: ${e.message}")
        }
    }
}
