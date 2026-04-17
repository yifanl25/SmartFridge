/// Maps catalog category metadata to grocery list filter chips.
enum GroceryUiCategory {
  produce,
  protein,
  dairy,
  pantry,
  drinks,
  snacks,
}

extension GroceryUiCategoryX on GroceryUiCategory {
  String get label {
    switch (this) {
      case GroceryUiCategory.produce:
        return 'Produce';
      case GroceryUiCategory.protein:
        return 'Protein';
      case GroceryUiCategory.dairy:
        return 'Dairy';
      case GroceryUiCategory.pantry:
        return 'Pantry';
      case GroceryUiCategory.drinks:
        return 'Drinks';
      case GroceryUiCategory.snacks:
        return 'Snacks';
    }
  }
}

GroceryUiCategory groceryUiCategoryFromApiIcon(String? icon, String? name) {
  final i = icon?.toLowerCase() ?? '';
  final n = name?.toLowerCase() ?? '';
  if (i == 'dairy_eggs' || n.contains('dairy')) {
    return GroceryUiCategory.dairy;
  }
  if (i == 'meat_seafood' || n.contains('meat')) {
    return GroceryUiCategory.protein;
  }
  if (i == 'vegetables' || i == 'fruits' || n.contains('produce')) {
    return GroceryUiCategory.produce;
  }
  if (i == 'beverages') {
    return GroceryUiCategory.drinks;
  }
  if (i == 'grains_pantry' || i == 'condiments_sauces') {
    return GroceryUiCategory.pantry;
  }
  return GroceryUiCategory.pantry;
}
