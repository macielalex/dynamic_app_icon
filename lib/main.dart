import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'package:flutter_dynamic_icon_plus/flutter_dynamic_icon_plus.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: HomeScreen(),
    );
  }
}

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  static const platform = MethodChannel('com.example.app_icon_dynamic/icon_changer');

  // Método para salvar a preferência do ícone
  Future<void> _saveIconPreference(String iconName) async {
    try {
      await platform.invokeMethod('saveIconPreference', {'iconName': iconName});
      print("Preferência de ícone salva: $iconName.");
    } on PlatformException catch (e) {
      print("Erro ao salvar a preferência do ícone: '${e.message}'.");
    }
  }


Future<void> _saveIconIos(String iconName) async {
 try {

  final bool supportsAlternateIcons =  await FlutterDynamicIconPlus.supportsAlternateIcons; 
    if (supportsAlternateIcons) {
      await FlutterDynamicIconPlus.setAlternateIconName(iconName: iconName);
      print("Ícone do app alterado com sucesso!");
    } else {
      print("Ícones alternativos não são suportados neste dispositivo.");
    }
  } on PlatformException catch (e) {
    print("Erro ao trocar o ícone: $e");
  }

}


  // Método para aplicar o ícone salvo antes de sair
  Future<void> _applyIconAndExit() async {
    try {
      // Recuperar o ícone salvo e aplicar
      await platform.invokeMethod('applySavedIcon');
      print("Ícone salvo aplicado com sucesso.");

      // Fechar o app
      SystemNavigator.pop();
    } on PlatformException catch (e) {
      print("Erro ao aplicar o ícone salvo: '${e.message}'.");
    }
  }

  // Mostrar diálogo para seleção de ícones
  void _showIconSelectionDialog() {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text("Escolha o Ícone"),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              ListTile(
                title: Text("Premium"),
                onTap: () async {
                  if(Platform.isAndroid){

                await  _saveIconPreference("PremiumActivity");
                  }else{
                     _saveIconIos("iconepremium");
                  }
                  Navigator.pop(context); // Apenas fecha o diálogo
                },
              ),
              ListTile(
                title: Text("Lemon"),
                onTap: ()async {
                 
                    if(Platform.isAndroid){

                   _saveIconPreference("LemonActivity");
                  }else{
                    _saveIconIos("iconelemon");
                  }
                  Navigator.pop(context); // Apenas fecha o diálogo
                },
              ),
              ListTile(
                title: Text("Orange"),
                onTap: () async{
                    if(Platform.isAndroid){

                 _saveIconPreference("OrangeActivity");
                  }else{
                    _saveIconIos("iconeorange");
                  }
                 
                  Navigator.pop(context); // Apenas fecha o diálogo
                },
              ),
              ListTile(
                title: Text("Padrão"),
                onTap: () async{
                 

                      if(Platform.isAndroid){

                 _saveIconPreference("MainActivity");
                  }else{
                    _saveIconIos("appicon");
                  }

                  
                  
                  Navigator.pop(context); // Apenas fecha o diálogo
                },
              ),
            ],
          ),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("App Icon Dynamic"),
      ),
      body: Center(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            ElevatedButton(
              onPressed: _showIconSelectionDialog,
              child: Text("Escolher Ícone"),
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: _applyIconAndExit,
              child: Text("Sair"),
            ),
          ],
        ),
      ),
    );
  }
}
