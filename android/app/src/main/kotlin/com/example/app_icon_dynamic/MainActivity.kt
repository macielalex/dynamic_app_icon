package com.example.app_icon_dynamic

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {

    private val CHANNEL = "com.example.app_icon_dynamic/icon_changer"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // Configurar o MethodChannel
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "updateToLemonIcon" -> {
                    // Alterar o ícone para Lemon
                    changeIcon("LemonActivity")
                    result.success("Ícone alterado para Lemon.")
                }
                else -> result.notImplemented()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Iniciar o serviço para detectar o fechamento do app por arrasto
        val serviceIntent = Intent(this, AppCloseService::class.java)
        startService(serviceIntent)

        // Registrar o observer do ciclo de vida
        val lifecycleObserver = AppLifecycleObserver(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)

        Log.d("MainActivity", "AppLifecycleObserver registrado.")
    }

    private fun changeIcon(newActivity: String) {
        val packageManager = packageManager
        val newComponentName = ComponentName(this, "$packageName.$newActivity")

        // Declaração explícita da lista de aliases
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