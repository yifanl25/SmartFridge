// ignore_for_file: slash_for_doc_comments

import 'package:flutter/material.dart';

import '../theme/smart_fridge_tokens.dart';

/** Inventory categories used by the fridge page. */
enum FoodCategory {
  all,
  vegetables,
  fruits,
  dairy,
  meat,
  beverages,
  grains,
  frozen,
}

/** Labels and icons for inventory categories. */
extension FoodCategoryX on FoodCategory {
  String get label {
    switch (this) {
      case FoodCategory.all:
        return 'All';
      case FoodCategory.vegetables:
        return 'Vegetables';
      case FoodCategory.fruits:
        return 'Fruits';
      case FoodCategory.dairy:
        return 'Dairy';
      case FoodCategory.meat:
        return 'Meat';
      case FoodCategory.beverages:
        return 'Beverages';
      case FoodCategory.grains:
        return 'Grains';
      case FoodCategory.frozen:
        return 'Frozen';
    }
  }

  IconData get icon {
    switch (this) {
      case FoodCategory.all:
        return Icons.kitchen_outlined;
      case FoodCategory.vegetables:
        return Icons.eco;
      case FoodCategory.fruits:
        return Icons.apple;
      case FoodCategory.dairy:
        return Icons.breakfast_dining;
      case FoodCategory.meat:
        return Icons.set_meal;
      case FoodCategory.beverages:
        return Icons.local_drink;
      case FoodCategory.grains:
        return Icons.agriculture;
      case FoodCategory.frozen:
        return Icons.ac_unit;
    }
  }
}

/**
 * Expiry status bucket.
 * <p>
 * Used for expiry labels and color styling.
 */
enum ExpiryBand {
  urgent,
  soon,
  ok,
}

/** Maps remaining days to an expiry band. */
ExpiryBand expiryBandForDays(int daysLeft) {
  if (daysLeft <= 0) return ExpiryBand.urgent;
  if (daysLeft <= 3) return ExpiryBand.soon;
  return ExpiryBand.ok;
}

/** Returns the color for an expiry band. */
Color expiryColor(ExpiryBand band) {
  switch (band) {
    case ExpiryBand.urgent:
      return SfColors.expiryUrgent;
    case ExpiryBand.soon:
      return SfColors.expirySoon;
    case ExpiryBand.ok:
      return SfColors.expiryOk;
  }
}

/** Returns the display label for remaining days. */
String expiryLabel(int daysLeft) {
  if (daysLeft < 0) return 'Expired';
  if (daysLeft == 0) return 'Expires Today';
  if (daysLeft == 1) return 'Tomorrow';
  if (daysLeft == 2) return '2 days left';
  return '$daysLeft days left';
}

/** Optional item badge shown on inventory cards. */
enum ItemBadge { urgent, newItem }

/**
 * Inventory item model.
 * <p>
 * Used by the fridge page for list and card rendering.
 */
class InventoryItem {
  const InventoryItem({
    required this.id,
    required this.name,
    required this.category,
    required this.quantityLabel,
    required this.daysLeft,
    this.isNew = false,
    this.badge,
  });

  final String id;
  final String name;
  final FoodCategory category;
  final String quantityLabel;
  final int daysLeft;
  final bool isNew;
  final ItemBadge? badge;

  /** Derived expiry band for the current item. */
  ExpiryBand get expiryBand => expiryBandForDays(daysLeft);
}
