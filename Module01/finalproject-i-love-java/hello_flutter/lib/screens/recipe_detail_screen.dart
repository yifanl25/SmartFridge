import 'package:flutter/material.dart';

import '../models/recipe_recommendation.dart';
import '../services/fridge_api_service.dart';
import '../theme/smart_fridge_tokens.dart';

class RecipeDetailScreen extends StatefulWidget {
  const RecipeDetailScreen({
    super.key,
    required this.recipe,
    this.detailJson,
  });

  final RecipeRecommendation recipe;

  /// Optional preloaded detail JSON from the previous screen.
  final Map<String, dynamic>? detailJson;

  @override
  State<RecipeDetailScreen> createState() => _RecipeDetailScreenState();
}

class _RecipeDetailScreenState extends State<RecipeDetailScreen> {
  bool _saved = false;
  bool _addingGrocery = false;

  late Future<Map<String, dynamic>?> _detailFuture;

  @override
  void initState() {
    super.initState();

    // Reference:
    // Flutter cookbook - Fetch data from the internet
    // https://docs.flutter.dev/cookbook/networking/fetch-data
    //
    // The page creates the Future once in initState so it does not refetch
    // every time build() runs.
    if (widget.detailJson != null) {
      _detailFuture = Future<Map<String, dynamic>?>.value(widget.detailJson);
    } else {
      _detailFuture = FridgeApiService.instance.fetchRecipeDetail(widget.recipe.id);
    }
  }

  RecipeRecommendation _mergeFromApi(
      RecipeRecommendation base,
      Map<String, dynamic>? json,
      ) {
    if (json == null) {
      return base;
    }

    final requiredIngredients = json['requiredIngredients'];
    List<RecipeIngredientDetail>? details;

    if (requiredIngredients is List && requiredIngredients.isNotEmpty) {
      details = <RecipeIngredientDetail>[];

      for (final item in requiredIngredients) {
        if (item is Map<String, dynamic>) {
          details.add(
            RecipeIngredientDetail(
              name: item['name'] as String? ?? '',
              quantityText: item['quantityText'] as String? ?? '',
              inFridge: item['inFridge'] as bool? ?? false,
            ),
          );
        }
      }
    }

    return RecipeRecommendation(
      id: (json['id'] as String?) ?? base.id,
      title: (json['title'] as String?) ?? base.title,
      matchPercent: (json['matchPercent'] as num?)?.toInt() ?? base.matchPercent,
      rating: (json['rating'] as num?)?.toDouble() ?? base.rating,
      ingredientTags: base.ingredientTags,
      prepMinutes: (json['cookTime'] as num?)?.toInt() ?? base.prepMinutes,
      kcal: (json['calories'] as num?)?.toInt() ?? base.kcal,
      slot: base.slot,
      description: (json['description'] as String?) ?? base.description,
      ingredientDetails:
      (details != null && details.isNotEmpty)
          ? details
          : base.ingredientDetails,
    );
  }

