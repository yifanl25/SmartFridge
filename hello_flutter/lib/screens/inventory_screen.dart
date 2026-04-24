// ignore_for_file: slash_for_doc_comments

import 'dart:async';

import 'package:flutter/material.dart';

import '../models/inventory_models.dart';
import '../services/fridge_api_service.dart';
import '../theme/smart_fridge_tokens.dart';

/**
 * Inventory screen.
 * <p>
 * Main fridge page with search, filters, sorting, and item cards.
 * Supports live API mode and local demo mode.
 *
 * Official references:
 * StatefulWidget:
 * https://api.flutter.dev/flutter/widgets/StatefulWidget-class.html
 * State:
 * https://api.flutter.dev/flutter/widgets/State-class.html
 * Scaffold:
 * https://api.flutter.dev/flutter/material/Scaffold-class.html
 * RefreshIndicator:
 * https://api.flutter.dev/flutter/material/RefreshIndicator-class.html
 * CustomScrollView:
 * https://api.flutter.dev/flutter/widgets/CustomScrollView-class.html
 * SliverGrid:
 * https://api.flutter.dev/flutter/widgets/SliverGrid-class.html
 *
 * Open-source reference:
 * Flutter samples:
 * https://github.com/flutter/samples
 * material_3_demo:
 * https://github.com/flutter/samples/tree/main/material_3_demo
 */
class InventoryScreen extends StatefulWidget {
  const InventoryScreen({super.key, this.useLiveApi = true});

  final bool useLiveApi;

  @override
  State<InventoryScreen> createState() => _InventoryScreenState();
}

/**
 * State for InventoryScreen.
 * <p>
 * Stores search text, selected category, active filters, sort mode,
 * loading state, API error state, and inventory data.
 */
class _InventoryScreenState extends State<InventoryScreen> {
  final TextEditingController _search = TextEditingController();

  FoodCategory _category = FoodCategory.all;
  final Set<_FilterTag> _activeFilters = {
    _FilterTag.expiry,
    _FilterTag.newItems,
  };
  InventorySort _sort = InventorySort.expiry;

  late List<InventoryItem> _items;
  bool _loading = false;
  String? _apiError;

  /**
   * Demo seed data.
   * <p>
   * Used in demo mode and as a fallback when the API is unavailable.
   */
  static List<InventoryItem> _seed() => [
    const InventoryItem(
      id: '1',
      name: 'Whole Milk',
      category: FoodCategory.dairy,
      quantityLabel: '1 L',
      daysLeft: 0,
      badge: ItemBadge.urgent,
    ),
    const InventoryItem(
      id: '2',
      name: 'Baby Spinach',
      category: FoodCategory.vegetables,
      quantityLabel: '150 g',
      daysLeft: 1,
      badge: ItemBadge.urgent,
    ),
    const InventoryItem(
      id: '3',
      name: 'Greek Yogurt',
      category: FoodCategory.dairy,
      quantityLabel: '500 g',
      daysLeft: 2,
      isNew: true,
      badge: ItemBadge.newItem,
    ),
    const InventoryItem(
      id: '4',
      name: 'Chicken Breast',
      category: FoodCategory.meat,
      quantityLabel: '400 g',
      daysLeft: 2,
    ),
    const InventoryItem(
      id: '5',
      name: 'Orange Juice',
      category: FoodCategory.beverages,
      quantityLabel: '1 L',
      daysLeft: 4,
    ),
    const InventoryItem(
      id: '6',
      name: 'Brown Rice',
      category: FoodCategory.grains,
      quantityLabel: '1 kg',
      daysLeft: 7,
      isNew: true,
      badge: ItemBadge.newItem,
    ),
    const InventoryItem(
      id: '7',
      name: 'Frozen Peas',
      category: FoodCategory.frozen,
      quantityLabel: '400 g',
      daysLeft: 14,
    ),
    const InventoryItem(
      id: '8',
      name: 'Apples',
      category: FoodCategory.fruits,
      quantityLabel: '6 pcs',
      daysLeft: 5,
    ),
  ];

