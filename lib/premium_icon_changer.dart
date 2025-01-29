import 'package:flutter/services.dart';

class  PremiumIconChanger  {
  static  const MethodChannel _channel = MethodChannel( 'premium_icon' );

  static Future< void > updateToDefaultIcon() async {
    try {
      await _channel.invokeMethod( 'updateToDefaultIcon' );
    } on PlatformException catch (e) { //adicione se necessário
      print ( "Falha ao alterar o ícone: ${e.message} " );
    } on MissingPluginException catch (e) { //adicione se necessário
      print ( "Falha ao alterar o ícone: ${e.message} " );
    }
  }

  static Future< void > updateToPremiumIcon() async {
    try {
      await _channel.invokeMethod( 'updateToPremiumIcon' );
    } on PlatformException catch (e) { //adicione se necessário
      print ( "Falha ao alterar o ícone: ${e.message} " );
    } on MissingPluginException catch (e) { //adicione se necessário
      print ( "Falha ao alterar o ícone: ${e.message} " );
    }
  }

  static Future< void > updateToLemonIcon() async {
    try {
      await _channel.invokeMethod( 'updateToLemonIcon' );
    } on PlatformException catch (e) { //adicione se necessário
      print ( "Falha ao alterar o ícone: ${e.message} " );
    } on MissingPluginException catch (e) { //adicione se necessário
      print ( "Falha ao alterar o ícone: ${e.message} " );
    }
  }

  static Future<void> updateToOrangeIcon() async {
    try {
      await _channel.invokeMethod('saveSelectedIcon', {'iconName': 'OrangeActivity'});
    } catch (e) {
      print("Erro ao salvar o ícone: $e");
    }
  }

  static Future<void> applyIcon() async {
    try {
      await _channel.invokeMethod('applySavedIcon'); // Chama o método nativo para aplicar o ícone salvo
    } catch (e) {
      print("Erro ao aplicar o ícone salvo: $e");
    }
  }

}