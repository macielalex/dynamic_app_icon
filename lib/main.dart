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

  // Método para salvar a preferência do ícone
  Future<void> _saveIconPreference(String iconName) async {
    try {
      await platform.invokeMethod('saveIconPreference', {'iconName': iconName});
      print("Preferência de ícone salva: $iconName.");
    } on PlatformException catch (e) {
      print("Erro ao salvar a preferência do ícone: '${e.message}'.");
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
                onTap: () {
                  _saveIconPreference("PremiumActivity");
                  Navigator.pop(context); // Apenas fecha o diálogo
                },
              ),
              ListTile(
                title: Text("Lemon"),
                onTap: () {
                  _saveIconPreference("LemonActivity");
                  Navigator.pop(context); // Apenas fecha o diálogo
                },
              ),
              ListTile(
                title: Text("Orange"),
                onTap: () {
                  _saveIconPreference("OrangeActivity");
                  Navigator.pop(context); // Apenas fecha o diálogo
                },
              ),
              ListTile(
                title: Text("Padrão"),
                onTap: () {
                  _saveIconPreference("MainActivity");
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