  @override
  void initState() {
    super.initState();
    _items = widget.useLiveApi
        ? <InventoryItem>[]
        : List<InventoryItem>.from(_seed());
    _search.addListener(() => setState(() {}));
    if (widget.useLiveApi) {
      _reloadFromApi();
    }
  }

  /**
   * Reload inventory from API.
   * <p>
   * Loads inventory from the backend.
   * On failure, stores the error and falls back to demo data.
   */
  Future<void> _reloadFromApi() async {
    setState(() {
      _loading = true;
      _apiError = null;
    });
    try {
      final list = await FridgeApiService.instance.fetchInventory();
      if (!mounted) return;
      setState(() {
        _items = list;
        _loading = false;
      });
    } catch (e) {
      if (!mounted) return;
      setState(() {
        _loading = false;
        _apiError = '$e';
        _items = List<InventoryItem>.from(_seed());
      });
    }
  }

  @override
  void dispose() {
    _search.dispose();
    super.dispose();
  }

  /**
   * Filtered and sorted inventory list.
   * <p>
   * Applies search, category, filters, and sorting to the current items.
   */
  List<InventoryItem> get _filtered {
    Iterable<InventoryItem> q = _items;

    final term = _search.text.trim().toLowerCase();
    if (term.isNotEmpty) {
      q = q.where((e) => e.name.toLowerCase().contains(term));
    }

    if (_category != FoodCategory.all) {
      q = q.where((e) => e.category == _category);
    }

    if (_activeFilters.isNotEmpty) {
      q = q.where((e) {
        var ok = false;
        if (_activeFilters.contains(_FilterTag.expiry)) {
          ok = ok || e.daysLeft <= 3;
        }
        if (_activeFilters.contains(_FilterTag.newItems)) {
          ok = ok || e.isNew;
        }
        return ok;
      });
    }

    final list = q.toList();
    switch (_sort) {
      case InventorySort.expiry:
        list.sort((a, b) => a.daysLeft.compareTo(b.daysLeft));
      case InventorySort.name:
        list.sort(
              (a, b) => a.name.toLowerCase().compareTo(b.name.toLowerCase()),
        );
    }
    return list;
  }

  /**
   * Expiring soon count.
   * <p>
   * Counts items with 3 days or less remaining.
   */
  int get _expiringSoonCount => _items.where((e) => e.daysLeft <= 3).length;

  /**
   * Open add-item flow.
   * <p>
   * In live mode, opens the catalog dialog and adds the selected item through the API.
   * In demo mode, opens a simple text input and appends a local item.
   */
  Future<void> _openAddItem() async {
    if (widget.useLiveApi) {
      final name = await showDialog<String>(
        context: context,
        builder: (ctx) => const _CatalogAddDialog(),
      );
      if (name == null || name.trim().isEmpty || !mounted) return;
      try {
        final added =
        await FridgeApiService.instance.addInventoryItem(name.trim());
        if (!mounted) return;
        setState(() {
          _items = [..._items, added];
        });
      } catch (e) {
        if (!mounted) return;
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Add failed: $e')),
        );
      }
      return;
    }

