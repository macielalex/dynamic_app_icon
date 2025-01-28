package com.example.app_icon_dynamic

import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class AppLifecycleObserver(private val context: Context) : DefaultLifecycleObserver {

    private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("IconPrefs", Context.MODE_PRIVATE)

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Log.d("AppLifecycleObserver", "App entrou em segundo plano (onStop chamado).")
        handleIconChange()
    }

    private fun handleIconChange() {
        val selectedIcon = sharedPreferences.getString("selected_icon", "MainActivity")
        if (selectedIcon != null) {
            changeIcon(selectedIcon)
        }
    }

    private fun changeIcon(newActivity: String) {
        val packageManager = context.packageManager
        val newComponentName = ComponentName(context, "${context.packageName}.$newActivity")

        try {
            // Ativar o alias selecionado
            packageManager.setComponentEnabledSetting(
                    newComponentName,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
            )
            Log.d("AppLifecycleObserver", "Novo alias habilitado: $newActivity")

            // Desativar outros aliases
            val aliases = listOf("PremiumActivity", "LemonActivity", "OrangeActivity", "MainActivity")
            aliases.forEach { alias ->
                if (alias != newActivity) {
                    val componentName = ComponentName(context, "${context.packageName}.$alias")
                    packageManager.setComponentEnabledSetting(
                            componentName,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                    )
                    Log.d("AppLifecycleObserver", "Alias desabilitado: $alias")
                }
            }
        } catch (e: Exception) {
            Log.e("AppLifecycleObserver", "Erro ao alterar o ícone: ${e.message}")
        }
    }
}
