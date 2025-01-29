import UIKit
import Flutter

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
    override func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        let controller: FlutterViewController = window?.rootViewController as! FlutterViewController
        let channel = FlutterMethodChannel(name: "com.example.app_icon_dynamic/icon_changer",
                                           binaryMessenger: controller.binaryMessenger)
        
        channel.setMethodCallHandler { (call, result) in
            switch call.method {
            case "updateIcon":
                if let args = call.arguments as? [String: String],
                   let iconName = args["iconName"] {
                    self.updateAppIcon(to: iconName, result: result)
                } else {
                    result(FlutterError(code: "INVALID_ARGUMENT", message: "iconName is missing", details: nil))
                }
            case "applySavedIcon":
                // Recuperar ícone salvo e aplicar
                self.applySavedIcon(result: result)
            default:
                result(FlutterMethodNotImplemented)
            }
        }
        
        return super.application(application, didFinishLaunchingWithOptions: launchOptions)
    }

    private func updateAppIcon(to iconName: String, result: FlutterResult) {
        guard UIApplication.shared.supportsAlternateIcons else {
            result(FlutterError(code: "UNSUPPORTED", message: "Alternate icons not supported", details: nil))
            return
        }
        
        UIApplication.shared.setAlternateIconName(iconName) { (error) in
            if let error = error {
                result(FlutterError(code: "ICON_CHANGE_FAILED", message: error.localizedDescription, details: nil))
            } else {
                result(true)
            }
        }
    }

    private func applySavedIcon(result: FlutterResult) {
        let userDefaults = UserDefaults.standard
        if let savedIcon = userDefaults.string(forKey: "selected_icon") {
            updateAppIcon(to: savedIcon, result: result)
        } else {
            result(FlutterError(code: "NO_ICON_SAVED", message: "No saved icon found", details: nil))
        }
    }
}
