// ignore_for_file: slash_for_doc_comments

import 'package:flutter/foundation.dart';

/** API base override from `--dart-define=SMARTFRIDGE_API=...`. */
const String _kDefineBase = String.fromEnvironment('SMARTFRIDGE_API');

/**
 * Returns the default API base URL.
 * <p>
 * Uses the dart-define override when provided.
 */
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
