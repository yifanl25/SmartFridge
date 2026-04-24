// ignore_for_file: slash_for_doc_comments

import 'package:flutter/material.dart';

/**
 * Shared color tokens.
 * <p>
 * Used across the SmartFridge Flutter UI.
 */
abstract final class SfColors {
  static const Color cream = Color(0xFFF9F1E7);
  static const Color brown = Color(0xFF5D2E1F);
  static const Color brownMuted = Color(0xFF8B7355);
  static const Color brownLabel = Color(0xFF6B5344);
  static const Color whiteCard = Color(0xFFFFFBF7);
  static const Color chipBgLight = Color(0xFFEDE4D8);
  static const Color stepInactive = Color(0xFFD4C4B0);

  /** Inventory expiry colors. */
  static const Color expiryUrgent = Color(0xFFC62828);
  static const Color expirySoon = Color(0xFFE65100);
  static const Color expiryOk = Color(0xFF2E7D32);
  static const Color badgeUrgentBg = Color(0xFFFFEBEE);

  /** Recipe match color. */
  static const Color matchGreen = Color(0xFF2E7D32);
}

/** Shared corner radius tokens. */
abstract final class SfRadii {
  static const double card = 24;
  static const double pill = 999;
}
