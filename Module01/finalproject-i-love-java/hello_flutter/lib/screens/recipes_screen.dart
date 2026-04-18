import 'package:flutter/material.dart';

import '../models/recipe_recommendation.dart';
import '../services/fridge_api_service.dart';
import '../theme/smart_fridge_tokens.dart';
import 'recipe_detail_screen.dart';

class RecipesScreen extends StatefulWidget {
  const RecipesScreen({super.key, this.useLiveApi = true});

  final bool useLiveApi;

  @override
  State<RecipesScreen> createState() => _RecipesScreenState();
}

class _RecipesScreenState extends State<RecipesScreen> {
  final TextEditingController _search = TextEditingController();

  RecipeMealSlot _slot = RecipeMealSlot.all;
  RecipeSubFilter _sub = RecipeSubFilter.all;
  final Set<String> _savedIds = {};

  late Future<List<RecipeRecommendation>> _recipesFuture;

  static List<RecipeRecommendation> _buildCatalog() {
    const base = <RecipeRecommendation>[
      RecipeRecommendation(
        id: 'b0',
        title: 'Scrambled Eggs Florentine',
        matchPercent: 94,
        rating: 4.8,
        ingredientTags: ['Eggs', 'Spinach', 'Cheese'],
        prepMinutes: 10,
        kcal: 280,
        slot: RecipeMealSlot.breakfast,
      ),
      RecipeRecommendation(
        id: 'b1',
        title: 'Green Power Smoothie',
        matchPercent: 88,
        rating: 4.6,
        ingredientTags: ['Spinach', 'Banana', 'Yogurt'],
        prepMinutes: 5,
        kcal: 190,
        slot: RecipeMealSlot.breakfast,
      ),
      RecipeRecommendation(
        id: 'l0',
        title: 'Mediterranean Grain Bowl',
        matchPercent: 91,
        rating: 4.7,
        ingredientTags: ['Chickpeas', 'Tomato', 'Rice'],
        prepMinutes: 18,
        kcal: 420,
        slot: RecipeMealSlot.lunch,
      ),
      RecipeRecommendation(
        id: 'd0',
        title: 'Herb Roasted Chicken',
        matchPercent: 86,
        rating: 4.9,
        ingredientTags: ['Chicken', 'Potato', 'Rosemary'],
        prepMinutes: 45,
        kcal: 560,
        slot: RecipeMealSlot.dinner,
      ),
      RecipeRecommendation(
        id: 's0',
        title: 'Garden Salad & Feta',
        matchPercent: 92,
        rating: 4.5,
        ingredientTags: ['Lettuce', 'Feta', 'Tomato'],
        prepMinutes: 12,
        kcal: 220,
        slot: RecipeMealSlot.salad,
      ),
      RecipeRecommendation(
        id: 'de0',
        title: 'Berry Yogurt Parfait',
        matchPercent: 89,
        rating: 4.4,
        ingredientTags: ['Yogurt', 'Berries', 'Honey'],
        prepMinutes: 8,
        kcal: 210,
        slot: RecipeMealSlot.dessert,
      ),
      RecipeRecommendation(
        id: 'so0',
        title: 'Tomato Basil Soup',
        matchPercent: 90,
        rating: 4.7,
        ingredientTags: ['Tomato', 'Basil', 'Cream'],
        prepMinutes: 25,
        kcal: 180,
        slot: RecipeMealSlot.soup,
      ),
      RecipeRecommendation(
        id: 'd1',
        title: 'One-Pan Salmon',
        matchPercent: 87,
        rating: 4.8,
        ingredientTags: ['Salmon', 'Lemon', 'Asparagus'],
        prepMinutes: 22,
        kcal: 480,
        slot: RecipeMealSlot.dinner,
      ),
    ];

    return List<RecipeRecommendation>.generate(24, (i) {
      final b = base[i % base.length];
      return RecipeRecommendation(
        id: 'r$i',
        title: i < base.length ? b.title : '${b.title} ${i + 1}',
        matchPercent: (b.matchPercent - (i % 5)).clamp(72, 98),
        rating: (b.rating - (i % 4) * 0.05).clamp(3.6, 5.0),
        ingredientTags: b.ingredientTags,
        prepMinutes: b.prepMinutes + (i % 6),
        kcal: b.kcal + (i % 8) * 12,
        slot: b.slot,
      );
    });
  }

