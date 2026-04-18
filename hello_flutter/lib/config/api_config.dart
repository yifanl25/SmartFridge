import 'package:flutter/foundation.dart';

const String _kDefineBase = String.fromEnvironment('SMARTFRIDGE_API');

String defaultSmartFridgeApiBase() {
  if (_kDefineBase.isNotEmpty) {
    return _kDefineBase.endsWith('/')
        ? _kDefineBase.substring(0, _kDefineBase.length - 1)
        : _kDefineBase;
  }

  if (kIsWeb) {
    return 'http://127.0.0.1:8080';
  }

  return 'http://127.0.0.1:8080';
}