    final nameCtrl = TextEditingController();
    showDialog<void>(
      context: context,
      builder: (ctx) => AlertDialog(
        backgroundColor: SfColors.whiteCard,
        title: Text(
          'Add item',
          style: TextStyle(
            color: SfColors.brown,
            fontWeight: FontWeight.w800,
          ),
        ),
        content: TextField(
          controller: nameCtrl,
          decoration: const InputDecoration(
            hintText: 'Item name',
            border: OutlineInputBorder(),
          ),
          textCapitalization: TextCapitalization.sentences,
          autofocus: true,
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx),
            child: Text(
              'Cancel',
              style: TextStyle(color: SfColors.brownLabel),
            ),
          ),
          FilledButton(
            onPressed: () {
              final name = nameCtrl.text.trim();
              if (name.isEmpty) return;
              setState(() {
                final cat = _category == FoodCategory.all
                    ? FoodCategory.dairy
                    : _category;
                _items = [
                  ..._items,
                  InventoryItem(
                    id: 'local-${DateTime.now().millisecondsSinceEpoch}',
                    name: name,
                    category: cat,
                    quantityLabel: '—',
                    daysLeft: 7,
                    isNew: true,
                    badge: ItemBadge.newItem,
                  ),
                ];
              });
              Navigator.pop(ctx);
            },
            style: FilledButton.styleFrom(backgroundColor: SfColors.brown),
            child: const Text('Add'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final filtered = _filtered;
    final wide = MediaQuery.sizeOf(context).width >= 700;

    final body = ColoredBox(
      color: SfColors.cream,
      child: CustomScrollView(
        physics: const AlwaysScrollableScrollPhysics(),
        slivers: [
          if (widget.useLiveApi && _loading)
            const SliverToBoxAdapter(
              child: Padding(
                padding: EdgeInsets.only(top: 8),
                child: LinearProgressIndicator(minHeight: 3),
              ),
            ),
          if (_apiError != null && widget.useLiveApi)
            SliverToBoxAdapter(
              child: Padding(
                padding: const EdgeInsets.fromLTRB(28, 12, 28, 0),
                child: Material(
                  color: Colors.orange.shade50,
                  borderRadius: BorderRadius.circular(12),
                  child: Padding(
                    padding: const EdgeInsets.all(12),
                    child: Text(
                      'API unavailable — showing demo data. '
                          'Start backend: ./gradlew bootRun. $_apiError',
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
            padding: const EdgeInsets.fromLTRB(28, 28, 28, 12),
            sliver: SliverToBoxAdapter(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  if (wide)
                    Row(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Expanded(
                          child: _HeaderTitle(
                            totalItems: _items.length,
                            expiringSoon: _expiringSoonCount,
                          ),
                        ),
                        const SizedBox(width: 24),
                        Expanded(child: _SearchBar(controller: _search)),
                      ],
                    )
                  else ...[
                    _HeaderTitle(
                      totalItems: _items.length,
                      expiringSoon: _expiringSoonCount,
                    ),
                    const SizedBox(height: 16),
                    _SearchBar(controller: _search),
                  ],
                ],
              ),
            ),
          ),
          SliverToBoxAdapter(
            child: _CategoryStrip(
              selected: _category,
              onSelect: (c) => setState(() => _category = c),
            ),
          ),
          SliverPadding(
            padding: const EdgeInsets.fromLTRB(28, 8, 28, 12),
            sliver: SliverToBoxAdapter(
              child: _FilterSortRow(
                resultCount: filtered.length,
                activeFilters: _activeFilters,
                onRemoveFilter: (f) =>
                    setState(() => _activeFilters.remove(f)),
                sort: _sort,
                onSort: (s) => setState(() => _sort = s),
              ),
            ),
          ),
          SliverPadding(
            padding: const EdgeInsets.fromLTRB(20, 0, 20, 40),
            sliver: SliverLayoutBuilder(
              builder: (context, constraints) {
                final w = constraints.crossAxisExtent;
                final cols = w >= 1100
                    ? 3
                    : w >= 720
                    ? 2
                    : 1;
                return SliverGrid(
                  gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                    crossAxisCount: cols,
                    mainAxisSpacing: 18,
                    crossAxisSpacing: 18,
                    mainAxisExtent: 268,
                  ),
                  delegate: SliverChildBuilderDelegate(
                        (context, index) {
                      if (index == 0) {
                        return _AddItemCard(onTap: _openAddItem);
                      }
                      final item = filtered[index - 1];
                      return _InventoryCard(item: item);
                    },
                    childCount: filtered.length + 1,
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
        onRefresh: () async {
          if (widget.useLiveApi) {
            await _reloadFromApi();
          }
        },
        child: body,
      ),
    );
  }
}

/**
 * Catalog add dialog.
 * <p>
 * Dialog for adding an item from the catalog.
 * Fetches name suggestions and returns the selected canonical name.
 *
 * Official references:
 * AlertDialog:
 * https://api.flutter.dev/flutter/material/AlertDialog-class.html
 * TextField:
 * https://api.flutter.dev/flutter/material/TextField-class.html
 * ListView.builder:
 * https://api.flutter.dev/flutter/widgets/ListView/ListView.builder.html
 *
 * Open-source reference:
 * material_3_demo:
 * https://github.com/flutter/samples/tree/main/material_3_demo
 */
class _CatalogAddDialog extends StatefulWidget {
  const _CatalogAddDialog();

  @override
  State<_CatalogAddDialog> createState() => _CatalogAddDialogState();
}

/**
 * State for _CatalogAddDialog.
 * <p>
 * Stores input text, debounce timer, suggestions, and loading state.
 */
class _CatalogAddDialogState extends State<_CatalogAddDialog> {
  final TextEditingController _ctrl = TextEditingController();
  Timer? _debounce;
  List<String> _suggestions = [];
  bool _loading = false;

  @override
  void initState() {
    super.initState();
    _ctrl.addListener(_onChanged);
  }

  /**
   * Debounced input listener.
   * <p>
   * Delays the suggestion request to avoid calling the API on every keystroke.
   */
  void _onChanged() {
    _debounce?.cancel();
    _debounce = Timer(const Duration(milliseconds: 320), _fetch);
  }

  /**
   * Fetch suggestion names from catalog API.
   * <p>
   * Loads catalog suggestions for the current query.
   */
  Future<void> _fetch() async {
    final q = _ctrl.text.trim();
    if (q.isEmpty) {
      if (mounted) setState(() => _suggestions = []);
      return;
    }
    setState(() => _loading = true);
    try {
      final list =
      await FridgeApiService.instance.fetchCatalogSuggestionNames(q);
      if (!mounted) return;
      setState(() {
        _suggestions = list;
        _loading = false;
      });
    } catch (_) {
      if (!mounted) return;
      setState(() {
        _suggestions = [];
        _loading = false;
      });
    }
  }

  @override
  void dispose() {
    _debounce?.cancel();
    _ctrl.removeListener(_onChanged);
    _ctrl.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      backgroundColor: SfColors.whiteCard,
      title: Text(
        'Add from catalog',
        style: TextStyle(
          color: SfColors.brown,
          fontWeight: FontWeight.w800,
        ),
      ),
      content: SizedBox(
        width: 360,
        height: 300,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            TextField(
              controller: _ctrl,
              decoration: const InputDecoration(
                hintText: 'Type a few letters (e.g. Mil)',
                border: OutlineInputBorder(),
              ),
              textCapitalization: TextCapitalization.sentences,
              autofocus: true,
            ),
            const SizedBox(height: 8),
            if (_loading)
              const LinearProgressIndicator(minHeight: 2)
            else
              Text(
                'Suggestions from server',
                style: TextStyle(
                  fontSize: 11,
                  color: SfColors.brownMuted,
                ),
              ),
            const SizedBox(height: 6),
            Expanded(
              child: ListView.builder(
                itemCount: _suggestions.length,
                itemBuilder: (context, i) {
                  final s = _suggestions[i];
                  return ListTile(
                    dense: true,
                    title: Text(
                      s,
                      style: TextStyle(color: SfColors.brown),
                    ),
                    onTap: () {
                      _ctrl.text = s;
                      setState(() => _suggestions = []);
                    },
                  );
                },
              ),
            ),
          ],
        ),
      ),
      actions: [
        TextButton(
          onPressed: () => Navigator.pop(context),
          child: Text(
            'Cancel',
            style: TextStyle(color: SfColors.brownLabel),
          ),
        ),
        FilledButton(
          onPressed: () {
            final n = _ctrl.text.trim();
            if (n.isEmpty) return;
            Navigator.pop(context, n);
          },
          style: FilledButton.styleFrom(backgroundColor: SfColors.brown),
          child: const Text('Add'),
        ),
      ],
    );
  }
}

enum _FilterTag { expiry, newItems }

enum InventorySort { expiry, name }

extension on _FilterTag {
  String get label {
    switch (this) {
      case _FilterTag.expiry:
        return 'expiry';
      case _FilterTag.newItems:
        return 'new';
    }
  }
}

/**
 * Header title block.
 * <p>
 * Shows the page title and the expiring summary.
 *
 * Official references:
 * Column:
 * https://api.flutter.dev/flutter/widgets/Column-class.html
 * Text:
 * https://api.flutter.dev/flutter/widgets/Text-class.html
 */
class _HeaderTitle extends StatelessWidget {
  const _HeaderTitle({
    required this.totalItems,
    required this.expiringSoon,
  });

  final int totalItems;
  final int expiringSoon;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          'My Fridge',
          style: TextStyle(
            fontSize: 32,
            fontWeight: FontWeight.w800,
            color: SfColors.brown,
            height: 1.1,
          ),
        ),
        const SizedBox(height: 8),
        Text(
          '$totalItems items tracked · $expiringSoon expiring soon',
          style: TextStyle(
            fontSize: 15,
            color: SfColors.brownMuted,
            fontWeight: FontWeight.w500,
          ),
        ),
      ],
    );
  }
}

/**
 * Search bar module.
 * <p>
 * Search field with a trailing action button.
 *
 * Official references:
 * TextField:
 * https://api.flutter.dev/flutter/material/TextField-class.html
 * FilledButton:
 * https://api.flutter.dev/flutter/material/FilledButton-class.html
 * InputDecoration:
 * https://api.flutter.dev/flutter/material/InputDecoration-class.html
 */
class _SearchBar extends StatelessWidget {
  const _SearchBar({required this.controller});

