// ignore_for_file: slash_for_doc_comments

import 'package:flutter/material.dart';

import '../navigation/app_destination.dart';
import '../theme/smart_fridge_tokens.dart';

/**
 * Application sidebar.
 * <p>
 * Sidebar with brand, navigation, badges, and a footer summary.
 * Used both as a fixed sidebar and as drawer content.
 *
 * Official references:
 * StatelessWidget:
 * https://api.flutter.dev/flutter/widgets/StatelessWidget-class.html
 * Column:
 * https://api.flutter.dev/flutter/widgets/Column-class.html
 * Spacer:
 * https://api.flutter.dev/flutter/widgets/Spacer-class.html
 * Drawer:
 * https://api.flutter.dev/flutter/material/Drawer-class.html
 *
 * Open-source reference:
 * Flutter samples:
 * https://github.com/flutter/samples
 * material_3_demo:
 * https://github.com/flutter/samples/tree/main/material_3_demo
 */
class AppSidebar extends StatelessWidget {
  const AppSidebar({
    super.key,
    required this.selected,
    required this.onSelect,
    this.myFridgeBadgeCount = 5,
    this.groceryBadgeCount = 2,
  });

  final AppDestination selected;
  final ValueChanged<AppDestination> onSelect;
  final int myFridgeBadgeCount;
  final int groceryBadgeCount;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 260,
      color: SfColors.whiteCard,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          /**
           * Brand header.
           * <p>
           * Top brand area with the app icon and name.
           *
           * Official references:
           * Row:
           * https://api.flutter.dev/flutter/widgets/Row-class.html
           * Container:
           * https://api.flutter.dev/flutter/widgets/Container-class.html
           * Icon:
           * https://api.flutter.dev/flutter/widgets/Icon-class.html
           */
          Padding(
            padding: const EdgeInsets.fromLTRB(20, 28, 20, 24),
            child: Row(
              children: [
                Container(
                  width: 40,
                  height: 40,
                  decoration: BoxDecoration(
                    color: SfColors.brown,
                    borderRadius: BorderRadius.circular(10),
                  ),
                  child: const Icon(
                    Icons.kitchen,
                    color: Colors.white,
                    size: 22,
                  ),
                ),
                const SizedBox(width: 12),
                Text(
                  'Smart Fridge',
                  style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w800,
                    color: SfColors.brown,
                  ),
                ),
              ],
            ),
          ),

          /**
           * Main navigation group.
           * <p>
           * Main navigation for inventory, recipes, grocery, and settings.
           * Highlights the current page and switches destination on tap.
           */
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 12),
            child: Column(
              children: [
                _NavRow(
                  label: 'My Fridge',
                  icon: Icons.kitchen_outlined,
                  selected: selected == AppDestination.inventory,
                  badge: myFridgeBadgeCount,
                  onTap: () => onSelect(AppDestination.inventory),
                ),
                const SizedBox(height: 6),
                _NavRow(
                  label: 'Recipes',
                  icon: Icons.menu_book_outlined,
                  selected: selected == AppDestination.recipes,
                  onTap: () => onSelect(AppDestination.recipes),
                ),
                const SizedBox(height: 6),
                _NavRow(
                  label: 'Grocery List',
                  icon: Icons.shopping_cart_outlined,
                  selected: selected == AppDestination.grocery,
                  badge: groceryBadgeCount,
                  onTap: () => onSelect(AppDestination.grocery),
                ),
                const SizedBox(height: 6),
                _NavRow(
                  label: 'Settings',
                  icon: Icons.settings_outlined,
                  selected: selected == AppDestination.settings,
                  onTap: () => onSelect(AppDestination.settings),
                ),
              ],
            ),
          ),
          const Spacer(),

          /**
           * Conditional footer area.
           * <p>
           * Shows the recipe summary on the recipes page,
           * and the storage summary on other pages.
           */
          if (selected == AppDestination.recipes)
            const _ThisWeekFooter()
          else ...[
            const Padding(
              padding: EdgeInsets.fromLTRB(20, 0, 20, 8),
              child: Text(
                'STORAGE',
                style: TextStyle(
                  fontSize: 10,
                  letterSpacing: 1.4,
                  fontWeight: FontWeight.w700,
                  color: SfColors.brownMuted,
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.fromLTRB(20, 0, 20, 28),
              child: Column(
                children: const [
                  _StorageRow(label: 'Fridge', count: 14, fraction: 14 / 24),
                  SizedBox(height: 12),
                  _StorageRow(label: 'Freezer', count: 6, fraction: 6 / 12),
                  SizedBox(height: 12),
                  _StorageRow(label: 'Pantry', count: 4, fraction: 4 / 10),
                ],
              ),
            ),
          ],
        ],
      ),
    );
  }
}

/**
 * Single navigation row.
 * <p>
 * One sidebar navigation item with icon, label,
 * selected state, and an optional badge.
 * Calls onTap to change the destination.
 *
 * Official references:
 * Material:
 * https://api.flutter.dev/flutter/material/Material-class.html
 * InkWell:
 * https://api.flutter.dev/flutter/material/InkWell-class.html
 * Ink:
 * https://api.flutter.dev/flutter/material/Ink-class.html
 * Row:
 * https://api.flutter.dev/flutter/widgets/Row-class.html
 */
class _NavRow extends StatelessWidget {
  const _NavRow({
    required this.label,
    required this.icon,
    required this.selected,
    required this.onTap,
    this.badge,
  });