  @override
  void initState() {
    super.initState();

    if (widget.useLiveApi) {
      _recipesFuture = FridgeApiService.instance.fetchRecommendations();
    } else {
      _recipesFuture = Future<List<RecipeRecommendation>>.value(_buildCatalog());
    }

    _search.addListener(() {
      setState(() {});
    });
  }

  @override
  void dispose() {
    _search.dispose();
    super.dispose();
  }

  Future<void> _refreshRecipes() async {
    if (!widget.useLiveApi) {
      setState(() {
        _recipesFuture =
        Future<List<RecipeRecommendation>>.value(_buildCatalog());
      });
      return;
    }

    setState(() {
      _recipesFuture = FridgeApiService.instance.fetchRecommendations();
    });

    await _recipesFuture;
  }

  List<RecipeRecommendation> _filtered(List<RecipeRecommendation> source) {
    Iterable<RecipeRecommendation> q = source;

    final term = _search.text.trim().toLowerCase();
    if (term.isNotEmpty) {
      q = q.where((e) => e.title.toLowerCase().contains(term));
    }

    if (_slot != RecipeMealSlot.all) {
      q = q.where((e) => e.slot == _slot);
    }

    switch (_sub) {
      case RecipeSubFilter.all:
        break;
      case RecipeSubFilter.highMatch:
        q = q.where((e) => e.matchPercent >= 85);
        break;
      case RecipeSubFilter.quickMeals:
        q = q.where((e) => e.prepMinutes <= 15);
        break;
    }

    final list = q.toList();
    list.sort((a, b) => b.matchPercent.compareTo(a.matchPercent));
    return list;
  }

