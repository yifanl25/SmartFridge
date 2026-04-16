import 'package:flutter/material.dart';

import '../navigation/app_destination.dart';
import '../screens/grocery_list_screen.dart';
import '../screens/inventory_screen.dart';
import '../screens/recipes_screen.dart';
import '../screens/settings_screen.dart';
import '../theme/smart_fridge_tokens.dart';
import '../widgets/app_sidebar.dart';

/// App shell: left sidebar (or drawer) + destination content. PRD navigation map.
class MainShell extends StatefulWidget {
  const MainShell({super.key, this.initialDestination = AppDestination.inventory});

  final AppDestination initialDestination;

  @override
  State<MainShell> createState() => _MainShellState();
}

class _MainShellState extends State<MainShell> {
  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();

  late AppDestination _destination = widget.initialDestination;

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

  void _select(AppDestination d) {
    setState(() => _destination = d);
  }

  @override
  Widget build(BuildContext context) {
    final body = _pageFor(_destination);

    return LayoutBuilder(
      builder: (context, constraints) {
        final wide = constraints.maxWidth >= 900;

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
                      onPressed: () => _scaffoldKey.currentState?.openDrawer(),
                    ),
                  ),
                ),
              ],
            ),
          );
        }

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
                        onPressed: () => _scaffoldKey.currentState?.openDrawer(),
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
