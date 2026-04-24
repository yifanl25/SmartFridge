// ignore_for_file: slash_for_doc_comments

import 'package:flutter/material.dart';

import '../app/main_shell.dart';
import '../models/health_goal.dart';
import '../services/fridge_api_service.dart';
import '../theme/smart_fridge_tokens.dart';

/**
 * Preferences screen.
 * <p>
 * Onboarding step 2. Users choose one health goal before entering the app.
 *
 * Official references:
 * ThemeData: https://api.flutter.dev/flutter/material/ThemeData-class.html
 * CustomScrollView: https://api.flutter.dev/flutter/widgets/CustomScrollView-class.html
 * Material widgets: https://docs.flutter.dev/ui/widgets/material
 *
 * Open-source reference:
 * material_3_demo: https://github.com/flutter/samples/tree/main/material_3_demo
 */
class PreferencesScreen extends StatefulWidget {
  const PreferencesScreen({super.key});

  @override
  State<PreferencesScreen> createState() => _PreferencesScreenState();
}

class _PreferencesScreenState extends State<PreferencesScreen> {
  /** Current selected goal. */
  HealthGoal _selected = HealthGoal.muscleBuilding;

  /** Goal card data. */
  static const List<_GoalOption> _options = [
    _GoalOption(
      goal: HealthGoal.muscleBuilding,
      badge: 'BULK UP',
      title: 'Muscle Building',
      description:
      'High-protein recommendations designed to support recovery, muscle repair, and lean mass growth with balanced complex carbs.',
      tags: ['High Protein', 'Complex Carbs', 'Recovery'],
      icon: Icons.fitness_center_rounded,
    ),
    _GoalOption(
      goal: HealthGoal.fatLoss,
      badge: 'CUT & LEAN',
      title: 'Fat Loss',
      description:
      'Lower-calorie, higher-satiety food suggestions that help control intake while still keeping meals practical and filling.',
      tags: ['Low Calorie', 'High Satiety', 'Fat Control'],
      icon: Icons.water_drop_outlined,
    ),
    _GoalOption(
      goal: HealthGoal.bloodSugarCare,
      badge: 'DIABETES',
      title: 'Blood Sugar Care',
      description:
      'Ingredients and meal ideas that prioritize steadier blood sugar and more fiber-forward choices aligned with care goals.',
      tags: ['Low GI', 'Glucose Stable', 'High Fibre'],
      icon: Icons.monitor_heart_outlined,
    ),
  ];

