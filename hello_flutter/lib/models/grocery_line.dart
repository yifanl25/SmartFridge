// ignore_for_file: slash_for_doc_comments

import '../utils/grocery_category_map.dart';

/**
 * Grocery list row model.
 * <p>
 * Mirrors one item from `GET /api/grocery/items`.
 */
class GroceryLineView {
  const GroceryLineView({
    required this.id,
    required this.name,
    required this.quantity,
    required this.price,
    required this.collected,
    required this.categoryIcon,
    required this.categoryName,
  });

  final String id;
  final String name;
  final int quantity;
  final double price;
  final bool collected;
  final String? categoryIcon;
  final String? categoryName;

  /** UI category derived from the backend category metadata. */
  GroceryUiCategory get uiCategory =>
      groceryUiCategoryFromApiIcon(categoryIcon, categoryName);

  /** Formatted quantity and price label for the list UI. */
  String get qtyLabel =>
      quantity <= 0 ? '0' : '$quantity × \$${price.toStringAsFixed(2)}';

  /** Builds a grocery row from API JSON. */
  factory GroceryLineView.fromJson(Map<String, dynamic> json) {
    final cat = json['category'] as Map<String, dynamic>?;
    return GroceryLineView(
      id: json['id'] as String? ?? '',
      name: json['name'] as String? ?? '',
      quantity: (json['quantity'] as num?)?.round() ?? 0,
      price: (json['price'] as num?)?.toDouble() ?? 0,
      collected: json['collected'] == true,
      categoryIcon: cat?['icon'] as String?,
      categoryName: cat?['name'] as String?,
    );
  }
}