  final TextEditingController controller;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Expanded(
          child: TextField(
            controller: controller,
            decoration: InputDecoration(
              hintText: 'Search items...',
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
      ],
    );
  }
}

/**
 * Horizontal category strip.
 * <p>
 * Horizontal scroller for category selection.
 *
 * Official references:
 * SingleChildScrollView:
 * https://api.flutter.dev/flutter/widgets/SingleChildScrollView-class.html
 * Row:
 * https://api.flutter.dev/flutter/widgets/Row-class.html
 */
class _CategoryStrip extends StatelessWidget {
  const _CategoryStrip({
    required this.selected,
    required this.onSelect,
  });

  final FoodCategory selected;
  final ValueChanged<FoodCategory> onSelect;

  @override
  Widget build(BuildContext context) {
    const order = <FoodCategory>[
      FoodCategory.all,
      FoodCategory.vegetables,
      FoodCategory.fruits,
      FoodCategory.dairy,
      FoodCategory.meat,
      FoodCategory.beverages,
      FoodCategory.grains,
      FoodCategory.frozen,
    ];
    return SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      padding: const EdgeInsets.symmetric(horizontal: 24),
      child: Row(
        children: [
          for (final c in order) ...[
            Padding(
              padding: const EdgeInsets.only(right: 10),
              child: _CategoryChip(
                category: c,
                selected: selected == c,
                onTap: () => onSelect(c),
              ),
            ),
          ],
        ],
      ),
    );
  }
}