  // Reference:
  // Flutter cookbook - Send data to the internet
  // https://docs.flutter.dev/cookbook/networking/send-data
  //
  // This action is kept in its own async method so the button logic stays simple.
  Future<void> _onAddMissingToGrocery() async {
    setState(() {
      _addingGrocery = true;
    });

    final ok = await FridgeApiService.instance
        .addRecipeMissingToGrocery(widget.recipe.id);

    if (!mounted) {
      return;
    }

    setState(() {
      _addingGrocery = false;
    });

    if (ok) {
      Navigator.of(context).pop(true);
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Could not add items to grocery list.'),
          behavior: SnackBarBehavior.floating,
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    // Reference:
    // Flutter cookbook - Fetch data from the internet
    // https://docs.flutter.dev/cookbook/networking/fetch-data
    //
    // FutureBuilder handles loading, error, and success states.
    return FutureBuilder<Map<String, dynamic>?>(
      future: _detailFuture,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Scaffold(
            backgroundColor: SfColors.cream,
            body: SafeArea(
              child: Center(
                child: CircularProgressIndicator(),
              ),
            ),
          );
        }

        RecipeRecommendation recipe = widget.recipe;

        if (snapshot.hasData) {
          recipe = _mergeFromApi(widget.recipe, snapshot.data);
        }

        return _buildScaffold(context, recipe, snapshot);
      },
    );
  }

  Widget _buildScaffold(
      BuildContext context,
      RecipeRecommendation recipe,
      AsyncSnapshot<Map<String, dynamic>?> snapshot,
      ) {
    final ingredients = _ingredientRows(recipe);
    final fromFridge = ingredients.where((e) => e.inFridge).length;
    final totalIngredients = ingredients.length;
    final match =
    totalIngredients == 0 ? 0 : ((fromFridge / totalIngredients) * 100).round();
    final reviews = 124;
    final totalCook = recipe.prepMinutes + 5;

    return Scaffold(
      backgroundColor: SfColors.cream,
      body: SafeArea(
        child: Column(
          children: [
            if (snapshot.hasError)
              Container(
                width: double.infinity,
                margin: const EdgeInsets.fromLTRB(16, 16, 16, 0),
                padding: const EdgeInsets.all(12),
                decoration: BoxDecoration(
                  color: Colors.orange.shade50,
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Text(
                  'API unavailable — showing basic recipe detail.\n${snapshot.error}',
                  style: TextStyle(
                    color: Colors.orange.shade900,
                    fontSize: 13,
                  ),
                ),
              ),
            Expanded(
              child: LayoutBuilder(
                builder: (context, constraints) {
                  if (constraints.maxWidth < 980) {
                    return _MobileRecipeDetail(
                      recipe: recipe,
                      saved: _saved,
                      onToggleSave: () {
                        setState(() {
                          _saved = !_saved;
                        });
                      },
                      ingredients: ingredients,
                      match: match,
                      onAddMissingToGrocery: _onAddMissingToGrocery,
                      addingGrocery: _addingGrocery,
                    );
                  }

                  return Row(
                    children: [
                      _RecipeInfoRail(
                        recipe: recipe,
                        match: match,
                        ingredients: ingredients,
                        onBack: () => Navigator.of(context).pop(),
                        onAddMissingToGrocery: _onAddMissingToGrocery,
                        addingGrocery: _addingGrocery,
                      ),
                      Expanded(
                        child: Column(
                          children: [
                            Expanded(
                              child: Container(
                                color: SfColors.whiteCard,
                                child: Stack(
                                  children: [
                                    Positioned(
                                      top: 18,
                                      left: 22,
                                      child: _ReviewPill(
                                        rating: recipe.rating,
                                        reviews: reviews,
                                      ),
                                    ),
                                    Positioned(
                                      top: 14,
                                      right: 18,
                                      child: Row(
                                        children: [
                                          _CircleIconButton(
                                            icon: _saved
                                                ? Icons.bookmark
                                                : Icons.bookmark_border,
                                            onTap: () {
                                              setState(() {
                                                _saved = !_saved;
                                              });
                                            },
                                          ),
                                          const SizedBox(width: 8),
                                          const _CircleIconButton(
                                            icon: Icons.share_outlined,
                                          ),
                                        ],
                                      ),
                                    ),
                                    Positioned(
                                      left: 18,
                                      bottom: 16,
                                      child: _TimelinePill(
                                        prep: recipe.prepMinutes,
                                        cook: 5,
                                        total: totalCook,
                                        serves: 2,
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            ),
                            SizedBox(
                              height: 290,
                              child: Row(
                                children: [
                                  for (final item in ingredients)
                                    Expanded(
                                      child: _IngredientTile(
                                        item: item,
                                        kcal: _kcalFor(item.name),
                                      ),
                                    ),
                                ],
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }

  List<_IngredientItem> _ingredientRows(RecipeRecommendation recipe) {
    final details = recipe.ingredientDetails;

    if (details != null && details.isNotEmpty) {
      return details.map((detail) {
        return _IngredientItem(
          name: detail.name,
          amount: detail.quantityText.isEmpty ? '—' : detail.quantityText,
          inFridge: detail.inFridge,
        );
      }).toList();
    }

    final rows = <_IngredientItem>[];

    for (final tag in recipe.ingredientTags) {
      rows.add(
        _IngredientItem(
          name: tag,
          amount: _amountFor(tag),
          inFridge: true,
        ),
      );
    }

    while (rows.length < 6) {
      final fillers = [
        const _IngredientItem(
          name: 'Whole Milk',
          amount: '2 tbsp',
          inFridge: true,
        ),
        const _IngredientItem(
          name: 'Unsalted Butter',
          amount: '10 g',
          inFridge: false,
        ),
        const _IngredientItem(
          name: 'Sourdough Bread',
          amount: '2 slices',
          inFridge: false,
        ),
      ];

      rows.add(
        fillers[(rows.length - recipe.ingredientTags.length) % fillers.length],
      );
    }

    return rows.take(6).toList();
  }

  String _amountFor(String text) {
    switch (text.toLowerCase()) {
      case 'eggs':
      case 'free range eggs':
        return '3 pcs';
      case 'spinach':
      case 'baby spinach':
        return '50 g';
      case 'cheese':
      case 'cheddar cheese':
        return '30 g';
      case 'whole milk':
        return '2 tbsp';
      default:
        return '1 portion';
    }
  }

  int _kcalFor(String text) {
    switch (text.toLowerCase()) {
      case 'eggs':
      case 'free range eggs':
        return 180;
      case 'spinach':
      case 'baby spinach':
        return 12;
      case 'cheese':
      case 'cheddar cheese':
        return 120;
      case 'whole milk':
        return 18;
      default:
        return 30;
    }
  }
}

class _RecipeInfoRail extends StatelessWidget {
  const _RecipeInfoRail({
    required this.recipe,
    required this.match,
    required this.ingredients,
    required this.onBack,
    required this.onAddMissingToGrocery,
    required this.addingGrocery,
  });

  final RecipeRecommendation recipe;
  final int match;
  final List<_IngredientItem> ingredients;
  final VoidCallback onBack;
  final VoidCallback onAddMissingToGrocery;
  final bool addingGrocery;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 272,
      padding: const EdgeInsets.fromLTRB(18, 16, 18, 18),
      decoration: const BoxDecoration(
        color: SfColors.cream,
        border: Border(right: BorderSide(color: Color(0xFFEDE4D8))),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              TextButton.icon(
                onPressed: onBack,
                icon: const Icon(Icons.arrow_back_ios_new, size: 14),
                label: const Text('Back'),
                style: TextButton.styleFrom(
                  foregroundColor: SfColors.brownMuted,
                  padding: const EdgeInsets.symmetric(horizontal: 0),
                ),
              ),
              const SizedBox(width: 8),
              const Text(
                '· SmartFridge',
                style: TextStyle(color: SfColors.brownMuted, fontSize: 12),
              ),
            ],
          ),
          const SizedBox(height: 8),
          Row(
            children: [
              _TagPill(label: recipe.slot.label, fill: SfColors.chipBgLight),
              const SizedBox(width: 8),
              _TagPill(
                label: '${recipe.matchPercent}% match',
                fill: const Color(0xFFEAF6EC),
                fg: SfColors.matchGreen,
              ),
            ],
          ),
          const SizedBox(height: 14),
          Text(
            recipe.title,
            style: const TextStyle(
              fontSize: 48,
              fontWeight: FontWeight.w800,
              color: SfColors.brown,
              height: 0.95,
            ),
          ),
          const SizedBox(height: 14),
          Text(
            (recipe.description != null && recipe.description!.trim().isNotEmpty)
                ? recipe.description!.trim()
                : 'A rich, protein-packed breakfast made from ingredients already in your fridge.',
            style: TextStyle(
              color: SfColors.brownLabel.withValues(alpha: 0.86),
              fontSize: 13,
              height: 1.45,
            ),
          ),
          const SizedBox(height: 14),
          Row(
            children: [
              Expanded(
                child: _InfoMiniCard(
                  value: '${recipe.prepMinutes}',
                  unit: 'min',
                  label: 'Prep time',
                ),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: _InfoMiniCard(
                  value: '${recipe.kcal}',
                  unit: 'kcal',
                  label: 'Calories',
                ),
              ),
            ],
          ),
          const SizedBox(height: 8),
          _MatchCard(match: match),
          const SizedBox(height: 14),
          const Text(
            'INGREDIENTS',
            style: TextStyle(
              fontSize: 11,
              letterSpacing: 2.2,
              fontWeight: FontWeight.w700,
              color: SfColors.brownMuted,
            ),
          ),
          const SizedBox(height: 8),
          Expanded(
            child: ListView.separated(
              itemCount: ingredients.length,
              separatorBuilder: (context, index) => const Divider(
                height: 1,
                color: Color(0xFFE7DED1),
              ),
              itemBuilder: (context, i) {
                final item = ingredients[i];
                return Padding(
                  padding: const EdgeInsets.symmetric(vertical: 8),
                  child: Row(
                    children: [
                      Icon(
                        Icons.circle,
                        size: 5,
                        color: item.inFridge
                            ? SfColors.brown
                            : SfColors.chipBgLight,
                      ),
                      const SizedBox(width: 8),
                      Expanded(
                        child: Text(
                          item.name,
                          style: TextStyle(
                            fontSize: 13,
                            fontWeight: FontWeight.w700,
                            color: item.inFridge
                                ? SfColors.brown
                                : SfColors.brownMuted,
                          ),
                          overflow: TextOverflow.ellipsis,
                        ),
                      ),
                      const SizedBox(width: 8),
                      Text(
                        item.amount,
                        style: TextStyle(
                          fontSize: 12,
                          color: item.inFridge
                              ? SfColors.brownLabel
                              : SfColors.brownMuted,
                          fontWeight: FontWeight.w500,
                        ),
                      ),
                      if (item.inFridge) ...[
                        const SizedBox(width: 6),
                        const Icon(
                          Icons.check,
                          size: 14,
                          color: SfColors.matchGreen,
                        ),
                      ],
                    ],
                  ),
                );
              },
            ),
          ),
          const SizedBox(height: 10),
          SizedBox(
            width: double.infinity,
            child: FilledButton(
              style: FilledButton.styleFrom(
                backgroundColor: SfColors.brown,
                foregroundColor: Colors.white,
                padding: const EdgeInsets.symmetric(vertical: 14),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(SfRadii.pill),
                ),
              ),
              onPressed: () {},
              child: const Text(
                'Start Cooking →',
                style: TextStyle(fontWeight: FontWeight.w700),
              ),
            ),
          ),
          const SizedBox(height: 10),
          SizedBox(
            width: double.infinity,
            child: FilledButton.tonal(
              style: FilledButton.styleFrom(
                foregroundColor: SfColors.brown,
                padding: const EdgeInsets.symmetric(vertical: 14),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(SfRadii.pill),
                ),
              ),
              onPressed: addingGrocery ? null : onAddMissingToGrocery,
              child: addingGrocery
                  ? const SizedBox(
                height: 22,
                width: 22,
                child: CircularProgressIndicator(strokeWidth: 2),
              )
                  : const Text(
                'Add missing to grocery',
                style: TextStyle(fontWeight: FontWeight.w700),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _MobileRecipeDetail extends StatelessWidget {
  const _MobileRecipeDetail({
    required this.recipe,
    required this.saved,
    required this.onToggleSave,
    required this.ingredients,
    required this.match,
    required this.onAddMissingToGrocery,
    required this.addingGrocery,
  });

  final RecipeRecommendation recipe;
  final bool saved;
  final VoidCallback onToggleSave;
  final List<_IngredientItem> ingredients;
  final int match;
  final VoidCallback onAddMissingToGrocery;
  final bool addingGrocery;

  @override
  Widget build(BuildContext context) {
    return CustomScrollView(
      slivers: [
        SliverAppBar(
          pinned: true,
          backgroundColor: SfColors.cream,
          foregroundColor: SfColors.brown,
          title: Text(
            recipe.title,
            style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w800),
          ),
          actions: [
            IconButton(
              onPressed: onToggleSave,
              icon: Icon(saved ? Icons.bookmark : Icons.bookmark_border),
            ),
          ],
        ),
        SliverPadding(
          padding: const EdgeInsets.all(16),
          sliver: SliverList(
            delegate: SliverChildListDelegate([
              _MatchCard(match: match),
              const SizedBox(height: 12),
              Wrap(
                spacing: 8,
                runSpacing: 8,
                children: [
                  _TagPill(
                    label: '${recipe.prepMinutes} min',
                    fill: SfColors.chipBgLight,
                  ),
                  _TagPill(
                    label: '${recipe.kcal} kcal',
                    fill: SfColors.chipBgLight,
                  ),
                  _TagPill(
                    label: '${recipe.matchPercent}% match',
                    fill: const Color(0xFFEAF6EC),
                    fg: SfColors.matchGreen,
                  ),
                ],
              ),
              const SizedBox(height: 16),
              if (recipe.description != null &&
                  recipe.description!.trim().isNotEmpty) ...[
                Text(
                  recipe.description!.trim(),
                  style: TextStyle(
                    fontSize: 14,
                    height: 1.45,
                    color: SfColors.brownLabel.withValues(alpha: 0.9),
                  ),
                ),
                const SizedBox(height: 16),
              ],
              const Text(
                'Ingredients',
                style: TextStyle(
                  fontWeight: FontWeight.w800,
                  color: SfColors.brown,
                ),
              ),
              const SizedBox(height: 8),
              ...ingredients.map(
                    (item) => ListTile(
                  dense: true,
                  contentPadding: EdgeInsets.zero,
                  leading: Icon(
                    item.inFridge
                        ? Icons.check_circle
                        : Icons.radio_button_unchecked,
                    color: item.inFridge
                        ? SfColors.matchGreen
                        : SfColors.brownMuted,
                  ),
                  title: Text(item.name),
                  trailing: Text(item.amount),
                ),
              ),
              const SizedBox(height: 20),
              SizedBox(
                width: double.infinity,
                child: FilledButton.tonal(
                  style: FilledButton.styleFrom(
                    foregroundColor: SfColors.brown,
                    padding: const EdgeInsets.symmetric(vertical: 14),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(SfRadii.pill),
                    ),
                  ),
                  onPressed: addingGrocery ? null : onAddMissingToGrocery,
                  child: addingGrocery
                      ? const SizedBox(
                    height: 22,
                    width: 22,
                    child: CircularProgressIndicator(strokeWidth: 2),
                  )
                      : const Text(
                    'Add missing to grocery',
                    style: TextStyle(fontWeight: FontWeight.w700),
                  ),
                ),
              ),
            ]),
          ),
        ),
      ],
    );
  }
}

class _IngredientItem {
  const _IngredientItem({
    required this.name,
    required this.amount,
    required this.inFridge,
  });

  final String name;
  final String amount;
  final bool inFridge;
}

class _InfoMiniCard extends StatelessWidget {
  const _InfoMiniCard({
    required this.value,
    required this.unit,
    required this.label,
  });

  final String value;
  final String unit;
  final String label;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.fromLTRB(12, 10, 12, 8),
      decoration: BoxDecoration(
        color: SfColors.whiteCard,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: const Color(0xFFECE3D8)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          RichText(
            text: TextSpan(
              text: value,
              style: const TextStyle(
                color: SfColors.brown,
                fontWeight: FontWeight.w800,
                fontSize: 33,
                height: 1,
              ),
              children: [
                TextSpan(
                  text: ' $unit',
                  style: const TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.w600,
                    color: SfColors.brownMuted,
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 2),
          Text(
            label,
            style: const TextStyle(
              fontSize: 12,
              color: SfColors.brownMuted,
            ),
          ),
        ],
      ),
    );
  }
}

class _MatchCard extends StatelessWidget {
  const _MatchCard({required this.match});

  final int match;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.fromLTRB(12, 10, 12, 10),
      decoration: BoxDecoration(
        color: SfColors.chipBgLight.withValues(alpha: 0.7),
        borderRadius: BorderRadius.circular(12),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          RichText(
            text: TextSpan(
              text: '$match',
              style: const TextStyle(
                color: SfColors.brown,
                fontWeight: FontWeight.w800,
                fontSize: 32,
                height: 1,
              ),
              children: const [
                TextSpan(
                  text: '%',
                  style: TextStyle(
                    fontSize: 14,
                    fontWeight: FontWeight.w700,
                    color: SfColors.brownMuted,
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 3),
          const Text(
            'Ingredient match — all essentials in fridge',
            style: TextStyle(fontSize: 12, color: SfColors.brownMuted),
          ),
        ],
      ),
    );
  }
}

class _TagPill extends StatelessWidget {
  const _TagPill({
    required this.label,
    required this.fill,
    this.fg = SfColors.brown,
  });

  final String label;
  final Color fill;
  final Color fg;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 9, vertical: 4),
      decoration: BoxDecoration(
        color: fill,
        borderRadius: BorderRadius.circular(SfRadii.pill),
      ),
      child: Text(
        label,
        style: TextStyle(
          fontSize: 11,
          fontWeight: FontWeight.w700,
          color: fg,
        ),
      ),
    );
  }
}

class _ReviewPill extends StatelessWidget {
  const _ReviewPill({required this.rating, required this.reviews});

  final double rating;
  final int reviews;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      decoration: BoxDecoration(
        color: SfColors.whiteCard,
        borderRadius: BorderRadius.circular(SfRadii.pill),
        border: Border.all(color: const Color(0xFFEEE5DA)),
      ),
      child: Row(
        children: [
          ...List.generate(5, (i) {
            final on = i + 1 <= rating.round();
            return Icon(
              on ? Icons.star : Icons.star_border,
              size: 14,
              color: const Color(0xFFFFB300),
            );
          }),
          const SizedBox(width: 6),
          Text(
            rating.toStringAsFixed(1),
            style: const TextStyle(
              fontSize: 12,
              fontWeight: FontWeight.w800,
              color: SfColors.brown,
            ),
          ),
          const SizedBox(width: 4),
          Text(
            '$reviews reviews',
            style: const TextStyle(
              fontSize: 11,
              color: SfColors.brownMuted,
            ),
          ),
        ],
      ),
    );
  }
}

class _TimelinePill extends StatelessWidget {
  const _TimelinePill({
    required this.prep,
    required this.cook,
    required this.total,
    required this.serves,
  });

  final int prep;
  final int cook;
  final int total;
  final int serves;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 8),
      decoration: BoxDecoration(
        color: SfColors.whiteCard,
        borderRadius: BorderRadius.circular(14),
        border: Border.all(color: const Color(0xFFEEE5DA)),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          _TimeBlock(title: 'Prep', value: '$prep min'),
          _divider(),
          _TimeBlock(title: 'Cook', value: '$cook min'),
          _divider(),
          _TimeBlock(title: 'Total', value: '$total min'),
          _divider(),
          _TimeBlock(title: 'Serves', value: '$serves people'),
        ],
      ),
    );
  }

  Widget _divider() {
    return Container(
      width: 1,
      height: 24,
      margin: const EdgeInsets.symmetric(horizontal: 8),
      color: const Color(0xFFE9E0D4),
    );
  }
}

class _TimeBlock extends StatelessWidget {
  const _TimeBlock({required this.title, required this.value});

  final String title;
  final String value;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          value,
          style: const TextStyle(
            color: SfColors.brown,
            fontSize: 13,
            fontWeight: FontWeight.w800,
          ),
        ),
        Text(
          title,
          style: const TextStyle(
            color: SfColors.brownMuted,
            fontSize: 10,
            fontWeight: FontWeight.w600,
          ),
        ),
      ],
    );
  }
}

class _CircleIconButton extends StatelessWidget {
  const _CircleIconButton({required this.icon, this.onTap});

  final IconData icon;
  final VoidCallback? onTap;

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(999),
      child: Container(
        width: 34,
        height: 34,
        decoration: BoxDecoration(
          color: SfColors.whiteCard,
          shape: BoxShape.circle,
          border: Border.all(color: const Color(0xFFEEE5DA)),
        ),
        child: Icon(icon, size: 18, color: SfColors.brown),
      ),
    );
  }
}

class _IngredientTile extends StatelessWidget {
  const _IngredientTile({required this.item, required this.kcal});

  final _IngredientItem item;
  final int kcal;

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: const BoxDecoration(
        color: SfColors.whiteCard,
        border: Border(
          top: BorderSide(color: Color(0xFFECE2D7)),
          right: BorderSide(color: Color(0xFFECE2D7)),
        ),
      ),
      padding: const EdgeInsets.fromLTRB(14, 10, 14, 14),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Align(
            alignment: Alignment.center,
            child: Icon(
              Icons.circle,
              size: 6,
              color: item.inFridge ? SfColors.matchGreen : SfColors.chipBgLight,
            ),
          ),
          const SizedBox(height: 10),
          Text(
            item.name,
            style: const TextStyle(
              color: SfColors.brown,
              fontWeight: FontWeight.w800,
              fontSize: 22,
              height: 1,
            ),
          ),
          const SizedBox(height: 4),
          Text(
            item.inFridge ? 'In fridge' : 'Need to buy',
            style: const TextStyle(
              color: SfColors.brownMuted,
              fontSize: 11,
            ),
          ),
          const Spacer(),
          Center(
            child: Container(
              width: 124,
              height: 124,
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                border: Border.all(color: const Color(0xFFEEE4D8), width: 2),
              ),
            ),
          ),
          const Spacer(),
          Center(
            child: Text(
              '${item.amount} · $kcal kcal',
              style: const TextStyle(
                color: SfColors.brownMuted,
                fontSize: 12,
              ),
            ),
          ),
        ],
      ),
    );
  }
}