import 'package:flutter/material.dart';

import '../app/main_shell.dart';
import '../models/health_goal.dart';
import '../services/fridge_api_service.dart';
import '../theme/smart_fridge_tokens.dart';

class PreferencesScreen extends StatefulWidget {
  const PreferencesScreen({super.key});

  @override
  State<PreferencesScreen> createState() => _PreferencesScreenState();
}

class _PreferencesScreenState extends State<PreferencesScreen> {
  HealthGoal _selected = HealthGoal.muscleBuilding;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: SfColors.cream,
      body: CustomScrollView(
        slivers: [
          SliverToBoxAdapter(child: _MarketingNavBar()),
          SliverToBoxAdapter(child: _OnboardingStepper(activeIndex: 1)),
          SliverPadding(
            padding: const EdgeInsets.fromLTRB(24, 8, 24, 32),
            sliver: SliverToBoxAdapter(
              child: Center(
                child: ConstrainedBox(
                  constraints: const BoxConstraints(maxWidth: 1120),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'STEP 2 OF 3',
                        style: TextStyle(
                          fontSize: 12,
                          letterSpacing: 1.2,
                          fontWeight: FontWeight.w600,
                          color: SfColors.brownMuted,
                        ),
                      ),
                      const SizedBox(height: 12),
                      Text(
                        "What's your health goal?",
                        style: TextStyle(
                          fontSize: 36,
                          fontWeight: FontWeight.w700,
                          height: 1.15,
                          color: SfColors.brown,
                        ),
                      ),
                      const SizedBox(height: 12),
                      Text(
                        'SmartFridge will recommend ingredients and recipes tailored to your goal. '
                        'You can change this anytime in settings.',
                        style: TextStyle(
                          fontSize: 15,
                          height: 1.45,
                          color: SfColors.brownLabel,
                        ),
                      ),
                      const SizedBox(height: 36),
                      LayoutBuilder(
                        builder: (context, c) {
                          final narrow = c.maxWidth < 880;
                          final cards = [
                            _GoalCardData(
                              goal: HealthGoal.muscleBuilding,
                              badge: 'BULK UP',
                              title: 'Muscle Building',
                              tags: const [
                                'High Protein',
                                'Complex Carbs',
                                'Recovery',
                              ],
                              description:
                                  'High-protein food recommendations optimised for post-workout recovery, '
                                  'muscle repair and lean mass growth with balanced complex carbs.',
                              icon: _GoalIconType.dumbbell,
                            ),
                            _GoalCardData(
                              goal: HealthGoal.fatLoss,
                              badge: 'CUT & LEAN',
                              title: 'Fat Loss',
                              tags: const [
                                'Low Calorie',
                                'High Satiety',
                                'Fat Control',
                              ],
                              description:
                                  'Low calorie, high-satiety ingredients first. Precise calorie tracking '
                                  'accelerates fat burning while keeping you full and energised.',
                              icon: _GoalIconType.droplet,
                            ),
                            _GoalCardData(
                              goal: HealthGoal.bloodSugarCare,
                              badge: 'DIABETES',
                              title: 'Blood Sugar Care',
                              tags: const [
                                'Low GI',
                                'Glucose Stable',
                                'High Fibre',
                              ],
                              description:
                                  'Prioritises low glycaemic index ingredients to stabilise post-meal blood sugar, '
                                  'with science-backed balanced nutrition aligned with medical advice.',
                              icon: _GoalIconType.dropletPlus,
                            ),
                          ];
                          if (narrow) {
                            return Column(
                              children: [
                                for (final data in cards) ...[
                                  _HealthGoalCard(
                                    data: data,
                                    selected: _selected == data.goal,
                                    onTap: () =>
                                        setState(() => _selected = data.goal),
                                  ),
                                  const SizedBox(height: 20),
                                ],
                              ],
                            );
                          }
                          return Row(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              for (var i = 0; i < cards.length; i++) ...[
                                Expanded(
                                  child: _HealthGoalCard(
                                    data: cards[i],
                                    selected: _selected == cards[i].goal,
                                    onTap: () => setState(
                                      () => _selected = cards[i].goal,
                                    ),
                                  ),
                                ),
                                if (i != cards.length - 1)
                                  const SizedBox(width: 20),
                              ],
                            ],
                          );
                        },
                      ),
                      const SizedBox(height: 40),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.end,
                        children: [
                          _OutlinedPillButton(
                            label: '← BACK',
                            onPressed: () {
                              Navigator.of(context).maybePop();
                            },
                          ),
                          const SizedBox(width: 16),
                          _FilledPillButton(
                            label: 'CONTINUE →',
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
                                ScaffoldMessenger.of(context).showSnackBar(
                                  SnackBar(
                                    content: Text(
                                      'Could not reach API (start Spring Boot: '
                                      './gradlew bootRun). $e',
                                    ),
                                  ),
                                );
                              }
                            },
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
    );
  }
}

enum _GoalIconType { dumbbell, droplet, dropletPlus }

class _GoalCardData {
  const _GoalCardData({
    required this.goal,
    required this.badge,
    required this.title,
    required this.tags,
    required this.description,
    required this.icon,
  });

  final HealthGoal goal;
  final String badge;
  final String title;
  final List<String> tags;
  final String description;
  final _GoalIconType icon;
}

class _HealthGoalCard extends StatelessWidget {
  const _HealthGoalCard({
    required this.data,
    required this.selected,
    required this.onTap,
  });

  final _GoalCardData data;
  final bool selected;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    final brown = SfColors.brown;
    final bg = selected ? brown : SfColors.whiteCard;
    final onPrimary = selected ? Colors.white : brown;
    final secondary = selected ? Colors.white.withValues(alpha: 0.92) : SfColors.brownLabel;
    final chipBg = selected
        ? Colors.white.withValues(alpha: 0.22)
        : SfColors.chipBgLight;
    final chipFg = selected ? Colors.white : brown;
    final iconCircle = selected
        ? Colors.white
        : SfColors.chipBgLight;
    final iconColor = selected ? brown : brown;

    return Material(
      color: Colors.transparent,
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(SfRadii.card),
        child: Ink(
          decoration: BoxDecoration(
            color: bg,
            borderRadius: BorderRadius.circular(SfRadii.card),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withValues(alpha: 0.07),
                blurRadius: 22,
                offset: const Offset(0, 10),
              ),
            ],
          ),
          child: Stack(
            clipBehavior: Clip.none,
            children: [
              if (selected)
                Positioned(
                  top: 14,
                  right: 14,
                  child: Container(
                    width: 26,
                    height: 26,
                    decoration: const BoxDecoration(
                      color: Colors.white,
                      shape: BoxShape.circle,
                    ),
                    child: Icon(Icons.check, size: 16, color: brown),
                  ),
                ),
              Padding(
                padding: const EdgeInsets.fromLTRB(20, 28, 20, 24),
                child: Column(
                  children: [
                    _GoalIconCircle(
                      type: data.icon,
                      circleColor: iconCircle,
                      iconColor: iconColor,
                    ),
                    const SizedBox(height: 16),
                    Text(
                      data.badge,
                      style: TextStyle(
                        fontSize: 11,
                        letterSpacing: 1.1,
                        fontWeight: FontWeight.w700,
                        color: onPrimary.withValues(alpha: selected ? 0.95 : 0.85),
                      ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      data.title,
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 22,
                        fontWeight: FontWeight.w700,
                        color: onPrimary,
                        height: 1.2,
                      ),
                    ),
                    const SizedBox(height: 16),
                    Wrap(
                      alignment: WrapAlignment.center,
                      spacing: 8,
                      runSpacing: 8,
                      children: data.tags
                          .map(
                            (t) => _Chip(
                              label: t,
                              background: chipBg,
                              foreground: chipFg,
                            ),
                          )
                          .toList(),
                    ),
                    const SizedBox(height: 18),
                    Text(
                      data.description,
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 13,
                        height: 1.45,
                        color: secondary,
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

class _GoalIconCircle extends StatelessWidget {
  const _GoalIconCircle({
    required this.type,
    required this.circleColor,
    required this.iconColor,
  });

  final _GoalIconType type;
  final Color circleColor;
  final Color iconColor;

  @override
  Widget build(BuildContext context) {
    Widget icon;
    switch (type) {
      case _GoalIconType.dumbbell:
        icon = Icon(Icons.fitness_center, size: 28, color: iconColor);
      case _GoalIconType.droplet:
        icon = Icon(Icons.water_drop, size: 30, color: iconColor);
      case _GoalIconType.dropletPlus:
        icon = Stack(
          alignment: Alignment.center,
          children: [
            Icon(Icons.water_drop_outlined, size: 30, color: iconColor),
            Positioned(
              right: 2,
              bottom: 4,
              child: Icon(Icons.add, size: 14, color: iconColor),
            ),
          ],
        );
    }
    return Container(
      width: 64,
      height: 64,
      decoration: BoxDecoration(color: circleColor, shape: BoxShape.circle),
      child: Center(child: icon),
    );
  }
}

class _Chip extends StatelessWidget {
  const _Chip({
    required this.label,
    required this.background,
    required this.foreground,
  });

  final String label;
  final Color background;
  final Color foreground;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
      decoration: BoxDecoration(
        color: background,
        borderRadius: BorderRadius.circular(SfRadii.pill),
      ),
      child: Text(
        label,
        style: TextStyle(
          fontSize: 11,
          fontWeight: FontWeight.w600,
          color: foreground,
        ),
      ),
    );
  }
}

class _MarketingNavBar extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 18),
      child: Center(
        child: ConstrainedBox(
          constraints: const BoxConstraints(maxWidth: 1120),
          child: Row(
            children: [
              Row(
                children: [
                  Container(
                    width: 36,
                    height: 36,
                    decoration: BoxDecoration(
                      color: SfColors.brown,
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: const Icon(Icons.kitchen, color: Colors.white, size: 20),
                  ),
                  const SizedBox(width: 10),
                  Text(
                    'SmartFridge',
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.w800,
                      color: SfColors.brown,
                    ),
                  ),
                ],
              ),
              Expanded(
                child: LayoutBuilder(
                  builder: (context, c) {
                    if (c.maxWidth < 360) {
                      return const SizedBox.shrink();
                    }
                    return Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        _NavLink(label: 'HOME', active: true),
                        _NavLink(label: 'FEATURES', active: false),
                        _NavLink(label: 'RECIPES', active: false),
                        _NavLink(label: 'PRICING', active: false),
                      ],
                    );
                  },
                ),
              ),
              Row(
                children: [
                  TextButton(
                    onPressed: () {},
                    child: Text(
                      'Sign In',
                      style: TextStyle(
                        color: SfColors.brownLabel,
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                  ),
                  const SizedBox(width: 4),
                  FilledButton(
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
                    onPressed: () {},
                    child: const Text(
                      'GET STARTED',
                      style: TextStyle(
                        fontWeight: FontWeight.w700,
                        letterSpacing: 0.6,
                        fontSize: 12,
                      ),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _NavLink extends StatelessWidget {
  const _NavLink({required this.label, required this.active});

  final String label;
  final bool active;

  @override
  Widget build(BuildContext context) {
    final color = active ? SfColors.brown : SfColors.brownMuted;
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 12),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Text(
            label,
            style: TextStyle(
              fontSize: 11,
              letterSpacing: 1.2,
              fontWeight: FontWeight.w700,
              color: color,
            ),
          ),
          const SizedBox(height: 6),
          Container(
            width: 36,
            height: 2,
            decoration: BoxDecoration(
              color: active ? SfColors.brown : Colors.transparent,
              borderRadius: BorderRadius.circular(2),
            ),
          ),
        ],
      ),
    );
  }
}

class _OnboardingStepper extends StatelessWidget {
  const _OnboardingStepper({required this.activeIndex});

  /// 0: Welcome, 1: Preferences, 2: Done
  final int activeIndex;

  @override
  Widget build(BuildContext context) {
    const labels = ['WELCOME', 'PREFERENCES', 'DONE'];
    final lineColor = SfColors.stepInactive.withValues(alpha: 0.55);
    return Padding(
      padding: const EdgeInsets.fromLTRB(24, 8, 24, 28),
      child: Center(
        child: ConstrainedBox(
          constraints: const BoxConstraints(maxWidth: 520),
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Expanded(
                child: _StepDot(
                  label: labels[0],
                  state: activeIndex == 0
                      ? _StepVisualState.active
                      : _StepVisualState.inactive,
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(top: 6),
                child: Container(width: 48, height: 2, color: lineColor),
              ),
              Expanded(
                child: _StepDot(
                  label: labels[1],
                  state: activeIndex == 1
                      ? _StepVisualState.active
                      : _StepVisualState.inactive,
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(top: 6),
                child: Container(width: 48, height: 2, color: lineColor),
              ),
              Expanded(
                child: _StepDot(
                  label: labels[2],
                  state: activeIndex == 2
                      ? _StepVisualState.active
                      : _StepVisualState.inactive,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

enum _StepVisualState { active, inactive }

class _StepDot extends StatelessWidget {
  const _StepDot({required this.label, required this.state});

  final String label;
  final _StepVisualState state;

  @override
  Widget build(BuildContext context) {
    final active = state == _StepVisualState.active;
    return Column(
      children: [
        Container(
          width: active ? 14 : 8,
          height: active ? 14 : 8,
          decoration: BoxDecoration(
            shape: BoxShape.circle,
            color: active ? SfColors.brown : SfColors.stepInactive,
          ),
        ),
        const SizedBox(height: 8),
        Text(
          label,
          textAlign: TextAlign.center,
          style: TextStyle(
            fontSize: 10,
            letterSpacing: 0.8,
            fontWeight: active ? FontWeight.w800 : FontWeight.w600,
            color: active ? SfColors.brown : SfColors.brownMuted,
          ),
        ),
      ],
    );
  }
}

class _OutlinedPillButton extends StatelessWidget {
  const _OutlinedPillButton({required this.label, required this.onPressed});

  final String label;
  final VoidCallback onPressed;

  @override
  Widget build(BuildContext context) {
    return OutlinedButton(
      style: OutlinedButton.styleFrom(
        foregroundColor: SfColors.brown,
        side: const BorderSide(color: SfColors.brown, width: 1.2),
        padding: const EdgeInsets.symmetric(horizontal: 22, vertical: 14),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(SfRadii.pill),
        ),
      ),
      onPressed: onPressed,
      child: Text(label, style: const TextStyle(fontWeight: FontWeight.w700)),
    );
  }
}

class _FilledPillButton extends StatelessWidget {
  const _FilledPillButton({required this.label, required this.onPressed});

  final String label;
  final VoidCallback onPressed;

  @override
  Widget build(BuildContext context) {
    return FilledButton(
      style: FilledButton.styleFrom(
        backgroundColor: SfColors.brown,
        foregroundColor: Colors.white,
        padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 14),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(SfRadii.pill),
        ),
      ),
      onPressed: onPressed,
      child: Text(label, style: const TextStyle(fontWeight: FontWeight.w700)),
    );
  }
}
