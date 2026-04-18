import 'package:flutter/material.dart';

import '../models/grocery_line.dart';
import '../services/fridge_api_service.dart';
import '../theme/smart_fridge_tokens.dart';
import '../utils/grocery_category_map.dart';

class GroceryListScreen extends StatefulWidget {
  const GroceryListScreen({super.key, this.useLiveApi = true});

  final bool useLiveApi;

  @override
  State<GroceryListScreen> createState() => _GroceryListScreenState();
}

class _GroceryListScreenState extends State<GroceryListScreen> {
  final TextEditingController _search = TextEditingController();
  final TextEditingController _quickAdd = TextEditingController();

  GroceryUiCategory? _filterCategory;
  bool _showCompleted = false;

  late Future<_GroceryScreenData> _screenFuture;

  @override
  void initState() {
    super.initState();
    _search.addListener(() {
      setState(() {});
    });

    if (widget.useLiveApi) {
      _screenFuture = _loadScreenData();
    } else {
      _screenFuture = Future<_GroceryScreenData>.value(
        const _GroceryScreenData(
          items: <GroceryLineView>[],
          subtotal: 0,
          tax: 0,
          total: 0,
        ),
      );
    }
  }

  @override
  void dispose() {
    _search.dispose();
    _quickAdd.dispose();
    super.dispose();
  }

  Future<_GroceryScreenData> _loadScreenData() async {
    final items = await FridgeApiService.instance.fetchGroceryItems();
    final totals = await FridgeApiService.instance.fetchGroceryTotals();

    return _GroceryScreenData(
      items: items,
      subtotal: totals['subtotal'] ?? 0,
      tax: totals['tax'] ?? 0,
      total: totals['total'] ?? 0,
    );
  }

  Future<void> _refresh() async {
    if (!widget.useLiveApi) {
      return;
    }

    setState(() {
      _screenFuture = _loadScreenData();
    });

    await _screenFuture;
  }

  List<GroceryLineView> _filtered(List<GroceryLineView> items) {
    Iterable<GroceryLineView> result =
    items.where((item) => item.collected == _showCompleted);

    final text = _search.text.trim().toLowerCase();
    if (text.isNotEmpty) {
      result = result.where((item) => item.name.toLowerCase().contains(text));
    }

    if (_filterCategory != null) {
      result = result.where((item) => item.uiCategory == _filterCategory);
    }

    return result.toList();
  }

