import 'dart:io';

/// Base URL for the Spring Boot API (`./gradlew bootRun`, default port 8080).
///
/// - iOS Simulator / macOS / Flutter Web: `127.0.0.1` works.
/// - Android Emulator: use `10.0.2.2` (maps to host localhost).
/// - Physical phone: use your computer's LAN IP, e.g. `http://192.168.1.10:8080`.
///
/// Override at run time:
/// `flutter run --dart-define=SMARTFRIDGE_API=http://10.0.2.2:8080`
const String _kDefineBase = String.fromEnvironment('SMARTFRIDGE_API');

String defaultSmartFridgeApiBase() {
  if (_kDefineBase.isNotEmpty) {
    return _kDefineBase.endsWith('/')
        ? _kDefineBase.substring(0, _kDefineBase.length - 1)
        : _kDefineBase;
  }
  if (Platform.isAndroid) {
    return 'http://10.0.2.2:8080';
  }
  return 'http://127.0.0.1:8080';
}