  Future<void> _openRecipe(RecipeRecommendation recipe) async {
    if (!widget.useLiveApi) {
      final added = await Navigator.of(context).push<bool>(
        MaterialPageRoute<bool>(
          builder: (_) => RecipeDetailScreen(recipe: recipe),
        ),
      );

      if (!mounted) {
        return;
      }

      if (added == true) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Missing ingredients were added to your grocery list.'),
            behavior: SnackBarBehavior.floating,
          ),
        );
      }
      return;
    }

    showDialog<void>(
      context: context,
      barrierDismissible: false,
      builder: (ctx) {
        return const Center(
          child: Card(
            child: Padding(
              padding: EdgeInsets.all(24),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  CircularProgressIndicator(),
                  SizedBox(height: 16),
                  Text('Loading recipe…'),
                ],
              ),
            ),
          ),
        );
      },
    );

    final detail =
    await FridgeApiService.instance.fetchRecipeDetail(recipe.id);

    if (!mounted) {
      return;
    }

    Navigator.of(context).pop();

    if (detail == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text(
            'Could not load recipe details. Is ./gradlew bootRun running?',
          ),
          behavior: SnackBarBehavior.floating,
        ),
      );
      return;
    }

    final added = await Navigator.of(context).push<bool>(
      MaterialPageRoute<bool>(
        builder: (_) => RecipeDetailScreen(
          recipe: recipe,
          detailJson: detail,
        ),
      ),
    );

    if (!mounted) {
      return;
    }

    if (added == true) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Missing ingredients were added to your grocery list.'),
          behavior: SnackBarBehavior.floating,
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<RecipeRecommendation>>(
      future: _recipesFuture,
      builder: (context, snapshot) {
        List<RecipeRecommendation> catalog = <RecipeRecommendation>[];
        String? apiError;
        bool loading = snapshot.connectionState == ConnectionState.waiting;

        if (snapshot.hasData) {
          catalog = snapshot.data!;
        } else if (snapshot.hasError) {
          catalog = _buildCatalog();
          apiError = '${snapshot.error}';
        }

        final filtered = _filtered(catalog);
        final total = catalog.length;
        final wide = MediaQuery.sizeOf(context).width >= 720;
        final subtitle =
            'Matched to your fridge · ${filtered.length} recipes found';

        final body = ColoredBox(
          color: SfColors.cream,
          child: CustomScrollView(
            physics: const AlwaysScrollableScrollPhysics(),
            slivers: [
              if (widget.useLiveApi && loading)
                const SliverToBoxAdapter(
                  child: Padding(
                    padding: EdgeInsets.only(top: 8),
                    child: LinearProgressIndicator(minHeight: 3),
                  ),
                ),
              if (apiError != null && widget.useLiveApi)
                SliverToBoxAdapter(
                  child: Padding(
                    padding: const EdgeInsets.fromLTRB(24, 12, 24, 0),
                    child: Material(
                      color: Colors.orange.shade50,
                      borderRadius: BorderRadius.circular(12),
                      child: Padding(
                        padding: const EdgeInsets.all(12),
                        child: Text(
                          'API unavailable — showing demo recipes. '
                              './gradlew bootRun · $apiError',
                          style: TextStyle(
                            color: Colors.orange.shade900,
                            fontSize: 13,
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
              SliverPadding(
                padding: const EdgeInsets.fromLTRB(28, 28, 28, 8),
                sliver: SliverToBoxAdapter(
                  child: wide
                      ? Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              'Recommended Recipes',
                              style: TextStyle(
                                fontSize: 32,
                                fontWeight: FontWeight.w800,
                                color: SfColors.brown,
                                height: 1.1,
                              ),
                            ),
                            const SizedBox(height: 8),
                            Text(
                              subtitle,
                              style: TextStyle(
                                fontSize: 15,
                                color: SfColors.brownMuted,
                                fontWeight: FontWeight.w500,
                              ),
                            ),
                          ],
                        ),
                      ),
                      SizedBox(
                        width: 420,
                        child: _RecipeSearchRow(controller: _search),
                      ),
                    ],
                  )
                      : Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'Recommended Recipes',
                        style: TextStyle(
                          fontSize: 28,
                          fontWeight: FontWeight.w800,
                          color: SfColors.brown,
                        ),
                      ),
                      const SizedBox(height: 8),
                      Text(
                        subtitle,
                        style: TextStyle(
                          fontSize: 14,
                          color: SfColors.brownMuted,
                        ),
                      ),
                      const SizedBox(height: 16),
                      _RecipeSearchRow(controller: _search),
                    ],
                  ),
                ),
              ),
              SliverToBoxAdapter(
                child: _MealSlotStrip(
                  selected: _slot,
                  onSelect: (slot) {
                    setState(() {
                      _slot = slot;
                    });
                  },
                ),
              ),
              SliverPadding(
                padding: const EdgeInsets.fromLTRB(28, 16, 28, 8),
                sliver: SliverToBoxAdapter(
                  child: LayoutBuilder(
                    builder: (context, constraints) {
                      final chips = Wrap(
                        spacing: 10,
                        runSpacing: 10,
                        children: RecipeSubFilter.values.map((filter) {
                          final selected = _sub == filter;
                          return FilterChip(
                            label: Text(filter.label),
                            selected: selected,
                            onSelected: (_) {
                              setState(() {
                                _sub = filter;
                              });
                            },
                            showCheckmark: false,
                            selectedColor: SfColors.brown,
                            checkmarkColor: Colors.white,
                            labelStyle: TextStyle(
                              color: selected ? Colors.white : SfColors.brown,
                              fontWeight: FontWeight.w700,
                              fontSize: 13,
                            ),
                            backgroundColor: SfColors.whiteCard,
                            side: BorderSide(
                              color: selected
                                  ? SfColors.brown
                                  : SfColors.chipBgLight,
                            ),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(SfRadii.pill),
                            ),
                          );
                        }).toList(),
                      );

                      final countLabel = Text(
                        'Showing ${filtered.length} of $total recipes',
                        style: TextStyle(
                          fontSize: 13,
                          fontWeight: FontWeight.w600,
                          color: SfColors.brownMuted,
                        ),
                      );

                      if (constraints.maxWidth < 520) {
                        return Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            chips,
                            const SizedBox(height: 10),
                            Align(
                              alignment: Alignment.centerRight,
                              child: countLabel,
                            ),
                          ],
                        );
                      }

                      return Row(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Expanded(child: chips),
                          countLabel,
                        ],
                      );
                    },
                  ),
                ),
              ),
              SliverPadding(
                padding: const EdgeInsets.fromLTRB(20, 8, 20, 40),
                sliver: SliverLayoutBuilder(
                  builder: (context, constraints) {
                    final width = constraints.crossAxisExtent;
                    final columns = width >= 1100
                        ? 3
                        : width >= 720
                        ? 2
                        : 1;

                    return SliverGrid(
                      gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                        crossAxisCount: columns,
                        mainAxisSpacing: 18,
                        crossAxisSpacing: 18,
                        mainAxisExtent: 312,
                      ),
                      delegate: SliverChildBuilderDelegate(
                            (context, index) {
                          final recipe = filtered[index];
                          final saved = _savedIds.contains(recipe.id);

                          return _RecipeCard(
                            recipe: recipe,
                            saved: saved,
                            onToggleSave: () {
                              setState(() {
                                if (saved) {
                                  _savedIds.remove(recipe.id);
                                } else {
                                  _savedIds.add(recipe.id);
                                }
                              });
                            },
                            onOpen: () => _openRecipe(recipe),
                          );
                        },
                        childCount: filtered.length,
                      ),
                    );
                  },
                ),
              ),
            ],
          ),
        );

        return Scaffold(
          backgroundColor: SfColors.cream,
          body: RefreshIndicator(
            onRefresh: _refreshRecipes,
            child: body,
          ),
        );
      },
    );
  }
}

