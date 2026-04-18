import '../models/inventory_models.dart';

FoodCategory foodCategoryFromApiIcon(String? icon) {
  switch (icon) {
    case 'dairy_eggs':
      return FoodCategory.dairy;
    case 'vegetables':
      return FoodCategory.vegetables;
    case 'fruits':
      return FoodCategory.fruits;
    case 'meat_seafood':
      return FoodCategory.meat;
    case 'grains_pantry':
      return FoodCategory.grains;
    case 'beverages':
      return FoodCategory.beverages;
    case 'condiments_sauces':
      return FoodCategory.grains;
    default:
      return FoodCategory.vegetables;
  }
}

/// Parses one FoodItem JSON object from the Java API.
InventoryItem inventoryItemFromJson(Map<String, dynamic> json) {
  final cat = json['category'] as Map<String, dynamic>?;
  final icon = cat?['icon'] as String?;
  final category = foodCategoryFromApiIcon(icon);

  final expiry = json['expiryDate'] as String?;
  final created = json['createdAt'] as String?;
  final now = DateTime.now();
  int daysLeft = 7;
  if (expiry != null && expiry.isNotEmpty) {
    final e = DateTime.parse(expiry);
    final today = DateTime(now.year, now.month, now.day);
    final ed = DateTime(e.year, e.month, e.day);
    daysLeft = ed.difference(today).inDays;
  }

  final qty = json['quantity'];
  final unit = json['unit'] as String? ?? 'pcs';
  final quantityLabel =
      qty is int ? '$qty $unit' : '${qty ?? 1} $unit';

  final isNew = json['newItem'] == true || (created != null && _isRecent(created));
  final urgent = json['urgent'] == true || daysLeft <= 0;

  ItemBadge? badge;
  if (urgent) {
    badge = ItemBadge.urgent;
  } else if (isNew) {
    badge = ItemBadge.newItem;
  }

  return InventoryItem(
    id: json['id'] as String? ?? '',
    name: json['name'] as String? ?? '',
    category: category,
    quantityLabel: quantityLabel,
    daysLeft: daysLeft,
    isNew: isNew,
    badge: badge,
  );
}

bool _isRecent(String createdAtIso) {
  try {
    final c = DateTime.parse(createdAtIso);
    final today = DateTime.now();
    final cDay = DateTime(c.year, c.month, c.day);
    final y = today.subtract(const Duration(days: 1));
    final yDay = DateTime(y.year, y.month, y.day);
    return !cDay.isBefore(yDay);
  } catch (_) {
    return false;
  }
}
