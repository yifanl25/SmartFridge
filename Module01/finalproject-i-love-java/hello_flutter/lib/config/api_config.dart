import 'package:flutter/foundation.dart';

/// Base URL for the Spring Boot API (`./gradlew bootRun`, default port 8080).
///
/// Override at run time:
/// flutter run --dart-define=SMARTFRIDGE_API=http://127.0.0.1:8080
const String _kDefineBase = String.fromEnvironment('SMARTFRIDGE_API');

String defaultSmartFridgeApiBase() {
  if (_kDefineBase.isNotEmpty) {
    return _kDefineBase.endsWith('/')
        ? _kDefineBase.substring(0, _kDefineBase.length - 1)
        : _kDefineBase;
  }

  // Flutter Web and desktop on the same machine as Spring Boot.
  if (kIsWeb) {
    return 'http://127.0.0.1:8080';
  }

  // Android emulator maps host localhost to 10.0.2.2.
  if (defaultTargetPlatform == TargetPlatform.android) {
    return 'http://10.0.2.2:8080';
  }

  // iOS simulator / macOS / other local runs.
  return 'http://127.0.0.1:8080';
}