/**
 * Category chip.
 * <p>
 * Single category button with selected styling.
 *
 * Official references:
 * Material:
 * https://api.flutter.dev/flutter/material/Material-class.html
 * InkWell:
 * https://api.flutter.dev/flutter/material/InkWell-class.html
 * Ink:
 * https://api.flutter.dev/flutter/material/Ink-class.html
 */
class _CategoryChip extends StatelessWidget {
  const _CategoryChip({
    required this.category,
    required this.selected,
    required this.onTap,
  });

  final FoodCategory category;
  final bool selected;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    final brown = SfColors.brown;
    return Material(
      color: Colors.transparent,
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(16),
        child: Ink(
          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
          decoration: BoxDecoration(
            color: selected ? brown : SfColors.whiteCard,
            borderRadius: BorderRadius.circular(16),
            boxShadow: [
              if (!selected)
                BoxShadow(
                  color: Colors.black.withValues(alpha: 0.04),
                  blurRadius: 8,
                  offset: const Offset(0, 3),
                ),
            ],
          ),
          child: Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              Icon(
                category.icon,
                size: 22,
                color: selected ? Colors.white : brown,
              ),
              const SizedBox(width: 8),
              Text(
                category.label,
                style: TextStyle(
                  fontWeight: FontWeight.w700,
                  fontSize: 13,
                  color: selected ? Colors.white : brown,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

/**
 * Filter and sort row.
 * <p>
 * Shows the result summary, active filters, and sort control.
 * Stacks vertically on narrow screens.
 *
 * Official references:
 * LayoutBuilder:
 * https://api.flutter.dev/flutter/widgets/LayoutBuilder-class.html
 * Wrap:
 * https://api.flutter.dev/flutter/widgets/Wrap-class.html
 * DropdownButton:
 * https://api.flutter.dev/flutter/material/DropdownButton-class.html
 */
class _FilterSortRow extends StatelessWidget {
  const _FilterSortRow({
    required this.resultCount,
    required this.activeFilters,
    required this.onRemoveFilter,
    required this.sort,
    required this.onSort,
  });

  final int resultCount;
  final Set<_FilterTag> activeFilters;
  final ValueChanged<_FilterTag> onRemoveFilter;
  final InventorySort sort;
  final ValueChanged<InventorySort> onSort;

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
      builder: (context, c) {
        final narrow = c.maxWidth < 520;
        final row = Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Expanded(
              child: Text(
                'Browse Items · $resultCount results',
                style: TextStyle(
                  fontWeight: FontWeight.w600,
                  color: SfColors.brownLabel,
                  fontSize: 14,
                ),
              ),
            ),
            if (!narrow) ...[
              Wrap(
                spacing: 8,
                children: [
                  for (final f in activeFilters)
                    _RemovableChip(
                      label: f.label,
                      onRemove: () => onRemoveFilter(f),
                    ),
                ],
              ),
              const SizedBox(width: 12),
            ],
            _SortDropdown(value: sort, onChanged: onSort),
          ],
        );
        if (narrow) {
          return Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Browse Items · $resultCount results',
                style: TextStyle(
                  fontWeight: FontWeight.w600,
                  color: SfColors.brownLabel,
                  fontSize: 14,
                ),
              ),
              const SizedBox(height: 10),
              Wrap(
                spacing: 8,
                runSpacing: 8,
                children: [
                  for (final f in activeFilters)
                    _RemovableChip(
                      label: f.label,
                      onRemove: () => onRemoveFilter(f),
                    ),
                  _SortDropdown(value: sort, onChanged: onSort),
                ],
              ),
            ],
          );
        }
        return row;
      },
    );
  }
}