  final String label;
  final IconData icon;
  final bool selected;
  final VoidCallback onTap;
  final int? badge;

  @override
  Widget build(BuildContext context) {
    final bg = selected ? SfColors.brown : Colors.transparent;
    final fg = selected ? Colors.white : SfColors.brown;

    return Material(
      color: Colors.transparent,
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(12),
        child: Ink(
          padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 12),
          decoration: BoxDecoration(
            color: bg,
            borderRadius: BorderRadius.circular(12),
          ),
          child: Row(
            children: [
              Icon(icon, size: 22, color: fg),
              const SizedBox(width: 12),
              Expanded(
                child: Text(
                  label,
                  style: TextStyle(
                    fontWeight: FontWeight.w700,
                    fontSize: 14,
                    color: fg,
                  ),
                ),
              ),
              if (badge != null && badge! > 0)
                Container(
                  padding: const EdgeInsets.symmetric(
                    horizontal: 8,
                    vertical: 2,
                  ),
                  decoration: BoxDecoration(
                    color: selected
                        ? Colors.white.withValues(alpha: 0.25)
                        : const Color(0xFFE53935),
                    borderRadius: BorderRadius.circular(SfRadii.pill),
                  ),
                  child: Text(
                    '$badge',
                    style: const TextStyle(
                      fontSize: 11,
                      fontWeight: FontWeight.w800,
                      color: Colors.white,
                    ),
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
 * Recipes-specific footer summary.
 * <p>
 * Footer summary shown when the recipes page is selected.
 * Displays weekly recipe stats and progress.
 *
 * Official references:
 * LinearProgressIndicator:
 * https://api.flutter.dev/flutter/material/LinearProgressIndicator-class.html
 * Column:
 * https://api.flutter.dev/flutter/widgets/Column-class.html
 * Row:
 * https://api.flutter.dev/flutter/widgets/Row-class.html
 */
class _ThisWeekFooter extends StatelessWidget {
  const _ThisWeekFooter();

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(20, 0, 20, 28),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            'THIS WEEK',
            style: TextStyle(
              fontSize: 10,
              letterSpacing: 1.4,
              fontWeight: FontWeight.w700,
              color: SfColors.brownMuted,
            ),
          ),
          const SizedBox(height: 14),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                'Recipes cooked',
                style: TextStyle(
                  fontSize: 13,
                  fontWeight: FontWeight.w600,
                  color: SfColors.brownLabel,
                ),
              ),
              Text(
                '5',
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.w800,
                  color: SfColors.brown,
                ),
              ),
            ],
          ),
          const SizedBox(height: 14),
          Text(
            'Matched from Fridge',
            style: TextStyle(
              fontSize: 13,
              fontWeight: FontWeight.w600,
              color: SfColors.brownLabel,
            ),
          ),
          const SizedBox(height: 6),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const SizedBox.shrink(),
              Text(
                '87%',
                style: TextStyle(
                  fontSize: 12,
                  fontWeight: FontWeight.w700,
                  color: SfColors.brownMuted,
                ),
              ),
            ],
          ),
          ClipRRect(
            borderRadius: BorderRadius.circular(4),
            child: const LinearProgressIndicator(
              value: 0.87,
              minHeight: 5,
              backgroundColor: SfColors.chipBgLight,
              color: SfColors.brown,
            ),
          ),
          const SizedBox(height: 14),
          Text(
            'Calories saved',
            style: TextStyle(
              fontSize: 13,
              fontWeight: FontWeight.w600,
              color: SfColors.brownLabel,
            ),
          ),
          const SizedBox(height: 6),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const SizedBox.shrink(),
              Text(
                '1.2k',
                style: TextStyle(
                  fontSize: 12,
                  fontWeight: FontWeight.w700,
                  color: SfColors.brownMuted,
                ),
              ),
            ],
          ),
          ClipRRect(
            borderRadius: BorderRadius.circular(4),
            child: const LinearProgressIndicator(
              value: 0.72,
              minHeight: 5,
              backgroundColor: SfColors.chipBgLight,
              color: SfColors.brown,
            ),
          ),
        ],
      ),
    );
  }
}

/**
 * Storage summary row.
 * <p>
 * One storage summary row with label, item count,
 * and usage progress.
 *
 * Official references:
 * LinearProgressIndicator:
 * https://api.flutter.dev/flutter/material/LinearProgressIndicator-class.html
 * Column:
 * https://api.flutter.dev/flutter/widgets/Column-class.html
 * Row:
 * https://api.flutter.dev/flutter/widgets/Row-class.html
 */
class _StorageRow extends StatelessWidget {
  const _StorageRow({
    required this.label,
    required this.count,
    required this.fraction,
  });

  final String label;
  final int count;
  final double fraction;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(
              label,
              style: TextStyle(
                fontSize: 13,
                fontWeight: FontWeight.w600,
                color: SfColors.brownLabel,
              ),
            ),
            Text(
              '$count items',
              style: TextStyle(
                fontSize: 12,
                fontWeight: FontWeight.w600,
                color: SfColors.brownMuted,
              ),
            ),
          ],
        ),
        const SizedBox(height: 6),
        ClipRRect(
          borderRadius: BorderRadius.circular(4),
          child: LinearProgressIndicator(
            value: fraction.clamp(0.0, 1.0),
            minHeight: 5,
            backgroundColor: SfColors.chipBgLight,
            color: SfColors.brown,
          ),
        ),
      ],
    );
  }
}
