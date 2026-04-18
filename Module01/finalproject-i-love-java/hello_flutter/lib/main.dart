import 'package:flutter/material.dart';

import 'app/main_shell.dart';
import 'screens/preferences_screen.dart';
import 'screens/welcome_screen.dart';
import 'theme/smart_fridge_tokens.dart';

void main() {
  runApp(const SmartFridgeApp());
}

class SmartFridgeApp extends StatelessWidget {
  const SmartFridgeApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'SmartFridge',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: true,
        scaffoldBackgroundColor: SfColors.cream,
        colorScheme: ColorScheme.fromSeed(
          seedColor: SfColors.brown,
          brightness: Brightness.light,
          primary: SfColors.brown,
          surface: SfColors.cream,
        ),
      ),
      routes: {
        '/': (_) => const WelcomePage(),
        '/preference': (_) => const PreferencesScreen(),
        '/main': (_) => const MainShell(),
      },
    );
  }
}
