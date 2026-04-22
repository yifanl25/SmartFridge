// ignore_for_file: slash_for_doc_comments

import 'package:flutter/material.dart';

import 'app/main_shell.dart';
import 'screens/preferences_screen.dart';
import 'screens/welcome_screen.dart';
import 'theme/smart_fridge_tokens.dart';

/** App entry point. */
void main() {
  runApp(const SmartFridgeApp());
}

/**
 * Root app widget.
 * <p>
 * Sets up the shared theme and named routes.
 *
 * Official references:
 * MaterialApp: https://api.flutter.dev/flutter/material/MaterialApp-class.html
 * ThemeData: https://api.flutter.dev/flutter/material/ThemeData-class.html
 */
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
