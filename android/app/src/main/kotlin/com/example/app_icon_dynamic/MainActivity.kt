package com.example.app_icon_dynamic
import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

class MainActivity : FlutterActivity() {

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // Inicialize o plugin
        PremiumIconPlugin(this).initWith(flutterEngine.dartExecutor.binaryMessenger)
    }
}
