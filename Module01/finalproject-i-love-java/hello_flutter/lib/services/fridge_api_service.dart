import 'dart:convert';

import 'package:http/http.dart' as http;

import '../config/api_config.dart';
import '../models/grocery_line.dart';
import '../models/health_goal.dart';
import '../models/inventory_models.dart';
import '../models/recipe_recommendation.dart';
import '../utils/inventory_json.dart';
import '../utils/recipe_json.dart';

/// Calls the Spring Boot API in `finalproject-i-love-java`
class FridgeApiService {
  FridgeApiService._();

  static final FridgeApiService instance = FridgeApiService._();

  String get baseUrl => defaultSmartFridgeApiBase();

  Uri _u(String path, [Map<String, String>? query]) {
    final root =
    baseUrl.endsWith('/') ? baseUrl.substring(0, baseUrl.length - 1) : baseUrl;
    final p = path.startsWith('/') ? path : '/$path';
    return Uri.parse('$root$p').replace(queryParameters: query);
  }

  Future<List<InventoryItem>> fetchInventory() async {
    final res = await http.get(_u('/api/inventory'));
    if (res.statusCode != 200) {
      throw FridgeApiException('inventory ${res.statusCode}', res.body);
    }

    final list = jsonDecode(res.body) as List<dynamic>;
    return list
        .map((e) => inventoryItemFromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<InventoryItem> addInventoryItem(String foodName) async {
    final res = await http.post(
      _u('/api/inventory'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'foodName': foodName}),
    );

    if (res.statusCode != 200) {
      throw FridgeApiException('add inventory ${res.statusCode}', res.body);
    }

    final map = jsonDecode(res.body) as Map<String, dynamic>;
    return inventoryItemFromJson(map);
  }

  /// Prefix search for add-item flow.
  Future<List<String>> fetchCatalogSuggestionNames(String prefix) async {
    final q = prefix.trim();
    if (q.isEmpty) {
      return const [];
    }

    final res = await http.get(_u('/api/catalog/suggestions', {'q': q}));
    if (res.statusCode != 200) {
      throw FridgeApiException('catalog ${res.statusCode}', res.body);
    }

    final list = jsonDecode(res.body) as List<dynamic>;
    return list
        .map((e) => (e as Map<String, dynamic>)['foodName'] as String? ?? '')
        .where((s) => s.isNotEmpty)
        .toList();
  }

  Future<void> savePreference(HealthGoal goal) async {
    final api = _healthGoalApi(goal);

    final res = await http.put(
      _u('/api/preference'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'healthGoal': api}),
    );

    if (res.statusCode != 200) {
      throw FridgeApiException('preference ${res.statusCode}', res.body);
    }
  }

  /// Returns null if the session has no saved preference yet.
  Future<HealthGoal?> fetchPreference() async {
    final res = await http.get(_u('/api/preference'));

    if (res.statusCode == 404) {
      return null;
    }

    if (res.statusCode != 200) {
      throw FridgeApiException('preference get ${res.statusCode}', res.body);
    }

    final rawBody = res.body.trim();
    if (rawBody.isEmpty || rawBody == 'null') {
      return null;
    }

    final decoded = jsonDecode(res.body);
    if (decoded == null) {
      return null;
    }

    if (decoded is! Map<String, dynamic>) {
      return null;
    }

    final raw = decoded['healthGoal'] as String?;
    return _parseHealthGoal(raw);
  }

  HealthGoal? _parseHealthGoal(String? raw) {
    if (raw == null || raw.isEmpty) {
      return null;
    }

    switch (raw.toUpperCase()) {
      case 'MUSCLE_BUILDING':
        return HealthGoal.muscleBuilding;
      case 'FAT_LOSS':
        return HealthGoal.fatLoss;
      case 'BLOOD_SUGAR_CARE':
        return HealthGoal.bloodSugarCare;
      default:
        return null;
    }
  }

  String _healthGoalApi(HealthGoal goal) {
    switch (goal) {
      case HealthGoal.muscleBuilding:
        return 'MUSCLE_BUILDING';
      case HealthGoal.fatLoss:
        return 'FAT_LOSS';
      case HealthGoal.bloodSugarCare:
        return 'BLOOD_SUGAR_CARE';
    }
  }

