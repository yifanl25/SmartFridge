// ignore_for_file: slash_for_doc_comments

import 'package:flutter/material.dart';

import '../navigation/app_destination.dart';
import '../screens/grocery_list_screen.dart';
import '../screens/inventory_screen.dart';
import '../screens/recipes_screen.dart';
import '../screens/settings_screen.dart';
import '../theme/smart_fridge_tokens.dart';
import '../widgets/app_sidebar.dart';

/**
 * Main application shell.
 * <p>
 * Main app shell that switches page content by destination.
 * Uses a sidebar on wide screens and a drawer on narrow screens.
 *
 * Official references:
 * StatefulWidget:
 * https://api.flutter.dev/flutter/widgets/StatefulWidget-class.html
 * State:
 * https://api.flutter.dev/flutter/widgets/State-class.html
 * LayoutBuilder:
 * https://api.flutter.dev/flutter/widgets/LayoutBuilder-class.html
 * Scaffold:
 * https://api.flutter.dev/flutter/material/Scaffold-class.html
 * Drawer:
 * https://api.flutter.dev/flutter/material/Drawer-class.html
 * SafeArea:
 * https://api.flutter.dev/flutter/widgets/SafeArea-class.html
 *
 * Open-source reference:
 * Flutter samples:
 * https://github.com/flutter/samples
 * material_3_demo:
 * https://github.com/flutter/samples/tree/main/material_3_demo
 */
class MainShell extends StatefulWidget {
  const MainShell({
    super.key,
    this.initialDestination = AppDestination.inventory,
  });

  final AppDestination initialDestination;

  @override
  State<MainShell> createState() => _MainShellState();
}

/**
 * State for MainShell.
 * <p>
 * Stores the current destination and the scaffold key for the drawer.
 */
class _MainShellState extends State<MainShell> {
  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();

  late AppDestination _destination = widget.initialDestination;

  /**
   * Resolves page widget for a destination.
   * <p>
   * Returns the page widget for the selected destination.
   */
  Widget _pageFor(AppDestination d) {
    switch (d) {
      case AppDestination.inventory:
        return const InventoryScreen();
      case AppDestination.recipes:
        return const RecipesScreen();
      case AppDestination.grocery:
        return const GroceryListScreen();
      case AppDestination.settings:
        return const SettingsScreen();
    }
  }

  /**
   * Resolves title for a destination.
   * <p>
   * Returns the title for the narrow-screen top bar.
   */
  String _titleFor(AppDestination d) {
    switch (d) {
      case AppDestination.inventory:
        return 'My Fridge';
      case AppDestination.recipes:
        return 'Recipes';
      case AppDestination.grocery:
        return 'Grocery List';
      case AppDestination.settings:
        return 'Settings';
    }
  }

  /**
   * Updates current destination.
   * <p>
   * Updates the current destination after a sidebar or drawer selection.
   */
  void _select(AppDestination d) {
    setState(() => _destination = d);
  }

  @override
  Widget build(BuildContext context) {
    final body = _pageFor(_destination);

    return LayoutBuilder(
      builder: (context, constraints) {
        final wide = constraints.maxWidth >= 900;

        /**
         * Wide-screen layout.
         * <p>
         * Uses a fixed sidebar and a content area on wide screens.
         *
         * Official references:
         * Row:
         * https://api.flutter.dev/flutter/widgets/Row-class.html
         * Expanded:
         * https://api.flutter.dev/flutter/widgets/Expanded-class.html
         */
        if (wide) {
          return ColoredBox(
            color: SfColors.cream,
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                AppSidebar(
                  selected: _destination,
                  onSelect: _select,
                ),
                Expanded(child: body),
              ],
            ),
          );
        }

        /**
         * Drawer for narrow screens.
         * <p>
         * Moves the sidebar into a drawer on narrow screens.
         *
         * Official references:
         * Drawer:
         * https://api.flutter.dev/flutter/material/Drawer-class.html
         * Navigator.pop:
         * https://api.flutter.dev/flutter/widgets/Navigator/pop.html
         */
        final drawer = Drawer(
          backgroundColor: SfColors.whiteCard,
          width: 280,
          child: SafeArea(
            child: AppSidebar(
              selected: _destination,
              onSelect: (d) {
                _select(d);
                Navigator.of(context).pop();
              },
            ),
          ),
        );

        /**
         * Full-bleed body layout for inventory / recipes.
         * <p>
         * Inventory and recipes keep their own page headers.
         * Narrow screens only add a menu button on top.
         *
         * Official references:
         * Stack:
         * https://api.flutter.dev/flutter/widgets/Stack-class.html
         * Positioned:
         * https://api.flutter.dev/flutter/widgets/Positioned-class.html
         * IconButton:
         * https://api.flutter.dev/flutter/material/IconButton-class.html
         */
        if (_destination == AppDestination.inventory ||
            _destination == AppDestination.recipes) {
          return Scaffold(
            key: _scaffoldKey,
            backgroundColor: SfColors.cream,
            drawer: drawer,
            body: Stack(
              children: [
                body,
                Positioned(
                  top: 0,
                  left: 0,
                  child: SafeArea(
                    child: IconButton(
                      icon: Icon(Icons.menu, color: SfColors.brown),
                      onPressed: () =>
                          _scaffoldKey.currentState?.openDrawer(),
                    ),
                  ),
                ),
              ],
            ),
          );
        }

        /**
         * Standard mobile shell layout for grocery / settings.
         * <p>
         * Grocery and settings use a standard mobile shell
         * with a menu button, title, and page content below.
         */
        return Scaffold(
          key: _scaffoldKey,
          backgroundColor: SfColors.cream,
          drawer: drawer,
          body: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              SafeArea(
                bottom: false,
                child: Padding(
                  padding: const EdgeInsets.fromLTRB(4, 4, 16, 8),
                  child: Row(
                    children: [
                      IconButton(
                        icon: Icon(Icons.menu, color: SfColors.brown),
                        onPressed: () =>
                            _scaffoldKey.currentState?.openDrawer(),
                      ),
                      Text(
                        _titleFor(_destination),
                        style: TextStyle(
                          fontSize: 18,
                          fontWeight: FontWeight.w800,
                          color: SfColors.brown,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              Expanded(child: body),
            ],
          ),
        );
      },
    );
  }
}