  Future<void> _toggleCollected(GroceryLineView item) async {
    if (!widget.useLiveApi) {
      return;
    }

    try {
      await FridgeApiService.instance.toggleGroceryCollected(item.id);

      if (!mounted) {
        return;
      }

      await _refresh();
    } catch (e) {
      if (!mounted) {
        return;
      }

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('$e')),
      );
    }
  }

  Future<void> _deltaQty(GroceryLineView item, int delta) async {
    if (!widget.useLiveApi) {
      return;
    }

    try {
      await FridgeApiService.instance.updateGroceryQuantity(item.id, delta);

      if (!mounted) {
        return;
      }

      await _refresh();
    } catch (e) {
      if (!mounted) {
        return;
      }

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('$e')),
      );
    }
  }

  Future<void> _addQuickItem() async {
    final text = _quickAdd.text.trim();
    if (text.isEmpty || !widget.useLiveApi) {
      return;
    }

    try {
      await FridgeApiService.instance.addGroceryLine(
        foodName: text,
        quantity: 1,
        price: 0,
      );

      _quickAdd.clear();

      if (!mounted) {
        return;
      }

      setState(() {
        _showCompleted = false;
      });

      await _refresh();
    } catch (e) {
      if (!mounted) {
        return;
      }

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('$e')),
      );
    }
  }

  Future<void> _checkout() async {
    if (!widget.useLiveApi) {
      return;
    }

    try {
      await FridgeApiService.instance.groceryCheckout();

      if (!mounted) {
        return;
      }

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Checkout complete — list cleared.'),
        ),
      );

      await _refresh();
    } catch (e) {
      if (!mounted) {
        return;
      }

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('$e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<_GroceryScreenData>(
      future: _screenFuture,
      builder: (context, snapshot) {
        final loading = snapshot.connectionState == ConnectionState.waiting;

        List<GroceryLineView> items = <GroceryLineView>[];
        String? apiError;
        double subtotal = 0;
        double tax = 0;
        double total = 0;

        if (snapshot.hasData) {
          items = snapshot.data!.items;
          subtotal = snapshot.data!.subtotal;
          tax = snapshot.data!.tax;
          total = snapshot.data!.total;
        } else if (snapshot.hasError) {
          apiError = '${snapshot.error}';
        }

        final active = items.where((item) => !item.collected).length;
        final done = items.where((item) => item.collected).length;
        final filtered = _filtered(items);

        return Scaffold(
          backgroundColor: SfColors.cream,
          body: Column(
            children: [
              if (widget.useLiveApi && loading)
                const LinearProgressIndicator(minHeight: 3),
              if (apiError != null && widget.useLiveApi)
                Padding(
                  padding: const EdgeInsets.fromLTRB(28, 8, 28, 0),
                  child: Material(
                    color: Colors.orange.shade50,
                    borderRadius: BorderRadius.circular(12),
                    child: Padding(
                      padding: const EdgeInsets.all(10),
                      child: Text(
                        'Could not load grocery API. ./gradlew bootRun · $apiError',
                        style: TextStyle(
                          color: Colors.orange.shade900,
                          fontSize: 12,
                        ),
                      ),
                    ),
                  ),
                ),
              Padding(
                padding: const EdgeInsets.fromLTRB(28, 28, 28, 12),
                child: Row(
                  children: [
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            'Grocery List',
                            style: TextStyle(
                              fontSize: 34,
                              fontWeight: FontWeight.w800,
                              color: SfColors.brown,
                              height: 1.1,
                            ),
                          ),
                          const SizedBox(height: 6),
                          Text(
                            '$active items to buy · $done already done',
                            style: TextStyle(
                              fontSize: 15,
                              color: SfColors.brownMuted,
                              fontWeight: FontWeight.w500,
                            ),
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(width: 18),
                    SizedBox(
                      width: 360,
                      child: TextField(
                        controller: _search,
                        decoration: InputDecoration(
                          hintText: 'Search grocery items...',
                          prefixIcon: Icon(
                            Icons.search,
                            color: SfColors.brownMuted,
                          ),
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
                  ],
                ),
              ),
              Padding(
                padding: const EdgeInsets.fromLTRB(28, 4, 28, 14),
                child: Row(
                  children: [
                    _TopToggleChip(
                      label: 'To Buy ($active)',
                      selected: !_showCompleted,
                      onTap: () {
                        setState(() {
                          _showCompleted = false;
                        });
                      },
                    ),
                    const SizedBox(width: 10),
                    _TopToggleChip(
                      label: 'Completed ($done)',
                      selected: _showCompleted,
                      onTap: () {
                        setState(() {
                          _showCompleted = true;
                        });
                      },
                    ),
                    const SizedBox(width: 16),
                    Expanded(
                      child: SingleChildScrollView(
                        scrollDirection: Axis.horizontal,
                        child: Row(
                          children: [
                            _CategoryFilterChip(
                              label: 'All',
                              selected: _filterCategory == null,
                              onTap: () {
                                setState(() {
                                  _filterCategory = null;
                                });
                              },
                            ),
                            for (final category in GroceryUiCategory.values)
                              _CategoryFilterChip(
                                label: category.label,
                                selected: _filterCategory == category,
                                onTap: () {
                                  setState(() {
                                    _filterCategory = category;
                                  });
                                },
                              ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              Expanded(
                child: RefreshIndicator(
                  onRefresh: widget.useLiveApi ? _refresh : () async {},
                  child: Padding(
                    padding: const EdgeInsets.fromLTRB(20, 0, 20, 16),
                    child: Container(
                      decoration: BoxDecoration(
                        color: SfColors.whiteCard,
                        borderRadius: BorderRadius.circular(SfRadii.card),
                        boxShadow: [
                          BoxShadow(
                            color: Colors.black.withValues(alpha: 0.05),
                            blurRadius: 14,
                            offset: const Offset(0, 6),
                          ),
                        ],
                      ),
                      child: filtered.isEmpty
                          ? ListView(
                        physics: const AlwaysScrollableScrollPhysics(),
                        children: [
                          const SizedBox(height: 80),
                          Center(
                            child: Text(
                              _showCompleted
                                  ? 'No completed items yet.'
                                  : 'No matching grocery items.',
                              style: TextStyle(
                                color: SfColors.brownMuted,
                                fontSize: 15,
                              ),
                            ),
                          ),
                        ],
                      )
                          : ListView.separated(
                        physics: const AlwaysScrollableScrollPhysics(),
                        padding: const EdgeInsets.symmetric(
                          vertical: 8,
                          horizontal: 12,
                        ),
                        itemBuilder: (context, index) {
                          final item = filtered[index];
                          return _GroceryRow(
                            item: item,
                            useApi: widget.useLiveApi,
                            onToggleDone: () => _toggleCollected(item),
                            onMinus: () => _deltaQty(item, -1),
                            onPlus: () => _deltaQty(item, 1),
                          );
                        },
                        separatorBuilder: (context, index) {
                          return const Divider(
                            color: Color(0xFFEDE2D6),
                            height: 1,
                          );
                        },
                        itemCount: filtered.length,
                      ),
                    ),
                  ),
                ),
              ),
              if (widget.useLiveApi)
                Padding(
                  padding: const EdgeInsets.fromLTRB(20, 0, 20, 8),
                  child: Material(
                    color: SfColors.whiteCard,
                    borderRadius: BorderRadius.circular(SfRadii.card),
                    child: Padding(
                      padding: const EdgeInsets.symmetric(
                        horizontal: 16,
                        vertical: 12,
                      ),
                      child: Row(
                        children: [
                          Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  'Subtotal \$${subtotal.toStringAsFixed(2)} · '
                                      'Tax \$${tax.toStringAsFixed(2)}',
                                  style: TextStyle(
                                    fontSize: 12,
                                    color: SfColors.brownMuted,
                                  ),
                                ),
                                Text(
                                  'Total \$${total.toStringAsFixed(2)}',
                                  style: TextStyle(
                                    fontSize: 18,
                                    fontWeight: FontWeight.w800,
                                    color: SfColors.brown,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          FilledButton(
                            onPressed: items.isEmpty ? null : _checkout,
                            style: FilledButton.styleFrom(
                              backgroundColor: SfColors.brown,
                              foregroundColor: Colors.white,
                            ),
                            child: const Text('Checkout'),
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
              Padding(
                padding: const EdgeInsets.fromLTRB(20, 0, 20, 20),
                child: Row(
                  children: [
                    Expanded(
                      child: TextField(
                        controller: _quickAdd,
                        enabled: widget.useLiveApi,
                        onSubmitted: (_) => _addQuickItem(),
                        decoration: InputDecoration(
                          hintText: widget.useLiveApi
                              ? 'Quick add (catalog name, e.g. Whole Milk)...'
                              : 'Offline mode',
                          filled: true,
                          fillColor: SfColors.whiteCard,
                          contentPadding: const EdgeInsets.symmetric(
                            horizontal: 16,
                            vertical: 14,
                          ),
                          border: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(SfRadii.pill),
                            borderSide: BorderSide.none,
                          ),
                        ),
                      ),
                    ),
                    const SizedBox(width: 10),
                    FilledButton.icon(
                      style: FilledButton.styleFrom(
                        backgroundColor: SfColors.brown,
                        foregroundColor: Colors.white,
                        padding: const EdgeInsets.symmetric(
                          horizontal: 20,
                          vertical: 14,
                        ),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(SfRadii.pill),
                        ),
                      ),
                      onPressed: widget.useLiveApi ? _addQuickItem : null,
                      icon: const Icon(Icons.add),
                      label: const Text(
                        'Add',
                        style: TextStyle(fontWeight: FontWeight.w700),
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        );
      },
    );
  }
}

class _GroceryScreenData {
  const _GroceryScreenData({
    required this.items,
    required this.subtotal,
    required this.tax,
    required this.total,
  });

  final List<GroceryLineView> items;
  final double subtotal;
  final double tax;
  final double total;
}

class _TopToggleChip extends StatelessWidget {
  const _TopToggleChip({
    required this.label,
    required this.selected,
    required this.onTap,
  });

  final String label;
  final bool selected;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return ChoiceChip(
      label: Text(label),
      selected: selected,
      onSelected: (_) => onTap(),
      selectedColor: SfColors.brown,
      backgroundColor: SfColors.whiteCard,
      side: BorderSide(
        color: selected ? SfColors.brown : SfColors.chipBgLight,
      ),
      labelStyle: TextStyle(
        color: selected ? Colors.white : SfColors.brown,
        fontSize: 13,
        fontWeight: FontWeight.w700,
      ),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(SfRadii.pill),
      ),
    );
  }
}

class _CategoryFilterChip extends StatelessWidget {
  const _CategoryFilterChip({
    required this.label,
    required this.selected,
    required this.onTap,
  });

  final String label;
  final bool selected;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(right: 8),
      child: FilterChip(
        label: Text(label),
        selected: selected,
        showCheckmark: false,
        onSelected: (_) => onTap(),
        selectedColor: SfColors.chipBgLight,
        backgroundColor: Colors.transparent,
        side: BorderSide(color: SfColors.chipBgLight),
        labelStyle: TextStyle(
          color: SfColors.brown,
          fontSize: 12,
          fontWeight: selected ? FontWeight.w700 : FontWeight.w600,
        ),
      ),
    );
  }
}

class _GroceryRow extends StatelessWidget {
  const _GroceryRow({
    required this.item,
    required this.useApi,
    required this.onToggleDone,
    required this.onMinus,
    required this.onPlus,
  });

  final GroceryLineView item;
  final bool useApi;
  final VoidCallback onToggleDone;
  final VoidCallback onMinus;
  final VoidCallback onPlus;

  @override
  Widget build(BuildContext context) {
    return ListTile(
      contentPadding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
      leading: IconButton(
        onPressed: useApi ? onToggleDone : null,
        icon: Icon(
          item.collected ? Icons.check_circle : Icons.radio_button_unchecked,
          color: item.collected ? SfColors.matchGreen : SfColors.brownMuted,
        ),
      ),
      title: Text(
        item.name,
        style: TextStyle(
          fontWeight: FontWeight.w700,
          color: item.collected ? SfColors.brownMuted : SfColors.brown,
          decoration: item.collected ? TextDecoration.lineThrough : null,
        ),
      ),
      subtitle: Text(
        '${item.uiCategory.label} · ${item.qtyLabel}',
        style: TextStyle(
          fontSize: 12,
          color: SfColors.brownMuted,
        ),
      ),
      trailing: useApi
          ? Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          IconButton(
            onPressed: item.quantity > 0 ? onMinus : null,
            icon: const Icon(Icons.remove_circle_outline),
            color: SfColors.brownMuted,
          ),
          IconButton(
            onPressed: onPlus,
            icon: const Icon(Icons.add_circle_outline),
            color: SfColors.brown,
          ),
        ],
      )
          : null,
    );
  }
}