  Future<List<RecipeRecommendation>> fetchRecommendations() async {
    final res = await http.get(_u('/api/recommendations'));

    if (res.statusCode != 200) {
      throw FridgeApiException('recommendations ${res.statusCode}', res.body);
    }

    final list = jsonDecode(res.body) as List<dynamic>;
    return list
        .map((e) => recipeRecommendationFromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<List<GroceryLineView>> fetchGroceryItems() async {
    final res = await http.get(_u('/api/grocery/items'));

    if (res.statusCode != 200) {
      throw FridgeApiException('grocery list ${res.statusCode}', res.body);
    }

    final list = jsonDecode(res.body) as List<dynamic>;
    return list
        .map((e) => GroceryLineView.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<GroceryLineView> addGroceryLine({
    required String foodName,
    int quantity = 1,
    double price = 0,
  }) async {
    final res = await http.post(
      _u('/api/grocery/items'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'foodName': foodName,
        'quantity': quantity,
        'price': price,
      }),
    );

    if (res.statusCode != 200) {
      throw FridgeApiException('grocery add ${res.statusCode}', res.body);
    }

    return GroceryLineView.fromJson(
      jsonDecode(res.body) as Map<String, dynamic>,
    );
  }

  Future<GroceryLineView> toggleGroceryCollected(String id) async {
    final res = await http.patch(_u('/api/grocery/items/$id/collected'));

    if (res.statusCode != 200) {
      throw FridgeApiException('grocery toggle ${res.statusCode}', res.body);
    }

    return GroceryLineView.fromJson(
      jsonDecode(res.body) as Map<String, dynamic>,
    );
  }

  Future<GroceryLineView> updateGroceryQuantity(String id, int delta) async {
    final res = await http.patch(
      _u('/api/grocery/items/$id/quantity', {'delta': '$delta'}),
    );

    if (res.statusCode != 200) {
      throw FridgeApiException('grocery qty ${res.statusCode}', res.body);
    }

    return GroceryLineView.fromJson(
      jsonDecode(res.body) as Map<String, dynamic>,
    );
  }

  Future<void> groceryCheckout() async {
    final res = await http.post(_u('/api/grocery/checkout'));

    if (res.statusCode != 200 && res.statusCode != 204) {
      throw FridgeApiException('checkout ${res.statusCode}', res.body);
    }
  }

  Future<Map<String, double>> fetchGroceryTotals() async {
    final res = await http.get(_u('/api/grocery/totals'));

    if (res.statusCode != 200) {
      throw FridgeApiException('totals ${res.statusCode}', res.body);
    }

    final map = jsonDecode(res.body) as Map<String, dynamic>;

    return {
      'subtotal': (map['subtotal'] as num?)?.toDouble() ?? 0,
      'tax': (map['tax'] as num?)?.toDouble() ?? 0,
      'total': (map['total'] as num?)?.toDouble() ?? 0,
    };
  }

  Future<Map<String, dynamic>?> fetchRecipeDetail(String id) async {
    try {
      final response = await http.get(_u('/api/recommendations/$id'));

      if (response.statusCode == 200) {
        return jsonDecode(response.body) as Map<String, dynamic>;
      } else {
        print(
          'Failed to fetch recipe detail: ${response.statusCode}, body=${response.body}',
        );
        return null;
      }
    } catch (e) {
      print('Network error in fetchRecipeDetail: $e');
      return null;
    }
  }

  Future<bool> addRecipeMissingToGrocery(String id) async {
    try {
      final response = await http.post(_u('/api/recommendations/$id/grocery'));

      if (response.statusCode == 200) {
        print('Successfully added missing ingredients to the grocery list');
        return true;
      } else {
        print(
          'Failed to add ingredients: ${response.statusCode}, body=${response.body}',
        );
        return false;
      }
    } catch (e) {
      print('Network error in addRecipeMissingToGrocery: $e');
      return false;
    }
  }
}

class FridgeApiException implements Exception {
  FridgeApiException(this.message, this.body);

  final String message;
  final String body;

  @override
  String toString() => 'FridgeApiException: $message\n$body';
}