  /**
   * Page theme.
   * <p>
   * Keeps the warm SmartFridge palette while staying close to Material 3.
   *
   * Official references:
   * ThemeData: https://api.flutter.dev/flutter/material/ThemeData-class.html
   * ThemeData.from: https://api.flutter.dev/flutter/material/ThemeData/ThemeData.from.html
   * FilledButtonThemeData: https://api.flutter.dev/flutter/material/FilledButtonThemeData-class.html
   * OutlinedButtonThemeData: https://api.flutter.dev/flutter/material/OutlinedButtonThemeData-class.html
   */
  ThemeData _pageTheme(BuildContext context) {
    final scheme = ColorScheme.fromSeed(
      seedColor: SfColors.brown,
      brightness: Brightness.light,
    ).copyWith(
      primary: SfColors.brown,
      onPrimary: Colors.white,
      surface: SfColors.cream,
      onSurface: SfColors.brown,
      onSurfaceVariant: SfColors.brownLabel,
      outline: SfColors.stepInactive,
      outlineVariant: SfColors.stepInactive.withValues(alpha: 0.55),
      primaryContainer: SfColors.brown,
      onPrimaryContainer: Colors.white,
      secondaryContainer: SfColors.chipBgLight,
      onSecondaryContainer: SfColors.brown,
    );

    return ThemeData.from(
      colorScheme: scheme,
      useMaterial3: true,
    ).copyWith(
      scaffoldBackgroundColor: SfColors.cream,
      canvasColor: SfColors.cream,
      cardTheme: CardThemeData(
        margin: EdgeInsets.zero,
        elevation: 0,
        clipBehavior: Clip.antiAlias,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(SfRadii.card),
        ),
      ),
      filledButtonTheme: FilledButtonThemeData(
        style: FilledButton.styleFrom(
          backgroundColor: SfColors.brown,
          foregroundColor: Colors.white,
          minimumSize: const Size(0, 48),
          padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 16),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(SfRadii.pill),
          ),
        ),
      ),
      outlinedButtonTheme: OutlinedButtonThemeData(
        style: OutlinedButton.styleFrom(
          foregroundColor: SfColors.brown,
          side: BorderSide(color: SfColors.brown, width: 1.2),
          minimumSize: const Size(0, 48),
          padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 16),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(SfRadii.pill),
          ),
        ),
      ),
      snackBarTheme: SnackBarThemeData(
        behavior: SnackBarBehavior.floating,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(16),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Theme(
      data: _pageTheme(context),
      child: Builder(
        builder: (context) {
          final theme = Theme.of(context);
          final textTheme = theme.textTheme;

          return Scaffold(
            body: SafeArea(
              child: CustomScrollView(
                slivers: [
                  SliverToBoxAdapter(
                    child: _MarketingNavBar(
                      onHomeTap: () {},
                      onFeaturesTap: () {},
                      onRecipesTap: () {},
                      onPricingTap: () {},
                    ),
                  ),

                  const SliverToBoxAdapter(
                    child: Padding(
                      padding: EdgeInsets.fromLTRB(24, 4, 24, 24),
                      child: _OnboardingProgress(activeIndex: 1),
                    ),
                  ),

                  SliverPadding(
                    padding: const EdgeInsets.fromLTRB(24, 8, 24, 32),
                    sliver: SliverToBoxAdapter(
                      child: Center(
                        child: ConstrainedBox(
                          constraints: const BoxConstraints(maxWidth: 1120),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              // Step label
                              Text(
                                'STEP 2 OF 3',
                                style: textTheme.labelMedium?.copyWith(
                                  letterSpacing: 1.2,
                                  fontWeight: FontWeight.w700,
                                  color: SfColors.brownMuted,
                                ),
                              ),
                              const SizedBox(height: 12),

                              // Heading
                              ConstrainedBox(
                                constraints:
                                const BoxConstraints(maxWidth: 760),
                                child: Text(
                                  "What's your health goal?",
                                  style: textTheme.displaySmall?.copyWith(
                                    fontWeight: FontWeight.w700,
                                    color: SfColors.brown,
                                    height: 1.1,
                                  ),
                                ),
                              ),
                              const SizedBox(height: 12),

                              // Intro text
                              ConstrainedBox(
                                constraints:
                                const BoxConstraints(maxWidth: 820),
                                child: Text(
                                  'Choose one goal for onboarding. SmartFridge will tailor recommendations around it, and you can always update the choice later in Settings.',
                                  style: textTheme.bodyLarge?.copyWith(
                                    color: SfColors.brownLabel,
                                    height: 1.5,
                                  ),
                                ),
                              ),
                              const SizedBox(height: 32),

                              _GoalGrid(
                                options: _options,
                                selected: _selected,
                                onSelected: (goal) {
                                  setState(() => _selected = goal);
                                },
                              ),
                              const SizedBox(height: 28),

                              Divider(
                                color: SfColors.stepInactive.withValues(
                                  alpha: 0.55,
                                ),
                              ),
                              const SizedBox(height: 20),

                              OverflowBar(
                                alignment: MainAxisAlignment.end,
                                spacing: 12,
                                overflowAlignment: OverflowBarAlignment.end,
                                children: [
                                  OutlinedButton.icon(
                                    onPressed: () {
                                      Navigator.of(context).maybePop();
                                    },
                                    icon: const Icon(Icons.arrow_back_rounded),
                                    label: const Text('Back'),
                                  ),

                                  FilledButton.icon(
                                    style: FilledButton.styleFrom(
                                      backgroundColor: SfColors.brown,
                                      foregroundColor: Colors.white,
                                    ),
                                    onPressed: () async {
                                      try {
                                        await FridgeApiService.instance
                                            .savePreference(_selected);

                                        if (!context.mounted) return;

                                        Navigator.of(context).pushReplacement(
                                          MaterialPageRoute<void>(
                                            builder: (_) => const MainShell(),
                                          ),
                                        );
                                      } catch (e) {
                                        if (!context.mounted) return;

                                        ScaffoldMessenger.of(context)
                                            .showSnackBar(
                                          SnackBar(
                                            content: Text(
                                              'Could not reach API (start Spring Boot with ./gradlew bootRun). $e',
                                            ),
                                          ),
                                        );
                                      }
                                    },
                                    icon:
                                    const Icon(Icons.arrow_forward_rounded),
                                    label: const Text('Continue'),
                                  ),
                                ],
                              ),
                            ],
                          ),
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}

/** Goal card model. */
class _GoalOption {
  const _GoalOption({
    required this.goal,
    required this.badge,
    required this.title,
    required this.description,
    required this.tags,
    required this.icon,
  });

  final HealthGoal goal;
  final String badge;
  final String title;
  final String description;
  final List<String> tags;
  final IconData icon;
}

/**
 * Responsive goal card layout.
 * <p>
 * Uses Wrap for a simple desktop and tablet grid.
 *
 * Official references:
 * LayoutBuilder: https://api.flutter.dev/flutter/widgets/LayoutBuilder-class.html
 * Wrap: https://api.flutter.dev/flutter/widgets/Wrap-class.html
 */
class _GoalGrid extends StatelessWidget {
  const _GoalGrid({
    required this.options,
    required this.selected,
    required this.onSelected,
  });

  final List<_GoalOption> options;
  final HealthGoal selected;
  final ValueChanged<HealthGoal> onSelected;

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
      builder: (context, constraints) {
        const spacing = 20.0;

        final width = constraints.maxWidth;
        final columns = width >= 1080
            ? 3
            : width >= 720
            ? 2
            : 1;

        final cardWidth = columns == 1
            ? width
            : (width - (spacing * (columns - 1))) / columns;

        return Wrap(
          spacing: spacing,
          runSpacing: spacing,
          children: [
            for (final option in options)
              SizedBox(
                width: cardWidth,
                child: _GoalSelectionCard(
                  option: option,
                  selected: selected == option.goal,
                  onTap: () => onSelected(option.goal),
                ),
              ),
          ],
        );
      },
    );
  }
}

/**
 * Goal selection card.
 * <p>
 * Shows one health goal with a centered, marketing-style layout.
 *
 * Official references:
 * Card: https://api.flutter.dev/flutter/material/Card-class.html
 * InkWell: https://api.flutter.dev/flutter/material/InkWell-class.html
 * Semantics: https://api.flutter.dev/flutter/widgets/Semantics-class.html
 */
class _GoalSelectionCard extends StatelessWidget {
  const _GoalSelectionCard({
    required this.option,
    required this.selected,
    required this.onTap,
  });

  final _GoalOption option;
  final bool selected;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    final textTheme = Theme.of(context).textTheme;

    final cardColor = selected ? SfColors.brown : SfColors.whiteCard;
    final borderColor = selected
        ? SfColors.brown
        : SfColors.stepInactive.withValues(alpha: 0.75);

    final iconBackground = selected
        ? Colors.white.withValues(alpha: 0.14)
        : SfColors.chipBgLight;

    final iconColor = selected ? Colors.white : SfColors.brown;

    final titleColor = selected ? Colors.white : SfColors.brown;
    final bodyColor = selected
        ? Colors.white.withValues(alpha: 0.90)
        : SfColors.brownLabel;

    // Keep tag contrast readable in the selected state.
    final chipBackground = selected
        ? Colors.white.withValues(alpha: 0.22)
        : SfColors.chipBgLight;
    final chipForeground = SfColors.brown;

    return Semantics(
      button: true,
      selected: selected,
      label: option.title,
      child: Card(
        color: cardColor,
        elevation: selected ? 2 : 0,
        shadowColor: Colors.black.withValues(alpha: 0.08),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(SfRadii.card),
          side: BorderSide(
            color: borderColor,
            width: selected ? 1.4 : 1,
          ),
        ),
        child: InkWell(
          borderRadius: BorderRadius.circular(SfRadii.card),
          onTap: onTap,
          child: Padding(
            padding: const EdgeInsets.fromLTRB(28, 26, 28, 24),
            child: Stack(
              children: [
                // Selected check
                if (selected)
                  Positioned(
                    top: 0,
                    right: 0,
                    child: Container(
                      width: 34,
                      height: 34,
                      decoration: const BoxDecoration(
                        color: Colors.white,
                        shape: BoxShape.circle,
                      ),
                      child: const Icon(
                        Icons.check,
                        size: 20,
                        color: SfColors.brown,
                      ),
                    ),
                  ),

                Column(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    // Icon
                    CircleAvatar(
                      radius: 28,
                      backgroundColor: iconBackground,
                      child: Icon(
                        option.icon,
                        color: iconColor,
                        size: 28,
                      ),
                    ),
                    const SizedBox(height: 18),

                    // Badge
                    Text(
                      option.badge,
                      textAlign: TextAlign.center,
                      style: textTheme.labelMedium?.copyWith(
                        fontSize: 12,
                        fontWeight: FontWeight.w700,
                        letterSpacing: 2.2,
                        color: selected
                            ? Colors.white.withValues(alpha: 0.88)
                            : SfColors.brownMuted,
                      ),
                    ),
                    const SizedBox(height: 14),

                    // Card title
                    Text(
                      option.title,
                      textAlign: TextAlign.center,
                      style: textTheme.headlineSmall?.copyWith(
                        fontWeight: FontWeight.w700,
                        color: titleColor,
                        height: 1.15,
                      ),
                    ),
                    const SizedBox(height: 16),

                    // Tags
                    FittedBox(
                      fit: BoxFit.scaleDown,
                      child: Wrap(
                        alignment: WrapAlignment.center,
                        spacing: 6,
                        runSpacing: 6,
                        children: [
                          for (final tag in option.tags)
                            Container(
                              padding: const EdgeInsets.symmetric(
                                horizontal: 12,
                                vertical: 6,
                              ),
                              decoration: BoxDecoration(
                                color: chipBackground,
                                borderRadius: BorderRadius.circular(
                                  SfRadii.pill,
                                ),
                              ),
                              child: Text(
                                tag,
                                style: textTheme.labelSmall?.copyWith(
                                  fontWeight: FontWeight.w700,
                                  color: chipForeground,
                                  letterSpacing: 0.2,
                                ),
                              ),
                            ),
                        ],
                      ),
                    ),
                    const SizedBox(height: 18),

                    // Card description
                    Text(
                      option.description,
                      textAlign: TextAlign.center,
                      style: textTheme.bodyMedium?.copyWith(
                        color: bodyColor,
                        height: 1.55,
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

/**
 * Top marketing navigation bar.
 * <p>
 * Custom navigation built from standard Material widgets.
 *
 * Official references:
 * TextButton: https://api.flutter.dev/flutter/material/TextButton-class.html
 * FilledButton: https://api.flutter.dev/flutter/material/FilledButton-class.html
 * LayoutBuilder: https://api.flutter.dev/flutter/widgets/LayoutBuilder-class.html
 */
class _MarketingNavBar extends StatelessWidget {
  const _MarketingNavBar({
    required this.onHomeTap,
    required this.onFeaturesTap,
    required this.onRecipesTap,
    required this.onPricingTap,
  });

  final VoidCallback onHomeTap;
  final VoidCallback onFeaturesTap;
  final VoidCallback onRecipesTap;
  final VoidCallback onPricingTap;

  @override
  Widget build(BuildContext context) {
    final textTheme = Theme.of(context).textTheme;

    return Padding(
      padding: const EdgeInsets.fromLTRB(24, 20, 24, 12),
      child: Center(
        child: ConstrainedBox(
          constraints: const BoxConstraints(maxWidth: 1120),
          child: LayoutBuilder(
            builder: (context, constraints) {
              final showCenterLinks = constraints.maxWidth >= 860;

              return Row(
                children: [
                  // Brand
                  Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      CircleAvatar(
                        radius: 20,
                        backgroundColor: SfColors.brown,
                        child: const Icon(
                          Icons.kitchen_rounded,
                          color: Colors.white,
                          size: 20,
                        ),
                      ),
                      const SizedBox(width: 12),
                      Text(
                        'SmartFridge',
                        style: textTheme.titleLarge?.copyWith(
                          fontWeight: FontWeight.w700,
                          color: SfColors.brown,
                        ),
                      ),
                    ],
                  ),

                  if (showCenterLinks) ...[
                    const SizedBox(width: 32),
                    Expanded(
                      child: Wrap(
                        alignment: WrapAlignment.center,
                        spacing: 8,
                        runSpacing: 8,
                        children: [
                          _TopNavButton(
                            label: 'Home',
                            active: true,
                            onTap: onHomeTap,
                          ),
                          _TopNavButton(
                            label: 'Features',
                            onTap: onFeaturesTap,
                          ),
                          _TopNavButton(
                            label: 'Recipes',
                            onTap: onRecipesTap,
                          ),
                          _TopNavButton(
                            label: 'Pricing',
                            onTap: onPricingTap,
                          ),
                        ],
                      ),
                    ),
                  ] else
                    const Spacer(),

                  const SizedBox(width: 16),

                  // Actions
                  Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      TextButton(
                        style: TextButton.styleFrom(
                          foregroundColor: SfColors.brownLabel,
                          textStyle: const TextStyle(
                            fontWeight: FontWeight.w600,
                          ),
                        ),
                        onPressed: () {},
                        child: const Text('Sign in'),
                      ),
                      const SizedBox(width: 8),
                      FilledButton(
                        style: FilledButton.styleFrom(
                          backgroundColor: SfColors.brown,
                          foregroundColor: Colors.white,
                          padding: const EdgeInsets.symmetric(
                            horizontal: 22,
                            vertical: 14,
                          ),
                        ),
                        onPressed: () {},
                        child: const Text('Get started'),
                      ),
                    ],
                  ),
                ],
              );
            },
          ),
        ),
      ),
    );
  }
}

/**
 * Top navigation text button.
 *
 * Official reference:
 * TextButton: https://api.flutter.dev/flutter/material/TextButton-class.html
 */
class _TopNavButton extends StatelessWidget {
  const _TopNavButton({
    required this.label,
    required this.onTap,
    this.active = false,
  });

  final String label;
  final VoidCallback onTap;
  final bool active;

  @override
  Widget build(BuildContext context) {
    return TextButton(
      onPressed: onTap,
      style: TextButton.styleFrom(
        foregroundColor: active ? SfColors.brown : SfColors.brownMuted,
        textStyle: const TextStyle(fontWeight: FontWeight.w600),
      ),
      child: Text(label),
    );
  }
}

/**
 * Onboarding progress bar.
 * <p>
 * Custom three-step progress UI for this flow.
 *
 * Official references:
 * Divider: https://api.flutter.dev/flutter/material/Divider-class.html
 *
 * Open-source reference:
 * material_3_demo: https://github.com/flutter/samples/tree/main/material_3_demo
 */
class _OnboardingProgress extends StatelessWidget {
  const _OnboardingProgress({required this.activeIndex});

  final int activeIndex;

  static const List<String> _labels = ['Welcome', 'Preferences', 'Done'];

  @override
  Widget build(BuildContext context) {
    return Center(
      child: ConstrainedBox(
        constraints: const BoxConstraints(maxWidth: 560),
        child: Row(
          children: [
            for (var i = 0; i < _labels.length; i++) ...[
              Expanded(
                child: _ProgressStep(
                  label: _labels[i],
                  active: i == activeIndex,
                ),
              ),
              if (i != _labels.length - 1)
                Expanded(
                  child: Divider(
                    color: SfColors.stepInactive.withValues(alpha: 0.55),
                    thickness: 1,
                  ),
                ),
            ],
          ],
        ),
      ),
    );
  }
}

/** One progress step. */
class _ProgressStep extends StatelessWidget {
  const _ProgressStep({
    required this.label,
    required this.active,
  });

  final String label;
  final bool active;

  @override
  Widget build(BuildContext context) {
    final textTheme = Theme.of(context).textTheme;

    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        AnimatedContainer(
          duration: const Duration(milliseconds: 180),
          width: active ? 14 : 10,
          height: active ? 14 : 10,
          decoration: BoxDecoration(
            shape: BoxShape.circle,
            color: active ? SfColors.brown : SfColors.stepInactive,
          ),
        ),
        const SizedBox(height: 8),
        Text(
          label,
          textAlign: TextAlign.center,
          style: textTheme.labelMedium?.copyWith(
            fontWeight: active ? FontWeight.w700 : FontWeight.w500,
            color: active ? SfColors.brown : SfColors.brownMuted,
          ),
        ),
      ],
    );
  }
}
