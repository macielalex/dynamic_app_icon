import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
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

  // Método para alterar o ícone e fechar o app
  Future<void> _closeAppAndChangeIcon() async {
    try {
      await platform.invokeMethod('updateToLemonIcon');
      print("Ícone alterado para Lemon.");
    } on PlatformException catch (e) {
      print("Falha ao alterar o ícone: '${e.message}'.");
    }

    // Fechar o app manualmente
    SystemNavigator.pop();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("App Icon Dynamic"),
      ),
      body: Center(
        child: ElevatedButton(
          onPressed: _closeAppAndChangeIcon,
          child: Text("Fechar App e Alterar Ícone"),
        ),
      ),
    );
  }
}