/**
 * Removable filter chip.
 * <p>
 * Active filter chip with a remove action.
 */
class _RemovableChip extends StatelessWidget {
  const _RemovableChip({required this.label, required this.onRemove});

  final String label;
  final VoidCallback onRemove;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
      decoration: BoxDecoration(
        color: SfColors.chipBgLight,
        borderRadius: BorderRadius.circular(SfRadii.pill),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Text(
            label,
            style: TextStyle(
              fontSize: 12,
              fontWeight: FontWeight.w600,
              color: SfColors.brown,
            ),
          ),
          const SizedBox(width: 4),
          InkWell(
            onTap: onRemove,
            child: Icon(Icons.close, size: 16, color: SfColors.brownMuted),
          ),
        ],
      ),
    );
  }
}

/**
 * Sort dropdown control.
 * <p>
 * Sort picker for expiry or name order.
 *
 * Official references:
 * DropdownButtonHideUnderline:
 * https://api.flutter.dev/flutter/material/DropdownButtonHideUnderline-class.html
 * DropdownButton:
 * https://api.flutter.dev/flutter/material/DropdownButton-class.html
 */
class _SortDropdown extends StatelessWidget {
  const _SortDropdown({required this.value, required this.onChanged});

  final InventorySort value;
  final ValueChanged<InventorySort> onChanged;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12),
      decoration: BoxDecoration(
        color: SfColors.whiteCard,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: SfColors.chipBgLight),
      ),
      child: DropdownButtonHideUnderline(
        child: DropdownButton<InventorySort>(
          value: value,
          icon: Icon(Icons.expand_more, color: SfColors.brown),
          items: const [
            DropdownMenuItem(
              value: InventorySort.expiry,
              child: Text('Sort by Expiry'),
            ),
            DropdownMenuItem(
              value: InventorySort.name,
              child: Text('Sort by Name'),
            ),
          ],
          onChanged: (v) {
            if (v != null) onChanged(v);
          },
        ),
      ),
    );
  }
}

/**
 * Add-item card.
 * <p>
 * Special first grid card used to start the add-item flow.
 *
 * Official references:
 * Material:
 * https://api.flutter.dev/flutter/material/Material-class.html
 * InkWell:
 * https://api.flutter.dev/flutter/material/InkWell-class.html
 * Ink:
 * https://api.flutter.dev/flutter/material/Ink-class.html
 */
