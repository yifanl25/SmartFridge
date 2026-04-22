// ignore_for_file: slash_for_doc_comments

import '../models/recipe_recommendation.dart';

/** Maps a backend recipe category name to a meal slot. */
RecipeMealSlot mealSlotFromCategoryName(String name) {
  switch (name.toLowerCase()) {
    case 'breakfast':
      return RecipeMealSlot.breakfast;
    case 'lunch':
      return RecipeMealSlot.lunch;
    case 'dinner':
      return RecipeMealSlot.dinner;
    case 'salad':
      return RecipeMealSlot.salad;
    case 'dessert':
      return RecipeMealSlot.dessert;
    case 'soup':
      return RecipeMealSlot.soup;
    default:
      return RecipeMealSlot.lunch;
  }
}

/** Normalizes text for case-insensitive ingredient matching. */
String _norm(String s) => s.trim().toLowerCase();

/**
 * Parses one recipe recommendation from API JSON.
 * <p>
 * Builds the app model used by the recipes and detail screens.
 */
RecipeRecommendation recipeRecommendationFromJson(Map<String, dynamic> json) {
  final cat = json['recipeCategory'] as Map<String, dynamic>?;
  final catName = cat?['name'] as String? ?? '';
  final slot = mealSlotFromCategoryName(catName);

  final required = json['requiredIngredients'] as List<dynamic>? ?? [];
  final tags = required
      .map((e) => (e as Map<String, dynamic>)['name'] as String? ?? '')
      .where((s) => s.isNotEmpty)
      .toList();

  final available = (json['availableIngredients'] as List<dynamic>?)
          ?.map((e) => e as String)
          .toList() ??
      const <String>[];
  final availableNorm = available.map(_norm).toSet();

  List<RecipeIngredientDetail>? details;
  if (required.isNotEmpty) {
    details = required.map((e) {
      final m = e as Map<String, dynamic>;
      final n = m['name'] as String? ?? '';
      final qty = m['quantityText'] as String? ?? '';
      final inFridge = availableNorm.contains(_norm(n));
      return RecipeIngredientDetail(
        name: n,
        quantityText: qty,
        inFridge: inFridge,
      );
    }).toList();
  }

  final match = (json['matchScore'] as num?)?.round().clamp(0, 100) ?? 0;
  final rating = (json['rating'] as num?)?.toDouble() ?? 0;
  final cook = (json['cookTime'] as num?)?.round() ?? 0;
  final kcal = (json['calories'] as num?)?.round() ?? 0;
  final description = json['description'] as String?;

  return RecipeRecommendation(
    id: json['id'] as String? ?? '',
    title: json['title'] as String? ?? '',
    matchPercent: match,
    rating: rating,
    ingredientTags: tags.isEmpty ? ['—'] : tags,
    prepMinutes: cook,
    kcal: kcal,
    slot: slot,
    description: description,
    ingredientDetails: details,
  );
}
