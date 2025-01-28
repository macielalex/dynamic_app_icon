package com.example.app_icon_dynamic

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log

class AppCloseService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d("AppCloseService", "App fechado por arrasto.")

        // Alterar o ícone quando o app é fechado por arrasto
        changeIcon("LemonActivity") // Altere para o ícone desejado

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
                }
            }

            // Log para confirmar a troca de ícone
            Log.d("AppCloseService", "Ícone alterado para: $newActivity")
        } catch (e: Exception) {
            Log.e("AppCloseService", "Erro ao alterar o ícone: ${e.message}")
        }
    }
}