class _RecipeSearchRow extends StatelessWidget {
  const _RecipeSearchRow({required this.controller});

  final TextEditingController controller;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Expanded(
          child: TextField(
            controller: controller,
            decoration: InputDecoration(
              hintText: 'find what you want',
              prefixIcon: Icon(Icons.search, color: SfColors.brownMuted),
              filled: true,
              fillColor: SfColors.whiteCard,
              contentPadding: const EdgeInsets.symmetric(vertical: 14),
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(SfRadii.pill),
                borderSide: BorderSide.none,
              ),
            ),
          ),
        ),
        const SizedBox(width: 12),
        FilledButton(
          style: FilledButton.styleFrom(
            backgroundColor: SfColors.brown,
            padding: const EdgeInsets.symmetric(horizontal: 22, vertical: 16),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(SfRadii.pill),
            ),
          ),
          onPressed: () => FocusScope.of(context).unfocus(),
          child: const Text('search'),
        ),
        const SizedBox(width: 12),
        CircleAvatar(
          radius: 24,
          backgroundColor: SfColors.chipBgLight,
          child: Icon(Icons.person, color: SfColors.brownMuted),
        ),
      ],
    );
  }
}

class _MealSlotStrip extends StatelessWidget {
  const _MealSlotStrip({
    required this.selected,
    required this.onSelect,
  });

  final RecipeMealSlot selected;
  final ValueChanged<RecipeMealSlot> onSelect;

  @override
  Widget build(BuildContext context) {
    const order = <RecipeMealSlot>[
      RecipeMealSlot.all,
      RecipeMealSlot.breakfast,
      RecipeMealSlot.lunch,
      RecipeMealSlot.dinner,
      RecipeMealSlot.salad,
      RecipeMealSlot.dessert,
      RecipeMealSlot.soup,
    ];

    return SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      padding: const EdgeInsets.symmetric(horizontal: 24),
      child: Row(
        children: [
          for (final slot in order)
            Padding(
              padding: const EdgeInsets.only(right: 14),
              child: _SlotOrb(
                slot: slot,
                selected: selected == slot,
                onTap: () => onSelect(slot),
              ),
            ),
        ],
      ),
    );
  }
}

class _SlotOrb extends StatelessWidget {
  const _SlotOrb({
    required this.slot,
    required this.selected,
    required this.onTap,
  });

  final RecipeMealSlot slot;
  final bool selected;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    final brown = SfColors.brown;

    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(999),
      child: Column(
        children: [
          Container(
            width: 56,
            height: 56,
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              color: selected ? brown : SfColors.whiteCard,
              boxShadow: [
                if (!selected)
                  BoxShadow(
                    color: Colors.black.withValues(alpha: 0.05),
                    blurRadius: 8,
                    offset: const Offset(0, 3),
                  ),
              ],
            ),
            child: Icon(
              slot.icon,
              color: selected ? Colors.white : brown,
              size: 26,
            ),
          ),
          const SizedBox(height: 6),
          Text(
            slot.label,
            style: TextStyle(
              fontSize: 11,
              fontWeight: FontWeight.w700,
              color: selected ? brown : SfColors.brownMuted,
            ),
          ),
        ],
      ),
    );
  }
}

