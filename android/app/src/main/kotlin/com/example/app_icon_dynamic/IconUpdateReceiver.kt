package com.example.app_icon_dynamic // Substitua pelo seu nome de pacote

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.os.Handler

class IconUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Handler(Looper.getMainLooper()).postDelayed({ // Adicionado atraso de 1 segundo
            val restartIntent = Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(restartIntent)
        }, 2000)
    }
}