class _AddItemCard extends StatelessWidget {
  const _AddItemCard({required this.onTap});

  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return Material(
      color: Colors.transparent,
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(SfRadii.card),
        child: Ink(
          decoration: BoxDecoration(
            color: SfColors.whiteCard,
            borderRadius: BorderRadius.circular(SfRadii.card),
            border: Border.all(color: SfColors.chipBgLight, width: 2),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withValues(alpha: 0.05),
                blurRadius: 12,
                offset: const Offset(0, 6),
              ),
            ],
          ),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(Icons.add, size: 44, color: SfColors.brownMuted),
              const SizedBox(height: 12),
              Text(
                'Add Item',
                style: TextStyle(
                  fontWeight: FontWeight.w800,
                  fontSize: 16,
                  color: SfColors.brown,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

/**
 * Inventory item card.
 * <p>
 * Displays one inventory item with badge, icon, meta text, and expiry status.
 *
 * Official references:
 * Material:
 * https://api.flutter.dev/flutter/material/Material-class.html
 * InkWell:
 * https://api.flutter.dev/flutter/material/InkWell-class.html
 * Stack:
 * https://api.flutter.dev/flutter/widgets/Stack-class.html
 * Positioned:
 * https://api.flutter.dev/flutter/widgets/Positioned-class.html
 */
class _InventoryCard extends StatelessWidget {
  const _InventoryCard({required this.item});

  final InventoryItem item;

  @override
  Widget build(BuildContext context) {
    final band = item.expiryBand;
    final expColor = expiryColor(band);
    return Material(
      color: Colors.transparent,
      child: InkWell(
        onTap: () {},
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
              if (item.badge != null)
                Positioned(
                  top: 12,
                  left: 12,
                  child: _ItemBadge(badge: item.badge!),
                ),
              Padding(
                padding: const EdgeInsets.fromLTRB(16, 44, 16, 16),
                child: Column(
                  children: [
                    Container(
                      width: 96,
                      height: 96,
                      decoration: BoxDecoration(
                        color: SfColors.chipBgLight,
                        shape: BoxShape.circle,
                      ),
                      child: Icon(
                        item.category.icon,
                        size: 40,
                        color: SfColors.brownMuted,
                      ),
                    ),
                    const SizedBox(height: 12),
                    Text(
                      item.name,
                      textAlign: TextAlign.center,
                      maxLines: 2,
                      overflow: TextOverflow.ellipsis,
                      style: TextStyle(
                        fontWeight: FontWeight.w800,
                        fontSize: 15,
                        color: SfColors.brown,
                      ),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      '${item.category.label} · ${item.quantityLabel}',
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 12,
                        color: SfColors.brownMuted,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                    const Spacer(),
                    Text(
                      expiryLabel(item.daysLeft),
                      style: TextStyle(
                        fontSize: 13,
                        fontWeight: FontWeight.w700,
                        color: expColor,
                      ),
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

/**
 * Item badge.
 * <p>
 * Status badge shown at the top-left of an inventory card.
 * Supports urgent and new item states.
 */
class _ItemBadge extends StatelessWidget {
  const _ItemBadge({required this.badge});

  final ItemBadge badge;

  @override
  Widget build(BuildContext context) {
    switch (badge) {
      case ItemBadge.urgent:
        return Container(
          padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
          decoration: BoxDecoration(
            color: SfColors.badgeUrgentBg,
            borderRadius: BorderRadius.circular(SfRadii.pill),
          ),
          child: Text(
            'Urgent',
            style: TextStyle(
              fontSize: 10,
              fontWeight: FontWeight.w800,
              color: SfColors.expiryUrgent,
            ),
          ),
        );
      case ItemBadge.newItem:
        return Container(
          padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
          decoration: BoxDecoration(
            color: SfColors.brown,
            borderRadius: BorderRadius.circular(SfRadii.pill),
          ),
          child: const Text(
            'New',
            style: TextStyle(
              fontSize: 10,
              fontWeight: FontWeight.w800,
              color: Colors.white,
            ),
          ),
        );
    }
  }
}
