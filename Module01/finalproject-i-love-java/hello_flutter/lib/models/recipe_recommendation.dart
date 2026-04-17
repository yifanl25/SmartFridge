import 'package:flutter/material.dart';

enum RecipeMealSlot {
  all,
  breakfast,
  lunch,
  dinner,
  salad,
  dessert,
  soup,
}

extension RecipeMealSlotX on RecipeMealSlot {
  String get label {
    switch (this) {
      case RecipeMealSlot.all:
        return 'All';
      case RecipeMealSlot.breakfast:
        return 'Breakfast';
      case RecipeMealSlot.lunch:
        return 'Lunch';
      case RecipeMealSlot.dinner:
        return 'Dinner';
      case RecipeMealSlot.salad:
        return 'Salad';
      case RecipeMealSlot.dessert:
        return 'Dessert';
      case RecipeMealSlot.soup:
        return 'Soup';
    }
  }

  IconData get icon {
    switch (this) {
      case RecipeMealSlot.all:
        return Icons.restaurant_menu;
      case RecipeMealSlot.breakfast:
        return Icons.breakfast_dining;
      case RecipeMealSlot.lunch:
        return Icons.lunch_dining;
      case RecipeMealSlot.dinner:
        return Icons.dinner_dining;
      case RecipeMealSlot.salad:
        return Icons.eco;
      case RecipeMealSlot.dessert:
        return Icons.icecream;
      case RecipeMealSlot.soup:
        return Icons.soup_kitchen;
    }
  }
}

/// Sub-filters: All / preference-weighted high match / quick prep.
enum RecipeSubFilter {
  all,
  highMatch,
  quickMeals,
}

extension RecipeSubFilterX on RecipeSubFilter {
  String get label {
    switch (this) {
      case RecipeSubFilter.all:
        return 'All Recipes';
      case RecipeSubFilter.highMatch:
        return 'High Match';
      case RecipeSubFilter.quickMeals:
        return 'Quick Meals';
    }
  }
}

/// One required ingredient line from the API (availability from scoring).
class RecipeIngredientDetail {
  const RecipeIngredientDetail({
    required this.name,
    required this.quantityText,
    required this.inFridge,
  });

  final String name;
  final String quantityText;
  final bool inFridge;
}

class RecipeRecommendation {
  const RecipeRecommendation({
    required this.id,
    required this.title,
    required this.matchPercent,
    required this.rating,
    required this.ingredientTags,
    required this.prepMinutes,
    required this.kcal,
    required this.slot,
    this.description,
    this.ingredientDetails,
  });

  final String id;
  final String title;
  final int matchPercent;
  final double rating;
  final List<String> ingredientTags;
  final int prepMinutes;
  final int kcal;
  final RecipeMealSlot slot;

  /// Long description from backend recipe JSON (null for demo-only cards).
  final String? description;

  /// When non-null (API), detail screen uses this instead of heuristic tags.
  final List<RecipeIngredientDetail>? ingredientDetails;
}