class _RecipeCard extends StatelessWidget {
  const _RecipeCard({
    required this.recipe,
    required this.saved,
    required this.onToggleSave,
    required this.onOpen,
  });

  final RecipeRecommendation recipe;
  final bool saved;
  final VoidCallback onToggleSave;
  final VoidCallback onOpen;

  @override
  Widget build(BuildContext context) {
    return Material(
      color: Colors.transparent,
      child: InkWell(
        onTap: onOpen,
        borderRadius: BorderRadius.circular(SfRadii.card),
        child: Ink(
          decoration: BoxDecoration(
            color: SfColors.whiteCard,
            borderRadius: BorderRadius.circular(SfRadii.card),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withValues(alpha: 0.06),
                blurRadius: 14,
                offset: const Offset(0, 6),
              ),
            ],
          ),
          child: Stack(
            children: [
              Positioned(
                top: 12,
                left: 12,
                child: Text(
                  '${recipe.matchPercent}% match',
                  style: const TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.w800,
                    color: SfColors.matchGreen,
                  ),
                ),
              ),
              Positioned(
                top: 8,
                right: 8,
                child: IconButton(
                  icon: Icon(
                    saved ? Icons.bookmark : Icons.bookmark_border,
                    color: SfColors.brown,
                  ),
                  onPressed: onToggleSave,
                ),
              ),
              Padding(
                padding: const EdgeInsets.fromLTRB(16, 40, 16, 14),
                child: Column(
                  children: [
                    Container(
                      width: 88,
                      height: 88,
                      decoration: BoxDecoration(
                        color: SfColors.chipBgLight,
                        shape: BoxShape.circle,
                      ),
                      child: Icon(
                        recipe.slot.icon,
                        size: 36,
                        color: SfColors.brownMuted,
                      ),
                    ),
                    const SizedBox(height: 10),
                    Text(
                      recipe.title,
                      maxLines: 2,
                      overflow: TextOverflow.ellipsis,
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontWeight: FontWeight.w800,
                        fontSize: 15,
                        color: SfColors.brown,
                        height: 1.2,
                      ),
                    ),
                    const SizedBox(height: 6),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        ...List.generate(5, (i) {
                          final value = recipe.rating;
                          final starOn = i + 1 <= value.round();

                          return Icon(
                            starOn ? Icons.star : Icons.star_border,
                            size: 15,
                            color: const Color(0xFFFFB300),
                          );
                        }),
                        const SizedBox(width: 6),
                        Text(
                          recipe.rating.toStringAsFixed(1),
                          style: TextStyle(
                            fontSize: 12,
                            fontWeight: FontWeight.w700,
                            color: SfColors.brownLabel,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 8),
                    Wrap(
                      alignment: WrapAlignment.center,
                      spacing: 6,
                      runSpacing: 6,
                      children: recipe.ingredientTags.take(3).map((tag) {
                        return Container(
                          padding: const EdgeInsets.symmetric(
                            horizontal: 8,
                            vertical: 4,
                          ),
                          decoration: BoxDecoration(
                            color: SfColors.chipBgLight,
                            borderRadius: BorderRadius.circular(SfRadii.pill),
                          ),
                          child: Text(
                            tag,
                            style: TextStyle(
                              fontSize: 10,
                              fontWeight: FontWeight.w600,
                              color: SfColors.brown,
                            ),
                          ),
                        );
                      }).toList(),
                    ),
                    const Spacer(),
                    Row(
                      children: [
                        Text(
                          '${recipe.prepMinutes} min',
                          style: TextStyle(
                            fontSize: 12,
                            fontWeight: FontWeight.w600,
                            color: SfColors.brownMuted,
                          ),
                        ),
                        const SizedBox(width: 10),
                        Text(
                          '${recipe.kcal} kcal',
                          style: TextStyle(
                            fontSize: 12,
                            fontWeight: FontWeight.w600,
                            color: SfColors.brownMuted,
                          ),
                        ),
                        const Spacer(),
                        Icon(
                          Icons.chevron_right,
                          color: SfColors.brownMuted,
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
